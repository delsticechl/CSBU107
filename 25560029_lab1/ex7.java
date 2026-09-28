import java.util.Scanner;

public class ex7 {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        System.out.print("Enter n: ");
        int n = input.nextInt();

        int[] a = new int[n];

        for (int i = 0; i < n; i++) {
            a[i] = input.nextInt();
        }

        System.out.print("Enter m: ");
        int m = input.nextInt();

        // 4. Read second array
        int[] b = new int[m];

        for (int i = 0; i < m; i++) {
            b[i] = input.nextInt();
        }

        int[] merged = new int[n + m];

        int i = 0;
        int j = 0;
        int k = 0;

        while (i < n && j < m) {
            if (a[i] <= b[j]) {
                merged[k] = a[i];
                i++;
            } else {
                merged[k] = b[j];
                j++;
            }
            k++;
        }

        while (i < n) {
            merged[k] = a[i];
            i++;
            k++;
        }

        while (j < m) {
            merged[k] = b[j];
            j++;
            k++;
        }

        System.out.print("Merged array: ");

        for (int x : merged) {
            System.out.print(x + " ");
        }
    }
}