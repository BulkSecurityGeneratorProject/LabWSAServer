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
 * A Assessment.
 */
@Entity
@Table(name = "assessment")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Assessment implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;


    @Column(name = "assessment_value")
    private Double assessmentValue;

    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @JsonSerialize(using = CustomDateTimeSerializer.class)
    @JsonDeserialize(using = CustomDateTimeDeserializer.class)
    @Column(name = "estimation_time")
    private DateTime estimationTime;

    @ManyToOne
    private Registeredobject assessment_object;

    @ManyToOne
    private Feature assessment_feature;

    @ManyToOne
    private Agent assessment_agent;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getAssessmentValue() {
        return assessmentValue;
    }

    public void setAssessmentValue(Double assessmentValue) {
        this.assessmentValue = assessmentValue;
    }

    public DateTime getEstimationTime() {
        return estimationTime;
    }

    public void setEstimationTime(DateTime estimationTime) {
        this.estimationTime = estimationTime;
    }

    public Registeredobject getAssessment_object() {
        return assessment_object;
    }

    public void setAssessment_object(Registeredobject registeredobject) {
        this.assessment_object = registeredobject;
    }

    public Feature getAssessment_feature() {
        return assessment_feature;
    }

    public void setAssessment_feature(Feature feature) {
        this.assessment_feature = feature;
    }

    public Agent getAssessment_agent() {
        return assessment_agent;
    }

    public void setAssessment_agent(Agent agent) {
        this.assessment_agent = agent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Assessment assessment = (Assessment) o;

        if ( ! Objects.equals(id, assessment.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Assessment{" +
                "id=" + id +
                ", assessmentValue='" + assessmentValue + "'" +
                ", estimationTime='" + estimationTime + "'" +
                '}';
    }

    public Assessment(){}

    public Assessment(Double assessmentValue, Feature feature, Registeredobject object, Agent agent, DateTime time){
        this.assessmentValue = assessmentValue;
        this.assessment_agent = agent;
        this.assessment_feature = feature;
        this.assessment_object = object;
        this.estimationTime = time;
    }
}
