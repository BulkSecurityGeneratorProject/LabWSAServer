package com.platform.repository;

import com.platform.domain.Registeredobject;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Registeredobject entity.
 */
public interface RegisteredobjectRepository extends JpaRepository<Registeredobject,Long> {

    public Registeredobject findByObjectName(String objectName);

}
