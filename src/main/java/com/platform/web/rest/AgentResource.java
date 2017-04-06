package com.platform.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.platform.domain.*;
import com.platform.repository.*;
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
import java.io.FileNotFoundException;
import java.net.URI;
import java.net.URISyntaxException;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * REST controller for managing Agent.
 */
@RestController
@RequestMapping("/api")
public class AgentResource {

    private final Logger log = LoggerFactory.getLogger(AgentResource.class);

    @Inject
    private AgentRepository agentRepository;

    @Inject
    private CommunicativeactionRepository communicativeactionRepository;

    @Inject
    private AgenttypeRepository agenttypeRepository;

    @Inject
    private SensorRepository sensorRepository;

    @Inject
    private UnittypeRepository unittypeRepository;

    @Inject
    private SensortypeRepository sensortypeRepository;

    @Inject
    private PresencerequestRepository presencerequestRepository;

    /**
     * POST  /agents -> Create a new agent.
     */
    @RequestMapping(value = "/acl_agents",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Agent> createAgent(@RequestBody Communicativeaction action) throws URISyntaxException {
        if (action.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new acl agent cannot already have an ID").body(null);
        }
        action.setActionTime(new DateTime());
        communicativeactionRepository.save(action);

        if (action.getContent() == null) {
            return ResponseEntity.badRequest().header("Failure", "No agent data given").body(null);
        }
        String[] agentInfoTable = action.getContent().split(",");
        String agentName = "";
        String agentDesctription = "";
        String agentTypeName = "";
        Agenttype agentType = null;
        ArrayList<Sensor> sensorsList = new ArrayList<>();
        for (String info : agentInfoTable) {
            String[] pairs = info.split(":");
            switch (pairs[0]) {
                case "agentName": {
                    agentName = pairs[1];
                    break;
                }
                case "agentDescription": {
                    agentDesctription = pairs[1];
                    break;
                }
                case "agentType": {
                    agentTypeName = pairs[1];
                    agentType = agenttypeRepository.findByAgentTypeName(agentTypeName);
                    break;
                }
                case "sensors": {
                    String[] sensors = pairs[1].split(";");
                    for (String sensor : sensors) {
                        //sensorName sensorAccuracy sensorType unitType
                        String[] sensorData = sensor.split(" ");
                        String sensorName = "thermometer";
                        Double sensorAccuracy = 0.0;
                        Unittype unitType = null;
                        Sensortype sensorType = null;
                        for (String sensorInfo : sensorData) {
                            String[] sensorPair = sensorInfo.split("-");
                            switch (sensorPair[0]) {
                                case "sensorName": {
                                    sensorName = sensorPair[1];
                                    break;
                                }
                                case "sensorAccuracy": {
                                    sensorAccuracy = Double.parseDouble(sensorPair[1]);
                                    break;
                                }
                                case "unitType": {
                                    unitType = unittypeRepository.findByUnitTypeName(sensorPair[1]);
                                    if (unitType == null) {
                                        unittypeRepository.save(new Unittype(sensorPair[1]));
                                    }
                                    break;
                                }
                                case "sensorType": {
                                    sensorType = sensortypeRepository.findBySensorTypeName(sensorPair[1]);
                                    if (sensorType == null) {
                                        sensortypeRepository.save(new Sensortype(sensorPair[1]));
                                    }
                                    break;
                                }
                            }
                        }
                        sensorsList.add(new Sensor(sensorName, sensorAccuracy, unitType, sensorType));
                    }
                }
                default: {
                    break;
                }
            }
        }
        if (agentRepository.findByAgentName(agentName) == null) {
            Agent agent = new Agent(agentName, agentDesctription, agentType);
            Agent result = agentRepository.save(agent);
            Iterator<Sensor> sensorIterator = sensorsList.iterator();
            while (sensorIterator.hasNext()) {
                Sensor s = sensorIterator.next();
                s.setSensor_agent(agent);
                sensorRepository.save(s);
            }
            return ResponseEntity.created(new URI("/api/agents/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("agent", result.getId().toString()))
                .body(result);
        }
        return ResponseEntity.badRequest().header("Failure", "There is already agent with the given name in data base").body(null);
    }

    /**
     * POST  /agents -> Create a new agent.
     */
    @RequestMapping(value = "/agents",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Agent> createAgent(@RequestBody Agent agent) throws URISyntaxException {
        log.debug("REST request to save Agent : {}", agent);
        if (agent.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new agent cannot already have an ID").body(null);
        }
        if (agentRepository.findByAgentName(agent.getAgentName()) != null) {
            return ResponseEntity.badRequest().header("Failure", "There is already agent with the given name in data base").body(null);
        }
        Agent result = agentRepository.save(agent);
        return ResponseEntity.created(new URI("/api/agents/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("agent", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /agents -> Updates an existing agent.
     */
    @RequestMapping(value = "/agents",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Agent> updateAgent(@RequestBody Agent agent) throws URISyntaxException {
        log.debug("REST request to update Agent : {}", agent);
        if (agent.getId() == null) {
            return createAgent(agent);
        }
        Agent result = agentRepository.save(agent);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("agent", agent.getId().toString()))
            .body(result);
    }

    //rest client trial

    /**
     * GET  /client_trial -> get request to the mock server
     */
    @RequestMapping(value = "/client_trial",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<String> sendGetRequestToTheServer(String params, String requestType, String urlAddress)
        throws URISyntaxException {
        String responseText = "<empty message>";
        HttpURLConnectionExample httpConnection = new HttpURLConnectionExample();
        try {
            switch (requestType) {
                case "POST": {
                    responseText = httpConnection.sendPost(urlAddress, "message=" + params);
                    break;
                }
                case "GET": {
                    responseText = httpConnection.sendGet(urlAddress);
                    break;
                }
            }
        } catch (FileNotFoundException fe) {
            responseText = "File not found- wrong URL format!";
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<String> temp = new ArrayList<>();
        temp.add(responseText);
        return temp;
    }

    /**
     * GET  /agents -> get all the agents.
     */
    @RequestMapping(value = "/agents",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Agent>> getAllAgents(Pageable pageable)
        throws URISyntaxException {
        Page<Agent> page = agentRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/agents");
        return new ResponseEntity<List<Agent>>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /agents -> get all currently present agents.
     */
    @RequestMapping(value = "/currently_present_agents",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Agent> getAllCurrentlyPresentAgents(Pageable pageable)
        throws URISyntaxException {
        DateTime now = new DateTime();
        List<Agent> agents = agentRepository.findAll();
        List<Agent> presentAgents = new ArrayList<>();
        for (Agent agent : agents) {
            Long agentId = agent.getId();
            List<String> temp = presencerequestRepository.findCurrentlyPresentAgents(agentId);
            if (temp.size() != 0) {
                if (temp.get(0).equals(Presencestatus.START_PRESENCE)) {
                    presentAgents.add(agent);
                }
            }
        }

        return presentAgents;
    }

    /**
     * GET  /agents/:id -> get the "id" agent.
     */
    @RequestMapping(value = "/agents/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Agent> getAgent(@PathVariable Long id, HttpServletResponse response) {
        log.debug("REST request to get Agent : {}", id);
        Agent agent = agentRepository.findOne(id);
        if (agent == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(agent, HttpStatus.OK);
    }

    /**
     * DELETE  /agents/:id -> delete the "id" agent.
     */
    @RequestMapping(value = "/agents/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteAgent(@PathVariable Long id) {
        log.debug("REST request to delete Agent : {}", id);
        agentRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("agent", id.toString())).build();
    }
}
