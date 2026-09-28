import dao.NoticeDAO;

public class TestDeleteNotice {

    public static void main(String[] args) {

        NoticeDAO noticeDAO = new NoticeDAO();

        // Delete the test notice you created earlier
        int noticeId = 11;

        boolean result = noticeDAO.deleteNotice(noticeId);

        if (result) {
            System.out.println("Notice deleted successfully!");
        } else {
            System.out.println("Notice not found or delete failed.");
        }
    }
}