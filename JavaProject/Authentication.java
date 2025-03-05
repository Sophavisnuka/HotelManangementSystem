package JavaProject;

public interface Authentication {
    boolean register(String UserName, String phoneNumber, String email, String password);
    boolean login(String email, String password);
}
