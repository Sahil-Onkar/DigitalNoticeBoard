package dao;

import util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class NoticeViewDAO {

    public boolean recordView(int noticeId, int userId) {

        String sql =
                "INSERT INTO notice_views (notice_id, user_id, viewed_at) " +
                        "VALUES (?, ?, NOW())";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, noticeId);
            statement.setInt(2, userId);
            return statement.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public int getViewCount(int noticeId) {

        String sql = "SELECT COUNT(*) FROM notice_views WHERE notice_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, noticeId);

            try (ResultSet rs = statement.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
}