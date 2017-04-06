package com.platform.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.platform.domain.Agent;
import com.platform.domain.Communicativeaction;
import com.platform.domain.Feature;
import com.platform.repository.CommunicativeactionRepository;
import com.platform.repository.FeatureRepository;
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
 * REST controller for managing Feature.
 */
@RestController
@RequestMapping("/api")
public class FeatureResource {

    private final Logger log = LoggerFactory.getLogger(FeatureResource.class);

    @Inject
    private FeatureRepository featureRepository;

    @Inject
    private CommunicativeactionRepository communicativeactionRepository;

    /**
     * POST  /features -> Create a new feature.
     */
    @RequestMapping(value = "/acl_features",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Feature> createFeature(@RequestBody Communicativeaction action) throws URISyntaxException {
        action.setActionTime(new DateTime());
        communicativeactionRepository.save(action);
        String [] featureInfo = action.getContent().split(",");
        String featureName = "";
        Agent agent = action.getAction_sender();

        if(featureInfo.length != 0){
            String[] featurePair = featureInfo[0].split(":");
            featureName = featurePair[1];
        }
        if(featureRepository.findByFeatureName(featureName) != null){
            return ResponseEntity.badRequest().header("Failure", "There is already feature with a given name in data base").body(null);
        }
        Feature feature = new Feature(featureName, agent, new DateTime());
        Feature result = featureRepository.save(feature);
        return ResponseEntity.created(new URI("/api/features/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("feature", result.getId().toString()))
            .body(result);
    }

    /**
     * POST  /features -> Create a new feature.
     */
    @RequestMapping(value = "/features",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Feature> createFeature(@RequestBody Feature feature) throws URISyntaxException {
        log.debug("REST request to save Feature : {}", feature);
        if (feature.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new feature cannot already have an ID").body(null);
        }
        if(featureRepository.findByFeatureName(feature.getFeatureName()) != null){
            return ResponseEntity.badRequest().header("Failure", "There is already feature with a given name in data base").body(null);
        }
        Feature result = featureRepository.save(feature);
        return ResponseEntity.created(new URI("/api/features/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("feature", result.getId().toString()))
                .body(result);
    }

    /**
     * PUT  /features -> Updates an existing feature.
     */
    @RequestMapping(value = "/features",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Feature> updateFeature(@RequestBody Feature feature) throws URISyntaxException {
        log.debug("REST request to update Feature : {}", feature);
        if (feature.getId() == null) {
            return createFeature(feature);
        }
        Feature result = featureRepository.save(feature);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("feature", feature.getId().toString()))
                .body(result);
    }

    /**
     * GET  /features -> get all the features.
     */
    @RequestMapping(value = "/features",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Feature>> getAllFeatures(Pageable pageable)
        throws URISyntaxException {
        Page<Feature> page = featureRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/features");
        return new ResponseEntity<List<Feature>>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /features/:id -> get the "id" feature.
     */
    @RequestMapping(value = "/features/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Feature> getFeature(@PathVariable Long id, HttpServletResponse response) {
        log.debug("REST request to get Feature : {}", id);
        Feature feature = featureRepository.findOne(id);
        if (feature == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(feature, HttpStatus.OK);
    }

    /**
     * DELETE  /features/:id -> delete the "id" feature.
     */
    @RequestMapping(value = "/features/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteFeature(@PathVariable Long id) {
        log.debug("REST request to delete Feature : {}", id);
        featureRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("feature", id.toString())).build();
    }
}
