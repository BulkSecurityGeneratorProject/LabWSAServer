package com.platform.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.platform.domain.Sensor;
import com.platform.repository.SensorRepository;
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
 * REST controller for managing Sensor.
 */
@RestController
@RequestMapping("/api")
public class SensorResource {

    private final Logger log = LoggerFactory.getLogger(SensorResource.class);

    @Inject
    private SensorRepository sensorRepository;

    /**
     * POST  /sensors -> Create a new sensor.
     */
    @RequestMapping(value = "/sensors",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Sensor> createSensor(@RequestBody Sensor sensor) throws URISyntaxException {
        log.debug("REST request to save Sensor : {}", sensor);
        if (sensor.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new sensor cannot already have an ID").body(null);
        }
        Sensor result = sensorRepository.save(sensor);
        return ResponseEntity.created(new URI("/api/sensors/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("sensor", result.getId().toString()))
                .body(result);
    }

    /**
     * PUT  /sensors -> Updates an existing sensor.
     */
    @RequestMapping(value = "/sensors",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Sensor> updateSensor(@RequestBody Sensor sensor) throws URISyntaxException {
        log.debug("REST request to update Sensor : {}", sensor);
        if (sensor.getId() == null) {
            return createSensor(sensor);
        }
        Sensor result = sensorRepository.save(sensor);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("sensor", sensor.getId().toString()))
                .body(result);
    }

    /**
     * GET  /sensors -> get all the sensors.
     */
    @RequestMapping(value = "/sensors",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Sensor>> getAllSensors(Pageable pageable)
        throws URISyntaxException {
        Page<Sensor> page = sensorRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/sensors");
        return new ResponseEntity<List<Sensor>>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /sensors/:id -> get the "id" sensor.
     */
    @RequestMapping(value = "/sensors/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Sensor> getSensor(@PathVariable Long id, HttpServletResponse response) {
        log.debug("REST request to get Sensor : {}", id);
        Sensor sensor = sensorRepository.findOne(id);
        if (sensor == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(sensor, HttpStatus.OK);
    }

    /**
     * DELETE  /sensors/:id -> delete the "id" sensor.
     */
    @RequestMapping(value = "/sensors/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteSensor(@PathVariable Long id) {
        log.debug("REST request to delete Sensor : {}", id);
        sensorRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("sensor", id.toString())).build();
    }
}
