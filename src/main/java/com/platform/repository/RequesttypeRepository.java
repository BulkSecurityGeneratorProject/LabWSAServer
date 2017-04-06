package com.platform.repository;

import com.platform.domain.Requesttype;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Requesttype entity.
 */
public interface RequesttypeRepository extends JpaRepository<Requesttype,Long> {

    public Requesttype findByRequestTypeName(String requestTypeName);
}
