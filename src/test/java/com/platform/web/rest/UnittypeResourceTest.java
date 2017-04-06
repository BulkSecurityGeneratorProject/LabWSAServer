package com.platform.web.rest;

import com.platform.Application;
import com.platform.domain.Unittype;
import com.platform.repository.UnittypeRepository;

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
 * Test class for the UnittypeResource REST controller.
 *
 * @see UnittypeResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class UnittypeResourceTest {

    private static final String DEFAULT_UNIT_TYPE_NAME = "AAAAA";
    private static final String UPDATED_UNIT_TYPE_NAME = "BBBBB";

    @Inject
    private UnittypeRepository unittypeRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restUnittypeMockMvc;

    private Unittype unittype;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        UnittypeResource unittypeResource = new UnittypeResource();
        ReflectionTestUtils.setField(unittypeResource, "unittypeRepository", unittypeRepository);
        this.restUnittypeMockMvc = MockMvcBuilders.standaloneSetup(unittypeResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        unittype = new Unittype();
        unittype.setUnitTypeName(DEFAULT_UNIT_TYPE_NAME);
    }

    @Test
    @Transactional
    public void createUnittype() throws Exception {
        int databaseSizeBeforeCreate = unittypeRepository.findAll().size();

        // Create the Unittype

        restUnittypeMockMvc.perform(post("/api/unittypes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(unittype)))
                .andExpect(status().isCreated());

        // Validate the Unittype in the database
        List<Unittype> unittypes = unittypeRepository.findAll();
        assertThat(unittypes).hasSize(databaseSizeBeforeCreate + 1);
        Unittype testUnittype = unittypes.get(unittypes.size() - 1);
        assertThat(testUnittype.getUnitTypeName()).isEqualTo(DEFAULT_UNIT_TYPE_NAME);
    }

    @Test
    @Transactional
    public void getAllUnittypes() throws Exception {
        // Initialize the database
        unittypeRepository.saveAndFlush(unittype);

        // Get all the unittypes
        restUnittypeMockMvc.perform(get("/api/unittypes"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(unittype.getId().intValue())))
                .andExpect(jsonPath("$.[*].unitTypeName").value(hasItem(DEFAULT_UNIT_TYPE_NAME.toString())));
    }

    @Test
    @Transactional
    public void getUnittype() throws Exception {
        // Initialize the database
        unittypeRepository.saveAndFlush(unittype);

        // Get the unittype
        restUnittypeMockMvc.perform(get("/api/unittypes/{id}", unittype.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(unittype.getId().intValue()))
            .andExpect(jsonPath("$.unitTypeName").value(DEFAULT_UNIT_TYPE_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingUnittype() throws Exception {
        // Get the unittype
        restUnittypeMockMvc.perform(get("/api/unittypes/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateUnittype() throws Exception {
        // Initialize the database
        unittypeRepository.saveAndFlush(unittype);

		int databaseSizeBeforeUpdate = unittypeRepository.findAll().size();

        // Update the unittype
        unittype.setUnitTypeName(UPDATED_UNIT_TYPE_NAME);

        restUnittypeMockMvc.perform(put("/api/unittypes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(unittype)))
                .andExpect(status().isOk());

        // Validate the Unittype in the database
        List<Unittype> unittypes = unittypeRepository.findAll();
        assertThat(unittypes).hasSize(databaseSizeBeforeUpdate);
        Unittype testUnittype = unittypes.get(unittypes.size() - 1);
        assertThat(testUnittype.getUnitTypeName()).isEqualTo(UPDATED_UNIT_TYPE_NAME);
    }

    @Test
    @Transactional
    public void deleteUnittype() throws Exception {
        // Initialize the database
        unittypeRepository.saveAndFlush(unittype);

		int databaseSizeBeforeDelete = unittypeRepository.findAll().size();

        // Get the unittype
        restUnittypeMockMvc.perform(delete("/api/unittypes/{id}", unittype.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Unittype> unittypes = unittypeRepository.findAll();
        assertThat(unittypes).hasSize(databaseSizeBeforeDelete - 1);
    }
}
