public class Pair implements Comparable<Pair> {
    private int level;
    private int count;

    public Pair(int level, int count) {
        this.level = level;
        this.count = count;
    }

    @Override
    public int compareTo(Pair p) {
        return Integer.compare(this.count, p.count);
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

}
