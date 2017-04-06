package com.platform.web.rest;

import com.platform.Application;
import com.platform.domain.Registeredobject;
import com.platform.repository.RegisteredobjectRepository;

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
 * Test class for the RegisteredobjectResource REST controller.
 *
 * @see RegisteredobjectResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class RegisteredobjectResourceTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");

    private static final String DEFAULT_OBJECT_NAME = "AAAAA";
    private static final String UPDATED_OBJECT_NAME = "BBBBB";
    private static final String DEFAULT_OBJECT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_OBJECT_DESCRIPTION = "BBBBB";

    private static final DateTime DEFAULT_REGISTRATION_TIME = new DateTime(0L, DateTimeZone.UTC);
    private static final DateTime UPDATED_REGISTRATION_TIME = new DateTime(DateTimeZone.UTC).withMillisOfSecond(0);
    private static final String DEFAULT_REGISTRATION_TIME_STR = dateTimeFormatter.print(DEFAULT_REGISTRATION_TIME);

    @Inject
    private RegisteredobjectRepository registeredobjectRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restRegisteredobjectMockMvc;

    private Registeredobject registeredobject;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        RegisteredobjectResource registeredobjectResource = new RegisteredobjectResource();
        ReflectionTestUtils.setField(registeredobjectResource, "registeredobjectRepository", registeredobjectRepository);
        this.restRegisteredobjectMockMvc = MockMvcBuilders.standaloneSetup(registeredobjectResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        registeredobject = new Registeredobject();
        registeredobject.setObjectName(DEFAULT_OBJECT_NAME);
        registeredobject.setObjectDescription(DEFAULT_OBJECT_DESCRIPTION);
        registeredobject.setRegistrationTime(DEFAULT_REGISTRATION_TIME);
    }

    @Test
    @Transactional
    public void createRegisteredobject() throws Exception {
        int databaseSizeBeforeCreate = registeredobjectRepository.findAll().size();

        // Create the Registeredobject

        restRegisteredobjectMockMvc.perform(post("/api/registeredobjects")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(registeredobject)))
                .andExpect(status().isCreated());

        // Validate the Registeredobject in the database
        List<Registeredobject> registeredobjects = registeredobjectRepository.findAll();
        assertThat(registeredobjects).hasSize(databaseSizeBeforeCreate + 1);
        Registeredobject testRegisteredobject = registeredobjects.get(registeredobjects.size() - 1);
        assertThat(testRegisteredobject.getObjectName()).isEqualTo(DEFAULT_OBJECT_NAME);
        assertThat(testRegisteredobject.getObjectDescription()).isEqualTo(DEFAULT_OBJECT_DESCRIPTION);
        assertThat(testRegisteredobject.getRegistrationTime().toDateTime(DateTimeZone.UTC)).isEqualTo(DEFAULT_REGISTRATION_TIME);
    }

    @Test
    @Transactional
    public void getAllRegisteredobjects() throws Exception {
        // Initialize the database
        registeredobjectRepository.saveAndFlush(registeredobject);

        // Get all the registeredobjects
        restRegisteredobjectMockMvc.perform(get("/api/registeredobjects"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(registeredobject.getId().intValue())))
                .andExpect(jsonPath("$.[*].objectName").value(hasItem(DEFAULT_OBJECT_NAME.toString())))
                .andExpect(jsonPath("$.[*].objectDescription").value(hasItem(DEFAULT_OBJECT_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].registrationTime").value(hasItem(DEFAULT_REGISTRATION_TIME_STR)));
    }

    @Test
    @Transactional
    public void getRegisteredobject() throws Exception {
        // Initialize the database
        registeredobjectRepository.saveAndFlush(registeredobject);

        // Get the registeredobject
        restRegisteredobjectMockMvc.perform(get("/api/registeredobjects/{id}", registeredobject.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(registeredobject.getId().intValue()))
            .andExpect(jsonPath("$.objectName").value(DEFAULT_OBJECT_NAME.toString()))
            .andExpect(jsonPath("$.objectDescription").value(DEFAULT_OBJECT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.registrationTime").value(DEFAULT_REGISTRATION_TIME_STR));
    }

    @Test
    @Transactional
    public void getNonExistingRegisteredobject() throws Exception {
        // Get the registeredobject
        restRegisteredobjectMockMvc.perform(get("/api/registeredobjects/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRegisteredobject() throws Exception {
        // Initialize the database
        registeredobjectRepository.saveAndFlush(registeredobject);

		int databaseSizeBeforeUpdate = registeredobjectRepository.findAll().size();

        // Update the registeredobject
        registeredobject.setObjectName(UPDATED_OBJECT_NAME);
        registeredobject.setObjectDescription(UPDATED_OBJECT_DESCRIPTION);
        registeredobject.setRegistrationTime(UPDATED_REGISTRATION_TIME);

        restRegisteredobjectMockMvc.perform(put("/api/registeredobjects")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(registeredobject)))
                .andExpect(status().isOk());

        // Validate the Registeredobject in the database
        List<Registeredobject> registeredobjects = registeredobjectRepository.findAll();
        assertThat(registeredobjects).hasSize(databaseSizeBeforeUpdate);
        Registeredobject testRegisteredobject = registeredobjects.get(registeredobjects.size() - 1);
        assertThat(testRegisteredobject.getObjectName()).isEqualTo(UPDATED_OBJECT_NAME);
        assertThat(testRegisteredobject.getObjectDescription()).isEqualTo(UPDATED_OBJECT_DESCRIPTION);
        assertThat(testRegisteredobject.getRegistrationTime().toDateTime(DateTimeZone.UTC)).isEqualTo(UPDATED_REGISTRATION_TIME);
    }

    @Test
    @Transactional
    public void deleteRegisteredobject() throws Exception {
        // Initialize the database
        registeredobjectRepository.saveAndFlush(registeredobject);

		int databaseSizeBeforeDelete = registeredobjectRepository.findAll().size();

        // Get the registeredobject
        restRegisteredobjectMockMvc.perform(delete("/api/registeredobjects/{id}", registeredobject.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Registeredobject> registeredobjects = registeredobjectRepository.findAll();
        assertThat(registeredobjects).hasSize(databaseSizeBeforeDelete - 1);
    }
}
