package dao;

import model.Notice;
import util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class NoticeDAO {

    public List<Notice> getAllNotices() {

        List<Notice> notices = new ArrayList<>();

        String sql = "SELECT * FROM notices";

        try (
                Connection connection = DBConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()
        ) {

            while (resultSet.next()) {

                Notice notice = new Notice();

                notice.setNoticeId(
                        resultSet.getInt("notice_id")
                );

                notice.setTitle(
                        resultSet.getString("title")
                );

                notice.setDescription(
                        resultSet.getString("description")
                );

                notice.setCategoryId(
                        resultSet.getInt("category_id")
                );

                notice.setCreatedBy(
                        resultSet.getInt("created_by")
                );

                // department_id can be NULL
                int departmentId =
                        resultSet.getInt("department_id");

                if (resultSet.wasNull()) {
                    notice.setDepartmentId(null);
                } else {
                    notice.setDepartmentId(departmentId);
                }

                notice.setCreatedAt(
                        resultSet.getTimestamp("created_at")
                                .toLocalDateTime()
                );

                // publish_date can be NULL
                if (resultSet.getTimestamp("publish_date") != null) {
                    notice.setPublishAt(
                            resultSet.getTimestamp("publish_date")
                                    .toLocalDateTime()
                    );
                }

                // expiry_date can be NULL
                if (resultSet.getTimestamp("expiry_date") != null) {
                    notice.setExpiresAt(
                            resultSet.getTimestamp("expiry_date")
                                    .toLocalDateTime()
                    );
                }

                notice.setPriority(
                        resultSet.getString("priority")
                );

                notice.setStatus(
                        resultSet.getString("status")
                );

                notices.add(notice);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return notices;
    }
    public List<Notice> getNoticesByCreator(int userId) {

        List<Notice> notices = new ArrayList<>();

        String sql =
                "SELECT * FROM notices " +
                        "WHERE created_by = ? " +
                        "ORDER BY created_at DESC";

        try (
                Connection connection =
                        DBConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setInt(1, userId);

            try (
                    ResultSet resultSet =
                            statement.executeQuery()
            ) {

                while (resultSet.next()) {

                    Notice notice = new Notice();

                    notice.setNoticeId(
                            resultSet.getInt("notice_id")
                    );

                    notice.setTitle(
                            resultSet.getString("title")
                    );

                    notice.setDescription(
                            resultSet.getString("description")
                    );

                    notice.setCreatedBy(
                            resultSet.getInt("created_by")
                    );

                    notice.setCategoryId(
                            resultSet.getInt("category_id")
                    );

                    int departmentId =
                            resultSet.getInt("department_id");

                    if (resultSet.wasNull()) {
                        notice.setDepartmentId(null);
                    } else {
                        notice.setDepartmentId(departmentId);
                    }

                    if (resultSet.getTimestamp("created_at") != null) {
                        notice.setCreatedAt(
                                resultSet.getTimestamp("created_at")
                                        .toLocalDateTime()
                        );
                    }

                    if (resultSet.getTimestamp("publish_date") != null) {
                        notice.setPublishAt(
                                resultSet.getTimestamp("publish_date")
                                        .toLocalDateTime()
                        );
                    }

                    if (resultSet.getTimestamp("expiry_date") != null) {
                        notice.setExpiresAt(
                                resultSet.getTimestamp("expiry_date")
                                        .toLocalDateTime()
                        );
                    }

                    notice.setPriority(
                            resultSet.getString("priority")
                    );

                    notice.setStatus(
                            resultSet.getString("status")
                    );

                    notices.add(notice);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return notices;
    }
    public boolean insertNotice(Notice notice) {

        String sql = """
            INSERT INTO notices
            (title, description, category_id, created_by,
             department_id, publish_date, expiry_date,
             priority, status)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;

        try (
                Connection connection = DBConnection.getConnection();
                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setString(1, notice.getTitle());
            statement.setString(2, notice.getDescription());
            statement.setInt(3, notice.getCategoryId());
            statement.setInt(4, notice.getCreatedBy());

            // department_id can be NULL
            if (notice.getDepartmentId() == null) {
                statement.setNull(5, java.sql.Types.INTEGER);
            } else {
                statement.setInt(5, notice.getDepartmentId());
            }

            // publish_date can be NULL
            if (notice.getPublishAt() == null) {
                statement.setNull(6, java.sql.Types.TIMESTAMP);
            } else {
                statement.setTimestamp(
                        6,
                        java.sql.Timestamp.valueOf(
                                notice.getPublishAt()
                        )
                );
            }

            // expiry_date can be NULL
            if (notice.getExpiresAt() == null) {
                statement.setNull(7, java.sql.Types.TIMESTAMP);
            } else {
                statement.setTimestamp(
                        7,
                        java.sql.Timestamp.valueOf(
                                notice.getExpiresAt()
                        )
                );
            }

            statement.setString(8, notice.getPriority());
            statement.setString(9, notice.getStatus());

            int rowsAffected = statement.executeUpdate();

            return rowsAffected > 0;

        } catch (Exception e) {

            e.printStackTrace();
            return false;
        }
    }
    public boolean updateNotice(Notice notice) {

        String sql = """
            UPDATE notices
            SET title = ?,
                description = ?,
                category_id = ?,
                department_id = ?,
                publish_date = ?,
                expiry_date = ?,
                priority = ?,
                status = ?
            WHERE notice_id = ?
            """;

        try (
                Connection connection = DBConnection.getConnection();
                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setString(1, notice.getTitle());
            statement.setString(2, notice.getDescription());
            statement.setInt(3, notice.getCategoryId());

            // department_id can be NULL
            if (notice.getDepartmentId() == null) {
                statement.setNull(4, java.sql.Types.INTEGER);
            } else {
                statement.setInt(4, notice.getDepartmentId());
            }

            // publish_date can be NULL
            if (notice.getPublishAt() == null) {
                statement.setNull(5, java.sql.Types.TIMESTAMP);
            } else {
                statement.setTimestamp(
                        5,
                        java.sql.Timestamp.valueOf(
                                notice.getPublishAt()
                        )
                );
            }

            // expiry_date can be NULL
            if (notice.getExpiresAt() == null) {
                statement.setNull(6, java.sql.Types.TIMESTAMP);
            } else {
                statement.setTimestamp(
                        6,
                        java.sql.Timestamp.valueOf(
                                notice.getExpiresAt()
                        )
                );
            }

            statement.setString(7, notice.getPriority());
            statement.setString(8, notice.getStatus());

            statement.setInt(9, notice.getNoticeId());

            int rowsAffected = statement.executeUpdate();

            return rowsAffected > 0;

        } catch (Exception e) {

            e.printStackTrace();
            return false;
        }
    }
    public boolean deleteNotice(int noticeId) {

        String sql = "DELETE FROM notices WHERE notice_id = ?";

        try (
                Connection connection = DBConnection.getConnection();
                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setInt(1, noticeId);

            int rowsAffected = statement.executeUpdate();

            return rowsAffected > 0;

        } catch (Exception e) {

            e.printStackTrace();
            return false;
        }
    }
    public List<Notice> searchNotices(
            String keyword,
            String status,
            String priority,
            int categoryId
    ) {

        List<Notice> notices = new ArrayList<>();

        StringBuilder sql = new StringBuilder("""
            SELECT * FROM notices
            WHERE (title LIKE ?
               OR description LIKE ?)
            """);

        if (!status.equals("ALL")) {
            sql.append(" AND status = ?");
        }

        if (!priority.equals("ALL")) {
            sql.append(" AND priority = ?");
        }

        if (categoryId != 0) {
            sql.append(" AND category_id = ?");
        }

        sql.append(" ORDER BY created_at DESC");

        try (
                Connection connection = DBConnection.getConnection();
                PreparedStatement statement =
                        connection.prepareStatement(sql.toString())
        ) {

            String searchPattern = "%" + keyword + "%";

            int parameterIndex = 1;

            statement.setString(
                    parameterIndex++,
                    searchPattern
            );

            statement.setString(
                    parameterIndex++,
                    searchPattern
            );

            if (!status.equals("ALL")) {

                statement.setString(
                        parameterIndex++,
                        status
                );
            }

            if (!priority.equals("ALL")) {

                statement.setString(
                        parameterIndex++,
                        priority
                );
            }

            if (categoryId != 0) {

                statement.setInt(
                        parameterIndex++,
                        categoryId
                );
            }

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                while (resultSet.next()) {

                    Notice notice = new Notice();

                    notice.setNoticeId(
                            resultSet.getInt("notice_id")
                    );

                    notice.setTitle(
                            resultSet.getString("title")
                    );

                    notice.setDescription(
                            resultSet.getString("description")
                    );

                    notice.setCategoryId(
                            resultSet.getInt("category_id")
                    );

                    notice.setCreatedBy(
                            resultSet.getInt("created_by")
                    );

                    int departmentId =
                            resultSet.getInt("department_id");

                    if (resultSet.wasNull()) {
                        notice.setDepartmentId(null);
                    } else {
                        notice.setDepartmentId(departmentId);
                    }

                    if (resultSet.getTimestamp("created_at") != null) {
                        notice.setCreatedAt(
                                resultSet.getTimestamp("created_at")
                                        .toLocalDateTime()
                        );
                    }

                    if (resultSet.getTimestamp("publish_date") != null) {
                        notice.setPublishAt(
                                resultSet.getTimestamp("publish_date")
                                        .toLocalDateTime()
                        );
                    }

                    if (resultSet.getTimestamp("expiry_date") != null) {
                        notice.setExpiresAt(
                                resultSet.getTimestamp("expiry_date")
                                        .toLocalDateTime()
                        );
                    }

                    notice.setPriority(
                            resultSet.getString("priority")
                    );

                    notice.setStatus(
                            resultSet.getString("status")
                    );

                    notices.add(notice);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return notices;
    }
    public List<Notice> searchNotices(
            String keyword,
            String status,
            String priority,
            int categoryId,
            Integer createdBy
    ) {

        List<Notice> notices = new ArrayList<>();

        StringBuilder sql = new StringBuilder("""
        SELECT * FROM notices
        WHERE (title LIKE ?
           OR description LIKE ?)
        """);

        if (!status.equals("ALL")) {
            sql.append(" AND status = ?");
        }

        if (!priority.equals("ALL")) {
            sql.append(" AND priority = ?");
        }

        if (categoryId != 0) {
            sql.append(" AND category_id = ?");
        }

        if (createdBy != null) {
            sql.append(" AND created_by = ?");
        }

        sql.append(" ORDER BY created_at DESC");

        try (
                Connection connection = DBConnection.getConnection();
                PreparedStatement statement =
                        connection.prepareStatement(sql.toString())
        ) {

            String searchPattern = "%" + keyword + "%";

            int parameterIndex = 1;

            statement.setString(
                    parameterIndex++,
                    searchPattern
            );

            statement.setString(
                    parameterIndex++,
                    searchPattern
            );

            if (!status.equals("ALL")) {
                statement.setString(
                        parameterIndex++,
                        status
                );
            }

            if (!priority.equals("ALL")) {
                statement.setString(
                        parameterIndex++,
                        priority
                );
            }

            if (categoryId != 0) {
                statement.setInt(
                        parameterIndex++,
                        categoryId
                );
            }

            if (createdBy != null) {
                statement.setInt(
                        parameterIndex++,
                        createdBy
                );
            }

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                while (resultSet.next()) {

                    Notice notice = new Notice();

                    notice.setNoticeId(
                            resultSet.getInt("notice_id")
                    );

                    notice.setTitle(
                            resultSet.getString("title")
                    );

                    notice.setDescription(
                            resultSet.getString("description")
                    );

                    notice.setCategoryId(
                            resultSet.getInt("category_id")
                    );

                    notice.setCreatedBy(
                            resultSet.getInt("created_by")
                    );

                    int departmentId =
                            resultSet.getInt("department_id");

                    if (resultSet.wasNull()) {
                        notice.setDepartmentId(null);
                    } else {
                        notice.setDepartmentId(departmentId);
                    }

                    if (resultSet.getTimestamp("created_at") != null) {
                        notice.setCreatedAt(
                                resultSet.getTimestamp("created_at")
                                        .toLocalDateTime()
                        );
                    }

                    if (resultSet.getTimestamp("publish_date") != null) {
                        notice.setPublishAt(
                                resultSet.getTimestamp("publish_date")
                                        .toLocalDateTime()
                        );
                    }

                    if (resultSet.getTimestamp("expiry_date") != null) {
                        notice.setExpiresAt(
                                resultSet.getTimestamp("expiry_date")
                                        .toLocalDateTime()
                        );
                    }

                    notice.setPriority(
                            resultSet.getString("priority")
                    );

                    notice.setStatus(
                            resultSet.getString("status")
                    );

                    notices.add(notice);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return notices;
    }
    public boolean updateNotice(Notice notice, int userId) {

        String sql = """
        UPDATE notices
        SET title = ?,
            description = ?,
            category_id = ?,
            department_id = ?,
            publish_date = ?,
            expiry_date = ?,
            priority = ?,
            status = ?
        WHERE notice_id = ?
        AND created_by = ?
        """;

        try (
                Connection connection = DBConnection.getConnection();
                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setString(1, notice.getTitle());
            statement.setString(2, notice.getDescription());
            statement.setInt(3, notice.getCategoryId());

            if (notice.getDepartmentId() == null) {
                statement.setNull(4, java.sql.Types.INTEGER);
            } else {
                statement.setInt(4, notice.getDepartmentId());
            }

            if (notice.getPublishAt() == null) {
                statement.setNull(5, java.sql.Types.TIMESTAMP);
            } else {
                statement.setTimestamp(
                        5,
                        java.sql.Timestamp.valueOf(
                                notice.getPublishAt()
                        )
                );
            }

            if (notice.getExpiresAt() == null) {
                statement.setNull(6, java.sql.Types.TIMESTAMP);
            } else {
                statement.setTimestamp(
                        6,
                        java.sql.Timestamp.valueOf(
                                notice.getExpiresAt()
                        )
                );
            }

            statement.setString(7, notice.getPriority());
            statement.setString(8, notice.getStatus());

            statement.setInt(9, notice.getNoticeId());

            // Ownership check
            statement.setInt(10, userId);

            int rowsAffected = statement.executeUpdate();

            return rowsAffected > 0;

        } catch (Exception e) {

            e.printStackTrace();
            return false;
        }
    }
    public boolean deleteNotice(int noticeId, int userId) {

        String sql =
                "DELETE FROM notices " +
                        "WHERE notice_id = ? " +
                        "AND created_by = ?";

        try (
                Connection connection = DBConnection.getConnection();
                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setInt(1, noticeId);

            // Ownership check
            statement.setInt(2, userId);

            int rowsAffected =
                    statement.executeUpdate();

            return rowsAffected > 0;

        } catch (Exception e) {

            e.printStackTrace();
            return false;
        }
    }
    public boolean updateNoticeStatus(
            int noticeId,
            String status
    ) {

        String sql =
                "UPDATE notices " +
                        "SET status = ? " +
                        "WHERE notice_id = ?";

        try (
                Connection connection =
                        DBConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setString(1, status);
            statement.setInt(2, noticeId);

            int rowsAffected =
                    statement.executeUpdate();

            return rowsAffected > 0;

        } catch (Exception e) {

            e.printStackTrace();
            return false;
        }
    }
    public List<Notice> getPublishedNotices(int departmentId) {

        List<Notice> notices = new ArrayList<>();

        String sql =
                "SELECT * FROM notices " +
                        "WHERE status = 'PUBLISHED' " +
                        "AND (publish_date IS NULL OR publish_date <= NOW()) " +
                        "AND (expiry_date IS NULL OR expiry_date > NOW()) " +
                        "AND (department_id IS NULL OR department_id = ?) " +
                        "ORDER BY created_at DESC";

        try (
                Connection connection = DBConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setInt(1, departmentId);

            try (ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {

                    Notice notice = new Notice();

                    notice.setNoticeId(resultSet.getInt("notice_id"));
                    notice.setTitle(resultSet.getString("title"));
                    notice.setDescription(resultSet.getString("description"));
                    notice.setCategoryId(resultSet.getInt("category_id"));
                    notice.setCreatedBy(resultSet.getInt("created_by"));

                    int dept = resultSet.getInt("department_id");
                    notice.setDepartmentId(resultSet.wasNull() ? null : dept);

                    notice.setCreatedAt(
                            resultSet.getTimestamp("created_at").toLocalDateTime());

                    if (resultSet.getTimestamp("publish_date") != null) {
                        notice.setPublishAt(
                                resultSet.getTimestamp("publish_date").toLocalDateTime());
                    }
                    if (resultSet.getTimestamp("expiry_date") != null) {
                        notice.setExpiresAt(
                                resultSet.getTimestamp("expiry_date").toLocalDateTime());
                    }

                    notice.setPriority(resultSet.getString("priority"));
                    notice.setStatus(resultSet.getString("status"));

                    notices.add(notice);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return notices;
    }

}