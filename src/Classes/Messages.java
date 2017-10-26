package Classes;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.JOptionPane;

@WebServlet(urlPatterns="/Messages")
public class Messages extends HttpServlet{

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException 
	{
	
		String userid=req.getParameter("userid");
		String viewMsgNo=req.getParameter("viewMsgNo");
		String deleteMsgNo=req.getParameter("deleteMsgNo");
		String makeReadMsgNo=req.getParameter("makeReadMsgNo");
		String source=req.getParameter("source");
	
		
		if(source==null)
		{
			source="empty";
			/*when ever this class is called all operations done depends on source type.
			
			viewMsg and  deleteMsg are not used souce parameter in their jsp files, so if this get a call
			from that jsp source will be null. if it is null while it is checking all if conditions it gives you
			Error.class so it should not be null and should not satisfy any other if condition, so 
			assign any String to it.*/
			
		}
		
		
		
		SqliteConnectionThread sqlthrd=new SqliteConnectionThread();
		sqlthrd.start();
		try 
		{
			
			sqlthrd.join();
		 
		Connection connection =sqlthrd.getConn();

		Statement st=connection.createStatement();

		if(source.equalsIgnoreCase("inbox") || source.equalsIgnoreCase("sent") || source.equalsIgnoreCase("draft"))
		{
			connection.close();
		req.getRequestDispatcher("Messages.jsp?userid="+userid+"&msgBox="+source+"").forward(req,resp);
		}
		
		else	if(source.equalsIgnoreCase("newMsg"))
		{
			connection.close();
		
			req.getRequestDispatcher("CreateNewMsg.jsp?userid="+userid+"").forward(req,resp);
		}
		
		
		
	/// send and save ///	
		else if(source.equalsIgnoreCase("send") || source.equalsIgnoreCase("save"))
		{
			
			String fromOrTo=req.getParameter("fromOrTo");
			String sub=req.getParameter("subject");
			String msg=req.getParameter("message");
			String boxType="";
			
			
			
			Date date=new Date();
			
			if(source.equalsIgnoreCase("send"))
			{
			boxType="sent";
			
			UserGuiDB user=new UserGuiDB(fromOrTo.toLowerCase());
			
			boolean result=user.searchID(connection);

			if(result==false)
			{
				connection.close();
				

				req.setAttribute("fromOrTo", fromOrTo);
				req.setAttribute("subject", sub);
				req.setAttribute("message",msg);
				
				req.setAttribute("notification", fromOrTo+" id  not found, try some other");
				req.getRequestDispatcher("CreateNewMsg.jsp?userid="+userid+"").forward(req,resp);
			}
			else if(result==true)
			{
				String boxTypeForSendingIssue=req.getParameter("boxTypeForSendingIssue");
				if(boxTypeForSendingIssue.equalsIgnoreCase("draft"))
				{
					int msgNmbrForDraft=Integer.parseInt(req.getParameter("msgNumberForDraftOnly"));
					
					String sqlDelete="delete from "+userid+"Messages where no="+msgNmbrForDraft+"";
					
					st.execute(sqlDelete);
					
				}
				
				
				String sqlMsgTable=" CREATE TABLE IF NOT EXISTS '"+fromOrTo+"Messages' ('no' INTEGER,'boxType' VARCHAR,'fromOrTo' VARCHAR,'sub' VARCHAR,"+
						"'msg' VARCHAR, 'date' DATETIME, 'status' VARCHAR NOT NULL  DEFAULT unread)";
				
				st.execute(sqlMsgTable);
				
				int msgNmbr=UserGuiDB.getMsgNumberTOStore(connection, fromOrTo);
				
				String sql2="insert into "+fromOrTo+"Messages values("+(msgNmbr+1)+",'inbox','"+userid+"','"+sub+"','"
						+ msg+"','"+date+"','unread')";
				st.execute(sql2);
				
				msgNmbr=UserGuiDB.getMsgNumberTOStore(connection, userid);

				String sql1="insert into "+userid+"Messages values("+(msgNmbr+1)+",'sent','"+fromOrTo+"','"+sub+"','"
						+ msg+"','"+date+"','unread')";
				
				st.execute(sql1);
				connection.close();
				
				req.setAttribute("notification", " message sent successfully");
				req.getRequestDispatcher("Messages.jsp?userid="+userid+"&msgBox=Sent").forward(req,resp);
				
			}
	
		}
			else if(source.equalsIgnoreCase("save"))
			{
				int msgNmbrForDraft=0;		
				String boxTypeForSendingIssue=req.getParameter("boxTypeForSendingIssue");
				String sql1="";
				if(boxTypeForSendingIssue.equalsIgnoreCase("draft"))
				{	
					msgNmbrForDraft=Integer.parseInt(req.getParameter("msgNumberForDraftOnly"));
					int newMsgNmbr=UserGuiDB.getMsgNumberTOStore(connection, userid);
					sql1="update "+userid+"Messages set fromOrTo='"+fromOrTo+"', sub ='"+sub+"', msg='"+ msg+"', date='"+date+"',"
						+"no="+(newMsgNmbr+1)+ " where no="+(msgNmbrForDraft)+"";
				}
				else		
				{
					int newMsgNmbr=UserGuiDB.getMsgNumberTOStore(connection, userid)+1;
					
			sql1="insert into "+userid+"Messages values('"+newMsgNmbr+"','draft','"+fromOrTo+"','"+sub+"','"
					+ msg+"','"+date+"','unread')";
				}
			st.execute(sql1);
			
			connection.close();
			
			req.setAttribute("notification", "message saved successfully");
			req.getRequestDispatcher("Messages.jsp?userid="+userid+"&msgBox=Draft").forward(req,resp);
			}
		}
	/// send and save 	end
		
		else if(viewMsgNo!=null)
		{
			
			
			String msgStatus=st.executeQuery("select status from "+userid+"Messages where no="+viewMsgNo+"").getString(1);
			
			
			String boxType=req.getParameter("boxType");
			
			if(msgStatus.equalsIgnoreCase("unread"))
			{
				String sql="update "+userid+"Messages set status='read' where no="+viewMsgNo+"";
				st.execute(sql);
			}
			
			
			connection.close();
		
			req.getRequestDispatcher("CreateNewMsg.jsp?userid="+userid+"&msgNo="+viewMsgNo+"&newOrView=view").forward(req, resp);
		
		}
		else if(deleteMsgNo!=null)
		{
			String boxType=req.getParameter("boxType");

			String sql="DELETE FROM "+userid+"Messages WHERE no="+deleteMsgNo+"";
			st.execute(sql);
				
			connection.close();
			req.setAttribute("notification", "messages deleted");
			req.getRequestDispatcher("Messages.jsp?userid="+userid+"&msgBox="+boxType+"").forward(req, resp);
			
		}
		else if(makeReadMsgNo!=null)
		{
			
			
			String msgStatus=st.executeQuery("select status from "+userid+"Messages where no="+makeReadMsgNo+"").getString(1);
			
			if(msgStatus.equalsIgnoreCase("unread"))
			{
				String sql="update "+userid+"Messages set status='read' where no="+makeReadMsgNo+"";
				st.execute(sql);
			}
			
			
			connection.close();
		
			req.getRequestDispatcher("Messages.jsp?userid="+userid+"").forward(req, resp);
		
		}
		
		else if(source.equalsIgnoreCase("reply"))
		{
			String fromOrTo=req.getParameter("fromOrTo");
			String sub=req.getParameter("subject");
			String msg=req.getParameter("message");
			
			req.setAttribute("fromOrTo", fromOrTo);
			req.setAttribute("subject", sub);
			req.setAttribute("message",null);
			
			
			connection.close();
		
			req.getRequestDispatcher("CreateNewMsg.jsp?userid="+userid+"&newOrView=new").forward(req, resp);
		
		}
		
		else if(source.equalsIgnoreCase("forward"))
		{
			String fromOrTo=req.getParameter("fromOrTo");
			String sub=req.getParameter("subject");
			String msg=req.getParameter("message");
			
			req.setAttribute("fromOrTo", null);
			req.setAttribute("subject", sub);
			req.setAttribute("message",msg);
			
			
			connection.close();
		
			req.getRequestDispatcher("CreateNewMsg.jsp?userid="+userid+"&newOrView=new").forward(req, resp);
		
		}
		
		
		
		
		
		
		
		
		
	} catch (SQLException | InterruptedException e) 
		{
	// TODO Auto-generated catch block
	e.printStackTrace();
		}

		
		
	}
	
}
