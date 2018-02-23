package com.mycompany.po_crm_stub.services;

import com.mycompany.po_crm_stub.models.Rep;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.DatatypeConverter;
import org.json.JSONObject;

public class AuthenticationManager {
    
    static void authenticate(JSONObject jRequest) throws Exception{
        
        Rep rep = Rep.findRepByUsername(jRequest.getString("username"));
        if(rep == null){
            throw new AuthenticationFailedException("Forbidden.");
        }
        
        String signature = jRequest.getString("signature");
        jRequest.remove("signature");
        String prep = jRequest.toString() + rep.getPassword();
        MessageDigest digest;
        
        digest = MessageDigest.getInstance("SHA-256");
        
        String hashString = DatatypeConverter.printHexBinary(digest.digest(prep.getBytes(StandardCharsets.UTF_8)));
        
        if(!signature.toUpperCase().equals(hashString)){
            throw new AuthenticationFailedException("Forbidden.");
        }
    
    }
    
}