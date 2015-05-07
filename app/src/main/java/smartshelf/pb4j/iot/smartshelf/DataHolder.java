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
    private List<Integer> colors = Arrays.asList(0, 0, 0, 0, 0, 0);

    private List<String> barcodes = Arrays.asList("X00000000000", "X09118973680", "X49247783502", "X49247783519",
            "X49247783526", "X57814133880", "X57814133873", "X57814133897", "X09118973673", "X09118973697");
    private List<String> itemNames = Arrays.asList("dummyItem", "Humira", "Nexium", "Crestor",
            "Remicade", "Rituxan", "Copaxone", "Atripla", "Abilify", "Enbrel");

    public List<Integer> getColors() {
        return colors;
    }

    public void setColors(List<Integer> colors) {
        this.colors = colors;
    }

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
