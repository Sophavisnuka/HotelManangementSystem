package gui;

public interface Authentication {
    boolean registerUser(String username, String phone, String email, String password);
    boolean loginUser(String loginEmail, String loginPassword);
}
