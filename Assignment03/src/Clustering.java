import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

public class Clustering {
    public static void main(String[] args) throws IOException {

        // input.txt    n     Eps     MinPts
        File input = new File(args[1]);
        int n = Integer.parseInt(args[2]);
        double eps = Double.parseDouble(args[3]);
        int minPts = Integer.parseInt(args[4]);
        String inputId = args[1].split(".txt")[0];

//        System.out.println("n: " + n + "\teps: " + eps + "\tminPts: " + minPts);

        BufferedReader br = new BufferedReader(new FileReader(input));
        ArrayList<Point> points = new ArrayList<>();
        String line;
        while ((line = br.readLine()) != null) {
            String[] str = line.split("\t");
            int id = Integer.parseInt(str[0]);
            double x = Double.parseDouble(str[1]);
            double y = Double.parseDouble(str[2]);
            Point point = new Point(id, x, y);
            points.add(point);
        }

        DBScan dbScan = new DBScan(n, eps, minPts, points);

        // points가 알아서 값 다 변경됨
        dbScan.cluster();

        ArrayList<Pair> clusterList = clusterPoints(points);

        writeOutput(inputId, n, clusterList, points);
    }

    // 정렬해서 cluster 개수 줄일 방법을 생각해보자
    public static ArrayList<Pair> clusterPoints(ArrayList<Point> points) {
        ArrayList<Pair> ret = new ArrayList<>();
        HashMap<Integer, Integer> clusterList = new HashMap<>();
        for (Point p : points) {
            // 이거 있어도 정상적으로 돌아가야 돼
            if (p.getLabel() == -2) continue;
            if (clusterList.containsKey(p.getLabel())) {
                int count = clusterList.get(p.getLabel()) + 1;
                clusterList.put(p.getLabel(), count);
            } else {
                clusterList.put(p.getLabel(), 1);
            }
        }

        for (Integer lev : clusterList.keySet()) {
            Pair pair = new Pair(lev, clusterList.get(lev));
            ret.add(pair);
        }

        Collections.sort(ret);

        for (Pair p : ret) {
            System.out.println("level: " + p.getLevel() + "\tcount: " + p.getCount());
        }

        return ret;
    }


    public static void writeOutput(String inputId, int n, ArrayList<Pair> clusterList, ArrayList<Point> points) throws IOException {
        // clusterList => {level, count}
        ArrayList<String> files = new ArrayList<>();
        int size = clusterList.size();
        for (int i = 0; i < n; i++) {
            String buffer = inputId;
            String fileNum = String.valueOf(i);
            buffer += ("_cluster_" + fileNum + ".txt");
            files.add(buffer);
        }


        int index = 0;
        System.out.println("size: " + size);
        System.out.println("n: " + n);
        if (size > n) {
            index = size - n;
        }
        int lev = 0;


        for (String fileName : files) {
            File output = new File(fileName);
            BufferedWriter bw = new BufferedWriter(new FileWriter(output));
            lev = clusterList.get(index).getLevel();
            for (Point p : points) {
                if (p.getLabel() == lev) {
                    String line = p.getId() + "\n";
                    bw.write(line);
                }
            }

            index++;
            bw.close();
        }
    }
}