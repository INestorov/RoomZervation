package application.controllers;

import application.entities.Holiday;
import application.repositories.HolidayRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.sql.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/holidays")
public class    HolidayController {

    @Autowired
    HolidayRepository repository;

    /**
     * get endpoint to read all holidays.
     *
     * @return all holidays
     * @throws IllegalArgumentException if nothing is found
     */
    @GetMapping("/read")
    public ResponseEntity<List<Holiday>> readHolidays() throws IllegalArgumentException {
        List<Holiday> list = repository.findAll();
        if (!list.isEmpty()) {
            return new ResponseEntity<>(list, new HttpHeaders(), HttpStatus.OK);
        } else {
            throw new IllegalArgumentException("no holiday found");
        }
    }

    /**
     * POST Endpoint to add a new holiday.
     *
     * @param body content of the new holiday
     */
    @PostMapping("/create")
    public void createHoliday(@RequestBody Map<Object, Object> body) {
        ObjectMapper obj = new ObjectMapper();
        JsonNode jsonNode = obj.valueToTree(body);
        Holiday holiday = new Holiday(jsonNode.get("name").asText(),
            Date.valueOf(jsonNode.get("start").asText()),
            Date.valueOf(jsonNode.get("end").asText()),
            Boolean.getBoolean(jsonNode.get("repetitive").asText()));
        repository.save(holiday);
    }

    /**
     * PUT Endpoint to update Holidays.
     */
    @PutMapping("update/{id}")
    public void updateHolidayById(@PathVariable("id") Integer id,
                                  @RequestBody final Holiday entity)
        throws IllegalArgumentException {
        Optional<Holiday> holiday = repository.findById(id);
        if (holiday.isPresent()) {
            Holiday newEntity = holiday.get();
            newEntity.setStart(entity.getStart());
            newEntity.setEnd(entity.getEnd());
            newEntity.setName(entity.getName());
            repository.save(newEntity);
        } else {
            throw new IllegalArgumentException("No such holiday exists");
        }
    }

    /**
     * DELETE Endpoint to delete holiday by id.
     */
    @Cascade(CascadeType.ALL)
    @DeleteMapping("delete/{id}")
    public void deleteHolidayById(@PathVariable("id") Integer id)
        throws IllegalArgumentException {
        Optional<Holiday> list = repository.findById(id);
        if (list.isPresent()) {
            repository.deleteById(id);
        } else {
            throw new IllegalArgumentException("No such holiday exists");
        }
    }

}
