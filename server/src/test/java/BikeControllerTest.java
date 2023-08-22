import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import application.controllers.BikeController;
import application.entities.Bike;
import application.entities.Building;
import application.repositories.BikeRepository;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class BikeControllerTest {

    @InjectMocks
    BikeController bikeController;

    @Mock
    BikeRepository bikeRepository;

    @Test
    public void testGetAllBikes() {
        Building building = new Building("a", Time.valueOf("10:00:00"), Time.valueOf("20:00:00"));
        Bike bike1 = new Bike(building);
        Bike bike2 = new Bike(building);
        List<Bike> bikes = new ArrayList<>();
        bikes.add(bike1);
        bikes.add(bike2);
        when(bikeRepository.findAll()).thenReturn(bikes);
        List<Bike> result = bikeController.getAllBikes().getBody();
        assertEquals(result.size(), 2);
    }

    @Test
    public void testGetBikeById() {
        Building building = new Building("a", Time.valueOf("10:00:00"), Time.valueOf("20:00:00"));
        Bike bike = new Bike(building);
        bike.setId(0);
        when(bikeRepository.findById(0)).thenReturn(java.util.Optional.of(bike));
        Bike result = bikeController.getBikeById(0).getBody();
        assertEquals(result, bike);
    }

    @Test
    public void testGetBikesByBuildingId() {
        Building building = new Building("a", Time.valueOf("10:00:00"), Time.valueOf("20:00:00"));
        building.setId(0);
        Bike bike = new Bike(building);
        List<Bike> bikes = new ArrayList<>();
        bikes.add(bike);
        when(bikeRepository.findAvailableBikesByBuilding_Id(0)).thenReturn(bikes);
        List<Bike> result = bikeController.getBikesByBuildingId(0).getBody();
        assertEquals(result.get(0), bike);
    }

    @Test
    public void testCreate() {
        List<Bike> bikes = new ArrayList<>();
        Building building = new Building("a", Time.valueOf("10:00:00"), Time.valueOf("20:00:00"));
        Bike bike = new Bike(building);
        bikes.add(bike);
        when(bikeRepository.findAll()).thenReturn(bikes);
        List<Bike> result = bikeController.create(bike);
        assertEquals(result.get(0), bike);
        assertEquals(result.size(), 1);
    }

    @Test
    public void testGetBikesNotReturned() {
        List<Bike> bikes = new ArrayList<>();
        Bike bike = new Bike();
        bikes.add(bike);
        when(bikeRepository.findBikesByBuildingIsNull()).thenReturn(bikes);
        List<Bike> result = bikeController.getBikesNotReturned().getBody();
        assertEquals(result.get(0), bike);

    }

    @Test
    public void testUpdateBikeById() {
        List<Bike> bikes = new ArrayList<>();
        Building building = new Building("a", Time.valueOf("10:00:00"), Time.valueOf("20:00:00"));
        Bike bike = new Bike(building);
        bikes.add(bike);
        bike.setId(0);
        when(bikeRepository.findById(0)).thenReturn(java.util.Optional.of(bike));
        when(bikeRepository.findAll()).thenReturn(bikes);
        Building building1 = new Building("a", Time.valueOf("10:00:00"), Time.valueOf("20:00:00"));
        Bike newBike = new Bike(building1);
        bike.setBuilding(newBike.getBuilding());
        List<Bike> result = bikeController.updateBikeById(0, newBike);
        assertEquals(result.get(0), bike);
    }

    @Test
    public void testGetAvailableBikesNumberByBuildingId() {
        Building building = new Building("a", Time.valueOf("10:00:00"), Time.valueOf("20:00:00"));
        building.setId(0);
        Bike bike = new Bike(building);
        List<Bike> bikes = new ArrayList<>();
        bikes.add(bike);
        when(bikeRepository.findAvailableBikesByBuilding_Id(0)).thenReturn(bikes);
        int number = bikeController.getAvailableBikesNumberByBuildingId(0);
        assertEquals(number, bikes.size());

    }

    @Test
    public void testGetBikesWithBuilding() {
        Building building = new Building("a", Time.valueOf("10:00:00"), Time.valueOf("20:00:00"));
        building.setId(0);
        Bike bike = new Bike(building);
        List<Bike> bikes = new ArrayList<>();
        bikes.add(bike);
        when(bikeRepository.findBikesByBuildingIsNotNull()).thenReturn(bikes);
        List<Bike> result = bikeController.getBikesWithBuilding().getBody();
        assertEquals(result.get(0), bike);
    }

    @Test
    public void testDeleteBikeById() {
        List<Bike> bikes = new ArrayList<>();
        Bike bike1 = new Bike();
        Bike bike2 = new Bike();
        bikes.add(bike1);
        bikes.add(bike2);
        when(bikeRepository.findById(0)).thenReturn(java.util.Optional.of(bike1));
        when(bikeRepository.findAll()).thenReturn(bikes);
        bikes.remove(0);
        List<Bike> result = bikeController.deleteBikeById(0);
        assertEquals(result.get(0), bike2);
    }


}
