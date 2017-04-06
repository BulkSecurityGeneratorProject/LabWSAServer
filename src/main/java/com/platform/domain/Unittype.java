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
 * A Unittype.
 */
@Entity
@Table(name = "unittype")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Unittype implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;


    @Column(name = "unit_type_name")
    private String unitTypeName;

    @OneToMany(mappedBy = "sensor_unittype")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Sensor> unittype_sensors = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUnitTypeName() {
        return unitTypeName;
    }

    public void setUnitTypeName(String unitTypeName) {
        this.unitTypeName = unitTypeName;
    }

    public Set<Sensor> getUnittype_sensors() {
        return unittype_sensors;
    }

    public void setUnittype_sensors(Set<Sensor> sensors) {
        this.unittype_sensors = sensors;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Unittype unittype = (Unittype) o;

        if ( ! Objects.equals(id, unittype.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Unittype{" +
                "id=" + id +
                ", unitTypeName='" + unitTypeName + "'" +
                '}';
    }

    public Unittype(){}

    public Unittype(String unitTypeName){
        this.unitTypeName = unitTypeName;
    }
}
