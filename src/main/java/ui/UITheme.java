package ui;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

/** Shared modern theme for the Digital Notice Board Swing UI. */
public final class UITheme {
    public static final Color NAVY = new Color(24, 35, 64);
    public static final Color BLUE = new Color(61, 92, 255);
    public static final Color BLUE_DARK = new Color(48, 70, 190);
    public static final Color BG = new Color(246, 248, 252);
    public static final Color CARD = Color.WHITE;
    public static final Color TEXT = new Color(31, 41, 55);
    public static final Color MUTED = new Color(107, 114, 128);
    public static final Color BORDER = new Color(226, 231, 240);
    public static final Color GREEN = new Color(22, 163, 74);
    public static final Color ORANGE = new Color(234, 136, 28);
    public static final Color RED = new Color(220, 53, 69);

    private UITheme() {}

    public static void install() {
        UIManager.put("Panel.background", BG);
        UIManager.put("OptionPane.background", CARD);
        UIManager.put("Label.foreground", TEXT);
        UIManager.put("TextField.background", Color.WHITE);
        UIManager.put("TextField.foreground", TEXT);
        UIManager.put("TextField.caretForeground", BLUE);
        UIManager.put("TextArea.background", Color.WHITE);
        UIManager.put("TextArea.foreground", TEXT);
        UIManager.put("ComboBox.background", Color.WHITE);
        UIManager.put("ComboBox.foreground", TEXT);
        UIManager.put("Table.background", Color.WHITE);
        UIManager.put("Table.foreground", TEXT);
        UIManager.put("Table.gridColor", BORDER);
        UIManager.put("Table.selectionBackground", new Color(224, 231, 255));
        UIManager.put("Table.selectionForeground", TEXT);
    }

    public static Font font(int size, int style) {
        return new Font("Segoe UI", style, size);
    }

    public static void styleFrame(JFrame frame, String title, int width, int height) {
        install();
        frame.setTitle(title);
        frame.setSize(width, height);
        frame.setMinimumSize(new Dimension(Math.min(width, 850), Math.min(height, 550)));
        frame.setLocationRelativeTo(null);
        frame.getContentPane().setBackground(BG);
    }

    public static JPanel card() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(CARD);
        p.setBorder(new CompoundBorder(
                new LineBorder(BORDER, 1, true),
                new EmptyBorder(18, 18, 18, 18)
        ));
        return p;
    }

    public static JButton button(String text, boolean primary) {
        JButton b = new JButton(text);
        b.setFont(font(13, Font.BOLD));
        b.setForeground(primary ? Color.WHITE : TEXT);
        b.setBackground(primary ? BLUE : Color.WHITE);
        b.setFocusPainted(false);
        b.setBorder(new CompoundBorder(
                new LineBorder(primary ? BLUE : BORDER, 1, true),
                new EmptyBorder(10, 16, 10, 16)
        ));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }

    public static JButton sidebarButton(String text, boolean selected) {
        JButton b = new JButton(text);
        b.setHorizontalAlignment(SwingConstants.LEFT);
        b.setFont(font(13, selected ? Font.BOLD : Font.PLAIN));
        b.setForeground(Color.WHITE);
        b.setBackground(selected ? new Color(55, 70, 110) : NAVY);
        b.setBorder(new EmptyBorder(12, 16, 12, 10));
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }

    public static void styleField(JTextField field) {
        field.setFont(font(13, Font.PLAIN));
        field.setBorder(new CompoundBorder(
                new LineBorder(BORDER, 1, true),
                new EmptyBorder(8, 10, 8, 10)
        ));
    }

    public static void styleCombo(JComboBox<?> box) {
        box.setFont(font(13, Font.PLAIN));
        box.setBackground(Color.WHITE);
        box.setForeground(TEXT);
        box.setBorder(new LineBorder(BORDER, 1, true));
    }

    public static void styleTable(JTable table) {
        table.setFont(font(12, Font.PLAIN));
        table.setRowHeight(38);
        table.setShowVerticalLines(false);
        table.setGridColor(BORDER);
        table.setIntercellSpacing(new Dimension(0, 1));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setFillsViewportHeight(true);
        table.getTableHeader().setFont(font(12, Font.BOLD));
        table.getTableHeader().setForeground(TEXT);
        table.getTableHeader().setBackground(new Color(242, 245, 250));
        table.getTableHeader().setPreferredSize(new Dimension(0, 40));
        table.setAutoCreateRowSorter(true);
    }

    public static JLabel title(String text, int size) {
        JLabel l = new JLabel(text);
        l.setFont(font(size, Font.BOLD));
        l.setForeground(TEXT);
        return l;
    }

    public static JLabel muted(String text) {
        JLabel l = new JLabel(text);
        l.setFont(font(12, Font.PLAIN));
        l.setForeground(MUTED);
        return l;
    }

    public static JLabel badge(String text) {
        JLabel l = new JLabel("  " + text + "  ");
        l.setOpaque(true);
        l.setFont(font(11, Font.BOLD));
        l.setForeground(TEXT);
        l.setBackground(new Color(239, 242, 247));
        l.setBorder(new EmptyBorder(5, 2, 5, 2));
        return l;
    }

    public static void colorBadge(JLabel label, String value) {
        String v = value == null ? "" : value.toUpperCase();
        if (v.equals("HIGH") || v.equals("REJECTED")) {
            label.setBackground(new Color(254, 226, 226));
            label.setForeground(new Color(185, 28, 28));
        } else if (v.equals("MEDIUM") || v.equals("SUBMITTED")) {
            label.setBackground(new Color(255, 237, 213));
            label.setForeground(new Color(154, 52, 18));
        } else if (v.equals("PUBLISHED") || v.equals("APPROVED")) {
            label.setBackground(new Color(220, 252, 231));
            label.setForeground(new Color(21, 128, 61));
        } else {
            label.setBackground(new Color(239, 246, 255));
            label.setForeground(new Color(30, 64, 175));
        }
    }

    public static void styleBadgeRenderer(JTable table, int column) {
        table.getColumnModel().getColumn(column).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object value, boolean selected,
                                                            boolean focused, int row, int col) {
                JLabel l = (JLabel) super.getTableCellRendererComponent(t, value, selected, focused, row, col);
                l.setHorizontalAlignment(SwingConstants.CENTER);
                l.setOpaque(true);
                l.setBorder(new EmptyBorder(5, 8, 5, 8));
                if (!selected) colorBadge(l, value == null ? "" : value.toString());
                return l;
            }
        });
    }

    public static JPanel sidebar(String role, String userName, Runnable logoutAction) {
        return sidebar(role, userName, logoutAction, null, null, null);
    }

    public static JPanel sidebar(String role, String userName, Runnable logoutAction, Runnable noticesAction, Runnable notificationsAction) {
        return sidebar(role, userName, logoutAction, noticesAction, notificationsAction, null);
    }

    public static JPanel sidebar(String role, String userName, Runnable logoutAction, Runnable noticesAction, Runnable notificationsAction, Runnable profileAction) {
        JPanel side = new JPanel(new BorderLayout(0, 12));
        side.setBackground(NAVY);
        side.setBorder(new EmptyBorder(24, 14, 18, 14));
        side.setPreferredSize(new Dimension(230, 0));

        JPanel brand = new JPanel(new BorderLayout(8, 2));
        brand.setOpaque(false);
        JLabel icon = new JLabel("▣");
        icon.setForeground(new Color(139, 158, 255));
        icon.setFont(font(30, Font.BOLD));
        brand.add(icon, BorderLayout.WEST);
        JPanel bt = new JPanel();
        bt.setOpaque(false);
        bt.setLayout(new BoxLayout(bt, BoxLayout.Y_AXIS));
        JLabel name = new JLabel("Digital Notice");
        name.setForeground(Color.WHITE);
        name.setFont(font(17, Font.BOLD));
        JLabel sub = new JLabel("BOARD SYSTEM");
        sub.setForeground(new Color(174, 185, 215));
        sub.setFont(font(9, Font.BOLD));
        bt.add(name); bt.add(sub);
        brand.add(bt, BorderLayout.CENTER);
        side.add(brand, BorderLayout.NORTH);

        JPanel nav = new JPanel();
        nav.setOpaque(false);
        nav.setLayout(new BoxLayout(nav, BoxLayout.Y_AXIS));
        JLabel menu = new JLabel("  MENU");
        menu.setForeground(new Color(150, 162, 194));
        menu.setFont(font(10, Font.BOLD));
        menu.setBorder(new EmptyBorder(12, 0, 8, 0));
        nav.add(menu);

        JButton dashboard = sidebarButton("⌂   Dashboard", true);
        dashboard.addActionListener(e -> {});
        nav.add(dashboard);
        nav.add(Box.createVerticalStrut(5));

        JButton notices = sidebarButton("▤   Notices", false);
        if (noticesAction != null) notices.addActionListener(e -> noticesAction.run());
        nav.add(notices);
        nav.add(Box.createVerticalStrut(5));

        JButton alerts = sidebarButton("◉   Notifications", false);
        if (notificationsAction != null) alerts.addActionListener(e -> notificationsAction.run());
        nav.add(alerts);
        nav.add(Box.createVerticalStrut(5));

        JButton profile = sidebarButton("◎   My Profile", false);
        if (profileAction != null) profile.addActionListener(e -> profileAction.run());
        nav.add(profile);
        nav.add(Box.createVerticalGlue());

        side.add(nav, BorderLayout.CENTER);

        JPanel bottom = new JPanel(new BorderLayout(8, 10));
        bottom.setOpaque(false);
        JPanel userPanel = new JPanel(new BorderLayout(8, 2));
        userPanel.setOpaque(false);
        JLabel avatar = new JLabel(initials(userName), SwingConstants.CENTER);
        avatar.setOpaque(true);
        avatar.setForeground(Color.WHITE);
        avatar.setBackground(BLUE);
        avatar.setFont(font(12, Font.BOLD));
        avatar.setPreferredSize(new Dimension(38, 38));
        JPanel info = new JPanel();
        info.setOpaque(false);
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
        JLabel un = new JLabel(userName);
        un.setForeground(Color.WHITE);
        un.setFont(font(12, Font.BOLD));
        JLabel ur = new JLabel(role);
        ur.setForeground(new Color(174, 185, 215));
        ur.setFont(font(10, Font.PLAIN));
        info.add(un); info.add(ur);
        userPanel.add(avatar, BorderLayout.WEST);
        userPanel.add(info, BorderLayout.CENTER);
        bottom.add(userPanel, BorderLayout.NORTH);

        JButton logout = sidebarButton("⇥   Logout", false);
        logout.addActionListener(e -> logoutAction.run());
        bottom.add(logout, BorderLayout.SOUTH);
        side.add(bottom, BorderLayout.SOUTH);
        return side;
    }

    public static String initials(String name) {
        if (name == null || name.isBlank()) return "DN";
        String[] p = name.trim().split("\\s+");
        return p.length == 1 ? p[0].substring(0, 1).toUpperCase() :
                (p[0].substring(0, 1) + p[p.length - 1].substring(0, 1)).toUpperCase();
    }

    public static JPanel statCard(String number, String label, String icon) {
        JPanel p = card();
        JPanel text = new JPanel();
        text.setOpaque(false);
        text.setLayout(new BoxLayout(text, BoxLayout.Y_AXIS));
        JLabel n = title(number, 25);
        JLabel l = muted(label);
        text.add(n); text.add(Box.createVerticalStrut(4)); text.add(l);
        JLabel i = new JLabel(icon, SwingConstants.CENTER);
        i.setOpaque(true);
        i.setBackground(new Color(238, 242, 255));
        i.setForeground(BLUE);
        i.setFont(font(22, Font.BOLD));
        i.setPreferredSize(new Dimension(48, 48));
        p.add(text, BorderLayout.CENTER);
        p.add(i, BorderLayout.EAST);
        return p;
    }
}
