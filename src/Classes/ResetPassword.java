package Classes;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.JOptionPane;

@WebServlet(urlPatterns="/resetpassword")
public class ResetPassword extends HttpServlet{
Connection connection;
String notification=null;
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException 
{
	
	String userid=(req.getParameter("userid").toLowerCase());
	String answer=req.getParameter("answer");
	String password=req.getParameter("password");
	String confirmpassword=req.getParameter("confirmpassword");
	String email=req.getParameter("email");
	String cell=req.getParameter("cell");
	String oldemail=req.getParameter("oldemail");
	String oldcell=req.getParameter("oldcell");
	String source=req.getParameter("source");
	

	req.setAttribute("userid",userid);
	
	
	
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
	
if(source.equalsIgnoreCase("changePassword") || source.equalsIgnoreCase("resetPassword"))
{
	
	boolean result=UserGuiDB.searchSecurityAnswerForUser(connection,userid.toLowerCase(), answer);
			
	if(result==true)
	{
		try {
			
		if((password.length()<6))
		{
			connection.close();
		notification=" password length should be more than or 6 ";
		req.setAttribute("error",notification);
		
		if(source.equalsIgnoreCase("changePassword"))
		{
			req.getRequestDispatcher("ChangePasswordFromAc.jsp").forward(req, resp);
		}
		else if(source.equalsIgnoreCase("resetPassword")){
			
			System.out.println("opening reset ");
			req.getRequestDispatcher("ResetPassword.jsp").forward(req, resp);	
		}
		
		
		}
		else if((password.equals(confirmpassword))==false)
		{
		
			connection.close();
		notification=" Confirmation password is not matched !";
		req.setAttribute("error",notification);
		if(source.equalsIgnoreCase("changePassword"))
		{
			req.getRequestDispatcher("ChangePasswordFromAc.jsp").forward(req, resp);
		}
		else if(source.equalsIgnoreCase("resetPassword")){
			
			System.out.println("opening reset ");
			req.getRequestDispatcher("ResetPassword.jsp").forward(req, resp);
			
		}
		}
	
		else{
	
			boolean isPasswordChanged=UserGuiDB.resetPassword(connection,userid.toLowerCase(),confirmpassword);
			if(isPasswordChanged=true)
			{
				connection.close();
				notification=" password is changed! try with new password";
				req.setAttribute("error",notification);
				req.setAttribute("password",confirmpassword);
				req.getRequestDispatcher("Login.jsp?position= ").forward(req, resp);
			}
			else
			{
				connection.close();
				notification=" password not changed";
				req.setAttribute("error",notification);
				
				if(source.equalsIgnoreCase("changePassword"))
				{
					req.getRequestDispatcher("ChangePasswordFromAc.jsp").forward(req, resp);
				}
				else if(source.equalsIgnoreCase("resetPassword")){
					
					System.out.println("opening reset ");
					req.getRequestDispatcher("ResetPassword.jsp").forward(req, resp);
					
				}
			}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}
	else
	{
		try {
			connection.close();
		
		notification=" security answer not matched ";
		req.setAttribute("error",notification);
		
		if(source.equalsIgnoreCase("changePassword"))
		{
			req.getRequestDispatcher("ChangePasswordFromAc.jsp").forward(req, resp);
		}
		else if(source.equalsIgnoreCase("resetPassword")){
			
			System.out.println("opening reset ");
			req.getRequestDispatcher("ResetPassword.jsp").forward(req, resp);	
		}
		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

else if(source.equalsIgnoreCase("changeInfo"))
{

	String position=req.getParameter("position");
	String url="";
	
	if(position.equalsIgnoreCase("applicant"))
		url="Applicant.jsp?userid="+userid;
	else if(position.equalsIgnoreCase("student"))
		url="Student.jsp?userid="+userid;
	else if(position.equalsIgnoreCase("admin"))
		url="Admin.jsp?userid="+userid;
	else if(position.equalsIgnoreCase("faculty"))
		url="Faculty.jsp?userid="+userid;
	
	try {
	//JOptionPane.showMessageDialog(null,"old ce :"+oldcell+"\n old email :"+oldemail+""
		//	+ "\n new cell :"+cell+" \n new email :"+email);
	
	
	if((oldcell.equalsIgnoreCase(cell))==false || (oldemail.equalsIgnoreCase(email)==false))
	{
		String sql=null;
		
		
		
		
		
		sql="update users set email='"+email+"',cell='"+cell+"' where username='"+userid+"'";
		
		Statement st=connection.createStatement();
		st.execute(sql);
		st.close();	
		connection.close();
		req.setAttribute("error","Personal Info changed");
		req.getRequestDispatcher(url).forward(req, resp);
	
	}
	else{
		connection.close();	
		req.setAttribute("error","Personal Info not changed");
		req.getRequestDispatcher(url).forward(req, resp);
	}
	} catch (SQLException e) 
	{
		e.printStackTrace();
	}

}


	
}
}
