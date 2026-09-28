package service;

import dao.UserDAO;
import model.User;

public class UserService {

    private UserDAO userDAO;

    public UserService() {
        userDAO = new UserDAO();
    }

    public String getUserNameById(int userId) {

        User user = userDAO.getUserById(userId);

        if (user != null) {
            return user.getName();
        }

        return "Unknown";
    }
}