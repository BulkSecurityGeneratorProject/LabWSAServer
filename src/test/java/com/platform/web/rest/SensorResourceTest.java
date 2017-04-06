package com.platform.web.rest;

import com.platform.Application;
import com.platform.domain.Sensor;
import com.platform.repository.SensorRepository;

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
 * Test class for the SensorResource REST controller.
 *
 * @see SensorResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class SensorResourceTest {

    private static final String DEFAULT_SENSOR_NAME = "AAAAA";
    private static final String UPDATED_SENSOR_NAME = "BBBBB";

    private static final Double DEFAULT_SENSOR_ACCURACY = 1D;
    private static final Double UPDATED_SENSOR_ACCURACY = 2D;

    @Inject
    private SensorRepository sensorRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restSensorMockMvc;

    private Sensor sensor;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SensorResource sensorResource = new SensorResource();
        ReflectionTestUtils.setField(sensorResource, "sensorRepository", sensorRepository);
        this.restSensorMockMvc = MockMvcBuilders.standaloneSetup(sensorResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        sensor = new Sensor();
        sensor.setSensorName(DEFAULT_SENSOR_NAME);
        sensor.setSensorAccuracy(DEFAULT_SENSOR_ACCURACY);
    }

    @Test
    @Transactional
    public void createSensor() throws Exception {
        int databaseSizeBeforeCreate = sensorRepository.findAll().size();

        // Create the Sensor

        restSensorMockMvc.perform(post("/api/sensors")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(sensor)))
                .andExpect(status().isCreated());

        // Validate the Sensor in the database
        List<Sensor> sensors = sensorRepository.findAll();
        assertThat(sensors).hasSize(databaseSizeBeforeCreate + 1);
        Sensor testSensor = sensors.get(sensors.size() - 1);
        assertThat(testSensor.getSensorName()).isEqualTo(DEFAULT_SENSOR_NAME);
        assertThat(testSensor.getSensorAccuracy()).isEqualTo(DEFAULT_SENSOR_ACCURACY);
    }

    @Test
    @Transactional
    public void getAllSensors() throws Exception {
        // Initialize the database
        sensorRepository.saveAndFlush(sensor);

        // Get all the sensors
        restSensorMockMvc.perform(get("/api/sensors"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(sensor.getId().intValue())))
                .andExpect(jsonPath("$.[*].sensorName").value(hasItem(DEFAULT_SENSOR_NAME.toString())))
                .andExpect(jsonPath("$.[*].sensorAccuracy").value(hasItem(DEFAULT_SENSOR_ACCURACY.doubleValue())));
    }

    @Test
    @Transactional
    public void getSensor() throws Exception {
        // Initialize the database
        sensorRepository.saveAndFlush(sensor);

        // Get the sensor
        restSensorMockMvc.perform(get("/api/sensors/{id}", sensor.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(sensor.getId().intValue()))
            .andExpect(jsonPath("$.sensorName").value(DEFAULT_SENSOR_NAME.toString()))
            .andExpect(jsonPath("$.sensorAccuracy").value(DEFAULT_SENSOR_ACCURACY.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingSensor() throws Exception {
        // Get the sensor
        restSensorMockMvc.perform(get("/api/sensors/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSensor() throws Exception {
        // Initialize the database
        sensorRepository.saveAndFlush(sensor);

		int databaseSizeBeforeUpdate = sensorRepository.findAll().size();

        // Update the sensor
        sensor.setSensorName(UPDATED_SENSOR_NAME);
        sensor.setSensorAccuracy(UPDATED_SENSOR_ACCURACY);

        restSensorMockMvc.perform(put("/api/sensors")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(sensor)))
                .andExpect(status().isOk());

        // Validate the Sensor in the database
        List<Sensor> sensors = sensorRepository.findAll();
        assertThat(sensors).hasSize(databaseSizeBeforeUpdate);
        Sensor testSensor = sensors.get(sensors.size() - 1);
        assertThat(testSensor.getSensorName()).isEqualTo(UPDATED_SENSOR_NAME);
        assertThat(testSensor.getSensorAccuracy()).isEqualTo(UPDATED_SENSOR_ACCURACY);
    }

    @Test
    @Transactional
    public void deleteSensor() throws Exception {
        // Initialize the database
        sensorRepository.saveAndFlush(sensor);

		int databaseSizeBeforeDelete = sensorRepository.findAll().size();

        // Get the sensor
        restSensorMockMvc.perform(delete("/api/sensors/{id}", sensor.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Sensor> sensors = sensorRepository.findAll();
        assertThat(sensors).hasSize(databaseSizeBeforeDelete - 1);
    }
}
