package application.entities;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import java.io.IOException;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;


@Entity
@Table(name = "room")
@JsonDeserialize(using = Room.RoomDeserializer.class)
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "building_id")
    @Cascade(CascadeType.MERGE)
    private Building building;


    @NotNull
    @Column(name = "name")
    private String name;

    @NotNull
    @Column(name = "size")
    private int size;

    @Column(name = "description")
    private String description;

    //@JsonManagedReference  // do not delete
    @ManyToMany(cascade = javax.persistence.CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinTable(
        name = "room_facility_information",
        joinColumns = @JoinColumn(name = "room_id"),
        inverseJoinColumns = @JoinColumn(name = "facility_id"))
    private Set<Facility> facilities = new HashSet<>();


    public Room() {
    }

    /**
     * Create a new Room instance.
     *
     * @param building building
     * @param name     name
     * @param size     size
     */
    public Room(Building building, String name, Integer size,
                Set<Facility> facilities, String description) {
        this.building = building;
        this.name = name;
        this.size = size;
        this.facilities = facilities;
        this.description = description;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Building getBuilding() {
        return building;
    }

    public void setBuilding(Building building) {
        this.building = building;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Set<Facility> getFacilities() {
        return facilities;
    }

    public void setFacilities(Set<Facility> facilities) {
        this.facilities = facilities;
    }

    public void addFacility(Facility facility) {
        facilities.add(facility);
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Room)) {
            return false;
        }
        Room room = (Room) o;
        return id.equals(room.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    static class RoomDeserializer extends StdDeserializer<Room> {

        protected RoomDeserializer() {
            super((Class<?>) null);
        }

        @Override
        public Room deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            JsonNode jsonNode = p.getCodec().readTree(p);
            ObjectMapper mapper = new ObjectMapper();
            Building building = mapper.treeToValue(jsonNode.get("building"), Building.class);
            String name = jsonNode.get("name").asText();
            int size = jsonNode.get("size").asInt();
            String description = jsonNode.get("description").asText();
            return new Room(building, name, size, null, description);
        }
    }
}
