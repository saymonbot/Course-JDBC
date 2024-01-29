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
import db.DbIntegrityException;

public class Program {

	public static void main(String[] args) {

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		
		try {
			conn = DB.getConnection();
			
			pst = conn.prepareStatement(
					"INSERT INTO Department "
					+ "(Name)"
					+ "VALUES "
					+ "(?)",
					Statement.RETURN_GENERATED_KEYS);
			
			pst.setString(1, "D1");
			
			int rowsAffected = pst.executeUpdate();
			
			if(rowsAffected > 0) {
				rs = pst.getGeneratedKeys();
				while (rs.next()) {
					int id = rs.getInt(1);
					System.out.println("Done! Id = " + id);
				}
			}
		}catch (SQLException e) {
				
		}
					
			try {
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
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
			
		try {
			pst = conn.prepareStatement(
				"UPDATE seller "
				+ "SET BaseSalary = BaseSalary + ? "
				+ "WHERE "
				+ "(DepartmentId = ?)");
			
			pst.setDouble(1, 200.0);
			pst.setInt(2, 2);
			
			int rowsAffected = pst.executeUpdate();
		
			System.out.println("Done! Rows Affected: " + rowsAffected);
			
			
			pst = conn.prepareStatement(
				"DELETE from department "
				+ "WHERE "
				+ "Name = 'D1' ");

			rowsAffected = pst.executeUpdate();
		
			System.out.println("Done! Rows Affected: " + rowsAffected);
			

		} catch(SQLException e) {
			throw new DbIntegrityException(e.getMessage());
		}
		finally {
			DB.closeStatement(pst);
			DB.closeResultSet(rs);
			DB.closeConnection();
		}
	}
}
