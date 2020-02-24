package appl.test.monitoring.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.List;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;

    @Column(name = "username")
    private String username;

    @Column(name = "email")
    @Email
    private String email;

    @Column(name = "accessToken")
    @Pattern(regexp="^[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$")
    private String accessToken;

    @OneToMany(mappedBy = "owner")
    @JsonIgnore
    private List<MonitoredEndpoint> endpoints;

    public User (String username, String email, String accessToken) {
        this.username = username;
        this.email = email;
        this.accessToken = accessToken;
    }

    public User (int id, String username, String email, String accessToken) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.accessToken = accessToken;
        this.endpoints = new ArrayList<>();
    }

    public User() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public List<MonitoredEndpoint> getEndpoints() {
        return endpoints;
    }

    public void setEndpoints(List<MonitoredEndpoint> endpoints) {
        this.endpoints = endpoints;
    }
}
