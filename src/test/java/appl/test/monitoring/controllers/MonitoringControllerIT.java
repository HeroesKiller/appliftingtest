package appl.test.monitoring.controllers;

import appl.test.monitoring.models.MonitoredEndpoint;
import appl.test.monitoring.models.User;
import appl.test.monitoring.repositories.UserRepository;
import appl.test.monitoring.services.MonitoringService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;


@ExtendWith(MockitoExtension.class)
class MonitoringControllerIT {

    @Mock
    private MonitoringService monitoringService;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    MonitoringController monitoringController;

    MockMvc mockMvc;

    List<MonitoredEndpoint> endpoints;
    User user1;
    User user2;
    MonitoredEndpoint endpoint1;
    MonitoredEndpoint endpoint2;
    MonitoredEndpoint endpoint3;

    @BeforeEach
    void setUp() {
        user1 = new User(1000, "Applifting", "info@applifting.cz", "93f39e2f-80de-4033-99ee-249d92736a25");
        user2 = new User(1001, "Batman", "batman@example.com", "dcb20f8a-5657-4f1b-9f7f-ce65739b359e");
        endpoint1 = new MonitoredEndpoint(1000, "Applifting", "https://www.applifting.cz", new Date(), user1, 5);
        endpoint2 = new MonitoredEndpoint(1001, "Google", "https://www.google.com", new Date(), user2, 10);
        endpoint3 = new MonitoredEndpoint(1002, "Twitter", "https://www.twitter.com", new Date(), user1, 15);
        endpoints = new ArrayList<>();
        endpoints.add(endpoint1);
        endpoints.add(endpoint2);
        endpoints.add(endpoint3);

        mockMvc = MockMvcBuilders.standaloneSetup(monitoringController).build();


    }


    @Test
    void findAllEndpoints_noToken_returnsBadRequest() throws Exception {
        mockMvc.perform(get("/endpoints")).andExpect(status().isBadRequest());
    }

    @Test
    void findAllEndpoints_valid_returnsCorrectEndpoints() throws Exception {
        when(monitoringService.findAllEndpoints(user1.getAccessToken())).thenReturn(endpoints.stream().filter(e -> e.getOwner().getAccessToken().equals(user1.getAccessToken())).collect(Collectors.toList()));

        mockMvc.perform(get("/endpoints").header("accessToken",user1.getAccessToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(2)))
                .andExpect(jsonPath("$[0].monitoredEndpointId",Matchers.equalTo(1000)))
                .andExpect(jsonPath("$[1].monitoredEndpointId",Matchers.equalTo(1002)));
    }

    @Test
    void createEndpoint_validBody_returnsOk() throws Exception {
        when(userRepository.findByAccessToken(user1.getAccessToken())).thenReturn(user1);

        mockMvc.perform(post("/endpoints").header("accessToken",user1.getAccessToken()).
                content("{\n" +
                        "\t\"name\": \"Google\",\n" +
                        "\t\"url\": \"https://www.google.cz/\",\n" +
                        "\t\"monitoredInterval\": 3\n" +
                        "}").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void createEndpoint_invalidBody_returnsBadRequest() throws Exception {
        mockMvc.perform(post("/endpoints").header("accessToken",user1.getAccessToken()).
                content("{\n" +
                        "\t\"name\": \"Google\",\n" +
                        "\t\"url\": \"https://www.google.cz/\",\n" +
                        "\t\"monitoredInterval\": -3\n" +
                        "}").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }


    @Test
    void getResultsForEndpoint_validId_returnsOk() throws Exception {
        mockMvc.perform(get("/endpoints/1000/results").header("accessToken",user1.getAccessToken()))
                .andExpect(status().isOk());
    }

    @Test
    void getResultsForEndpoint_invalidId_returnsBadRequest() throws Exception {
        mockMvc.perform(get("/endpoints/0.5/results").header("accessToken",user1.getAccessToken()))
                .andExpect(status().isBadRequest());
    }
}