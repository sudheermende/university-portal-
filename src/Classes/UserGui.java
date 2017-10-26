package Classes;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.text.NumberFormat;
import java.util.LinkedList;
import java.util.Scanner;

import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;

public class UserGui{

	private static String to=null;
	private static String sub=null;
	private static String compose=null;;
	private static String newpswrd=null;
	private String[] args=new String[] {""}; 
	static String notification="";
	JTextField tof=new JTextField(20);
	JTextField subf=new JTextField(20);
	JTextField comf=new JTextField(20);
	int composenumber;
	
static Scanner input= new Scanner(System.in);

	final int LIMIT=15;
	private String userid=null;
	private String password=null;
	private String securityAnswer;  // question :  What is your High school name?
	private String[] inboxmsg= new String[LIMIT];
	private String[] sentmsg=new String[LIMIT];
	private String[] draftmsg=new String[LIMIT];
	private String[] inboxfrom=new String[LIMIT];
	private String[] inboxsub=new String[LIMIT];
	private String[] sentto=new String[LIMIT];
	private String[] sentsub=new String[LIMIT];
	private String[] draftto=new String[LIMIT];
	private String[] draftsub=new String[LIMIT];
// class implementations ///////////
	private int inboxcount=0;
	private int sentcount=0;
	private int draftcount=0;
	private int inboxfromcount=0;
	private int inboxsubcount=0;
	private int senttocount=0;
	private int sentsubcount=0;
	private int drafttocount=0;
	private int draftsubcount=0;

	
	
	public void setInboxmsg(String msg)
	{
		inboxmsg[inboxcount]=msg;
		inboxcount++;
	}
	public void setSentmsg(String msg)
	{
		sentmsg[sentcount]=msg;
		sentcount++;
	}
	public void setDraftmsg(String msg)
	{
		draftmsg[draftcount]=msg;
		draftcount++;
	}

	public String getInboxmsg(int i)
	{
	 return inboxmsg[i];
	}
	public String getSentmsg(int i)
	{
	 return sentmsg[i];
	} 
	public String getDraftmsg(int i)
	{
	 return draftmsg[i];
	}
	
	/// INBOX TOTAL///////
	public String getInbox()
	{
		String s="  INBOX :"
				+ "\n-----------------------------------------------------------------------------------------------------------------------------------------";
		for (int i = 0; i <inboxcount; i++)
	 {
		//s=s+String.format("%8s",(i+1))+String.format("%15s",inboxfrom[i])+String.format("%28s",inboxsub[i])+String.format("%28s",inboxmsg[i]);
			s=s+"\n";
			s=s+("   "+String.format("%2d", i+1)+". ")
		    +"  from : "+modify(inboxfrom[i])
		    +"\n         sub  : "+modify(inboxsub[i])
		    +"\n         Msg  : "+modify(inboxmsg[i])+
		        "\n-----------------------------------------------------------------------------------------------------------------------------------------";
	 }
	//System.out.println(s);
return s;
  }
	
	/// SENT TOTAL ///
	public String getSent()
	{
		String s="  SENT : "
				+ "\n-----------------------------------------------------------------------------------------------------------------------------------------";
	for (int i = 0; i <sentcount; i++)
	 {
		s=s+"\n";
		s=s+("   "+String.format("%2d", i+1)+". ")
	    +"   to  : "+modify(sentto[i])
	    +"\n         sub  : "+modify(sentsub[i])
	    +"\n         Msg  : "+modify(sentmsg[i])+
	               "\n-----------------------------------------------------------------------------------------------------------------------------------------";
 }
	//System.out.println(s);
	return s;
	}

	/// DRAFT TOTAL ////
	public String getDraft()
	{
  String s="  DRAFT :"
  		+ "\n-----------------------------------------------------------------------------------------------------------------------------------------";
	for (int i = 0; i <draftcount; i++)
	 {
		s=s+"\n";
		s=s+("   "+String.format("%2d", i+1)+". ")
	    +"   to  : "+modify(draftto[i])
	    +"\n         sub  : "+modify(draftsub[i])
	    +"\n         Msg  : "+modify(draftmsg[i])+
	        "\n-----------------------------------------------------------------------------------------------------------------------------------------";
 }
	//System.out.println(s); 
	return s; 
	}

	///// MODIFY ///
	public String modify(String s)
	{
	 int a = s.length();
	 String s1=s;
	 if (a <= 25)

	 {
	  //System.out.print(s);
	  for (int i = 0; i < 30- a; i++)
	  {
	   //System.out.print(" ");
		  s1=s1+" ";
	  }         
	 }

	 else
	 {
		 //System.out.print(s.substring(0,22)+"...   ");
	s1=s.substring(0,22)+"...   ";
	 }
return s1; 
	}

	public void setUserId(String id)
	{
		userid=id;
	}
	public String getUserId()
	{
		return userid;
	}

	public void setPassword(String pw)
	{
		password=pw;
	}
	public String getPassword()
	{
		return password;
	}

	public String getSecurityAnswer() {
		return securityAnswer;
	}
	public void setSecurityAnswer(String securityAnswer) {
		this.securityAnswer = securityAnswer;
	}

	
	public void manageSent(int a)
	{
	 for (int i = a; i < sentcount; i++)
	 {
	  sentmsg[i - 1] = sentmsg[i];
	  sentsub[i - 1] = sentsub[i];
	  sentto[i - 1] = sentto[i];
	 }
	 sentcount--;
	 sentsubcount--;
	 senttocount--;
	 System.out.println("Sent  altered ");
	}
	
	public void manageInbox(int a)
	{
	 for (int i = a; i < inboxcount; i++)
	 {
	  inboxmsg[i - 1] = inboxmsg[i];
	  inboxsub[i - 1] = inboxsub[i];
	  inboxfrom[i - 1] = inboxfrom[i];
	 }
	 inboxcount--;
	 inboxsubcount--;
	 inboxfromcount--;
	 System.out.println("Inbox altered ");
	}
	
	public void manageDraft(int a)
	{
	 for (int i = a; i < draftcount; i++)
	 {
	  draftmsg[i - 1] = draftmsg[i];
	  draftsub[i - 1] = draftsub[i];
	  draftto[i - 1] = draftto[i];
	 }
	 draftcount--;
	 draftsubcount--;
	 drafttocount--;
	 System.out.println("draft altered ");
	}

	public static void banner() 
	{
	 System.out.println(" S-MAIL ");
	 System.out.println(("        Sample implementation of basic e-mail features  "));
	}
	public static String Banner()
{
	String b="G_mail   The best mail from google";
	return b;
}
	public int getInboxcount()
	{
	 return inboxcount;
	}
	public int getSentcount()
	{
	 return sentcount;
	}
	public int getDraftcount()
	{
	 return draftcount;
	}

	///   INBOX  FROM & SUB 
	 public void setInboxfrom(String t)
	{
	 inboxfrom[inboxfromcount] = t;
	 inboxfromcount++;
	}
	public String getInboxfrom(int z)
	{
	  return inboxfrom[z];
	}
	public void setInboxsub(String subj)
	{
	 inboxsub[inboxsubcount] = subj;
	 inboxsubcount++;
	}
	public String getInboxsub(int z)
	{
	 return inboxsub[z];
	}

	/// SENT TO & SUB
	public void setSentto(String t)
	{
	 sentto[senttocount] = t;
	 senttocount++;
	}
	public String getSentto(int z) 
	{ 
	return sentto[z];
	}
	public void setSentsub(String subj)
	{
	 sentsub[sentsubcount] = subj;
	 sentsubcount++;
	}
	public String getSentsub(int z)
	{
	 return sentsub[z];
	}

	// DRAFT TO & SUB

	public void setDraftto(String t)
	{
	 draftto[drafttocount] = t;
	 drafttocount++;
	} 
	public String getDraftto(int z)
	{
	  return draftto[z];
	}
	public void setDraftsub(String subj)
	{
	 draftsub[draftsubcount] = subj;
	 draftsubcount++;
	}
	public String getDraftsub(int z)
	{
	  return draftsub[z];
	 
	}	
	
	/// it says true or false if required object is inside list or not
protected static boolean search(LinkedList<UserGui> list, UserGui newU)
{
	/*
	 for(int num=0; num<list.size(); num++)
     {
   	  System.out.println(" U"+(num+1)+"   Name: "+list.get(num).getUserId()+" pswrd:"+list.get(num).getPassword());
     }*/
	try{
			for(int i=0;i<list.size();i++) 
			{
			if((list.get(i).getUserId().compareToIgnoreCase(newU.getUserId()))==0)
				{ 
					//System.out.println(newU.getUserId()+" is found inside list");
					return true;
				}
			}
			}
	catch(NullPointerException e)
	{
		//System.out.println(newU.getUserId()+" is not found inside list");
		return false;
	}
return false;
	}

// it return the index number if required object is inside, else retuns '0',  userid and pswrd checks
protected static int searchLogin(LinkedList<UserGui> list, UserGui newU)
{
	/*
	System.out.println("\n\n  search login \n");
	 for(int num=0; num<list.size(); num++)
     {
   	  System.out.println(" U"+(num+1)+"   Name: "+list.get(num).getUserId()+" pswrd:"+list.get(num).getPassword());
     } */
	try{
	for(int i=0;i<list.size();i++)
	{
		if(list.get(i).getUserId().compareToIgnoreCase(newU.getUserId())==0)
		{ 
			if(list.get(i).getPassword().compareTo(newU.getPassword())==0)
			
				return i;
		}
	}	}
	catch(NullPointerException e)
	{
	return -1;	
	}
	return -1;
}

/////    checks only UserId, retuns index
protected static int searchUserId(LinkedList<UserGui> list, UserGui newU)
{
	
	 /*System.out.println("\n\n  search login \n");
	 for(int num=0; num<list.size(); num++)
     {
   	  System.out.println(" U"+(num+1)+"   Name: "+list.get(num).getUserId()+" pswrd:"+list.get(num).getPassword());
     }*/
	try{
	for(int i=0;i<list.size();i++)
	{
		if(list.get(i).getUserId().compareToIgnoreCase(newU.getUserId())==0)
		{ 
				return i;
		}
	}	}
	catch(NullPointerException e)
	{
	return -1;	
	}
	return -1;
}

	
/// User class CONSTRUCTOR ///
public UserGui(){};
public UserGui(String id,String pw){
	userid=id;
	password=pw;
	}
public UserGui(String id,String pw,String secAnswer)
{
 userid=id;
 password=pw;
 securityAnswer=secAnswer;
}

/////  deleting the account ///
public  void DeletingAccount(LinkedList<UserGui> list,UserGui us)
{
notification="";
for(;;)	
{
String[] op={" Delete"," Cancel"};
JPanel dlt=new JPanel();
dlt.setLayout(new GridLayout(6,1));
dlt.add(new JLabel(" Deleting the account"));
dlt.add(new JLabel(notification));
dlt.add(new JLabel(" User Id  :" +us.getUserId()));
JPasswordField p=new JPasswordField();
dlt.add(p);
dlt.add(new JLabel(" Are you sure, that you want to delete the account?? "));
int n=JOptionPane.showOptionDialog(null,dlt," S-Mail ",
	JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.PLAIN_MESSAGE,null,op,op[0]);
notification="";
if(n==0)
{
if(p.getText().equals(us.getPassword()))
{
	notification=us.getUserId()+"  account is deleted ";
list.remove(us);
	//UserTestGui.newMain(list);
break;
}
else
{
	notification= " Password is not correct";
continue;
}
}
else
 break;
}
}

	
///////////////	 CLASS IMPLEMENTATION END ///////////////////////
	
	
	
}
