package Classes;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.LinkedList;

import javax.rmi.CORBA.Stub;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.JOptionPane;



@WebServlet(urlPatterns="/ClassTasks")
public class ClassTasks extends HttpServlet{

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	
		}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	
		String userId=req.getParameter("userid");
		String courseId=req.getParameter("course");
		String taskName=req.getParameter("taskName");
		String taskDetails=req.getParameter("taskDetails");
		String dateTime=req.getParameter("lastDate");
		String maxMarks=req.getParameter("maxMarks");
		
		String source=req.getParameter("source");

		SqliteConnectionThread sqlthrd=new SqliteConnectionThread();
		sqlthrd.start();
		try 
		{
			sqlthrd.join();
		} catch (InterruptedException e) 
		{

			e.printStackTrace();
		}
		Connection	connection =sqlthrd.getConn();	
try {
				Statement st=connection.createStatement();
			
//Creating a New Task (*For Respective Faculty only)		
		if(source.equalsIgnoreCase("postTask"))
		{
			int count=st.executeQuery("select count(task) from "+courseId+"Details where task='"+taskName+"'").getInt(1);
			
			if(count>0)
			{
				connection.close();

				req.setAttribute("notification",""+taskName+" is already added !");
				req.getRequestDispatcher("ClassTasks.jsp?userid="+userId+"&course="+courseId+"").forward(req, resp);
				
			}
			
			
			Date postedDate=new Date();
			
			String sql="insert into "+courseId+"Details values('"+taskName+"',"
					+ "'"+taskDetails+"','"+postedDate+"','"+dateTime+"','"+maxMarks+"')";
			st.execute(sql);
			
			connection.close();

			req.setAttribute("notification","New "+taskName+" is added !");
			req.getRequestDispatcher("ClassTasks.jsp?userid="+userId+"&course="+courseId+"").forward(req, resp);
			
		}
		
		else if(source.equalsIgnoreCase("editTask"))
		{
			int taskPage=Integer.parseInt(req.getParameter("taskNumber"));
			
			connection.close();
			req.getRequestDispatcher("CreateNewTask.jsp?userid="+userId+"&course="+courseId+"&no="+taskPage+"&update=update").forward(req, resp);
		}
		
	
// For Updating the task details (for *Respective faculty only)		
		else if(source.equalsIgnoreCase("updateTask"))
		{
			int taskNumber=Integer.parseInt(req.getParameter("taskNumber"))-1;
			
			
			taskDetails="Last Edited Date :"+new Date()+"\n"
					+ "-------------------------------------------------------------"
					+ "\n"+taskDetails;
			
			String sql="update "+courseId+"Details set details='"+taskDetails+"',lastDate='"+dateTime+"',"
					+ "marks='"+maxMarks+"' where task='"+taskName+"'";
			
			// when updating a task it may contains max marks chnages alos, so we need to update the MaxScore 
			// in MarksList table for that perticular course and task. so updating two tables			
			
			String sql2="update MarksList set maxMarks='"+maxMarks+"' where courseId='"+courseId+"' and task='"+taskName+"'";
						
			st.execute(sql);
			st.execute(sql2);
			connection.close();

			req.setAttribute("notification",taskName+" is edited");
			req.getRequestDispatcher("ClassTasks.jsp?userid="+userId+"&course="+courseId+"").forward(req, resp);
			
		}
		
// view Student Activity in a perticular subject( *All admins and Respective faculty)
		else if(source.equalsIgnoreCase("viewActivities"))
		{
			String student=req.getParameter("student");
			
			connection.close();
			req.getRequestDispatcher("ClassTasks.jsp?userid="+userId+"&course="+courseId+"&singleOrAll="+student+"").forward(req, resp);
			
		}

// view Student Activity in a perticular subject( *All students)
		else if(source.equalsIgnoreCase("viewPerformance"))
		{
			
			connection.close();
			req.getRequestDispatcher("StudentActivities.jsp?userid="+userId+"&course="+courseId+"").forward(req, resp);
			
		}
		
		else if(source.equalsIgnoreCase("gradeCourse"))
		{
			String sql="select marks from "+courseId+"Details";
			ResultSet rsMarks=st.executeQuery(sql);
			
			int totalMaxMarks=0;
			
			while(rsMarks.next())
			{
				totalMaxMarks=totalMaxMarks+rsMarks.getInt("marks");
			}
			
			String sql2="select studentName from "+courseId+"List";
			ResultSet rsStudentList=st.executeQuery(sql2);
			
			LinkedList<String> list=new LinkedList<>();
			
			while(rsStudentList.next())
			{
				list.add(rsStudentList.getString(1));
					
			}
			
			for(int i=0;i<list.size();i++)
			{
				String oneStudent=list.get(i);
				String sql3="select scoredMarks from MarksList where courseId='"+courseId+"'"
			
					+ " and studentName='"+oneStudent+"'";
			
			ResultSet rsStudentMarks=st.executeQuery(sql3);
			int studentMarks=0;
		
			while(rsStudentMarks.next())
			{
				studentMarks=studentMarks+rsStudentMarks.getInt("scoredMarks");
			}
			
			
			double gpa=EncriptedFormat.getGpa(totalMaxMarks, studentMarks);
			String grade=EncriptedFormat.getGrade(gpa);
			
			String sql4="update "+oneStudent+"Academics set status='finished' , grade='"+grade+"' , gpa="+gpa+""
					+ " where courseId='"+courseId+"'";
			
			st.execute(sql4);
			
			double cgpa=EncriptedFormat.getCgpa(connection,oneStudent);
			
			String sql5="update users set grade="+cgpa+" where username='"+oneStudent+"'";
	
			st.execute(sql5);
			
			
			}
			
			String sql6="update courses set grading='finished' where courseId='"+courseId+"'";
			st.execute(sql6);
				
			
			connection.close();
			req.getRequestDispatcher("FacultyCourseList.jsp?userid="+userId+"&course="+courseId+"").forward(req, resp);
			
		}
		
		
		
		
} catch (SQLException e) {
				
				e.printStackTrace();
			}
	}
	
}
