package com.platform.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.platform.domain.Agenttype;
import com.platform.domain.Communicativeaction;
import com.platform.repository.AgenttypeRepository;
import com.platform.repository.CommunicativeactionRepository;
import com.platform.web.rest.util.HeaderUtil;
import com.platform.web.rest.util.PaginationUtil;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * REST controller for managing Agenttype.
 */
@RestController
@RequestMapping("/api")
public class AgenttypeResource {

    private final Logger log = LoggerFactory.getLogger(AgenttypeResource.class);

    @Inject
    private AgenttypeRepository agenttypeRepository;

    @Inject
    CommunicativeactionRepository communicativeactionRepository;

    /**
     * POST  /agenttypes -> Create a new agenttype.
     */
    @RequestMapping(value = "/agenttypes",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Agenttype> createAgenttype(@RequestBody Agenttype agenttype) throws URISyntaxException {
        log.debug("REST request to save Agenttype : {}", agenttype);
        if (agenttype.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new agenttype cannot already have an ID").body(null);
        }
        //agenttype name has to be unique
        if(agenttypeRepository.findByAgentTypeName(agenttype.getAgentTypeName())!= null){
            return ResponseEntity.badRequest().header("Failure", "There is already agenttype with the given name in data base").body(null);
        }
        Agenttype result = agenttypeRepository.save(agenttype);
        return ResponseEntity.created(new URI("/api/agenttypes/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("agenttype", result.getId().toString()))
                .body(result);
    }

    /**
     * POST  /agenttypes -> Create a new agenttype.
     */
    @RequestMapping(value = "/acl_agenttypes",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Agenttype> createAgenttype(@RequestBody Communicativeaction action) throws URISyntaxException {
        action.setActionTime(new DateTime());
        communicativeactionRepository.save(action);
        Agenttype result = agenttypeRepository.save(new Agenttype(action.getContent()));
        return ResponseEntity.created(new URI("/api/agenttypes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("agenttype", result.getId().toString()))
            .body(result);
    }


    /**
     * PUT  /agenttypes -> Updates an existing agenttype.
     */
    @RequestMapping(value = "/agenttypes",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Agenttype> updateAgenttype(@RequestBody Agenttype agenttype) throws URISyntaxException {
        log.debug("REST request to update Agenttype : {}", agenttype);
        if (agenttype.getId() == null) {
            return createAgenttype(agenttype);
        }
        Agenttype result = agenttypeRepository.save(agenttype);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("agenttype", agenttype.getId().toString()))
                .body(result);
    }

    /**
     * GET  /agenttypes -> get all the agenttypes.
     */
    @RequestMapping(value = "/agenttypes",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Agenttype>> getAllAgenttypes(Pageable pageable)
        throws URISyntaxException {
        Page<Agenttype> page = agenttypeRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/agenttypes");
        return new ResponseEntity<List<Agenttype>>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /agenttypes/:id -> get the "id" agenttype.
     */
    @RequestMapping(value = "/agenttypes/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Agenttype> getAgenttype(@PathVariable Long id, HttpServletResponse response) {
        log.debug("REST request to get Agenttype : {}", id);
        Agenttype agenttype = agenttypeRepository.findOne(id);
        if (agenttype == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(agenttype, HttpStatus.OK);
    }

    /**
     * DELETE  /agenttypes/:id -> delete the "id" agenttype.
     */
    @RequestMapping(value = "/agenttypes/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteAgenttype(@PathVariable Long id) {
        log.debug("REST request to delete Agenttype : {}", id);
        agenttypeRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("agenttype", id.toString())).build();
    }
}
