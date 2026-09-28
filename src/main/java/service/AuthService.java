package service;

import dao.UserDAO;
import model.User;
import util.PasswordUtil;

public class AuthService {

    private final UserDAO userDAO;

    public AuthService() {
        userDAO = new UserDAO();
    }

    public User login(String email, String password) {

        User user = userDAO.getUserByEmail(email);

        if (user == null) {
            return null;
        }

        String storedPassword = user.getPassword();
        boolean passwordCorrect = false;

        // New accounts: PBKDF2 hash verification.
        if (storedPassword != null && storedPassword.startsWith("pbkdf2$")) {
            passwordCorrect = PasswordUtil.verify(password, storedPassword);
        } else if (storedPassword != null) {
            // Legacy accounts created before password hashing was enabled.
            // If the old plaintext password is correct, immediately replace it
            // with a PBKDF2 hash so it is not kept in plaintext anymore.
            passwordCorrect = storedPassword.equals(password);

            if (passwordCorrect) {
                String newHash = PasswordUtil.hash(password);
                userDAO.updatePasswordHash(user.getUserId(), newHash);
                user.setPassword(newHash);
            }
        }

        if (!passwordCorrect) {
            return null;
        }

        if (user.getStatus() == null ||
                !user.getStatus().equalsIgnoreCase("ACTIVE")) {
            return null;
        }

        return user;
    }
}
