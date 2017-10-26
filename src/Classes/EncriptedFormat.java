package Classes;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class EncriptedFormat {

	
	
public static String getCard(String card)
{
	String subS=card.substring(12);
	String newNumber="************"+subS;
return newNumber;
}


  public static byte[] encrypt(String str) {
  
	  return str.getBytes();

	}

  public static String decrypt(String str)  {
   return str.toString();
	}
  
  public static double getGpa(int total,int got)
  {
	  double pern=((double)got/(double)total)*100;
	 double gpa;
	  
if(pern>=90)
	gpa=4.0;
else if(pern>=80)
	gpa= 3.7;
else if(pern>=70)
	gpa= 3.5;
else if(pern>=60)
	gpa= 3.3;
else if(pern>=50)
	gpa= 3.0;
else if(pern>=35)
	gpa= 2.7;
else if(pern==0)
	gpa=0;
else 
	gpa= 2.0;

	return gpa;
	  
  }
  
  public static String getGrade(double gpa)
  {
	  String grade;
	  if(gpa==4)
		  grade="A+";
		  
	  else if(gpa>=3.7)
		  grade="A";
	  else if(gpa>=3.5)
		  grade="B+";
	  else if(gpa>=3.3)
		  grade="B";
	  else if(gpa>=3.0)
		  grade="C+";
	  else if(gpa>=2.7)
		  grade="C+";
	  else 
		  grade="FAIL";
	  
	  
	  return grade;
  }

public static double getCgpa(Connection connection,String student)
{
	
	try {
		Statement st=connection.createStatement();
	
	ResultSet rs=st.executeQuery("Select gpa from "+student+"Academics where status='finished'");
	
	double cgpa=0;
	
	int count=0;
	while(rs.next())
	{
		double localGpa=0;
		if(rs.getString(1).equalsIgnoreCase("pending"))
			localGpa=0;
		else 
			localGpa=Double.parseDouble(rs.getString(1));
		
		
		
		cgpa=cgpa+localGpa;
		
	count++;
	}
	
	
	cgpa=cgpa/count;
	
	return cgpa;
	} catch (SQLException e) {
		
		e.printStackTrace();
	}
	
	
	
	return 0;
}
  
  


}
// pradeep qmeTNFDnq+c=
//N2WnsVUS8bE=