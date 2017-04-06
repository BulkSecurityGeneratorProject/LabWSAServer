package com.platform.web.rest;

import com.platform.Application;
import com.platform.domain.Sensortype;
import com.platform.repository.SensortypeRepository;

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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the SensortypeResource REST controller.
 *
 * @see SensortypeResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class SensortypeResourceTest {

    private static final String DEFAULT_SENSOR_TYPE_NAME = "AAAAA";
    private static final String UPDATED_SENSOR_TYPE_NAME = "BBBBB";

    @Inject
    private SensortypeRepository sensortypeRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restSensortypeMockMvc;

    private Sensortype sensortype;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SensortypeResource sensortypeResource = new SensortypeResource();
        ReflectionTestUtils.setField(sensortypeResource, "sensortypeRepository", sensortypeRepository);
        this.restSensortypeMockMvc = MockMvcBuilders.standaloneSetup(sensortypeResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        sensortype = new Sensortype();
        sensortype.setSensorTypeName(DEFAULT_SENSOR_TYPE_NAME);
    }

    @Test
    @Transactional
    public void createSensortype() throws Exception {
        int databaseSizeBeforeCreate = sensortypeRepository.findAll().size();

        // Create the Sensortype

        restSensortypeMockMvc.perform(post("/api/sensortypes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(sensortype)))
                .andExpect(status().isCreated());

        // Validate the Sensortype in the database
        List<Sensortype> sensortypes = sensortypeRepository.findAll();
        assertThat(sensortypes).hasSize(databaseSizeBeforeCreate + 1);
        Sensortype testSensortype = sensortypes.get(sensortypes.size() - 1);
        assertThat(testSensortype.getSensorTypeName()).isEqualTo(DEFAULT_SENSOR_TYPE_NAME);
    }

    @Test
    @Transactional
    public void getAllSensortypes() throws Exception {
        // Initialize the database
        sensortypeRepository.saveAndFlush(sensortype);

        // Get all the sensortypes
        restSensortypeMockMvc.perform(get("/api/sensortypes"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(sensortype.getId().intValue())))
                .andExpect(jsonPath("$.[*].sensorTypeName").value(hasItem(DEFAULT_SENSOR_TYPE_NAME.toString())));
    }

    @Test
    @Transactional
    public void getSensortype() throws Exception {
        // Initialize the database
        sensortypeRepository.saveAndFlush(sensortype);

        // Get the sensortype
        restSensortypeMockMvc.perform(get("/api/sensortypes/{id}", sensortype.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(sensortype.getId().intValue()))
            .andExpect(jsonPath("$.sensorTypeName").value(DEFAULT_SENSOR_TYPE_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingSensortype() throws Exception {
        // Get the sensortype
        restSensortypeMockMvc.perform(get("/api/sensortypes/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSensortype() throws Exception {
        // Initialize the database
        sensortypeRepository.saveAndFlush(sensortype);

		int databaseSizeBeforeUpdate = sensortypeRepository.findAll().size();

        // Update the sensortype
        sensortype.setSensorTypeName(UPDATED_SENSOR_TYPE_NAME);

        restSensortypeMockMvc.perform(put("/api/sensortypes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(sensortype)))
                .andExpect(status().isOk());

        // Validate the Sensortype in the database
        List<Sensortype> sensortypes = sensortypeRepository.findAll();
        assertThat(sensortypes).hasSize(databaseSizeBeforeUpdate);
        Sensortype testSensortype = sensortypes.get(sensortypes.size() - 1);
        assertThat(testSensortype.getSensorTypeName()).isEqualTo(UPDATED_SENSOR_TYPE_NAME);
    }

    @Test
    @Transactional
    public void deleteSensortype() throws Exception {
        // Initialize the database
        sensortypeRepository.saveAndFlush(sensortype);

		int databaseSizeBeforeDelete = sensortypeRepository.findAll().size();

        // Get the sensortype
        restSensortypeMockMvc.perform(delete("/api/sensortypes/{id}", sensortype.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Sensortype> sensortypes = sensortypeRepository.findAll();
        assertThat(sensortypes).hasSize(databaseSizeBeforeDelete - 1);
    }
}
