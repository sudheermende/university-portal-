package Classes;

import java.io.IOException;
import java.sql.Connection;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns="/ViewDeptList")
public class ViewDeptList extends HttpServlet{

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String userid=req.getParameter("userid");
		String source=req.getParameter("source");
	
//		SqliteConnectionThread sqlthrd=new SqliteConnectionThread();
//		sqlthrd.start();
//		try 
//		{
//			sqlthrd.join();
//		} catch (InterruptedException e) 
//		{
//
//			e.printStackTrace();
//		}
//		Connection connection =sqlthrd.getConn();	
		
		if(source!=null)
		{
			
req.getRequestDispatcher("ViewDepartment.jsp?userid="+userid+"&dept="+source+"").forward(req, resp);		
		}
		
	
	
	
	}
}
