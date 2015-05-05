package smartshelf.pb4j.iot.smartshelf;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by narenvasanad on 5/2/15.
 */
public class DataHolder {
    private String barcode = "";
    private String previousBarcode = "";
    private List<Integer> shelfWeights = new ArrayList<Integer>();
    private List<Integer> previousShelfWeights = new ArrayList<Integer>();
    private List<ShelfItem> items = new ArrayList<ShelfItem>();
    private LED led= new LED(0, 0, 0, 0);
    private String currentActivity = "";
    private boolean addFlag = false;
    private boolean placeFlag = false;
    private boolean newDay = true;
    private static int WEIGHT_THRESHOLD = 125;

    public static int getWEIGHT_THRESHOLD() {
        return WEIGHT_THRESHOLD;
    }

    public static void setWEIGHT_THRESHOLD(int WEIGHT_THRESHOLD) {
        DataHolder.WEIGHT_THRESHOLD = WEIGHT_THRESHOLD;
    }

    public boolean isNewDay() {
        return newDay;
    }

    public void setNewDay(boolean newDay) {
        this.newDay = newDay;
    }

    private List<String> barcodes = Arrays.asList("X3400704", "X3400607", "X3400908");
    private List<String> itemNames = Arrays.asList("Coolmint", "Spearmint", "Wintergreen");

    public List<String> getBarcodes() {
        return barcodes;
    }

    public void setBarcodes(List<String> barcodes) {
        this.barcodes = barcodes;
    }

    public List<String> getItemNames() {
        return itemNames;
    }

    public void setItemNames(List<String> itemNames) {
        this.itemNames = itemNames;
    }

    public boolean isAddFlag() {
        return addFlag;
    }

    public void setAddFlag(boolean addFlag) {
        this.addFlag = addFlag;
    }

    public boolean isPlaceFlag() {
        return placeFlag;
    }

    public void setPlaceFlag(boolean placeFlag) {
        this.placeFlag = placeFlag;
    }

    public String getCurrentActivity() {
        return currentActivity;
    }

    public void setCurrentActivity(String currentActivity) {
        this.currentActivity = currentActivity;
    }

    public LED getLed() {
        return led;
    }

    public void setLed(LED led) {
        this.led = led;
    }

    public List<ShelfItem> getItems() {
        return items;
    }

    public void setItems(List<ShelfItem> items) {
        this.items = items;
    }

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
        this.previousShelfWeights = this.shelfWeights;
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
