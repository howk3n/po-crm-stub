/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.po_crm_stub.services;


import com.mycompany.po_crm_stub.models.Rep;
import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import org.json.*;



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
        
        JSONObject jRequest = new JSONObject(jsonString);
        if(jRequest.keySet().size() != 2 || !jRequest.has("username") || !jRequest.has("signature")){
            return "{\"status\": \"400\",\"message\":\"Bad request.\"}";
        }
        String username = jRequest.getString("username");
        String signature = jRequest.getString("signature");
        Rep rep = Rep.findRepByUsername(username);
        if(rep == null){
            return "{\"status\": \"ERROR 403\",\"message\":\"Forbidden.\"}";
        }
        return "{\"status\": \"OK\",\"message\":\"\"}";
        }
}
