package clipSample;

import java.io.FileReader;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;
import java.util.UUID;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class TransDetails
{
  static Double amnt;
  static String des;
  static String date_val;
  static String userId;
  static String transId;
  
  public static String getTransId()
  {
    return transId;
  }
  
  public static void setTransId(String transId)
  {
    transId = transId;
  }
  
  public String getUserId()
  {
    return userId;
  }
  
  public void setUserId(String userId)
  {
    userId = userId;
  }
  
  public double getAmnt()
  {
    return amnt.doubleValue();
  }
  
  public void setAmnt(Double amnt)
  {
    amnt = amnt;
  }
  
  public String getDes()
  {
    return des;
  }
  
  public void setDes(String des)
  {
    des = des;
  }
  
  public String getDate_val()
  {
    return date_val;
  }
  
  public void setDate_val(String date_val)
  {
    date_val = date_val;
  }
  
  public static void main(String[] args)
  {
    System.out.println("Select the following options\n");
    System.out.println("1) Add Transaction\n2) Show Transaction\n3) List Transaction\n4) Sum Transaction \n");
    Scanner ch = new Scanner(System.in);
    int a = ch.nextInt();
    switch (a)
    {
    case 1: 
      AddTrans();
      break;
    case 2: 
      ShowTrans();
      break;
    case 3: 
      ListTrans();
      break;
    case 4: 
      SumTranscation();
      break;
    }
  }
  
  private static void SumTranscation()
  {
    TransDetails td3 = new TransDetails();
    System.out.println("Enter UserId\n");
    Scanner ch3 = new Scanner(System.in);
    td3.setUserId(ch3.nextLine());
    try
    {
      Connection con3 = dbConnect();
      if (con3 != null) {
        System.out.println("DB connection successfull");
      } else {
        System.out.println("DB connection Failed");
      }
      Statement stmt = con3.createStatement();
      String sql3 = "SELECT userId, sum(amnt) as 'Sum' from details where userId = '" + td3.getUserId() + "' group by userId";
      ResultSet rs3 = stmt.executeQuery(sql3);
      int resultCounter1 = 0;
      while (rs3.next())
      {
        String id = rs3.getString(1);
        Double sum = Double.valueOf(rs3.getDouble(2));
        System.out.format("%s, %.2f", new Object[] { id, sum });
        resultCounter1++;
      }
      rs3.close();
      if (resultCounter1 == 0) {
        System.out.println("No Transaction Found");
      }
    }
    catch (Exception e)
    {
      System.err.println(e.getMessage());
      System.exit(0);
    }
  }
  
  private static void ListTrans()
  {
    TransDetails td2 = new TransDetails();
    System.out.println("Enter UserId\n");
    Scanner ch3 = new Scanner(System.in);
    td2.setUserId(ch3.nextLine());
    try
    {
      Connection con2 = dbConnect();
      if (con2 != null) {
        System.out.println("DB connection successfull");
      } else {
        System.out.println("DB connection Failed");
      }
      Statement stmt = con2.createStatement();
      String sql2 = "select * from details where userId = '" + td2.getUserId() + "'";
      ResultSet rs1 = stmt.executeQuery(sql2);
      int resultCounter = 0;
      while (rs1.next())
      {
        String id = rs1.getString(1);
        Double amt = Double.valueOf(rs1.getDouble(2));
        String des = rs1.getString(3);
        String date_val = rs1.getString(4);
        String iD = rs1.getString(5);
        System.out.format("%s, %.2f, %s, %s, %s\n", new Object[] { id, amt, des, date_val, iD });
        resultCounter++;
      }
      if (resultCounter == 0) {
        System.out.println("No Transaction Found");
      }
      rs1.close();
    }
    catch (Exception e)
    {
      System.err.println(e.getMessage());
      System.exit(0);
    }
  }
  
  private static void ShowTrans()
  {
    TransDetails td = new TransDetails();
    System.out.println("Enter UserId\n");
    Scanner ch1 = new Scanner(System.in);
    td.setUserId(ch1.nextLine());
    
    System.out.println("Enter TransactionID\n");
    Scanner ch2 = new Scanner(System.in);
    String tId = ch2.nextLine();
    setTransId(tId);
    try
    {
      Connection con1 = dbConnect();
      if (con1 != null) {
        System.out.println("DB connection successfull");
      } else {
        System.out.println("DB connection Failed");
      }
      Statement stmt = con1.createStatement();
      String sql1 = "select * from details where userId = '" + td.getUserId() + "' and transId = '" + getTransId() + "'";
      ResultSet rs = stmt.executeQuery(sql1);
      int resultCounter2 = 0;
      while (rs.next())
      {
        String id = rs.getString(1);
        Double amt = Double.valueOf(rs.getDouble(2));
        String des = rs.getString(3);
        String date_val = rs.getString(4);
        String iD = rs.getString(5);
        System.out.format("%s, %.2f, %s, %s, %s\n", new Object[] { id, amt, des, date_val, iD });
        resultCounter2++;
      }
      rs.close();
      if (resultCounter2 == 0) {
        System.out.println("No Transaction Found");
      }
    }
    catch (Exception e)
    {
      System.err.println("Error" + e.getMessage());
      System.exit(0);
    }
  }
  
  private static void AddTrans()
  {
    TransDetails td1 = new TransDetails();
    System.out.println("Enter UserId to add\n");
    Scanner ch1 = new Scanner(System.in);
    td1.setUserId(ch1.nextLine());
    
    System.out.println("Enter Transaction_jason file name\n");
    Scanner ch2 = new Scanner(System.in);
    String fName = ch2.nextLine();
    setTransId(UUID.randomUUID().toString());
    try
    {
      FileReader fReader = new FileReader(fName);
      JSONParser parser = new JSONParser();
      Object obj = parser.parse(fReader);
      JSONObject jobj = (JSONObject)obj;
      td1.setAmnt((Double)jobj.get("amount"));
      td1.setDes((String)jobj.get("description"));
      td1.setDate_val((String)jobj.get("date"));
      
      Connection con = dbConnect();
      if (con != null) {
        System.out.println("DB connection successfull");
      } else {
        System.out.println("DB connection Failed");
      }
      Statement stmt = con.createStatement();
      String sql = "insert into details values('" + getTransId() + "', '" + td1.getAmnt() + "', '" + td1.getDes() + "', '" + td1.getDate_val() + "', '" + td1.getUserId() + "')";
      int count = stmt.executeUpdate(sql);
      if (count > 0) {
        System.out.println("Insertion successfull");
      } else {
        System.out.println("Insertion Failed");
      }
    }
    catch (Exception e)
    {
      System.err.println(e.getMessage());
      System.exit(0);
    }
  }
  
  private static Connection dbConnect()
    throws ClassNotFoundException
  {
    Connection con1 = null;
    try
    {
      Class.forName("org.sqlite.JDBC");
      con1 = DriverManager.getConnection("jdbc:sqlite:C:/Users/RAJATH/Pictures/test.db;");
    }
    catch (SQLException e)
    {
      e.printStackTrace();
    }
    return con1;
  }
}
