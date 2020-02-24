package appl.test.monitoring.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.validator.constraints.URL;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.Positive;
import java.util.Date;
import java.util.List;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class MonitoredEndpoint {
    @Id
    @GeneratedValue(generator = "endpoint_generator")
    @SequenceGenerator(
            name = "endpoint_generator",
            sequenceName = "endpoint_sequence",
            initialValue = 1000
    )
    protected long monitoredEndpointId;

    @Column(name = "name")
    private String name;

    @Column(name = "url", nullable = false)
    @URL
    private String url;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false, updatable = false)
    @CreatedDate
    private Date dateOfCreation;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "lastchecked_at")
    private Date dateOfLastCheck;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User owner;

    @OneToMany(mappedBy = "monitoredEndpoint", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<MonitoringResult> monitoringResults;

    @Column(name = "monitored_interval", nullable = false)
    @Positive
    private int monitoredInterval;

    public MonitoredEndpoint() {
    }

    public MonitoredEndpoint(long id, String name, String url, Date dateOfCreation, User owner, int monitoredInterval) {
        this.monitoredEndpointId = id;
        this.name = name;
        this.url = url;
        this.dateOfCreation = dateOfCreation;
        this.owner = owner;
        this.monitoredInterval = monitoredInterval;
    }

    public long getMonitoredEndpointId() {
        return monitoredEndpointId;
    }

    public void setMonitoredEndpointId(long monitoredEndpointId) {
        this.monitoredEndpointId = monitoredEndpointId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Date getDateOfCreation() {
        return dateOfCreation;
    }

    public void setDateOfCreation(Date dateOfCreation) {
        this.dateOfCreation = dateOfCreation;
    }

    public Date getDateOfLastCheck() {
        return dateOfLastCheck;
    }

    public void setDateOfLastCheck(Date dateOfLastCheck) {
        this.dateOfLastCheck = dateOfLastCheck;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public List<MonitoringResult> getMonitoringResults() {
        return monitoringResults;
    }

    public void setMonitoringResults(List<MonitoringResult> monitoringResults) {
        this.monitoringResults = monitoringResults;
    }

    public int getMonitoredInterval() {
        return monitoredInterval;
    }

    public void setMonitoredInterval(int monitoredInterval) {
        this.monitoredInterval = monitoredInterval;
    }
}
