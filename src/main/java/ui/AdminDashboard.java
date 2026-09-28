package ui;

import model.Notice;
import model.User;
import service.NoticeService;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class AdminDashboard extends JFrame {
    private final User user;
    private final NoticeService noticeService = new NoticeService();
    private JPanel stats;

    public AdminDashboard(User user) {
        this.user = user;
        UITheme.styleFrame(this, "Admin Dashboard - Digital Notice Board", 1180, 720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        buildUI();
        setVisible(true);
    }

    private void buildUI() {
        setLayout(new BorderLayout());
        add(UITheme.sidebar("Administrator", user.getName(), this::logout, () -> new NoticeManagementFrame(user), () -> new NotificationsFrame(user, null), () -> new ProfileFrame(user)), BorderLayout.WEST);
        JPanel main = new JPanel(new BorderLayout(0, 22));
        main.setBorder(BorderFactory.createEmptyBorder(28, 30, 28, 30));
        main.setBackground(UITheme.BG);

        JPanel header = new JPanel(new BorderLayout()); header.setOpaque(false);
        JPanel h = new JPanel(); h.setOpaque(false); h.setLayout(new BoxLayout(h, BoxLayout.Y_AXIS));
        h.add(UITheme.title("Good morning, " + user.getName() + " 👋", 25));
        h.add(Box.createVerticalStrut(5)); h.add(UITheme.muted("Manage notices, approvals and campus communication from one place."));
        header.add(h, BorderLayout.WEST);
        JButton refresh = UITheme.button("↻  Refresh", false); refresh.addActionListener(e -> refreshStats()); header.add(refresh, BorderLayout.EAST);
        main.add(header, BorderLayout.NORTH);

        JPanel content = new JPanel(new BorderLayout(0, 20)); content.setOpaque(false);
        stats = new JPanel(new GridLayout(1, 3, 16, 0)); stats.setOpaque(false); content.add(stats, BorderLayout.NORTH);

        JPanel recent = UITheme.card();
        recent.setLayout(new BorderLayout(0, 12));
        recent.add(UITheme.title("Recent Notices", 17), BorderLayout.NORTH);
        JPanel list = new JPanel(); list.setOpaque(false); list.setLayout(new BoxLayout(list, BoxLayout.Y_AXIS));
        List<Notice> notices = noticeService.getAllNotices();
        int count = Math.min(6, notices.size());
        for (int i = 0; i < count; i++) addNoticePreview(list, notices.get(i));
        if (count == 0) list.add(UITheme.muted("No notices available yet."));
        JScrollPane sp = new JScrollPane(list); sp.setBorder(null); sp.getViewport().setBackground(Color.WHITE); recent.add(sp, BorderLayout.CENTER);
        content.add(recent, BorderLayout.CENTER);
        main.add(content, BorderLayout.CENTER);
        add(main, BorderLayout.CENTER);
        refreshStats();
    }

    private void addNoticePreview(JPanel parent, Notice n) {
        JPanel row = new JPanel(new BorderLayout(12, 4)); row.setOpaque(false); row.setBorder(BorderFactory.createEmptyBorder(9, 4, 9, 4));
        JPanel text = new JPanel(); text.setOpaque(false); text.setLayout(new BoxLayout(text, BoxLayout.Y_AXIS));
        JLabel title = UITheme.title(n.getTitle(), 13); title.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel meta = UITheme.muted(n.getPriority() + " priority  •  " + n.getStatus()); meta.setAlignmentX(Component.LEFT_ALIGNMENT);
        text.add(title); text.add(Box.createVerticalStrut(3)); text.add(meta); row.add(text, BorderLayout.CENTER);
        JLabel badge = UITheme.badge(n.getStatus()); UITheme.colorBadge(badge, n.getStatus()); row.add(badge, BorderLayout.EAST);
        parent.add(row); parent.add(new JSeparator());
    }

    private void refreshStats() {
        stats.removeAll(); List<Notice> all = noticeService.getAllNotices();
        int published = 0, pending = 0;
        for (Notice n : all) { if ("PUBLISHED".equals(n.getStatus())) published++; if ("SUBMITTED".equals(n.getStatus())) pending++; }
        stats.add(UITheme.statCard(String.valueOf(all.size()), "Total notices", "▤"));
        stats.add(UITheme.statCard(String.valueOf(published), "Published", "✓"));
        stats.add(UITheme.statCard(String.valueOf(pending), "Pending approval", "!"));
        stats.revalidate(); stats.repaint();
    }

    private void logout() { dispose(); new LoginFrame(); }
}
