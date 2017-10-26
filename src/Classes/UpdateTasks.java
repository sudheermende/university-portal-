package Classes;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns="/UpdateTasks")
public class UpdateTasks extends HttpServlet{

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	
		String userid=req.getParameter("userid");
		String student=req.getParameter("student");
		String course=req.getParameter("course");
		
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
		
	// updating students marsk (only for respective faculty)	
		if(source.equalsIgnoreCase("updateMarks"))
		{
			int task=Integer.parseInt(req.getParameter("taskNumber"));
			
			
			int marks=Integer.parseInt(req.getParameter("marks"));
			
		String sql="update MarksList set scoredMarks="+marks+" where courseId='"+course+"' and  studentName='"+student+"' and task='Task"+task+"'";
		
		st.execute(sql);
		
		connection.close();
		
		req.setAttribute("notification",student+" marks updated in task "+task);
		req.getRequestDispatcher("ClassTasks.jsp?userid="+userid+"&course="+course+"&no="+(task-1)+"").forward(req, resp);
			
		}
		
		if(source.equalsIgnoreCase("submitAnswer"))
		{
			int task=Integer.parseInt(req.getParameter("taskNumber"));
			
			
			String answer=req.getParameter("answer");
			String maxMarks=req.getParameter("maxMarks");
			
			// select answer from  MarksList where studentName='sudheerm' and courseId='CS005' and task='Task5'
			String sql="select count(answer) from  MarksList where studentName='"+userid+"' and courseId='"+course+"' and task='Task"+task+"'";
			int sampleAnswer=st.executeQuery(sql).getInt(1);
			
			if(sampleAnswer!=0)
			{
				connection.close();
				req.setAttribute("notification","Task "+(task)+" answer already submitted");
				req.getRequestDispatcher("StudentActivities.jsp?userid="+userid+"&course="+course+"&no="+(task-1)+"").forward(req,resp);
					
			}
			
			
			String sql1="insert into MarksList values('"+userid+"','"+course+"','Task"+task+"','"+answer+"',"
					+ "'"+new Date()+"','"+maxMarks+"',0,'false',' ')";
			
			st.execute(sql1);
			
			
			connection.close();
			req.setAttribute("notification","Task "+(task	)+" answer submitted");
			req.getRequestDispatcher("StudentActivities.jsp?userid="+userid+"&course="+course+"&no="+(task-1)+"").forward(req,resp);
			
		}
		
		if(source.equalsIgnoreCase("updateAnswer"))
		{
			int task=Integer.parseInt(req.getParameter("taskNumber"));
			
			
			String answer=req.getParameter("answer");
			
			String sql="update MarksList set answer='"+answer+"', editable='false',submittedDate='"+new Date()+"' where studentName='"+userid+"' and courseId='"
					+ course+"' and task='Task"+task+"'";
			
			st.execute(sql);
			
			
			connection.close();
			req.setAttribute("notification","Task "+(task	)+" answer submitted");
			req.getRequestDispatcher("StudentActivities.jsp?userid="+userid+"&course="+course+"&no="+(task-1)+"").forward(req,resp);
			
		}
		
		if(source.equalsIgnoreCase("changeEditable"))
		{
			
			int task=Integer.parseInt(req.getParameter("taskNumber"));
			
			
			String status=req.getParameter("editable");
			String changeStatus="";
			
			if(status.equalsIgnoreCase("false"))
			changeStatus="true";
			else
				changeStatus="false";
				
			
			
			
			String sql="update MarksList set editable='"+changeStatus+"' where studentName='"+student+"' and courseId='"
					+ course+"' and task='Task"+task+"'";
			
			st.execute(sql);
			
			connection.close();
			req.setAttribute("notification",student+" allowed to edit the answer in Task "+(task));
			req.getRequestDispatcher("ClassTasks.jsp?userid="+userid+"&course="+course+"&no="+(task-1)+"").forward(req,resp);
			
		}
		
		
		if(source.equalsIgnoreCase("changeGradingStatus"))
		{
			String status=req.getParameter("status"); 
			String newStatus="";
			if(status.equalsIgnoreCase("pending"))
				newStatus="finished";
			else
				newStatus="pending";
			
				
			String sql="update courses set grading='"+newStatus+"' where courseId='"+course+"'";
			st.execute(sql);
			
			connection.close();
			req.getRequestDispatcher("ViewOneCourse.jsp?userid="+userid+"&course="+course+"").forward(req, resp);
			
		}
		
		
		
	
} catch (SQLException e) {
	e.printStackTrace();
}
}
}
