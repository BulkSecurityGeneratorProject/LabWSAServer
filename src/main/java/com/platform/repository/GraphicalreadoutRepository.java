package com.platform.repository;

import com.platform.domain.Graphicalreadout;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Graphicalreadout entity.
 */
public interface GraphicalreadoutRepository extends JpaRepository<Graphicalreadout,Long> {

}
