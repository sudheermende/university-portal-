package Classes;

public class EmailDemo {

	public static void main(String[] args) {
		
		String[] to={"s.universityinfo@gmail.com"};
		
if(EmailSender.sendMail(
		"Welcome S_University","subject",to))System.out.println("email sent successfully");

else
	System.out.println("fail");
	}

}
