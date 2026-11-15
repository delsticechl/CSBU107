import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

abstract class Employee {
    protected String id, fullName, department;

    public Employee(String id, String fullName, String department) {
        this.id = id;
        this.fullName = fullName;
        this.department = department;
    }

    public abstract double calculateSalary();

    public abstract String getType();

    public void displayInfo() {
        System.out.printf("%-12s | ID: %-6s | Name: %-18s | Dept: %-10s",
                getType(), id, fullName, department);
    }

    public String getId() { return id; }
    public String getFullName() { return fullName; }
    public String getDepartment() { return department; }
}

class FullTimeEmployee extends Employee {
    private double baseSalary, allowance;
    private int workdays;

    public FullTimeEmployee(String id, String fullName, String department,
                            double baseSalary, double allowance, int workdays) {
        super(id, fullName, department);
        this.baseSalary = baseSalary;
        this.allowance = allowance;
        this.workdays = workdays;
    }

    @Override
    public double calculateSalary() {
        return baseSalary * Math.min(workdays, 26) / 26.0 + allowance;
    }

    @Override
    public String getType() { return "Full-time"; }

    @Override
    public void displayInfo() {
        super.displayInfo();
        System.out.printf(" | Workdays: %d | Salary: %,.0f VND%n", workdays, calculateSalary());
    }
}

class PartTimeEmployee extends Employee {
    private double hoursWorked, hourlyRate;

    public PartTimeEmployee(String id, String fullName, String department,
                            double hoursWorked, double hourlyRate) {
        super(id, fullName, department);
        this.hoursWorked = hoursWorked;
        this.hourlyRate = hourlyRate;
    }

    @Override
    public double calculateSalary() {
        return hoursWorked * hourlyRate;
    }

    @Override
    public String getType() { return "Part-time"; }

    @Override
    public void displayInfo() {
        super.displayInfo();
        System.out.printf(" | Hours: %.1f | Salary: %,.0f VND%n", hoursWorked, calculateSalary());
    }
}

public class PayrollManagement {
    static Scanner sc = new Scanner(System.in);

    // Read an integer within [min, max]
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

    // Read a number >= min
    static double readDouble(String prompt, double min) {
        while (true) {
            System.out.print(prompt);
            try {
                double v = Double.parseDouble(sc.nextLine().trim());
                if (v >= min) return v;
                System.out.println("  Value must be >= " + (long) min + ".");
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

    // ---- Overloaded search methods ----
    static Employee findEmployee(List<Employee> list, String id) {
        for (Employee e : list)
            if (e.getId().equalsIgnoreCase(id)) return e;
        return null;
    }

    static Employee findEmployee(List<Employee> list, String name, String department) {
        for (Employee e : list)
            if (e.getFullName().equalsIgnoreCase(name)
                    && e.getDepartment().equalsIgnoreCase(department))
                return e;
        return null;
    }

    static List<Employee> findByDepartment(List<Employee> list, String department) {
        List<Employee> result = new ArrayList<>();
        for (Employee e : list)
            if (e.getDepartment().equalsIgnoreCase(department)) result.add(e);
        return result;
    }

    static String readUniqueId(List<Employee> list) {
        while (true) {
            String id = readNonEmpty("  ID: ");
            if (findEmployee(list, id) == null) return id;
            System.out.println("  Duplicate ID! Please enter a different one.");
        }
    }

    public static void main(String[] args) {
        List<Employee> staff = new ArrayList<>();

        int n = readInt("Number of employees (N): ", 1, Integer.MAX_VALUE);

        for (int i = 1; i <= n; i++) {
            System.out.println("\nEmployee " + i + "/" + n);
            int type = readInt("  Type (1 = Full-time, 2 = Part-time): ", 1, 2);

            String id = readUniqueId(staff);
            String name = readNonEmpty("  Full name: ");
            String dept = readNonEmpty("  Department: ");

            if (type == 1) {
                double base = readDouble("  Base salary (>= 0): ", 0);
                double allowance = readDouble("  Allowance (>= 0): ", 0);
                int workdays = readInt("  Workdays (0-31): ", 0, 31);
                staff.add(new FullTimeEmployee(id, name, dept, base, allowance, workdays));
            } else {
                double hours = readDouble("  Hours worked (>= 0): ", 0);
                double rate = readDouble("  Hourly rate (>= 0): ", 0);
                staff.add(new PartTimeEmployee(id, name, dept, hours, rate));
            }
        }

        // Payroll (polymorphism through the Employee reference)
        System.out.println("\n===== PAYROLL =====");
        for (Employee e : staff) e.displayInfo();

        // Find by department
        System.out.println("\n===== FIND BY DEPARTMENT =====");
        String dept = readNonEmpty("Enter department: ");
        List<Employee> inDept = findByDepartment(staff, dept);
        if (inDept.isEmpty()) {
            System.out.println("No employees in department '" + dept + "'.");
        } else {
            for (Employee e : inDept) e.displayInfo();
        }

        // Overloaded search 1: by ID
        System.out.println("\n===== FIND BY ID =====");
        String searchId = readNonEmpty("Enter ID: ");
        Employee byId = findEmployee(staff, searchId);
        if (byId != null) byId.displayInfo();
        else System.out.println("Employee with ID '" + searchId + "' not found.");

        // Overloaded search 2: by name + department
        System.out.println("\n===== FIND BY NAME + DEPARTMENT =====");
        String sName = readNonEmpty("Enter full name: ");
        String sDept = readNonEmpty("Enter department: ");
        Employee byNameDept = findEmployee(staff, sName, sDept);
        if (byNameDept != null) byNameDept.displayInfo();
        else System.out.println("No match for '" + sName + "' in '" + sDept + "'.");

        // Total payroll and highest salary
        double total = 0;
        Employee top = staff.get(0);
        for (Employee e : staff) {
            total += e.calculateSalary();
            if (e.calculateSalary() > top.calculateSalary()) top = e;
        }

        System.out.println("\n===== STATISTICS =====");
        System.out.printf("Total payroll: %,.0f VND%n", total);
        System.out.println("Highest-paid employee:");
        top.displayInfo();
    }
}