package ui;

import model.Notice;
import model.User;
import service.NoticeService;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class FacultyDashboard extends JFrame {
    private final User user;
    private final NoticeService noticeService = new NoticeService();
    private JPanel stats;

    public FacultyDashboard(User user) {
        this.user = user;
        UITheme.styleFrame(this, "Faculty Dashboard - Digital Notice Board", 1180, 720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        buildUI(); setVisible(true);
    }

    private void buildUI() {
        setLayout(new BorderLayout());
        add(UITheme.sidebar("Faculty", user.getName(), this::logout, () -> new NoticeManagementFrame(user), () -> new NotificationsFrame(user, null), () -> new ProfileFrame(user)), BorderLayout.WEST);
        JPanel main = new JPanel(new BorderLayout(0, 22)); main.setBackground(UITheme.BG); main.setBorder(BorderFactory.createEmptyBorder(28,30,28,30));
        JPanel header = new JPanel(new BorderLayout()); header.setOpaque(false);
        JPanel h = new JPanel(); h.setOpaque(false); h.setLayout(new BoxLayout(h, BoxLayout.Y_AXIS));
        h.add(UITheme.title("Welcome, " + user.getName() + " 👋", 25)); h.add(Box.createVerticalStrut(5)); h.add(UITheme.muted("Create, edit and track your college notices.")); header.add(h, BorderLayout.WEST);
        JButton manage = UITheme.button("＋  Manage Notices", true); manage.addActionListener(e -> new NoticeManagementFrame(user)); header.add(manage, BorderLayout.EAST); main.add(header, BorderLayout.NORTH);
        JPanel body = new JPanel(new BorderLayout(0,20)); body.setOpaque(false);
        stats = new JPanel(new GridLayout(1,3,16,0)); stats.setOpaque(false); body.add(stats, BorderLayout.NORTH);
        JPanel recent = UITheme.card(); recent.setLayout(new BorderLayout(0,12)); recent.add(UITheme.title("My Recent Notices",17),BorderLayout.NORTH);
        JPanel list = new JPanel(); list.setOpaque(false); list.setLayout(new BoxLayout(list,BoxLayout.Y_AXIS)); List<Notice> notices=noticeService.getNoticesByCreator(user.getUserId());
        for(int i=0;i<Math.min(6,notices.size());i++) addRow(list,notices.get(i)); if(notices.isEmpty()) list.add(UITheme.muted("You have not created any notices yet."));
        recent.add(new JScrollPane(list),BorderLayout.CENTER); body.add(recent,BorderLayout.CENTER); main.add(body,BorderLayout.CENTER); add(main,BorderLayout.CENTER); refreshStats();
    }

    private void addRow(JPanel p, Notice n){ JPanel r=new JPanel(new BorderLayout(10,4)); r.setOpaque(false); r.setBorder(BorderFactory.createEmptyBorder(8,4,8,4)); JPanel t=new JPanel();t.setOpaque(false);t.setLayout(new BoxLayout(t,BoxLayout.Y_AXIS));t.add(UITheme.title(n.getTitle(),13));t.add(Box.createVerticalStrut(3));t.add(UITheme.muted(n.getPriority()+" priority  •  "+n.getStatus()));r.add(t,BorderLayout.CENTER);JLabel b=UITheme.badge(n.getStatus());UITheme.colorBadge(b,n.getStatus());r.add(b,BorderLayout.EAST);p.add(r);p.add(new JSeparator()); }
    private void refreshStats(){ stats.removeAll(); List<Notice> a=noticeService.getNoticesByCreator(user.getUserId());int published=0,pending=0;for(Notice n:a){if("PUBLISHED".equals(n.getStatus()))published++;if("SUBMITTED".equals(n.getStatus()))pending++;}stats.add(UITheme.statCard(String.valueOf(a.size()),"My notices","▤"));stats.add(UITheme.statCard(String.valueOf(published),"Published","✓"));stats.add(UITheme.statCard(String.valueOf(pending),"Pending","!"));stats.revalidate();stats.repaint(); }
    private void logout(){dispose();new LoginFrame();}
}
