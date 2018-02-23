/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.po_crm_stub.services;


import com.mycompany.po_crm_stub.models.Rep;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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
import org.json.*;
import javax.xml.bind.DatatypeConverter;



@Path("/login/")
public class login {

    @Context
    private UriInfo context;
    
    @Context
    private ServletContext sContext;
    
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String login(String jsonString) throws JSONException{
        try{
            JSONObject jRequest = new JSONObject(jsonString);
            if(jRequest.keySet().size() != 2 || !jRequest.has("username") || !jRequest.has("signature")){
                return "{\"status\":\"400\",\"message\":\"Bad request.\"}";
            }

            AuthenticationManager.authenticate(jRequest);
            return "{\"status\":\"200\",\"message\":\"\"}";
            
        }catch(AuthenticationFailedException ex){
            return "{\"status\":\"403\",\"message\":\"" + ex.getMessage() + "\"}";
        }catch(Exception e){
            return "{\"status\":\"500\",\"message\":\"" + e.getMessage() + "\"}";
        }
        
    }
}
