package Classes;

import java.util.Properties;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;

public class EmailSender {
public static boolean sendMail(String message,String subject, String to[])
{
	String from="s.universityinfo@gmail.com";
	String password="mounikasudheer";
	String host="smtp.gmail.com";
	Properties props =new Properties();
	props.put(("mail.smtp.ssl.enable"),"true");
	props.put("mail.smtp.host",host);
	props.put("mail.smtp.user",from);
	props.put("mail.smtp.password",password);
	props.put("mail.smtp.port",465);
	props.put("mail.smtp.auth","true ");
	Session session=Session.getDefaultInstance(props, null);
	MimeMessage mimeMessage =new MimeMessage(session);

	try {
		mimeMessage.setFrom(new InternetAddress(from));
	
	InternetAddress[] toAddresses=new InternetAddress[to.length];
	for(int i=0;i<to.length;i++)
	{
		toAddresses[i]=new InternetAddress(to[i]);
		
	}
	
for(int i=0;i<toAddresses.length;i++)
{
	mimeMessage.addRecipient(RecipientType.TO ,toAddresses[i]);
}
	
mimeMessage.setSubject(subject);
mimeMessage.setText(message);

Transport transport=session.getTransport("smtp");
transport.connect(host,from,password);
transport.sendMessage(mimeMessage,mimeMessage.getAllRecipients());
transport.close();
return true;


	} catch (MessagingException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	
	
	return false;
}
}
