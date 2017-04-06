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
 * A Requesttype.
 */
@Entity
@Table(name = "requesttype")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Requesttype implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    
    @Column(name = "request_type_name")
    private String requestTypeName;

    @OneToMany(mappedBy = "action_requesttype")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Communicativeaction> requesttype_actions = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRequestTypeName() {
        return requestTypeName;
    }

    public void setRequestTypeName(String requestTypeName) {
        this.requestTypeName = requestTypeName;
    }

    public Set<Communicativeaction> getRequesttype_actions() {
        return requesttype_actions;
    }

    public void setRequesttype_actions(Set<Communicativeaction> communicativeactions) {
        this.requesttype_actions = communicativeactions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Requesttype requesttype = (Requesttype) o;

        if ( ! Objects.equals(id, requesttype.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Requesttype{" +
                "id=" + id +
                ", requestTypeName='" + requestTypeName + "'" +
                '}';
    }
}
