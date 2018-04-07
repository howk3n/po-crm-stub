/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.po_crm_stub.authentication;

import org.json.JSONArray;
import org.json.JSONObject;

public class SyncAuthenticationManager extends AuthenticationManager{

    @Override
    protected String prepJsonForSigning(JSONObject jRequest) {
    	StringBuilder s = new StringBuilder();
        
        s.append("{\"messages\":[");
        JSONArray messages = jRequest.getJSONArray("messages");
        
        for(int i = 0; i < messages.length(); i++) {
        	
        	JSONObject c = messages.getJSONObject(i);
        	
        	s.append("{");
        	
        	s.append("\"sender\":");
        	s.append("\"" + c.getString("sender") + "\",");
        	
        	JSONArray recipient = c.getJSONArray("recipient");
        	s.append("\"recipient\":");
        	s.append(recipient.toString());
        	s.append(",");
        	
        	s.append("\"subject\":");
        	s.append("\"" + c.getString("subject") + "\",");
        	
        	s.append("\"body\":");
        	s.append("\"" + c.getString("body") + "\",");

        	s.append("\"date\":");
        	s.append("\"" + c.getString("date") + "\"");
        	
        	s.append("}");
        	if(i < messages.length() - 1) {
        		s.append(",");
        	}
        	
        }
        
        s.append("],\"username\":");
        s.append("\"" + jRequest.getString("username") + "\"}");

//      System.out.println(s.toString());
        return s.toString();
    }
    
}
