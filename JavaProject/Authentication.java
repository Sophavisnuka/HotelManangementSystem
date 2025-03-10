package JavaProject;

public interface Authentication {
    boolean register(String UserName);
    boolean login(String email, String password);
}
