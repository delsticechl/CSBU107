class Circle {
    private double radius;
    private double centerX;
    private double centerY;

    Circle() {
        radius = 0;
        centerX = 0;
        centerY = 0;
    }

    Circle(double r, double x, double y) {
        if (r>0)
            radius = r;
        else
            radius = 0;
        centerX = x;
        centerY = y;
    }

    public double area() {
        return Math.PI * radius * radius;
    }

    public double circumference() {
        return 2 * Math.PI * radius;
    }

    public boolean contains(double x, double y) {
        double distance = Math.sqrt(Math.pow(x - centerX, 2) + Math.pow(y - centerY, 2));
        return distance <= radius;
    }
    
    public void displayInfo() {
        System.out.println("Circle: " + radius + " at (" + centerX + ", " + centerY + ")");
    }


}
public class ex4 {
    public static void main(String[] args) {

        Circle c1 = new Circle(5, 0, 0);

        c1.displayInfo();

        Circle c2 = new Circle(3, 2, 2);

        c2.displayInfo();
    }
}

