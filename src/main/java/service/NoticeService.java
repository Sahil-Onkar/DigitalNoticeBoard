package service;

import dao.NoticeDAO;
import model.Notice;

import java.util.List;

public class NoticeService {

    private NoticeDAO noticeDAO;
    private dao.NotificationDAO notificationDAO = new dao.NotificationDAO();
    public NoticeService() {
        noticeDAO = new NoticeDAO();
    }

    public List<Notice> getAllNotices() {
        return noticeDAO.getAllNotices();
    }
    public List<Notice> getNoticesByCreator(int userId) {
        return noticeDAO.getNoticesByCreator(userId);
    }
    public List<Notice> searchNotices(
            String keyword,
            String status,
            String priority,
            int categoryId
    ) {
        return noticeDAO.searchNotices(
                keyword,
                status,
                priority,
                categoryId
        );
    }
    public List<Notice> searchNotices(
            String keyword,
            String status,
            String priority,
            int categoryId,
            Integer createdBy
    ) {
        return noticeDAO.searchNotices(
                keyword,
                status,
                priority,
                categoryId,
                createdBy
        );
    }
    public List<Notice> getPublishedNotices(int departmentId) {
        return noticeDAO.getPublishedNotices(departmentId);
    }
    public boolean insertNotice(Notice notice, int roleId) {

        if (notice == null) {
            return false;
        }

        if (!isValidStatusForRole(roleId, notice.getStatus())) {
            return false;
        }

        return noticeDAO.insertNotice(notice);
    }
    public boolean updateNotice(Notice notice) {
        return noticeDAO.updateNotice(notice);
    }
    public boolean updateNotice(Notice notice, int userId) {
        return noticeDAO.updateNotice(notice, userId);
    }
    public boolean deleteNotice(int noticeId) {
        return noticeDAO.deleteNotice(noticeId);
    }
    public boolean deleteNotice(int noticeId, int userId) {
        return noticeDAO.deleteNotice(noticeId, userId);
    }
    public boolean isValidStatusForRole(int roleId, String status) {

        if (status == null) {
            return false;
        }

        if (roleId == 1) {
            // Admin can use all statuses
            return status.equals("DRAFT")
                    || status.equals("SUBMITTED")
                    || status.equals("APPROVED")
                    || status.equals("REJECTED")
                    || status.equals("PUBLISHED");
        }

        if (roleId == 2) {
            // Faculty can only create Draft or Submitted notices
            return status.equals("DRAFT")
                    || status.equals("SUBMITTED");
        }

        // Students cannot create notices
        return false;
    }
    public boolean updateNoticeStatus(int noticeId, String status) {

        boolean updated = noticeDAO.updateNoticeStatus(noticeId, status);

        if (updated && "PUBLISHED".equals(status)) {

            for (Notice notice : noticeDAO.getAllNotices()) {

                if (notice.getNoticeId() == noticeId) {

                    notificationDAO.notifyStudents(
                            noticeId,
                            notice.getDepartmentId(),
                            "New notice: " + notice.getTitle()
                    );
                    break;
                }
            }
        }

        return updated;
    }
}