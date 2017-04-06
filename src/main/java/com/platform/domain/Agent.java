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
 * A Agent.
 */
@Entity
@Table(name = "agent")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Agent implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;


    @Column(name = "agent_name")
    private String agentName;

    @Column(name = "agent_description")
    private String agentDescription;

    @OneToMany(mappedBy = "sensor_agent")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Sensor> agent_sensors = new HashSet<>();

    @OneToMany(mappedBy = "location_agent")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Location> agent_locations = new HashSet<>();

    @OneToMany(mappedBy = "action_receiver")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Communicativeaction> receiver_actions = new HashSet<>();

    @OneToMany(mappedBy = "action_sender")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Communicativeaction> sender_actions = new HashSet<>();

    @OneToMany(mappedBy = "presencerequest_agent")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Presencerequest> agent_presencerequests = new HashSet<>();

    @OneToMany(mappedBy = "feature_agent")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Feature> agent_features = new HashSet<>();

    @OneToMany(mappedBy = "object_agent")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Registeredobject> agent_objects = new HashSet<>();

    @OneToMany(mappedBy = "assessment_agent")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Assessment> agent_assessments = new HashSet<>();

    @ManyToOne
    private Agenttype agent_agenttype;

    public Set<Presencerequest> getAgent_presencerequests() {
        return agent_presencerequests;
    }

    public void setAgent_presencerequests(Set<Presencerequest> agent_presencerequests) {
        this.agent_presencerequests = agent_presencerequests;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public String getAgentDescription() {
        return agentDescription;
    }

    public void setAgentDescription(String agentDescription) {
        this.agentDescription = agentDescription;
    }

    public Set<Sensor> getAgent_sensors() {
        return agent_sensors;
    }

    public void setAgent_sensors(Set<Sensor> sensors) {
        this.agent_sensors = sensors;
    }

    public Set<Location> getAgent_locations() {
        return agent_locations;
    }

    public void setAgent_locations(Set<Location> locations) {
        this.agent_locations = locations;
    }

    public Set<Communicativeaction> getReceiver_actions() {
        return receiver_actions;
    }

    public void setReceiver_actions(Set<Communicativeaction> communicativeactions) {
        this.receiver_actions = communicativeactions;
    }

    public Set<Communicativeaction> getSender_actions() {
        return sender_actions;
    }

    public void setSender_actions(Set<Communicativeaction> communicativeactions) {
        this.sender_actions = communicativeactions;
    }

    public Agenttype getAgent_agenttype() {
        return agent_agenttype;
    }

    public void setAgent_agenttype(Agenttype agenttype) {
        this.agent_agenttype = agenttype;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Agent agent = (Agent) o;

        if ( ! Objects.equals(id, agent.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Agent{" +
                "id=" + id +
                ", agentName='" + agentName + "'" +
                ", agentDescription='" + agentDescription + "'" +
                '}';
    }

    public Agent(){}

    public Agent(String agentName, String agentDescription, Agenttype agenttype){
        this.agentName = agentName;
        this.agentDescription = agentDescription;
        this.agent_agenttype = agenttype;
    }

    public Agent(String agentName, String agentDescription, Agenttype agenttype, Set<Sensor> sensors){
        this.agentName = agentName;
        this.agentDescription = agentDescription;
        this.agent_agenttype = agenttype;
        this.agent_sensors = sensors;
    }
}
