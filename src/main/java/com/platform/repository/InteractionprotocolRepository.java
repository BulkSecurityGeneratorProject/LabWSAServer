package com.platform.repository;

import com.platform.domain.Interactionprotocol;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Interactionprotocol entity.
 */
public interface InteractionprotocolRepository extends JpaRepository<Interactionprotocol,Long> {

}
