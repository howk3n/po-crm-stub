/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.po_crm_stub.models;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

@Entity
@Table(name = "opportunity")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Opportunity.findAll", query = "SELECT o FROM Opportunity o")
    , @NamedQuery(name = "Opportunity.findById", query = "SELECT o FROM Opportunity o WHERE o.id = :id")
    , @NamedQuery(name = "Opportunity.findByAmount", query = "SELECT o FROM Opportunity o WHERE o.amount = :amount")
    , @NamedQuery(name = "Opportunity.findByDate", query = "SELECT o FROM Opportunity o WHERE o.date = :date")
    , @NamedQuery(name = "Opportunity.findByStatus", query = "SELECT o FROM Opportunity o WHERE o.status = :status")})
public class Opportunity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "amount")
    private int amount;
    @Basic(optional = false)
    @NotNull
    @Column(name = "date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "status")
    private String status;
    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Customer customerId;

    public Opportunity() {
    }

    public Opportunity(Integer id) {
        this.id = id;
    }

    public Opportunity(Integer id, int amount, Date date, String status) {
        this.id = id;
        this.amount = amount;
        this.date = date;
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Customer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Customer customerId) {
        this.customerId = customerId;
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
        if (!(object instanceof Opportunity)) {
            return false;
        }
        Opportunity other = (Opportunity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "models.Opportunity[ id=" + id + " ]";
    }
    
    public static List<Opportunity> selectQuery(int customerId){
		
            List<Opportunity> opportunityList = null;
            StringBuilder opportunityString = null;
            Session session = HibernateUtil.createSessionFactory().openSession();
            Transaction tx = null;

            try {
                
                tx = session.beginTransaction();
            
                Query query = session.createQuery("from Opportunity where customer_id = " + customerId);
                opportunityList = query.list();
                
                tx.commit();
                
            } catch (HibernateException e) {
                if (tx != null) {
                    tx.rollback();
                }
                System.out.println(e);
            } finally {
                session.close();
            }
            
            return opportunityList;
        }
    
}
