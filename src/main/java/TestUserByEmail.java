import dao.UserDAO;
import model.User;

public class TestUserByEmail {

    public static void main(String[] args) {

        UserDAO userDAO = new UserDAO();

        User user = userDAO.getUserByEmail("admin@college.com");

        if (user != null) {

            System.out.println("User Found!");

            System.out.println("ID: " + user.getUserId());
            System.out.println("Name: " + user.getName());
            System.out.println("Email: " + user.getEmail());
            System.out.println("Role ID: " + user.getRole());
            System.out.println("Status: " + user.getStatus());

        } else {

            System.out.println("User Not Found!");
        }
    }
}