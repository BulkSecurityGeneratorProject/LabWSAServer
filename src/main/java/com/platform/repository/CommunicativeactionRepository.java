package com.platform.repository;

import com.platform.domain.Communicativeaction;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Communicativeaction entity.
 */
public interface CommunicativeactionRepository extends JpaRepository<Communicativeaction,Long> {

    @Query("select c from Communicativeaction c where c.content like ?1%")
    Page<Communicativeaction> findFilteredActions(String content, Pageable pageable);
}
