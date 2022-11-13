package door.one.marketplace.Model;

public class Users {
    private  String username,phoneuser,password,image;
    public Users(){

    }

    public Users(String username, String phoneuser, String password,String image) {
        this.username = username;
        this.phoneuser = phoneuser;
        this.password = password;
        this.image=image;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhoneuser() {
        return phoneuser;
    }

    public void setPhoneuser(String phoneuser) {
        this.phoneuser = phoneuser;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
