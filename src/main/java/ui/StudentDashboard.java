package ui;

import dao.NotificationDAO;
import model.User;
import javax.swing.*;
import java.awt.*;

public class StudentDashboard extends JFrame {
    private final NotificationDAO notificationDAO = new NotificationDAO();
    private final User user;
    private JButton notificationButton;

    public StudentDashboard(User user) {
        this.user=user; UITheme.styleFrame(this,"Student Dashboard - Digital Notice Board",1180,720); setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); buildUI(); setVisible(true);
    }
    private void buildUI(){
        setLayout(new BorderLayout());
        add(UITheme.sidebar("Student", user.getName(), this::logout, () -> new StudentNoticeFrame(user), () -> new NotificationsFrame(user, this::refreshNotificationCount), () -> new ProfileFrame(user)), BorderLayout.WEST);
        JPanel main=new JPanel(new BorderLayout(0,22));main.setBackground(UITheme.BG);main.setBorder(BorderFactory.createEmptyBorder(28,30,28,30));
        JPanel head=new JPanel(new BorderLayout());head.setOpaque(false);JPanel h=new JPanel();h.setOpaque(false);h.setLayout(new BoxLayout(h,BoxLayout.Y_AXIS));h.add(UITheme.title("Hello, "+user.getName()+" 👋",25));h.add(Box.createVerticalStrut(5));h.add(UITheme.muted("Stay updated with the latest college announcements."));head.add(h,BorderLayout.WEST);
        notificationButton=UITheme.button("🔔  Notifications",false);notificationButton.addActionListener(e->new NotificationsFrame(user,this::refreshNotificationCount));head.add(notificationButton,BorderLayout.EAST);main.add(head,BorderLayout.NORTH);
        JPanel content=new JPanel(new GridLayout(1,2,18,0));content.setOpaque(false);
        JPanel notices=UITheme.card();notices.setLayout(new BoxLayout(notices,BoxLayout.Y_AXIS));notices.add(UITheme.title("College Notices",18));notices.add(Box.createVerticalStrut(8));notices.add(UITheme.muted("Browse published notices for your department."));notices.add(Box.createVerticalGlue());JButton view=UITheme.button("View All Notices  →",true);view.addActionListener(e->new StudentNoticeFrame(user));notices.add(view);
        JPanel alert=UITheme.card();alert.setLayout(new BoxLayout(alert,BoxLayout.Y_AXIS));alert.add(UITheme.title("Quick access",18));alert.add(Box.createVerticalStrut(14));alert.add(UITheme.muted("Important announcements and updates appear here as notifications."));alert.add(Box.createVerticalStrut(20));JButton n=UITheme.button("Open Notifications",false);n.addActionListener(e->new NotificationsFrame(user,this::refreshNotificationCount));alert.add(n);content.add(notices);content.add(alert);main.add(content,BorderLayout.CENTER);add(main,BorderLayout.CENTER);refreshNotificationCount();
    }
    private void refreshNotificationCount(){int count=notificationDAO.getUnreadCount(user.getUserId());notificationButton.setText("🔔  Notifications"+(count>0?" ("+count+")":""));}
    private void logout(){dispose();new LoginFrame();}
}
