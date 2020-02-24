package appl.test.monitoring.services;

import appl.test.monitoring.controllers.MonitoringController;
import appl.test.monitoring.exceptions.NotFoundException;
import appl.test.monitoring.models.MonitoredEndpoint;
import appl.test.monitoring.models.MonitoringResult;
import appl.test.monitoring.models.User;
import appl.test.monitoring.repositories.MonitoredEndpointRepository;
import appl.test.monitoring.repositories.MonitoringResultRepository;
import appl.test.monitoring.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.*;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class MonitoringServiceImpl implements MonitoringService {
    private final MonitoredEndpointRepository monitoredEndpointRepository;
    private final MonitoringResultRepository monitoringResultRepository;
    private final UserRepository userRepository;
    Logger logger = LoggerFactory.getLogger(MonitoringController.class);

    public MonitoringServiceImpl(MonitoredEndpointRepository monitoredEndpointRepository, MonitoringResultRepository monitoringResultRepository, UserRepository userRepository) {
        this.monitoredEndpointRepository = monitoredEndpointRepository;
        this.monitoringResultRepository = monitoringResultRepository;
        this.userRepository = userRepository;
    }

    @Override
    public MonitoredEndpoint findEndpointById(long id, String accessToken) {
        Optional<MonitoredEndpoint> endpoint = monitoredEndpointRepository.findByMonitoredEndpointIdAndOwner_AccessToken(id, accessToken);
        if (endpoint.isPresent()) {
            return endpoint.get();
        } else {
            throw new NotFoundException("Endpoint with id \"" + id + "\" has not been found.");
        }
    }

    @Override
    public List<MonitoredEndpoint> findAllEndpoints(String accessToken) {
        return monitoredEndpointRepository.findAllByOwner_AccessToken(accessToken);
    }

    @Override
    public MonitoredEndpoint createEndpoint(MonitoredEndpoint endpoint, String accessToken) {
        User user = userRepository.findByAccessToken(accessToken);
        endpoint.setOwner(user);
        monitoredEndpointRepository.save(endpoint);
        return endpoint;
    }

    @Override
    public MonitoredEndpoint updateEndpoint(long id, MonitoredEndpoint endpoint, String accessToken) {
        Optional<MonitoredEndpoint> oldEndpointOpt = monitoredEndpointRepository.findByMonitoredEndpointIdAndOwner_AccessToken(id, accessToken);
        if (oldEndpointOpt.isPresent()) {
            MonitoredEndpoint oldEndpoint = oldEndpointOpt.get();
            oldEndpoint.setMonitoredInterval(endpoint.getMonitoredInterval());
            oldEndpoint.setName(endpoint.getName());
            oldEndpoint.setUrl(endpoint.getUrl());
            return oldEndpoint;
        } else {
            throw new NotFoundException("Endpoint with id \"" + id + "\" has not been found.");
        }
    }

    @Override
    public void deleteEndpoint(long id, String accessToken) {
        Optional<MonitoredEndpoint> endpoint = monitoredEndpointRepository.findByMonitoredEndpointIdAndOwner_AccessToken(id, accessToken);
        if (endpoint.isPresent()) {
            monitoredEndpointRepository.deleteById(id);
        } else {
            throw new NotFoundException("Endpoint with id \"" + id + "\" has not been found.");
        }
    }

    @Override
    public List<MonitoringResult> findResultsForEndpoint(long endpointId, String accessToken) {
        Pageable lastTen = PageRequest.of(0, 10, Sort.by("dateOfCheck").descending());
        return monitoringResultRepository.findAllByMonitoredEndpoint_MonitoredEndpointIdAndMonitoredEndpoint_Owner_AccessToken
                (endpointId, accessToken, lastTen);
    }

    @Scheduled(fixedRate = 1000)
    private void monitorEndpoints() {
        List<MonitoredEndpoint> endpoints = monitoredEndpointRepository.findAll();
        Date now = new Date();
        endpoints.forEach(e -> {
            try {
                checkEndpoint(e, now);
            } catch (IOException ex) {
                logger.error("An error occurred during endpoint checking: " + ex.getMessage());
            }
        });
        monitoredEndpointRepository.saveAll(endpoints);
    }



    private void checkEndpoint(MonitoredEndpoint endpoint, Date currentDate) throws IOException {
        Date lastCheck = endpoint.getDateOfLastCheck();
        if (lastCheck == null || currentDate.getTime() - lastCheck.getTime() >= endpoint.getMonitoredInterval() * 1000) {
            endpoint.setDateOfLastCheck(currentDate);

//            HttpURLConnection.setFollowRedirects(false);
            HttpURLConnection connection = (HttpURLConnection) new URL(endpoint.getUrl()).openConnection();
            int responseCode = connection.getResponseCode();
            InputStream inputStream;
            if (200 <= responseCode && responseCode <= 299) {
                inputStream = connection.getInputStream();
            } else {
                inputStream = connection.getErrorStream();
            }

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(
                            inputStream));
            StringBuilder response = new StringBuilder();
            String currentLine;
            while ((currentLine = in.readLine()) != null)
                response.append(currentLine);
            in.close();

            logger.debug(response.toString());

            MonitoringResult newResult = new MonitoringResult(currentDate, responseCode, response.toString(), endpoint);
            monitoringResultRepository.save(newResult);
        }
    }


}
