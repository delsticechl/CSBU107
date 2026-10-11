import java.util.Scanner;
public class ex2 {
 public static void main(String[] args) {
 Scanner scanner = new Scanner(System.in);
 System.out.print("Enter 3 numbers: ");
 int a = scanner.nextInt();
 int b = scanner.nextInt();
 int c = scanner.nextInt();

 int largest = a;
 if (b > largest) {
 largest = b;
 }
 if (c > largest) {
 largest = c;
 }
 System.out.println("The largest number is: " + largest);
 scanner.close();
 }
}
 
 
