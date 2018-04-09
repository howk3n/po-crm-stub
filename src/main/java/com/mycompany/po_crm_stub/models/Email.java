/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.po_crm_stub.models;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

@Entity
@Table(name = "email")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Email.findAll", query = "SELECT e FROM Email e")
    , @NamedQuery(name = "Email.findById", query = "SELECT e FROM Email e WHERE e.id = :id")
    , @NamedQuery(name = "Email.findBySender", query = "SELECT e FROM Email e WHERE e.sender = :sender")
    , @NamedQuery(name = "Email.findByRecipient", query = "SELECT e FROM Email e WHERE e.recipient = :recipient")
    , @NamedQuery(name = "Email.findByThreadId", query = "SELECT e FROM Email e WHERE e.threadId = :threadId")
    , @NamedQuery(name = "Email.findBySubject", query = "SELECT e FROM Email e WHERE e.subject = :subject")
    , @NamedQuery(name = "Email.findByDate", query = "SELECT e FROM Email e WHERE e.date = :date")})
public class Email implements Serializable {

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "emailId")
    private Collection<Attachment> attachmentCollection;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "sender")
    private String sender;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Size(min = 1, max = 65535)
    @Column(name = "recipient")
    private String recipient;
    @JoinColumn(name = "thread_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Thread threadId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "subject")
    private String subject;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Size(min = 1, max = 65535)
    @Column(name = "body")
    private String body;
    @Basic(optional = false)
    @NotNull
    @Column(name = "date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    public Email() {
    }

    public Email(Integer id) {
        this.id = id;
    }

    public Email(String sender, String recipient, Thread threadId, String subject, String body, Date date) {
        
        this.sender = sender;
        this.recipient = recipient;
        this.threadId = threadId;
        this.subject = subject;
        this.body = body;
        this.date = date;
    }
    
     public Email(String sender, String recipient, String subject, String body, Date date) {
        
        this.sender = sender;
        this.recipient = recipient;
        this.subject = subject;
        this.body = body;
        this.date = date;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public Thread getThreadId() {
        return threadId;
    }

    public void setThreadId(Thread threadId) {
        this.threadId = threadId;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
    
    @XmlTransient
    public Collection<Attachment> getAttachmentCollection() {
        return attachmentCollection;
    }

    public void setAttachmentCollection(Collection<Attachment> attachmentCollection) {
        this.attachmentCollection = attachmentCollection;
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
        if (!(object instanceof Email)) {
            return false;
        }
        Email other = (Email) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.po_crm_stub_maven.models.Email[ id=" + id + " ]";
    }
    
    
    
    public static Thread findThread(String sender, String recipient, String subject, String body, Date date) throws Exception{
        
        return findEmail(sender, recipient, subject, body, date).getThreadId();

    }
    
    public static Email findEmail(String sender, String recipient, String subject, String body, Date date) throws Exception{
        
        Session session = HibernateUtil.createSessionFactory().openSession();
        Transaction tx = null;
        List<Email> emails = null;
        try {
            
            tx = session.beginTransaction();
            
            String queryString = "from Email e where e.sender = :senderParam and e.subject = :subjectParam and e.body = :bodyParam and e.recipient = :recipientParam and e.date = :dateParam";
           
            Query query = session.createQuery(queryString);
           
            query.setParameter("senderParam", sender);
            query.setParameter("subjectParam", subject);
            query.setParameter("bodyParam", body);
            query.setParameter("dateParam", date);
            query.setParameter("recipientParam", recipient);
            System.out.println(date);
            emails = query.list();
            tx.commit();

        } catch (HibernateException e) {
            e.printStackTrace();
            if (tx != null) {
                tx.rollback();
            }
        } finally {
            session.close();
        }
        if(emails == null || emails.isEmpty()){
            return null;
        }
        return emails.get(0);

    }
    
    public static Email insert(String sender, String recipient, Thread thread, String subject, String body, Date date){
        
        Session session = HibernateUtil.createSessionFactory().openSession();
        Transaction tx = null;
        Email email = null;
        try {
            
            tx = session.beginTransaction();
            
            email = new Email(sender, recipient, thread, subject, body, date);
            session.persist(email);
           
            tx.commit();

        } catch (HibernateException e) {
            e.printStackTrace();
            if (tx != null) {
                tx.rollback();
            }
            return null;
        } finally {
            session.close();
        }
        return email;

    }
    
    public static List<Email> findByThreadId(int threadId){
        
        List<Email> emailList = null;
        Session session = HibernateUtil.createSessionFactory().openSession();
        Transaction tx = null;

        try {

            tx = session.beginTransaction();

            Query query = session.createQuery("from Email where thread_id = :threadIdParam order by date asc");
            query.setParameter("threadIdParam", threadId);
            
            emailList = query.list();

            tx.commit();

        } catch (HibernateException e) {
            e.printStackTrace();
            if (tx != null) {
                tx.rollback();
            }
        } finally {
            session.close();
        }
        if(emailList.isEmpty()){
            return null;
        }
        return emailList;
    }
    
    public static Email findByEmailId(int emailId){
        
        List<Email> emails = null;
        Session session = HibernateUtil.createSessionFactory().openSession();
        Transaction tx = null;

        try {

            tx = session.beginTransaction();

            Query query = session.createQuery("from Email where id = :idParam");
            query.setParameter("idParam", emailId);
            
            emails = query.list();
            
            tx.commit();

        } catch (HibernateException e) {
            e.printStackTrace();
            if (tx != null) {
                tx.rollback();
            }
        } finally {
            session.close();
        }
        if(emails.isEmpty()){
            return null;
        }
        return emails.get(0);
    }
    
}
