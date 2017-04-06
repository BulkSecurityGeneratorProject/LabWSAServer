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
 * A Interactionprotocol.
 */
@Entity
@Table(name = "interactionprotocol")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Interactionprotocol implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;


    @Column(name = "protocol_name")
    private String protocolName;

    @Column(name = "protocol_description")
    private String protocolDescription;

    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @JsonSerialize(using = CustomDateTimeSerializer.class)
    @JsonDeserialize(using = CustomDateTimeDeserializer.class)
    @Column(name = "opening_time")
    private DateTime openingTime;

    @OneToMany(mappedBy = "action_protocol")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Communicativeaction> protocol_actions = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProtocolName() {
        return protocolName;
    }

    public void setProtocolName(String protocolName) {
        this.protocolName = protocolName;
    }

    public String getProtocolDescription() {
        return protocolDescription;
    }

    public void setProtocolDescription(String protocolDescription) {
        this.protocolDescription = protocolDescription;
    }

    public DateTime getOpeningTime() {
        return openingTime;
    }

    public void setOpeningTime(DateTime openingTime) {
        this.openingTime = openingTime;
    }

    public Set<Communicativeaction> getProtocol_actions() {
        return protocol_actions;
    }

    public void setProtocol_actions(Set<Communicativeaction> communicativeactions) {
        this.protocol_actions = communicativeactions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Interactionprotocol interactionprotocol = (Interactionprotocol) o;

        if ( ! Objects.equals(id, interactionprotocol.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Interactionprotocol{" +
                "id=" + id +
                ", protocolName='" + protocolName + "'" +
                ", protocolDescription='" + protocolDescription + "'" +
                ", openingTime='" + openingTime + "'" +
                '}';
    }

    public Interactionprotocol(){}

    public Interactionprotocol(String protocolName, String protocolDescription, Agent agent, DateTime time){
        this.protocolName = protocolName;
        this.protocolDescription = protocolDescription;
        this.openingTime = time;
    }
}
