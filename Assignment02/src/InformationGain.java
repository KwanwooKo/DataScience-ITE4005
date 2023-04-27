import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

// getSelectedFeature 호출할 때 selectedFeature 선택 checkFeature[getSelectedFeature()] = true 하면 됨
public class InformationGain {
    // data들 자체로 들고 옴
    private ArrayList<String> information;
    private int numberOfFeature;
    private boolean[] checkFeature;
    private double gainRatio = -1.0;
    private int selectedFeature = -1;
    private HashMap<String, Integer> rootClassification;
    public InformationGain(ArrayList<String> information, boolean[] checkFeature) {
        this.information = new ArrayList<>(information);
        // numberOfFeature를 먼저 계산
        numberOfFeature = information.get(0).split("\t").length - 1;
        this.checkFeature = checkFeature;
        rootClassification = new HashMap<>();

        // 여기서 root를 classify을 하자
        for (String singleInfo : information) {
            String[] str = singleInfo.split("\t");
            String key = str[str.length - 1];
            int count = rootClassification.getOrDefault(key, 0) + 1;
            rootClassification.put(key, count);
        }
    }

    public int getSelectedFeature() {
        informationGain();
        return selectedFeature;
    }

    public void informationGain() {

        // 각각의 feature로 information을 갈라보자
        for (int i = 0; i < numberOfFeature; i++) {
            if (checkFeature[i]) continue;
            // i 번째 feature로 구분하기
            HashMap<String, Integer> childCount = new HashMap<>();
            HashMap<String, ArrayList<String>> child = new HashMap<>();
            for (String singleInfo : information) {
                String key = singleInfo.split("\t")[i];

                int count = childCount.getOrDefault(key, 0) + 1;
                childCount.put(key, count);
                ArrayList<String> value;

                // child에 해당 information 추가
                if (child.containsKey(key)) {
                    // 여기서는 put 안해줘도 됨
                    value = child.get(key);
                    value.add(singleInfo);
                } else {
                    value = new ArrayList<>();
                    value.add(singleInfo);
                    child.put(key, value);
                }
            }
            if (gainRatio < (calculateInfo(rootClassification) - calculateInfoAfterSplit(childCount, child)) / calculateSplitInfo(childCount, child)) {
                gainRatio = (calculateInfo(rootClassification) - calculateInfoAfterSplit(childCount, child)) / calculateSplitInfo(childCount, child);
                selectedFeature = i;
            }

            childCount = null;
            child = null;
        }

    }

    public double calculateInfo(HashMap<String, Integer> child) {

        double info = 0.0;
        // 만약 종류가 하나 밖에 없다면 그냥 0.0을 반환
        if (child.size() == 1) return info;


        int total = 0;
        for (String key : child.keySet()) {
            total += child.get(key);
        }

        for (String key : child.keySet()) {
            double percentage = (double) child.get(key) / (double) total;
            info -= (percentage * (Math.log(percentage) / Math.log(2)));
        }

        return info;
    }


    public double calculateInfoAfterSplit(HashMap<String, Integer> childCount, HashMap<String, ArrayList<String>> child) {
        double infoAfterSplit = 0.0;
        int total = 0;
        for (String key : childCount.keySet()) {
            total += childCount.get(key);
        }

        // child를 HashMap<String, Integer> 로 보내면 가능할듯? => 여기서 classification으로 종류 만들어야 된다
        for (String key : child.keySet()) {
            HashMap<String, Integer> classification = new HashMap<>();

            ArrayList<String> information = child.get(key);
            for (String singleInfo : information) {
                String[] str = singleInfo.split("\t");
                String answer = str[str.length - 1];
                int count = classification.getOrDefault(answer, 0) + 1;
                classification.put(answer, count);
            }

            infoAfterSplit += (double) childCount.get(key) / (double) total * calculateInfo(classification);

            classification = null;
        }

        return infoAfterSplit;
    }

    public double calculateSplitInfo(HashMap<String, Integer> childCount, HashMap<String, ArrayList<String>> child) {
        double splitInfo = 0.0;
        int total = 0;
        for (String key : childCount.keySet()) {
            total += childCount.get(key);
        }

        for (String key : child.keySet()) {
            HashMap<String, Integer> classification = new HashMap<>();

            ArrayList<String> information = child.get(key);
            for (String singleInfo : information) {
                String[] str = singleInfo.split("\t");
                String answer = str[str.length - 1];
                int count = classification.getOrDefault(answer, 0) + 1;
                classification.put(answer, count);
            }
            double percentage = (double) childCount.get(key) / (double) total;
            splitInfo -= percentage * (Math.log(percentage) / Math.log(2));

            classification = null;
        }


        return splitInfo;
    }
}

























