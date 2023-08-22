import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import application.controllers.BikeRentalController;
import application.entities.Bike;
import application.entities.BikeRental;
import application.entities.User;
import application.repositories.BikeRentalRepository;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class BikeRentalControllerTest {

    @InjectMocks
    BikeRentalController bikeRentalController;

    @Mock
    BikeRentalRepository bikeRentalRepository;

    @Test
    public void testGetAllBikes() {
        Bike bike = new Bike();
        User user = new User();
        BikeRental bikeRental = new BikeRental(bike,user,
            Timestamp.valueOf("2019-11-11 14:00:00"),Timestamp.valueOf("2019-11-11 15:00:00"));
        List<BikeRental> bikeRentals = new ArrayList<>();
        bikeRentals.add(bikeRental);
        when(bikeRentalRepository.findAll()).thenReturn(bikeRentals);
        List<BikeRental> result = bikeRentalController.getAllBikeRentals().getBody();
        assertEquals(result.size(), 1);
    }

    @Test
    public void testGetBikeRentalById() {
        Bike bike = new Bike();
        User user = new User();
        BikeRental bikeRental = new BikeRental(bike,user,
            Timestamp.valueOf("2019-11-11 14:00:00"),Timestamp.valueOf("2019-11-11 15:00:00"));
        bikeRental.setId(0);
        when(bikeRentalRepository.findById(0)).thenReturn(java.util.Optional.of(bikeRental));
        BikeRental result = bikeRentalController.getBikeRentalById(0).getBody();
        assertEquals(result, bikeRental);
    }

    @Test
    public void testGetBikeRentalsByBikeId() {
        Bike bike = new Bike();
        bike.setId(0);
        User user = new User();
        BikeRental bikeRental = new BikeRental(bike,user,
            Timestamp.valueOf("2019-11-11 14:00:00"),Timestamp.valueOf("2019-11-11 15:00:00"));
        List<BikeRental> bikeRentals = new ArrayList<>();
        bikeRentals.add(bikeRental);
        when(bikeRentalRepository.findBikeRentalsByBike_Id(0)).thenReturn(bikeRentals);
        List<BikeRental> result = bikeRentalController.getBikeRentalsByBikeId(0).getBody();
        assertEquals(result.get(0), bikeRental);
    }

    @Test
    public void testCreate() {
        Bike bike = new Bike();
        User user = new User();
        BikeRental bikeRental = new BikeRental(bike,user,
            Timestamp.valueOf("2019-11-11 14:00:00"),Timestamp.valueOf("2019-11-11 15:00:00"));
        List<BikeRental> bikeRentals = new ArrayList<>();
        bikeRentals.add(bikeRental);
        when(bikeRentalRepository.findAll()).thenReturn(bikeRentals);
        List<BikeRental> result = bikeRentalController.create(bikeRental);
        assertEquals(result.get(0), bikeRental);
    }




    @Test
    public void testUpdateBikeById() {
        Bike bike = new Bike();
        User user = new User();
        BikeRental bikeRental = new BikeRental(bike,user,
            Timestamp.valueOf("2019-11-11 14:00:00"),Timestamp.valueOf("2019-11-11 15:00:00"));
        bikeRental.setId(0);
        List<BikeRental> bikeRentals = new ArrayList<>();
        bikeRentals.add(bikeRental);
        when(bikeRentalRepository.findById(0)).thenReturn(java.util.Optional.of(bikeRental));
        when(bikeRentalRepository.findAll()).thenReturn(bikeRentals);
        BikeRental bikeRental1 = new BikeRental(bike,user,
            Timestamp.valueOf("2019-11-11 15:00:00"),Timestamp.valueOf("2019-11-11 16:00:00"));
        bikeRental.setStart(bikeRental1.getStart());
        bikeRental.setEnd(bikeRental1.getEnd());
        List<BikeRental> result = bikeRentalController.updateBikeRentalById(0,bikeRental1);
        assertEquals(result.get(0), bikeRental);
    }


}
