import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class RootInformation implements Information{
    private int featureIndex;
    private ArrayList<String> info;
    private boolean[] selectedFeature;
    private ArrayList<Information> child;
    private int numberOfChild;

    public RootInformation(File trainFile) throws IOException {
        info = new ArrayList<>();
        child = new ArrayList<>();
        FileReader fr = new FileReader(trainFile);
        BufferedReader br = new BufferedReader(fr);
        String buffer = "";
        buffer = br.readLine();
        String[] str = buffer.split("\t");
        while ((buffer = br.readLine()) != null) {
            info.add(buffer);
        }
        selectedFeature = new boolean[str.length];
        br.close();
    }

    // 여기서 split 하고 child에 연결
    public void splitInfo() {
        // info를 split 해야돼 featureIndex 기준으로
        InformationGain ig = new InformationGain(info, selectedFeature);
        featureIndex = ig.getSelectedFeature();
        selectedFeature[featureIndex] = true;
        // featureIndex 기준으로 split 진행
        HashMap<String, ArrayList<String>> splits = new HashMap<>();
        for (String singleInfo : info) {
            String[] str = singleInfo.split("\t");
            String key = str[featureIndex];

            ArrayList<String> data;
            if (splits.containsKey(key)) {
                data = splits.get(key);
                data.add(singleInfo);
            } else {
                data = new ArrayList<>();
                data.add(singleInfo);
                splits.put(key, data);
            }
        }

        for (String key : splits.keySet()) {
            Information childInfo = new InformationImpl(splits.get(key), selectedFeature);
            child.add(childInfo);
            numberOfChild++;
        }

    }

    public int getNumberOfChild() {
        return numberOfChild;
    }

    public ArrayList<Information> getChild() {
        return child;
    }
}
