class Dice{
    private int value;

    Dice(){
        value = 1;
    }

    public void roll(){
        value = (int)(Math.random() * 6) + 1;
    }

    public class ex5 {
    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);

        System.out.print("Enter number of rolls: ");
        int n = input.nextInt();

        Dice dice = new Dice();

        int[] count = new int[6];

        for (int i = 0; i < n; i++) {
            int result = dice.roll();
            count[result - 1]++;
        }

        System.out.println("\nResults:");

        for (int i = 0; i < 6; i++) {
            double probability = (double) count[i] / n;

            System.out.println(
                "Face " + (i + 1) +
                ": " + count[i] +
                " times, probability = " + probability
            );
        }
    }
}
}

