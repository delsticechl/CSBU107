import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

abstract class CourseRegistration {
    private String courseCode;
    private String courseName;
    private int credits;
    private int registeredStudents;

    public CourseRegistration(String courseCode, String courseName, int credits, int registeredStudents) {
        if (credits < 1) throw new IllegalArgumentException("Credits must be >= 1");
        if (registeredStudents < 0) throw new IllegalArgumentException("Registered students must be >= 0");
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.credits = credits;
        this.registeredStudents = registeredStudents;
    }

    public abstract double tuitionPerStudent();

    public abstract String getType();

    // Expected total collected = tuition per student x registered students
    public double expectedTotal() {
        return tuitionPerStudent() * registeredStudents;
    }

    // Common display; subclasses extend it where needed
    public void displayInfo() {
        System.out.printf("%-6s | Code: %-7s | Name: %-20s | Credits: %d | Students: %d",
                getType(), courseCode, courseName, credits, registeredStudents);
    }

    protected void displayMoney() {
        System.out.printf(" | Tuition/student: %,.0f VND | Expected total: %,.0f VND%n",
                tuitionPerStudent(), expectedTotal());
    }

    public String getCourseCode() { return courseCode; }
    public String getCourseName() { return courseName; }
    public int getCredits() { return credits; }
    public int getRegisteredStudents() { return registeredStudents; }
}

class TheoryCourse extends CourseRegistration {
    public TheoryCourse(String courseCode, String courseName, int credits, int registeredStudents) {
        super(courseCode, courseName, credits, registeredStudents);
    }

    @Override
    public double tuitionPerStudent() {
        return getCredits() * 450000.0;
    }

    @Override
    public String getType() { return "Theory"; }

    @Override
    public void displayInfo() {
        super.displayInfo();
        displayMoney();
    }
}

class LabCourse extends CourseRegistration {
    private double equipmentFee; // per student

    public LabCourse(String courseCode, String courseName, int credits,
                     int registeredStudents, double equipmentFee) {
        super(courseCode, courseName, credits, registeredStudents);
        if (equipmentFee < 0) throw new IllegalArgumentException("Equipment fee must be >= 0");
        this.equipmentFee = equipmentFee;
    }

    @Override
    public double tuitionPerStudent() {
        return getCredits() * 600000.0 + equipmentFee;
    }

    @Override
    public String getType() { return "Lab"; }

    @Override
    public void displayInfo() {
        super.displayInfo();
        System.out.printf(" | Equipment fee: %,.0f VND", equipmentFee);
        displayMoney();
    }
}

public class CourseManagement {
    static Scanner sc = new Scanner(System.in);

    static int readInt(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            try {
                int v = Integer.parseInt(sc.nextLine().trim());
                if (v >= min && v <= max) return v;
                System.out.println("  Value must be between " + min + " and " + max + ".");
            } catch (NumberFormatException e) {
                System.out.println("  Please enter a valid integer.");
            }
        }
    }

    static double readNonNegative(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                double v = Double.parseDouble(sc.nextLine().trim());
                if (v >= 0) return v;
                System.out.println("  Value must be >= 0.");
            } catch (NumberFormatException e) {
                System.out.println("  Please enter a valid number.");
            }
        }
    }

    static String readNonEmpty(String prompt) {
        while (true) {
            System.out.print(prompt);
            String s = sc.nextLine().trim();
            if (!s.isEmpty()) return s;
            System.out.println("  This field cannot be empty.");
        }
    }

    static CourseRegistration findByCode(List<CourseRegistration> list, String code) {
        for (CourseRegistration c : list)
            if (c.getCourseCode().equalsIgnoreCase(code)) return c;
        return null;
    }

    static String readUniqueCode(List<CourseRegistration> list) {
        while (true) {
            String code = readNonEmpty("  Course code: ");
            if (findByCode(list, code) == null) return code;
            System.out.println("  Duplicate course code! Please enter a different one.");
        }
    }

    public static void main(String[] args) {
        List<CourseRegistration> courses = new ArrayList<>();

        int n = readInt("Number of courses (N): ", 1, Integer.MAX_VALUE);

        for (int i = 1; i <= n; i++) {
            System.out.println("\nCourse " + i + "/" + n);
            int type = readInt("  Type (1 = Theory, 2 = Lab): ", 1, 2);
            String code = readUniqueCode(courses);
            String name = readNonEmpty("  Course name: ");
            int credits = readInt("  Credits (>= 1): ", 1, Integer.MAX_VALUE);
            int students = readInt("  Registered students (>= 0): ", 0, Integer.MAX_VALUE);

            if (type == 1) {
                courses.add(new TheoryCourse(code, name, credits, students));
            } else {
                double fee = readNonNegative("  Equipment fee per student in VND (>= 0): ");
                courses.add(new LabCourse(code, name, credits, students, fee));
            }
        }

        // Display all courses (polymorphism through the CourseRegistration reference)
        System.out.println("\n===== ALL COURSES (amounts in VND) =====");
        for (CourseRegistration c : courses) c.displayInfo();

        // Search by course code
        System.out.println("\n===== SEARCH BY COURSE CODE =====");
        String searchCode = readNonEmpty("Enter course code: ");
        CourseRegistration found = findByCode(courses, searchCode);
        if (found != null) found.displayInfo();
        else System.out.println("Course '" + searchCode + "' not found.");

        // Statistics
        int theoryCount = 0, labCount = 0, totalStudents = 0;
        double totalCollection = 0;
        CourseRegistration top = courses.get(0);

        for (CourseRegistration c : courses) {
            if (c instanceof TheoryCourse) theoryCount++;
            else if (c instanceof LabCourse) labCount++;

            totalStudents += c.getRegisteredStudents();
            totalCollection += c.expectedTotal();

            if (c.expectedTotal() > top.expectedTotal()) top = c;
        }

        System.out.println("\n===== STATISTICS =====");
        System.out.println("Theory courses: " + theoryCount);
        System.out.println("Lab courses   : " + labCount);
        System.out.printf("Total expected collection: %,.0f VND%n", totalCollection);

        // Weighted average tuition per student = total collection / total students
        if (totalStudents == 0) {
            System.out.println("Average tuition per student (weighted): N/A");
        } else {
            System.out.printf("Average tuition per student (weighted): %,.2f VND%n",
                    totalCollection / totalStudents);
        }

        System.out.println("Course with the largest expected collection:");
        top.displayInfo();
    }
}