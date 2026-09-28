package ui;

import dao.DepartmentDAO;
import dao.UserDAO;
import model.Department;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class RegistrationFrame extends JFrame {

    private JTextField nameField;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JComboBox<RoleItem> roleComboBox;
    private JComboBox<DepartmentItem> departmentComboBox;

    private final UserDAO userDAO = new UserDAO();
    private final DepartmentDAO departmentDAO = new DepartmentDAO();

    public RegistrationFrame() {
        UITheme.styleFrame(this, "Digital Notice Board - Registration", 920, 650);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        createUI();
        loadDepartments();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void createUI() {
        JPanel root = new JPanel(new GridLayout(1, 2));
        root.setBackground(UITheme.BG);

        JPanel left = new JPanel();
        left.setBackground(UITheme.NAVY);
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        left.setBorder(new EmptyBorder(60, 50, 50, 50));

        JLabel logo = new JLabel("▣");
        logo.setForeground(new Color(139, 158, 255));
        logo.setFont(UITheme.font(54, Font.BOLD));

        JLabel brand = new JLabel("Digital Notice Board");
        brand.setForeground(Color.WHITE);
        brand.setFont(UITheme.font(27, Font.BOLD));

        JLabel sub = new JLabel("<html>Create your account and stay<br>connected with campus.</html>");
        sub.setForeground(new Color(190, 199, 222));
        sub.setFont(UITheme.font(14, Font.PLAIN));

        left.add(logo);
        left.add(Box.createVerticalStrut(20));
        left.add(brand);
        left.add(Box.createVerticalStrut(8));
        left.add(sub);
        left.add(Box.createVerticalGlue());

        JLabel info = new JLabel("<html>✓ Student & Faculty registration<br><br>✓ Department-based access<br><br>✓ Secure password storage</html>");
        info.setForeground(new Color(205, 212, 232));
        info.setFont(UITheme.font(13, Font.PLAIN));
        left.add(info);
        left.add(Box.createVerticalGlue());

        JLabel footer = new JLabel("ACCOUNT REGISTRATION");
        footer.setForeground(new Color(150, 162, 194));
        footer.setFont(UITheme.font(10, Font.BOLD));
        left.add(footer);

        JPanel right = new JPanel(new GridBagLayout());
        right.setBackground(UITheme.BG);

        JPanel card = UITheme.card();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setPreferredSize(new Dimension(390, 570));

        JLabel title = UITheme.title("Create Account", 25);
        JLabel hint = UITheme.muted("Register as a Student or Faculty member");
        card.add(title);
        card.add(Box.createVerticalStrut(5));
        card.add(hint);
        card.add(Box.createVerticalStrut(17));

        card.add(UITheme.muted("FULL NAME"));
        nameField = new JTextField();
        UITheme.styleField(nameField);
        fieldSize(nameField);
        card.add(Box.createVerticalStrut(6));
        card.add(nameField);
        card.add(Box.createVerticalStrut(11));

        card.add(UITheme.muted("EMAIL ADDRESS"));
        emailField = new JTextField();
        UITheme.styleField(emailField);
        fieldSize(emailField);
        card.add(Box.createVerticalStrut(6));
        card.add(emailField);
        card.add(Box.createVerticalStrut(11));

        card.add(UITheme.muted("ACCOUNT TYPE"));
        roleComboBox = new JComboBox<>();
        roleComboBox.addItem(new RoleItem(3, "Student"));
        roleComboBox.addItem(new RoleItem(2, "Faculty"));
        styleCombo(roleComboBox);
        card.add(Box.createVerticalStrut(6));
        card.add(roleComboBox);
        card.add(Box.createVerticalStrut(11));

        card.add(UITheme.muted("DEPARTMENT"));
        departmentComboBox = new JComboBox<>();
        styleCombo(departmentComboBox);
        card.add(Box.createVerticalStrut(6));
        card.add(departmentComboBox);
        card.add(Box.createVerticalStrut(11));

        card.add(UITheme.muted("PASSWORD"));
        passwordField = new JPasswordField();
        UITheme.styleField(passwordField);
        fieldSize(passwordField);
        card.add(Box.createVerticalStrut(6));
        card.add(passwordField);
        card.add(Box.createVerticalStrut(11));

        card.add(UITheme.muted("CONFIRM PASSWORD"));
        confirmPasswordField = new JPasswordField();
        UITheme.styleField(confirmPasswordField);
        fieldSize(confirmPasswordField);
        card.add(Box.createVerticalStrut(6));
        card.add(confirmPasswordField);
        card.add(Box.createVerticalStrut(16));

        JButton register = UITheme.button("Create Account", true);
        register.setAlignmentX(Component.LEFT_ALIGNMENT);
        register.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        card.add(register);
        card.add(Box.createVerticalStrut(8));

        JButton login = UITheme.button("Already have an account?  Sign In", false);
        login.setAlignmentX(Component.LEFT_ALIGNMENT);
        login.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        card.add(login);
        card.add(Box.createVerticalGlue());

        JLabel note = UITheme.muted("Passwords are stored securely using PBKDF2.");
        note.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(note);

        register.addActionListener(e -> register());
        login.addActionListener(e -> goToLogin());
        confirmPasswordField.addActionListener(e -> register());

        right.add(card);
        root.add(left);
        root.add(right);
        setContentPane(root);

        SwingUtilities.invokeLater(() -> nameField.requestFocusInWindow());
    }

    private void fieldSize(JComponent field) {
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
    }

    private void styleCombo(JComboBox<?> combo) {
        combo.setFont(UITheme.font(14, Font.PLAIN));
        combo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        combo.setPreferredSize(new Dimension(300, 40));
    }

    private void loadDepartments() {
        departmentComboBox.removeAllItems();
        List<Department> departments = departmentDAO.getAllDepartments();

        for (Department department : departments) {
            departmentComboBox.addItem(new DepartmentItem(
                    department.getDepartmentId(),
                    department.getDepartmentName(),
                    department.getDepartmentCode()
            ));
        }
    }

    private void register() {
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword());
        String confirm = new String(confirmPasswordField.getPassword());

        if (name.isEmpty()) {
            showError("Please enter your full name.");
            nameField.requestFocusInWindow();
            return;
        }

        if (email.isEmpty() || !email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            showError("Please enter a valid email address.");
            emailField.requestFocusInWindow();
            return;
        }

        if (roleComboBox.getSelectedItem() == null) {
            showError("Please select an account type.");
            return;
        }

        if (departmentComboBox.getSelectedItem() == null) {
            showError("Please select a department.");
            return;
        }

        if (password.length() < 6) {
            showError("Password must contain at least 6 characters.");
            passwordField.requestFocusInWindow();
            return;
        }

        if (!password.equals(confirm)) {
            showError("Passwords do not match.");
            confirmPasswordField.requestFocusInWindow();
            return;
        }

        RoleItem role = (RoleItem) roleComboBox.getSelectedItem();
        DepartmentItem department = (DepartmentItem) departmentComboBox.getSelectedItem();

        boolean registered = userDAO.registerUser(
                name,
                email,
                password,
                role.id,
                department.id
        );

        if (!registered) {
            showError("This email is already registered or registration failed.");
            return;
        }

        JOptionPane.showMessageDialog(
                this,
                "Account created successfully!\n\n" +
                        "Account type: " + role.name + "\n" +
                        "You can now sign in with your email and password.",
                "Registration Successful",
                JOptionPane.INFORMATION_MESSAGE
        );

        dispose();
        new LoginFrame();
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Registration Error", JOptionPane.ERROR_MESSAGE);
    }

    private void goToLogin() {
        dispose();
        new LoginFrame();
    }

    private static class RoleItem {
        private final int id;
        private final String name;

        RoleItem(int id, String name) {
            this.id = id;
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    private static class DepartmentItem {
        private final int id;
        private final String name;
        private final String code;

        DepartmentItem(int id, String name, String code) {
            this.id = id;
            this.name = name;
            this.code = code;
        }

        @Override
        public String toString() {
            if (code == null || code.trim().isEmpty()) return name;
            return code + " - " + name;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(RegistrationFrame::new);
    }
}
