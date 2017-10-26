package Classes;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.JOptionPane;

@WebServlet(urlPatterns="/forgotpassword")
public class ForgotPassword extends HttpServlet{

	Connection connection=null;
	
@Override
protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

	String userid=(req.getParameter("userid")).toLowerCase();
	

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
	
	UserGuiDB user=new UserGuiDB(userid.toLowerCase());
	
	boolean result=user.searchID(connection);
	
	try {
		connection.close();
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	if(result==true)
	{
		req.setAttribute("userid",userid);
		req.getRequestDispatcher("ResetPassword.jsp").forward(req, resp);
	}
	
	else	{ 
		req.setAttribute("error",userid+" is not available, Try with other user id");
		req.getRequestDispatcher("ForgotPassword.jsp").forward(req, resp);
	}
	
}
	
}
