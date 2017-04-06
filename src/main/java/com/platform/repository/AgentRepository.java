package com.platform.repository;

import com.platform.domain.Agent;
import org.joda.time.DateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.*;

import java.awt.print.Pageable;
import java.util.List;

/**
 * Spring Data JPA repository for the Agent entity.
 */
public interface AgentRepository extends JpaRepository<Agent,Long> {

    public Agent findByAgentName(String agentName);
}
