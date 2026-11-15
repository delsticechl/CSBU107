import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

abstract class Delivery {
    // Static field: belongs to the CLASS, not to any single object.
    // All Delivery objects (Standard and Express) share this ONE counter.
    private static int deliveryCount = 0;

    // Encapsulated instance fields: each object has its own copy
    private String orderId;
    private String customerName;
    private double distance; // km
    private double weight;   // kg

    public Delivery(String orderId, String customerName, double distance, double weight) {
        // Validate first, so the counter only counts SUCCESSFULLY created objects
        if (distance <= 0) throw new IllegalArgumentException("Distance must be > 0 km");
        if (weight <= 0) throw new IllegalArgumentException("Weight must be > 0 kg");
        if (orderId == null || orderId.trim().isEmpty())
            throw new IllegalArgumentException("Order ID cannot be empty");

        this.orderId = orderId;
        this.customerName = customerName;
        this.distance = distance;
        this.weight = weight;
        deliveryCount++; // reached only if the object was created successfully
    }

    public abstract double calculateShippingFee();

    public abstract String getType();

    public void displayInfo() {
        System.out.printf("%-9s | Order: %-6s | Customer: %-15s | Distance: %.1f km | Weight: %.1f kg",
                getType(), orderId, customerName, distance, weight);
    }

    // Getters only (no setters) keep the data valid after creation
    public String getOrderId() { return orderId; }
    public String getCustomerName() { return customerName; }
    public double getDistance() { return distance; }
    public double getWeight() { return weight; }

    public static int getDeliveryCount() { return deliveryCount; }
}

class StandardDelivery extends Delivery {
    public StandardDelivery(String orderId, String customerName, double distance, double weight) {
        super(orderId, customerName, distance, weight);
    }

    @Override
    public double calculateShippingFee() {
        return 15000 + 3000 * getDistance() + 2000 * getWeight();
    }

    @Override
    public String getType() { return "Standard"; }

    @Override
    public void displayInfo() {
        super.displayInfo();
        System.out.printf(" | Fee: %,.0f VND%n", calculateShippingFee());
    }
}

class ExpressDelivery extends Delivery {
    public ExpressDelivery(String orderId, String customerName, double distance, double weight) {
        super(orderId, customerName, distance, weight);
    }

    @Override
    public double calculateShippingFee() {
        return 30000 + 5000 * getDistance() + 4000 * getWeight();
    }

    @Override
    public String getType() { return "Express"; }

    @Override
    public void displayInfo() {
        super.displayInfo();
        System.out.printf(" | Fee: %,.0f VND%n", calculateShippingFee());
    }
}

public class DeliveryManagement {
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

    // Reads a number strictly greater than 0
    static double readPositive(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                double v = Double.parseDouble(sc.nextLine().trim());
                if (v > 0) return v;
                System.out.println("  Value must be > 0.");
            } catch (NumberFormatException e) {
                System.out.println("  Please enter a valid number.");
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

    static Delivery findById(List<Delivery> list, String id) {
        for (Delivery d : list)
            if (d.getOrderId().equalsIgnoreCase(id)) return d;
        return null;
    }

    static String readUniqueId(List<Delivery> list) {
        while (true) {
            String id = readNonEmpty("  Order ID: ");
            if (findById(list, id) == null) return id;
            System.out.println("  Duplicate order ID! Please enter a different one.");
        }
    }

    public static void main(String[] args) {
        List<Delivery> orders = new ArrayList<>();

        int n = readInt("Number of orders (N): ", 1, Integer.MAX_VALUE);

        for (int i = 1; i <= n; i++) {
            System.out.println("\nOrder " + i + "/" + n);
            int type = readInt("  Type (1 = Standard, 2 = Express): ", 1, 2);
            String id = readUniqueId(orders);
            String name = readNonEmpty("  Customer name: ");
            double distance = readPositive("  Distance in km (> 0): ");
            double weight = readPositive("  Weight in kg (> 0): ");

            if (type == 1) orders.add(new StandardDelivery(id, name, distance, weight));
            else orders.add(new ExpressDelivery(id, name, distance, weight));
        }

        // Display all shipping fees (polymorphism through the Delivery reference)
        System.out.println("\n===== ALL ORDERS (fees in VND) =====");
        for (Delivery d : orders) d.displayInfo();

        // Search by order ID
        System.out.println("\n===== SEARCH BY ORDER ID =====");
        String searchId = readNonEmpty("Enter order ID: ");
        Delivery found = findById(orders, searchId);
        if (found != null) found.displayInfo();
        else System.out.println("Order '" + searchId + "' not found.");

        // Count by type and total revenue
        int standardCount = 0, expressCount = 0;
        double total = 0;
        for (Delivery d : orders) {
            if (d instanceof StandardDelivery) standardCount++;
            else if (d instanceof ExpressDelivery) expressCount++;
            total += d.calculateShippingFee();
        }

        // Orders above a fee threshold
        System.out.println("\n===== ORDERS ABOVE A FEE THRESHOLD =====");
        double threshold = readNonNegative("Enter fee threshold (VND): ");
        boolean any = false;
        for (Delivery d : orders) {
            if (d.calculateShippingFee() > threshold) {
                d.displayInfo();
                any = true;
            }
        }
        if (!any) System.out.printf("No order has a fee above %,.0f VND.%n", threshold);

        // Statistics
        System.out.println("\n===== STATISTICS =====");
        System.out.println("Standard orders: " + standardCount);
        System.out.println("Express orders : " + expressCount);
        System.out.printf("Total shipping revenue: %,.0f VND%n", total);

        // Static counter
        System.out.println("\n===== STATIC COUNTER =====");
        System.out.println("Delivery objects created successfully: " + Delivery.getDeliveryCount());
        System.out.println("Note: 'static' belongs to the class, not to an object. There is only one");
        System.out.println("copy of deliveryCount, shared by every StandardDelivery and ExpressDelivery,");
        System.out.println("so it can track how many objects the class has created in total.");
        System.out.println("It is accessed through the class name: Delivery.getDeliveryCount().");
    }
}