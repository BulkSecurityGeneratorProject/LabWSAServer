package com.platform.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.platform.domain.util.CustomDateTimeDeserializer;
import com.platform.domain.util.CustomDateTimeSerializer;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Readout.
 */
@Entity
@Table(name = "readout")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Readout implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;


    @Column(name = "readout_value")
    private Double readoutValue;

    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @JsonSerialize(using = CustomDateTimeSerializer.class)
    @JsonDeserialize(using = CustomDateTimeDeserializer.class)
    @Column(name = "readout_time")
    private DateTime readoutTime;

    @ManyToOne
    private Sensor readout_sensor;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getReadoutValue() {
        return readoutValue;
    }

    public void setReadoutValue(Double readoutValue) {
        this.readoutValue = readoutValue;
    }

    public DateTime getReadoutTime() {
        return readoutTime;
    }

    public void setReadoutTime(DateTime readoutTime) {
        this.readoutTime = readoutTime;
    }

    public Sensor getReadout_sensor() {
        return readout_sensor;
    }

    public void setReadout_sensor(Sensor sensor) {
        this.readout_sensor = sensor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Readout readout = (Readout) o;

        if ( ! Objects.equals(id, readout.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Readout{" +
                "id=" + id +
                ", readoutValue='" + readoutValue + "'" +
                ", readoutTime='" + readoutTime + "'" +
                '}';
    }

    public Readout(){}

    public Readout(Double readoutValue, DateTime time, Sensor sensor){
        this.readoutValue = readoutValue;
        this.readoutTime = time;
        this.readout_sensor = sensor;
    }
}
