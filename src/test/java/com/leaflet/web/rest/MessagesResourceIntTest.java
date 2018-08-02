package com.leaflet.web.rest;

import com.leaflet.CivilDefanceApp;

import com.leaflet.domain.Messages;
import com.leaflet.repository.MessagesRepository;
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
 * Test class for the MessagesResource REST controller.
 *
 * @see MessagesResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CivilDefanceApp.class)
public class MessagesResourceIntTest {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_MESSAGE_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_MESSAGE_CONTENT = "BBBBBBBBBB";

    @Autowired
    private MessagesRepository messagesRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restMessagesMockMvc;

    private Messages messages;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        MessagesResource messagesResource = new MessagesResource(messagesRepository);
        this.restMessagesMockMvc = MockMvcBuilders.standaloneSetup(messagesResource)
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
    public static Messages createEntity(EntityManager em) {
        Messages messages = new Messages()
            .title(DEFAULT_TITLE)
            .messageContent(DEFAULT_MESSAGE_CONTENT);
        return messages;
    }

    @Before
    public void initTest() {
        messages = createEntity(em);
    }

    @Test
    @Transactional
    public void createMessages() throws Exception {
        int databaseSizeBeforeCreate = messagesRepository.findAll().size();

        // Create the Messages
        restMessagesMockMvc.perform(post("/api/messages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(messages)))
            .andExpect(status().isCreated());

        // Validate the Messages in the database
        List<Messages> messagesList = messagesRepository.findAll();
        assertThat(messagesList).hasSize(databaseSizeBeforeCreate + 1);
        Messages testMessages = messagesList.get(messagesList.size() - 1);
        assertThat(testMessages.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testMessages.getMessageContent()).isEqualTo(DEFAULT_MESSAGE_CONTENT);
    }

    @Test
    @Transactional
    public void createMessagesWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = messagesRepository.findAll().size();

        // Create the Messages with an existing ID
        messages.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMessagesMockMvc.perform(post("/api/messages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(messages)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Messages> messagesList = messagesRepository.findAll();
        assertThat(messagesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllMessages() throws Exception {
        // Initialize the database
        messagesRepository.saveAndFlush(messages);

        // Get all the messagesList
        restMessagesMockMvc.perform(get("/api/messages?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(messages.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
            .andExpect(jsonPath("$.[*].messageContent").value(hasItem(DEFAULT_MESSAGE_CONTENT.toString())));
    }

    @Test
    @Transactional
    public void getMessages() throws Exception {
        // Initialize the database
        messagesRepository.saveAndFlush(messages);

        // Get the messages
        restMessagesMockMvc.perform(get("/api/messages/{id}", messages.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(messages.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
            .andExpect(jsonPath("$.messageContent").value(DEFAULT_MESSAGE_CONTENT.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingMessages() throws Exception {
        // Get the messages
        restMessagesMockMvc.perform(get("/api/messages/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMessages() throws Exception {
        // Initialize the database
        messagesRepository.saveAndFlush(messages);
        int databaseSizeBeforeUpdate = messagesRepository.findAll().size();

        // Update the messages
        Messages updatedMessages = messagesRepository.findOne(messages.getId());
        updatedMessages
            .title(UPDATED_TITLE)
            .messageContent(UPDATED_MESSAGE_CONTENT);

        restMessagesMockMvc.perform(put("/api/messages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedMessages)))
            .andExpect(status().isOk());

        // Validate the Messages in the database
        List<Messages> messagesList = messagesRepository.findAll();
        assertThat(messagesList).hasSize(databaseSizeBeforeUpdate);
        Messages testMessages = messagesList.get(messagesList.size() - 1);
        assertThat(testMessages.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testMessages.getMessageContent()).isEqualTo(UPDATED_MESSAGE_CONTENT);
    }

    @Test
    @Transactional
    public void updateNonExistingMessages() throws Exception {
        int databaseSizeBeforeUpdate = messagesRepository.findAll().size();

        // Create the Messages

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restMessagesMockMvc.perform(put("/api/messages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(messages)))
            .andExpect(status().isCreated());

        // Validate the Messages in the database
        List<Messages> messagesList = messagesRepository.findAll();
        assertThat(messagesList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteMessages() throws Exception {
        // Initialize the database
        messagesRepository.saveAndFlush(messages);
        int databaseSizeBeforeDelete = messagesRepository.findAll().size();

        // Get the messages
        restMessagesMockMvc.perform(delete("/api/messages/{id}", messages.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Messages> messagesList = messagesRepository.findAll();
        assertThat(messagesList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Messages.class);
        Messages messages1 = new Messages();
        messages1.setId(1L);
        Messages messages2 = new Messages();
        messages2.setId(messages1.getId());
        assertThat(messages1).isEqualTo(messages2);
        messages2.setId(2L);
        assertThat(messages1).isNotEqualTo(messages2);
        messages1.setId(null);
        assertThat(messages1).isNotEqualTo(messages2);
    }
}
