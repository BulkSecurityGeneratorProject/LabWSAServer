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
 * A Agenttype.
 */
@Entity
@Table(name = "agenttype")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Agenttype implements Serializable {

    public Agenttype(){}

    public Agenttype(String agentTypeName){
        this.agentTypeName = agentTypeName;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;


    @Column(name = "agent_type_name")
    private String agentTypeName;

    @OneToMany(mappedBy = "agent_agenttype")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Agent> agenttype_agents = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAgentTypeName() {
        return agentTypeName;
    }

    public void setAgentTypeName(String agentTypeName) {
        this.agentTypeName = agentTypeName;
    }

    public Set<Agent> getAgenttype_agents() {
        return agenttype_agents;
    }

    public void setAgenttype_agents(Set<Agent> agents) {
        this.agenttype_agents = agents;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Agenttype agenttype = (Agenttype) o;

        if ( ! Objects.equals(id, agenttype.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Agenttype{" +
                "id=" + id +
                ", agentTypeName='" + agentTypeName + "'" +
                '}';
    }
}
