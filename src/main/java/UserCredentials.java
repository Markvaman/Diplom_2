public class UserCredentials {
    private String email;
    private String login;
    private String password;

    public static UserCredentials from(BurgersUser user) {
        return new UserCredentials(user.getEmail(), user.getLogin(), user.getPassword());
    }

    public UserCredentials(String email, String login, String password) {
        this.email = email;
        this.login = login;
        this.password = password;
    }

    public UserCredentials(String email, String password) {
        this.email = email;
        this.password = password;
    }
    public static UserCredentials fromTwo(BurgersUser user) {
        return new UserCredentials(user.getEmail(), user.getPassword());
    }


}
