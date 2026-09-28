import model.User;
import service.AuthService;

public class TestAuthService {

    public static void main(String[] args) {

        AuthService authService = new AuthService();

        User user = authService.login(
                "admin@college.com",
                "admin123"
        );

        if (user != null) {

            System.out.println("Login Successful!");
            System.out.println("Welcome, " + user.getName());
            System.out.println("Role ID: " + user.getRole());

        } else {

            System.out.println("Invalid email/password or account inactive.");
        }
    }
}