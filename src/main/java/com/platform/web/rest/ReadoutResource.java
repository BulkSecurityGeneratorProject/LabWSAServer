package com.platform.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.platform.domain.Agent;
import com.platform.domain.Communicativeaction;
import com.platform.domain.Readout;
import com.platform.domain.Sensor;
import com.platform.repository.AgentRepository;
import com.platform.repository.CommunicativeactionRepository;
import com.platform.repository.ReadoutRepository;
import com.platform.repository.SensorRepository;
import com.platform.web.rest.util.HeaderUtil;
import com.platform.web.rest.util.PaginationUtil;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * REST controller for managing Readout.
 */
@RestController
@RequestMapping("/api")
public class ReadoutResource {

    private final Logger log = LoggerFactory.getLogger(ReadoutResource.class);

    @Inject
    private ReadoutRepository readoutRepository;

    @Inject
    private CommunicativeactionRepository communicativeactionRepository;

    @Inject
    private SensorRepository sensorRepository;

    @Inject
    private AgentRepository agentRepository;

    /**
     * POST  /readouts -> Create a new readout.
     */
    @RequestMapping(value = "/acl_readouts",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Readout> createReadout(@RequestBody Communicativeaction action) throws URISyntaxException {
        action.setActionTime(new DateTime());
        communicativeactionRepository.save(action);
        String [] readoutInfo = action.getContent().split(",");
        Double readoutValue = 0.0;
        String sensorName = "";
        Agent agent = action.getAction_sender();
        for(String info : readoutInfo){
            String[] readoutPair = info.split(":");
            switch(readoutPair[0]){
                case "readoutValue":{
                    readoutValue = Double.parseDouble(readoutPair[1]);
                    break;
                }
                case "sensorName":{
                    sensorName = readoutPair[1];
                    break;
                }
            }
        }
        Sensor sensor = sensorRepository.findBySensorNameAndAgent(sensorName, agent);
        Readout readout = new Readout(readoutValue, new DateTime(), sensor);
        Readout result = readoutRepository.save(readout);
        return ResponseEntity.created(new URI("/api/readouts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("readout", result.getId().toString()))
            .body(result);
    }

    /**
     * POST  /readouts -> Create a new readout.
     */
    @RequestMapping(value = "/readouts",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Readout> createReadout(@RequestBody Readout readout) throws URISyntaxException {
        log.debug("REST request to save Readout : {}", readout);
        if (readout.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new readout cannot already have an ID").body(null);
        }
        Readout result = readoutRepository.save(readout);
        return ResponseEntity.created(new URI("/api/readouts/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("readout", result.getId().toString()))
                .body(result);
    }

    /**
     * PUT  /readouts -> Updates an existing readout.
     */
    @RequestMapping(value = "/readouts",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Readout> updateReadout(@RequestBody Readout readout) throws URISyntaxException {
        log.debug("REST request to update Readout : {}", readout);
        if (readout.getId() == null) {
            return createReadout(readout);
        }
        Readout result = readoutRepository.save(readout);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("readout", readout.getId().toString()))
                .body(result);
    }

    /**
     * GET  /readouts -> get all the readouts.
     */
    @RequestMapping(value = "/readouts",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Readout>> getAllReadouts(Pageable pageable)
        throws URISyntaxException {
        Page<Readout> page = readoutRepository.findAllOrderByReadoutTimeAsc(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/readouts");
        return new ResponseEntity<List<Readout>>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /readouts -> get all the readouts.
     */
    @RequestMapping(value = "/readouts_for_agent",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Readout>> getAllReadoutsForAgent(Long agent, @DateTimeFormat(pattern="yyyy-MM-dd'T'HH:mm:ss.SSS'Z'") Date fromDate, @DateTimeFormat(pattern="yyyy-MM-dd'T'HH:mm:ss.SSS'Z'") Date toDate, Pageable pageable)
        throws URISyntaxException {
        Calendar calstart = Calendar.getInstance();
        calstart.setTime(fromDate);
        DateTime start =  new DateTime(calstart.getTime());
        calstart.setTime(toDate);
        DateTime end =  new DateTime(calstart.getTime());
        Page<Readout> page = readoutRepository.findAllReadoutsForAgent(agent, start, end,  pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/readouts");
        return new ResponseEntity<List<Readout>>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /readouts -> get all the readouts.
     */
    @RequestMapping(value = "/chart_labels/{sensor}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<String> getAllLabelsForChart(@PathVariable Long sensor)
        throws URISyntaxException {
        List<Readout> readoutList = readoutRepository.findAllForSensor(sensor);
        List<String> labels = new ArrayList<>();
        for(Readout r : readoutList){
            if(r.getReadoutTime()!=null){
                labels.add(r.getReadoutTime().getDayOfMonth()+"-"+r.getReadoutTime().getMonthOfYear()+"-"+r.getReadoutTime().getYear());
            }
            else{
                labels.add("unknown label");
            }
        }
        return labels;
    }


    /**
     * GET  /readouts -> get all the readouts.
     */
    @RequestMapping(value = "/chart_readouts/{sensor}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Double> getAllReadoutsForChart(@PathVariable Long sensor)
        throws URISyntaxException {
        List<Readout> readoutList = readoutRepository.findAllForSensor(sensor);
        List<Double> readouts = new ArrayList<>();
        for(Readout r : readoutList){
            readouts.add(r.getReadoutValue());
        }
        return readouts;
    }

    /**
     * GET  /readouts -> get all the readouts.
     */
    @RequestMapping(value = "/chart_readouts_with_timerange",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Double> getAllReadoutsForChartInGivenTimeRange(@DateTimeFormat(pattern="yyyy-MM-dd'T'HH:mm:ss.SSS'Z'") Date fromDate,
                                                               @DateTimeFormat(pattern="yyyy-MM-dd'T'HH:mm:ss.SSS'Z'") Date toDate, Long sensor)
        throws URISyntaxException {
        Calendar calstart = Calendar.getInstance();
        calstart.setTime(fromDate);
        DateTime start =  new DateTime(calstart.getTime());
        calstart.setTime(toDate);
        DateTime end =  new DateTime(calstart.getTime());
        log.debug("Data poczatkowa " + fromDate.toString(), fromDate.toString());
        log.debug("Data koncowa " +  toDate.toString(), toDate.toString());
        List<Readout> readoutList = readoutRepository.findAllForSensorInGivenTimeRange(sensor, start, end);
        List<Double> readouts = new ArrayList<>();
        for(Readout r : readoutList){
            readouts.add(r.getReadoutValue());
        }
        return readouts;
    }


    /**
     * GET  /readouts -> get all the readouts.
     */
    @RequestMapping(value = "/chart_labels_with_time_range",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<String> getAllLabelsForChartInTimeRange(@DateTimeFormat(pattern="yyyy-MM-dd'T'HH:mm:ss.SSS'Z'") Date fromDate,
                                             @DateTimeFormat(pattern="yyyy-MM-dd'T'HH:mm:ss.SSS'Z'") Date toDate, Long sensor)
        throws URISyntaxException {
       Calendar calstart = Calendar.getInstance();
        calstart.setTime(fromDate);
        DateTime start =  new DateTime(calstart.getTime());
        calstart.setTime(toDate);
        DateTime end =  new DateTime(calstart.getTime());
        List<Readout> readoutList = readoutRepository.findAllForSensorInGivenTimeRange(sensor, start, end);
        List<String> labels = new ArrayList<>();
        for(Readout r : readoutList){
            if(r.getReadoutTime()!=null){
                labels.add(r.getReadoutTime().getDayOfMonth()+"-"+r.getReadoutTime().getMonthOfYear()+"-"+r.getReadoutTime().getYear()+" "+r.getReadoutTime().getHourOfDay()+":"+r.getReadoutTime().getMinuteOfHour());
            }
            else{
                labels.add("unknown label");
            }
        }
        return labels;
    }


    /**
     * GET  /readouts/:id -> get the "id" readout.
     */
    @RequestMapping(value = "/readouts/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Readout> getReadout(@PathVariable Long id, HttpServletResponse response) {
        log.debug("REST request to get Readout : {}", id);
        Readout readout = readoutRepository.findOne(id);
        if (readout == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(readout, HttpStatus.OK);
    }

    /**
     * GET  /readouts/agents -> get all agents
     */
    @RequestMapping(value = "/readouts/agents",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Agent> getAllAgents(Pageable pageable) {

        List agents = agentRepository.findAll();
        return agents;
    }

    /**
     * GET  /readouts/agents -> get all sensors
     */
    @RequestMapping(value = "/readouts/sensors/{a}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Sensor> getAllSensors(@PathVariable Long a) {
        List<Sensor> sensors = readoutRepository.findAllSensorsForChoosenAgent(a);
        return sensors;
    }

    /**
     * DELETE  /readouts/:id -> delete the "id" readout.
     */
    @RequestMapping(value = "/readouts/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteReadout(@PathVariable Long id) {
        log.debug("REST request to delete Readout : {}", id);
        readoutRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("readout", id.toString())).build();
    }
}
