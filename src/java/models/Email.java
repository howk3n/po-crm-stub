/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.Basic;
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

/**
 *
 * @author Tamashimaru
 */
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
    @Basic(optional = false)
    @NotNull
    @Column(name = "thread_id")
    private int threadId;
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

    public Email(String sender, String recipient, int threadId, String subject, String body, Date date) {
        
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

    public int getThreadId() {
        return threadId;
    }

    public void setThreadId(int threadId) {
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
        return "models.Email[ id=" + id + " ]";
    }
    
    
    
    public static int findThread(String sender, String recipient, String subject, String body, String dateStr) throws Exception{
        
        Session session = HibernateUtil.createSessionFactory().openSession();
        Transaction tx = null;
        List<Email> emails = null;
        try {
            
            Date date = parseDate(dateStr);
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
            return 0;
        }
       
        return emails.get(0).getThreadId();

    }
    
    public static int insert(String sender, String recipient, String subject, String body, String dateStr){
        
        Session session = HibernateUtil.createSessionFactory().openSession();
        Transaction tx = null;
        int threadId = 0;
        try {
            
            tx = session.beginTransaction();
            
            Email email = new Email(sender, recipient, subject, body, parseDate(dateStr));
            email.setThreadId(email.getId());
            session.persist(email);
            threadId = email.getThreadId();
           
            tx.commit();

        } catch (HibernateException e) {
            e.printStackTrace();
            if (tx != null) {
                tx.rollback();
            }
        } finally {
            session.close();
        }
        
        return threadId;

    }
    
    public static String insert(int threadId, String sender, String recipient, String subject, String body, String dateStr){
        
       Session session = HibernateUtil.createSessionFactory().openSession();
        Transaction tx = null;
        try {
            
            tx = session.beginTransaction();
            
            Email email = new Email(sender, recipient, threadId, subject, body, parseDate(dateStr));
            session.persist(email);
           
            tx.commit();

        } catch (HibernateException e) {
            e.printStackTrace();
            if (tx != null) {
                tx.rollback();
            }
            return "ERROR";
        } finally {
            session.close();
        }
        
        return "OK";

    }
    
    private static Date parseDate(String dateStr){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = sdf.parse(dateStr);
        } catch (ParseException ex) {
            Logger.getLogger(Email.class.getName()).log(Level.SEVERE, null, ex);
        }
        return date;
    }
    
}
