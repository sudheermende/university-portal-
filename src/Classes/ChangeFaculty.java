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

@WebServlet(urlPatterns="/EditCourse")
public class ChangeFaculty extends HttpServlet{

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String userid=req.getParameter("userid");
		String course=req.getParameter("course");
		String newFaculty=req.getParameter("newFaculty");
		String oldFaculty=req.getParameter("oldFaculty");
		String credits=req.getParameter("credits");
		String preReq=req.getParameter("preReq");
		String 	capacity=req.getParameter("capacity");
		String time=req.getParameter("time");
		String room=req.getParameter("room");
		String cost=req.getParameter("cost");
		
		

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

		Connection connection =sqlthrd.getConn();

			try {
				Statement st=connection.createStatement();
				
			
		
		if(source.equalsIgnoreCase("editCourse"))
		{
			
			String sql="update courses set credits="+credits+", prerequisite='"+preReq+"', maxStudents="+capacity+" , time='"+time+"' , "
					+ "room='"+room+"', cost="+cost+", instructor='"+newFaculty+"'   where courseId='"+course+"'";
				st.execute(sql);
				connection.close();
				req.setAttribute("notification","'"+course+"' course details changed");
				req.getRequestDispatcher("FacultyList.jsp?userid="+userid+"").forward(req, resp);
	
			
			
			
			
		}
			} catch (SQLException e) {
					// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
	}
}
