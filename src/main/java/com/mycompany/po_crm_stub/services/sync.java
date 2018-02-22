/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.po_crm_stub.services;

import com.mycompany.po_crm_stub.models.Customer;
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
import com.mycompany.po_crm_stub.models.Email;
import com.mycompany.po_crm_stub.models.Thread;
import org.json.*;



@Path("/sync/")
public class sync {

    @Context
    private UriInfo context;
    
    @Context
    private ServletContext sContext; 
    
    private static Date parseDate(String dateStr){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = sdf.parse(dateStr);
        } catch (ParseException ex) {
            Logger.getLogger(Email.class.getName()).log(Level.SEVERE, null, ex);
        }
        return date;
    }
    
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String sync(String jsonString) throws JSONException{
        
//      variables needed to return
        int requested;
        int inserted = 0;
        int threadId;
        ArrayList<Integer> emailId = new ArrayList<>();
//        

        JSONObject jRequest = new JSONObject(jsonString);
        
        if(jRequest.keySet().size() != 3 || !jRequest.has("messages") || !jRequest.has("username") || !jRequest.has("signature")){
            return "{\"status\": \"400\",\"message\":\"Bad request.\"}";
        }
        
        JSONArray messages = jRequest.getJSONArray("messages");
        requested = messages.length();
        
        if(requested == 0 || messages.isNull(0)){
            return "{\"status\": \"ERROR 400\",\"message\":\"No messages recieved in request.\"}";
        }
        for(int i = 0; i < messages.length(); i++){
            JSONObject message = messages.getJSONObject(i);
            if(message.keySet().size() != 5 || !message.has("sender") || !message.has("recipient") || !message.has("subject") || !message.has("body") || !message.has("date")){
                return "{\"status\": \"400\",\"message\":\"Bad request.\"}";
            }
        }
        
//        Array of messages in request
        ArrayList<Email> mails = new ArrayList<>();
//        only 1 customer in a thread
        Customer customer = null;
//        adds each message in request into the ArrayList<Email> mails
        for(int i = 0; i < requested; i++){
            JSONObject message = messages.getJSONObject(i);
            ArrayList<String> recipientsList = new ArrayList<>();
            JSONArray recipients = message.getJSONArray("recipient");
            
            for(int j = 0; j < recipients.length(); j++){
                recipientsList.add(recipients.getString(j));
            }
            recipientsList.sort(String.CASE_INSENSITIVE_ORDER);
            
            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < recipientsList.size(); j++){
                if(j != 0){
                    sb.append(", ");
                }
                sb.append(recipientsList.get(j));
            }
            
            mails.add(new Email(message.getString("sender"), sb.toString(), message.getString("subject"), message.getString("body"), parseDate(message.getString("date"))));
        }
        
        threadId = 0;
        
        ArrayList<Integer> mailsToSkip = new ArrayList<>();
//      finds the threadId and customer
        for(int i = 0; i < mails.size(); i++){
            
            Email currentMail = mails.get(i);
            Thread newThread = null;
            if(customer == null){
                customer = Customer.selectQuery(currentMail.getSender());
            }
            
            try {
//                returns null if thread doesn't exist, and threadId if it does
                newThread = Email.findThread(currentMail.getSender(), currentMail.getRecipient(), currentMail.getSubject(), currentMail.getBody(), currentMail.getDate());
                if(newThread != null){
                    if(newThread.getId() != threadId && threadId != 0){
                        return "{\"status\": \"ERROR 400\",\"message\":\"Critical error; threads of given messages not matching.\"}";
                    }
                    threadId = newThread.getId();
                    mailsToSkip.add(i);
                }
            } catch (Exception ex) {
                return "{\"status\": \"ERROR 500\",\"message\":\"" + ex.getMessage() + "\"}";
            }
        }
        
        if(customer == null){
            return "{\"status\": \"ERROR 400\",\"message\":\"There seems to be no customer in the messages requested.\"}";
        }
//      Inserts all mails
        
        Thread thread;
        for(int i = 0; i < mails.size(); i++){
            
            if(mailsToSkip.contains(i)){
                continue;
            }
            
            int mailId;
            Email currentMail = mails.get(i);
            Email newMail = null;
            if(threadId == 0){
                thread = Thread.insertThread(customer);
                threadId = thread.getId();
            }
            else{
                thread = Thread.findThreadByThreadId(threadId);
            }
            newMail = Email.insert(currentMail.getSender(), currentMail.getRecipient(), thread, currentMail.getSubject(), currentMail.getBody(), currentMail.getDate());
            inserted++;
            emailId.add(newMail.getId());
        }
        
        JSONObject jResponse = new JSONObject();
        
        jResponse.put("requested", requested);
        jResponse.put("inserted", inserted);
        jResponse.put("threadId", threadId);
        JSONArray emailIdArray = new JSONArray(emailId);
        jResponse.put("emailId", emailIdArray);
           
        return jResponse.toString();
        
    }
}
