package com.platform.web.rest;

import com.platform.Application;
import com.platform.domain.Feature;
import com.platform.repository.FeatureRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the FeatureResource REST controller.
 *
 * @see FeatureResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class FeatureResourceTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");

    private static final String DEFAULT_FEATURE_NAME = "AAAAA";
    private static final String UPDATED_FEATURE_NAME = "BBBBB";

    private static final DateTime DEFAULT_REGISTRATION_TIME = new DateTime(0L, DateTimeZone.UTC);
    private static final DateTime UPDATED_REGISTRATION_TIME = new DateTime(DateTimeZone.UTC).withMillisOfSecond(0);
    private static final String DEFAULT_REGISTRATION_TIME_STR = dateTimeFormatter.print(DEFAULT_REGISTRATION_TIME);

    @Inject
    private FeatureRepository featureRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restFeatureMockMvc;

    private Feature feature;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        FeatureResource featureResource = new FeatureResource();
        ReflectionTestUtils.setField(featureResource, "featureRepository", featureRepository);
        this.restFeatureMockMvc = MockMvcBuilders.standaloneSetup(featureResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        feature = new Feature();
        feature.setFeatureName(DEFAULT_FEATURE_NAME);
        feature.setRegistrationTime(DEFAULT_REGISTRATION_TIME);
    }

    @Test
    @Transactional
    public void createFeature() throws Exception {
        int databaseSizeBeforeCreate = featureRepository.findAll().size();

        // Create the Feature

        restFeatureMockMvc.perform(post("/api/features")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(feature)))
                .andExpect(status().isCreated());

        // Validate the Feature in the database
        List<Feature> features = featureRepository.findAll();
        assertThat(features).hasSize(databaseSizeBeforeCreate + 1);
        Feature testFeature = features.get(features.size() - 1);
        assertThat(testFeature.getFeatureName()).isEqualTo(DEFAULT_FEATURE_NAME);
        assertThat(testFeature.getRegistrationTime().toDateTime(DateTimeZone.UTC)).isEqualTo(DEFAULT_REGISTRATION_TIME);
    }

    @Test
    @Transactional
    public void getAllFeatures() throws Exception {
        // Initialize the database
        featureRepository.saveAndFlush(feature);

        // Get all the features
        restFeatureMockMvc.perform(get("/api/features"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(feature.getId().intValue())))
                .andExpect(jsonPath("$.[*].featureName").value(hasItem(DEFAULT_FEATURE_NAME.toString())))
                .andExpect(jsonPath("$.[*].registrationTime").value(hasItem(DEFAULT_REGISTRATION_TIME_STR)));
    }

    @Test
    @Transactional
    public void getFeature() throws Exception {
        // Initialize the database
        featureRepository.saveAndFlush(feature);

        // Get the feature
        restFeatureMockMvc.perform(get("/api/features/{id}", feature.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(feature.getId().intValue()))
            .andExpect(jsonPath("$.featureName").value(DEFAULT_FEATURE_NAME.toString()))
            .andExpect(jsonPath("$.registrationTime").value(DEFAULT_REGISTRATION_TIME_STR));
    }

    @Test
    @Transactional
    public void getNonExistingFeature() throws Exception {
        // Get the feature
        restFeatureMockMvc.perform(get("/api/features/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFeature() throws Exception {
        // Initialize the database
        featureRepository.saveAndFlush(feature);

		int databaseSizeBeforeUpdate = featureRepository.findAll().size();

        // Update the feature
        feature.setFeatureName(UPDATED_FEATURE_NAME);
        feature.setRegistrationTime(UPDATED_REGISTRATION_TIME);

        restFeatureMockMvc.perform(put("/api/features")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(feature)))
                .andExpect(status().isOk());

        // Validate the Feature in the database
        List<Feature> features = featureRepository.findAll();
        assertThat(features).hasSize(databaseSizeBeforeUpdate);
        Feature testFeature = features.get(features.size() - 1);
        assertThat(testFeature.getFeatureName()).isEqualTo(UPDATED_FEATURE_NAME);
        assertThat(testFeature.getRegistrationTime().toDateTime(DateTimeZone.UTC)).isEqualTo(UPDATED_REGISTRATION_TIME);
    }

    @Test
    @Transactional
    public void deleteFeature() throws Exception {
        // Initialize the database
        featureRepository.saveAndFlush(feature);

		int databaseSizeBeforeDelete = featureRepository.findAll().size();

        // Get the feature
        restFeatureMockMvc.perform(delete("/api/features/{id}", feature.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Feature> features = featureRepository.findAll();
        assertThat(features).hasSize(databaseSizeBeforeDelete - 1);
    }
}
