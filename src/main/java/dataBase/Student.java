package dataBase;

public class Student {
    private String Id;
    private String Name;
    private String Gender;
    private String Email;
    private int Mobile;
    
    public Student(String id, String name, String gender, String email, int mobile) {
        Id = id;
        Name = name;
        Gender = gender;
        Email = email;
        Mobile = mobile;
    }

    public static Student getInstance(String id, String name, String gender, String email, int mobile) {
        return new Student(id, name, gender, email, mobile);
    }

    public String getId() {
        return Id;
    }

    public String getName() {
        return Name;
    }

    public String getGender() {
        return Gender;
    }

    public String getEmail() {
        return Email;
    }

    public int getMobile() {
        return Mobile;
    }
}
