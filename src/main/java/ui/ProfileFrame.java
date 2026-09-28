package ui;

import model.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.format.DateTimeFormatter;

/** Modern read-only profile screen for the currently logged-in user. */
public class ProfileFrame extends JFrame {
    private final User user;

    public ProfileFrame(User user) {
        this.user = user;
        UITheme.styleFrame(this, "My Profile - Digital Notice Board", 760, 560);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        buildUI();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void buildUI() {
        JPanel root = new JPanel(new BorderLayout(0, 18));
        root.setBackground(UITheme.BG);
        root.setBorder(new EmptyBorder(26, 30, 26, 30));

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);

        JPanel heading = new JPanel();
        heading.setOpaque(false);
        heading.setLayout(new BoxLayout(heading, BoxLayout.Y_AXIS));
        heading.add(UITheme.title("My Profile", 25));
        heading.add(Box.createVerticalStrut(5));
        heading.add(UITheme.muted("View your Digital Notice Board account information."));
        header.add(heading, BorderLayout.WEST);

        JButton close = UITheme.button("Close", false);
        close.addActionListener(e -> dispose());
        header.add(close, BorderLayout.EAST);
        root.add(header, BorderLayout.NORTH);

        JPanel card = UITheme.card();
        card.setLayout(new BorderLayout(24, 0));

        JPanel avatarPanel = new JPanel(new BorderLayout());
        avatarPanel.setOpaque(false);
        JLabel avatar = new JLabel(UITheme.initials(user.getName()), SwingConstants.CENTER);
        avatar.setOpaque(true);
        avatar.setBackground(UITheme.BLUE);
        avatar.setForeground(Color.WHITE);
        avatar.setFont(UITheme.font(30, Font.BOLD));
        avatar.setPreferredSize(new Dimension(105, 105));
        avatarPanel.add(avatar, BorderLayout.NORTH);
        card.add(avatarPanel, BorderLayout.WEST);

        JPanel details = new JPanel();
        details.setOpaque(false);
        details.setLayout(new BoxLayout(details, BoxLayout.Y_AXIS));

        JLabel name = UITheme.title(user.getName(), 22);
        details.add(name);
        details.add(Box.createVerticalStrut(5));
        details.add(UITheme.muted(roleName(user.getRole())));
        details.add(Box.createVerticalStrut(22));

        details.add(infoRow("Email", safe(user.getEmail())));
        details.add(Box.createVerticalStrut(13));
        details.add(infoRow("Department", departmentText(user.getDepartmentId())));
        details.add(Box.createVerticalStrut(13));
        details.add(infoRow("Account Status", safe(user.getStatus())));
        details.add(Box.createVerticalStrut(13));

        String created = user.getCreated_at() == null ? "Not available" :
                user.getCreated_at().format(DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm"));
        details.add(infoRow("Member Since", created));

        card.add(details, BorderLayout.CENTER);
        root.add(card, BorderLayout.CENTER);

        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        footer.setOpaque(false);
        JButton done = UITheme.button("Done", true);
        done.addActionListener(e -> dispose());
        footer.add(done);
        root.add(footer, BorderLayout.SOUTH);

        setContentPane(root);
    }

    private JPanel infoRow(String label, String value) {
        JPanel row = new JPanel(new BorderLayout(18, 0));
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

        JLabel l = UITheme.muted(label);
        l.setPreferredSize(new Dimension(120, 25));
        JLabel v = UITheme.title(value, 13);
        row.add(l, BorderLayout.WEST);
        row.add(v, BorderLayout.CENTER);
        return row;
    }

    private String departmentText(int id) {
        switch (id) {
            case 1: return "CSE - Computer Science and Engineering";
            case 2: return "IT - Information Technology";
            case 3: return "ENTC - Electronics and Telecommunication";
            case 4: return "ME - Mechanical Engineering";
            case 5: return "CIVIL - Civil Engineering";
            default: return "Not assigned";
        }
    }

    private String roleName(int role) {
        switch (role) {
            case 1: return "Administrator";
            case 2: return "Faculty";
            case 3: return "Student";
            default: return "User";
        }
    }

    private String safe(String value) {
        return value == null || value.isBlank() ? "Not available" : value;
    }
}
