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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
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
@Table(name = "customer")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Customer.findAll", query = "SELECT c FROM Customer c")
    , @NamedQuery(name = "Customer.findById", query = "SELECT c FROM Customer c WHERE c.id = :id")
    , @NamedQuery(name = "Customer.findByName", query = "SELECT c FROM Customer c WHERE c.name = :name")
    , @NamedQuery(name = "Customer.findByAddress1", query = "SELECT c FROM Customer c WHERE c.address1 = :address1")
    , @NamedQuery(name = "Customer.findByAddress2", query = "SELECT c FROM Customer c WHERE c.address2 = :address2")
    , @NamedQuery(name = "Customer.findByAddress3", query = "SELECT c FROM Customer c WHERE c.address3 = :address3")})
public class Customer implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "name")
    private String name;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "address1")
    private String address1;
    @Size(max = 255)
    @Column(name = "address2")
    private String address2;
    @Size(max = 255)
    @Column(name = "address3")
    private String address3;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "customerId")
    private Collection<Repcust> repcustCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "customerId")
    private Collection<Opportunity> opportunityCollection;

    public Customer() {
    }

    public Customer(Integer id) {
        this.id = id;
    }

    public Customer(Integer id, String name, String address1) {
        this.id = id;
        this.name = name;
        this.address1 = address1;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getAddress3() {
        return address3;
    }

    public void setAddress3(String address3) {
        this.address3 = address3;
    }

    @XmlTransient
    public Collection<Repcust> getRepcustCollection() {
        return repcustCollection;
    }

    public void setRepcustCollection(Collection<Repcust> repcustCollection) {
        this.repcustCollection = repcustCollection;
    }

    @XmlTransient
    public Collection<Opportunity> getOpportunityCollection() {
        return opportunityCollection;
    }

    public void setOpportunityCollection(Collection<Opportunity> opportunityCollection) {
        this.opportunityCollection = opportunityCollection;
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
        if (!(object instanceof Customer)) {
            return false;
        }
        Customer other = (Customer) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "models.Customer[ id=" + id + " ]";
    }
    
    public static Customer selectQuery(String address){
        List<Customer> customerList = null;
        Session session = HibernateUtil.createSessionFactory().openSession();
        Transaction tx = null;

        try {

            tx = session.beginTransaction();

            Query query = session.createQuery("from Customer where address1 = '" + address + "' or address2 = '" + address + "' or address3 = '" + address + "'");
            
            customerList = query.list();

            tx.commit();

        } catch (HibernateException e) {
            e.printStackTrace();
            if (tx != null) {
                tx.rollback();
            }
        } finally {
            session.close();
        }
        if(customerList.isEmpty()){
            return null;
        }

        return customerList.get(0);
    }
    
    public static List<Customer> selectAll(){
    
        List<Customer> customerList = null;
        Session session = HibernateUtil.createSessionFactory().openSession();
        Transaction tx = null;

        try {

            tx = session.beginTransaction();

//            Query query = session.createQuery("from Customer where address1 = '" + address + "' or address2 = '" + address + "' or address3 = '" + address + "'");
            Query query = session.getNamedQuery("Customer.findAll");
            customerList = query.list();

            tx.commit();

        } catch (HibernateException e) {
            e.printStackTrace();
            if (tx != null) {
                tx.rollback();
            }
        } finally {
            session.close();
        }
        if(customerList == null || customerList.isEmpty()){
            return null;
        }

        return customerList;
        
    }
    
}
