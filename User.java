public class User {
    private String username;
    private String password;
    private String email;
    private String fullName;
    private int age;
    private String phone;
    private String role; // "admin" or "user"

    public User(String username, String password, String email, String fullName, int age, String phone, String role) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.fullName = fullName;
        this.age = age;
        this.phone = phone;
        this.role = role;
    }

    // Getters and Setters
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    @Override
    public String toString() {
        return username + "," + password + "," + email + "," + fullName + "," + age + "," + phone + "," + role;
    }

    public static User fromString(String csvLine) {
        String[] data = csvLine.split(",");
        if (data.length != 7) return null;
        
        return new User(
            data[0], // username
            data[1], // password
            data[2], // email
            data[3], // fullName
            Integer.parseInt(data[4]), // age
            data[5], // phone
            data[6]  // role
        );
    }
}