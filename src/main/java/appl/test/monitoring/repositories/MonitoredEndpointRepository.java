package appl.test.monitoring.repositories;

import appl.test.monitoring.models.MonitoredEndpoint;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MonitoredEndpointRepository extends JpaRepository<MonitoredEndpoint, Long> {
//    List<MonitoredEndpoint> findAllByOwner_AccessToken(String accessToken, Pageable pageable);
    List<MonitoredEndpoint> findAllByOwner_AccessToken(String accessToken);
    Optional<MonitoredEndpoint> findByMonitoredEndpointIdAndOwner_AccessToken(Long id, String accessToken);
}
