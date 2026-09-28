package ui;

import model.User;
import service.AuthService;
import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {
    private JTextField emailField;
    private JPasswordField passwordField;
    private final AuthService authService = new AuthService();

    public LoginFrame() {
        UITheme.styleFrame(this, "Digital Notice Board - Login", 920, 570);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        createUI();
        setVisible(true);
    }

    private void createUI() {
        JPanel root = new JPanel(new GridLayout(1, 2));
        root.setBackground(UITheme.BG);

        JPanel left = new JPanel();
        left.setBackground(UITheme.NAVY);
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        left.setBorder(BorderFactory.createEmptyBorder(65, 55, 55, 55));
        JLabel logo = new JLabel("▣");
        logo.setForeground(new Color(139, 158, 255));
        logo.setFont(UITheme.font(54, Font.BOLD));
        JLabel brand = new JLabel("Digital Notice Board");
        brand.setForeground(Color.WHITE);
        brand.setFont(UITheme.font(27, Font.BOLD));
        JLabel sub = new JLabel("Smart campus communication, simplified.");
        sub.setForeground(new Color(190, 199, 222));
        sub.setFont(UITheme.font(14, Font.PLAIN));
        left.add(logo); left.add(Box.createVerticalStrut(20)); left.add(brand);
        left.add(Box.createVerticalStrut(8)); left.add(sub);
        left.add(Box.createVerticalGlue());
        JLabel footer = new JLabel("ADMIN  •  FACULTY  •  STUDENT");
        footer.setForeground(new Color(150, 162, 194));
        footer.setFont(UITheme.font(10, Font.BOLD));
        left.add(footer);

        JPanel right = new JPanel(new GridBagLayout());
        right.setBackground(UITheme.BG);
        JPanel card = UITheme.card();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setPreferredSize(new Dimension(360, 390));

        JLabel welcome = UITheme.title("Welcome back 👋", 25);
        JLabel hint = UITheme.muted("Sign in to continue to your dashboard");
        card.add(welcome); card.add(Box.createVerticalStrut(6)); card.add(hint);
        card.add(Box.createVerticalStrut(28));

        card.add(UITheme.muted("EMAIL ADDRESS"));
        emailField = new JTextField();
        UITheme.styleField(emailField);
        emailField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        card.add(Box.createVerticalStrut(7)); card.add(emailField);
        card.add(Box.createVerticalStrut(16));

        card.add(UITheme.muted("PASSWORD"));
        passwordField = new JPasswordField();
        UITheme.styleField(passwordField);
        passwordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        card.add(Box.createVerticalStrut(7)); card.add(passwordField);
        card.add(Box.createVerticalStrut(24));

        JButton login = UITheme.button("Sign In", true);
        login.setAlignmentX(Component.LEFT_ALIGNMENT);
        login.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
        JButton clear = UITheme.button("Clear", false);
        clear.setAlignmentX(Component.LEFT_ALIGNMENT);
        clear.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        card.add(login); card.add(Box.createVerticalStrut(9)); card.add(clear);

        JButton register = UITheme.button("Create New Student / Faculty Account", false);
        register.setAlignmentX(Component.LEFT_ALIGNMENT);
        register.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        card.add(Box.createVerticalStrut(9));
        card.add(register);

        card.add(Box.createVerticalGlue());
        JLabel note = UITheme.muted("Use your registered college account.");
        note.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(note);

        login.addActionListener(e -> login());
        clear.addActionListener(e -> clearFields());
        register.addActionListener(e -> openRegistration());
        passwordField.addActionListener(e -> login());

        right.add(card);
        root.add(left); root.add(right);
        setContentPane(root);
        SwingUtilities.invokeLater(() -> emailField.requestFocusInWindow());
    }

    private void login() {
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword());
        if (email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter email and password.", "Login Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        User user = authService.login(email, password);
        if (user == null) {
            JOptionPane.showMessageDialog(this, "Invalid email/password or account inactive.", "Login Failed", JOptionPane.ERROR_MESSAGE);
            return;
        }
        switch (user.getRole()) {
            case 1 -> new AdminDashboard(user);
            case 2 -> new FacultyDashboard(user);
            case 3 -> new StudentDashboard(user);
            default -> {
                JOptionPane.showMessageDialog(this, "Unknown user role: " + user.getRole(), "Role Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        dispose();
    }

    private void openRegistration() {
        dispose();
        new RegistrationFrame();
    }

    private void clearFields() {
        emailField.setText(""); passwordField.setText(""); emailField.requestFocusInWindow();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginFrame::new);
    }
}
