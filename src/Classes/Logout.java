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

@WebServlet(urlPatterns="/Logout")
public class Logout extends HttpServlet{

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	
		SqliteConnectionThread sqlthrd=new SqliteConnectionThread();
		sqlthrd.start();
		try 
		{
		
			sqlthrd.join();
		
		Connection connection =sqlthrd.getConn();

		String userid=req.getParameter("userid");
		
		Statement st=connection.createStatement();
		
		String sql="UPDATE USERS SET STATUS='active' where username='"+userid+"'";
	
		st.execute(sql);
		
		connection.close();
		req.setAttribute("error", "Already Loggedin ");
		req.getRequestDispatcher("HomeLogin.jsp").forward(req, resp);
		
		
		} catch (InterruptedException | SQLException e) 
		{
			e.printStackTrace();
		}
	
	}
}
