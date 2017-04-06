package com.platform.repository;

import com.platform.domain.Presencestatus;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Presencestatus entity.
 */
public interface PresencestatusRepository extends JpaRepository<Presencestatus,Long> {

    public Presencestatus findByPresenceStatusName(String presenceStatusName);
}
