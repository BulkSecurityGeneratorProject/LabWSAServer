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
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Communicativeaction.
 */
@Entity
@Table(name = "communicativeaction")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Communicativeaction implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;


    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @JsonSerialize(using = CustomDateTimeSerializer.class)
    @JsonDeserialize(using = CustomDateTimeDeserializer.class)
    @Column(name = "action_time")
    private DateTime actionTime;

    @Column(name = "content")
    private String content;

    @Column(name = "language")
    private String language;

    @ManyToOne
    private Interactionprotocol action_protocol;

    @ManyToOne
    private Requesttype action_requesttype;

    @ManyToOne
    private Agent action_receiver;

    @ManyToOne
    private Agent action_sender;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DateTime getActionTime() {
        return actionTime;
    }

    public void setActionTime(DateTime actionTime) {
        this.actionTime = actionTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Interactionprotocol getAction_protocol() {
        return action_protocol;
    }

    public void setAction_protocol(Interactionprotocol interactionprotocol) {
        this.action_protocol = interactionprotocol;
    }

    public Requesttype getAction_requesttype() {
        return action_requesttype;
    }

    public void setAction_requesttype(Requesttype requesttype) {
        this.action_requesttype = requesttype;
    }

    public Agent getAction_receiver() {
        return action_receiver;
    }

    public void setAction_receiver(Agent agent) {
        this.action_receiver = agent;
    }

    public Agent getAction_sender() {
        return action_sender;
    }

    public void setAction_sender(Agent agent) {
        this.action_sender = agent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Communicativeaction communicativeaction = (Communicativeaction) o;

        if ( ! Objects.equals(id, communicativeaction.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Communicativeaction{" +
                "id=" + id +
                ", actionTime='" + actionTime + "'" +
                ", content='" + content + "'" +
                ", language='" + language + "'" +
                '}';
    }

    public Communicativeaction(){}

    public Communicativeaction(DateTime actionTime, String content, String language, Interactionprotocol action_protocol, Requesttype action_requesttype, Agent action_receiver, Agent action_sender) {
        this.actionTime = actionTime;
        this.content = content;
        this.language = language;
        this.action_protocol = action_protocol;
        this.action_requesttype = action_requesttype;
        this.action_receiver = action_receiver;
        this.action_sender = action_sender;
    }
}
