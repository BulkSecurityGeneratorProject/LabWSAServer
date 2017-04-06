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
 * A Sensortype.
 */
@Entity
@Table(name = "sensortype")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Sensortype implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;


    @Column(name = "sensor_type_name")
    private String sensorTypeName;

    @OneToMany(mappedBy = "sensor_sensortype")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Sensor> sensortype_sensors = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSensorTypeName() {
        return sensorTypeName;
    }

    public void setSensorTypeName(String sensorTypeName) {
        this.sensorTypeName = sensorTypeName;
    }

    public Set<Sensor> getSensortype_sensors() {
        return sensortype_sensors;
    }

    public void setSensortype_sensors(Set<Sensor> sensors) {
        this.sensortype_sensors = sensors;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Sensortype sensortype = (Sensortype) o;

        if ( ! Objects.equals(id, sensortype.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Sensortype{" +
                "id=" + id +
                ", sensorTypeName='" + sensorTypeName + "'" +
                '}';
    }

    public Sensortype(){}

    public Sensortype(String sensorTypeName){
        this.sensorTypeName = sensorTypeName;
    }
}
