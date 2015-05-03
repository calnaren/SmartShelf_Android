package smartshelf.pb4j.iot.smartshelf;

import java.util.List;

/**
 * Created by narenvasanad on 5/2/15.
 */
public class DataHolder {
    private String barcode;
    private List<Integer> shelfWeights;
    public String getBarcode() {
        return this.barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public List<Integer> getShelf() {
        return this.shelfWeights;
    }

    public void setShelf(List<Integer> shelfWeights_arg) {
        this.shelfWeights = shelfWeights_arg;
    }

    private static final DataHolder holder = new DataHolder();

    public static DataHolder getInstance() {
        return holder;
    }
}
