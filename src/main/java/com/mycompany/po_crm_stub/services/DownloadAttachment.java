/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.po_crm_stub.services;

import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.UriInfo;

import com.mycompany.po_crm_stub.models.Attachment;

@Path("/downloadAttachment/{attachmentId}")
public class DownloadAttachment {

    @Context
    private UriInfo context;
    
    @Context
    private ServletContext sContext; 
    
    @GET
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response download(@PathParam("attachmentId") Integer attachmentId){
    	
    	try {
    		
    		final Attachment attachment = Attachment.findById(attachmentId);
			if(attachment == null) {
				throw new Exception("Attachment with id: " + attachmentId + " not found in database");
			}else {
				ResponseBuilder response = Response.ok(attachment.getFileContent());
	    		System.out.println(new String(attachment.getFileContent()));
	    		
	    		response.header("Content-Disposition", "attachment; filename=\"" + attachment.getFileName() + "\"");
	    		return response.build();
			}			

    	}
    	
    	catch(Exception e) {
    		e.printStackTrace();
    		return Response.status(Response.Status.NOT_FOUND).build();
    	}
		
    }  	
}

    
