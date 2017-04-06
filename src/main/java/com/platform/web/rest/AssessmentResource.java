package com.platform.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.platform.domain.*;
import com.platform.repository.AssessmentRepository;
import com.platform.repository.CommunicativeactionRepository;
import com.platform.repository.FeatureRepository;
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
 * REST controller for managing Assessment.
 */
@RestController
@RequestMapping("/api")
public class AssessmentResource {

    private final Logger log = LoggerFactory.getLogger(AssessmentResource.class);

    @Inject
    private AssessmentRepository assessmentRepository;

    @Inject
    private FeatureRepository featureRepository;

    @Inject
    private RegisteredobjectRepository registeredobjectRepository;

    @Inject
    private CommunicativeactionRepository communicativeactionRepository;
    /**
     * POST  /assessments -> Create a new assessment.
     */
    @RequestMapping(value = "/acl_assessments",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Assessment> createAssessment(@RequestBody Communicativeaction action) throws URISyntaxException {
        action.setActionTime(new DateTime());
        communicativeactionRepository.save(action);
        String [] assessmentInfo = action.getContent().split(",");
        Registeredobject object = null;
        Feature feature = null;
        Double assessmentValue = 0.0;
        Agent agent = action.getAction_sender();
        for(String info : assessmentInfo){
            String[] assessmentPair = info.split(":");
            switch(assessmentPair[0]){
                case "object":{
                    object = registeredobjectRepository.findByObjectName(assessmentPair[1]);
                    break;
                }
                case "feature":{
                    feature = featureRepository.findByFeatureName(assessmentPair[1]);
                    break;
                }
                case "assessmentValue":{
                    assessmentValue = Double.parseDouble(assessmentPair[1]);
                    break;
                }
            }
        }
        if(object == null){
           return ResponseEntity.badRequest().header("Failure", "Assessed object was not found in data base").body(null);
        }
        if(feature == null){
          return ResponseEntity.badRequest().header("Failure", "Assessed feature was not found in data base").body(null);
        }
        Assessment assessment = new Assessment(assessmentValue, feature, object, agent, new DateTime());
        Assessment result = assessmentRepository.save(assessment);
        return ResponseEntity.created(new URI("/api/assessments/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("assessment", result.getId().toString()))
            .body(result);
    }

    /**
     * POST  /assessments -> Create a new assessment.
     */
    @RequestMapping(value = "/assessments",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Assessment> createAssessment(@RequestBody Assessment assessment) throws URISyntaxException {
        log.debug("REST request to save Assessment : {}", assessment);
        if (assessment.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new assessment cannot already have an ID").body(null);
        }
        Assessment result = assessmentRepository.save(assessment);
        return ResponseEntity.created(new URI("/api/assessments/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("assessment", result.getId().toString()))
                .body(result);
    }

    /**
     * PUT  /assessments -> Updates an existing assessment.
     */
    @RequestMapping(value = "/assessments",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Assessment> updateAssessment(@RequestBody Assessment assessment) throws URISyntaxException {
        log.debug("REST request to update Assessment : {}", assessment);
        if (assessment.getId() == null) {
            return createAssessment(assessment);
        }
        Assessment result = assessmentRepository.save(assessment);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("assessment", assessment.getId().toString()))
                .body(result);
    }

    /**
     * GET  /assessments -> get all the assessments.
     */
    @RequestMapping(value = "/assessments",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Assessment>> getAllAssessments(Pageable pageable)
        throws URISyntaxException {
        Page<Assessment> page = assessmentRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/assessments");
        return new ResponseEntity<List<Assessment>>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /assessments/:id -> get the "id" assessment.
     */
    @RequestMapping(value = "/assessments/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Assessment> getAssessment(@PathVariable Long id, HttpServletResponse response) {
        log.debug("REST request to get Assessment : {}", id);
        Assessment assessment = assessmentRepository.findOne(id);
        if (assessment == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(assessment, HttpStatus.OK);
    }

    /**
     * DELETE  /assessments/:id -> delete the "id" assessment.
     */
    @RequestMapping(value = "/assessments/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteAssessment(@PathVariable Long id) {
        log.debug("REST request to delete Assessment : {}", id);
        assessmentRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("assessment", id.toString())).build();
    }
}
