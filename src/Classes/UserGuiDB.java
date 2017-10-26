package Classes;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class UserGuiDB extends UserGui {

	private Date time;

	// private Connection connection;

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public UserGuiDB() {
		// this.connection=connection;
		time = new Date();
	}

	public UserGuiDB(String name, String pswrd) {
		// this.connection=connection;
		setUserId(name);
		setPassword(pswrd);
		time = new Date();
	}

	public UserGuiDB(String name) {
		// this.connection=connection;
		setUserId(name);
		time = new Date();
	}

	// setting MSG,SUB,FROMID in INBOX TABLE
	public void setInboxmsg(Connection connection, String msg, String sub, String fromID, Date time) {
		// ResultSet rs = null;
		String tableName = getUserId() + "Inbox";
		try {
			String query = " INSERT INTO ? VALUES (?,?,?,?)";
			PreparedStatement pst = connection.prepareStatement(query);
			pst.setString(1, tableName);
			pst.setString(2, fromID);
			pst.setString(3, sub);
			pst.setString(4, msg);
			pst.setString(5, time.toString());
			pst.execute();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// setting MSG,SUB,FROMID in SENT TABLE
	public void setSentmsg(Connection connection, String msg, String sub, String fromID, Date time) {
		ResultSet rs = null;
		String tableName = getUserId() + "Sent";
		try {
			String query = " INSERT INTO ? VALUES (?,?,?,?)";// +tableName;
			PreparedStatement pst = connection.prepareStatement(query);
			pst.setString(1, tableName);
			pst.setString(2, fromID);
			pst.setString(3, sub);
			pst.setString(4, msg);
			pst.setString(5, time.toString());

			pst.execute();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	//// setting MSG,SUB,FROMID in DRAFT TABLE
	public void setDraftmsg(Connection connection, String msg, String sub, String fromID, Date time) {
		ResultSet rs = null;
		String tableName = getUserId() + "Draft";
		try {
			String query = " INSERT INTO ? VALUES (?,?,?,?)";// +tableName;
			PreparedStatement pst = connection.prepareStatement(query);
			pst.setString(1, tableName);
			pst.setString(2, fromID);
			pst.setString(3, sub);
			pst.setString(4, msg);
			pst.setString(5, time.toString());

			pst.execute();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// getting ONE MSG FROM INBOX
	public ResultSet getInboxmsgTable(Connection connection, int i) {
		ResultSet rs = null;
		String tableName = getUserId() + "Inbox";
		try {
			String query = "SELECT * FROM (SELECT ROW_NUMBER() OVER (ORDER BY ?) AS RowNum, * FROM ?) sub WHERE RowNum = ?";// +tableName;
			PreparedStatement pst = connection.prepareStatement(query);
			pst.setString(1, "Date");
			pst.setString(2, tableName);
			pst.setInt(3, i);
			rs = pst.executeQuery();

		} catch (Exception e2) {
			// JOptionPane.showMessageDialog(null,e2);
			e2.printStackTrace();
		}
		return rs;
	}

	// getting ONE MSG FROM SENT
	public ResultSet getSentmsgTable(Connection connection, int i) {
		ResultSet rs = null;
		String tableName = getUserId() + "Sent";
		try {
			String query = "SELECT * FROM (SELECT ROW_NUMBER() OVER (ORDER BY ?) AS RowNum, * FROM ?) sub WHERE RowNum = ?";// +tableName;
			PreparedStatement pst = connection.prepareStatement(query);
			pst.setString(1, "Date");
			pst.setString(2, tableName);
			pst.setInt(3, i);
			rs = pst.executeQuery();

		} catch (Exception e2) {
			// JOptionPane.showMessageDialog(null,e2);
			e2.printStackTrace();
		}
		return rs;
	}

	// getting ONE MSG FROM DRAFT
	public ResultSet getDraftmsgTable(Connection connection, int i) {
		ResultSet rs = null;
		String tableName = getUserId() + "Draft";
		try {
			String query = "SELECT * FROM (SELECT ROW_NUMBER() OVER (ORDER BY ?) AS RowNum, * FROM ?) sub WHERE RowNum = ?";// +tableName;
			PreparedStatement pst = connection.prepareStatement(query);
			pst.setString(1, "Date");
			pst.setString(2, tableName);
			pst.setInt(3, i);
			rs = pst.executeQuery();

		} catch (Exception e2) {
			// JOptionPane.showMessageDialog(null,e2);
			e2.printStackTrace();
		}
		return rs;
	}

	/// getting TOTAL INBOX
	public ResultSet getInBoxTable(Connection connection) {

		ResultSet rs = null;
		String tableName = getUserId() + "Inbox";
		try {
			String query = " select * FROM " + tableName + " ORDER BY time DESC";// +tableName;

			// String query=" select fromID,sub,message,ROW_NUMBER() OVER(ORDER
			// BY time DESC) AS RowNumber from "+tableName;

			PreparedStatement pst = connection.prepareStatement(query);
			rs = pst.executeQuery();

		} catch (Exception e2) {
			// JOptionPane.showMessageDialog(null,e2);
			e2.printStackTrace();
		}
		return rs;
	}

	/// getting TOTAL SENT
	public ResultSet getSentTable(Connection connection) {
		ResultSet rs = null;
		String tableName = getUserId() + "Sent";
		try {
			String query = " select * FROM " + tableName + " ORDER BY time DESC";// +tableName;
			PreparedStatement pst = connection.prepareStatement(query);
			rs = pst.executeQuery();

		} catch (Exception e2) {
			// JOptionPane.showMessageDialog(null,e2);
			e2.printStackTrace();
		}
		return rs;
	}

	/// getting TOTAL DRAFT
	public ResultSet getDraftTable(Connection connection) {
		ResultSet rs = null;
		String tableName = getUserId() + "Draft";
		try {

			String query = " select toid,sub,message,time FROM " + tableName + " ORDER BY time DESC";// +tableName;

			// String query="select rowid, ?.* from ( select * from ? ) ?";

			// String query="SELECT *,ROW_NUMBER() OVER (ORDER BY (SELECT 1)) AS
			// SNO FROM (?)";
			PreparedStatement pst = connection.prepareStatement(query);
			// pst.setString(1,tableName);
			// pst.setString(2,tableName);
			// pst.setString(3,tableName);

			rs = pst.executeQuery();

		} catch (Exception e2) {
			// JOptionPane.showMessageDialog(null,e2);
			e2.printStackTrace();
		}
		return rs;
	}

	// getting the INBOX MSGS NUMBER
	public int getInboxCount(Connection connection) {
		String sql = "SELECT COUNT(*) FROM " + getUserId() + "Inbox";
		try {
			PreparedStatement pst = connection.prepareStatement(sql);
			ResultSet rs = pst.executeQuery();
			int i = (int) rs.getObject(1);

			return i;

		} catch (SQLException e) {

			e.printStackTrace();
		}
		return 0;
	}

	// getting the SENT MSGS NUMBER
	public int getSentCount(Connection connection) {

		String sql = "SELECT COUNT(*) FROM " + getUserId() + "Sent";
		try {
			PreparedStatement pst = connection.prepareStatement(sql);
			ResultSet rs = pst.executeQuery();
			int i = (int) rs.getObject(1);

			return i;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	// getting the DRAFT MSGS NUMBER
	public int getDraftCount(Connection connection) {

		String sql = "SELECT COUNT(*) FROM " + getUserId() + "Draft";
		try {
			PreparedStatement pst = connection.prepareStatement(sql);
			ResultSet rs = pst.executeQuery();
			int i = (int) rs.getObject(1);

			return i;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	// DELETING AN ACCOUNT
	public void DeletingAccount(Connection connection) {
		try {
			String query = "DELETE FROM USERS WHERE username=? and password=?";
			PreparedStatement pst = connection.prepareStatement(query);
			pst.setString(1, getUserId());
			pst.setString(2, getPassword());
			pst.execute();

			JOptionPane.showConfirmDialog(null, getUserId() + " name");

			String inbox = " DROP Table " + getUserId() + "Inbox";

			PreparedStatement pstinbox = connection.prepareStatement(inbox);
			pstinbox.execute();

			String sent = " DROP Table " + getUserId() + "Sent";

			PreparedStatement pstSent = connection.prepareStatement(sent);
			pstSent.execute();

			String draft = " DROP Table " + getUserId() + "Draft";

			PreparedStatement pstDraft = connection.prepareStatement(draft);
			pstDraft.execute();

		} catch (Exception e2) {
			// JOptionPane.showMessageDialog(null,e2);
			e2.printStackTrace();

		}
	}

	// SEARCHING FOR AN ACCOUNT IN DB (checks only userID) (use for FORGET
	// PASSWORD and SIGNUP )

	public boolean searchID(Connection connection) {

		try {
			String query = "select UserName from Users where UPPER(username)=UPPER(?)";

			PreparedStatement pst = connection.prepareStatement(query);
			pst.setString(1, getUserId());
			ResultSet rs = pst.executeQuery();
			int count = 0;
			while (rs.next()) {
				count++;
			}

			if (count == 1) {

				return true;
			} else if (count > 1) {
				return false; // we dont face this proble. ( DB DOESNT STORE
								// DUPLICATE USERS )
			} else if (count == 0) {

				return false;
			}

			rs.close();

		} // try close
		catch (Exception e1) {
			JOptionPane.showMessageDialog(null, e1);
		}

		return false;
	}

	// SEARCHING FOR AN ACCOUNT IN DB (checks only userID and PASSWORD) (use for
	// LOGIN PASSWORD and DELETE ACCOUNT )
	public boolean searchAccount(Connection connection) {

		try {
			String query = "select UserName,password from Users where UPPER(username)=UPPER(?) AND password=? AND status=(?)";

			PreparedStatement pst = connection.prepareStatement(query);
			pst.setString(1, getUserId());
			pst.setString(2, getPassword());
			pst.setString(3, "active");

			ResultSet rs = pst.executeQuery();
			int count = 0;
			while (rs.next()) {
				count++;
			}

			if (count == 1) {

				return true;
			} else if (count > 1) {
				return false; // we dont face this proble. ( DB DOESNT STORE
								// DUPLICATE USERS )
			} else if (count == 0) {
				return false;
			}
			rs.close();
		} // try close
		catch (Exception e1) {
			
		}

		return false;
	}

	public String getPassWordForUser(Connection connection) throws SQLException {
		String sql = "select password from USERS where UPPER(username)=UPPER(?)";
		PreparedStatement pst = connection.prepareStatement(sql);
		pst.setString(1, getUserId());
		ResultSet rs = pst.executeQuery();
		String pswrd = rs.getString("password");

		return pswrd;
	}

	public void setPassWordForUser(Connection connection, String pswrd) throws SQLException {
		String sql = "UPDATE users set password=? where UPPER(username)=UPPER(?)";
		PreparedStatement pst = connection.prepareStatement(sql);
		pst.setString(1, pswrd);
		pst.setString(2, getUserId());
		pst.execute();

	}

	// create inbox,sent,draft anyway
	public static Exception CreatingTablesIfNotCreated(Connection localConn, String name,String position) {
		String inboxName = name + "Inbox";
		String sentName = name + "Sent";
		String draftName = name + "Draft";
		String academicName = name + "Academics";
		String payments = name + "Payments";
		String courses=name+"Courses";
		String messages=name+"Messages";
		Statement statement;
		
		try {
			statement = localConn.createStatement();

			/*
			 * String sqlInbox=" create table IF NOT EXISTS "+inboxName+
			 * " (fromID varchar(300),sub varchar(300),message varchar(300),time DATETIME NOT NULL  DEFAULT CURRENT_TIMESTAMP )"
			 * ; String sqlSent=" create table IF NOT EXISTS "+sentName+
			 * " (toID varchar(300),sub varchar(300),message varchar(300),time  DATETIME NOT NULL  DEFAULT CURRENT_TIMESTAMP )"
			 * ; String sqlDraft=" create table IF NOT EXISTS "+draftName+
			 * " (toID varchar(300),sub varchar(300),message varchar(300),time DATETIME NOT NULL  DEFAULT CURRENT_TIMESTAMP )"
			 * ;
			 */
			
			String sqlMsgTable=" CREATE TABLE IF NOT EXISTS '"+messages+"' ('no' INTEGER,'boxType' VARCHAR,'fromOrTo' VARCHAR,'sub' VARCHAR,"+
					"'msg' VARCHAR, 'date' DATETIME, 'status' VARCHAR NOT NULL  DEFAULT unread)";
			
			statement.execute(sqlMsgTable);
			
			
			String usersTable="CREATE table IF NOT EXISTS 'users' ('username' VARCHAR PRIMARY KEY  NOT NULL ,'password' VARCHAR NOT NULL ,"+
					"'inbox' VARCHAR NOT NULL ,'sent' VARCHAR NOT NULL ,'draft' VARCHAR NOT NULL ,'question' VARCHAR NOT NULL ,"+
					"'answer' VARCHAR NOT NULL ,'registereddate' VARCHAR,'cell' varchar DEFAULT (null) ,'email' VARCHAR,'dateofbirth' "+
				    "VARCHAR,'position' VARCHAR,'level' VRACHAR,'department' VARCHAR NOT NULL ,'id' INTEGER DEFAULT (null) , 'status'"+
					" VARCHAR NOT NULL  DEFAULT active, 'Grade' INTEGER DEFAULT 0, 'financialPendings' DOUBLE)";
			
			String courseTable="CREATE TABLE IF NOT EXISTS 'courses' ('course' VARCHAR PRIMARY KEY  NOT NULL  DEFAULT (null) ,'courseId' VARCHAR NOT NULL  DEFAULT (null) ,"
					+ "'department' VARCHAR NOT NULL ,'credits' INTEGER NOT NULL ,'prerequisite' VARCHAR,'instructor' VARCHAR NOT NULL ,'time' VARCHAR DEFAULT (null) ,"
					+"'room' VARCHAR,'maxStudents' INTEGER,'studentsCount' INTEGER DEFAULT (0) ,'isOpen' BOOL,'level' VARCHAR,'cost' FLOAT DEFAULT (1350) )";
			
			statement.execute(usersTable);
			statement.execute(courseTable);
			
			
			if( /*position.equalsIgnoreCase("applicant") || */ position.equalsIgnoreCase("student"))
			{
			String sqlAcademics = " create table IF NOT EXISTS " + academicName
					+ "(courseId varchar(300) PRIMARY KEY  NOT NULL  UNIQUE,status varchar(300) NOT NULL  DEFAULT 'pursuing',grade varchar(300) NOT NULL  DEFAULT 'pending',gpa FLOAT NOT NULL  DEFAULT 'pending')";
			String sqlPayments = "CREATE TABLE IF NOT EXISTS " + payments
					+ " ('task' VARCHAR,'amount' DOUBLE,'status' VARCHAR,'addedDate' VARCHAR,'paymentType' VARCHAR,'cardHolderName' VARCHAR,'cardNo' VARCHAR,'paidAmount' DOUBLE,"
					+ "'dueAmount' DOUBLE,'paidDate' VARCHAR,'CardHolderAddress' VARCHAR)";

			String table = name + "Temp";
			
				String sql = "CREATE TABLE IF NOT EXISTS '" + table
						+ "' ('courseid' VARCHAR PRIMARY KEY  NOT NULL  UNIQUE )";
				
			/*
			 * statement.execute(sqlInbox); statement.execute(sqlSent);
			 * statement.execute(sqlDraft);
			 */
			statement.execute(sqlAcademics);
			statement.execute(sqlPayments);
			statement.execute(sql);
			}
			
			else 			
			if(position.equalsIgnoreCase("faculty"))
			{
				String sqlPayments = "CREATE TABLE IF NOT EXISTS " + payments
						+ " ('task' VARCHAR,'amount' DOUBLE,'status' VARCHAR,'addedDate' VARCHAR,'paymentType' VARCHAR,'cardHolderName' VARCHAR,'cardNo' VARCHAR,'paidAmount' DOUBLE,"
						+ "'dueAmount' DOUBLE,'paidDate' VARCHAR,'CardHolderAddress' VARCHAR)";
				
				//String sqlCourses="CREATE TABLE IF NOT EXISTS "+courses+" ('courseid' VARCHAR)";
			
				//statement.execute(sqlCourses);
				statement.execute(sqlPayments);
			}
			
				else 			
					if(position.equalsIgnoreCase("admin"))
					{
						String sqlPayments =" CREATE TABLE IF NOT EXISTS 'universityPayments' ('task' VARCHAR, 'status' VARCHAR, 'paidDate' VARCHAR, 'paymentType' VARCHAR, 'nameOnCard' VARCHAR,"+ 
								"'cardNumber' VARCHAR, 'address' VARCHAR,'amountIn' DOUBLE DEFAULT (0) ,'amountOut' DOUBLE DEFAULT (0))";
						statement.execute(sqlPayments);
					}
			return null;
		} 
		
		catch (SQLException e) {
			/// JOptionPane.showConfirmDialog(null,"exsptn in creatng
			/// tablsifntfnd");
			e.printStackTrace();
			return e;
		}

	}

	// Creating New User In DB
	public static void CreatingNewUser(Connection localConn, String name, String password, String answer, String cell,
			String email, String dob, String level,String position, String department) {

		String inboxName = name + "Inbox";
		String sentName = name + "Sent";
		String draftName = name + "Draft";
		  								// only Applicant can register so
										// position always apllicant( then their
										// position will change to student or
										// alumni//
		Date time = new Date();
		int id = UserGuiDB.createIdForAnyList(localConn, "position", position);
		int grade = 0;
		try {
			String sql = "insert into USERS values(?,?,?,?,?,'What is your school name',?,?,?,?,?,?,?,?,?,?,?,?)";
			UserGuiDB.createIdForAnyList(localConn, "position", "applicant");
			PreparedStatement pst1 = localConn.prepareStatement(sql);
			pst1.setString(1, name);
			pst1.setString(2, password);
			pst1.setString(3, inboxName);
			pst1.setString(4, sentName);
			pst1.setString(5, draftName);
			pst1.setString(6, answer);
			pst1.setString(7, time.toString());
			pst1.setString(8, cell);
			pst1.setString(9, email);
			pst1.setString(10, dob);
			pst1.setString(11, position);
			pst1.setString(12, level);
			pst1.setString(13, department);
			pst1.setLong(14, id);
			pst1.setString(15, "active");/// status is actice for all as default
			pst1.setLong(16, grade);// grade is 0 for applicants as default
			pst1.setLong(17, grade);
			pst1.execute();

			
			UserGuiDB.CreatingTablesIfNotCreated(localConn, name,position);
/*
			String applicationFee = " insert into " + name + "Payments values ('Application Fee',100,'paid','"
					+ new Date() + "','cash','','',100,0,'" + new Date() + "')";
			PreparedStatement pst2 = localConn.prepareStatement(applicationFee);

			pst2.execute();
*/
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static String getUserPosition(Connection connection, String name) {
		try {

			Statement statement = connection.createStatement();
			String sql = "select position from USERS where username=LOWER( '" + name + "')";
			ResultSet rs = statement.executeQuery(sql);

			String position = rs.getString("position");
			return position;

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;

	}

	/// giving the inbox in lable model
	public JPanel inboxPanel(Connection connection) {

		JPanel boxPanel = new JPanel();

		ResultSet rs = null;
		String tableName = getUserId() + "Inbox";
		try {
			String query = " select * FROM " + tableName + " ORDER BY time DESC";// +tableName;
			PreparedStatement pst = connection.prepareStatement(query);
			rs = pst.executeQuery();

			while (rs.next()) {
				String labelName = rs.getString("fromId") + "    " + rs.getString("sub") + "     "
						+ rs.getString("message");

				JLabel lb = new JLabel(labelName);

				boxPanel.add(lb);

			}

			boxPanel.setVisible(true);
			return boxPanel;

		} catch (Exception e2) {
			// JOptionPane.showMessageDialog(null,e2);
			e2.printStackTrace();
		}

		return boxPanel;
	}

	public static boolean searchSecurityAnswerForUser(Connection connection, String name, String answer) {
		try {
			Statement statement = connection.createStatement();
			String sql = "select answer from USERS where username=LOWER( '" + name + "')";
			ResultSet rs = statement.executeQuery(sql);

			String ans = rs.getString("answer");
			if (ans.equalsIgnoreCase(answer))
				return true;
			else
				return false;

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}

	public static boolean resetPassword(Connection connection, String name, String password) {
		try {
			Statement statement = connection.createStatement();

			String sql = "UPDATE  USERS set password='" + password + "' where UPPER(username)=UPPER('" + name + "')";

			statement.execute(sql);
//			statement.close();
	//		

			return true;

		} catch (SQLException e) {

			e.printStackTrace();
		}

		return false;

	}

	public static ResultSet getUserFromDB(Connection connection, String userid) {
		String sql = "select * from users where username='" + userid + "'";
		ResultSet rs = null;
		Statement st;
		try {
			st = connection.createStatement();
			rs = st.executeQuery(sql);
			return rs;

		} catch (SQLException e) {
			e.printStackTrace();
			return rs;
		}
	}

	public static ResultSet getAnyListFromDB(Connection connection, String columnName, String search) {
		String sql = "select * from users where " + columnName + "='" + search + "' order by id desc";
		ResultSet rs = null;
		Statement st;
		try {
			st = connection.createStatement();
			rs = st.executeQuery(sql);

			return rs;

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rs;
	}

	public static ResultSet getTempTable(Connection connection, String name) {
		String sql = "select * from " + name + "Temp ";
		ResultSet rs = null;
		Statement st;
		try {
			st = connection.createStatement();
			rs = st.executeQuery(sql);

			return rs;

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rs;
	}

	public static ResultSet getCourseListFromDB(Connection connection) {
		String sql = "select * from courses order by department";
		ResultSet rs = null;
		Statement st;
		try {
			st = connection.createStatement();
			rs = st.executeQuery(sql);

			return rs;

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rs;
	}

	public static ResultSet getDepartmentListFromDB(Connection connection) {
		String sql = "select departmentList.departmentName,count(courses.department) from departmentList LEFT JOIN courses on departmentList.departmentName=courses.department GROUP BY departmentList.departmentName";
		ResultSet rs = null;
		Statement st;
		try {
			st = connection.createStatement();
			rs = st.executeQuery(sql);

			return rs;

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rs;
	}

	public static int getCountOfAnyListFromDB(Connection connection, String columnName, String search) {
		String sql = "SELECT COUNT(" + columnName + ") FROM users where " + columnName + "='" + search + "'";
		int count = 20;
		ResultSet rs = null;
		Statement st;
		try {
			st = connection.createStatement();
			rs = st.executeQuery(sql);
			
			count = rs.getInt(count + "(" + columnName + ")");

			JOptionPane.showMessageDialog(null, "from getCountOfAnyList method" + rs.toString());
			return count;

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}

	public static int createIdForAnyList(Connection connection, String columnName, String search) {

		String sql = "select id from users where " + columnName + "='" + search + "' order by id desc";
		/*
		 * in above sql query if we put order by asc that will create the id
		 * number by searching from first number to last number, if middle
		 * number is not available it creates that middle number!
		 * 
		 * 
		 * if we use order by desc it creates the next number to latest id
		 * number! (better to use)
		 */

		ResultSet rs = null;
		Statement st;
		ResultSet rs1 = null;
		Statement st1;

		boolean found = false;
		int newId;

		try {
			st = connection.createStatement();
			rs = st.executeQuery(sql);
			int count = 0;
			String query = "select count(position) from users where position='" + search + "'";
			st1 = connection.createStatement();
			rs1 = st1.executeQuery(query);

			if (rs1.getLong("count(position)") == 0) {
				if (search.equalsIgnoreCase("applicant"))
					count = 1000;
				if (search.equalsIgnoreCase("student"))
					count = 2000;
				if (search.equalsIgnoreCase("faculty"))
					count = 3000;
			} else
				count = rs.getInt("id");

			for (newId = count + 1;; newId++) {
				found = UserGuiDB.findId(connection, newId);
				if (found == true) {
					continue;
				} else {
					return newId;
				}

			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		// return rs;

		return 1;
	}

	public static boolean findId(Connection connection, int id) {

		try {
			Statement st = connection.createStatement();
			String query = "SELECT id FROM users where id=" + id;
			// create a statement which check the newId in database weather that
			// id is already exist or not

			st = connection.createStatement();
			ResultSet rs = st.executeQuery(query);

			int i = 0;
			while (rs.next()) {
				i++;
			}

			if (i != 0) {
				return true;
			} else
				return false;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}
		return false;
	}

	public static ResultSet getStudentCourseList(Connection connection, String name, String status) {
		String table = name + "Academics";
		String sql;
		if(status.equalsIgnoreCase("full"))
		{
			sql = "SELECT " + table + ".courseid,courses.course,courses.department,courses.instructor," + table
					+ ".grade," + table + ".gpa  from " + table + ",courses where " + table
					+ ".courseid=courses.courseid";

		}
		
		// String sql="SELECT * from "+table+" where status='"+status+"'";
		else{
		sql = "SELECT " + table + ".courseid,courses.course,courses.department,courses.instructor," + table
				+ ".grade," + table + ".gpa  from " + table + ",courses where " + table
				+ ".courseid=courses.courseid and " + table + ".status='" + status + "'";
		}
		ResultSet rs = null;
		Statement st;
		try {
			st = connection.createStatement();
			rs = st.executeQuery(sql);
			// st.close();

			return rs;

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	public static Exception addCourse(Connection connection, String userid, String subj) {
		Statement st;
		try {
			st = connection.createStatement();

			String sql = " insert into " + userid + "Academics values('" + subj + "','pursuing','pending','pending')";
			st.execute(sql);
		return null;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		return e;
		}

	}

	public static boolean findInList(LinkedList<String> list,String word)
{
	for(int j=0;j<list.size();j++)
		{

		if(word.equalsIgnoreCase(list.get(j)))
		{
			return true;
		}
		}
	return false;
}

	public static boolean findInMap(HashMap<String,String> map, String word)
	{
	if(map.containsKey("word"))
	{
		
		
	}
		
		return false;
	}
	
	public static void addToPayments(LinkedList<String> list,String userid,Connection connection)
	{
		try {
			Statement st=connection.createStatement();
			HashMap<String,Double> paymentList=new HashMap<>();
			HashMap<String, String> courseList=new HashMap<>();
			
		
		
		String sql="select courseid,cost from courses";
		ResultSet rs=st.executeQuery(sql);
		
		while(rs.next())
		{
			courseList.put(rs.getString(1),rs.getString(2));
		}
		
		for(int i=0;i<list.size();i++)
		{
			String task=list.get(i)+" registration";
			double amount =Double.parseDouble(courseList.get(list.get(i)));

			/*       if(level.equalsIgnoreCase("undergraduate"))
			{
			amount=400;
			}
			else if (level.equalsIgnoreCase("graduate"))
			{
			amount=450;
			}
			else if (level.equalsIgnoreCase("master"))
			{
			amount=500;
			}
*/		
			paymentList.put(task,amount);
		
		}
		
		
		Iterator it = paymentList.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry pair = (Map.Entry)it.next();
	      
	        Date date=new Date();
	        double money=(double) pair.getValue();
	        
	        String sql2 = "insert into "+userid+"Payments values(?,?,?,?,?,?,?,?,?,?,?)";
			PreparedStatement pst1 = connection.prepareStatement(sql2);
			pst1.setString(1, pair.getKey().toString());
			pst1.setString(2, pair.getValue().toString());
			pst1.setString(3, "pending");
			pst1.setString(4, date.toString());
			pst1.setString(5, "");
			pst1.setString(6, "");
			pst1.setString(7, "");
			pst1.setDouble(8, 0);
			pst1.setString(9, pair.getValue().toString());
			pst1.setString(10,"");
			pst1.setString(11,"");
			
			pst1.execute();
	        
	        it.remove(); // avoids a ConcurrentModificationException
	    }
		
		double money=getTotalMoney(connection, userid,"student");
		
		String query="update users set financialPendings="+money+" where username='"+userid+"'";
		st.execute(query);
		
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

	public static double getTotalMoney(Connection connection,String userid,String position)
	{
		double totalMoney=0;
		double paidMoney=0;
		double pendingMoney=0;
		double amountIn=0;
		double amountOut=0;
		
		if(position.equalsIgnoreCase("admin"))
		{
			try {
				
				Statement st=connection.createStatement();
				ResultSet rs=st.executeQuery("select * from "+userid+"Payments");

				while(rs.next())
				{
					amountIn=amountIn+rs.getDouble("amountIn");
					amountOut=amountOut+rs.getDouble("amountOut");
					
				}
			
				st.close();
					} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return amountIn;//-amountOut;
		}
		else
		try {
			
			Statement st=connection.createStatement();
			ResultSet rs=st.executeQuery("select * from "+userid+"Payments");

			while(rs.next())
			{
				totalMoney=totalMoney+rs.getDouble("amount");
				paidMoney=paidMoney+rs.getDouble("paidAmount");
				pendingMoney=pendingMoney+rs.getDouble("dueAmount");
			}
		
			st.close();
				} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return totalMoney-paidMoney;
	}
	
	public static boolean isCourseFree(Connection connection,String course)
	{
		Statement st;
		try {
			st = connection.createStatement();
		
			ResultSet rs=st.executeQuery("select maxStudents, studentsCount,grading from courses where courseId='"+course+"'");
		
			int max=rs.getInt(1); 
			int enrolled=rs.getInt(2);
			String grading=rs.getString(3); 
			int available=max-enrolled;

			
			
			if(available>0  && grading.equals("pending")  )
			{
				return true;
			}
			else
				return false;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	public static void changeSubjectDetails(Connection connection,String userid,LinkedList<String> course)
	{
		
		for(int i=0;i<course.size();i++)
		{
		
		try {
			Statement st=connection.createStatement();
			createCourseListTable(connection,course.get(i));
			
			String sql2="insert into "+course.get(i)+"List values('"+userid+"')";
			st.execute(sql2);	
			
			String sql3="select studentsCount from courses where courseId='"+course.get(i)+"'";
			ResultSet rs3=st.executeQuery(sql3);
			int studentsCount=rs3.getInt("studentsCount");
			studentsCount++;
			
			
			String sql4="update courses set studentsCount="+studentsCount+" where courseId='"+course.get(i)+"'";
			st.execute(sql4);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
		
		
	}

	public static void createCourseListTable(Connection connection,String course)

	{
		
		System.out.println(" course : "+course);
		Statement st;
		try {
			st = connection.createStatement();
		
		String sql="create Table If Not EXISTS "+course+"List(studentName varchar(300) PRIMARY KEY)";
		String sql1="create Table If Not EXISTS "+course+"Details(task VARCHAR, details VARCHAR, "
				+ "postedDate VARCHAR, lastDate VARCHAR, marks INTEGER)";
		
		String sql2="CREATE TABLE If Not EXISTS MarksList (studentName VARCHAR, courseId VARCHAR, task VARCHAR,"+
				"answer VARCHAR, submittedDate VARCHAR, maxMarks  INTEGER DEFAULT (0),scoredMarks VARCHAR DEFAULT 0)";
		st.execute(sql);
		st.execute(sql1);
		st.execute(sql2);
		
		} catch (SQLException e) {
		System.out.println("error 2 @1116");
			e.printStackTrace();
		}
	}
	
	public static boolean makePayment(Connection connection,String userid,HashMap<String, String> map)
	{
		JOptionPane.showMessageDialog(null,"in UserGuiDB @ 1024");
		try {
		Statement st=connection.createStatement();
		
		String date=new Date().toString();
		
		double amount=Double.parseDouble(map.get("amount"));
		
		
		String sql="insert into "+userid+"Payments values('"+map.get("task")+"',0,'"+map.get("status")+"',"+
				"' ','CARD','"+map.get("cardHolderName")+"','"+map.get("cardNO")+"','"+amount+"',0,'"+date+"','"+map.get("address")+"')";

		st.execute(sql);
		st.close();
		
		/*String sql1="insert into "+userid+"Payments ('task','amount','status','paymentType','cardHolderName','cardNo',"
				+ "'paidDate','cardHolderAddress') values(?,?,?,?,?,?,?,?)";
		
		PreparedStatement pst=connection.prepareStatement(sql1);
		pst.setString(1, map.get("task"));
		pst.setString(2, map.get("amount"));
		pst.setString(3, map.get("status"));
		pst.setString(4, map.get("paymentType"));
		pst.setString(5, map.get("cardHolderName"));
		pst.setString(6, map.get("cardNo"));
		pst.setString(7, new Date().toString());
		pst.setString(8, map.get("address"));
		
		pst.execute();
		pst.close();*/
		
		
			
			/*
		*/	
			return true;
			
		} catch (SQLException e) {
			e.printStackTrace();
		return false;
		}
						
	}
	
	public static boolean isLoggedIn(Connection connection,String userid)
	{
		try {
			
		String sql="select status from users where username='"+userid+"'";
			Statement st=connection.createStatement();
			ResultSet rs=st.executeQuery(sql);
			String status =rs.getString("status");
			st.close();
			
			if(status.equalsIgnoreCase("active"))
			return false;
			else
			return true;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return false;
	}
		
	public static ResultSet getAllStudentsTasks(Connection connection,String courseId,String task,String singleOrAll)
	{
		String course=courseId+"List";
		String sql1="";
		
		try {
		Statement st=connection.createStatement();
		
		if(singleOrAll.equalsIgnoreCase("all"))
		{
		sql1= "select "+course+".studentName ,MarksList.answer,MarksList.courseId,MarksList.task,MarksList.submittedDate,MarksList.maxMarks,"
				+ "MarksList.scoredMarks,MarksList.editable from "+course+"  LEFT JOIN MarksList  on "+course+".studentName=MarksList.studentName  and "
 		+ " MarksList.courseId='"+courseId+"' and MarksList.task='"+task+"'";
		}
		else
		{
		sql1= "select "+course+".studentName ,MarksList.courseId,MarksList.task,MarksList.answer,MarksList.submittedDate,MarksList.maxMarks,"
				+ "MarksList.scoredMarks,MarksList.editable from "+course+"  LEFT JOIN MarksList  on "+course+".studentName=MarksList.studentName  and "
		+ " MarksList.courseId='"+courseId+"' and MarksList.task='"+task+"' where "+course+".studentName='"+singleOrAll+"'";
			
		
		}
		
			ResultSet rs=
					st.executeQuery(sql1);
			return rs;
			
		} catch (SQLException e) {
			e.printStackTrace();
		return null;
		}
	}
	
	public static int getMsgNumberTOStore(Connection connection,String userid)
	{
		String sql="select no from "+userid+"Messages";
		
		try {
			Statement st=connection.createStatement();
		
		ResultSet rs=st.executeQuery(sql);
		
		int i=0;
		
		while(rs.next())
		{
			if(i<rs.getInt(1))
			i=rs.getInt(1);
		}
		
		return i;
		
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
		
	}
	
	/*
	 * // checking if user is opened or not public static int isOpen(Connection
	 * localConn,String name) { try { String sql=
	 * "select isOpen from USERS where UPPER(username)=UPPER(?)";
	 * PreparedStatement pst=localConn.prepareStatement(sql);
	 * 
	 * 
	 * pst.setString(1,name);
	 * 
	 * 
	 * ResultSet rs=pst.executeQuery(); int status=rs.getInt("isOpen");
	 * 
	 * return status; } catch (SQLException e) { // TODO Auto-generated catch
	 * block e.printStackTrace(); }
	 * 
	 * return 0;
	 * 
	 * }
	 * 
	 * // changing the status of The User Account to Zero (closing) public
	 * static void changeTheStatusToClose(String name) {
	 * 
	 * try {
	 * 
	 * SqliteConnectionThread thread=new SqliteConnectionThread();
	 * thread.start(); thread.join(); Connection localConn=thread.getConn();
	 * 
	 * JOptionPane.showMessageDialog(null,name+" changing status Open to close"
	 * );
	 * 
	 * String sql="UPDATE USERS set isOpen=0 where username='"+name+"'";
	 * Statement st=localConn.createStatement();
	 * 
	 * st.execute(sql);
	 * 
	 * 
	 * localConn.close(); } catch (SQLException | InterruptedException e) { //
	 * TODO Auto-generated catch block e.printStackTrace(); }
	 * 
	 * }
	 * 
	 * //changing the status of The User Account to 1 (opening) public static
	 * void changeTheStatusToOpen(String name) {
	 * 
	 * try {
	 * 
	 * SqliteConnectionThread thread=new SqliteConnectionThread();
	 * thread.start(); thread.join(); Connection localConn=thread.getConn();
	 * 
	 * 
	 * 
	 * JOptionPane.showMessageDialog(null,name+" changing status close to Open"
	 * ); String sql="UPDATE USERS set isOpen=1 where username=\""+name+"\"";
	 * Statement st=localConn.createStatement();
	 * 
	 * st.execute(sql);
	 * 
	 * 
	 * localConn.close(); } catch (SQLException | InterruptedException e) { //
	 * TODO Auto-generated catch block e.printStackTrace(); } }
	 * 
	 */

	///
	
}