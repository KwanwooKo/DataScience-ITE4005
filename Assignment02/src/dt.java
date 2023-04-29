import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

public class dt {
    public static void buildTree(Information node) {
        node.splitInfo();
        ArrayList<Information> info = node.getChild();
        for (int i = 0; i < node.getNumberOfChild(); i++) {
            buildTree(info.get(i));
        }
    }

    public static void main(String[] args) throws Exception {

        // 1. 각 노드 별로 info 설정
        // 2. info의 information gain 진행 => feature selection 하기
        // 3. 해당 feature를 기준으로 split 진행 => child 개수 파악 후 child 개수 만큼 InformationImpl 할당 => child와 연결
        //      1. child 개수 파악
        //      2. child 개수 만큼 InformationImpl class 할당
        //      3. Information의 child와 연결
        // 4. node 이동해서 마저 진행
        File trainFile = new File(args[0]);
        File testFile = new File(args[1]);
        File resultFile = new File(args[2]);

        // 제일 먼저 root node에 대해서 해당 과정 진행
        Information rootInfo = new RootInformation(trainFile);
        buildTree(rootInfo);

        // buildTree까지 완료

        Result result = new Result(trainFile, testFile, resultFile, rootInfo);
        // 이렇게 하면 알아서 적히게 끔
        result.writeResult();

    }
}