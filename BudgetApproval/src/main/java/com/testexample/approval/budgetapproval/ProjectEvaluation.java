package com.testexample.approval.budgetapproval;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
 import java.util.Date;
 import java.util.*;
 import java.text.DateFormat;
import java.text.SimpleDateFormat;
/**
 * This class was automatically generated by the data modeler tool.
 */

@javax.persistence.Entity
public class ProjectEvaluation implements java.io.Serializable
{

   static final long serialVersionUID = 1L;
  
   @javax.persistence.GeneratedValue(strategy = javax.persistence.GenerationType.AUTO, generator = "PROJECTEVALUATION_ID_GENERATOR")
   @javax.persistence.Id
   @javax.persistence.SequenceGenerator(sequenceName = "PROJECTEVALUATION_ID_SEQ", name = "PROJECTEVALUATION_ID_GENERATOR")
   private java.lang.Long id;
   private java.lang.String project;

   private java.lang.String description;

   private int fundValueRequisted;

   public ProjectEvaluation()
   {
   }

   public java.lang.Long getId()
   {
      return this.id;
   }

   public void setId(java.lang.Long id)
   {
      this.id = id;
   }

   public java.lang.String getProject()
   {

      return this.project;
   }

   public void setProject(java.lang.String project)
   {

      this.project = project;
   }

   public java.lang.String getDescription()
   {

      return this.description;
   }

   public void setDescription(java.lang.String description)
   {

      this.description = description;
   }

   public int getFundValueRequisted()
   {

      return this.fundValueRequisted;
   }

   public void setFundValueRequisted(int fundValueRequisted)
   {
      this.fundValueRequisted = fundValueRequisted;
   }
   
   public String getEmailID(String userName) throws SQLException
   {  
       DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	Date date = new Date();
	System.out.println(dateFormat.format(date)); 
      // System.out.println("userName"+userName);
       String userEmail=null;
       Connection c=null;
       Statement stmt=null;
      ResultSet rs = null;
     // System.out.println("getFundValueRequisted() : "+getFundValueRequisted()+ "getProject() : "+getProject()+"getDescription() : "+ getDescription() );
    //  System.out.println("org driver class");
      try
      {
         Class.forName("org.postgresql.Driver");
    
         c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/mytest", "postgres", "espl@2017");
  
         stmt = c.createStatement();
       String sql="INSERT INTO PROJECT_EVALUATION VALUES('"+getProject()+"','"+getDescription()+"',"+getFundValueRequisted()+",'"+userName+"','"+dateFormat.format(date)+"')";
        stmt.executeUpdate(sql);
        
        //Budget Request is less approved data store in db
        if(getFundValueRequisted() < 500000)
        {
             int project_Id=0;
              rs= stmt.executeQuery("(SELECT project_Id from PROJECT_EVALUATION ORDER BY project_Id DESC LIMIT 1 )");
			  while ( rs.next() ) 
			         {
				            project_Id = rs.getInt("project_Id");
				     }
				     System.out.println("project_Id in ProjectEvaluation"+project_Id);
            String sql1="INSERT INTO PROJECT_APPROVER_STATUS VALUES('SelfApproved',"+true+",'Project requested fund is less than 500000 so self approved',"+project_Id+",'"+dateFormat.format(date)+"')";
        stmt.executeUpdate(sql1);
        }
        
        //Get Email Id
         rs = stmt.executeQuery("SELECT email from USERJBPM where name='"+userName+"'");
         while (rs.next())
         {
          userEmail=(rs.getString("email"));
       //  System.out.println(rs.getString("email"));
         }
      }
      catch (ClassNotFoundException e)
      {
         e.printStackTrace();
      }
      finally
      {
         rs.close();
         stmt.close();
         c.close();

      }
     userEmail +=";"; 
System.out.println(userEmail);
      return userEmail;
   }

   public ProjectEvaluation(java.lang.Long id, java.lang.String project,
         java.lang.String description, int fundValueRequisted)
   {
      this.id = id;
      this.project = project;
      this.description = description;
      this.fundValueRequisted = fundValueRequisted;
   }

}