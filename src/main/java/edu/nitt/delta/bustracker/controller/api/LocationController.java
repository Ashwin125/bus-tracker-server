package edu.nitt.delta.bustracker.controller.api;

import edu.nitt.delta.bustracker.controller.response.LocationListResponse;
import edu.nitt.delta.bustracker.controller.response.LocationResponse;
import edu.nitt.delta.bustracker.model.Location;
import edu.nitt.delta.bustracker.service.LocationService;
import edu.nitt.delta.bustracker.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/location")
public class LocationController {

    @Autowired private LocationService locationService;

    @Autowired private UserService userService;

    @GetMapping
    public ResponseEntity<LocationListResponse> getAllLocation() {
        try {
            List<Location> data = locationService.getAllLocation();

            LocationListResponse res =
                    LocationListResponse.builder().locations(data).message("OK").build();

            return new ResponseEntity<>(res, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(
                    LocationListResponse.builder().message("Something went wrong.").build(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    public ResponseEntity<LocationResponse> updateLocation(
            @RequestBody Location location, Principal principal) {

        try {
            Location updatedLocation =
                    locationService.updateLocation(location, principal.getName());

            LocationResponse res =
                    LocationResponse.builder().location(updatedLocation).message("OK").build();

            return new ResponseEntity<>(res, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(
                    LocationResponse.builder().message("Something went wrong.").build(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/{id}/status")
    public ResponseEntity<Boolean> updateStatue(
            @PathVariable String id, @RequestBody Location location, Principal principal) {
        Boolean isOccupied = location.getIsOccupied();
        String driverId = userService.getDriverId(principal.getName());
        location = locationService.updateStatus(id, driverId, isOccupied);

        if (location == null) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(isOccupied, HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<LocationResponse> deleteLocation(
            @RequestBody Location location, Principal principal) {

        try {
            if (locationService.deleteLocation(location, principal.getName())) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(
                        LocationResponse.builder().message("Not found").build(),
                        HttpStatus.NOT_FOUND);
            }

        } catch (Exception e) {
            return new ResponseEntity<>(
                    LocationResponse.builder().message("Something went wrong.").build(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
