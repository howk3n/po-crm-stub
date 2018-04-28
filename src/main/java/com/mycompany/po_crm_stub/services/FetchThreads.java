/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.po_crm_stub.services;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.json.JSONException;

import com.mycompany.po_crm_stub.models.Attachment;
import com.mycompany.po_crm_stub.models.Customer;
import com.mycompany.po_crm_stub.models.Email;
import com.mycompany.po_crm_stub.models.Thread;

@Path("/fetchThreads/")
public class FetchThreads {

    @Context
    private UriInfo context;
    
    @Context
    private ServletContext sContext; 
    
    @GET
    public Response fetch(@QueryParam("customerId") Integer customerId) throws JSONException{
//http://localhost:8084/PO_CRM_stub/api/fetchThreads?customerId=4

        StringBuilder s = new StringBuilder();
        
        s.append("<div style = 'margin:10px;'>");
        s.append("Select customer:<form><select name=\"customerId\" onchange=\"this.form.submit();\"><option value = \"0\"></option>");
        List<Customer> customerList = Customer.selectAll();
        boolean found = false;
        for(int i = 0; i < customerList.size(); i++){
            Customer currentCustomer = customerList.get(i);
            s.append("<option value = \"").append(currentCustomer.getId()).append("\"");
            if(customerId == currentCustomer.getId()){
                s.append(" selected");
                found = true;
            }
            s.append(">").append(currentCustomer.getName()).append("</option>");
        }
        s.append("</select></form><br />");
            
        if(customerId != null){
        	
        	if (!found) {
        		return Response.status(Status.NOT_FOUND).build();
        	}
        	
            List<Thread> threads = Thread.selectThreads(customerId);
            if(threads != null){
                ArrayList<List<Email>> mailThreads = new ArrayList<List<Email>>();
            
                for(int i = 0; i < threads.size(); i++){
                    mailThreads.add(Email.findByThreadId(threads.get(i).getId()));
                }
                for(int i = 0; i < mailThreads.size(); i++){
                    List<Email> currentThread = mailThreads.get(i);
                    int currentThreadId = currentThread.get(0).getThreadId().getId();
                    s.append("<span id = 'thread-").append(currentThreadId).append("' style='text-decoration:underline;font-weight:bold;'>").append("Subject: ").append(currentThread.get(0).getSubject()).append("</span><br><br>");
                    for(int j = 0; j < currentThread.size(); j++){
                        Email currentMail = currentThread.get(j);
                        s.append("<div style = 'border:solid 1px black; max-width:1024px; padding:15px; margin-bottom:10px;'>");
                        s.append("From: ").append(currentMail.getSender()).append("<br>");
                        s.append("To: ").append(currentMail.getRecipient()).append("<br>");
                        s.append("<br><div style='background-color:#eee;'>").append(currentMail.getBody()).append("</div><br>");
                        
                        Email persistedEmail = Email.findByEmailId(currentMail.getId().intValue());
                        List<Attachment> attachments = Attachment.findByEmail(persistedEmail);
                        
                        if(attachments == null) {}
                        else if(!attachments.isEmpty()) {
                        	s.append("<div>Attachments: <br>");
                        	for(int k = 0; k < attachments.size(); k++) {
                        		s.append("<a href=\"downloadAttachment/" + attachments.get(k).getId() + "\">" + attachments.get(k).getFileName() + "</a><br>");
                        	}
                        	s.append("</div><br>");
                        	
                        }
                        
                        SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
                        String date = sdf.format(currentMail.getDate());
                        s.append("Sent: ").append(date).append("</div>");
                    }
                }
                s.append("</div>");
            }
            
        }
       
        return Response.ok(s.toString()).build();

    }
}
