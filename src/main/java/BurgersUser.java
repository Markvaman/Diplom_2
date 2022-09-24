public class BurgersUser {
    private String email;
    private String name;
    private String password;


    public BurgersUser(String email, String password, String name) {
        this.email = email;
        this.name = name;
        this.password = password;
    }

    public BurgersUser() {
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setLogin(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public BurgersUser (String email, String password){
        this.email = email;
        this.password = password;
    }

}
