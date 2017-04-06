package com.platform.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.platform.domain.Agent;
import com.platform.domain.Communicativeaction;
import com.platform.domain.Interactionprotocol;
import com.platform.domain.Sensor;
import com.platform.repository.AgentRepository;
import com.platform.repository.CommunicativeactionRepository;
import com.platform.repository.InteractionprotocolRepository;
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
 * REST controller for managing Interactionprotocol.
 */
@RestController
@RequestMapping("/api")
public class InteractionprotocolResource {

    private final Logger log = LoggerFactory.getLogger(InteractionprotocolResource.class);

    @Inject
    private InteractionprotocolRepository interactionprotocolRepository;

    @Inject
    private CommunicativeactionRepository communicativeactionRepository;

    @Inject
    private AgentRepository agentRepository;

    /**
     * POST  /interactionprotocols -> Create a new interactionprotocol.
     */
    @RequestMapping(value = "/acl_interactionprotocols",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Interactionprotocol> createInteractionprotocol(@RequestBody Communicativeaction action) throws URISyntaxException {
        String [] protocolInfo = action.getContent().split(",");
        String protocolName = "";
        String protocolDescription = "";
        Agent agent = action.getAction_sender();
        for(String info : protocolInfo){
            String[] protocolPair = info.split(":");
            switch(protocolPair[0]){
                case "protocolName":{
                   protocolName = protocolPair[1];
                    break;
                }
                case "protocolDescription":{
                    protocolDescription = protocolPair[1];
                    break;
                }
            }
        }
        Interactionprotocol protocol = new Interactionprotocol(protocolName, protocolDescription, agent, new DateTime());
        Interactionprotocol result = interactionprotocolRepository.save(protocol);
        action.setAction_protocol(protocol);
        action.setActionTime(new DateTime());
        communicativeactionRepository.save(action);
        return ResponseEntity.created(new URI("/api/interactionprotocols/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("interactionprotocol", result.getId().toString()))
            .body(result);
    }

    /**
     * POST  /interactionprotocols -> Create a new interactionprotocol.
     */
    @RequestMapping(value = "/interactionprotocols",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Interactionprotocol> createInteractionprotocol(@RequestBody Interactionprotocol interactionprotocol) throws URISyntaxException {
        log.debug("REST request to save Interactionprotocol : {}", interactionprotocol);
        if (interactionprotocol.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new interactionprotocol cannot already have an ID").body(null);
        }
        Interactionprotocol result = interactionprotocolRepository.save(interactionprotocol);
        return ResponseEntity.created(new URI("/api/interactionprotocols/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("interactionprotocol", result.getId().toString()))
                .body(result);
    }

    /**
     * PUT  /interactionprotocols -> Updates an existing interactionprotocol.
     */
    @RequestMapping(value = "/interactionprotocols",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Interactionprotocol> updateInteractionprotocol(@RequestBody Interactionprotocol interactionprotocol) throws URISyntaxException {
        log.debug("REST request to update Interactionprotocol : {}", interactionprotocol);
        if (interactionprotocol.getId() == null) {
            return createInteractionprotocol(interactionprotocol);
        }
        Interactionprotocol result = interactionprotocolRepository.save(interactionprotocol);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("interactionprotocol", interactionprotocol.getId().toString()))
                .body(result);
    }

    /**
     * GET  /interactionprotocols -> get all the interactionprotocols.
     */
    @RequestMapping(value = "/interactionprotocols",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Interactionprotocol>> getAllInteractionprotocols(Pageable pageable)
        throws URISyntaxException {
        Page<Interactionprotocol> page = interactionprotocolRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/interactionprotocols");
        return new ResponseEntity<List<Interactionprotocol>>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /interactionprotocols/:id -> get the "id" interactionprotocol.
     */
    @RequestMapping(value = "/interactionprotocols/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Interactionprotocol> getInteractionprotocol(@PathVariable Long id, HttpServletResponse response) {
        log.debug("REST request to get Interactionprotocol : {}", id);
        Interactionprotocol interactionprotocol = interactionprotocolRepository.findOne(id);
        if (interactionprotocol == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(interactionprotocol, HttpStatus.OK);
    }

    /**
     * DELETE  /interactionprotocols/:id -> delete the "id" interactionprotocol.
     */
    @RequestMapping(value = "/interactionprotocols/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteInteractionprotocol(@PathVariable Long id) {
        log.debug("REST request to delete Interactionprotocol : {}", id);
        interactionprotocolRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("interactionprotocol", id.toString())).build();
    }
}
