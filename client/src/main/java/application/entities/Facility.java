package application.entities;

import java.util.List;
import java.util.Objects;

public class Facility {

    private List<Integer> roomIds;
    private String name;


    public Facility(List<Integer> roomIds, String name) {
        this.roomIds = roomIds;
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Facility facility = (Facility) o;
        return roomIds.equals(facility.roomIds)
            && name.equals(facility.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(roomIds, name);
    }

    public List<Integer> getRoomIds() {
        return roomIds;
    }

    public void setRoomIds(List<Integer> roomIds) {
        this.roomIds = roomIds;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
