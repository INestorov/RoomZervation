import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import application.entities.Holiday;
import java.sql.Date;
import org.junit.jupiter.api.Test;

public class HolidayTest {

    @Test
    public void holidayConstructorTest() {
        Holiday holiday = new Holiday("new year",
            Date.valueOf("2019-01-01"), Date.valueOf("2019-01-03"), true);
        assertEquals(holiday.getName(), "new year");
        assertEquals(holiday.getStart(), Date.valueOf("2019-01-01"));
        assertEquals(holiday.getEnd(), Date.valueOf("2019-01-03"));
    }

    @Test
    public void holidaySetNameTest() {
        Holiday holiday = new Holiday("new year",
            Date.valueOf("2019-01-01"), Date.valueOf("2019-01-03"), true);
        holiday.setName("New Year");
        assertEquals(holiday.getName(), "New Year");
    }

    @Test
    public void holidaySetStartTest() {
        Holiday holiday = new Holiday("new year",
            Date.valueOf("2019-01-01"), Date.valueOf("2019-01-03"), true);
        holiday.setStart(Date.valueOf("2019-01-02"));
        assertEquals(holiday.getStart(), Date.valueOf("2019-01-02"));
    }

    @Test
    public void holidaySetEndTest() {
        Holiday holiday = new Holiday("new year",
            Date.valueOf("2019-01-01"), Date.valueOf("2019-01-03"), true);
        holiday.setEnd(Date.valueOf("2019-01-04"));
        assertEquals(holiday.getEnd(), Date.valueOf("2019-01-04"));
    }

    @Test
    public void holidaySetIdTest() {
        Holiday holiday = new Holiday("new year",
            Date.valueOf("2019-01-01"), Date.valueOf("2019-01-03"), true);
        holiday.setId(0);
        assertEquals(holiday.getId(), 0);
    }

    @Test
    public void holidayEqualsTest() {
        Holiday holiday1 = new Holiday("new year",
            Date.valueOf("2019-01-01"), Date.valueOf("2019-01-03"), true);
        Holiday holiday2 = new Holiday("Christmas",
            Date.valueOf("2019-12-24"), Date.valueOf("2019-12-26"), true);
        assertNotEquals(holiday1, holiday2);
    }
}
