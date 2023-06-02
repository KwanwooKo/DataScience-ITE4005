import java.io.BufferedReader;
import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

public class DBScan {
    private int n;
    private double eps;
    private int minPts;
    private ArrayList<Point> points;


    // undefined: -1
    // noise: -2;
    final int undefined = -1;
    final int noise = -2;

    public DBScan(int n, double eps, int minPts, ArrayList<Point> points) {
        this.n = n;
        this.eps = eps;
        this.minPts = minPts;
        // 이거 굳이 복사 안하고 그냥 받아도 될듯
        this.points = points;
    }

    public double getDist(Point p, Point neighbor) {
        double x = p.getX();
        double y = p.getY();
        double nx = neighbor.getX();
        double ny = neighbor.getY();

        return Math.sqrt(   Math.pow((x - nx), 2)    +     Math.pow((y - ny), 2)      );
    }

    public ArrayList<Point> findNeighbors(Point p) {
        ArrayList<Point> neighbors = new ArrayList<>();

        for (Point neighbor : points) {
            double dist = getDist(p, neighbor);
            if (dist <= eps)
                neighbors.add(neighbor);
        }

        return neighbors;
    }

    // p를 제외한 neighbor들
    public ArrayList<Point> makeSeed(ArrayList<Point> neighbors, int id) {
        ArrayList<Point> seed = new ArrayList<>();
        for (Point p : neighbors) {
            if (p.getId() == id) continue;
            seed.add(p);
        }
        return seed;
    }

    public void unionSeedAndNeighbor(ArrayList<Point> seed, ArrayList<Point> neighbors) {
        for (Point neighbor : neighbors) {
            boolean find = false;
            for (Point s : seed) {
                if (s.getId() == neighbor.getId()) {
                    find = true;
                    break;
                }
            }
            if (find) continue;
            seed.add(neighbor);
        }
    }

    public void cluster() {
        int label = -1;

        for (Point p : points) {
            if (p.getLabel() != undefined) continue;
            ArrayList<Point> neighbors = findNeighbors(p);
            if (neighbors.size() < minPts) {
                p.setLabel(noise);
                continue;
            }

            label++;
            p.setLabel(label);
            ArrayList<Point> seed = makeSeed(neighbors, p.getId());   // p를 제외한 neighbor 선정

            int size = seed.size();
            for (int i = 0; i < size; i++) {
                Point q = seed.get(i);
                if (q.getLabel() == noise) q.setLabel(label);
                if (q.getLabel() != undefined) continue;
                neighbors = findNeighbors(q);
                q.setLabel(label);
                if (neighbors.size() < minPts) continue;
                unionSeedAndNeighbor(seed, neighbors);
                size = seed.size();
            }
        }

    }

}
