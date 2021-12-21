package edu.nitt.delta.bustracker.controller.api;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import edu.nitt.delta.bustracker.controller.response.VehicleListResponse;
import edu.nitt.delta.bustracker.controller.response.VehicleResponse;
import edu.nitt.delta.bustracker.model.Vehicle;
import edu.nitt.delta.bustracker.model.VehicleType;
import edu.nitt.delta.bustracker.service.VehicleService;

@RestController
@RequestMapping("/vehicle")
public class VehicleController {

    @Autowired
    private VehicleService vehicleService;

    @GetMapping
    public ResponseEntity<VehicleListResponse> getAllVehicle() {

        try {
            List<Vehicle> data = vehicleService.getAllVehicle();

            VehicleListResponse res = VehicleListResponse
                .builder()
                .vehicles(data)
                .message("OK")
                .build();

            return new ResponseEntity<>(res, HttpStatus.OK);
            
        } catch (Exception e) {
            return new ResponseEntity<>(VehicleListResponse
                .builder()
                .message("Something went wrong.")
                .build(),
                HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @GetMapping("/active")
    public ResponseEntity<VehicleListResponse> getAllActiveVehicles(@RequestParam("VehicleType") Optional<VehicleType> vehicleType) {
        try {
            List<Vehicle> data = vehicleService.getAllActiveVehicles(vehicleType.orElse(null));
            VehicleListResponse res = VehicleListResponse
                .builder()
                .vehicles(data)
                .message("OK")
                .build();

            return new ResponseEntity<>(res, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(VehicleListResponse
                .builder()
                .message("Something went wrong.")
                .build(),
                HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @GetMapping("/inactive")
    public ResponseEntity<VehicleListResponse> getAllInactiveVehicles(@RequestParam("VehicleType") Optional<VehicleType> vehicleType) {
        try {
            List<Vehicle> data = vehicleService.getAllInactiveVehicles(vehicleType.orElse(null));

            VehicleListResponse res = VehicleListResponse
                .builder()
                .vehicles(data)
                .message("OK")
                .build();

            return new ResponseEntity<>(res, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(VehicleListResponse
                .builder()
                .message("Something went wrong.")
                .build(),
                HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<VehicleResponse> getVehicleById(@PathVariable String id) {

        try {
            Vehicle vehicle = vehicleService.getVehicleById(id);

            if (vehicle == null) {
                return new ResponseEntity<>(VehicleResponse
                    .builder()
                    .message("Invalid Id")
                    .build(), 
                    HttpStatus.NOT_FOUND
                );
            }
    
            return new ResponseEntity<>(VehicleResponse
                .builder()
                .vehicle(vehicle)
                .message("OK")
                .build(), 
                HttpStatus.OK
            );

        } catch (Exception e) {
            return new ResponseEntity<>(VehicleResponse
                .builder()
                .message("Something went wrong.")
                .build(),
                HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @PostMapping
    public ResponseEntity<VehicleResponse> insertVehicle(@RequestBody Vehicle vehicle) {

        try {
            Vehicle insertedVehicle = vehicleService.insertVehicle(vehicle);

            VehicleResponse res = VehicleResponse
                .builder()
                .vehicle(insertedVehicle)
                .message("OK")
                .build();

            return new ResponseEntity<>(res, HttpStatus.CREATED);

        } catch (Exception e) {
            return new ResponseEntity<>(VehicleResponse
                .builder()
                .message("Something went wrong.")
                .build(),
                HttpStatus.INTERNAL_SERVER_ERROR    
            );
        }
    }

    @PostMapping("/{id}/status")
    public ResponseEntity<VehicleResponse> changeVehicleStatus(@PathVariable String id, @RequestBody Vehicle vehicle) {
        try {
            Vehicle updatedVehicle = vehicleService.changeVehicleStatus(id, vehicle.getIsOccupied());

            VehicleResponse res = VehicleResponse
                .builder()
                .vehicle(updatedVehicle)
                .message("OK")
                .build();

            return new ResponseEntity<>(res, HttpStatus.OK);

        } catch (Exception e) {

            return new ResponseEntity<>(VehicleResponse
                .builder()
                .message("Something went wrong.")
                .build(),
                HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }
}
