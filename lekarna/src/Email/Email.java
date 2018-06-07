package Email;

//package srbic.ivan.strategija;
//
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.time.LocalDate;
import java.util.Properties;

public class Email {
    private static String USER_NAME = "test.bananaacc";  // GMail user name (just the part before "@gmail.com")
    private static String PASSWORD = "test123123123"; // GMail password

    public static void sendMail(String email, String message) {
    	System.out.println(email);
    	System.out.println(message);
        try {
            sendFromGMail(USER_NAME, PASSWORD, new String[] { email }, "Obvestilo", message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    private static void sendFromGMail(String from, String password, String[] to, String subject, String body) throws MessagingException {
        Properties props = System.getProperties();
        String host = "smtp.gmail.com";
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.user", from);
        props.put("mail.smtp.password", password);
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");

        Session session = Session.getDefaultInstance(props);
        MimeMessage message = new MimeMessage(session);

        try {
            message.setFrom(new InternetAddress(from));
            InternetAddress[] toAddress = new InternetAddress[to.length];

            for( int i = 0; i < to.length; i++ ) {
                toAddress[i] = new InternetAddress(to[i]);
            }

            for( int i = 0; i < toAddress.length; i++) {
                message.addRecipient(Message.RecipientType.TO, toAddress[i]);
            }

            message.setSubject(subject);
            message.setText(body);
            Transport transport = session.getTransport("smtp");
            transport.connect(host, from, password);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
        }
        catch (AddressException ae) {
            ae.printStackTrace();
        }
        catch (MessagingException me) {
            me.printStackTrace();
        }
    }
}
//
//
////package srbic.ivan.strategija;
////
////import java.io.UnsupportedEncodingException;
////import java.util.Date;
////import java.util.Properties;
////
////import javax.activation.DataHandler;
////import javax.activation.DataSource;
////import javax.activation.FileDataSource;
////import javax.mail.BodyPart;
////import javax.mail.Message;
////import javax.mail.MessagingException;
////import javax.mail.Multipart;
////import javax.mail.Session;
////import javax.mail.Transport;
////import javax.mail.internet.InternetAddress;
////import javax.mail.internet.MimeBodyPart;
////import javax.mail.internet.MimeMessage;
////import javax.mail.internet.MimeMultipart;
////
////public class Email{
////
////	/**
////	 * Utility method to send simple HTML email
////	 * @param session
////	 * @param toEmail
////	 * @param subject
////	 * @param body
////	 */
////	public static void sendEmail(String email, String sporocilo){
////		try
////	    {
////		  Properties props = System.getProperties();
////		  Session session = Session.getDefaultInstance(props);
////	      MimeMessage msg = new MimeMessage(session);
//////	    	System.out.println(email);
//////	    	System.out.println(message);
////	      String toEmail=email;
////	      String body=sporocilo;
////	      String subject="Vas paket";
////	  
////	      //set message headers
////	      msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
////	      msg.addHeader("format", "flowed");
////	      msg.addHeader("Content-Transfer-Encoding", "8bit");
////
////	      msg.setFrom(new InternetAddress("ivan.s333@gmail.com", "NoReply-JD"));
////
////	      msg.setReplyTo(InternetAddress.parse("no_reply@example.com", false));
////
////	      msg.setSubject(subject, "UTF-8");
////
////	      msg.setText(body, "UTF-8");
////
////	      msg.setSentDate(new Date());
////
////	      msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail, false));
////	      System.out.println("Message is ready");
////    	  Transport.send(msg);  
////
////	      System.out.println("EMail Sent Successfully!!");
////	    }
////	    catch (Exception e) {
////	      e.printStackTrace();
////	    }
////	}
////}
////package 
//import java.util.Properties;
//
//import javax.mail.Authenticator;
//import javax.mail.PasswordAuthentication;
//import javax.mail.Session;
//
//public class Email {
//
//	/**
//	   Outgoing Mail (SMTP) Server
//	   requires TLS or SSL: smtp.gmail.com (use authentication)
//	   Use Authentication: Yes
//	   Port for SSL: 465
//	 */
//  public static void sendMail(String email, String message) {
//	  final String fromEmail = "test.bananaacc"; //requires valid gmail id
//		final String password = "test123123123"; // correct password for gmail id
//		
//		System.out.println("SSLEmail Start");
//		Properties props = new Properties();
//		props.put("mail.smtp.host", "smtp.gmail.com"); //SMTP Host
//		props.put("mail.smtp.socketFactory.port", "465"); //SSL Port
//		props.put("mail.smtp.transport.protocol", "smtp");
//		props.put("mail.smtp.starttls.enable", "true"); 
//		props.put("mail.smtp.socketFactory.class",
//				"javax.net.ssl.SSLSocketFactory"); //SSL Factory Class
//		props.put("mail.smtp.auth", "true"); //Enabling SMTP Authentication
//		props.put("mail.smtp.port", "465"); //SMTP Port
//		
//		Authenticator auth = new Authenticator() {
//			//override the getPasswordAuthentication method
//			protected PasswordAuthentication getPasswordAuthentication() {
//				return new PasswordAuthentication(fromEmail, password);
//			}
//		};
//		
//		Session session= Session.getInstance(props, auth);
//		//Session session = Session.getDefaultInstance(props, auth);
//		System.out.println("Session created");
//	        EmailUtil.sendEmail(session, email,"", message);
//}
//
//
//	
//}