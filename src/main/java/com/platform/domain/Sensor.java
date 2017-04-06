package com.platform.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Sensor.
 */
@Entity
@Table(name = "sensor")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Sensor implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;


    @Column(name = "sensor_name")
    private String sensorName;

    @Column(name = "sensor_accuracy")
    private Double sensorAccuracy;

    @ManyToOne
    private Agent sensor_agent;

    @OneToMany(mappedBy = "readout_sensor")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Readout> sensor_readouts = new HashSet<>();

    @ManyToOne
    private Unittype sensor_unittype;

    @ManyToOne
    private Sensortype sensor_sensortype;

    @OneToMany(mappedBy = "graphicalreadout_sensor")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Graphicalreadout> sensor_graphicalreadouts = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSensorName() {
        return sensorName;
    }

    public void setSensorName(String sensorName) {
        this.sensorName = sensorName;
    }

    public Double getSensorAccuracy() {
        return sensorAccuracy;
    }

    public void setSensorAccuracy(Double sensorAccuracy) {
        this.sensorAccuracy = sensorAccuracy;
    }

    public Agent getSensor_agent() {
        return sensor_agent;
    }

    public void setSensor_agent(Agent agent) {
        this.sensor_agent = agent;
    }

    public Set<Readout> getSensor_readouts() {
        return sensor_readouts;
    }

    public void setSensor_readouts(Set<Readout> readouts) {
        this.sensor_readouts = readouts;
    }

    public Unittype getSensor_unittype() {
        return sensor_unittype;
    }

    public void setSensor_unittype(Unittype unittype) {
        this.sensor_unittype = unittype;
    }

    public Sensortype getSensor_sensortype() {
        return sensor_sensortype;
    }

    public void setSensor_sensortype(Sensortype sensortype) {
        this.sensor_sensortype = sensortype;
    }

    public Set<Graphicalreadout> getSensor_graphicalreadouts() {
        return sensor_graphicalreadouts;
    }

    public void setSensor_graphicalreadouts(Set<Graphicalreadout> sensor_graphicalreadouts) {
        this.sensor_graphicalreadouts = sensor_graphicalreadouts;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Sensor sensor = (Sensor) o;

        if ( ! Objects.equals(id, sensor.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Sensor{" +
                "id=" + id +
                ", sensorName='" + sensorName + "'" +
                ", sensorAccuracy='" + sensorAccuracy + "'" +
                '}';
    }

    public Sensor(){}

    public Sensor(String sensorName, Double sensorAccuracy, Unittype unittype, Sensortype sensortype){
        this.sensorName = sensorName;
        this.sensorAccuracy = sensorAccuracy;
        this.sensor_sensortype = sensortype;
        this.sensor_unittype = unittype;
    }
}
