package com.platform.repository;

import com.platform.domain.Agent;
import com.platform.domain.Readout;
import com.platform.domain.Sensor;
import org.joda.time.DateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Readout entity.
 */
public interface ReadoutRepository extends JpaRepository<Readout,Long> {

    @Query("select r from Readout r where r.readout_sensor.id = ?1")
    List<Readout> findAllForSensor(Long s);

    @Query("select r from Readout r where r.readout_sensor.id = ?1 and r.readoutTime > ?2 and r.readoutTime < ?3 order by r.readoutTime asc")
    List<Readout> findAllForSensorInGivenTimeRange(Long s, DateTime from, DateTime to);

    @Query("select distinct r.readout_sensor from Readout r where r.readout_sensor.sensor_agent.id = ?1")
    List<Sensor> findAllSensorsForChoosenAgent(Long a);

    @Query("select r from Readout r order by r.readoutTime asc")
    Page<Readout> findAllOrderByReadoutTimeAsc(Pageable pageable);

    @Query("select r from Readout r where r.readout_sensor.sensor_agent.id = ?1 and r.readoutTime > ?2 and r.readoutTime < ?3 order by r.readoutTime asc")
    Page<Readout> findAllReadoutsForAgent(Long agent, DateTime from, DateTime to, Pageable pageable);

}
