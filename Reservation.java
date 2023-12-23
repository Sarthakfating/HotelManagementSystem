package com.practise;


import java.sql.Connection;
import java.util.Scanner; 
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Reservation {

	public static void main(String[] args) {
	 Connection con = null;
	 Statement stmt=null;
	 
	
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
	    final String url="jdbc:mysql://localhost:3307/hotelmanagement";
		final String username="root";
		final String password="root";
		
		
		try {
			con = DriverManager.getConnection(url, username, password);
			System.out.println("Connection established");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	
		try {
			stmt = con.createStatement();
		
		String sql="create table reservations"+"(reservation_id INTEGER  not NULL AUTO_INCREMENT PRIMARY KEY,"+
					"gname VARCHAR(255),"+"room_number INTEGER not NULL,"+"contact_no VARCHAR(10) not NULL,"+
					"reservation_date TIMESTAMP default CURRENT_TIMESTAMP)";
		
	//	stmt.executeUpdate(sql);
	//	System.out.println("Table created reservatons");
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		try {
		while(true) {
			System.out.println();
			System.out.println("Hotel Management System");
			Scanner sc = new Scanner(System.in);
			System.out.println("1.Reserve a room");
			System.out.println("2.View Reservations");
			System.out.println("3.Get Room number");
			System.out.println("4.Update Reservation");
			System.out.println("5.Delete Reservation ");
			System.out.println("6. Exit");
			System.out.println("Choose an option: ");
			int choice=sc.nextInt(); 
			switch(choice) {
			case 1: reserveRoom(con,sc,stmt);
			break;
			case 2: viewReservations(con,stmt);
			break;
			case 3: getRoomNumber(con,sc,stmt);
			break;
			case 4: updateReservation(con,sc,stmt);
			break;
			case 5: deleteReservation(con,sc,stmt);
			break;
			case 6: exit();
			sc.close();
			return;
			default: System.out.println("Invalid choice,Try again");
			}
		}
		}catch (Exception e) {
			e.printStackTrace();
		}
		
			
			}


	private static void deleteReservation(Connection con, Scanner sc, Statement stmt) {
	  try {
		  System.out.println("Enter Reservation Id to delete: ");
		  int reservationId=sc.nextInt();
		  
		  if(!reservationExists(con,reservationId, stmt)) {
				System.out.println("Reservation not found for the Given Id: ");
				return;
			}
		  
		  String sql5="DELETE FROM reservations WHERE reservation_id="+reservationId;
		  
		  stmt=con.createStatement();
		  int affectedrows=stmt.executeUpdate(sql5);
		  
		  if(affectedrows>0) {
				System.out.println("Reservation Deleted Successfully");
			}else {
				System.out.println("Reservation Deletion Failed");
			}
	  }catch (Exception e) {
		e.printStackTrace();
	}
		
	}


	private static boolean reservationExists(Connection con, int reservationId,Statement stmt) {
		try {
			String sql6="SELECT reservation_id FROM resrvations WHERE reservation_id="+reservationId;
			stmt=con.createStatement();
			ResultSet rs=stmt.executeQuery(sql6);
			return rs.next();
		}catch (Exception e) {
			e.printStackTrace();
		return false;
		}
	}


	private static void exit() throws InterruptedException{
		System.out.println("Existing the System");
		int i=5;
		while(i!=0) {
			System.out.print(".");
			Thread.sleep(450);
			i--;
		}
		System.out.println();
		System.out.println("Thankyou for using Hotel Reservation System");
		System.out.println(":-)");
	}


	private static void updateReservation(Connection con, Scanner sc, Statement stmt) {
		try {
			System.out.println("Enter Reservation ID to update: ");
			int reservationId=sc.nextInt();
			sc.nextLine();
			
			if(!reservationExists(con,reservationId, stmt)) {
				System.out.println("Reservation not found for the Given Id: ");
				return;
			}
			
			System.out.println("Enter Guest Name: ");
			String newgname=sc.next();
			System.out.println("Enter new Room Number");
			int newRoomno=sc.nextInt();
			System.out.println("Enter new Contact Number");
			String newcontact_no=sc.next();
			
			String sql4="UPDATE reservations SET gname='"+newgname+","+
					"room_number="+newRoomno+","+"contact_no="+newcontact_no+","+
					"WHERE reservation_id="+reservationId;
			
			stmt=con.createStatement();
			int affectedrows=stmt.executeUpdate(sql4);
			
			if(affectedrows>0) {
				System.out.println("Reservation Updated Successfully");
			}else {
				System.out.println("Reservation Update Failed");
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		
	}


	private static void getRoomNumber(Connection con, Scanner sc, Statement stmt) {
		try {
			System.out.println("Enter Reservation ID: ");
			int reservationId=sc.nextInt();
			System.out.println("Enter Guest name: ");
			String gname=sc.next();
			
			String sql3="SELECT room_number FROM reservations"+" WHERE reservation_id="+reservationId +
					"and gname="+gname+"'";
			
			stmt=con.createStatement();
			ResultSet rs=stmt.executeQuery(sql3) ;
			if(rs.next()) {
				int roomno=rs.getInt("room_number");
				System.out.println("Room number for Reservation ID"+reservationId+"and Guest"+
				gname+" is: "+roomno);
			}else {
				System.out.println("Reservation not found for given ID and Guest name");
			}
		}catch (Exception e) {
		 e.printStackTrace();
		}
		
	}


	private static void viewReservations(Connection con, Statement stmt) {
		 String sql2="SELECT reservation_id,gname,room_number,contact_no,reservation_date FROM reservations";
		 try {
			stmt=con.createStatement(); 
		    ResultSet rs=stmt.executeQuery(sql2);
			
			System.out.println("Current Reservations:");
			System.out.println("+----------------+-----------+-------------+-------------+------------------+");
			System.out.println("|Registration ID | Guest     | Room Number | Contact No. | Reservation Date |");
			System.out.println("+----------------+-----------+-------------+-------------+------------------+");
			while(rs.next()) {
				int reservid=rs.getInt("reservation_id");
				String gname=rs.getString("gname");
				int roomno=rs.getInt("room_number");
				String contact=rs.getString("contact_no");
				String reservdate=rs.getTimestamp("reservation_date").toString();
				
				System.out.printf("*| %-14d | %-15s | %-13d | %-20s | %-19s |\n",
						reservid,gname,roomno,contact,reservdate);
			}
			System.out.println("+----------------+-----------+-------------+-------------+------------------+");

		} catch (SQLException e) {
			e.printStackTrace();
		}
		 
	}


	private static void reserveRoom(Connection con, Scanner sc, Statement stmt) {
		try {
			System.out.println("Enter Guest name: ");
			String name=sc.next();
			sc.nextLine();
			System.out.println("Enter Room number: ");
			int roomno=sc.nextInt();
			System.out.println("Enter Contact number: ");
			String contactno=sc.next();
			
			String sql1="INSERT INTO reservations(gname,room_number,contact_no)"+
			                "VALUES("+ name +","+ roomno +","+ contactno +")";
			
			stmt=con.createStatement();
			int affectedrows=stmt.executeUpdate(sql1);
			if(affectedrows>0) {
				System.out.println("Reservation done successfully");
			}else {
				System.out.println("Reservation failed");
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		
	
		
	
	}
}

