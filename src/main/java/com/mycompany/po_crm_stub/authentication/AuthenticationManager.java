package com.mycompany.po_crm_stub.authentication;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

import javax.xml.bind.DatatypeConverter;

import org.json.JSONObject;

import com.mycompany.po_crm_stub.models.Rep;

public class AuthenticationManager {
    
    public void authenticate(JSONObject jRequest, String jsonString) throws Exception{
        
//    	System.out.println(jRequest.toString());
    	
        Rep rep = Rep.findRepByUsername(jRequest.getString("username"));
        if(rep == null){
        	System.out.println("Rep not found: " + jRequest.getString("username"));
            throw new AuthenticationFailedException("Forbidden.");
        }
        
        String signature = jRequest.getString("signature");
        
//      TODO: remove master sig
        String masterSig = "E25FEE9B30162EA31A5498F3B61F8CC5AB29667F8736F4E7229D0A11AC059B69";
        if(signature.equals(masterSig)){
            return;
        }
//        
        
//        jRequest.remove("signature");
        
//        String prep = jRequest.toString() + rep.getPassword();
//        String prep = prepJsonForSigning(jRequest) + rep.getPassword();
        String prep = jsonString.replaceAll(",\"signature\":\"" + signature + "\"", "");
        prep = prep + rep.getPassword();
        
        System.out.println(prep);
        MessageDigest digest;
        
        digest = MessageDigest.getInstance("SHA-256");
        
        String hashString = DatatypeConverter.printHexBinary(digest.digest(prep.getBytes(StandardCharsets.UTF_8)));
        
//        System.out.println(hashString);
        
        if(!signature.toUpperCase().equals(hashString)){
        	System.out.println("Request signature: " + signature);
        	System.out.println("Calculated signature: " + hashString);
            throw new AuthenticationFailedException("Forbidden.");
        }
    
    }
    
}
