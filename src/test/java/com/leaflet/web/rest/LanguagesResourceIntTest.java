package com.leaflet.web.rest;

import com.leaflet.CivilDefanceApp;

import com.leaflet.domain.Languages;
import com.leaflet.repository.LanguagesRepository;
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
 * Test class for the LanguagesResource REST controller.
 *
 * @see LanguagesResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CivilDefanceApp.class)
public class LanguagesResourceIntTest {

    private static final String DEFAULT_LANGUAGE_DESC = "AAAAAAAAAA";
    private static final String UPDATED_LANGUAGE_DESC = "BBBBBBBBBB";

    @Autowired
    private LanguagesRepository languagesRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restLanguagesMockMvc;

    private Languages languages;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        LanguagesResource languagesResource = new LanguagesResource(languagesRepository);
        this.restLanguagesMockMvc = MockMvcBuilders.standaloneSetup(languagesResource)
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
    public static Languages createEntity(EntityManager em) {
        Languages languages = new Languages()
            .languageDesc(DEFAULT_LANGUAGE_DESC);
        return languages;
    }

    @Before
    public void initTest() {
        languages = createEntity(em);
    }

    @Test
    @Transactional
    public void createLanguages() throws Exception {
        int databaseSizeBeforeCreate = languagesRepository.findAll().size();

        // Create the Languages
        restLanguagesMockMvc.perform(post("/api/languages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(languages)))
            .andExpect(status().isCreated());

        // Validate the Languages in the database
        List<Languages> languagesList = languagesRepository.findAll();
        assertThat(languagesList).hasSize(databaseSizeBeforeCreate + 1);
        Languages testLanguages = languagesList.get(languagesList.size() - 1);
        assertThat(testLanguages.getLanguageDesc()).isEqualTo(DEFAULT_LANGUAGE_DESC);
    }

    @Test
    @Transactional
    public void createLanguagesWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = languagesRepository.findAll().size();

        // Create the Languages with an existing ID
        languages.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restLanguagesMockMvc.perform(post("/api/languages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(languages)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Languages> languagesList = languagesRepository.findAll();
        assertThat(languagesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllLanguages() throws Exception {
        // Initialize the database
        languagesRepository.saveAndFlush(languages);

        // Get all the languagesList
        restLanguagesMockMvc.perform(get("/api/languages?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(languages.getId().intValue())))
            .andExpect(jsonPath("$.[*].languageDesc").value(hasItem(DEFAULT_LANGUAGE_DESC.toString())));
    }

    @Test
    @Transactional
    public void getLanguages() throws Exception {
        // Initialize the database
        languagesRepository.saveAndFlush(languages);

        // Get the languages
        restLanguagesMockMvc.perform(get("/api/languages/{id}", languages.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(languages.getId().intValue()))
            .andExpect(jsonPath("$.languageDesc").value(DEFAULT_LANGUAGE_DESC.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingLanguages() throws Exception {
        // Get the languages
        restLanguagesMockMvc.perform(get("/api/languages/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLanguages() throws Exception {
        // Initialize the database
        languagesRepository.saveAndFlush(languages);
        int databaseSizeBeforeUpdate = languagesRepository.findAll().size();

        // Update the languages
        Languages updatedLanguages = languagesRepository.findOne(languages.getId());
        updatedLanguages
            .languageDesc(UPDATED_LANGUAGE_DESC);

        restLanguagesMockMvc.perform(put("/api/languages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedLanguages)))
            .andExpect(status().isOk());

        // Validate the Languages in the database
        List<Languages> languagesList = languagesRepository.findAll();
        assertThat(languagesList).hasSize(databaseSizeBeforeUpdate);
        Languages testLanguages = languagesList.get(languagesList.size() - 1);
        assertThat(testLanguages.getLanguageDesc()).isEqualTo(UPDATED_LANGUAGE_DESC);
    }

    @Test
    @Transactional
    public void updateNonExistingLanguages() throws Exception {
        int databaseSizeBeforeUpdate = languagesRepository.findAll().size();

        // Create the Languages

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restLanguagesMockMvc.perform(put("/api/languages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(languages)))
            .andExpect(status().isCreated());

        // Validate the Languages in the database
        List<Languages> languagesList = languagesRepository.findAll();
        assertThat(languagesList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteLanguages() throws Exception {
        // Initialize the database
        languagesRepository.saveAndFlush(languages);
        int databaseSizeBeforeDelete = languagesRepository.findAll().size();

        // Get the languages
        restLanguagesMockMvc.perform(delete("/api/languages/{id}", languages.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Languages> languagesList = languagesRepository.findAll();
        assertThat(languagesList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Languages.class);
        Languages languages1 = new Languages();
        languages1.setId(1L);
        Languages languages2 = new Languages();
        languages2.setId(languages1.getId());
        assertThat(languages1).isEqualTo(languages2);
        languages2.setId(2L);
        assertThat(languages1).isNotEqualTo(languages2);
        languages1.setId(null);
        assertThat(languages1).isNotEqualTo(languages2);
    }
}
