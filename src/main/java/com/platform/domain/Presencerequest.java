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
 * A Presencerequest.
 */
@Entity
@Table(name = "presencerequest")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Presencerequest implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;


    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @JsonSerialize(using = CustomDateTimeSerializer.class)
    @JsonDeserialize(using = CustomDateTimeDeserializer.class)
    @Column(name = "presence_request_time")
    private DateTime presenceRequestTime;

    @ManyToOne
    private Presencestatus presencerequest_presencestatus;

    @ManyToOne
    private Agent presencerequest_agent;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DateTime getPresenceRequestTime() {
        return presenceRequestTime;
    }

    public void setPresenceRequestTime(DateTime presenceRequestTime) {
        this.presenceRequestTime = presenceRequestTime;
    }

    public Presencestatus getPresencerequest_presencestatus() {
        return presencerequest_presencestatus;
    }

    public void setPresencerequest_presencestatus(Presencestatus presencestatus) {
        this.presencerequest_presencestatus = presencestatus;
    }

    public Agent getPresencerequest_agent() {
        return presencerequest_agent;
    }

    public void setPresencerequest_agent(Agent agent) {
        this.presencerequest_agent = agent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Presencerequest presencerequest = (Presencerequest) o;

        if ( ! Objects.equals(id, presencerequest.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Presencerequest{" +
                "id=" + id +
                ", presenceRequestTime='" + presenceRequestTime + "'" +
                '}';
    }

    public Presencerequest(){}

    public Presencerequest(Presencestatus status, DateTime time, Agent agent){
        this.presenceRequestTime = time;
        this.presencerequest_presencestatus = status;
        this.presencerequest_agent = agent;
    }
}
