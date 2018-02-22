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
@Table(name = "rep")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Rep.findAll", query = "SELECT r FROM Rep r")
    , @NamedQuery(name = "Rep.findById", query = "SELECT r FROM Rep r WHERE r.id = :id")
    , @NamedQuery(name = "Rep.findByUsername", query = "SELECT r FROM Rep r WHERE r.username = :username")
    , @NamedQuery(name = "Rep.findByPassword", query = "SELECT r FROM Rep r WHERE r.password = :password")
    , @NamedQuery(name = "Rep.findByAdmin", query = "SELECT r FROM Rep r WHERE r.admin = :admin")})
public class Rep implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "username")
    private String username;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "password")
    private String password;
    @Basic(optional = false)
    @NotNull
    @Column(name = "admin")
    private boolean admin;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "repId")
    private Collection<Repcust> repcustCollection;

    public Rep() {
    }

    public Rep(Integer id) {
        this.id = id;
    }

    public Rep(Integer id, String username, String password, boolean admin) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.admin = admin;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean getAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    @XmlTransient
    public Collection<Repcust> getRepcustCollection() {
        return repcustCollection;
    }

    public void setRepcustCollection(Collection<Repcust> repcustCollection) {
        this.repcustCollection = repcustCollection;
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
        if (!(object instanceof Rep)) {
            return false;
        }
        Rep other = (Rep) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "models.Rep[ id=" + id + " ]";
    }
    
    public static Rep findRepByUsername(String username){
        Session session = HibernateUtil.createSessionFactory().openSession();
        Transaction tx = null;
        List<Rep> repList = null;
        
        try {

            tx = session.beginTransaction();

            Query query = session.createQuery("from Rep where username = :usernameParam");
            query.setParameter("usernameParam", username);
            
            repList = query.list();

            tx.commit();

        } catch (HibernateException e) {
            e.printStackTrace();
            if (tx != null) {
                tx.rollback();
            }
        } finally {
            session.close();
        }
        if(repList.isEmpty()){
            return null;
        }

        return repList.get(0);
    }
    
}
