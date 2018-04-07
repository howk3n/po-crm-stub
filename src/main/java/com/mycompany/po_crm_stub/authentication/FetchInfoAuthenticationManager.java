/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.po_crm_stub.authentication;

import org.json.JSONArray;
import org.json.JSONObject;

public class FetchInfoAuthenticationManager extends AuthenticationManager{

    @Override
    protected String prepJsonForSigning(JSONObject jRequest) {
    	
        StringBuilder s = new StringBuilder();
        
        s.append("{\"addresses\":[");
        JSONArray addresses = jRequest.getJSONArray("addresses");
        
        for(int i = 0; i < addresses.length(); i++) {
        	s.append("\"" + addresses.getString(i) + "\"");
        	if(i < addresses.length() - 1) {
        		s.append(",");
        	}
        }
        
        s.append("],\"username\":");
        s.append("\"" + jRequest.getString("username") + "\"}");
        
//        System.out.println(s.toString());
        return s.toString();
    }
    
}
