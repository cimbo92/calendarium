/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managerBeans;

import java.util.Properties;
import javax.ejb.Remote;
import javax.ejb.Stateless;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author home
 */

@Stateless
@Remote(MailSenderManagerInterface.class)
public class MailSenderManager implements MailSenderManagerInterface {
    
    // Recipient's email ID needs to be mentioned.
    private String to;
    // Sender's email ID needs to be mentioned
    private String from = "meteocalendarium@gmail.com";
    private final String password = "alessandro3";//change accordingly
    private final String username = "meteocalendarium@gmail.com";//change accordingly
    private String host = "smtp.gmail.com";
    private String subject;
    private String body;
   
   public void sendMail(String to, String subject, String body) {
      
      this.to = to;//change accordingly

      
    
     
      

      // Assuming you are sending email through relay.jangosmtp.net
      

      Properties props = new Properties();
      props.put("mail.smtp.auth", "true");
      props.put("mail.smtp.starttls.enable", "true");
      props.put("mail.smtp.host", host);
      props.put("mail.smtp.port", "587");

      // Get the Session object.
      Session session = Session.getInstance(props,
      new javax.mail.Authenticator() {
         protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(username, password);
         }
      });

      try {
         // Create a default MimeMessage object.
         Message message = new MimeMessage(session);

         // Set From: header field of the header.
         message.setFrom(new InternetAddress(from));

         // Set To: header field of the header.
         message.setRecipients(Message.RecipientType.TO,
         InternetAddress.parse(to));

         // Set Subject: header field
         message.setSubject(subject);

         // Now set the actual message
         message.setText(body);

         // Send message
         Transport.send(message);

         System.out.println("Sent message successfully....");

      } catch (MessagingException e) {
            throw new RuntimeException(e);
      }
   }
}