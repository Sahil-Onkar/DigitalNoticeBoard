import dao.NoticeDAO;
import model.Notice;

import java.util.List;

public class TestNoticeDAO {

    public static void main(String[] args) {

        NoticeDAO noticeDAO = new NoticeDAO();

        List<Notice> notices = noticeDAO.getAllNotices();

        System.out.println("Total Notices: " + notices.size());

        for (Notice notice : notices) {

            System.out.println("----------------------------");

            System.out.println(
                    "ID: " + notice.getNoticeId()
            );

            System.out.println(
                    "Title: " + notice.getTitle()
            );

            System.out.println(
                    "Created By: " + notice.getCreatedBy()
            );

            System.out.println(
                    "Category ID: " + notice.getCategoryId()
            );

            System.out.println(
                    "Department ID: " + notice.getDepartmentId()
            );

            System.out.println(
                    "Priority: " + notice.getPriority()
            );

            System.out.println(
                    "Status: " + notice.getStatus()
            );
        }
    }
}