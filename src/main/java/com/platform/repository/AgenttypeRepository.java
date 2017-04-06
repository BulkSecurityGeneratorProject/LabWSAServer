package com.platform.repository;

import com.platform.domain.Agenttype;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Agenttype entity.
 */
public interface AgenttypeRepository extends JpaRepository<Agenttype,Long> {

    public Agenttype findByAgentTypeName(String agentTypeName);
}
