package application.entities;

import application.communication.ServerCommunication;
import com.google.gson.JsonParser;
import java.util.List;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

// Used properties in order to be able to make a proper sync (allows to be notified when
// something changed)
public class Room {
    private final StringProperty name;
    private final IntegerProperty size;
    private StringProperty buildingName;
    private IntegerProperty buildingId;
    private IntegerProperty id;
    private List<String> facilities;
    private StringProperty description;

    /**
     * Create a new Room instance.
     *
     * @param name         name
     * @param buildingName name of building
     * @param size         size
     */
    public Room(Integer id, String name, String buildingName, Integer size) {
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
        this.buildingName = new SimpleStringProperty(buildingName);
        this.size = new SimpleIntegerProperty(size);
        // this.calendar=new SimpleObjectProperty<Something>();
    }

    /**
     * Another constructor.
     *
     * @param buildingId Id of building  the room belongs to
     * @param name       name of the building
     * @param size       size of the building
     */
    public Room(Integer id, String name, Integer buildingId, Integer size,
                List<String> facilities, String description) {
        this.facilities = facilities;
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
        this.buildingId = new SimpleIntegerProperty(buildingId);
        this.size = new SimpleIntegerProperty(size);
        this.description = new SimpleStringProperty(description);
        // this.calendar=new SimpleObjectProperty<Something>();
    }

    /**
     * Returns a nice looking string with the facilities of this room.
     *
     * @return string facilities.
     */
    public String getFacilities() {
        String ret = "";
        if (this.facilities == null || this.facilities.size() == 0) {
            return "Facilities not uploaded yet.";
        }
        for (int i = 0; i < this.facilities.size() - 1; i++) {
            ret = ret + facilities.get(i) + ", ";
        }
        ret = ret + this.facilities.get(this.facilities.size() - 1);
        return ret;
    }

    public String getDescription() {
        return description.get();
    }

    public StringProperty descriptionProperty() {
        return description;
    }

    public int getId() {
        return id.get();
    }

    public int getBuildingId() {
        return buildingId.get();
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public StringProperty nameProperty() {
        return name;
    }

    public int getSize() {
        return size.get();
    }

    public void setSize(int size) {
        this.size.set(size);
    }

    public IntegerProperty sizeProperty() {
        return size;
    }

    public String getBuildingName() {
        return buildingName.get();
    }


    public String getBuilding(int id) {
        return JsonParser.parseString(ServerCommunication.getBuildingById(id))
            .getAsJsonObject().get("name").getAsString();
    }

    public StringProperty buildingNameProperty() {
        return buildingName;
    }

    public String toString() {
        return this.id + " " + this.name + " " + this.buildingName + " " + this.size + " ";
    }
}