/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.po_crm_stub.services;

import java.util.List;
import javax.servlet.ServletContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Path;
import com.mycompany.po_crm_stub.models.Email;
import com.mycompany.po_crm_stub.models.Thread;
import java.util.ArrayList;
import javax.ws.rs.GET;
import javax.ws.rs.PathParam;
import org.json.*;

@Path("/fetchThreads/")
public class fetchThreads {

    @Context
    private UriInfo context;
    
    @Context
    private ServletContext sContext; 
    
    @GET
    @Path("/{customerId}")
    public String fetch(@PathParam("customerId") Integer customerId) throws JSONException{

        List<Thread> threads = Thread.selectThreads(customerId);
        
        if(threads.isEmpty()){
            return "No threads";
        }
        ArrayList<List<Email>> mailThreads = new ArrayList();
        StringBuilder s = new StringBuilder();
        for(int i = 0; i < threads.size(); i++){
            mailThreads.add(Email.selectQuery(threads.get(i).getId()));
        }
        s.append("<div style = 'margin:10px;'>");
        for(int i = 0; i < mailThreads.size(); i++){
            List<Email> currentThread = mailThreads.get(i);
            s.append("<span style='text-decoration:underline;font-weight:bold;'>Thread ").append(currentThread.get(0).getThreadId()).append("</span><br><br>");
            for(int j = 0; j < currentThread.size(); j++){
                Email currentMail = currentThread.get(j);
                s.append("<div style = 'border:solid 1px black; max-width:1024px; padding:15px; margin-bottom:10px;'>");
                s.append("Thread ID: ").append(currentMail.getThreadId()).append("<br>");
                s.append("Sender: ").append(currentMail.getSender()).append("<br>");
                s.append("Recipient: ").append(currentMail.getRecipient()).append("<br>");
                s.append("Subject: ").append(currentMail.getSubject()).append("<br>");
                s.append("Body: ").append(currentMail.getBody()).append("<br>");
                s.append("Date: ").append(currentMail.getDate()).append("</div>");
            }
        }
        s.append("</div>");
        return s.toString();

    }
}
