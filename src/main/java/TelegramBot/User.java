package TelegramBot;


public class User {
    private String name ;
    private String secondName ;
    private String phone;

    public User(String name, String secondName, String phone) {
        this.name = name;
        this.secondName = secondName;
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
