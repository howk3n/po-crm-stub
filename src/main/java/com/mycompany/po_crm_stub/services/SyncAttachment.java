/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.po_crm_stub.services;

import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import org.json.JSONException;
import org.json.JSONObject;

import com.mycompany.po_crm_stub.authentication.AuthenticationFailedException;
import com.mycompany.po_crm_stub.authentication.AuthenticationManager;
import com.mycompany.po_crm_stub.models.Attachment;
import com.mycompany.po_crm_stub.models.Email;

@Path("/syncAttachment/")
public class SyncAttachment {

    @Context
    private UriInfo context;
    
    @Context
    private ServletContext sContext; 
    
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String syncAttachment(String jsonString) throws JSONException{
        try{
            
            JSONObject jRequest = new JSONObject(jsonString);

            if(jRequest.keySet().size() != 6 || !jRequest.has("emailId") || !jRequest.has("ord") || !jRequest.has("fileName") || !jRequest.has("fileContent") || !jRequest.has("username")  || !jRequest.has("signature") ){
                return "{\"status\":\"400\",\"message\":\"Bad request.\"}";
            }
            
            (new AuthenticationManager()).authenticate(jRequest, jsonString);
            
            int emailId = jRequest.getInt("emailId");
            int ord = jRequest.getInt("ord");
            String fileName = jRequest.getString("fileName");
            String fileContent = jRequest.getString("fileContent");

            Email email = Email.findByEmailId(emailId);
            
            if(email == null){
                return "{\"status\":\"404\",\"message\":\"No matches found for requested emailId.\"}";
            }
            
            Integer attachmentId = Attachment.insert(email, ord, fileName, fileContent);
            
            if(attachmentId == null){
                return "{\"status\":\"500\",\"message\":\"Database error.\"}";
            }
            
            JSONObject jResponse = new JSONObject();

            jResponse.put("status", "200");
            jResponse.put("message", "");
            jResponse.put("attachmentId", attachmentId);

            return jResponse.toString();
            
        }catch(AuthenticationFailedException e){
            return "{\"status\":\"403\",\"message\":\"" + e.getMessage() + "\"}";
        }catch(DuplicateResourceException e){
            return "{\"status\":\"409\",\"message\":\"" + e.getMessage() + "\"}";
        }catch(JSONException e){
            return "{\"status\":\"400\",\"message\":\"Bad request.\"}";
        }catch(Exception e){
            e.printStackTrace();
            return "{\"status\":\"500\",\"message\":\"" + e.getMessage() + "\"}";
        }
    }
}
