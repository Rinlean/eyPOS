package DAOstuff.user;

import componentStuff.DatabaseUtil;
import javax.swing.*;
import java.sql.*;
import java.util.*;

public class UserDAO {

    public static List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users";
        try (Connection conn = DatabaseUtil.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                users.add(new User(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("type")
                ));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to load users: " + ex.getMessage());
        }
        return users;
    }

    public static boolean updateUser(User user) {
        String sql = "UPDATE users SET username=?, password=?, type=? WHERE UserId=?";
        try (Connection conn = DatabaseUtil.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getType());
            stmt.setInt(4, user.getUserId());
            stmt.executeUpdate();
            return true;
        } catch (SQLException ex) {
            if (ex.getSQLState() != null && ex.getSQLState().startsWith("23")) {
                JOptionPane.showMessageDialog(null, "Username must be unique!");
            } else {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Failed to update user: " + ex.getMessage());
            }
            return false;
        }
    }
}
