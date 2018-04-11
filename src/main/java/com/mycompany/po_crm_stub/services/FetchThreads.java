/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.po_crm_stub.services;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
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
    public String fetch(@QueryParam("customerId") Integer customerId) throws JSONException{
//http://localhost:8084/PO_CRM_stub/api/fetchThreads?customerId=4

        StringBuilder s = new StringBuilder();
        
        s.append("<div style = 'margin:10px;'>");
        s.append("Select customer:<form><select name=\"customerId\" onchange=\"this.form.submit();\"><option value = \"0\"></option>");
        List<Customer> customerList = Customer.selectAll();
        for(int i = 0; i < customerList.size(); i++){
            Customer currentCustomer = customerList.get(i);
            if(customerId == currentCustomer.getId()){
                s.append("<option value = \"").append(currentCustomer.getId()).append("\" selected>").append(currentCustomer.getName()).append("</option>");
            }
            else{
                s.append("<option value = \"").append(currentCustomer.getId()).append("\">").append(currentCustomer.getName()).append("</option>");
            }
        }
        s.append("</select></form><br />");
            
        if(customerId != null){
            List<Thread> threads = Thread.selectThreads(customerId);
            if(threads != null){
                ArrayList<List<Email>> mailThreads = new ArrayList();
            
                for(int i = 0; i < threads.size(); i++){
                    mailThreads.add(Email.findByThreadId(threads.get(i).getId()));
                }
                System.out.println("Number of threads: " + mailThreads.size());
                for(int i = 0; i < mailThreads.size(); i++){
                    List<Email> currentThread = mailThreads.get(i);
                    int currentThreadId = currentThread.get(0).getThreadId().getId();
                    s.append("<span id = 'thread-").append(currentThreadId).append("' style='text-decoration:underline;font-weight:bold;'>").append(currentThread.get(0).getSubject()).append("</span><br><br>");
                    for(int j = 0; j < currentThread.size(); j++){
                    	System.out.println("currentThread size: " + currentThread.size());
                        Email currentMail = currentThread.get(j);
                        s.append("<div style = 'border:solid 1px black; max-width:1024px; padding:15px; margin-bottom:10px;'>");
                        s.append("From: ").append(currentMail.getSender()).append("<br>");
                        s.append("To: ").append(currentMail.getRecipient()).append("<br>");
                        s.append("<br><div style='background-color:#eee;'>").append(currentMail.getBody()).append("</div><br>");
                        s.append("<div>Attachments: <br>");
                        System.out.println(Email.findByEmailId(currentMail.getId()));
                        Email persistedEmail = Email.findByEmailId(currentMail.getId().intValue());
                        if(persistedEmail == null) {
                        	System.out.println("Mail from database is null");
                        }
                        else {
                        	System.out.println(persistedEmail.getBody());
                        }
                        List<Attachment> attachments = Attachment.findByEmail(persistedEmail);
                        if(attachments == null) {
                        	System.out.println("None found");
                        }
                        else if(!attachments.isEmpty()) {
                        	System.out.println(attachments.size());
                        	for(int k = 0; k < attachments.size(); k++) {
                        		s.append("<a href=\"downloadAttachment/" + attachments.get(k).getId() + "\">" + attachments.get(k).getFileName() + " </a><br>");
                        	}
                        	
                        }else {
                        	System.out.println("Empty");
                        }
                        
//                        ArrayList<Attachment> attachments = new ArrayList<Attachment>((Email.findByEmailId(currentMail.getId().intValue())).getAttachmentCollection());
//                        for(int k = 0; k < attachments.size(); k++) {
//                        	s.append(attachments.get(k).getFileName());
//                        }
                        s.append("</div><br>");
                        s.append("Sent: ").append(currentMail.getDate()).append("</div>");
                    }
                }
                s.append("</div>");
            }
            
        }
       
        return s.toString();

    }
}
