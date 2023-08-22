import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import application.controls.DateTimePicker;
import java.time.DayOfWeek;
import org.junit.jupiter.api.Test;

public class DateTimePickerTest {

    @Test
    void testDateTimeFormatConstructor() {
        DateTimePicker.DateTimeFormat test = new DateTimePicker.DateTimeFormat(DayOfWeek.FRIDAY, 1,
            2, 2020, 4, 1);
        assertNotNull(test);
    }

    @Test
    void testGetDate() {
        DateTimePicker.DateTimeFormat test = new DateTimePicker.DateTimeFormat(DayOfWeek.FRIDAY, 1,
            2, 2020, 4, 1);
        assertEquals(test.getDate(), " FRIDAY 1.2.2020");
    }

    @Test
    void testGetTime() {
        DateTimePicker.DateTimeFormat test = new DateTimePicker.DateTimeFormat(DayOfWeek.FRIDAY, 1,
            2, 2020, 4, 1);
        assertEquals(test.getTime(), "4:1");
    }

    @Test
    void testToString() {
        DateTimePicker.DateTimeFormat test = new DateTimePicker.DateTimeFormat(DayOfWeek.FRIDAY, 1,
            2, 2020, 4, 1);
        assertEquals(test.toString(), " FRIDAY 1.2.2020");
    }

    @Test
    void testGetDayOfWeek() {
        DateTimePicker.DateTimeFormat test = new DateTimePicker.DateTimeFormat(DayOfWeek.FRIDAY, 1,
            2, 2020, 4, 1);
        assertEquals(test.getDayOfWeek(), DayOfWeek.FRIDAY);
    }

    @Test
    void testGetHour() {
        DateTimePicker.DateTimeFormat test = new DateTimePicker.DateTimeFormat(DayOfWeek.FRIDAY, 1,
            2, 2020, 4, 1);
        assertEquals(test.getHour(), 4);
    }

    @Test
    void testGetMinute() {
        DateTimePicker.DateTimeFormat test = new DateTimePicker.DateTimeFormat(DayOfWeek.FRIDAY, 1,
            2, 2020, 4, 1);
        assertEquals(test.getMinute(), 1);
    }

    @Test
    void testGetMonth() {
        DateTimePicker.DateTimeFormat test = new DateTimePicker.DateTimeFormat(DayOfWeek.FRIDAY, 1,
            2, 2020, 4, 1);
        assertEquals(test.getMonth(), 2);
    }

    @Test
    void testGetYear() {
        DateTimePicker.DateTimeFormat test = new DateTimePicker.DateTimeFormat(DayOfWeek.FRIDAY, 1,
            2, 2020, 4, 1);
        assertEquals(test.getYear(), 2020);
    }

    @Test
    void testGetDay() {
        DateTimePicker.DateTimeFormat test = new DateTimePicker.DateTimeFormat(DayOfWeek.FRIDAY, 1,
            2, 2020, 4, 1);
        assertEquals(test.getDay(), 1);
    }

}
