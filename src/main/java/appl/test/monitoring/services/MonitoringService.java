package appl.test.monitoring.services;


import appl.test.monitoring.models.MonitoredEndpoint;
import appl.test.monitoring.models.MonitoringResult;
import appl.test.monitoring.models.User;

import java.util.List;

public interface MonitoringService {
    MonitoredEndpoint findEndpointById(long id, String accessToken);

    List<MonitoredEndpoint> findAllEndpoints(String accessToken);

    MonitoredEndpoint createEndpoint(MonitoredEndpoint endpoint, String accessToken);

    MonitoredEndpoint updateEndpoint(long id, MonitoredEndpoint endpoint, String accessToken);

    void deleteEndpoint(long id, String accessToken);

    List<MonitoringResult> findResultsForEndpoint(long endpointId, String accessToken);
}
