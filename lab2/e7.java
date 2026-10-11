
class UITStudent {
    private String studentId;
    private String fullName;
    private String major;
    private double GPA;

    public UITStudent() {
        this("Unknown", "Unknown");
    }

    public UITStudent(String studentId, String fullName) {
        this(studentId, fullName, "Undeclared", 0.0);
    }

    public UITStudent(String studentId, String fullName,
                      String major, double GPA) {
        this.studentId = studentId;
        this.fullName = fullName;
        this.major = major;
        this.GPA = GPA;
    }

    public void displayInfo() {
        System.out.println("Student ID: " + studentId);
        System.out.println("Full Name: " + fullName);
        System.out.println("Major: " + major);
        System.out.println("GPA: " + GPA);
        System.out.println("Passed: " + isPassed());
        System.out.println("Classification: "
                           + academicClassification());
        System.out.println("-------------------------");
    }

    public boolean isPassed() {
        return GPA >= 2.0;
    }

    public String academicClassification() {
        if (GPA >= 3.6) {
            return "Excellent";
        } else if (GPA >= 3.2) {
            return "Very Good";
        } else if (GPA >= 2.5) {
            return "Good";
        } else if (GPA >= 2.0) {
            return "Average";
        } else {
            return "Poor";
        }
    }
}

public class e7 {
    public static void main(String[] args) {
        UITStudent s1 = new UITStudent();

        UITStudent s2 = new UITStudent(
            "22520001", "Nguyen An"
        );

        UITStudent s3 = new UITStudent(
            "22520002", "Tran Binh",
            "Computer Science", 3.8
        );

        UITStudent s4 = new UITStudent(
            "22520003", "Le Chi",
            "Information Systems", 2.7
        );

        UITStudent s5 = new UITStudent(
            "22520004", "Pham Dung",
            "Data Science", 1.5
        );

        // Display all students
        s1.displayInfo();
        s2.displayInfo();
        s3.displayInfo();
        s4.displayInfo();
        s5.displayInfo();
    }
}
