package Classes;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.JOptionPane;

@WebServlet(urlPatterns="/AddingNewMembers")
public class AddingNewMembers extends HttpServlet{

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	
		String userid=req.getParameter("userid");
		
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
		
		
	if(source.equalsIgnoreCase("AddFaculty"))
	{
		
		String password=req.getParameter("password").trim();
		String answer=req.getParameter("answer");
		String cell=req.getParameter("cell").trim();
		String confirmpassword=req.getParameter("confirmpassword").trim();
		String email=req.getParameter("email");
		String dob=req.getParameter("dob");
		String notification=null;
		String department=req.getParameter("department");
		String level=req.getParameter("level");
		String newFaculty=req.getParameter("newFaculty");
		String newCourse=req.getParameter("newCourse");

			UserGuiDB user=new UserGuiDB(newFaculty,"");
			boolean result=user.searchID(connection);
			
			if(result==false)
			{
				if(userid.length()==0 )
				{
					try {
						connection.close();
					} catch (SQLException e) 
					{
						e.printStackTrace();
					}
					notification=" User name should not be empty";
					req.setAttribute("error",notification);
					req.getRequestDispatcher("AddNewFaculty.jsp?userid="+userid+"&position=admin").forward(req, resp);
				}

			else if(answer.length()==0 )
				{
				try {
					connection.close();
				} catch (SQLException e) 
				{
					e.printStackTrace();
				}
					notification=" Security answer is empty";
					
					req.setAttribute("error",notification);
					req.getRequestDispatcher("AddNewFaculty.jsp?userid="+userid+"&position=admin").forward(req, resp);
				}
				
				else if((password.length()<6))
				{
					try {
						connection.close();
					} catch (SQLException e) 
					{
						e.printStackTrace();
					}
					notification=" password length should be more than 6 ";
					req.setAttribute("error",notification);
					
					req.getRequestDispatcher("AddNewFaculty.jsp?userid="+userid+"&position=admin").forward(req, resp);

				}
				 else if((password.equals(confirmpassword))==false)
				{
					 try {
							connection.close();
						} catch (SQLException e) 
						{
							e.printStackTrace();
						}
					 
				notification=" Confirmation password is not matched !";
				req.setAttribute("error",notification);
				req.getRequestDispatcher("AddNewFaculty.jsp?userid="+userid+"&position=admin").forward(req, resp);

				}
				
				 else{	
			
			
			
			
			UserGuiDB.CreatingNewUser(connection, newFaculty, confirmpassword, answer, cell, email, dob, level,"faculty", department);
			String message=" hi "+newFaculty+","
					+ "\nThank you for your interest to work for S_University."
					+ "\n You Have got Job!"+
					"\n you can Login to http://localhost:8085/S_University/Login.jsp?position=Faculty"
					+ "\n username :"+newFaculty
					+ "\n password :"+confirmpassword
					+ "\nThank you"
					+ "\n"
					+ "\n"
					+ "\nRegard"
					+ "\nAdmin depart"
					+ "\nS_university"; 
					
					String subj="Job application :"+userid;
					
					String[] to={email};
					
					EmailSender.sendMail(message,subj,to);
					req.setAttribute("notification",notification);

					try {
						connection.close();
					} catch (SQLException e) 
					{
						e.printStackTrace();
					}
					req.getRequestDispatcher("ViewFaculty.jsp?userid="+userid+"&faculty="+newFaculty).forward(req, resp);
					 }
				}
				else
				{

					try {
						connection.close();
					} catch (SQLException e) 
					{
						e.printStackTrace();
					}
					req.setAttribute("error","username already exist! Try new One");
					req.getRequestDispatcher("AddNewFaculty.jsp?userid="+userid+"&position=admin").forward(req, resp);
				}
		}
			
	else if(source.equalsIgnoreCase("AddCourse"))
		{
		String course=req.getParameter("newCourse");
		String courseId=req.getParameter("courseId").toUpperCase();
		String credits=req.getParameter("credits");
		String preReq=req.getParameter("preReq");
		String level=req.getParameter("level");
		String capacity=req.getParameter("capacity");
		String time=req.getParameter("time");
		String room=req.getParameter("room");
		String instructor=req.getParameter("instructor");
		String department=req.getParameter("department");
		double cost=0.0;
		
		if(level.equalsIgnoreCase("undergraduate"))
			cost=1200;
		else if(level.equalsIgnoreCase("graduate"))
			cost=1350;
		else if(level.equalsIgnoreCase("master"))
			cost=1500;
			
		String sql="Insert into courses values (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		try {
			PreparedStatement pst=connection.prepareStatement(sql);
		
			pst.setString(1, course);
			pst.setString(2, courseId.toUpperCase());
			pst.setString(3, department);
			pst.setString(4, credits);
			pst.setString(5, preReq);
			pst.setString(6, "TBD");
			pst.setString(7, time);
			pst.setString(8, room);
			pst.setString(9, capacity);
			pst.setDouble(10,0);
			pst.setString(11, "true");
			pst.setString(12, level);
			pst.setDouble(13, cost);
			pst.setString(14, "pending");
			
			pst.execute();
		
			UserGuiDB.createCourseListTable(connection, courseId.toUpperCase());
				connection.close();
			
			req.setAttribute("notification","new course created!");
			req.getRequestDispatcher("CourseList.jsp?userid="+userid+"").forward(req, resp);
		
			
			
		} catch (SQLException e) {
			
			req.setAttribute("notification","course name or courseId are already created!");
			req.getRequestDispatcher("CourseList.jsp?userid="+userid+"").forward(req, resp);
		
			
			
			e.printStackTrace();
		}
		
		}
	
	else if(source.equalsIgnoreCase("AddDepartment"))	
	{
		String department=req.getParameter("newDepartment");
		
		try {
			Statement st=connection.createStatement();
		
			String sql1="select * from departmentList";
			ResultSet rs=st.executeQuery(sql1);
			
			LinkedList<String> list= new LinkedList<>();
			while(rs.next())
			{
				list.add(rs.getString(1));
			}
			
		if(list.contains(department)==false)
		{	
			
			String sql2="Insert into departmentList values ('"+department+"')";
			st.execute(sql2); 

			connection.close();
			req.setAttribute("notification","new department added");
			req.getRequestDispatcher("DepartmentList.jsp?userid="+userid+"").forward(req, resp);
			
		}
		
		else
		{
			connection.close();
			req.setAttribute("notification","this department is already exist");
			req.getRequestDispatcher("AddNewDept.jsp?userid="+userid+"").forward(req, resp);

		}

		} catch (SQLException e) {
		try {
			connection.close();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
			e.printStackTrace();
		}
		
		
		
		
	}
	
	
	
			}	
			}
