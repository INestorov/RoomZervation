package application.controls;

import application.entities.TimeSlot;
import java.util.Comparator;

public class Sort implements Comparator<TimeSlot> {
    public int compare(TimeSlot a, TimeSlot b) {
        return a.getStart().compareTo(b.getStart());
    }
}