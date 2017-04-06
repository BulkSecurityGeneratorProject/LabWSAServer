package com.platform.web.rest;

import com.platform.Application;
import com.platform.domain.Interactionprotocol;
import com.platform.repository.InteractionprotocolRepository;

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
 * Test class for the InteractionprotocolResource REST controller.
 *
 * @see InteractionprotocolResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class InteractionprotocolResourceTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");

    private static final String DEFAULT_PROTOCOL_NAME = "AAAAA";
    private static final String UPDATED_PROTOCOL_NAME = "BBBBB";
    private static final String DEFAULT_PROTOCOL_DESCRIPTION = "AAAAA";
    private static final String UPDATED_PROTOCOL_DESCRIPTION = "BBBBB";

    private static final DateTime DEFAULT_OPENING_TIME = new DateTime(0L, DateTimeZone.UTC);
    private static final DateTime UPDATED_OPENING_TIME = new DateTime(DateTimeZone.UTC).withMillisOfSecond(0);
    private static final String DEFAULT_OPENING_TIME_STR = dateTimeFormatter.print(DEFAULT_OPENING_TIME);

    @Inject
    private InteractionprotocolRepository interactionprotocolRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restInteractionprotocolMockMvc;

    private Interactionprotocol interactionprotocol;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        InteractionprotocolResource interactionprotocolResource = new InteractionprotocolResource();
        ReflectionTestUtils.setField(interactionprotocolResource, "interactionprotocolRepository", interactionprotocolRepository);
        this.restInteractionprotocolMockMvc = MockMvcBuilders.standaloneSetup(interactionprotocolResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        interactionprotocol = new Interactionprotocol();
        interactionprotocol.setProtocolName(DEFAULT_PROTOCOL_NAME);
        interactionprotocol.setProtocolDescription(DEFAULT_PROTOCOL_DESCRIPTION);
        interactionprotocol.setOpeningTime(DEFAULT_OPENING_TIME);
    }

    @Test
    @Transactional
    public void createInteractionprotocol() throws Exception {
        int databaseSizeBeforeCreate = interactionprotocolRepository.findAll().size();

        // Create the Interactionprotocol

        restInteractionprotocolMockMvc.perform(post("/api/interactionprotocols")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(interactionprotocol)))
                .andExpect(status().isCreated());

        // Validate the Interactionprotocol in the database
        List<Interactionprotocol> interactionprotocols = interactionprotocolRepository.findAll();
        assertThat(interactionprotocols).hasSize(databaseSizeBeforeCreate + 1);
        Interactionprotocol testInteractionprotocol = interactionprotocols.get(interactionprotocols.size() - 1);
        assertThat(testInteractionprotocol.getProtocolName()).isEqualTo(DEFAULT_PROTOCOL_NAME);
        assertThat(testInteractionprotocol.getProtocolDescription()).isEqualTo(DEFAULT_PROTOCOL_DESCRIPTION);
        assertThat(testInteractionprotocol.getOpeningTime().toDateTime(DateTimeZone.UTC)).isEqualTo(DEFAULT_OPENING_TIME);
    }

    @Test
    @Transactional
    public void getAllInteractionprotocols() throws Exception {
        // Initialize the database
        interactionprotocolRepository.saveAndFlush(interactionprotocol);

        // Get all the interactionprotocols
        restInteractionprotocolMockMvc.perform(get("/api/interactionprotocols"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(interactionprotocol.getId().intValue())))
                .andExpect(jsonPath("$.[*].protocolName").value(hasItem(DEFAULT_PROTOCOL_NAME.toString())))
                .andExpect(jsonPath("$.[*].protocolDescription").value(hasItem(DEFAULT_PROTOCOL_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].openingTime").value(hasItem(DEFAULT_OPENING_TIME_STR)));
    }

    @Test
    @Transactional
    public void getInteractionprotocol() throws Exception {
        // Initialize the database
        interactionprotocolRepository.saveAndFlush(interactionprotocol);

        // Get the interactionprotocol
        restInteractionprotocolMockMvc.perform(get("/api/interactionprotocols/{id}", interactionprotocol.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(interactionprotocol.getId().intValue()))
            .andExpect(jsonPath("$.protocolName").value(DEFAULT_PROTOCOL_NAME.toString()))
            .andExpect(jsonPath("$.protocolDescription").value(DEFAULT_PROTOCOL_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.openingTime").value(DEFAULT_OPENING_TIME_STR));
    }

    @Test
    @Transactional
    public void getNonExistingInteractionprotocol() throws Exception {
        // Get the interactionprotocol
        restInteractionprotocolMockMvc.perform(get("/api/interactionprotocols/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateInteractionprotocol() throws Exception {
        // Initialize the database
        interactionprotocolRepository.saveAndFlush(interactionprotocol);

		int databaseSizeBeforeUpdate = interactionprotocolRepository.findAll().size();

        // Update the interactionprotocol
        interactionprotocol.setProtocolName(UPDATED_PROTOCOL_NAME);
        interactionprotocol.setProtocolDescription(UPDATED_PROTOCOL_DESCRIPTION);
        interactionprotocol.setOpeningTime(UPDATED_OPENING_TIME);

        restInteractionprotocolMockMvc.perform(put("/api/interactionprotocols")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(interactionprotocol)))
                .andExpect(status().isOk());

        // Validate the Interactionprotocol in the database
        List<Interactionprotocol> interactionprotocols = interactionprotocolRepository.findAll();
        assertThat(interactionprotocols).hasSize(databaseSizeBeforeUpdate);
        Interactionprotocol testInteractionprotocol = interactionprotocols.get(interactionprotocols.size() - 1);
        assertThat(testInteractionprotocol.getProtocolName()).isEqualTo(UPDATED_PROTOCOL_NAME);
        assertThat(testInteractionprotocol.getProtocolDescription()).isEqualTo(UPDATED_PROTOCOL_DESCRIPTION);
        assertThat(testInteractionprotocol.getOpeningTime().toDateTime(DateTimeZone.UTC)).isEqualTo(UPDATED_OPENING_TIME);
    }

    @Test
    @Transactional
    public void deleteInteractionprotocol() throws Exception {
        // Initialize the database
        interactionprotocolRepository.saveAndFlush(interactionprotocol);

		int databaseSizeBeforeDelete = interactionprotocolRepository.findAll().size();

        // Get the interactionprotocol
        restInteractionprotocolMockMvc.perform(delete("/api/interactionprotocols/{id}", interactionprotocol.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Interactionprotocol> interactionprotocols = interactionprotocolRepository.findAll();
        assertThat(interactionprotocols).hasSize(databaseSizeBeforeDelete - 1);
    }
}
