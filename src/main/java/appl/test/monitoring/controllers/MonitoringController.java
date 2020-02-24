package appl.test.monitoring.controllers;

import appl.test.monitoring.models.MonitoredEndpoint;
import appl.test.monitoring.models.MonitoringResult;
import appl.test.monitoring.models.User;
import appl.test.monitoring.repositories.UserRepository;
import appl.test.monitoring.services.MonitoringService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class MonitoringController {
    private final MonitoringService monitoringService;
    private final UserRepository userRepository;

    @Autowired
    public MonitoringController(MonitoringService monitoringService, UserRepository userRepository) {
        this.monitoringService = monitoringService;
        this.userRepository = userRepository;
    }



    @GetMapping("/endpoints")
    public List<MonitoredEndpoint> findAllEndpoints(@RequestHeader(value="accessToken") String accessToken) {
        return monitoringService.findAllEndpoints(accessToken);
    }

    @GetMapping("/endpoints/{id}")
    public MonitoredEndpoint findEndpointById(@RequestHeader(value="accessToken") String accessToken, @PathVariable Long id) {
        return monitoringService.findEndpointById(id, accessToken);
    }

    @PostMapping("/endpoints")
    public MonitoredEndpoint createEndpoint(@RequestHeader(value="accessToken") String accessToken, @Valid @RequestBody MonitoredEndpoint endpoint) {
        User owner = userRepository.findByAccessToken(accessToken);
        owner.getEndpoints().add(endpoint);
        endpoint.setOwner(owner);
        return monitoringService.createEndpoint(endpoint, accessToken);
    }

    @PutMapping("/endpoints/{id}")
    public MonitoredEndpoint updateEndpoint(@RequestHeader(value="accessToken") String accessToken, @PathVariable Long id, @Valid @RequestBody MonitoredEndpoint endpoint) {
        return monitoringService.updateEndpoint(id, endpoint, accessToken);
    }

    @DeleteMapping("/endpoints/{id}")
    public void deleteEndpoint(@RequestHeader(value="accessToken") String accessToken, @PathVariable Long id) {
        monitoringService.deleteEndpoint(id, accessToken);
    }

    @GetMapping("/endpoints/{endpointId}/results")
    public List<MonitoringResult> getResultsForEndpoint(@RequestHeader(value="accessToken") String accessToken, @PathVariable Long endpointId) {
        return monitoringService.findResultsForEndpoint(endpointId, accessToken);
    }

    @GetMapping("/users")
    public List<User> getUsers() {
        return userRepository.findAll();
    }


}
