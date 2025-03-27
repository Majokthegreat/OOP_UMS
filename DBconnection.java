package UMS;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class DBconnection implements Person {

    private static final String URL = "jdbc:postgresql://localhost:5432/university_db"; // Ensure this is correct
    private static final String USER = "postgres"; // Ensure this username is correct
    private static final String PASSWORD = "D@Godisgreat10"; // Ensure this password is correct

    // Constructor with no parameters (Fixing the issue)
    public DBconnection() {}

    // Database connection method
    public static Connection connect() {
        try {
            Class.forName("org.postgresql.Driver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            System.out.println("PostgreSQL JDBC Driver not found.");
            return null;
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
            return null;
        }
    }

    // Method to insert data into any table dynamically
    public void insertion(String tableName, String[] columnNames, String[] values) {
        if (columnNames.length != values.length) {
            JOptionPane.showMessageDialog(null, "The number of columns and values must match.");
            return;
        }

        // Create the SQL query dynamically
        StringBuilder sql = new StringBuilder("INSERT INTO " + tableName + " (");
        for (int i = 0; i < columnNames.length; i++) {
            sql.append(columnNames[i]);
            if (i < columnNames.length - 1) {
                sql.append(", ");
            }
        }
        sql.append(") VALUES (");
        for (int i = 0; i < values.length; i++) {
            sql.append("?");
            if (i < values.length - 1) {
                sql.append(", ");
            }
        }
        sql.append(")");

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < values.length; i++) {
                pstmt.setString(i + 1, values[i]);
            }

            int rowsInserted = pstmt.executeUpdate();
            if (rowsInserted > 0) {
                JOptionPane.showMessageDialog(null, "Data inserted successfully!");
            } else {
                JOptionPane.showMessageDialog(null, "Insertion failed.");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }

    // Method to load data into JTable
    public void loadData(DefaultTableModel model, String tableName, String[] columnNames) {
        StringBuilder sql = new StringBuilder("SELECT ");
        for (int i = 0; i < columnNames.length; i++) {
            sql.append(columnNames[i]);
            if (i < columnNames.length - 1) {
                sql.append(", ");
            }
        }
        sql.append(" FROM ").append(tableName);

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql.toString());
             ResultSet rs = pstmt.executeQuery()) {

            model.setRowCount(0); // Clear existing data
            while (rs.next()) {
                Object[] row = new Object[columnNames.length];
                for (int i = 0; i < columnNames.length; i++) {
                    row[i] = rs.getString(columnNames[i]);
                }
                model.addRow(row);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error Loading Data: " + e.getMessage());
        }
    }

    @Override
    public void loadData() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void insertion() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
