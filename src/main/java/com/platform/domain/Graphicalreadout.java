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
 * A Graphicalreadout.
 */
@Entity
@Table(name = "graphicalreadout")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Graphicalreadout implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;


    @Lob
    @Column(name = "image")
    private byte[] image;

    @Column(name = "image_content_type")
    private String imageContentType;

    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @JsonSerialize(using = CustomDateTimeSerializer.class)
    @JsonDeserialize(using = CustomDateTimeDeserializer.class)
    @Column(name = "readout_time")
    private DateTime readoutTime;

    @ManyToOne
    private Sensor graphicalreadout_sensor;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getImageContentType() {
        return imageContentType;
    }

    public void setImageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
    }

    public DateTime getReadoutTime() {
        return readoutTime;
    }

    public void setReadoutTime(DateTime readoutTime) {
        this.readoutTime = readoutTime;
    }

    public Sensor getGraphicalreadout_sensor() {
        return graphicalreadout_sensor;
    }

    public void setGraphicalreadout_sensor(Sensor sensor) {
        this.graphicalreadout_sensor = sensor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Graphicalreadout graphicalreadout = (Graphicalreadout) o;

        if ( ! Objects.equals(id, graphicalreadout.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Graphicalreadout{" +
                "id=" + id +
                ", image='" + image + "'" +
                ", imageContentType='" + imageContentType + "'" +
                ", readoutTime='" + readoutTime + "'" +
                '}';
    }

    public Graphicalreadout(){}

    public Graphicalreadout(byte[] image, String imageContentType, DateTime readoutTime, Sensor graphicalreadout_sensor) {
        this.image = image;
        this.imageContentType = imageContentType;
        this.readoutTime = readoutTime;
        this.graphicalreadout_sensor = graphicalreadout_sensor;
    }
}
