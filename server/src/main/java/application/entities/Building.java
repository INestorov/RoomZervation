package application.entities;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import java.io.IOException;
import java.sql.Time;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import org.springframework.lang.Nullable;


@Entity
@Table(name = "building")
//@JsonDeserialize(using = Building.BuildingDeserializer.class)
public class Building {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    //generates value automatically for field id
    private Integer id;

    @NotNull
    @Column(name = "name")
    private String name;

    @NotNull
    @Column(name = "opening_time")
    private Time openingTime;

    @NotNull
    @Column(name = "closing_time")
    private Time closingTime;

    //    @OneToMany(mappedBy = "building", cascade = CascadeType.MERGE)
    //    private Set<Room> rooms;

    public Building() {
    }

    /**
     * Create a new Building instance.
     *
     * @param name        name
     * @param openingTime opening time
     * @param closingTime closing time
     */
    public Building(String name, Time openingTime, Time closingTime) {
        this.name = name;
        this.openingTime = openingTime;
        this.closingTime = closingTime;
        //        this.rooms = Stream.of(rooms).collect(Collectors.toSet());
        //        this.rooms.forEach(x -> x.setBuilding(this));
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Time getOpeningTime() {
        return openingTime;
    }

    public void setOpeningTime(Time openingTime) {
        this.openingTime = openingTime;
    }

    public Time getClosingTime() {
        return closingTime;
    }

    public void setClosingTime(Time closingTime) {
        this.closingTime = closingTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Building)) {
            return false;
        }
        Building building = (Building) o;
        return id.equals(building.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }


    static class BuildingDeserializer extends StdDeserializer<Building> {
        protected BuildingDeserializer() {
            super((Class<?>) null);
        }

        @Override
        public Building deserialize(com.fasterxml.jackson.core.JsonParser p,
                                    DeserializationContext ctxt) throws IOException {
            JsonNode jsonNode = p.getCodec().readTree(p);
            ObjectMapper mapper = new ObjectMapper();
            String name = jsonNode.get("name").asText();
            Time start = Time.valueOf(jsonNode.get("openingTime").asText());
            Time end = Time.valueOf(jsonNode.get("closingTime").asText());
            Building building = new Building(name, start, end);
            building.setId(jsonNode.get("id").asInt());
            return building;
        }
    }
}