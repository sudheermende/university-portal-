package Classes;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.print.attribute.standard.JobPrioritySupported;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.JOptionPane;


@WebServlet(urlPatterns="/index")
public class FirstServlet extends HttpServlet{

	Connection connection=null;
	

@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		req.getRequestDispatcher("/index.jsp").forward(req, resp);
		}
	
@Override
protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException 
{
	// TODO Auto-generated method stub
	String firstuser=(req.getParameter("userid").toLowerCase());
	String userid=firstuser.toLowerCase();
	String password=req.getParameter("password");
	String position=null;

	req.setAttribute("userid",userid);
	req.setAttribute("password",password);
	
	
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
	
	UserGuiDB user=new UserGuiDB(userid,password);
	boolean result=user.searchAccount(connection);
	
	
	if(result==true)
	{
		try {
		/*
			if(UserGuiDB.isLoggedIn(connection, userid)==true)
		{
			
			connection.close();
			String pos=req.getParameter("position");
			req.setAttribute("error", "Already Loggedin ");
			req.getRequestDispatcher("Login.jsp?position="+pos+"").forward(req, resp);
			return;
		}
		*/

		position=UserGuiDB.getUserPosition(connection,userid);
		
		ResultSet rs=UserGuiDB.getUserFromDB(connection, userid);
		
		String answer=req.getParameter("answer");
		String cell=rs.getString("cell");
		String email=rs.getString("email");
		String dateofbirth=rs.getString("dateofbirth");
		String notification=null;
		String department=rs.getString("department");
		String level=rs.getString("level");
	
		//JOptionPane.showMessageDialog(null, userid);
	/*
		String sql="UPDATE USERS SET STATUS='opened' where username='"+userid+"'";
		Statement st=connection.createStatement();
		st.execute(sql);
		//st.close();
*/		
		if(position.equalsIgnoreCase("applicant")|position.equalsIgnoreCase("canceled"))
		{
			UserGuiDB.CreatingTablesIfNotCreated(connection,userid,"applicant");

		//rs.close();
		connection.close();
		//JOptionPane.showMessageDialog(null,userid);

				req.getRequestDispatcher("Applicant.jsp?userid="+userid+"").forward(req, resp);
		}
		else
			if(position.equalsIgnoreCase("student"))
		{
		UserGuiDB.CreatingTablesIfNotCreated(connection,userid,"student");

				//rs.close();
				connection.close();
			
		req.getRequestDispatcher("Student.jsp?userid="+userid+"").forward(req, resp);
			
		}
			
			else if(position.equalsIgnoreCase("faculty"))
			{
		UserGuiDB.CreatingTablesIfNotCreated(connection,userid,"faculty");
		
				//rs.close();
				connection.close();
				//JOptionPane.showMessageDialog(null,userid);

				req.getRequestDispatcher("Faculty.jsp?userid="+userid+"").forward(req, resp);
				
			}
			else if(position.equalsIgnoreCase("Admin"))
			{
	UserGuiDB.CreatingTablesIfNotCreated(connection,userid,"admin");
	
				//rs.close();
				connection.close();
				//JOptionPane.showMessageDialog(null,userid);

				req.getRequestDispatcher("Admin.jsp?userid="+userid+"").forward(req, resp);		
			}
		
		} catch (SQLException e) {
		
			e.printStackTrace();
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
		String pos=req.getParameter("position");
		req.setAttribute("error", "Invalid Details");
		req.getRequestDispatcher("Login.jsp?position="+pos+"").forward(req, resp);
		
		
}

	
}
}
