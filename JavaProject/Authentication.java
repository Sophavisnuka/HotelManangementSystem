package JavaProject;

public interface Authentication {
    boolean register(int userId, String UserName, String phoneNumber, String email, String password, String role);
    boolean login(String email, String password);
}
