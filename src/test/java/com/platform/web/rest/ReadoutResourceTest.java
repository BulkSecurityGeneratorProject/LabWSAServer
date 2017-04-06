package com.platform.web.rest;

import com.platform.Application;
import com.platform.domain.Readout;
import com.platform.repository.ReadoutRepository;

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
 * Test class for the ReadoutResource REST controller.
 *
 * @see ReadoutResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class ReadoutResourceTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");


    private static final Double DEFAULT_READOUT_VALUE = 1D;
    private static final Double UPDATED_READOUT_VALUE = 2D;

    private static final DateTime DEFAULT_READOUT_TIME = new DateTime(0L, DateTimeZone.UTC);
    private static final DateTime UPDATED_READOUT_TIME = new DateTime(DateTimeZone.UTC).withMillisOfSecond(0);
    private static final String DEFAULT_READOUT_TIME_STR = dateTimeFormatter.print(DEFAULT_READOUT_TIME);

    @Inject
    private ReadoutRepository readoutRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restReadoutMockMvc;

    private Readout readout;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ReadoutResource readoutResource = new ReadoutResource();
        ReflectionTestUtils.setField(readoutResource, "readoutRepository", readoutRepository);
        this.restReadoutMockMvc = MockMvcBuilders.standaloneSetup(readoutResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        readout = new Readout();
        readout.setReadoutValue(DEFAULT_READOUT_VALUE);
        readout.setReadoutTime(DEFAULT_READOUT_TIME);
    }

    @Test
    @Transactional
    public void createReadout() throws Exception {
        int databaseSizeBeforeCreate = readoutRepository.findAll().size();

        // Create the Readout

        restReadoutMockMvc.perform(post("/api/readouts")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(readout)))
                .andExpect(status().isCreated());

        // Validate the Readout in the database
        List<Readout> readouts = readoutRepository.findAll();
        assertThat(readouts).hasSize(databaseSizeBeforeCreate + 1);
        Readout testReadout = readouts.get(readouts.size() - 1);
        assertThat(testReadout.getReadoutValue()).isEqualTo(DEFAULT_READOUT_VALUE);
        assertThat(testReadout.getReadoutTime().toDateTime(DateTimeZone.UTC)).isEqualTo(DEFAULT_READOUT_TIME);
    }

    @Test
    @Transactional
    public void getAllReadouts() throws Exception {
        // Initialize the database
        readoutRepository.saveAndFlush(readout);

        // Get all the readouts
        restReadoutMockMvc.perform(get("/api/readouts"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(readout.getId().intValue())))
                .andExpect(jsonPath("$.[*].readoutValue").value(hasItem(DEFAULT_READOUT_VALUE.doubleValue())))
                .andExpect(jsonPath("$.[*].readoutTime").value(hasItem(DEFAULT_READOUT_TIME_STR)));
    }

    @Test
    @Transactional
    public void getReadout() throws Exception {
        // Initialize the database
        readoutRepository.saveAndFlush(readout);

        // Get the readout
        restReadoutMockMvc.perform(get("/api/readouts/{id}", readout.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(readout.getId().intValue()))
            .andExpect(jsonPath("$.readoutValue").value(DEFAULT_READOUT_VALUE.doubleValue()))
            .andExpect(jsonPath("$.readoutTime").value(DEFAULT_READOUT_TIME_STR));
    }

    @Test
    @Transactional
    public void getNonExistingReadout() throws Exception {
        // Get the readout
        restReadoutMockMvc.perform(get("/api/readouts/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateReadout() throws Exception {
        // Initialize the database
        readoutRepository.saveAndFlush(readout);

		int databaseSizeBeforeUpdate = readoutRepository.findAll().size();

        // Update the readout
        readout.setReadoutValue(UPDATED_READOUT_VALUE);
        readout.setReadoutTime(UPDATED_READOUT_TIME);

        restReadoutMockMvc.perform(put("/api/readouts")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(readout)))
                .andExpect(status().isOk());

        // Validate the Readout in the database
        List<Readout> readouts = readoutRepository.findAll();
        assertThat(readouts).hasSize(databaseSizeBeforeUpdate);
        Readout testReadout = readouts.get(readouts.size() - 1);
        assertThat(testReadout.getReadoutValue()).isEqualTo(UPDATED_READOUT_VALUE);
        assertThat(testReadout.getReadoutTime().toDateTime(DateTimeZone.UTC)).isEqualTo(UPDATED_READOUT_TIME);
    }

    @Test
    @Transactional
    public void deleteReadout() throws Exception {
        // Initialize the database
        readoutRepository.saveAndFlush(readout);

		int databaseSizeBeforeDelete = readoutRepository.findAll().size();

        // Get the readout
        restReadoutMockMvc.perform(delete("/api/readouts/{id}", readout.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Readout> readouts = readoutRepository.findAll();
        assertThat(readouts).hasSize(databaseSizeBeforeDelete - 1);
    }
}
