/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.po_crm_stub.services;

import java.util.Set;

import javax.ws.rs.core.Application;

@javax.ws.rs.ApplicationPath("api")
public class ApplicationConfig extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
        addRestResourceClasses(resources);
        return resources;
    }

    /**
     * Do not modify addRestResourceClasses() method.
     * It is automatically populated with
     * all resources defined in the project.
     * If required, comment out calling this method in getClasses().
     */
    private void addRestResourceClasses(Set<Class<?>> resources) {
        resources.add(com.mycompany.po_crm_stub.services.FetchInfo.class);
        resources.add(com.mycompany.po_crm_stub.services.FetchThreads.class);
        resources.add(com.mycompany.po_crm_stub.services.Login.class);
        resources.add(com.mycompany.po_crm_stub.services.Sync.class);
        resources.add(com.mycompany.po_crm_stub.services.SyncAttachment.class);
        resources.add(com.mycompany.po_crm_stub.services.DownloadAttachment.class);
    }
    
}
