import dao.UserDAO;
import model.User;

import java.util.List;

public class TestUserDAO {

    public static void main(String[] args) {

        UserDAO userDAO = new UserDAO();

        List<User> users = userDAO.getAllUsers();

        for (User user : users) {
            System.out.println(
                    user.getUserId() + " | " +
                            user.getName() + " | " +
                            user.getEmail() + " | " +
                            user.getRole() + " | " +
                            user.getCreated_at()
            );
        }
    }
}