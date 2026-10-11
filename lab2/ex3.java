class Time {
    private int hour;
    private int minute;
    private int second;

    Time() {
        hour = 0;
        minute = 0;
        second = 0;
    }

    Time(int h, int m, int s) {
        if (h>0 && h<24)
            hour = h;
        else
            hour = 0;
        if (m>0 && m<60)
            minute = m;
        else
            minute = 0;
        if (s>0 && s<60)
            second = s;
        else
            second = 0;
    }

    public void addSeconds(int s) {
        second += s;
        if (second >= 60) {
            minute += second / 60;
            second = second % 60;
        }
        if (minute >= 60) {
            hour += minute / 60;
            minute = minute % 60;
        }
        if (hour >= 24) {
            hour = hour % 24;
        }
    }

    public void subtractSeconds(int s) {
        second -= s;
        if (second < 0) {
            minute += second / 60 - 1;
            second = 60 + (second % 60);
        }
        if (minute < 0) {
            hour += minute / 60 - 1;
            minute = 60 + (minute % 60);
        }
        if (hour < 0) {
            hour = 24 + (hour % 24);
        }
    }
    
    public void displayInfo() {
        System.out.println("Time: " + hour + ":" + minute + ":" + second);
    }


}
public class ex3 {
    public static void main(String[] args) {

        Time t1 = new Time(23, 59, 50);

        t1.displayInfo();

        t1.addSeconds(20);

        t1.displayInfo();


        Time t2 = new Time(0, 0, 10);

        t2.displayInfo ();

        t2.subtractSeconds(20);

        t2.displayInfo();
    }
}

