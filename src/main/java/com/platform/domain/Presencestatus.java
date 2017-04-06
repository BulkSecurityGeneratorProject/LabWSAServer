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
 * A Presencestatus.
 */
@Entity
@Table(name = "presencestatus")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Presencestatus implements Serializable {

    public static final String START_PRESENCE = "start_presence";
    public static final String END_PRESENCE = "end_presence";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "presence_status_name")
    private String presenceStatusName;

    @OneToMany(mappedBy = "presencerequest_presencestatus")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Presencerequest> presencestatus_presencerequests = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPresenceStatusName() {
        return presenceStatusName;
    }

    public void setPresenceStatusName(String presenceStatusName) {
        this.presenceStatusName = presenceStatusName;
    }

    public Set<Presencerequest> getPresencestatus_presencerequests() {
        return presencestatus_presencerequests;
    }

    public void setPresencestatus_presencerequests(Set<Presencerequest> presencerequests) {
        this.presencestatus_presencerequests = presencerequests;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Presencestatus presencestatus = (Presencestatus) o;

        if ( ! Objects.equals(id, presencestatus.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Presencestatus{" +
                "id=" + id +
                ", presenceStatusName='" + presenceStatusName + "'" +
                '}';
    }
}
