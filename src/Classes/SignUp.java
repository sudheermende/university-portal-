package Classes;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns="/SignUp")
public class SignUp extends HttpServlet
{
Connection connection=null;
	@Override
		protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
			// TODO Auto-generated method stub
			super.doGet(req, resp);
		}
	
@Override
protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	// TODO Auto-generated method stub
	String userid=req.getParameter("userid").trim();
	String password=req.getParameter("password").trim();
	String answer=req.getParameter("answer");
	String cell=req.getParameter("cell").trim();
	String confirmpassword=req.getParameter("confirmpassword").trim();
	String email=req.getParameter("email");
	String dateofbirth=req.getParameter("dob");
	String notification=null;
	String department=req.getParameter("department");
	String level=req.getParameter("level");
	String source=req.getParameter("source");

	req.setAttribute("userid",userid);
	req.setAttribute("password",password);
	req.setAttribute("answer",answer);
	req.setAttribute("cell",cell);
	req.setAttribute("confirmpassword",confirmpassword);
	req.setAttribute("email",email);
	req.setAttribute("dob",dateofbirth);
	req.setAttribute("level",level);
	req.setAttribute("department", department);
	req.setAttribute("position", "applicant");

	SqliteConnectionThread sqlthrd=new SqliteConnectionThread();
	sqlthrd.start();
	try 
	{
		sqlthrd.join();
	} catch (InterruptedException e) 
	{

		e.printStackTrace();
	}
	connection =sqlthrd.getConn();	

if(source.equalsIgnoreCase("signup"))
{		
	UserGuiDB user=new UserGuiDB(userid,password);
	boolean result=user.searchID(connection);
	

	

	if(result==false)
	{

		if(userid.length()==0 )
		{
			try {
				connection.close();
			} catch (SQLException e) 
			{
				e.printStackTrace();
			}
			notification=" User name should not be empty";
			req.setAttribute("error",notification);
			req.getRequestDispatcher("Signup.jsp").forward(req, resp);
		}

	else if(answer.length()==0 )
		{
		try {
			connection.close();
		} catch (SQLException e) 
		{
			e.printStackTrace();
		}
			notification=" Security answer is empty";
			req.setAttribute("error",notification);
			req.getRequestDispatcher("Signup.jsp").forward(req, resp);
		}
		
		else if((password.length()<6))
		{
			try {
				connection.close();
			} catch (SQLException e) 
			{
				e.printStackTrace();
			}
			notification=" password length should be more than 6 ";
			req.setAttribute("error",notification);
			req.getRequestDispatcher("Signup.jsp").forward(req, resp);

		}
		 else if((password.equals(confirmpassword))==false)
		{
			 try {
					connection.close();
				} catch (SQLException e) 
				{
					e.printStackTrace();
				}
			 
		notification=" Confirmation password is not matched !";
		req.setAttribute("error",notification);
		req.getRequestDispatcher("Signup.jsp").forward(req, resp);

		}
		
		 else{	
		
		String message=" hi "+userid+","
		+ "\nThank you for your interest to continue your studies in S_University."
		+ "\nYou have applied successfully!"+
		"\n your status will change to 'STUDENT' when you get admission!"
		+ "\n"
		+ "\n"
		+ "\nThank you"
		+ "\n"
		+ "\n"
		+ "\nRegard"
		+ "\nAdmin depart"
		+ "\nS_university"; 
		
		String subj="Application :"+userid;
		
		String[] to={email};
		
		EmailSender.sendMail(message,subj,to);
			 UserGuiDB.CreatingNewUser(connection, userid.toLowerCase(), password, answer,cell,email,dateofbirth,level,"applicant",department);
			 notification=" You have applied successfully! your status will change to 'STUDENT' when you get admition!";
		req.setAttribute("notification",notification);

		try {
			connection.close();
		} catch (SQLException e) 
		{
			e.printStackTrace();
		}
		req.getRequestDispatcher("Applicant.jsp?userid="+userid+"").forward(req, resp);
		 }
	}
	else
	{
		try {
			connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		req.setAttribute("error","username already exist! Try new One");
		req.getRequestDispatcher("Signup.jsp").forward(req, resp);
	}
}
	
}	
}
