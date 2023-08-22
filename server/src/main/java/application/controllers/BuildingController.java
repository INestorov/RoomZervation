package application.controllers;

import application.entities.Building;
import application.repositories.BuildingRepository;
import java.util.ArrayList;
import java.util.List;
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
@RequestMapping("/buildings")
public class BuildingController {

    @Autowired
    BuildingRepository repository;


    /**
     * GET endpoint for reading all buildings.
     *
     * @return a list of all buildings
     */
    @GetMapping("/read")
    public ResponseEntity<List<Building>> getAllBuildings() {
        List<Building> list = repository.findAll();
        if (list.size() > 0) {
            return new ResponseEntity<>(list, new HttpHeaders(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ArrayList<>(), new HttpHeaders(), HttpStatus.OK);
        }
    }

    /**
     * GET endpoint for reading a specific building.
     *
     * @return one http response containing a building
     */
    @GetMapping("/read/{id}")
    public ResponseEntity<Building> getBuildingById(@PathVariable("id") Integer id)
        throws IllegalArgumentException {
        Optional<Building> building = repository.findById(id);
        if (building.isPresent()) {
            return new ResponseEntity<>(building.get(), new HttpHeaders(), HttpStatus.OK);
        } else {
            throw new IllegalArgumentException("No building exists");
        }
    }

    /**
     * POST Endpoint to create buildings.
     *
     * @return buildings
     */
    @PostMapping("/post")
    public List<Building> create(@RequestBody final Building building) {
        repository.save(building);
        return repository.findAll();
    }


    /**
     * PUT Endpoint to update buildings.
     *
     * @return buildings
     */
    @PutMapping("update/{id}")
    public List<Building> updateBuildingById(@PathVariable("id") Integer id,
                                             @RequestBody final Building entity)
        throws IllegalArgumentException {
        Optional<Building> building = repository.findById(id);
        if (building.isPresent()) {
            Building newEntity = building.get();
            newEntity.setName(entity.getName());
            newEntity.setOpeningTime(entity.getOpeningTime());
            newEntity.setClosingTime(entity.getClosingTime());

            repository.save(newEntity);

            return repository.findAll();
        } else {
            throw new IllegalArgumentException("No building exists");
        }
    }

    /**
     * DELETE Endpoint to delete building by id.
     *
     * @return buildings
     */
    @Cascade(CascadeType.ALL)
    @DeleteMapping("delete/{id}")
    public List<Building> deleteBuildingById(@PathVariable("id") Integer id)
        throws IllegalArgumentException {
        Optional<Building> building = repository.findById(id);

        if (building.isPresent()) {
            repository.deleteById(id);
            return repository.findAll();
        } else {
            throw new IllegalArgumentException("No building exists");
        }
    }


}

