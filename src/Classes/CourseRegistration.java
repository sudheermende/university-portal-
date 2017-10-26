package Classes;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.LinkedList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.JOptionPane;

//import com.firstWebApp.Classes.Course;

@WebServlet(urlPatterns="/registration")
public class CourseRegistration extends HttpServlet{

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException 
	{
		String notification = null;
		Connection connection=null;
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
		
		String addCourse=req.getParameter("addCourse");
		String viewCourse=req.getParameter("viewCourse");
		String userid=req.getParameter("userid");
		String dropCourse=req.getParameter("dropCourse");
		String checkOut=req.getParameter("checkOut");
		String position=req.getParameter("position");
		

			// while adding course
		if(addCourse!=null&&viewCourse==null)
		{
			try {
				Statement st=connection.createStatement();
		String table=userid+"Temp";	
		
			String sql="Insert into "+table+" values( '"+addCourse+"' )";
			st.execute(sql);
			
			req.setAttribute("error","course "+addCourse+" added to cart");
			//st.close();
			connection.close();
			
			} catch (SQLException e) {
				e.printStackTrace();
				req.setAttribute("error","course "+addCourse+" already in cart");

			}
			req.getRequestDispatcher("Registration.jsp?userid="+userid+"").forward(req, resp);
			
		}
		// while viewing course
		else if(addCourse==null&&viewCourse!=null)
		{
		
		req.setAttribute("userid",userid);
		req.setAttribute("course",viewCourse);
		req.setAttribute("position",position);
		try {
			connection.close();
			
		} catch (SQLException e) {
		
			e.printStackTrace();
		}
		req.getRequestDispatcher("ViewOneCourse.jsp?course="+viewCourse+"").forward(req, resp);
		}
	
		
		if(dropCourse!=null)
		{
			try {
				Statement st=connection.createStatement();
		String table=userid+"Temp";	
		
			String sql="DELETE FROM "+table+" WHERE courseid='"+dropCourse+"' ";
			st.execute(sql);	
			//st.close();
			
			connection.close();
			
			
			} catch (SQLException e) {
				e.printStackTrace();
			}
			req.setAttribute("error"," course "+dropCourse+" removed from cart");
			req.getRequestDispatcher("Registration.jsp?userid="+userid+"").forward(req, resp);
		
			//req.getRequestDispatcher("/Student.jsp?userid="+userid+"").forward(req, resp);
			
		}
	

		if(checkOut!=null)
		{
			LinkedList<String> checkOutList=new LinkedList<>();
			HashMap<String, String> preReqList=new HashMap<>();
			HashMap<String, String> studentAcademicsList=new HashMap<>();
			LinkedList<String> proceedList=new LinkedList<>();
			LinkedList<String> errorCourseList=new LinkedList<>();

			
				
			try {
				Statement st=connection.createStatement();
				String table=userid+"Temp";	
		
			String sql="select * from "+table;
			ResultSet rs=st.executeQuery(sql);
			
			try {
			if(rs.getString(1)==null)
			{
				// if checklist is empty exception will occure that will do the result in catch
			}
			}
			catch (SQLException e) 
			{
				connection.close();
				
				req.setAttribute("error","Course  cart  is  empty ! Cant proceed");
				req.getRequestDispatcher("Registration.jsp?userid="+userid+"").forward(req, resp);
				return;
			}
			
			
			while(rs.next())
			{
				checkOutList.add(rs.getString(1));
			}
			//st.close();

			String sql1=" select COURSEID,PREREQUISITE from COURSES";
			ResultSet rs1=st.executeQuery(sql1);	
			while(rs1.next())
			{
				if(rs1.getString("prerequisite").length()==0)
				{
					preReqList.put(rs1.getString("courseid"),"empty");
							
				}
				else
				preReqList.put(rs1.getString("courseid"), rs1.getString("prerequisite"));
			}
			
			//st.close();

			String sql2=" select COURSEID,status from "+userid+"Academics ";   ///// here we started editing
			
			ResultSet rs2=st.executeQuery(sql2);	
			while(rs2.next())
			{
				if(rs2.getString(1)!=null)
				{
					studentAcademicsList.put(rs2.getString(1),rs2.getString(2));
				}
			}
			//st.close();

			// important concept starts from here 
			for(int i=0;i<checkOutList.size();i++)
			{
				String sub=checkOutList.get(i);
				String preReq= preReqList.get(sub);
			
				if(studentAcademicsList.size()==0)		 // if there is no subjects in studentAcademics table		
				{
				
					if( UserGuiDB.isCourseFree(connection,sub)==false)
					{
						notification = sub+"  course is full ! or Grading Finished !        Drop it";

						errorCourseList.add(sub);
					}
					
					else
					{
					
						if(preReq.equals("empty"))
						{
							
							proceedList.add(sub);
						}
						
						else
						{
							notification=sub+" need Pre requisite! Drop it!  Check course list ";
						
							
							errorCourseList.add(sub);
						}
						
					}
					
					
					
	
				}
				else   // if there are few  subjects in studentAcademics table		
				{	
				
					
						//if(UserGuiDB.findInList(studentAllCourseList,sub)==true )
						
					if(studentAcademicsList.containsKey(sub)==true)
						{ 
							notification = "You have taken "+sub+" course already! check Your 'Mycourse List'! Drop it";
					
							errorCourseList.add(sub);
						}
						
						else if( UserGuiDB.isCourseFree(connection,sub)==false)
						{
							notification = sub+"  course is full ! or Grading Finished !        Drop it";
							errorCourseList.add(sub);
						}
						
						//else if(UserGuiDB.findInList(studentAllCourseList,sub)==false)
						else if(studentAcademicsList.containsKey(sub)==false)// no this sub in student academics  and ..
						{
									if(preReq.equals("empty"))     // no pre reqs for selected subj so he can register.
									{
										proceedList.add(sub);
									}
							
									else    // if the selected sub has pre req
									{
										
												if(studentAcademicsList.containsKey(preReq)  && studentAcademicsList.get(preReq).equalsIgnoreCase("finished")==true ) // that pre req has to be in stuedent academics and it has to be finished.
													{
																		proceedList.add(sub);
													}
												
												else
												{
													notification=sub+" need Pre requisite! Drop it!  Check course list  ";
													errorCourseList.add(sub);
												}
									}		
						}
						
					
				 }
				
				}
	
		
	if(errorCourseList.size()==0)
	{
		String reCheck="";
		for(int i=0;i<proceedList.size();i++)
		{
		Exception ex=UserGuiDB.addCourse(connection, userid, proceedList.get(i));
		}
		String sql3="DELETE FROM "+userid+"Temp";
		st.execute(sql3);
		st.close();
		
			String success=" Course registration successful! ";

			UserGuiDB.changeSubjectDetails(connection, userid,proceedList);
			
			UserGuiDB.addToPayments(proceedList, userid, connection);
			
			
			//st.close();
			connection.close();
			
			req.setAttribute("error",success);
			req.getRequestDispatcher("Registration.jsp?userid="+userid+"").forward(req, resp);

		
	}
	else
	{
		String error="";
		for(int i=0;i<errorCourseList.size();i++)
		{
		error =error+ errorCourseList.get(i)+",";
		}		
		connection.close();
		
		req.setAttribute("error",notification);
		req.getRequestDispatcher("Registration.jsp?userid="+userid+"").forward(req, resp);
	
	}
		
			// important concept end here

	
	connection.close();
	
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			
			
			
		}
	
		
		
	}
	

	
}
