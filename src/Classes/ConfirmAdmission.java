package Classes;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.JOptionPane;


@WebServlet(urlPatterns="/confirmAdmission")
public class ConfirmAdmission extends HttpServlet{

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	Connection connection=null;
	//String applicant=req.getParameter("applicantName");
	String userName=req.getParameter("userid");
	String confirmaction=req.getParameter("confirmaction");
	String cancelaction=req.getParameter("cancelaction");
	String student=req.getParameter("viewPerformance");
	String faculty=req.getParameter("viewClasses");
	String course=req.getParameter("seeCourse");
	//String department=req.getParameter("deptClasses");
	String noOfCourseinDeprtment=req.getParameter("noOfCourses");
	String view=req.getParameter("view");
	String position=req.getParameter("position");
	
	
	SqliteConnectionThread sqlthrd=new SqliteConnectionThread();
	sqlthrd.start();
	Statement st;

	if(cancelaction==null&&confirmaction!=null)
	{
		String applicant=confirmaction;
		System.out.println("3 Usrname : "+userName+" applicant : "+applicant);
		try 
		{
		sqlthrd.join();
	
		connection =sqlthrd.getConn();	
	
		
		System.out.println("4  Usrname : "+userName+" applicant : "+applicant);
		int newId=UserGuiDB.createIdForAnyList(connection, "position", "student");
		System.out.println("5  Usrname : "+userName+" applicant : "+applicant);
		
		//JOptionPane.showMessageDialog(null,newId);
		String sql="update users set position='student' , id="+newId+" where username='"+applicant+"'";
	
		st=connection.createStatement();
		st.execute(sql);
		st.close();
		System.out.println("6  Usrname : "+userName+" applicant : "+applicant);
		
		req.setAttribute("notification",applicant+" admission is confirmed!");
		req.getRequestDispatcher("ApplicantsList.jsp?userid="+userName+"").forward(req, resp);
	
		
		
		String message=" Hi "+applicant+","
		+ "\n\nThank you for your interest to continue your studies in S_University."
		+ "\nYou have applied successfully!"+
		"\n your have got admission in S_University ."
		+ "\n\n Your Student ID : "+newId
		+ "\n"
		+ "\nThank you."
		+ "\n"
		+ "\n"
		+ "\nRegards"
		+ "\nAdmin depart"
		+ "\nS_university"; 
		
		String subj="Admission Approved :"+applicant;
		
		ResultSet rs=UserGuiDB.getUserFromDB(connection, applicant);
		String email=rs.getString("email");
		
		connection.close();
		
		String[] to={email};
		
		EmailSender.sendMail(message,subj,to);

		
		
		}
		catch ( SQLException | InterruptedException e) 
		{
			System.out.println("catch 8  Usrname : "+userName+" applicant : "+applicant);
			
		e.printStackTrace();
		}

	}
	
	else if(cancelaction!=null&&confirmaction==null)
	{
		String applicant=cancelaction;
		try 
		{
			
		sqlthrd.join();
		
		connection =sqlthrd.getConn();	
		
		int newId=0;
		
		String sql="update users set position='canceled' , id="+newId+" where username='"+applicant+"'";
		
		st=connection.createStatement();
		st.execute(sql);
		
		req.setAttribute("notification", applicant+" admission is canceled!");
		req.getRequestDispatcher("ApplicantsList.jsp?userid="+userName+"").forward(req, resp);
		
		connection.close();
		} catch ( SQLException | InterruptedException e) 
		{

			e.printStackTrace();
		}
		
	}
	
	else if(view != null)
	{
		if(position.equalsIgnoreCase("applicant"))
		{
			System.out.println(" 1 Usrname : "+userName+" applicant : "+view);
			req.getRequestDispatcher("ViewApplicant.jsp?userid="+userName+"&applicant="+view+"").forward(req, resp);
		}
		else if(position.equalsIgnoreCase("student"))
		{
			req.getRequestDispatcher("ViewStudent.jsp?userid="+userName+"&student="+view+"").forward(req, resp);			
		}
		else if(position.equalsIgnoreCase("faculty"))
		{
			req.getRequestDispatcher("ViewFaculty.jsp?userid="+userName+"&faculty="+view+"").forward(req, resp);			
		}
		
	}

	
	}


}
