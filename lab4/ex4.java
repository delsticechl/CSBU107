import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

abstract class Ticket {
    // Encapsulated fields
    private String ticketId;
    private String movieTitle;
    private int quantity;

    public Ticket(String ticketId, String movieTitle, int quantity) {
        if (quantity < 1) throw new IllegalArgumentException("Quantity must be >= 1");
        this.ticketId = ticketId;
        this.movieTitle = movieTitle;
        this.quantity = quantity;
    }

    // Convenience constructor: one ticket (constructor chaining with this(...))
    public Ticket(String ticketId, String movieTitle) {
        this(ticketId, movieTitle, 1);
    }

    public abstract double getUnitPrice();

    public abstract double getDiscountRate();

    public abstract String getType();

    public abstract double calculateTotalPrice();

    public double getDiscountAmount() {
        return getUnitPrice() * quantity * getDiscountRate();
    }

    public void displayInfo() {
        System.out.printf("%-8s | ID: %-6s | Movie: %-16s | Qty: %d | Unit: %,.0f VND | Discount: %.0f%% (-%,.0f VND)",
                getType(), ticketId, movieTitle, quantity, getUnitPrice(),
                getDiscountRate() * 100, getDiscountAmount());
        System.out.printf(" | Final: %,.0f VND%n", calculateTotalPrice());
    }

    public String getTicketId() { return ticketId; }
    public String getMovieTitle() { return movieTitle; }
    public int getQuantity() { return quantity; }
}

class RegularTicket extends Ticket {
    private static final double UNIT_PRICE = 80000;

    public RegularTicket(String ticketId, String movieTitle, int quantity) {
        super(ticketId, movieTitle, quantity);
    }

    public RegularTicket(String ticketId, String movieTitle) {
        super(ticketId, movieTitle); // chains to Ticket(id, title) -> Ticket(id, title, 1)
    }

    @Override
    public double getUnitPrice() { return UNIT_PRICE; }

    @Override
    public double getDiscountRate() { return getQuantity() >= 5 ? 0.10 : 0.0; }

    @Override
    public String getType() { return "Regular"; }

    @Override
    public double calculateTotalPrice() {
        return getUnitPrice() * getQuantity() - getDiscountAmount();
    }
}

class VIPTicket extends Ticket {
    private static final double UNIT_PRICE = 150000;

    public VIPTicket(String ticketId, String movieTitle, int quantity) {
        super(ticketId, movieTitle, quantity);
    }

    public VIPTicket(String ticketId, String movieTitle) {
        super(ticketId, movieTitle);
    }

    @Override
    public double getUnitPrice() { return UNIT_PRICE; }

    @Override
    public double getDiscountRate() { return getQuantity() >= 3 ? 0.15 : 0.0; }

    @Override
    public String getType() { return "VIP"; }

    @Override
    public double calculateTotalPrice() {
        return getUnitPrice() * getQuantity() - getDiscountAmount();
    }
}

public class CinemaManagement {
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

    static String readNonEmpty(String prompt) {
        while (true) {
            System.out.print(prompt);
            String s = sc.nextLine().trim();
            if (!s.isEmpty()) return s;
            System.out.println("  This field cannot be empty.");
        }
    }

    static boolean idExists(List<Ticket> list, String id) {
        for (Ticket t : list)
            if (t.getTicketId().equalsIgnoreCase(id)) return true;
        return false;
    }

    static String readUniqueId(List<Ticket> list) {
        while (true) {
            String id = readNonEmpty("  Ticket ID: ");
            if (!idExists(list, id)) return id;
            System.out.println("  Duplicate ticket ID! Please enter a different one.");
        }
    }

    // Sort a COPY from highest to lowest final total (selection sort, no frameworks)
    static List<Ticket> sortByTotalDescending(List<Ticket> list) {
        List<Ticket> sorted = new ArrayList<>(list);
        for (int i = 0; i < sorted.size() - 1; i++) {
            int maxIdx = i;
            for (int j = i + 1; j < sorted.size(); j++) {
                if (sorted.get(j).calculateTotalPrice() > sorted.get(maxIdx).calculateTotalPrice())
                    maxIdx = j;
            }
            Ticket tmp = sorted.get(i);
            sorted.set(i, sorted.get(maxIdx));
            sorted.set(maxIdx, tmp);
        }
        return sorted;
    }

    public static void main(String[] args) {
        List<Ticket> tickets = new ArrayList<>();

        int n = readInt("Number of purchases (N): ", 1, Integer.MAX_VALUE);

        for (int i = 1; i <= n; i++) {
            System.out.println("\nPurchase " + i + "/" + n);
            int type = readInt("  Type (1 = Regular, 2 = VIP): ", 1, 2);
            String id = readUniqueId(tickets);
            String movie = readNonEmpty("  Movie title: ");
            int qty = readInt("  Quantity (>= 1): ", 1, Integer.MAX_VALUE);

            if (type == 1) tickets.add(new RegularTicket(id, movie, qty));
            else tickets.add(new VIPTicket(id, movie, qty));
        }

        // Display all purchases (polymorphism through the Ticket reference)
        System.out.println("\n===== ALL PURCHASES =====");
        for (Ticket t : tickets) t.displayInfo();

        // Search by movie title (case-insensitive, exact title)
        System.out.println("\n===== SEARCH BY MOVIE TITLE =====");
        String searchTitle = readNonEmpty("Enter movie title: ");
        boolean found = false;
        for (Ticket t : tickets) {
            if (t.getMovieTitle().equalsIgnoreCase(searchTitle)) {
                t.displayInfo();
                found = true;
            }
        }
        if (!found) System.out.println("No purchases found for '" + searchTitle + "'.");

        // Total revenue
        double total = 0;
        for (Ticket t : tickets) total += t.calculateTotalPrice();

        // Movie with the highest aggregated revenue
        String bestMovie = null;
        double bestRevenue = -1;
        for (int i = 0; i < tickets.size(); i++) {
            String title = tickets.get(i).getMovieTitle();

            // Skip titles already processed at an earlier index
            boolean seen = false;
            for (int j = 0; j < i; j++) {
                if (tickets.get(j).getMovieTitle().equalsIgnoreCase(title)) {
                    seen = true;
                    break;
                }
            }
            if (seen) continue;

            // Aggregate every purchase of this movie
            double sum = 0;
            for (Ticket t : tickets)
                if (t.getMovieTitle().equalsIgnoreCase(title)) sum += t.calculateTotalPrice();

            if (sum > bestRevenue) {
                bestRevenue = sum;
                bestMovie = title;
            }
        }

        System.out.println("\n===== STATISTICS =====");
        System.out.printf("Total revenue: %,.0f VND%n", total);
        System.out.printf("Top movie by revenue: %s (%,.0f VND)%n", bestMovie, bestRevenue);

        // Sorted display
        System.out.println("\n===== PURCHASES SORTED BY FINAL TOTAL (HIGH TO LOW) =====");
        for (Ticket t : sortByTotalDescending(tickets)) t.displayInfo();
    }
}