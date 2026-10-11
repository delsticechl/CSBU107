import java.util.Scanner;
public class ex4 {
 public static void main(String[] args) {
 Scanner scanner = new Scanner(System.in);
 System.out.print("Enter a string: ");
 String string = scanner.nextLine();

 String ReverseString = "";
 for (int i = string.length() - 1; i >= 0; i--) {
 ReverseString += string.charAt(i);
 }

 System.out.println("The reversed string: " + ReverseString);
 scanner.close();
 }
}
 
 