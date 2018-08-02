package com.leaflet.web.rest;

import com.leaflet.CivilDefanceApp;

import com.leaflet.domain.Importance;
import com.leaflet.repository.ImportanceRepository;
import com.leaflet.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ImportanceResource REST controller.
 *
 * @see ImportanceResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CivilDefanceApp.class)
public class ImportanceResourceIntTest {

    private static final String DEFAULT_DESC = "AAAAAAAAAA";
    private static final String UPDATED_DESC = "BBBBBBBBBB";

    @Autowired
    private ImportanceRepository importanceRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restImportanceMockMvc;

    private Importance importance;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ImportanceResource importanceResource = new ImportanceResource(importanceRepository);
        this.restImportanceMockMvc = MockMvcBuilders.standaloneSetup(importanceResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Importance createEntity(EntityManager em) {
        Importance importance = new Importance()
            .desc(DEFAULT_DESC);
        return importance;
    }

    @Before
    public void initTest() {
        importance = createEntity(em);
    }

    @Test
    @Transactional
    public void createImportance() throws Exception {
        int databaseSizeBeforeCreate = importanceRepository.findAll().size();

        // Create the Importance
        restImportanceMockMvc.perform(post("/api/importances")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(importance)))
            .andExpect(status().isCreated());

        // Validate the Importance in the database
        List<Importance> importanceList = importanceRepository.findAll();
        assertThat(importanceList).hasSize(databaseSizeBeforeCreate + 1);
        Importance testImportance = importanceList.get(importanceList.size() - 1);
        assertThat(testImportance.getDesc()).isEqualTo(DEFAULT_DESC);
    }

    @Test
    @Transactional
    public void createImportanceWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = importanceRepository.findAll().size();

        // Create the Importance with an existing ID
        importance.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restImportanceMockMvc.perform(post("/api/importances")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(importance)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Importance> importanceList = importanceRepository.findAll();
        assertThat(importanceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllImportances() throws Exception {
        // Initialize the database
        importanceRepository.saveAndFlush(importance);

        // Get all the importanceList
        restImportanceMockMvc.perform(get("/api/importances?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(importance.getId().intValue())))
            .andExpect(jsonPath("$.[*].desc").value(hasItem(DEFAULT_DESC.toString())));
    }

    @Test
    @Transactional
    public void getImportance() throws Exception {
        // Initialize the database
        importanceRepository.saveAndFlush(importance);

        // Get the importance
        restImportanceMockMvc.perform(get("/api/importances/{id}", importance.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(importance.getId().intValue()))
            .andExpect(jsonPath("$.desc").value(DEFAULT_DESC.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingImportance() throws Exception {
        // Get the importance
        restImportanceMockMvc.perform(get("/api/importances/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateImportance() throws Exception {
        // Initialize the database
        importanceRepository.saveAndFlush(importance);
        int databaseSizeBeforeUpdate = importanceRepository.findAll().size();

        // Update the importance
        Importance updatedImportance = importanceRepository.findOne(importance.getId());
        updatedImportance
            .desc(UPDATED_DESC);

        restImportanceMockMvc.perform(put("/api/importances")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedImportance)))
            .andExpect(status().isOk());

        // Validate the Importance in the database
        List<Importance> importanceList = importanceRepository.findAll();
        assertThat(importanceList).hasSize(databaseSizeBeforeUpdate);
        Importance testImportance = importanceList.get(importanceList.size() - 1);
        assertThat(testImportance.getDesc()).isEqualTo(UPDATED_DESC);
    }

    @Test
    @Transactional
    public void updateNonExistingImportance() throws Exception {
        int databaseSizeBeforeUpdate = importanceRepository.findAll().size();

        // Create the Importance

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restImportanceMockMvc.perform(put("/api/importances")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(importance)))
            .andExpect(status().isCreated());

        // Validate the Importance in the database
        List<Importance> importanceList = importanceRepository.findAll();
        assertThat(importanceList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteImportance() throws Exception {
        // Initialize the database
        importanceRepository.saveAndFlush(importance);
        int databaseSizeBeforeDelete = importanceRepository.findAll().size();

        // Get the importance
        restImportanceMockMvc.perform(delete("/api/importances/{id}", importance.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Importance> importanceList = importanceRepository.findAll();
        assertThat(importanceList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Importance.class);
        Importance importance1 = new Importance();
        importance1.setId(1L);
        Importance importance2 = new Importance();
        importance2.setId(importance1.getId());
        assertThat(importance1).isEqualTo(importance2);
        importance2.setId(2L);
        assertThat(importance1).isNotEqualTo(importance2);
        importance1.setId(null);
        assertThat(importance1).isNotEqualTo(importance2);
    }
}
