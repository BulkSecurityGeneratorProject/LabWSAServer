package com.platform.repository;

import com.platform.domain.Sensortype;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Sensortype entity.
 */
public interface SensortypeRepository extends JpaRepository<Sensortype,Long> {

    public Sensortype findBySensorTypeName(String sensorTypeName);

}
