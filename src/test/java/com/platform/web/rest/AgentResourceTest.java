package com.platform.web.rest;

import com.platform.Application;
import com.platform.domain.Agent;
import com.platform.repository.AgentRepository;

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
 * Test class for the AgentResource REST controller.
 *
 * @see AgentResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class AgentResourceTest {

    private static final String DEFAULT_AGENT_NAME = "AAAAA";
    private static final String UPDATED_AGENT_NAME = "BBBBB";
    private static final String DEFAULT_AGENT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_AGENT_DESCRIPTION = "BBBBB";

    @Inject
    private AgentRepository agentRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restAgentMockMvc;

    private Agent agent;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        AgentResource agentResource = new AgentResource();
        ReflectionTestUtils.setField(agentResource, "agentRepository", agentRepository);
        this.restAgentMockMvc = MockMvcBuilders.standaloneSetup(agentResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        agent = new Agent();
        agent.setAgentName(DEFAULT_AGENT_NAME);
        agent.setAgentDescription(DEFAULT_AGENT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void createAgent() throws Exception {
        int databaseSizeBeforeCreate = agentRepository.findAll().size();

        // Create the Agent

        restAgentMockMvc.perform(post("/api/agents")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(agent)))
                .andExpect(status().isCreated());

        // Validate the Agent in the database
        List<Agent> agents = agentRepository.findAll();
        assertThat(agents).hasSize(databaseSizeBeforeCreate + 1);
        Agent testAgent = agents.get(agents.size() - 1);
        assertThat(testAgent.getAgentName()).isEqualTo(DEFAULT_AGENT_NAME);
        assertThat(testAgent.getAgentDescription()).isEqualTo(DEFAULT_AGENT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllAgents() throws Exception {
        // Initialize the database
        agentRepository.saveAndFlush(agent);

        // Get all the agents
        restAgentMockMvc.perform(get("/api/agents"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(agent.getId().intValue())))
                .andExpect(jsonPath("$.[*].agentName").value(hasItem(DEFAULT_AGENT_NAME.toString())))
                .andExpect(jsonPath("$.[*].agentDescription").value(hasItem(DEFAULT_AGENT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void getAgent() throws Exception {
        // Initialize the database
        agentRepository.saveAndFlush(agent);

        // Get the agent
        restAgentMockMvc.perform(get("/api/agents/{id}", agent.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(agent.getId().intValue()))
            .andExpect(jsonPath("$.agentName").value(DEFAULT_AGENT_NAME.toString()))
            .andExpect(jsonPath("$.agentDescription").value(DEFAULT_AGENT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingAgent() throws Exception {
        // Get the agent
        restAgentMockMvc.perform(get("/api/agents/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAgent() throws Exception {
        // Initialize the database
        agentRepository.saveAndFlush(agent);

		int databaseSizeBeforeUpdate = agentRepository.findAll().size();

        // Update the agent
        agent.setAgentName(UPDATED_AGENT_NAME);
        agent.setAgentDescription(UPDATED_AGENT_DESCRIPTION);

        restAgentMockMvc.perform(put("/api/agents")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(agent)))
                .andExpect(status().isOk());

        // Validate the Agent in the database
        List<Agent> agents = agentRepository.findAll();
        assertThat(agents).hasSize(databaseSizeBeforeUpdate);
        Agent testAgent = agents.get(agents.size() - 1);
        assertThat(testAgent.getAgentName()).isEqualTo(UPDATED_AGENT_NAME);
        assertThat(testAgent.getAgentDescription()).isEqualTo(UPDATED_AGENT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void deleteAgent() throws Exception {
        // Initialize the database
        agentRepository.saveAndFlush(agent);

		int databaseSizeBeforeDelete = agentRepository.findAll().size();

        // Get the agent
        restAgentMockMvc.perform(delete("/api/agents/{id}", agent.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Agent> agents = agentRepository.findAll();
        assertThat(agents).hasSize(databaseSizeBeforeDelete - 1);
    }
}
