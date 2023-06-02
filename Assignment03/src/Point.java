import java.awt.*;

public class Point {
    // undefined: -1
    // noise: -2
    private int id;
    private double x;
    private double y;
    private int label;

    public Point(int id, double x, double y) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.label = -1;
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public int getLabel() {
        return label;
    }

    public void setLabel(int label) {
        this.label = label;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }
}
