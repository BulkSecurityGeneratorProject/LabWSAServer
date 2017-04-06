package com.platform.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.platform.domain.Presencestatus;
import com.platform.repository.PresencestatusRepository;
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
 * REST controller for managing Presencestatus.
 */
@RestController
@RequestMapping("/api")
public class PresencestatusResource {

    private final Logger log = LoggerFactory.getLogger(PresencestatusResource.class);

    @Inject
    private PresencestatusRepository presencestatusRepository;

    /**
     * POST  /presencestatuss -> Create a new presencestatus.
     */
    @RequestMapping(value = "/presencestatuss",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Presencestatus> createPresencestatus(@RequestBody Presencestatus presencestatus) throws URISyntaxException {
        log.debug("REST request to save Presencestatus : {}", presencestatus);
        if (presencestatus.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new presencestatus cannot already have an ID").body(null);
        }
        if(presencestatusRepository.findByPresenceStatusName(presencestatus.getPresenceStatusName()) !=  null){
            return ResponseEntity.badRequest().header("Failure", "The given presence status already exists").body(null);
        }
        Presencestatus result = presencestatusRepository.save(presencestatus);
        return ResponseEntity.created(new URI("/api/presencestatuss/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("presencestatus", result.getId().toString()))
                .body(result);
    }

    /**
     * PUT  /presencestatuss -> Updates an existing presencestatus.
     */
    @RequestMapping(value = "/presencestatuss",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Presencestatus> updatePresencestatus(@RequestBody Presencestatus presencestatus) throws URISyntaxException {
        log.debug("REST request to update Presencestatus : {}", presencestatus);
        if (presencestatus.getId() == null) {
            return createPresencestatus(presencestatus);
        }
        Presencestatus result = presencestatusRepository.save(presencestatus);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("presencestatus", presencestatus.getId().toString()))
                .body(result);
    }

    /**
     * GET  /presencestatuss -> get all the presencestatuss.
     */
    @RequestMapping(value = "/presencestatuss",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Presencestatus>> getAllPresencestatuss(Pageable pageable)
        throws URISyntaxException {
        Page<Presencestatus> page = presencestatusRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/presencestatuss");
        return new ResponseEntity<List<Presencestatus>>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /presencestatuss/:id -> get the "id" presencestatus.
     */
    @RequestMapping(value = "/presencestatuss/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Presencestatus> getPresencestatus(@PathVariable Long id, HttpServletResponse response) {
        log.debug("REST request to get Presencestatus : {}", id);
        Presencestatus presencestatus = presencestatusRepository.findOne(id);
        if (presencestatus == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(presencestatus, HttpStatus.OK);
    }

    /**
     * DELETE  /presencestatuss/:id -> delete the "id" presencestatus.
     */
    @RequestMapping(value = "/presencestatuss/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deletePresencestatus(@PathVariable Long id) {
        log.debug("REST request to delete Presencestatus : {}", id);
        presencestatusRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("presencestatus", id.toString())).build();
    }
}
