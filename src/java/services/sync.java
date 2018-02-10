/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import models.Email;
import org.json.*;

@Path("/sync/")
public class sync {

    @Context
    private UriInfo context;
    
    @Context
    private ServletContext sContext; 
    
    @POST
//    @Produces(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String sync(String jsonString) throws JSONException{
        
//        example request:
//{
//	"messages": [
//		{
//			"sender": "djordjec@gmail.com",
//			"recipient": [
//                              "dulbec@gmail.com",
//                              "custB@gmail.com",
//                      ],
//			"subject": "A business offer...",
//			"body": "something something dark side",
//			"date": "2018-02-07 00:00:00"
//		},
//		{
//			"sender": "custB@gmail.com",
//			"recipient": [
//                          "djordjec@gmail.com",
//                          "dublec@gmail.com"
//                      ],
//			"subject": "RE: A business offer...",
//			"body": "something something cookies",
//			"date": "2018-02-08 12:00:00"
//		}
//	],
//	"username": "djordjec",
//	"signature": "028ba9af611925a0064012a6e6fd765b"
//}
//
//example response:
//{
//	"status": "OK",
//	"message": ""
//}
        

        JSONObject jRequest = new JSONObject(jsonString);
        JSONArray messages = jRequest.getJSONArray("messages");
        
        if(messages.length()==0 || messages.isNull(0)){
            return "{\"status\": \"ERROR 400\",\"message\":\"No messages recieved.\"}";
        }
        
//        Array of messages in request
        ArrayList<HelperMail> mails = new ArrayList<HelperMail>();
        
//        adds each message in request into the ArrayList<HelperMail> mails
        for(int i = 0; i < messages.length(); i++){
            JSONObject message = messages.getJSONObject(i);
            ArrayList<String> recipientsList = new ArrayList<String>();
            JSONArray recipients = message.getJSONArray("recipient");
            
            for(int j = 0; j < recipients.length(); j++){
                recipientsList.add(recipients.getString(j));
            }
            recipientsList.sort(String::compareTo);
            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < recipientsList.size(); j++){
                if(j != 0){
                    sb.append(", ");
                }
                sb.append(recipientsList.get(j));
            }
            mails.add(new HelperMail(message.getString("sender"), sb.toString(), message.getString("subject"), message.getString("body"), message.getString("date")));
        }
        
        int threadId = 0;
        
        ArrayList<Integer> mailsToSkip = new ArrayList<Integer>();
//        for each message in mails
        for(int i = 0; i < mails.size(); i++){
            
            HelperMail currentMail = mails.get(i);
            int newThreadId;
            try {
//                returns 0 if thread doesn't exist, and threadId if it does
                newThreadId = Email.findThread(currentMail.getSender(), currentMail.getRecipient(), currentMail.getSubject(), currentMail.getBody(), currentMail.getDate());
                if(newThreadId != 0){
                    if(newThreadId != threadId && threadId != 0){
                        return "{\"status\": \"ERROR 400\",\"message\":\"Critical error, threads of given messages not matching, please check out DB.\"}";
                    }
                    threadId = newThreadId;
                    mailsToSkip.add(i);
                }
            } catch (Exception ex) {
                return "{\"status\": \"ERROR 400\",\"message\":\"" + ex.getMessage() + "\"}";
            }
        }
        
        for(int i = 0; i < mails.size(); i++){
            
            HelperMail currentMail = mails.get(i);
            
            if(threadId == 0){
                //                create new thread, and insert all mails
                int newThreadId = 0;
                if(i == 0){
                    newThreadId = Email.insert(currentMail.getSender(), currentMail.getRecipient(), currentMail.getSubject(), currentMail.getBody(), currentMail.getDate());
                }
                else{
                    Email.insert(newThreadId, currentMail.getSender(), currentMail.getRecipient(), currentMail.getSubject(), currentMail.getBody(), currentMail.getDate());
                }
                
            }
            else{
//                use threadIdMatch to update all mails except for mailsIdMatched with threadId
                if(!mailsToSkip.contains(i)){
                    Email.insert(threadId, currentMail.getSender(), currentMail.getRecipient(), currentMail.getSubject(), currentMail.getBody(), currentMail.getDate());
                }
            }
            
        }
        
        return "{\"status\": \"OK\",\"message\":\"\"}";
        
    }
}
