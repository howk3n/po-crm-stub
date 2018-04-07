/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.po_crm_stub.authentication;

import org.json.JSONObject;

public class SyncAttachmentAuthenticationManager extends AuthenticationManager{

    @Override
    protected String prepJsonForSigning(JSONObject jRequest) {
		StringBuilder s = new StringBuilder();
    	
    	s.append("{");
    	
    	s.append("\"emailId\":");
    	s.append("\"" + jRequest.getString("emailId") + "\",");
    	
    	s.append("\"ord\":");
    	s.append("\"" + jRequest.getInt("ord") + "\",");
    	
    	s.append("\"fileName\":");
    	s.append("\"" + jRequest.getString("fileName") + "\",");
    	
    	s.append("\"fileContent\":");
    	s.append("\"" + jRequest.getString("fileContent") + "\",");
        
        s.append("\"username\":");
        s.append("\"" + jRequest.getString("username") + "\"}");

//      System.out.println(s.toString());
        return s.toString();
    }
    
}
