package com.platform.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.platform.domain.Communicativeaction;
import com.platform.domain.Requesttype;
import com.platform.repository.CommunicativeactionRepository;
import com.platform.repository.RequesttypeRepository;
import com.platform.web.rest.util.HeaderUtil;
import com.platform.web.rest.util.PaginationUtil;
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
 * REST controller for managing Communicativeaction.
 */
@RestController
@RequestMapping("/api")
public class CommunicativeactionResource {

    private final Logger log = LoggerFactory.getLogger(CommunicativeactionResource.class);

    @Inject
    private CommunicativeactionRepository communicativeactionRepository;

    @Inject
    private RequesttypeRepository requesttypeRepository;

    /**
     * POST  /communicativeactions -> Create a new communicativeaction.
     */
    @RequestMapping(value = "/communicativeactions",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Communicativeaction> createCommunicativeaction(@RequestBody Communicativeaction communicativeaction) throws URISyntaxException {
        log.debug("REST request to save Communicativeaction : {}", communicativeaction);
        if (communicativeaction.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new communicativeaction cannot already have an ID").body(null);
        }
        Communicativeaction result = communicativeactionRepository.save(communicativeaction);
        return ResponseEntity.created(new URI("/api/communicativeactions/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("communicativeaction", result.getId().toString()))
                .body(result);
    }

    /**
     * PUT  /communicativeactions -> Updates an existing communicativeaction.
     */
    @RequestMapping(value = "/communicativeactions",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Communicativeaction> updateCommunicativeaction(@RequestBody Communicativeaction communicativeaction) throws URISyntaxException {
        log.debug("REST request to update Communicativeaction : {}", communicativeaction);
        if (communicativeaction.getId() == null) {
            return createCommunicativeaction(communicativeaction);
        }
        Communicativeaction result = communicativeactionRepository.save(communicativeaction);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("communicativeaction", communicativeaction.getId().toString()))
                .body(result);
    }

    /**
     * GET  /communicativeactions -> get all the communicativeactions.
     */
    @RequestMapping(value = "/communicativeactions",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Communicativeaction>> getAllCommunicativeactions(Pageable pageable)
        throws URISyntaxException {
        Page<Communicativeaction> page = communicativeactionRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/communicativeactions");
        return new ResponseEntity<List<Communicativeaction>>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /communicativeactions -> get all the communicativeactions.
     */
    @RequestMapping(value = "/filter_communicativeactions/{filterType}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Communicativeaction>> getFilteredCommunicativeactions(Pageable pageable,@PathVariable String filterType)
        throws URISyntaxException {
        String s = "agentName";
        Page<Communicativeaction> page = communicativeactionRepository.findFilteredActions(filterType, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/communicativeactions");
        return new ResponseEntity<List<Communicativeaction>>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /communicativeactions/:id -> get the "id" communicativeaction.
     */
    @RequestMapping(value = "/communicativeactions/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Communicativeaction> getCommunicativeaction(@PathVariable Long id, HttpServletResponse response) {
        log.debug("REST request to get Communicativeaction : {}", id);
        Communicativeaction communicativeaction = communicativeactionRepository.findOne(id);
        if (communicativeaction == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(communicativeaction, HttpStatus.OK);
    }

    /**
     * DELETE  /communicativeactions/:id -> delete the "id" communicativeaction.
     */
    @RequestMapping(value = "/communicativeactions/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteCommunicativeaction(@PathVariable Long id) {
        log.debug("REST request to delete Communicativeaction : {}", id);
        communicativeactionRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("communicativeaction", id.toString())).build();
    }
}
