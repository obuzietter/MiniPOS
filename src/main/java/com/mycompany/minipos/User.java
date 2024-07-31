/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.minipos;

/**
 *
 * @author OBUZIETTER3000
 */
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class User {

    private String userId;
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private boolean active;
    private boolean tillOpen;

    public User(String userId, String firstName, String lastName, String username, String password, boolean active, boolean tillOpen) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.active = active;
        this.tillOpen = tillOpen;
    }

    public User() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isTillOpen() {
        return tillOpen;
    }

    public void setTillOpen(boolean tillOpen) {
        this.tillOpen = tillOpen;
    }

    // Database connection
    private Connection connect() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/minipos";
        String user = "root";
        String pwd = "";
        return DriverManager.getConnection(url, user, pwd);
    }

    // Create
   public void createUser() {
    String checkUserSql = "SELECT COUNT(*) FROM users WHERE username = ?";
    String insertUserSql = "INSERT INTO users (first_name, last_name, username, password, active, till_open) VALUES (?, ?, ?, ?, ?, ?)";
    
    try (Connection conn = connect()) {
        // Check if username already exists
        try (PreparedStatement checkUserStmt = conn.prepareStatement(checkUserSql)) {
            checkUserStmt.setString(1, username);
            try (ResultSet rs = checkUserStmt.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    JOptionPane.showMessageDialog(null, "Username already exists. Please choose a different username.");
                    return;
                }
            }
        }

        // Insert the new user if username does not exist
        try (PreparedStatement pstmt = conn.prepareStatement(insertUserSql)) {
            pstmt.setString(1, firstName);
            pstmt.setString(2, lastName);
            pstmt.setString(3, username);
            pstmt.setString(4, password);
            pstmt.setBoolean(5, active);
            pstmt.setBoolean(6, tillOpen);
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "User Created Successfully In Database!");
        }
    } catch (SQLException e) {
        System.out.println(e.getMessage());
    }
}

    // Read
    public User readUser(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";
        User user = null;
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                user = new User(
                        rs.getString("id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getBoolean("active"),
                        rs.getBoolean("till_open")
                );
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return user;
    }

    // Update
    public void updateUser() {
        String sql = "UPDATE users SET first_name = ?, last_name = ?, username = ?, password = ?, active = ?, till_open = ? WHERE id = ?";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, firstName);
            pstmt.setString(2, lastName);
            pstmt.setString(3, username);
            pstmt.setString(4, password);
            pstmt.setBoolean(5, active);
            pstmt.setBoolean(6, tillOpen);
            pstmt.setString(7, userId);
            
            pstmt.executeUpdate();
            if (pstmt.executeUpdate()>0){
               JOptionPane.showMessageDialog(null, "User Updated Successfully!");
            }
            
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // Delete
    public void deleteUser(String username) {
        String sql = "DELETE FROM users WHERE username = ?";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


     // Get all users
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users";
        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                User user = new User(
                        rs.getString("id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getBoolean("active"),
                        rs.getBoolean("till_open")
                );
                users.add(user);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return users;
    }

}
