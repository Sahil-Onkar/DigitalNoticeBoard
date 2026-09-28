import dao.NoticeDAO;
import model.Notice;

public class TestInsertNotice {

    public static void main(String[] args) {

        Notice notice = new Notice();

        notice.setTitle("Java Advanced Workshop");

        notice.setDescription(
                "A workshop on Advanced Java and JDBC will be conducted for students."
        );

        notice.setCategoryId(3);

        // Use an existing user_id from your users table
        notice.setCreatedBy(3);

        // CSE department
        notice.setDepartmentId(1);

        notice.setPublishAt(
                java.time.LocalDateTime.of(2026, 9, 15, 10, 0)
        );

        notice.setExpiresAt(
                java.time.LocalDateTime.of(2026, 9, 25, 17, 0)
        );

        notice.setPriority("HIGH");

        notice.setStatus("DRAFT");

        NoticeDAO noticeDAO = new NoticeDAO();

        boolean result = noticeDAO.insertNotice(notice);

        if (result) {
            System.out.println("Notice inserted successfully!");
        } else {
            System.out.println("Failed to insert notice.");
        }
    }
}