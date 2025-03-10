package JavaProject;
//inheritance
public class Customer extends User {
    public Customer(int userId, String UserName, String phoneNumber, String email, String password, String role) {
        super(userId, UserName, phoneNumber, email, password, role);
    }
}
