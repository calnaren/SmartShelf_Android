package smartshelf.pb4j.iot.smartshelf;

/**
 * Created by narenvasanad on 5/2/15.
 */
public class ShelfItem {
    private String name = "";
    private int index = -1;
    private int weight = -1;
    private boolean[] schedule = {false, false, false};

    public boolean[] getSchedule() {
        return schedule;
    }

    public void setSchedule(boolean[] schedule) {
        this.schedule = schedule;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
}