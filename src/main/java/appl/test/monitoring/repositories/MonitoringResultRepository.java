package appl.test.monitoring.repositories;

import appl.test.monitoring.models.MonitoringResult;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MonitoringResultRepository extends JpaRepository<MonitoringResult, Long> {
    List<MonitoringResult> findAllByMonitoredEndpoint_MonitoredEndpointIdAndMonitoredEndpoint_Owner_AccessToken
            (Long endpointId, String accessToken, Pageable pageable);
}
