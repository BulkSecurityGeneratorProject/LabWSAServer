package com.platform.web.rest;

import com.platform.Application;
import com.platform.domain.Assessment;
import com.platform.repository.AssessmentRepository;

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
 * Test class for the AssessmentResource REST controller.
 *
 * @see AssessmentResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class AssessmentResourceTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");


    private static final Double DEFAULT_ASSESSMENT_VALUE = 1D;
    private static final Double UPDATED_ASSESSMENT_VALUE = 2D;

    private static final DateTime DEFAULT_ESTIMATION_TIME = new DateTime(0L, DateTimeZone.UTC);
    private static final DateTime UPDATED_ESTIMATION_TIME = new DateTime(DateTimeZone.UTC).withMillisOfSecond(0);
    private static final String DEFAULT_ESTIMATION_TIME_STR = dateTimeFormatter.print(DEFAULT_ESTIMATION_TIME);

    @Inject
    private AssessmentRepository assessmentRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restAssessmentMockMvc;

    private Assessment assessment;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        AssessmentResource assessmentResource = new AssessmentResource();
        ReflectionTestUtils.setField(assessmentResource, "assessmentRepository", assessmentRepository);
        this.restAssessmentMockMvc = MockMvcBuilders.standaloneSetup(assessmentResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        assessment = new Assessment();
        assessment.setAssessmentValue(DEFAULT_ASSESSMENT_VALUE);
        assessment.setEstimationTime(DEFAULT_ESTIMATION_TIME);
    }

    @Test
    @Transactional
    public void createAssessment() throws Exception {
        int databaseSizeBeforeCreate = assessmentRepository.findAll().size();

        // Create the Assessment

        restAssessmentMockMvc.perform(post("/api/assessments")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(assessment)))
                .andExpect(status().isCreated());

        // Validate the Assessment in the database
        List<Assessment> assessments = assessmentRepository.findAll();
        assertThat(assessments).hasSize(databaseSizeBeforeCreate + 1);
        Assessment testAssessment = assessments.get(assessments.size() - 1);
        assertThat(testAssessment.getAssessmentValue()).isEqualTo(DEFAULT_ASSESSMENT_VALUE);
        assertThat(testAssessment.getEstimationTime().toDateTime(DateTimeZone.UTC)).isEqualTo(DEFAULT_ESTIMATION_TIME);
    }

    @Test
    @Transactional
    public void getAllAssessments() throws Exception {
        // Initialize the database
        assessmentRepository.saveAndFlush(assessment);

        // Get all the assessments
        restAssessmentMockMvc.perform(get("/api/assessments"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(assessment.getId().intValue())))
                .andExpect(jsonPath("$.[*].assessmentValue").value(hasItem(DEFAULT_ASSESSMENT_VALUE.doubleValue())))
                .andExpect(jsonPath("$.[*].estimationTime").value(hasItem(DEFAULT_ESTIMATION_TIME_STR)));
    }

    @Test
    @Transactional
    public void getAssessment() throws Exception {
        // Initialize the database
        assessmentRepository.saveAndFlush(assessment);

        // Get the assessment
        restAssessmentMockMvc.perform(get("/api/assessments/{id}", assessment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(assessment.getId().intValue()))
            .andExpect(jsonPath("$.assessmentValue").value(DEFAULT_ASSESSMENT_VALUE.doubleValue()))
            .andExpect(jsonPath("$.estimationTime").value(DEFAULT_ESTIMATION_TIME_STR));
    }

    @Test
    @Transactional
    public void getNonExistingAssessment() throws Exception {
        // Get the assessment
        restAssessmentMockMvc.perform(get("/api/assessments/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAssessment() throws Exception {
        // Initialize the database
        assessmentRepository.saveAndFlush(assessment);

		int databaseSizeBeforeUpdate = assessmentRepository.findAll().size();

        // Update the assessment
        assessment.setAssessmentValue(UPDATED_ASSESSMENT_VALUE);
        assessment.setEstimationTime(UPDATED_ESTIMATION_TIME);

        restAssessmentMockMvc.perform(put("/api/assessments")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(assessment)))
                .andExpect(status().isOk());

        // Validate the Assessment in the database
        List<Assessment> assessments = assessmentRepository.findAll();
        assertThat(assessments).hasSize(databaseSizeBeforeUpdate);
        Assessment testAssessment = assessments.get(assessments.size() - 1);
        assertThat(testAssessment.getAssessmentValue()).isEqualTo(UPDATED_ASSESSMENT_VALUE);
        assertThat(testAssessment.getEstimationTime().toDateTime(DateTimeZone.UTC)).isEqualTo(UPDATED_ESTIMATION_TIME);
    }

    @Test
    @Transactional
    public void deleteAssessment() throws Exception {
        // Initialize the database
        assessmentRepository.saveAndFlush(assessment);

		int databaseSizeBeforeDelete = assessmentRepository.findAll().size();

        // Get the assessment
        restAssessmentMockMvc.perform(delete("/api/assessments/{id}", assessment.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Assessment> assessments = assessmentRepository.findAll();
        assertThat(assessments).hasSize(databaseSizeBeforeDelete - 1);
    }
}
