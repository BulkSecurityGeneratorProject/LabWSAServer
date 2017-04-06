package com.platform.web.rest;

import com.platform.Application;
import com.platform.domain.Agenttype;
import com.platform.repository.AgenttypeRepository;

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
 * Test class for the AgenttypeResource REST controller.
 *
 * @see AgenttypeResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class AgenttypeResourceTest {

    private static final String DEFAULT_AGENT_TYPE_NAME = "AAAAA";
    private static final String UPDATED_AGENT_TYPE_NAME = "BBBBB";

    @Inject
    private AgenttypeRepository agenttypeRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restAgenttypeMockMvc;

    private Agenttype agenttype;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        AgenttypeResource agenttypeResource = new AgenttypeResource();
        ReflectionTestUtils.setField(agenttypeResource, "agenttypeRepository", agenttypeRepository);
        this.restAgenttypeMockMvc = MockMvcBuilders.standaloneSetup(agenttypeResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        agenttype = new Agenttype();
        agenttype.setAgentTypeName(DEFAULT_AGENT_TYPE_NAME);
    }

    @Test
    @Transactional
    public void createAgenttype() throws Exception {
        int databaseSizeBeforeCreate = agenttypeRepository.findAll().size();

        // Create the Agenttype

        restAgenttypeMockMvc.perform(post("/api/agenttypes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(agenttype)))
                .andExpect(status().isCreated());

        // Validate the Agenttype in the database
        List<Agenttype> agenttypes = agenttypeRepository.findAll();
        assertThat(agenttypes).hasSize(databaseSizeBeforeCreate + 1);
        Agenttype testAgenttype = agenttypes.get(agenttypes.size() - 1);
        assertThat(testAgenttype.getAgentTypeName()).isEqualTo(DEFAULT_AGENT_TYPE_NAME);
    }

    @Test
    @Transactional
    public void getAllAgenttypes() throws Exception {
        // Initialize the database
        agenttypeRepository.saveAndFlush(agenttype);

        // Get all the agenttypes
        restAgenttypeMockMvc.perform(get("/api/agenttypes"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(agenttype.getId().intValue())))
                .andExpect(jsonPath("$.[*].agentTypeName").value(hasItem(DEFAULT_AGENT_TYPE_NAME.toString())));
    }

    @Test
    @Transactional
    public void getAgenttype() throws Exception {
        // Initialize the database
        agenttypeRepository.saveAndFlush(agenttype);

        // Get the agenttype
        restAgenttypeMockMvc.perform(get("/api/agenttypes/{id}", agenttype.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(agenttype.getId().intValue()))
            .andExpect(jsonPath("$.agentTypeName").value(DEFAULT_AGENT_TYPE_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingAgenttype() throws Exception {
        // Get the agenttype
        restAgenttypeMockMvc.perform(get("/api/agenttypes/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAgenttype() throws Exception {
        // Initialize the database
        agenttypeRepository.saveAndFlush(agenttype);

		int databaseSizeBeforeUpdate = agenttypeRepository.findAll().size();

        // Update the agenttype
        agenttype.setAgentTypeName(UPDATED_AGENT_TYPE_NAME);

        restAgenttypeMockMvc.perform(put("/api/agenttypes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(agenttype)))
                .andExpect(status().isOk());

        // Validate the Agenttype in the database
        List<Agenttype> agenttypes = agenttypeRepository.findAll();
        assertThat(agenttypes).hasSize(databaseSizeBeforeUpdate);
        Agenttype testAgenttype = agenttypes.get(agenttypes.size() - 1);
        assertThat(testAgenttype.getAgentTypeName()).isEqualTo(UPDATED_AGENT_TYPE_NAME);
    }

    @Test
    @Transactional
    public void deleteAgenttype() throws Exception {
        // Initialize the database
        agenttypeRepository.saveAndFlush(agenttype);

		int databaseSizeBeforeDelete = agenttypeRepository.findAll().size();

        // Get the agenttype
        restAgenttypeMockMvc.perform(delete("/api/agenttypes/{id}", agenttype.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Agenttype> agenttypes = agenttypeRepository.findAll();
        assertThat(agenttypes).hasSize(databaseSizeBeforeDelete - 1);
    }
}
