import static java.util.Optional.of;
import static org.mockito.Mockito.when;

import application.controllers.FacilityController;
import application.entities.Facility;
import application.repositories.FacilityRepository;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)

public class FacilityControllerTest {
    @InjectMocks
    FacilityController facilityController;

    @Mock
    FacilityRepository facilityRepository;

    @Test
    public void testGetAllFacilities() {
        List<Facility> facilities = new ArrayList<>();
        Facility facility1 = new Facility("TV");
        Facility facility2 = new Facility("screen");
        facilities.add(facility1);
        facilities.add(facility2);
        when(facilityRepository.findAll()).thenReturn(facilities);
        List<Facility> result = facilityController.getAllFacilities().getBody();
        Assertions.assertEquals(result.size(), 2);
        Assertions.assertEquals(result.get(0).getName(), "TV");
    }

    @Test
    public void testGetFacilityById() {
        Facility facility = new Facility("TV");
        facility.setId(0);
        when(facilityRepository.findById(0)).thenReturn(of(facility));
        Facility result = facilityController.getFacilityById(0).getBody();
        Assertions.assertEquals(result, facility);
    }

    @Test
    public void testCreate() {
        List<Facility> facilities = new ArrayList<>();
        Facility facility = new Facility("TV");
        facilities.add(facility);
        when(facilityRepository.findAll()).thenReturn(facilities);
        List<Facility> result = facilityController.create(facility);
        Assertions.assertEquals(result.get(0), facility);
    }

    @Test
    public void testUpdateUserById() {
        List<Facility> facilities = new ArrayList<>();
        Facility facility = new Facility("TV");
        facility.setId(0);
        facilities.add(facility);
        Facility facility1 = new Facility("screen");
        when(facilityRepository.findById(0)).thenReturn(of(facility));
        when(facilityRepository.findAll()).thenReturn(facilities);
        List<Facility> result = facilityController.updateFacilityById(0,facility1);
        Assertions.assertEquals(result.get(0).getName(), "screen");
    }

    @Test
    public void testDeleteUserById() {
        List<Facility> facilities = new ArrayList<>();
        Facility facility = new Facility("TV");
        facility.setId(0);
        facilities.add(facility);
        when(facilityRepository.findById(0)).thenReturn(of(facility));
        facilities.remove(facility);
        when(facilityRepository.findAll()).thenReturn(facilities);
        List<Facility> result = facilityController.deleteFacilityById(0);
        Assertions.assertEquals(result.size(),0);
    }
}
