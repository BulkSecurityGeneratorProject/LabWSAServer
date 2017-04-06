package com.platform.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.platform.domain.Agent;
import com.platform.domain.Communicativeaction;
import com.platform.domain.Registeredobject;
import com.platform.repository.CommunicativeactionRepository;
import com.platform.repository.RegisteredobjectRepository;
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
 * REST controller for managing Registeredobject.
 */
@RestController
@RequestMapping("/api")
public class RegisteredobjectResource {

    private final Logger log = LoggerFactory.getLogger(RegisteredobjectResource.class);

    @Inject
    private RegisteredobjectRepository registeredobjectRepository;

    @Inject
    private CommunicativeactionRepository communicativeactionRepository;


    /**
     * POST  /registeredobjects -> Create a new registeredobject.
     */
    @RequestMapping(value = "/acl_registeredobjects",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Registeredobject> createRegisteredobject(@RequestBody Communicativeaction action) throws URISyntaxException {
        action.setActionTime(new DateTime());
        communicativeactionRepository.save(action);
        String [] objectInfo = action.getContent().split(",");
        String objectName = "";
        String objectDescription = "";
        Agent agent = action.getAction_sender();
        for(String info : objectInfo){
            String[] objectPair = info.split(":");
            switch(objectPair[0]){
                case "objectName":{
                    objectName = objectPair[1];
                    break;
                }
                case "objectDescription":{
                    objectDescription = objectPair[1];
                    break;
                }
            }
        }
        if(registeredobjectRepository.findByObjectName(objectName) != null){
            return ResponseEntity.badRequest().header("Failure", "There is already object with the given name registered in the data base").body(null);
        }
        Registeredobject regObject = new Registeredobject(objectName, objectDescription, agent, new DateTime());
        Registeredobject result = registeredobjectRepository.save(regObject);
        return ResponseEntity.created(new URI("/api/registeredobjects/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("registeredobject", result.getId().toString()))
            .body(result);
    }

    /**
     * POST  /registeredobjects -> Create a new registeredobject.
     */
    @RequestMapping(value = "/registeredobjects",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Registeredobject> createRegisteredobject(@RequestBody Registeredobject registeredobject) throws URISyntaxException {
        log.debug("REST request to save Registeredobject : {}", registeredobject);
        if (registeredobject.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new registeredobject cannot already have an ID").body(null);
        }
        if(registeredobjectRepository.findByObjectName(registeredobject.getObjectName()) != null){
            return ResponseEntity.badRequest().header("Failure", "There is already object with the given name registered in the data base").body(null);
        }
        Registeredobject result = registeredobjectRepository.save(registeredobject);
        return ResponseEntity.created(new URI("/api/registeredobjects/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("registeredobject", result.getId().toString()))
                .body(result);
    }

    /**
     * PUT  /registeredobjects -> Updates an existing registeredobject.
     */
    @RequestMapping(value = "/registeredobjects",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Registeredobject> updateRegisteredobject(@RequestBody Registeredobject registeredobject) throws URISyntaxException {
        log.debug("REST request to update Registeredobject : {}", registeredobject);
        if (registeredobject.getId() == null) {
            return createRegisteredobject(registeredobject);
        }
        Registeredobject result = registeredobjectRepository.save(registeredobject);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("registeredobject", registeredobject.getId().toString()))
                .body(result);
    }

    /**
     * GET  /registeredobjects -> get all the registeredobjects.
     */
    @RequestMapping(value = "/registeredobjects",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Registeredobject>> getAllRegisteredobjects(Pageable pageable)
        throws URISyntaxException {
        Page<Registeredobject> page = registeredobjectRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/registeredobjects");
        return new ResponseEntity<List<Registeredobject>>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /registeredobjects/:id -> get the "id" registeredobject.
     */
    @RequestMapping(value = "/registeredobjects/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Registeredobject> getRegisteredobject(@PathVariable Long id, HttpServletResponse response) {
        log.debug("REST request to get Registeredobject : {}", id);
        Registeredobject registeredobject = registeredobjectRepository.findOne(id);
        if (registeredobject == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(registeredobject, HttpStatus.OK);
    }

    /**
     * DELETE  /registeredobjects/:id -> delete the "id" registeredobject.
     */
    @RequestMapping(value = "/registeredobjects/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteRegisteredobject(@PathVariable Long id) {
        log.debug("REST request to delete Registeredobject : {}", id);
        registeredobjectRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("registeredobject", id.toString())).build();
    }
}
