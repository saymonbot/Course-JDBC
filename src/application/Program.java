package application;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
//import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import db.DB;

public class Program {

	public static void main(String[] args) {

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		
		Connection conn = null;
		PreparedStatement pst = null;
	//	Statement st = null;
		ResultSet rs = null;
		
		try {
			conn = DB.getConnection();
					
			pst = conn.prepareStatement(
					"INSERT INTO seller "
					+ "(Name, Email, BirthDate, BaseSalary, DepartmentId)"
					+ "VALUES "
					+ "(?, ?, ?, ?, ?)",
					Statement.RETURN_GENERATED_KEYS);
			
			pst.setString(1, "Carl Purple");
			pst.setString(2, "carl@gmail.com");
			pst.setDate(3, new java.sql.Date(sdf.parse("22/04/1985").getTime()));
			pst.setDouble(4, 3000.0);
			pst.setInt(5, 4);
			
			int rowsAffected = pst.executeUpdate();
			
			if(rowsAffected > 0) {
				rs = pst.getGeneratedKeys();
				while (rs.next()) {
					int id = rs.getInt(1);
					System.out.println("Done! Id = " + id);
				}
			}else {
				System.out.println("No rows affected!");
			}
			
			pst = conn.prepareStatement(
				"UPDATE seller "
				+ "SET BaseSalary = BaseSalary + ? "
				+ "WHERE "
				+ "(DepartmentId = ?)");
			
			pst.setDouble(1, 200.0);
			pst.setInt(2, 2);
			
			rowsAffected = pst.executeUpdate();
		
			System.out.println("Done! Rows Affected: " + rowsAffected);
			
		//	rs = st.executeQuery("select * from department");
			

		} catch(SQLException e) {
			e.printStackTrace();
		} catch(ParseException e) {
			e.printStackTrace();
		}
		finally {
			DB.closeStatement(pst);
			DB.closeResultSet(rs);
		//	DB.closeStatement(st);
			DB.closeConnection();
		}
	}
}
