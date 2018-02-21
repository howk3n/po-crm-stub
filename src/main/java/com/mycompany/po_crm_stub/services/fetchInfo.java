/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.po_crm_stub.services;

import java.util.List;
import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import com.mycompany.po_crm_stub.models.Customer;
import com.mycompany.po_crm_stub.models.Opportunity;
import org.json.*;

@Path("/fetchInfo/")
public class fetchInfo {

    @Context
    private UriInfo context;
    
    @Context
    private ServletContext sContext; 
    
    @POST
//    @Produces(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String fetch(String jsonString) throws JSONException{

        JSONObject jRequest = new JSONObject(jsonString);
        JSONArray addresses = jRequest.getJSONArray("addresses");
        Customer customer = null;
        for(int i = 0; i < addresses.length(); i++){
            customer = Customer.selectQuery(addresses.getString(i));
            
            if(customer != null){
                break;
            }
            
        }
        if(customer == null){
            return "{\"status\": \"ERROR 404\",\"message\":\"No customers found for given addresses.\"}";
        }
        List<Opportunity> opportunities = Opportunity.selectQuery(customer.getId());
        
        JSONObject jResponse = new JSONObject();
        
        JSONArray opportunitiesArray = null;
        if(!opportunities.isEmpty()){
            opportunitiesArray = new JSONArray();
            JSONObject opportunityObject;
            for(int i = 0; i < opportunities.size(); i++){
                Opportunity opportunity = opportunities.get(i);
                opportunityObject = new JSONObject();
                
                opportunityObject.put("amount", opportunity.getAmount());
                opportunityObject.put("date", opportunity.getDate());
                opportunityObject.put("status", opportunity.getStatus());
                opportunitiesArray.put(opportunityObject);
            }
        }
        
        jResponse.put("customerId", customer.getId());
        jResponse.put("customerName", customer.getName());        
        jResponse.put("opportunities", opportunitiesArray);
           
        return jResponse.toString();
        
    }
}
