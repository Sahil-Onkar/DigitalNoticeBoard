package dao;

import model.Notification;
import util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class NotificationDAO {

    // One row per active student; department == null means all departments
    public boolean notifyStudents(int noticeId, Integer departmentId, String message) {

        String sql =
                "INSERT INTO notifications " +
                        "(user_id, notice_id, message, is_read, created_at) " +
                        "SELECT user_id, ?, ?, 0, NOW() FROM users " +
                        "WHERE role_id = 3 AND LOWER(status) = 'active'" +
                        (departmentId != null ? " AND department_id = ?" : "");

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, noticeId);
            statement.setString(2, message);

            if (departmentId != null) {
                statement.setInt(3, departmentId);
            }

            statement.executeUpdate();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public int getUnreadCount(int userId) {

        String sql =
                "SELECT COUNT(*) FROM notifications " +
                        "WHERE user_id = ? AND is_read = 0";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, userId);

            try (ResultSet rs = statement.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public List<Notification> getNotificationsForUser(int userId) {

        List<Notification> list = new ArrayList<>();

        String sql =
                "SELECT * FROM notifications " +
                        "WHERE user_id = ? ORDER BY created_at DESC";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, userId);

            try (ResultSet rs = statement.executeQuery()) {

                while (rs.next()) {

                    Notification n = new Notification();

                    n.setNotificationId(rs.getInt("notification_id"));
                    n.setNoticeId(rs.getInt("notice_id"));
                    n.setMessage(rs.getString("message"));
                    n.setRead(rs.getInt("is_read") == 1);

                    Timestamp ts = rs.getTimestamp("created_at");
                    if (ts != null) {
                        n.setCreatedAt(ts.toLocalDateTime());
                    }

                    list.add(n);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public void markAllRead(int userId) {

        String sql =
                "UPDATE notifications SET is_read = 1 " +
                        "WHERE user_id = ? AND is_read = 0";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, userId);
            statement.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
