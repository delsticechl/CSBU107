class Fraction {
    private int num;
    private int denom;

    Fraction() {
        num = 0;
        denom = 1;
    }
    
    private int gcd(int a, int b) {
    a = Math.abs(a);
    b = Math.abs(b);

    while (b != 0) {
        int temp = b;
        b = a % b;
        a = temp;
    }

    return a;
}

    Fraction(int n, int d) {
        num = n;
        if (d != 0) {
            denom = d;
        } else {
            denom = 1;
        }
    }

    public Fraction(Fraction other) {
        this.num = other.num;
        this.denom = other.denom;

    }


    public double simplify() {
        int gcd = gcd(num, denom);
        num /= gcd;
        denom /= gcd;
        return (double) num / denom;
    }

    public double add(Fraction other) {
        int newNum = this.num * other.denom + other.num * this.denom;
        int newDenom = this.denom * other.denom;
        return (double) newNum / newDenom;
    }

    public double subtract(Fraction other) {
        int newNum = this.num * other.denom - other.num * this.denom;
        int newDenom = this.denom * other.denom;
        return (double) newNum / newDenom;
    }

    public double multiply(Fraction other) {
        int newNum = this.num * other.num;
        int newDenom = this.denom * other.denom;
        return (double) newNum / newDenom;
    }

    public double divide(Fraction other) {
        int newNum = this.num * other.denom;
        int newDenom = this.denom * other.num;
        return (double) newNum / newDenom;
    }
    public void displayInfo() {
        System.out.println("Fraction: " + num + "/" + denom);
        System.out.println("Numerator: " + num);
        System.out.println("Denominator: " + denom);
        System.out.println("Simplified: " + simplify());
    }
}
    public class ex2 {
    public static void main(String[] args) {
        Fraction f1 = new Fraction();
        Fraction f2 = new Fraction(1, 2);
        Fraction f3 = new Fraction(3, 4);

        f1.displayInfo();
        f2.displayInfo();
        f3.displayInfo();
    }
}

