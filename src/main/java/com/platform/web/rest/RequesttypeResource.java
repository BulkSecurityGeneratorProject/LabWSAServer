package com.platform.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.platform.domain.Requesttype;
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
 * REST controller for managing Requesttype.
 */
@RestController
@RequestMapping("/api")
public class RequesttypeResource {

    private final Logger log = LoggerFactory.getLogger(RequesttypeResource.class);

    @Inject
    private RequesttypeRepository requesttypeRepository;

    /**
     * POST  /requesttypes -> Create a new requesttype.
     */
    @RequestMapping(value = "/requesttypes",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Requesttype> createRequesttype(@RequestBody Requesttype requesttype) throws URISyntaxException {
        log.debug("REST request to save Requesttype : {}", requesttype);
        if (requesttype.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new requesttype cannot already have an ID").body(null);
        }
        Requesttype result = requesttypeRepository.save(requesttype);
        return ResponseEntity.created(new URI("/api/requesttypes/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("requesttype", result.getId().toString()))
                .body(result);
    }

    /**
     * PUT  /requesttypes -> Updates an existing requesttype.
     */
    @RequestMapping(value = "/requesttypes",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Requesttype> updateRequesttype(@RequestBody Requesttype requesttype) throws URISyntaxException {
        log.debug("REST request to update Requesttype : {}", requesttype);
        if (requesttype.getId() == null) {
            return createRequesttype(requesttype);
        }
        Requesttype result = requesttypeRepository.save(requesttype);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("requesttype", requesttype.getId().toString()))
                .body(result);
    }

    /**
     * GET  /requesttypes -> get all the requesttypes.
     */
    @RequestMapping(value = "/requesttypes",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Requesttype>> getAllRequesttypes(Pageable pageable)
        throws URISyntaxException {
        Page<Requesttype> page = requesttypeRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/requesttypes");
        return new ResponseEntity<List<Requesttype>>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /requesttypes/:id -> get the "id" requesttype.
     */
    @RequestMapping(value = "/requesttypes/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Requesttype> getRequesttype(@PathVariable Long id, HttpServletResponse response) {
        log.debug("REST request to get Requesttype : {}", id);
        Requesttype requesttype = requesttypeRepository.findOne(id);
        if (requesttype == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(requesttype, HttpStatus.OK);
    }

    /**
     * DELETE  /requesttypes/:id -> delete the "id" requesttype.
     */
    @RequestMapping(value = "/requesttypes/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteRequesttype(@PathVariable Long id) {
        log.debug("REST request to delete Requesttype : {}", id);
        requesttypeRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("requesttype", id.toString())).build();
    }
}
