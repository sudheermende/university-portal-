package Classes;
import java.awt.JobAttributes;
import java.sql.*;
import java.util.concurrent.ExecutionException;

import javax.swing.*;
public class SqliteConnectionThread extends Thread{

	private  Connection conn=null;
	
	public void run()
	{
		conn=dbConnecter();
	}
	
	
	public  Connection getConn() {
		
		return conn;
	}

	public  Connection dbConnecter()
	{
		
		try{
		Class.forName("org.sqlite.JDBC");
	
		 conn=DriverManager.getConnection("jdbc:sqlite:G:\\Masters\\Java\\Java Mars\\com.firstWebApp\\DB\\S_University.sqlite");	
			
	
	
		//Connection conn=DriverManager.getConnection("jdbc:sqlite:G:\\Masters\\Java\\Java Mars\\JavaDataBase\\S-mail.sqlite ");	
		
		
			Statement sta=conn.createStatement();
		
		String sqluser="CREATE TABLE IF NOT EXISTS users(username VARCHAR PRIMARY KEY  NOT NULL ,password VARCHAR NOT NULL ,inbox VARCHAR NOT NULL ,sent VARCHAR NOT NULL ,"
				+ "draft VARCHAR NOT NULL ,question VARCHAR NOT NULL ,answer VARCHAR NOT NULL , date VARCHAR)";
		
		sta.execute(sqluser);
		sta.close();
		
	//	JOptionPane.showMessageDialog(null,conn);
		return conn;
		
	}
	catch(Exception e)
	{
		JOptionPane.showMessageDialog(null, e);
		System.out.println("from sqlite class");
		return null;	

	}
	
	

	}
}