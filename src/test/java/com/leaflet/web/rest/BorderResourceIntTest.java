package com.leaflet.web.rest;

import com.leaflet.CivilDefanceApp;

import com.leaflet.domain.Border;
import com.leaflet.repository.BorderRepository;
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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the BorderResource REST controller.
 *
 * @see BorderResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CivilDefanceApp.class)
public class BorderResourceIntTest {

    private static final String DEFAULT_DESC = "AAAAAAAAAA";
    private static final String UPDATED_DESC = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_INSERTION_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_INSERTION_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_INSERTED_BY = "AAAAAAAAAA";
    private static final String UPDATED_INSERTED_BY = "BBBBBBBBBB";

    private static final Boolean DEFAULT_COLOR_RESET = false;
    private static final Boolean UPDATED_COLOR_RESET = true;

    @Autowired
    private BorderRepository borderRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restBorderMockMvc;

    private Border border;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        BorderResource borderResource = new BorderResource(borderRepository);
        this.restBorderMockMvc = MockMvcBuilders.standaloneSetup(borderResource)
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
    public static Border createEntity(EntityManager em) {
        Border border = new Border()
            .desc(DEFAULT_DESC)
            .insertionDate(DEFAULT_INSERTION_DATE)
            .insertedBy(DEFAULT_INSERTED_BY)
            .colorReset(DEFAULT_COLOR_RESET);
        return border;
    }

    @Before
    public void initTest() {
        border = createEntity(em);
    }

    @Test
    @Transactional
    public void createBorder() throws Exception {
        int databaseSizeBeforeCreate = borderRepository.findAll().size();

        // Create the Border
        restBorderMockMvc.perform(post("/api/borders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(border)))
            .andExpect(status().isCreated());

        // Validate the Border in the database
        List<Border> borderList = borderRepository.findAll();
        assertThat(borderList).hasSize(databaseSizeBeforeCreate + 1);
        Border testBorder = borderList.get(borderList.size() - 1);
        assertThat(testBorder.getDesc()).isEqualTo(DEFAULT_DESC);
        assertThat(testBorder.getInsertionDate()).isEqualTo(DEFAULT_INSERTION_DATE);
        assertThat(testBorder.getInsertedBy()).isEqualTo(DEFAULT_INSERTED_BY);
        assertThat(testBorder.isColorReset()).isEqualTo(DEFAULT_COLOR_RESET);
    }

    @Test
    @Transactional
    public void createBorderWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = borderRepository.findAll().size();

        // Create the Border with an existing ID
        border.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restBorderMockMvc.perform(post("/api/borders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(border)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Border> borderList = borderRepository.findAll();
        assertThat(borderList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllBorders() throws Exception {
        // Initialize the database
        borderRepository.saveAndFlush(border);

        // Get all the borderList
        restBorderMockMvc.perform(get("/api/borders?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(border.getId().intValue())))
            .andExpect(jsonPath("$.[*].desc").value(hasItem(DEFAULT_DESC.toString())))
            .andExpect(jsonPath("$.[*].insertionDate").value(hasItem(DEFAULT_INSERTION_DATE.toString())))
            .andExpect(jsonPath("$.[*].insertedBy").value(hasItem(DEFAULT_INSERTED_BY.toString())))
            .andExpect(jsonPath("$.[*].colorReset").value(hasItem(DEFAULT_COLOR_RESET.booleanValue())));
    }

    @Test
    @Transactional
    public void getBorder() throws Exception {
        // Initialize the database
        borderRepository.saveAndFlush(border);

        // Get the border
        restBorderMockMvc.perform(get("/api/borders/{id}", border.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(border.getId().intValue()))
            .andExpect(jsonPath("$.desc").value(DEFAULT_DESC.toString()))
            .andExpect(jsonPath("$.insertionDate").value(DEFAULT_INSERTION_DATE.toString()))
            .andExpect(jsonPath("$.insertedBy").value(DEFAULT_INSERTED_BY.toString()))
            .andExpect(jsonPath("$.colorReset").value(DEFAULT_COLOR_RESET.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingBorder() throws Exception {
        // Get the border
        restBorderMockMvc.perform(get("/api/borders/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBorder() throws Exception {
        // Initialize the database
        borderRepository.saveAndFlush(border);
        int databaseSizeBeforeUpdate = borderRepository.findAll().size();

        // Update the border
        Border updatedBorder = borderRepository.findOne(border.getId());
        updatedBorder
            .desc(UPDATED_DESC)
            .insertionDate(UPDATED_INSERTION_DATE)
            .insertedBy(UPDATED_INSERTED_BY)
            .colorReset(UPDATED_COLOR_RESET);

        restBorderMockMvc.perform(put("/api/borders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedBorder)))
            .andExpect(status().isOk());

        // Validate the Border in the database
        List<Border> borderList = borderRepository.findAll();
        assertThat(borderList).hasSize(databaseSizeBeforeUpdate);
        Border testBorder = borderList.get(borderList.size() - 1);
        assertThat(testBorder.getDesc()).isEqualTo(UPDATED_DESC);
        assertThat(testBorder.getInsertionDate()).isEqualTo(UPDATED_INSERTION_DATE);
        assertThat(testBorder.getInsertedBy()).isEqualTo(UPDATED_INSERTED_BY);
        assertThat(testBorder.isColorReset()).isEqualTo(UPDATED_COLOR_RESET);
    }

    @Test
    @Transactional
    public void updateNonExistingBorder() throws Exception {
        int databaseSizeBeforeUpdate = borderRepository.findAll().size();

        // Create the Border

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restBorderMockMvc.perform(put("/api/borders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(border)))
            .andExpect(status().isCreated());

        // Validate the Border in the database
        List<Border> borderList = borderRepository.findAll();
        assertThat(borderList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteBorder() throws Exception {
        // Initialize the database
        borderRepository.saveAndFlush(border);
        int databaseSizeBeforeDelete = borderRepository.findAll().size();

        // Get the border
        restBorderMockMvc.perform(delete("/api/borders/{id}", border.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Border> borderList = borderRepository.findAll();
        assertThat(borderList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Border.class);
        Border border1 = new Border();
        border1.setId(1L);
        Border border2 = new Border();
        border2.setId(border1.getId());
        assertThat(border1).isEqualTo(border2);
        border2.setId(2L);
        assertThat(border1).isNotEqualTo(border2);
        border1.setId(null);
        assertThat(border1).isNotEqualTo(border2);
    }
}
