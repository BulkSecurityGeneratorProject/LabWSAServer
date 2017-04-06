package com.platform.web.rest;

import com.platform.Application;
import com.platform.domain.Presencestatus;
import com.platform.repository.PresencestatusRepository;

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
 * Test class for the PresencestatusResource REST controller.
 *
 * @see PresencestatusResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class PresencestatusResourceTest {

    private static final String DEFAULT_PRESENCE_STATUS_NAME = "AAAAA";
    private static final String UPDATED_PRESENCE_STATUS_NAME = "BBBBB";

    @Inject
    private PresencestatusRepository presencestatusRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restPresencestatusMockMvc;

    private Presencestatus presencestatus;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PresencestatusResource presencestatusResource = new PresencestatusResource();
        ReflectionTestUtils.setField(presencestatusResource, "presencestatusRepository", presencestatusRepository);
        this.restPresencestatusMockMvc = MockMvcBuilders.standaloneSetup(presencestatusResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        presencestatus = new Presencestatus();
        presencestatus.setPresenceStatusName(DEFAULT_PRESENCE_STATUS_NAME);
    }

    @Test
    @Transactional
    public void createPresencestatus() throws Exception {
        int databaseSizeBeforeCreate = presencestatusRepository.findAll().size();

        // Create the Presencestatus

        restPresencestatusMockMvc.perform(post("/api/presencestatuss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(presencestatus)))
                .andExpect(status().isCreated());

        // Validate the Presencestatus in the database
        List<Presencestatus> presencestatuss = presencestatusRepository.findAll();
        assertThat(presencestatuss).hasSize(databaseSizeBeforeCreate + 1);
        Presencestatus testPresencestatus = presencestatuss.get(presencestatuss.size() - 1);
        assertThat(testPresencestatus.getPresenceStatusName()).isEqualTo(DEFAULT_PRESENCE_STATUS_NAME);
    }

    @Test
    @Transactional
    public void getAllPresencestatuss() throws Exception {
        // Initialize the database
        presencestatusRepository.saveAndFlush(presencestatus);

        // Get all the presencestatuss
        restPresencestatusMockMvc.perform(get("/api/presencestatuss"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(presencestatus.getId().intValue())))
                .andExpect(jsonPath("$.[*].presenceStatusName").value(hasItem(DEFAULT_PRESENCE_STATUS_NAME.toString())));
    }

    @Test
    @Transactional
    public void getPresencestatus() throws Exception {
        // Initialize the database
        presencestatusRepository.saveAndFlush(presencestatus);

        // Get the presencestatus
        restPresencestatusMockMvc.perform(get("/api/presencestatuss/{id}", presencestatus.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(presencestatus.getId().intValue()))
            .andExpect(jsonPath("$.presenceStatusName").value(DEFAULT_PRESENCE_STATUS_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPresencestatus() throws Exception {
        // Get the presencestatus
        restPresencestatusMockMvc.perform(get("/api/presencestatuss/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePresencestatus() throws Exception {
        // Initialize the database
        presencestatusRepository.saveAndFlush(presencestatus);

		int databaseSizeBeforeUpdate = presencestatusRepository.findAll().size();

        // Update the presencestatus
        presencestatus.setPresenceStatusName(UPDATED_PRESENCE_STATUS_NAME);

        restPresencestatusMockMvc.perform(put("/api/presencestatuss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(presencestatus)))
                .andExpect(status().isOk());

        // Validate the Presencestatus in the database
        List<Presencestatus> presencestatuss = presencestatusRepository.findAll();
        assertThat(presencestatuss).hasSize(databaseSizeBeforeUpdate);
        Presencestatus testPresencestatus = presencestatuss.get(presencestatuss.size() - 1);
        assertThat(testPresencestatus.getPresenceStatusName()).isEqualTo(UPDATED_PRESENCE_STATUS_NAME);
    }

    @Test
    @Transactional
    public void deletePresencestatus() throws Exception {
        // Initialize the database
        presencestatusRepository.saveAndFlush(presencestatus);

		int databaseSizeBeforeDelete = presencestatusRepository.findAll().size();

        // Get the presencestatus
        restPresencestatusMockMvc.perform(delete("/api/presencestatuss/{id}", presencestatus.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Presencestatus> presencestatuss = presencestatusRepository.findAll();
        assertThat(presencestatuss).hasSize(databaseSizeBeforeDelete - 1);
    }
}
