package com.platform.repository;

import com.platform.domain.Presencerequest;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.*;

import java.awt.print.Pageable;
import java.util.List;

/**
 * Spring Data JPA repository for the Presencerequest entity.
 */
public interface PresencerequestRepository extends JpaRepository<Presencerequest,Long> {

    @Query("select p.presencerequest_presencestatus.presenceStatusName from Presencerequest p where p.presencerequest_agent.id = ?1 order by p.presenceRequestTime desc")
    List<String> findCurrentlyPresentAgents(Long agent);

}
