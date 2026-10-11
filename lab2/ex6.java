
class Calculator {

    int add(int a, int b) {
        return a + b;
    }

    double add(double a, double b) {
        return a + b;
    }

    int add(int a, int b, int c) {
        return a + b + c;
    }

    int max(int a, int b) {
        return (a > b) ? a : b;
    }

    double max(double a, double b) {
        return (a > b) ? a : b;
    }
}

public class ex6 {
    public static void main(String[] args) {

        Calculator cal = new Calculator();

        System.out.println(cal.add(3, 5));
        System.out.println(cal.add(2.5, 4.5));
        System.out.println(cal.add(1, 2, 3));

        System.out.println(cal.max(10, 20));
        System.out.println(cal.max(3.5, 2.5));
    }
}
