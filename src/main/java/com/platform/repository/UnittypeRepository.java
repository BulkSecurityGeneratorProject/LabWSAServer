package com.platform.repository;

import com.platform.domain.Unittype;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Unittype entity.
 */
public interface UnittypeRepository extends JpaRepository<Unittype,Long> {

    public Unittype findByUnitTypeName(String unitTypeName);
}
