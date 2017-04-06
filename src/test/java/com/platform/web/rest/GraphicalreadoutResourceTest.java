package com.platform.web.rest;

import com.platform.Application;
import com.platform.domain.Graphicalreadout;
import com.platform.repository.GraphicalreadoutRepository;

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
import org.springframework.util.Base64Utils;

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
 * Test class for the GraphicalreadoutResource REST controller.
 *
 * @see GraphicalreadoutResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class GraphicalreadoutResourceTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");


    private static final byte[] DEFAULT_IMAGE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_IMAGE = TestUtil.createByteArray(2, "1");
    private static final String DEFAULT_IMAGE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_IMAGE_CONTENT_TYPE = "image/png";

    private static final DateTime DEFAULT_READOUT_TIME = new DateTime(0L, DateTimeZone.UTC);
    private static final DateTime UPDATED_READOUT_TIME = new DateTime(DateTimeZone.UTC).withMillisOfSecond(0);
    private static final String DEFAULT_READOUT_TIME_STR = dateTimeFormatter.print(DEFAULT_READOUT_TIME);

    @Inject
    private GraphicalreadoutRepository graphicalreadoutRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restGraphicalreadoutMockMvc;

    private Graphicalreadout graphicalreadout;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        GraphicalreadoutResource graphicalreadoutResource = new GraphicalreadoutResource();
        ReflectionTestUtils.setField(graphicalreadoutResource, "graphicalreadoutRepository", graphicalreadoutRepository);
        this.restGraphicalreadoutMockMvc = MockMvcBuilders.standaloneSetup(graphicalreadoutResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        graphicalreadout = new Graphicalreadout();
        graphicalreadout.setImage(DEFAULT_IMAGE);
        graphicalreadout.setReadoutTime(DEFAULT_READOUT_TIME);
    }

    @Test
    @Transactional
    public void createGraphicalreadout() throws Exception {
        int databaseSizeBeforeCreate = graphicalreadoutRepository.findAll().size();

        // Create the Graphicalreadout

        restGraphicalreadoutMockMvc.perform(post("/api/graphicalreadouts")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(graphicalreadout)))
                .andExpect(status().isCreated());

        // Validate the Graphicalreadout in the database
        List<Graphicalreadout> graphicalreadouts = graphicalreadoutRepository.findAll();
        assertThat(graphicalreadouts).hasSize(databaseSizeBeforeCreate + 1);
        Graphicalreadout testGraphicalreadout = graphicalreadouts.get(graphicalreadouts.size() - 1);
        assertThat(testGraphicalreadout.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testGraphicalreadout.getReadoutTime().toDateTime(DateTimeZone.UTC)).isEqualTo(DEFAULT_READOUT_TIME);
    }

    @Test
    @Transactional
    public void getAllGraphicalreadouts() throws Exception {
        // Initialize the database
        graphicalreadoutRepository.saveAndFlush(graphicalreadout);

        // Get all the graphicalreadouts
        restGraphicalreadoutMockMvc.perform(get("/api/graphicalreadouts"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(graphicalreadout.getId().intValue())))
                .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))))
                .andExpect(jsonPath("$.[*].readoutTime").value(hasItem(DEFAULT_READOUT_TIME_STR)));
    }

    @Test
    @Transactional
    public void getGraphicalreadout() throws Exception {
        // Initialize the database
        graphicalreadoutRepository.saveAndFlush(graphicalreadout);

        // Get the graphicalreadout
        restGraphicalreadoutMockMvc.perform(get("/api/graphicalreadouts/{id}", graphicalreadout.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(graphicalreadout.getId().intValue()))
            .andExpect(jsonPath("$.image").value(Base64Utils.encodeToString(DEFAULT_IMAGE)))
            .andExpect(jsonPath("$.readoutTime").value(DEFAULT_READOUT_TIME_STR));
    }

    @Test
    @Transactional
    public void getNonExistingGraphicalreadout() throws Exception {
        // Get the graphicalreadout
        restGraphicalreadoutMockMvc.perform(get("/api/graphicalreadouts/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateGraphicalreadout() throws Exception {
        // Initialize the database
        graphicalreadoutRepository.saveAndFlush(graphicalreadout);

		int databaseSizeBeforeUpdate = graphicalreadoutRepository.findAll().size();

        // Update the graphicalreadout
        graphicalreadout.setImage(UPDATED_IMAGE);
        graphicalreadout.setReadoutTime(UPDATED_READOUT_TIME);

        restGraphicalreadoutMockMvc.perform(put("/api/graphicalreadouts")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(graphicalreadout)))
                .andExpect(status().isOk());

        // Validate the Graphicalreadout in the database
        List<Graphicalreadout> graphicalreadouts = graphicalreadoutRepository.findAll();
        assertThat(graphicalreadouts).hasSize(databaseSizeBeforeUpdate);
        Graphicalreadout testGraphicalreadout = graphicalreadouts.get(graphicalreadouts.size() - 1);
        assertThat(testGraphicalreadout.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testGraphicalreadout.getReadoutTime().toDateTime(DateTimeZone.UTC)).isEqualTo(UPDATED_READOUT_TIME);
    }

    @Test
    @Transactional
    public void deleteGraphicalreadout() throws Exception {
        // Initialize the database
        graphicalreadoutRepository.saveAndFlush(graphicalreadout);

		int databaseSizeBeforeDelete = graphicalreadoutRepository.findAll().size();

        // Get the graphicalreadout
        restGraphicalreadoutMockMvc.perform(delete("/api/graphicalreadouts/{id}", graphicalreadout.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Graphicalreadout> graphicalreadouts = graphicalreadoutRepository.findAll();
        assertThat(graphicalreadouts).hasSize(databaseSizeBeforeDelete - 1);
    }
}
