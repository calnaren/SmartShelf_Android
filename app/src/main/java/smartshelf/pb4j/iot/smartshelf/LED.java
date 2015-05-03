package smartshelf.pb4j.iot.smartshelf;

/**
 * Created by narenvasanad on 5/2/15.
 */
public class LED {
    private int index = 0;
    private int r = 0;
    private int g = 0;
    private int b = 0;

    public LED (int index, int r, int g, int b) {
        this.index = index;
        this.r = r;
        this.g = g;
        this.b = b;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getR() {
        return r;
    }

    public void setR(int r) {
        this.r = r;
    }

    public int getG() {
        return g;
    }

    public void setG(int g) {
        this.g = g;
    }

    public int getB() {
        return b;
    }

    public void setB(int b) {
        this.b = b;
    }
}
