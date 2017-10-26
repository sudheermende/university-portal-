package Classes;

import java.io.IOException;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.JOptionPane;

@WebServlet(urlPatterns="/ChangeStatus")
public class ChangeStatus extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	String userid=req.getParameter("userid");
	String student=req.getParameter("studentName");
	String action=req.getParameter("action");


SqliteConnectionThread sqlthrd=new SqliteConnectionThread();
sqlthrd.start();
try 
{
	
	sqlthrd.join();
} catch (InterruptedException e) 
{
	e.printStackTrace();
}
Connection connection =sqlthrd.getConn();


try {
	Statement st=connection.createStatement();
	String sql=null;
	
	if(action.equalsIgnoreCase("block"))
	
	sql="update users set status='blocked' where username='"+student+"'";
	
	else if(action.equalsIgnoreCase("active"))
		
		sql="update users set status='active' where username='"+student+"'";
		
	st.execute(sql);
	
	connection.close();
		req.setAttribute("notification",student+" status changed ");
		req.getRequestDispatcher("StudentsList.jsp?userid="+userid+"").forward(req, resp);
	

} catch (SQLException e) {
	try {
		connection.close();
	} catch (SQLException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
	req.setAttribute("notification",student+" status not changed ");
	req.getRequestDispatcher("StudentsList.jsp?userid="+userid+"").forward(req, resp);	
	e.printStackTrace();
}

	
	}
}
