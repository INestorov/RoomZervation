import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import application.controllers.BuildingController;
import application.entities.Building;
import application.repositories.BuildingRepository;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)

public class BuildingControllerTest {
    @InjectMocks
    BuildingController buildingController;

    @Mock
    BuildingRepository buildingRepository;

    @Test
    public void testGetAllBuildings() {
        Building building1 = new Building("a", Time.valueOf("10:00:00"), Time.valueOf("20:00:00"));
        Building building2 = new Building("b", Time.valueOf("10:00:00"), Time.valueOf("20:00:00"));
        List<Building> buildings = new ArrayList<>();
        buildings.add(building1);
        buildings.add(building2);
        when(buildingRepository.findAll()).thenReturn(buildings);
        List<Building> result = buildingController.getAllBuildings().getBody();
        assertEquals(result.size(), 2);
        assertEquals(result.get(0).getName(), "a");
    }

    @Test
    public void testGetBuildingById() {
        Building building = new Building("a", Time.valueOf("10:00:00"), Time.valueOf("20:00:00"));
        building.setId(0);
        when(buildingRepository.findById(0)).thenReturn(java.util.Optional.of(building));
        Building result = buildingController.getBuildingById(0).getBody();
        assertEquals(result, building);
    }

    @Test
    public void testCreate() {
        List<Building> buildings = new ArrayList<>();
        Building building = new Building("a", Time.valueOf("10:00:00"), Time.valueOf("20:00:00"));
        buildings.add(building);
        when(buildingRepository.findAll()).thenReturn(buildings);
        List<Building> result = buildingController.create(building);
        assertEquals(result.get(0), building);
    }

    @Test
    public void testUpdateBuildingById() {
        List<Building> buildings = new ArrayList<>();
        Building building = new Building("a", Time.valueOf("10:00:00"), Time.valueOf("20:00:00"));
        building.setId(0);
        buildings.add(building);
        Building building1 = new Building("b", Time.valueOf("10:00:00"), Time.valueOf("20:00:00"));
        when(buildingRepository.findById(0)).thenReturn(java.util.Optional.of(building));
        when(buildingRepository.findAll()).thenReturn(buildings);
        List<Building> result = buildingController.updateBuildingById(0, building1);
        assertEquals(result.get(0).getName(), "b");
    }

    @Test
    public void testDeleteBuildingById() {
        List<Building> buildings = new ArrayList<>();
        Building building = new Building("a", Time.valueOf("10:00:00"), Time.valueOf("20:00:00"));
        building.setId(0);
        buildings.add(building);
        when(buildingRepository.findById(0)).thenReturn(java.util.Optional.of(building));
        buildings.remove(building);
        when(buildingRepository.findAll()).thenReturn(buildings);
        List<Building> result = buildingController.deleteBuildingById(0);
        assertEquals(result.size(), 0);
    }
}


