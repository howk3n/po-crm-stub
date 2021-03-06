/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.po_crm_stub.services;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mycompany.po_crm_stub.authentication.AuthenticationFailedException;
import com.mycompany.po_crm_stub.authentication.AuthenticationManager;
import com.mycompany.po_crm_stub.models.Customer;
import com.mycompany.po_crm_stub.models.Email;
import com.mycompany.po_crm_stub.models.Thread;


@Path("/sync/")
public class Sync {

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
        try{
    //      variables needed to return
            int requested;
            int inserted = 0;
            int threadId;
            ArrayList<Integer> emailId = new ArrayList<>();

            JSONObject jRequest = new JSONObject(jsonString);
            

            if(jRequest.keySet().size() != 3 || !jRequest.has("messages") || !jRequest.has("username") || !jRequest.has("signature")){
                return "{\"status\":\"400\",\"message\":\"Bad request.\"}";
            }
            JSONArray messages = jRequest.getJSONArray("messages");
            requested = messages.length();
            for(int i = 0; i < messages.length(); i++){
                JSONObject message = messages.getJSONObject(i);
                if(message.keySet().size() != 5 || !message.has("sender") || !message.has("recipient") || !message.has("subject") || !message.has("body") || !message.has("date")){
                    return "{\"status\":\"400\",\"message\":\"Bad request.\"}";
                }
            }
            
            if(requested == 0 || messages.isNull(0)){
                return "{\"status\":\"400\",\"message\":\"No messages recieved in request.\"}";
            }
            
            (new AuthenticationManager()).authenticate(jRequest, jsonString);

            String username = jRequest.getString("username");
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
                    customer = Customer.selectQuery(currentMail.getSender(), username);
                }

    //          returns null if thread doesn't exist
                newThread = Email.findThread(currentMail.getSender(), currentMail.getRecipient(), currentMail.getSubject(), currentMail.getBody(), currentMail.getDate());
                if(newThread != null){
                    if(newThread.getId() != threadId && threadId != 0){
                        return "{\"status\":\"400\",\"message\":\"Requested messages are parts of different threads.\"}";
                    }
                    threadId = newThread.getId();
                    mailsToSkip.add(i);
                }

            }
            
            if(customer == null) {
            	for(int i = 0; i < mails.size(); i++) {
//            		System.out.println("i Itteration #" + i);
            		Email currentMail = mails.get(i);
            		String[] recipients = currentMail.getRecipient().split(",");
            		for(int j = 0; j < recipients.length; j++) {
//            			System.out.println("j Itteration #" + j);
//            			System.out.println(j + " recipient " + recipients[j]);
            			customer = Customer.selectQuery(recipients[j].trim(), username);
            			if(customer != null) {
//            				System.out.println("Break from inner cycle (j)");
            				break;
            			}
            		}
            		if(customer != null) {
//            			System.out.println("Break from outer cycle (i)");
            			break;
            		}
            		
            	}
            }
//            System.out.println(customer.getName());

            if(customer == null){
                return "{\"status\":\"400\",\"message\":\"There is no customer in the messages requested.\"}";
            }
            
    //      Inserts all mails
            Thread thread;
        	for(int i = 0; i < mails.size(); i++){
        	
        		Email currentMail = mails.get(i);
        		Email persistedMail = null;
        		
                if(mailsToSkip.contains(i)){
                	persistedMail = Email.findEmail(currentMail.getSender(), currentMail.getRecipient(), currentMail.getSubject(), currentMail.getBody(), currentMail.getDate());
                }
                else {
                	if(threadId == 0){
                        thread = Thread.insertThread(customer);
                        threadId = thread.getId();
                    }
                    else{
                        thread = Thread.findThreadByThreadId(threadId);
                    }
                    persistedMail = Email.insert(currentMail.getSender(), currentMail.getRecipient(), thread, currentMail.getSubject(), currentMail.getBody(), currentMail.getDate());
                    inserted++;
                }
                emailId.add(persistedMail.getId());
                
            }
            
            JSONObject jResponse = new JSONObject();

            jResponse.put("requested", requested);
            jResponse.put("inserted", inserted);
            jResponse.put("threadId", threadId);
            JSONArray emailIdArray = new JSONArray(emailId);
            jResponse.put("emailId", emailIdArray);

            return jResponse.toString();
            
        }catch(AuthenticationFailedException e){
            return "{\"status\":\"403\",\"message\":\"" + e.getMessage() + "\"}";
        }catch(JSONException e){
            return "{\"status\":\"400\",\"message\":\"Bad request.\"}";
        }catch(Exception e){
            e.printStackTrace();
            return "{\"status\":\"500\",\"message\":\"" + e.getMessage() + "\"}";
        }
        
    }
}
