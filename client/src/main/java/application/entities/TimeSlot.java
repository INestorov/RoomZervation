package application.entities;

import java.sql.Timestamp;
//@author - Silviu Marii

public class TimeSlot {
    private int id;
    private String start;
    private String end;
    //private String closingTime;
    //private String openingTime;
    private Room room;
    private Timestamp startTime;
    private Timestamp endTime;
    private int reservationId;

    public TimeSlot() {
    }

    /**
     * Constructor.
     *
     * @param id    room id
     * @param start start time
     * @param end   end time
     */
    public TimeSlot(int id, String start, String end, int reservationId) {
        this.id = id;
        this.start = start;
        this.end = end;
        this.reservationId = reservationId;
        //        this.openingTime=openingTime;
        //        this.closingTime=closingTime;
    }

    /**
     * Another constructor for timeSlot.
     *
     * @param room  the room that has this new time slot
     * @param id    id of the time slot retrieved from server
     * @param start start time of the time slot
     * @param end   end time of the time slot
     */
    public TimeSlot(Room room, int id, String start, String end) {
        this.id = id;
        this.room = room;
        this.start = start;
        this.end = end;
        //        this.openingTime=openingTime;
        //        this.closingTime=closingTime;
    }

    /**
     * Another constructor for timeSlot.
     *
     * @param id    id of the time slot retrieved from server
     * @param start start time of the time slot
     * @param end   end time of the time slot
     */
    public TimeSlot(int id, String start, String end) {
        this.id = id;
        this.start = start;
        this.end = end;
        //        this.openingTime=openingTime;
        //        this.closingTime=closingTime;
    }

    /**
     * Another constructor for timeSlot.
     *
     * @param room  the room that has this new time slot
     * @param id    id of the time slot retrieved from server
     * @param start start time of the time slot
     * @param end   end time of the time slot
     */
    public TimeSlot(Room room, int id, String start, String end, int reservationId) {
        this.id = id;
        this.room = room;
        this.start = start;
        this.end = end;
        this.reservationId = reservationId;
        //        this.openingTime=openingTime;
        //        this.closingTime=closingTime;
    }

    /**
     * Constructor for posting to server.
     *
     * @param id        id of the room the time slot was selected
     * @param startTime start time of the time slot
     * @param endTime   end time of the time slot
     */
    public TimeSlot(int id, Timestamp startTime, Timestamp endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.id = id;
    }

    public Room getRoom() {
        return room;
    }

    public Timestamp getEndTime() {
        return endTime;
    }


    public int getId() {
        return id;
    }

    public String getStart() {
        return start;
    }

    public String getEnd() {
        return end;
    }

    public String toString() {
        return this.room.toString() + this.start + " " + this.end;
    }

    public int getReservationId() {
        return reservationId;
    }

    public void setReservationId(int reservationId) {
        this.reservationId = reservationId;
    }
}
