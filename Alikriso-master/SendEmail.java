import java.util.Properties;
import java.util.LinkedHashMap;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

//reference from https://www.tutorialspoint.com/javamail_api/javamail_api_gmail_smtp_server.htm
/**
 * Connects to gmail API and send email
 * @author xiayanjing, took some code from https://www.tutorialspoint.com/javamail_api/javamail_api_gmail_smtp_server.htm
 * @version 04/04/2021
 */
public class SendEmail {
  
    private static Session session;
    private static String fromEmail;
    
    /**
     * connect to gmail API
     *
     */
    public static void connectToGmailServer()
    {
        fromEmail = "alikrisoppa@gmail.com";
        final String username = "alikrisoppa";
        final String password = "guolaoshi666";
        String host = "smtp.gmail.com";
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.ssl.trust", "*");
        session = Session.getInstance(props,
        new javax.mail.Authenticator()
        {
             protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
             }
          });
    }
    
    /**
     * Send booking summary email to the client
     *
     * @param userData are data the client inserted during booking
     * @param property it's the property the client booked
     */
    public static void send(LinkedHashMap<String,TextField>userData,AirbnbListing property)throws Exception
    {
     
         // Create a default MimeMessage object.
         Message message = new MimeMessage(session);
         message.setFrom(new InternetAddress(fromEmail));
         message.setRecipients(Message.RecipientType.TO,
         InternetAddress.parse(userData.get("email").getText().trim()));
         message.setSubject("Booking summary");
         message.setText("Dear "+ userData.get("surname").getText()+" "+userData.get("name").getText()+"\n"
            + "Thank you for booking at Alikriso airnbn. "+"\n"+"we confirm that we have received your booking and below is the summary  "+"\n"
            +"Property: "+property.getName()+"\n"
            +"Neightbourhood: "+property.getNeighbourhood()+"\n"
            +"Number of nights booked: "+userData.get("numberOfNights").getText()+"\n"
            +"Total amount: "+userData.get("totalAmount").getText()+"\n"
            +"Kind Regards,"+"\n"+"Alikriso.");

         // Send message
         Transport.send(message);

     
    }
}
