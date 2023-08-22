import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import application.controllers.HolidayController;
import application.entities.Holiday;
import application.repositories.HolidayRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class HolidayControllerTest {
    @InjectMocks
    HolidayController holidayController;

    @Mock
    HolidayRepository holidayRepository;

    @Test
    public void testReadHolidays() {
        Holiday holiday1 = new Holiday("new year",
            Date.valueOf("2019-01-01"), Date.valueOf("2019-01-03"), true);
        Holiday holiday2 = new Holiday("Christmas",
            Date.valueOf("2019-12-24"), Date.valueOf("2019-12-26"), true);
        List<Holiday> holidays = new ArrayList<>();
        holidays.add(holiday1);
        holidays.add(holiday2);
        when(holidayRepository.findAll()).thenReturn(holidays);
        List<Holiday> result = holidayController.readHolidays().getBody();
        assertEquals(result.get(0), holiday1);
    }

    @Test
    public void testUpdateHolidayById() {
        Holiday holiday = new Holiday("new year",
            Date.valueOf("2019-01-01"), Date.valueOf("2019-01-03"), true);
        holiday.setId(0);
        List<Holiday> holidays = new ArrayList<>();
        holidays.add(holiday);
        when(holidayRepository.findById(0)).thenReturn(java.util.Optional.of(holiday));
        when(holidayRepository.findAll()).thenReturn(holidays);
        Holiday holiday1 = new Holiday("Christmas",
            Date.valueOf("2019-12-24"), Date.valueOf("2019-12-26"), true);
        holidayController.updateHolidayById(0, holiday1);
        assertEquals(holidayController.readHolidays().getBody().get(0), holiday1);
    }

    @Test
    public void testDeleteHolidayById() {
        Holiday holiday1 = new Holiday("new year",
            Date.valueOf("2019-01-01"), Date.valueOf("2019-01-03"), true);
        Holiday holiday2 = new Holiday("Christmas",
            Date.valueOf("2019-12-24"), Date.valueOf("2019-12-26"), true);
        List<Holiday> holidays = new ArrayList<>();
        holidays.add(holiday1);
        holidays.add(holiday2);
        when(holidayRepository.findById(0)).thenReturn(java.util.Optional.of(holiday1));
        holidayController.deleteHolidayById(0);
        holidays.remove(0);
        when(holidayRepository.findAll()).thenReturn(holidays);
        assertEquals(holidayController.readHolidays().getBody().size(), 1);
    }
}
