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


@Path("/login/")
public class Login {

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
            
            (new AuthenticationManager()).authenticate(jRequest, jsonString);
            return "{\"status\":\"200\",\"message\":\"\"}";
            
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
