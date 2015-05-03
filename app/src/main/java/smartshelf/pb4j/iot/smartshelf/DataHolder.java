package smartshelf.pb4j.iot.smartshelf;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by narenvasanad on 5/2/15.
 */
public class DataHolder {
    private String barcode = "";
    private String previousBarcode = "";
    private List<Integer> shelfWeights = new ArrayList<Integer>();
    private List<Integer> previousShelfWeights = new ArrayList<Integer>();
    public String getBarcode() {
        return this.barcode;
    }

    public void setBarcode(String barcode) {
        this.previousBarcode = this.barcode;
        this.barcode = barcode;
    }

    public String getPreviousBarcode() {
        return this.previousBarcode;
    }

    public void setPreviousBarcode(String previousBarcode) {
        this.previousBarcode = previousBarcode;
    }


    public List<Integer> getShelf() {
        return this.shelfWeights;
    }

    public void setShelf(List<Integer> shelfWeights_arg) {
        ArrayList<Integer> temp = new ArrayList<Integer>();
        for (Integer i: this.shelfWeights) {
            temp.add(i);
        }
        this.previousShelfWeights = temp;
        this.shelfWeights = shelfWeights_arg;
    }

    public List<Integer> getPreviousShelf() {
        return this.previousShelfWeights;
    }

    public void setPreviousShelf(List<Integer> previousShelfWeights_arg) {
        this.previousShelfWeights = previousShelfWeights_arg;
    }

    private static final DataHolder holder = new DataHolder();

    public static DataHolder getInstance() {
        return holder;
    }
}
