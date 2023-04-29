import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class InformationImpl implements Information{
    private int featureIndex;
    private ArrayList<String> info;
    private boolean[] selectedFeature;
    private ArrayList<Information> child;
    private int numberOfChild;
    private boolean isLeaf; // isLeaf 이면 무조건 classification 진행

    public InformationImpl(ArrayList<String> info, boolean[] selectedFeature) {
        this.info = new ArrayList<>(info);
        this.child = new ArrayList<>();
        this.selectedFeature = Arrays.copyOf(selectedFeature, selectedFeature.length);
    }

    public void splitInfo() {
        // 여기서 break 걸자 => info의 class label이 하나로 통일되어 있으면 멈추는 걸로
        // feature가 선택되기 전에 classLabel 종류 확인
        HashMap<String, Integer> classLabel = new HashMap<>();
        for (String singleInfo : info) {
            String[] str = singleInfo.split("\t");
            String key = str[str.length - 1];
            int count = classLabel.getOrDefault(key, 0) + 1;
            classLabel.put(key, count);
        }
        if (classLabel.size() == 1) {
            isLeaf = true;
            return;
        }


        InformationGain ig = new InformationGain(info, selectedFeature);
        featureIndex = ig.getSelectedFeature();
        selectedFeature[featureIndex] = true;
        HashMap<String, ArrayList<String>> splits = new HashMap<>();
        for (String singleInfo : info) {
            String[] str = singleInfo.split("\t");
            String key = str[featureIndex];

            ArrayList<String> data;
            if (!splits.containsKey(key)) {
                data = new ArrayList<>();
                data.add(singleInfo);
                splits.put(key, data);
            } else {
                data = splits.get(key);
                data.add(singleInfo);
            }
        }

        // 만약 label 종류가 여러개라면 여기서 마저 진행
        for (String key : splits.keySet()) {
            InformationImpl childInfo = new InformationImpl(splits.get(key), selectedFeature);
            child.add(childInfo);
            numberOfChild++;
        }
    }

    @Override
    public int getNumberOfChild() {
        return numberOfChild;
    }

    @Override
    public ArrayList<Information> getChild() {
        return child;
    }

    @Override
    public boolean getIsLeaf() {
        return isLeaf;
    }

    @Override
    public int getFeatureIndex() {
        return featureIndex;
    }

    @Override
    public ArrayList<String> getInfo() {
        return info;
    }
}
