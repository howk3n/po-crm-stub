package com.mycompany.po_crm_stub.models;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
 
public class HibernateUtil {
 
    static SessionFactory sessionFactory;
    private static ServiceRegistry serviceRegistry;
 
    public static SessionFactory createSessionFactory() {
        if(sessionFactory==null){
            Configuration configuration = new Configuration();
            configuration.configure();
            configuration.addAnnotatedClass(com.mycompany.po_crm_stub.models.Attachment.class);
            configuration.addAnnotatedClass(com.mycompany.po_crm_stub.models.Customer.class);
            configuration.addAnnotatedClass(com.mycompany.po_crm_stub.models.Email.class);
            configuration.addAnnotatedClass(com.mycompany.po_crm_stub.models.Opportunity.class);
            configuration.addAnnotatedClass(com.mycompany.po_crm_stub.models.Rep.class);
            configuration.addAnnotatedClass(com.mycompany.po_crm_stub.models.Repcust.class);
            configuration.addAnnotatedClass(com.mycompany.po_crm_stub.models.Thread.class);
            String jdbcDbUrl = System.getenv("JDBC_DATABASE_URL");
            if (null != jdbcDbUrl) {
                configuration.setProperty("hibernate.connection.url", jdbcDbUrl);
            } else {
                configuration.setProperty("hibernate.connection.username", "root");
            }
            serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
            sessionFactory = configuration.buildSessionFactory(serviceRegistry);
        }
        return sessionFactory;
    }
 
}