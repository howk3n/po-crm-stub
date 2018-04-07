/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.po_crm_stub.authentication;

import org.json.JSONObject;

public class LoginAuthenticationManager extends AuthenticationManager{

    @Override
    protected String prepJsonForSigning(JSONObject jRequest) {

        StringBuilder s = new StringBuilder();
        
        s.append("{\"username\":");
        s.append("\"" + jRequest.getString("username") + "\"}");
        
//      System.out.println(s.toString());
        return s.toString();
    }
    
}
