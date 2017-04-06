package com.platform.repository;

import com.platform.domain.Agent;
import com.platform.domain.Sensor;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Sensor entity.
 */
public interface SensorRepository extends JpaRepository<Sensor,Long> {

    @Query("select s from Sensor s where s.sensorName = ?1 and s.sensor_agent = ?2")
    Sensor findBySensorNameAndAgent(String sensorName, Agent agent);
}
