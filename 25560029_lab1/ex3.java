import java.util.Scanner;
public class ex3 {
 public static void main(String[] args) {
 Scanner scanner = new Scanner(System.in);
 System.out.print("Enter a number: ");
 int n = scanner.nextInt();

 int sum = 0;
 while (n > 0) {
 sum += n % 10;
 n /= 10;
 }
 
 System.out.println("The sum of digits: " + sum);
 scanner.close();
 }
}
 
 