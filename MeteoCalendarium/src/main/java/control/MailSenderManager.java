/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import HelpClasses.Mail;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import javax.ejb.Remote;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
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
@Singleton
@Remote(MailSenderManagerInterface.class)
public class MailSenderManager implements MailSenderManagerInterface {

    // Sender's email ID needs to be mentioned
    private String from = "meteocalendarium@gmail.com";
    private final String password = "alessandro3";//change accordingly
    private final String username = "meteocalendarium@gmail.com";//change accordingly
    private String host = "smtp.gmail.com";
    private List<Mail> queue = new ArrayList<>();

    public void sendMail(String to, String subject, String body) {
        synchronized (queue) {
            queue.add(new Mail(to, subject, body));
        }
    }

    @Schedule(second = "59", minute = "*", hour = "*", persistent = false)
    private void sendMailReal() {

        // Assuming you are sending email through relay.jangosmtp.net
        synchronized (queue) {

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

            for (Mail m : queue) {

                try {
                    // Create a default MimeMessage object.
                    Message message = new MimeMessage(session);

                    // Set From: header field of the header.
                    message.setFrom(new InternetAddress(from));

                    // Set To: header field of the header.
                    message.setRecipients(Message.RecipientType.TO,
                            InternetAddress.parse(m.getTo()));

                    // Set Subject: header field
                    message.setSubject(m.getSubject());

                    // Now set the actual message
                    message.setText(m.getBody());

                    // Send message
                    Transport.send(message);

                    System.out.println("Sent message successfully....");

                } catch (MessagingException e) {
                    throw new RuntimeException(e);
                }

            }

            queue = new ArrayList();
        }
    }
}
