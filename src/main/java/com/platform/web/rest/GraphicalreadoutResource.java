package com.platform.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.platform.domain.Agent;
import com.platform.domain.Communicativeaction;
import com.platform.domain.Graphicalreadout;
import com.platform.domain.Sensor;
import com.platform.repository.CommunicativeactionRepository;
import com.platform.repository.GraphicalreadoutRepository;
import com.platform.repository.SensorRepository;
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
import org.springframework.security.crypto.codec.Base64;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * REST controller for managing Graphicalreadout.
 */
@RestController
@RequestMapping("/api")
public class GraphicalreadoutResource {

    private final Logger log = LoggerFactory.getLogger(GraphicalreadoutResource.class);

    @Inject
    private GraphicalreadoutRepository graphicalreadoutRepository;

    @Inject
    private CommunicativeactionRepository communicativeactionRepository;

    @Inject
    private SensorRepository sensorRepository;

    /**
     * POST  /graphicalreadouts -> Create a new graphicalreadout.
     */
    @RequestMapping(value = "/graphicalreadouts",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Graphicalreadout> createGraphicalreadout(@RequestBody Graphicalreadout graphicalreadout) throws URISyntaxException {
        log.debug("REST request to save Graphicalreadout : {}", graphicalreadout);
        if (graphicalreadout.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new graphicalreadout cannot already have an ID").body(null);
        }
        Graphicalreadout result = graphicalreadoutRepository.save(graphicalreadout);
        return ResponseEntity.created(new URI("/api/graphicalreadouts/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("graphicalreadout", result.getId().toString()))
                .body(result);
    }

    /**
     * POST  /graphicalreadouts -> Create a new graphicalreadout.
     */
    @RequestMapping(value = "/acl_graphicalreadouts",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Graphicalreadout> createAclGraphicalreadout(@RequestBody Communicativeaction action) throws URISyntaxException {

        action.setActionTime(new DateTime());
        String [] imageInfo = action.getContent().split(",");
        String imageType = "";
        byte[] image= {};
        String sensorName="";
        Agent agent = action.getAction_sender();

        for(String info : imageInfo){
            String[] readoutPair = info.split(":");
            switch(readoutPair[0]){
                case "imageType":{
                    imageType = readoutPair[1];
                    break;
                }
                case "image":{
                    image = Base64.decode(readoutPair[1].getBytes());
                    break;
                }
                case "sensor":{
                    sensorName = readoutPair[1];
                    break;
                }
            }
        }

        Sensor sensor = sensorRepository.findBySensorNameAndAgent(sensorName, agent);
        Graphicalreadout graphicalreadout = new Graphicalreadout(image, imageType, new DateTime(), sensor);

        action.setContent("Long image content");
        communicativeactionRepository.save(action);

        Graphicalreadout result = graphicalreadoutRepository.save(graphicalreadout);
        return ResponseEntity.created(new URI("/api/graphicalreadouts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("graphicalreadout", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /graphicalreadouts -> Updates an existing graphicalreadout.
     */
    @RequestMapping(value = "/graphicalreadouts",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Graphicalreadout> updateGraphicalreadout(@RequestBody Graphicalreadout graphicalreadout) throws URISyntaxException {
        log.debug("REST request to update Graphicalreadout : {}", graphicalreadout);
        if (graphicalreadout.getId() == null) {
            return createGraphicalreadout(graphicalreadout);
        }
        Graphicalreadout result = graphicalreadoutRepository.save(graphicalreadout);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("graphicalreadout", graphicalreadout.getId().toString()))
                .body(result);
    }

    /**
     * GET  /graphicalreadouts -> get all the graphicalreadouts.
     */
    @RequestMapping(value = "/graphicalreadouts",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Graphicalreadout>> getAllGraphicalreadouts(Pageable pageable)
        throws URISyntaxException {
        Page<Graphicalreadout> page = graphicalreadoutRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/graphicalreadouts");
        return new ResponseEntity<List<Graphicalreadout>>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /graphicalreadouts/:id -> get the "id" graphicalreadout.
     */
    @RequestMapping(value = "/graphicalreadouts/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Graphicalreadout> getGraphicalreadout(@PathVariable Long id, HttpServletResponse response) {
        log.debug("REST request to get Graphicalreadout : {}", id);
        Graphicalreadout graphicalreadout = graphicalreadoutRepository.findOne(id);
        if (graphicalreadout == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(graphicalreadout, HttpStatus.OK);
    }

    /**
     * DELETE  /graphicalreadouts/:id -> delete the "id" graphicalreadout.
     */
    @RequestMapping(value = "/graphicalreadouts/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteGraphicalreadout(@PathVariable Long id) {
        log.debug("REST request to delete Graphicalreadout : {}", id);
        graphicalreadoutRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("graphicalreadout", id.toString())).build();
    }
}
