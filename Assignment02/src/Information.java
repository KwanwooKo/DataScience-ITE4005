import java.util.ArrayList;

public interface Information {
    int numberOfChild = 0;
    int featureIndex = -1;
    boolean isLeaf = false;
    ArrayList<String> info = null;
    boolean[] selectedFeature = null;
    ArrayList<InformationImpl> child = null;

    void splitInfo();
    int getNumberOfChild();
    ArrayList<Information> getChild();
    boolean getIsLeaf();
    int getFeatureIndex();
    ArrayList<String> getInfo();

}
