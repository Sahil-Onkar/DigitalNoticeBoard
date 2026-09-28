import model.Notice;
import service.NoticeService;

import java.util.List;

public class TestNoticeService {

    public static void main(String[] args) {

        NoticeService noticeService = new NoticeService();

        List<Notice> notices = noticeService.getAllNotices();

        System.out.println("Total Notices: " + notices.size());

        for (Notice notice : notices) {

            System.out.println(
                    notice.getNoticeId() + " - "
                            + notice.getTitle() + " - "
                            + notice.getStatus()
            );
        }
    }
}