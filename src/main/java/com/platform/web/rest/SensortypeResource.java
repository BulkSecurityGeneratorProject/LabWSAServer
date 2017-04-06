package com.platform.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.platform.domain.Sensortype;
import com.platform.repository.SensortypeRepository;
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
 * REST controller for managing Sensortype.
 */
@RestController
@RequestMapping("/api")
public class SensortypeResource {

    private final Logger log = LoggerFactory.getLogger(SensortypeResource.class);

    @Inject
    private SensortypeRepository sensortypeRepository;

    /**
     * POST  /sensortypes -> Create a new sensortype.
     */
    @RequestMapping(value = "/sensortypes",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Sensortype> createSensortype(@RequestBody Sensortype sensortype) throws URISyntaxException {
        log.debug("REST request to save Sensortype : {}", sensortype);
        if (sensortype.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new sensortype cannot already have an ID").body(null);
        }
        if(sensortypeRepository.findBySensorTypeName(sensortype.getSensorTypeName()) != null){
            return ResponseEntity.badRequest().header("Failure", "There is already sensortype with the given name in data base").body(null);
        }
        Sensortype result = sensortypeRepository.save(sensortype);
        return ResponseEntity.created(new URI("/api/sensortypes/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("sensortype", result.getId().toString()))
                .body(result);
    }

    /**
     * PUT  /sensortypes -> Updates an existing sensortype.
     */
    @RequestMapping(value = "/sensortypes",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Sensortype> updateSensortype(@RequestBody Sensortype sensortype) throws URISyntaxException {
        log.debug("REST request to update Sensortype : {}", sensortype);
        if (sensortype.getId() == null) {
            return createSensortype(sensortype);
        }
        Sensortype result = sensortypeRepository.save(sensortype);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("sensortype", sensortype.getId().toString()))
                .body(result);
    }

    /**
     * GET  /sensortypes -> get all the sensortypes.
     */
    @RequestMapping(value = "/sensortypes",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Sensortype>> getAllSensortypes(Pageable pageable)
        throws URISyntaxException {
        Page<Sensortype> page = sensortypeRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/sensortypes");
        return new ResponseEntity<List<Sensortype>>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /sensortypes/:id -> get the "id" sensortype.
     */
    @RequestMapping(value = "/sensortypes/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Sensortype> getSensortype(@PathVariable Long id, HttpServletResponse response) {
        log.debug("REST request to get Sensortype : {}", id);
        Sensortype sensortype = sensortypeRepository.findOne(id);
        if (sensortype == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(sensortype, HttpStatus.OK);
    }

    /**
     * DELETE  /sensortypes/:id -> delete the "id" sensortype.
     */
    @RequestMapping(value = "/sensortypes/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteSensortype(@PathVariable Long id) {
        log.debug("REST request to delete Sensortype : {}", id);
        sensortypeRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("sensortype", id.toString())).build();
    }
}
