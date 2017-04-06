package com.platform.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.platform.domain.Agent;
import com.platform.domain.Communicativeaction;
import com.platform.domain.Presencerequest;
import com.platform.domain.Presencestatus;
import com.platform.repository.AgentRepository;
import com.platform.repository.CommunicativeactionRepository;
import com.platform.repository.PresencerequestRepository;
import com.platform.repository.PresencestatusRepository;
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
 * REST controller for managing Presencerequest.
 */
@RestController
@RequestMapping("/api")
public class PresencerequestResource {

    private final Logger log = LoggerFactory.getLogger(PresencerequestResource.class);

    @Inject
    private PresencerequestRepository presencerequestRepository;

    @Inject
    private CommunicativeactionRepository communicativeactionRepository;

    @Inject
    private PresencestatusRepository presencestatusRepository;

    @Inject
    private AgentRepository agentRepository;

    /**
     * POST  /presencerequests -> Create a new presencerequest.
     */
    @RequestMapping(value = "/presencerequests",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Presencerequest> createPresencerequest(@RequestBody Presencerequest presencerequest) throws URISyntaxException {
        log.debug("REST request to save Presencerequest : {}", presencerequest);
        if (presencerequest.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new presencerequest cannot already have an ID").body(null);
        }
        Presencerequest result = presencerequestRepository.save(presencerequest);
        return ResponseEntity.created(new URI("/api/presencerequests/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("presencerequest", result.getId().toString()))
                .body(result);
    }

    /**
     * POST  /presencerequests -> Create a new presencerequest.
     */
    @RequestMapping(value = "/acl_presencerequests",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Presencerequest> createPresencerequest(@RequestBody Communicativeaction communicativeaction) throws URISyntaxException {
        communicativeaction.setActionTime(new DateTime());
        communicativeactionRepository.save(communicativeaction);
        String [] presenceReqInfo = communicativeaction.getContent().split(",");
        DateTime time = new DateTime();
        Presencestatus presenceStatus = null;
        Agent agent = communicativeaction.getAction_sender();
        for(String info : presenceReqInfo){
            String [] presencePair = info.split(":");
            switch(presencePair[0]){
                case "presenceStatus":{
                    presenceStatus = presencestatusRepository.findByPresenceStatusName(presencePair[1]);
                    break;
                }
            }
        }
        if(presenceStatus != null){
            Presencerequest presence = new Presencerequest(presenceStatus, time, agent);
            Presencerequest result = presencerequestRepository.save(presence);
            return ResponseEntity.created(new URI("/api/presencerequests/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("presencerequest", result.getId().toString()))
                .body(result);
        }
      return ResponseEntity.created(new URI("/api/presencerequests/"))
          .headers(HeaderUtil.createEntityCreationAlert("Failure", "Not a valid presence request status"))
          .body(null);
    }

    /**
     * PUT  /presencerequests -> Updates an existing presencerequest.
     */
    @RequestMapping(value = "/presencerequests",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Presencerequest> updatePresencerequest(@RequestBody Presencerequest presencerequest) throws URISyntaxException {
        log.debug("REST request to update Presencerequest : {}", presencerequest);
        if (presencerequest.getId() == null) {
            return createPresencerequest(presencerequest);
        }
        Presencerequest result = presencerequestRepository.save(presencerequest);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("presencerequest", presencerequest.getId().toString()))
                .body(result);
    }

    /**
     * GET  /presencerequests -> get all the presencerequests.
     */
    @RequestMapping(value = "/presencerequests",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Presencerequest>> getAllPresencerequests(Pageable pageable)
        throws URISyntaxException {
        Page<Presencerequest> page = presencerequestRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/presencerequests");
        return new ResponseEntity<List<Presencerequest>>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /presencerequests/:id -> get the "id" presencerequest.
     */
    @RequestMapping(value = "/presencerequests/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Presencerequest> getPresencerequest(@PathVariable Long id, HttpServletResponse response) {
        log.debug("REST request to get Presencerequest : {}", id);
        Presencerequest presencerequest = presencerequestRepository.findOne(id);
        if (presencerequest == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(presencerequest, HttpStatus.OK);
    }

    /**
     * DELETE  /presencerequests/:id -> delete the "id" presencerequest.
     */
    @RequestMapping(value = "/presencerequests/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deletePresencerequest(@PathVariable Long id) {
        log.debug("REST request to delete Presencerequest : {}", id);
        presencerequestRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("presencerequest", id.toString())).build();
    }
}
