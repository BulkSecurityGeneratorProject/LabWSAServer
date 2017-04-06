package com.platform.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.platform.domain.Unittype;
import com.platform.repository.UnittypeRepository;
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
 * REST controller for managing Unittype.
 */
@RestController
@RequestMapping("/api")
public class UnittypeResource {

    private final Logger log = LoggerFactory.getLogger(UnittypeResource.class);

    @Inject
    private UnittypeRepository unittypeRepository;

    /**
     * POST  /unittypes -> Create a new unittype.
     */
    @RequestMapping(value = "/unittypes",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Unittype> createUnittype(@RequestBody Unittype unittype) throws URISyntaxException {
        log.debug("REST request to save Unittype : {}", unittype);
        if (unittype.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new unittype cannot already have an ID").body(null);
        }
        if(unittypeRepository.findByUnitTypeName(unittype.getUnitTypeName()) != null){
            return ResponseEntity.badRequest().header("Failure", "There is already unittype with the given name in data base").body(null);
        }
        Unittype result = unittypeRepository.save(unittype);
        return ResponseEntity.created(new URI("/api/unittypes/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("unittype", result.getId().toString()))
                .body(result);
    }

    /**
     * PUT  /unittypes -> Updates an existing unittype.
     */
    @RequestMapping(value = "/unittypes",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Unittype> updateUnittype(@RequestBody Unittype unittype) throws URISyntaxException {
        log.debug("REST request to update Unittype : {}", unittype);
        if (unittype.getId() == null) {
            return createUnittype(unittype);
        }
        Unittype result = unittypeRepository.save(unittype);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("unittype", unittype.getId().toString()))
                .body(result);
    }

    /**
     * GET  /unittypes -> get all the unittypes.
     */
    @RequestMapping(value = "/unittypes",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Unittype>> getAllUnittypes(Pageable pageable)
        throws URISyntaxException {
        Page<Unittype> page = unittypeRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/unittypes");
        return new ResponseEntity<List<Unittype>>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /unittypes/:id -> get the "id" unittype.
     */
    @RequestMapping(value = "/unittypes/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Unittype> getUnittype(@PathVariable Long id, HttpServletResponse response) {
        log.debug("REST request to get Unittype : {}", id);
        Unittype unittype = unittypeRepository.findOne(id);
        if (unittype == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(unittype, HttpStatus.OK);
    }

    /**
     * DELETE  /unittypes/:id -> delete the "id" unittype.
     */
    @RequestMapping(value = "/unittypes/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteUnittype(@PathVariable Long id) {
        log.debug("REST request to delete Unittype : {}", id);
        unittypeRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("unittype", id.toString())).build();
    }
}
