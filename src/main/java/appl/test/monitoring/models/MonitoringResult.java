package appl.test.monitoring.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
public class MonitoringResult {
    @Id
    @GeneratedValue(generator = "result_generator")
    @SequenceGenerator(
            name = "result_generator",
            sequenceName = "result_sequence",
            initialValue = 1000
    )
    protected long monitoringResultId;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "checked_at", nullable = false, updatable = false)
    @CreatedDate
    private Date dateOfCheck;

    @Column(name = "returnedStatusCode", nullable = false)
    @Range(min = 100, max = 599)
    private Integer returnedStatusCode;

    @Column(name = "returnedPayload", nullable = false)
    @Lob
    @JsonIgnore
    private String returnedPayload;

    @ManyToOne
    @JoinColumn(name = "monitoredEndpoint_id", nullable = false)
    private MonitoredEndpoint monitoredEndpoint;

    public MonitoringResult() {
    }

    public MonitoringResult(Date dateOfCheck, Integer statusCode, String payload, MonitoredEndpoint endpoint) {
        this.dateOfCheck = dateOfCheck;
        this.returnedStatusCode = statusCode;
        this.returnedPayload = payload;
        this.monitoredEndpoint = endpoint;
    }

    public long getMonitoringResultId() {
        return monitoringResultId;
    }

    public void setMonitoringResultId(long monitoringResultId) {
        this.monitoringResultId = monitoringResultId;
    }

    public Date getDateOfCheck() {
        return dateOfCheck;
    }

    public void setDateOfCheck(Date dateOfCheck) {
        this.dateOfCheck = dateOfCheck;
    }

    public Integer getReturnedStatusCode() {
        return returnedStatusCode;
    }

    public void setReturnedStatusCode(Integer returnedStatusCode) {
        this.returnedStatusCode = returnedStatusCode;
    }

    public String getReturnedPayload() {
        return returnedPayload;
    }

    public void setReturnedPayload(String returnedPayload) {
        this.returnedPayload = returnedPayload;
    }

    public MonitoredEndpoint getMonitoredEndpoint() {
        return monitoredEndpoint;
    }

    public void setMonitoredEndpoint(MonitoredEndpoint monitoredEndpoint) {
        this.monitoredEndpoint = monitoredEndpoint;
    }
}
