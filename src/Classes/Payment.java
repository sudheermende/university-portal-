package Classes;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.JOptionPane;

@WebServlet(urlPatterns="/Payment")
public class Payment extends HttpServlet{

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException 
	{	String paymentMethod=req.getParameter("payment");
		String amount=req.getParameter("amount");
	
		String userid=req.getParameter("userid");
		
		String pay=req.getParameter("pay");
		Connection connection=null;
		
	//	JOptionPane.showMessageDialog(null,amount+" "+userid+" "+paymentMethod);
	
		SqliteConnectionThread sqlthrd=new SqliteConnectionThread();
		sqlthrd.start();
		try 
		{
			sqlthrd.join();
		
	connection =sqlthrd.getConn();
	//	ResultSet rs=UserGuiDB.getUserFromDB(connection, userid);
		
		 if(pay!=null)
		{
			String cardno=req.getParameter("cardNumber");
			String cardName=req.getParameter("nameOnCard");
			String cvv=req.getParameter("cvv");
			
			String address=req.getParameter("address");
			String city=req.getParameter("city");
			String state=req.getParameter("state");
			String country=req.getParameter("country");
			String zip=req.getParameter("zip");

		String totalAddress=""
				+ "Address : "+address+"\n"
				+ "City    : "+city+"\n"
				+ "State   : "+state+"\n"
				+ "Country :"+country+"\n"
				+ "ZipCode :"+zip;
			
		
			String date= new Date().toString();
			
			String sql="insert into "+userid+"Payments values('payment',0,'success','-','CARD','"+cardName+"','"+cardno+"','"+amount+"',0,'"+date+"','"+totalAddress+"')";

			Statement st=connection.createStatement();
			st = connection.createStatement();
			st.execute(sql);
			st.close();
			
			double pendingMoney=UserGuiDB.getTotalMoney(connection, userid,"student");
			
			
			String sql2="update users set financialPendings="+pendingMoney+" where username='"+userid+"'";			
			
			st.execute(sql2);
			st.close();

			
			String sql3="insert into universityPayments values('payment for "+userid +"','success','"+date+"','CARD','"+cardName+"','"+cardno+"','"+totalAddress+"','"+amount+"',0)";
			st.execute(sql3);
			st.close();
		
			connection.close();
			req.setAttribute("error","Payment Success !");
			req.getRequestDispatcher("Student.jsp?userid="+userid+"").forward(req, resp);
			
			
			ResultSet rs=UserGuiDB.getUserFromDB(connection, userid);
			String email=rs.getString("email");
			String id=rs.getString("id");
			connection.close();
			
			String message=" Hi "+userid+","
					+ "\n\n Payment  on "+date+" was successful"
					+ "\n\n Student name :"+userid +"  ID :"+id
					+ "\n\n Paid With card number     : "+EncriptedFormat.getCard(cardno)
					+ "\n\ncard Holder name: "+cardName
					+ "\n\n Paid Amount               : "+amount
					+ "\n\n Payee Address             : \n"+totalAddress
										
					+ "\nThank you."
					+ "\n"
					+ "\n"
					+ "\nRegards"
					+ "\nAdmin depart"
					+ "\nS_university"; 
					
					String subj="Payment successful :"+userid;
	
					String[] to={email};
					
					EmailSender.sendMail(message,subj,to);
			
		}
	
		} catch (InterruptedException | SQLException e) 
		{
			e.printStackTrace();
			try {
				connection.close();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			req.setAttribute("error","Payment Failure !");
			req.getRequestDispatcher("Student.jsp?userid="+userid+"").forward(req, resp);
			
		}
		 
		 
	}
	

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String paymentMethod=req.getParameter("payment");
		String amount=req.getParameter("amount");
		String userid=req.getParameter("userid");
		
		if(paymentMethod.equalsIgnoreCase("card"))
		{
		 

		req.setAttribute("userid",userid);
		req.setAttribute("amount",amount);
		req.getRequestDispatcher("PayingWithCard.jsp").forward(req, resp);
		
		}
	}
	
}
