package JavaProject;


public interface Authentication {
    // void registerUser(Scanner scanner);
    // void loginUser(Scanner scanner);
    boolean registerUser(String username, String phone, String email, String password);
    boolean loginUser();
}
