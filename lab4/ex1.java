import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

abstract class LibraryItem {
    protected String id, title, author;
    protected int days;

    public LibraryItem(String id, String title, String author, int days) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.days = days;
    }

    public abstract long calculateFee();

    public void displayInfo() {
        System.out.printf("ID: %s | Title: %s | Author: %s | Days: %d",
                id, title, author, days);
    }

    public String getId() { return id; }
}

class PrintedBook extends LibraryItem {
    private int pages;

    public PrintedBook(String id, String title, String author, int days, int pages) {
        super(id, title, author, days);
        this.pages = pages;
    }

    @Override
    public long calculateFee() {
        return 5000L * days + 10L * pages;
    }

    @Override
    public void displayInfo() {
        System.out.print("[Book]     ");
        super.displayInfo();
        System.out.printf(" | Pages: %d | Fee: %,d VND%n", pages, calculateFee());
    }
}

class Magazine extends LibraryItem {
    private int issueNumber;

    public Magazine(String id, String title, String author, int days, int issueNumber) {
        super(id, title, author, days);
        this.issueNumber = issueNumber;
    }

    @Override
    public long calculateFee() {
        return 3000L * days + 2000L;
    }

    @Override
    public void displayInfo() {
        System.out.print("[Magazine] ");
        super.displayInfo();
        System.out.printf(" | Issue: %d | Fee: %,d VND%n", issueNumber, calculateFee());
    }
}

public class LibraryManagement {
    static Scanner sc = new Scanner(System.in);

    // Read an integer >= min, keep asking until valid
    static int readInt(String prompt, int min) {
        while (true) {
            System.out.print(prompt);
            String line = sc.nextLine().trim();
            try {
                int value = Integer.parseInt(line);
                if (value >= min) return value;
                System.out.println("  Value must be >= " + min + ". Try again.");
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

    static LibraryItem findById(List<LibraryItem> list, String id) {
        for (LibraryItem item : list)
            if (item.getId().equalsIgnoreCase(id)) return item;
        return null;
    }

    static String readUniqueId(List<LibraryItem> list) {
        while (true) {
            String id = readNonEmpty("  ID: ");
            if (findById(list, id) == null) return id;
            System.out.println("  Duplicate ID! Please enter a different one.");
        }
    }

    public static void main(String[] args) {
        List<LibraryItem> items = new ArrayList<>();

        int n = readInt("Number of items (N): ", 1);

        for (int i = 1; i <= n; i++) {
            System.out.println("\nItem " + i + "/" + n);
            int type;
            while (true) {
                type = readInt("  Type (1 = PrintedBook, 2 = Magazine): ", 1);
                if (type <= 2) break;
                System.out.println("  Type must be 1 or 2.");
            }

            String id = readUniqueId(items);
            String title = readNonEmpty("  Title: ");
            String author = readNonEmpty("  Author: ");
            int days = readInt("  Days borrowed (>= 1): ", 1);

            if (type == 1) {
                int pages = readInt("  Number of pages (>= 1): ", 1);
                items.add(new PrintedBook(id, title, author, days, pages));
            } else {
                int issue = readInt("  Issue number (>= 1): ", 1);
                items.add(new Magazine(id, title, author, days, issue));
            }
        }

        // Display all items (polymorphism: LibraryItem reference)
        System.out.println("\n===== ALL ITEMS =====");
        for (LibraryItem item : items) {
            item.displayInfo();
        }

        // Search by ID
        System.out.println("\n===== SEARCH BY ID =====");
        System.out.print("Enter ID to search: ");
        String searchId = sc.nextLine().trim();
        LibraryItem found = findById(items, searchId);
        if (found != null) {
            found.displayInfo();
        } else {
            System.out.println("Item with ID '" + searchId + "' not found.");
        }

        // Statistics
        int bookCount = 0, magCount = 0;
        long total = 0;
        LibraryItem max = items.get(0);

        for (LibraryItem item : items) {
            if (item instanceof PrintedBook) bookCount++;
            else if (item instanceof Magazine) magCount++;

            total += item.calculateFee();

            if (item.calculateFee() > max.calculateFee()) max = item;
        }

        System.out.println("\n===== STATISTICS =====");
        System.out.println("PrintedBook count: " + bookCount);
        System.out.println("Magazine count   : " + magCount);
        System.out.printf("Total revenue    : %,d VND%n", total);
        System.out.println("Item with the highest fee:");
        max.displayInfo();
    }
}
