class Rectangle {
    private double height;
    private double width;

    Rectangle() {
        height = 1.0;
        width = 1.0;
    }

    Rectangle(double h, double w) {
       if (h>0)
        height = h;
    else
        height = 1.0;
        
    if (w>0)
        width = w;
    else
        width = 1.0;
    }

    public double area() {
        return height * width;
    }
    
    public double perimeter() {
        return 2 * (height + width);
    }

    public void displayInfo() {
        System.out.println("Height: " + height);
        System.out.println("Width: " + width);
        System.out.println("Area: " + area());
        System.out.println("Perimeter: " + perimeter());
    }
}
    public class ex1 {
    public static void main(String[] args) {
        Rectangle r1 = new Rectangle();
        Rectangle r2 = new Rectangle(5.0, 3.0);
        Rectangle r3 = new Rectangle(-2.0, 4.0);
    
    r1.displayInfo();
    r2.displayInfo();
    r3.displayInfo();
    }
}

