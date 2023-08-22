package application.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.sun.istack.NotNull;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

@Entity
@Table(name = "reservation")
@JsonDeserialize(using = Reservation.ReservationDeserializer.class)
//    contentUsing = Room.RoomDeserializer.class)
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @ManyToOne
    @NotNull
    @Cascade(value = {CascadeType.PERSIST})
    @JoinColumn(name = "room_Id")
    private Room room;

    @ManyToOne
    @NotNull
    @Cascade(CascadeType.PERSIST)
    @JoinColumn(name = "user_Id")
    private User user;

    @NotNull
    @Column(name = "start")
    private Timestamp start;

    @NotNull
    @Column(name = "end")
    private Timestamp end;

    private Integer idRoom;
    private Integer idUser;

    public Reservation() {
    }

    /**
     * Class constructor.
     *
     * @param room  connector to room
     * @param start start time for reservation
     * @param end   end time for resersvation
     */
    @JsonIgnore
    public Reservation(Room room, Timestamp start, Timestamp end, User user) {
        this.room = room;
        this.user = user;
        this.start = start;
        this.end = end;
    }

    public Integer getRoomId() {
        return idRoom;
    }

    public Integer getUserId() {
        return idUser;
    }

    /** another constructor, can't remember where I used it.
     *
     * @param roomId room Id
     * @param start  start time of reservation
     * @param end    end time of reservation
     * @param userId user Id
     */
    public Reservation(Integer roomId, Timestamp start, Timestamp end, Integer userId) {
        this.idRoom = roomId;
        this.start = start;
        this.end = end;
        this.idUser = userId;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public Room getRoom() {
        return room;
    }

    public Timestamp getStart() {
        return start;
    }

    public void setStart(Timestamp start) {
        this.start = start;
    }

    public Timestamp getEnd() {
        return end;
    }

    public void setEnd(Timestamp end) {
        this.end = end;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Reservation)) {
            return false;
        }
        Reservation that = (Reservation) o;
        return getId() == that.getId()
            && getRoom().equals(that.getRoom())
            && getStart().equals(that.getStart())
            && getEnd().equals(that.getEnd());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    static class ReservationDeserializer extends StdDeserializer<Reservation> {
        protected ReservationDeserializer() {
            super((Class<?>) null);
        }

        @Override
        public Reservation deserialize(com.fasterxml.jackson.core.JsonParser p,
                                       DeserializationContext ctxt) throws IOException,
            JsonProcessingException {
            // ObjectMapper obj = new ObjectMapper();
            // Room room = obj.treeToValue(jsonNode.get("room"), Room.class);
            JsonNode jsonNode = p.getCodec().readTree(p);
            Timestamp start = Timestamp.valueOf(jsonNode.get("startTime").asText());
            Timestamp end = Timestamp.valueOf(jsonNode.get("endTime").asText());
            Integer room = jsonNode.get("roomId").asInt();
            Integer user = jsonNode.get("userId").asInt();
            return new Reservation(room,start,end,user);
        }
    }
}