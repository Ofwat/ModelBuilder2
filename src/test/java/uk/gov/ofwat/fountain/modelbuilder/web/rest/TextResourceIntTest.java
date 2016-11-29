package uk.gov.ofwat.fountain.modelbuilder.web.rest;

import uk.gov.ofwat.fountain.modelbuilder.ModelBuilderApp;

import uk.gov.ofwat.fountain.modelbuilder.domain.Text;
import uk.gov.ofwat.fountain.modelbuilder.repository.TextRepository;
import uk.gov.ofwat.fountain.modelbuilder.repository.search.TextSearchRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the TextResource REST controller.
 *
 * @see TextResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ModelBuilderApp.class)
public class TextResourceIntTest {

    private static final String DEFAULT_CODE = "AAAAA";
    private static final String UPDATED_CODE = "BBBBB";

    @Inject
    private TextRepository textRepository;

    @Inject
    private TextSearchRepository textSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restTextMockMvc;

    private Text text;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TextResource textResource = new TextResource();
        ReflectionTestUtils.setField(textResource, "textSearchRepository", textSearchRepository);
        ReflectionTestUtils.setField(textResource, "textRepository", textRepository);
        this.restTextMockMvc = MockMvcBuilders.standaloneSetup(textResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Text createEntity(EntityManager em) {
        Text text = new Text()
                .code(DEFAULT_CODE);
        return text;
    }

    @Before
    public void initTest() {
        textSearchRepository.deleteAll();
        text = createEntity(em);
    }

    @Test
    @Transactional
    public void createText() throws Exception {
        int databaseSizeBeforeCreate = textRepository.findAll().size();

        // Create the Text

        restTextMockMvc.perform(post("/api/texts")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(text)))
                .andExpect(status().isCreated());

        // Validate the Text in the database
        List<Text> texts = textRepository.findAll();
        assertThat(texts).hasSize(databaseSizeBeforeCreate + 1);
        Text testText = texts.get(texts.size() - 1);
        assertThat(testText.getCode()).isEqualTo(DEFAULT_CODE);

        // Validate the Text in ElasticSearch
        Text textEs = textSearchRepository.findOne(testText.getId());
        assertThat(textEs).isEqualToComparingFieldByField(testText);
    }

    @Test
    @Transactional
    public void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = textRepository.findAll().size();
        // set the field null
        text.setCode(null);

        // Create the Text, which fails.

        restTextMockMvc.perform(post("/api/texts")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(text)))
                .andExpect(status().isBadRequest());

        List<Text> texts = textRepository.findAll();
        assertThat(texts).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTexts() throws Exception {
        // Initialize the database
        textRepository.saveAndFlush(text);

        // Get all the texts
        restTextMockMvc.perform(get("/api/texts?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(text.getId().intValue())))
                .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())));
    }

    @Test
    @Transactional
    public void getText() throws Exception {
        // Initialize the database
        textRepository.saveAndFlush(text);

        // Get the text
        restTextMockMvc.perform(get("/api/texts/{id}", text.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(text.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingText() throws Exception {
        // Get the text
        restTextMockMvc.perform(get("/api/texts/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateText() throws Exception {
        // Initialize the database
        textRepository.saveAndFlush(text);
        textSearchRepository.save(text);
        int databaseSizeBeforeUpdate = textRepository.findAll().size();

        // Update the text
        Text updatedText = textRepository.findOne(text.getId());
        updatedText
                .code(UPDATED_CODE);

        restTextMockMvc.perform(put("/api/texts")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedText)))
                .andExpect(status().isOk());

        // Validate the Text in the database
        List<Text> texts = textRepository.findAll();
        assertThat(texts).hasSize(databaseSizeBeforeUpdate);
        Text testText = texts.get(texts.size() - 1);
        assertThat(testText.getCode()).isEqualTo(UPDATED_CODE);

        // Validate the Text in ElasticSearch
        Text textEs = textSearchRepository.findOne(testText.getId());
        assertThat(textEs).isEqualToComparingFieldByField(testText);
    }

    @Test
    @Transactional
    public void deleteText() throws Exception {
        // Initialize the database
        textRepository.saveAndFlush(text);
        textSearchRepository.save(text);
        int databaseSizeBeforeDelete = textRepository.findAll().size();

        // Get the text
        restTextMockMvc.perform(delete("/api/texts/{id}", text.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean textExistsInEs = textSearchRepository.exists(text.getId());
        assertThat(textExistsInEs).isFalse();

        // Validate the database is empty
        List<Text> texts = textRepository.findAll();
        assertThat(texts).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchText() throws Exception {
        // Initialize the database
        textRepository.saveAndFlush(text);
        textSearchRepository.save(text);

        // Search the text
        restTextMockMvc.perform(get("/api/_search/texts?query=id:" + text.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(text.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())));
    }
}
