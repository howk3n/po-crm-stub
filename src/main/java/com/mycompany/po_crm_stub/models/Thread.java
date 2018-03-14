/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.po_crm_stub.models;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author Tamashimaru
 */
@Entity
@Table(name = "thread")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Thread.findAll", query = "SELECT t FROM Thread t")
    , @NamedQuery(name = "Thread.findById", query = "SELECT t FROM Thread t WHERE t.id = :id")})
public class Thread implements Serializable {

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "threadId")
    private Collection<Email> emailCollection;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Customer customerId;

    public Thread() {
    }

    public Thread(Integer id) {
        this.id = id;
    }
    
    public Thread(Customer customerId){
        this.customerId = customerId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Customer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Customer customerId) {
        this.customerId = customerId;
    }
    
    @XmlTransient
    public Collection<Email> getEmailCollection() {
        return emailCollection;
    }

    public void setEmailCollection(Collection<Email> emailCollection) {
        this.emailCollection = emailCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Thread)) {
            return false;
        }
        Thread other = (Thread) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "models.Thread[ id=" + id + " ]";
    }
    
    public static List<Thread> selectThreads(int customerId){
        
        List<Thread> threadList = null;
        Session session = HibernateUtil.createSessionFactory().openSession();
        Transaction tx = null;

        try {

            tx = session.beginTransaction();

            Query query = session.createQuery("from Thread where customer_id = :customerIdParam");
            query.setParameter("customerIdParam", customerId);
            
            threadList = query.list();

            tx.commit();

        } catch (HibernateException e) {
            e.printStackTrace();
            if (tx != null) {
                tx.rollback();
            }
        } finally {
            session.close();
        }
        if(threadList == null || threadList.isEmpty()){
            return null;
        }

        return threadList;
    }
    
    public static Thread insertThread(Customer customer){
        
        Session session = HibernateUtil.createSessionFactory().openSession();
        Transaction tx = null;
        Thread thread = null;
        try {

            tx = session.beginTransaction();

            thread = new Thread(customer);
            session.persist(thread);
            
            tx.commit();

        } catch (HibernateException e) {
            e.printStackTrace();
            if (tx != null) {
                tx.rollback();
            }
        } finally {
            session.close();
        }

        return thread;
        
    }
    
    public static Thread findThreadByThreadId(int threadId){
        
        Session session = HibernateUtil.createSessionFactory().openSession();
        Transaction tx = null;
        List<Thread> threadList = null;
        
        try {

            tx = session.beginTransaction();

            Query query = session.createQuery("from Thread where id = :threadIdParam");
            query.setParameter("threadIdParam", threadId);
            
            threadList = query.list();

            tx.commit();

        } catch (HibernateException e) {
            e.printStackTrace();
            if (tx != null) {
                tx.rollback();
            }
        } finally {
            session.close();
        }
        if(threadList.isEmpty()){
            return null;
        }

        return threadList.get(0);
    }

}
