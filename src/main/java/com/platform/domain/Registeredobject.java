package com.platform.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
 * A Registeredobject.
 */
@Entity
@Table(name = "registeredobject")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Registeredobject implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;


    @Column(name = "object_name")
    private String objectName;

    @Column(name = "object_description")
    private String objectDescription;

    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @JsonSerialize(using = CustomDateTimeSerializer.class)
    @JsonDeserialize(using = CustomDateTimeDeserializer.class)
    @Column(name = "registration_time")
    private DateTime registrationTime;

    @ManyToOne
    private Agent object_agent;

    @OneToMany(mappedBy = "assessment_object")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Assessment> object_assessments = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    public String getObjectDescription() {
        return objectDescription;
    }

    public void setObjectDescription(String objectDescription) {
        this.objectDescription = objectDescription;
    }

    public DateTime getRegistrationTime() {
        return registrationTime;
    }

    public void setRegistrationTime(DateTime registrationTime) {
        this.registrationTime = registrationTime;
    }

    public Agent getObject_agent() {
        return object_agent;
    }

    public void setObject_agent(Agent agent) {
        this.object_agent = agent;
    }

    public Set<Assessment> getObject_assessments() {
        return object_assessments;
    }

    public void setObject_assessments(Set<Assessment> assessments) {
        this.object_assessments = assessments;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Registeredobject registeredobject = (Registeredobject) o;

        if ( ! Objects.equals(id, registeredobject.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Registeredobject{" +
                "id=" + id +
                ", objectName='" + objectName + "'" +
                ", objectDescription='" + objectDescription + "'" +
                ", registrationTime='" + registrationTime + "'" +
                '}';
    }

    public Registeredobject(){}

    public Registeredobject(String objectName, String objectDescription, Agent sender, DateTime time){
        this.objectName = objectName;
        this.objectDescription = objectDescription;
        this.object_agent = sender;
        this.registrationTime = time;
    }
}
