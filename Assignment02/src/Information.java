import java.util.ArrayList;

public interface Information {
    int numberOfChild = 0;
    int featureIndex = 0;
    ArrayList<String> info = null;
    boolean[] selectedFeature = null;
    ArrayList<InformationImpl> child = null;

    void splitInfo();

    int getNumberOfChild();
    ArrayList<Information> getChild();
}
