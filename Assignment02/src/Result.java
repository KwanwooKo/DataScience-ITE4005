import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class Result {
    private FileReader fr;
    private FileReader train_fr;
    private FileWriter fw;

    private BufferedReader test;
    private BufferedWriter result;
    private BufferedReader train;

    private String label;
    private Information rootInfo;
    public Result(File trainFile, File testFile, File resultFile, Information rootInfo) {
        try {
            train_fr = new FileReader(trainFile);
            fr = new FileReader(testFile);
            fw = new FileWriter(resultFile);
            test = new BufferedReader(fr);
            result = new BufferedWriter(fw);
            train = new BufferedReader(train_fr);

            this.rootInfo = rootInfo;

            label = train.readLine() + "\n";
            test.readLine();
            train.close();
            train_fr.close();

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    void writeResult() throws IOException {
        // 처음에 label 적고
        result.write(label);
        Information cur = rootInfo;
        String line = "";

        while ((line = test.readLine()) != null) {
            traverseTree(rootInfo, line);
        }


        result.close();
        test.close();
        fw.close();
        fr.close();
    }

    String classification(Information node) {
        String ret = "";
        ArrayList<String> info = node.getInfo();
        HashMap<String, Integer> classLabel = new HashMap<>();
        for (String singleInfo : info) {
            String[] singleInfoSplit = singleInfo.split("\t");
            String label = singleInfoSplit[singleInfoSplit.length - 1];
            int count = classLabel.getOrDefault(label, 0) + 1;
            classLabel.put(label, count);
        }
        int maxCount = Collections.max(classLabel.values());
        for (String key : classLabel.keySet()) {
            if (classLabel.get(key) == maxCount) {
                ret = key;
                break;
            }
        }
        return ret;
    }

    void traverseTree(Information node, String line) throws IOException {
        if (node.getIsLeaf()) {
            // classification
            String labelClass = classification(node);
            // 여기서 한줄 적고 끝내면 됨
            String buffer = line + "\t";
            buffer += labelClass;
            buffer += "\n";

            result.write(buffer);
            return;
        }
        int featureIndex = node.getFeatureIndex();
        boolean findTraverse = false;
        for (Information child : node.getChild()) {
            ArrayList<String> info = child.getInfo();
            String[] label = info.get(0).split("\t");
            String[] lineSplit = line.split("\t");
            if (label[featureIndex].equals(lineSplit[featureIndex])) {
                traverseTree(child, line);
                findTraverse = true;
            }
        }
        if (!findTraverse) {
            String labelClass = classification(node);
            String buffer = line + "\t";
            buffer += labelClass;
            buffer += "\n";

            result.write(buffer);
        }
    }
}




















