package smartshelf.pb4j.iot.smartshelf;

/**
 * Created by narenvasanad on 5/2/15.
 */
public class DataHolder {
    private String barcode;
    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    private static final DataHolder holder = new DataHolder();

    public static DataHolder getInstance() {
        return holder;
    }
}
