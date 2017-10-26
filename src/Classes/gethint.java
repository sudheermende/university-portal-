package Classes;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns="/gethint")
public class gethint extends HttpServlet{
	
	  public void init() {}
	 
	  public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {


		  SqliteConnectionThread sqlthrd=new SqliteConnectionThread();
		  sqlthrd.start();
		  try 
		  {
		  	
		  	sqlthrd.join();
		 
		  Connection connection =sqlthrd.getConn();

		  String source=req.getParameter("source");
		  String  result="";
		  String word=req.getParameter("id").toLowerCase();
		 
		  
		  // check for prereq
		  if(source.equalsIgnoreCase("prereq"))
		  {
			  result=getPreReqMatch(word,connection);
		
			 
					connection.close();
		  }

		  if(source.equalsIgnoreCase("applicant")||source.equalsIgnoreCase("student")||source.equalsIgnoreCase("faculty"))
		  {
			  
			  result=getMatchPersons(word, connection, source);
			  
			  connection.close();
		  }
		  
		  
		 
		  res.getOutputStream().print(result);
  
		  } catch (InterruptedException | SQLException e) 
		  {
		  	e.printStackTrace();
		  }
		  
		  
	  }
	 // end of doGet
	  
	  
	  public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
	


	  }
	 
	  public void destroy() {}

	  
/// getting all pre req subjects for required sub	  
	public String getPreReqMatch(String word,Connection connection)
	{

		
	String sql="select course,courseId from courses where department='"+word+"'";
	
	try {
		Statement st=connection.createStatement();

	ResultSet rs=st.executeQuery(sql);
		

		String match="";
		
		while(rs.next())
			{
			
			String course=rs.getString("course");
			String courseId=rs.getString("courseId");
			
			match+="<option id='"+courseId+"' value='"+courseId+"'> ( "+courseId + " )   "+course+"</option>\n";
				
		}
		

		if(match.length()==0)
		{
			match="<option value='' > no match </option>\n";
			
		}
		else
		{
			match="<option value=''>select</option>\n"+match;
		}
		
		return match;
	
	} catch (SQLException e) {
		e.printStackTrace();
	}
	
	return "";
	
	}
// end of getting prereqs	
	

public String getMatchPersons(String word,Connection connection,String position)
{
	
String sql="select username from users where position='"+position+"'";
	
	try {
		Statement st=connection.createStatement();

	ResultSet rs=st.executeQuery(sql);
		
	LinkedList<String> list=new LinkedList<>();
	
	while(rs.next())
	{
		list.add(rs.getString(1));
	}
	

		String match="";
		
	
		
		for(int i=0;i<list.size();i++)
			{
			
			if(list.get(i).contains(word))
			{
		
				match+="<option id='"+list.get(i)+"' value='"+list.get(i)+"'> "+list.get(i)+"</option>\n";
			}
			}
		
		if(match.length()==0)
		{
			match="<option value='' > no match </option>\n";
			
		}
		else
		{
			match="<option value=''>select</option>\n"+match;
		}
		

		return match;
	
	} catch (SQLException e) {
		e.printStackTrace();
	}
	
	return "";
	
}
		
	  
}









  



