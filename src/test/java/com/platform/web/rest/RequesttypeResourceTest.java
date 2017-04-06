package com.platform.web.rest;

import com.platform.Application;
import com.platform.domain.Requesttype;
import com.platform.repository.RequesttypeRepository;

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
 * Test class for the RequesttypeResource REST controller.
 *
 * @see RequesttypeResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class RequesttypeResourceTest {

    private static final String DEFAULT_REQUEST_TYPE_NAME = "AAAAA";
    private static final String UPDATED_REQUEST_TYPE_NAME = "BBBBB";

    @Inject
    private RequesttypeRepository requesttypeRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restRequesttypeMockMvc;

    private Requesttype requesttype;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        RequesttypeResource requesttypeResource = new RequesttypeResource();
        ReflectionTestUtils.setField(requesttypeResource, "requesttypeRepository", requesttypeRepository);
        this.restRequesttypeMockMvc = MockMvcBuilders.standaloneSetup(requesttypeResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        requesttype = new Requesttype();
        requesttype.setRequestTypeName(DEFAULT_REQUEST_TYPE_NAME);
    }

    @Test
    @Transactional
    public void createRequesttype() throws Exception {
        int databaseSizeBeforeCreate = requesttypeRepository.findAll().size();

        // Create the Requesttype

        restRequesttypeMockMvc.perform(post("/api/requesttypes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(requesttype)))
                .andExpect(status().isCreated());

        // Validate the Requesttype in the database
        List<Requesttype> requesttypes = requesttypeRepository.findAll();
        assertThat(requesttypes).hasSize(databaseSizeBeforeCreate + 1);
        Requesttype testRequesttype = requesttypes.get(requesttypes.size() - 1);
        assertThat(testRequesttype.getRequestTypeName()).isEqualTo(DEFAULT_REQUEST_TYPE_NAME);
    }

    @Test
    @Transactional
    public void getAllRequesttypes() throws Exception {
        // Initialize the database
        requesttypeRepository.saveAndFlush(requesttype);

        // Get all the requesttypes
        restRequesttypeMockMvc.perform(get("/api/requesttypes"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(requesttype.getId().intValue())))
                .andExpect(jsonPath("$.[*].requestTypeName").value(hasItem(DEFAULT_REQUEST_TYPE_NAME.toString())));
    }

    @Test
    @Transactional
    public void getRequesttype() throws Exception {
        // Initialize the database
        requesttypeRepository.saveAndFlush(requesttype);

        // Get the requesttype
        restRequesttypeMockMvc.perform(get("/api/requesttypes/{id}", requesttype.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(requesttype.getId().intValue()))
            .andExpect(jsonPath("$.requestTypeName").value(DEFAULT_REQUEST_TYPE_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingRequesttype() throws Exception {
        // Get the requesttype
        restRequesttypeMockMvc.perform(get("/api/requesttypes/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRequesttype() throws Exception {
        // Initialize the database
        requesttypeRepository.saveAndFlush(requesttype);

		int databaseSizeBeforeUpdate = requesttypeRepository.findAll().size();

        // Update the requesttype
        requesttype.setRequestTypeName(UPDATED_REQUEST_TYPE_NAME);

        restRequesttypeMockMvc.perform(put("/api/requesttypes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(requesttype)))
                .andExpect(status().isOk());

        // Validate the Requesttype in the database
        List<Requesttype> requesttypes = requesttypeRepository.findAll();
        assertThat(requesttypes).hasSize(databaseSizeBeforeUpdate);
        Requesttype testRequesttype = requesttypes.get(requesttypes.size() - 1);
        assertThat(testRequesttype.getRequestTypeName()).isEqualTo(UPDATED_REQUEST_TYPE_NAME);
    }

    @Test
    @Transactional
    public void deleteRequesttype() throws Exception {
        // Initialize the database
        requesttypeRepository.saveAndFlush(requesttype);

		int databaseSizeBeforeDelete = requesttypeRepository.findAll().size();

        // Get the requesttype
        restRequesttypeMockMvc.perform(delete("/api/requesttypes/{id}", requesttype.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Requesttype> requesttypes = requesttypeRepository.findAll();
        assertThat(requesttypes).hasSize(databaseSizeBeforeDelete - 1);
    }
}
