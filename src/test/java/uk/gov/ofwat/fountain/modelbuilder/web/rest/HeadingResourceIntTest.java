package uk.gov.ofwat.fountain.modelbuilder.web.rest;

import uk.gov.ofwat.fountain.modelbuilder.ModelBuilderApp;

import uk.gov.ofwat.fountain.modelbuilder.domain.Heading;
import uk.gov.ofwat.fountain.modelbuilder.repository.HeadingRepository;
import uk.gov.ofwat.fountain.modelbuilder.repository.search.HeadingSearchRepository;

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
 * Test class for the HeadingResource REST controller.
 *
 * @see HeadingResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ModelBuilderApp.class)
public class HeadingResourceIntTest {

    private static final String DEFAULT_CODE = "AAAAA";
    private static final String UPDATED_CODE = "BBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";

    private static final String DEFAULT_NOTES = "AAAAA";
    private static final String UPDATED_NOTES = "BBBBB";

    private static final String DEFAULT_PARENT = "AAAAA";
    private static final String UPDATED_PARENT = "BBBBB";

    private static final String DEFAULT_ANNOTATION = "AAAAA";
    private static final String UPDATED_ANNOTATION = "BBBBB";

    @Inject
    private HeadingRepository headingRepository;

    @Inject
    private HeadingSearchRepository headingSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restHeadingMockMvc;

    private Heading heading;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        HeadingResource headingResource = new HeadingResource();
        ReflectionTestUtils.setField(headingResource, "headingSearchRepository", headingSearchRepository);
        ReflectionTestUtils.setField(headingResource, "headingRepository", headingRepository);
        this.restHeadingMockMvc = MockMvcBuilders.standaloneSetup(headingResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Heading createEntity(EntityManager em) {
        Heading heading = new Heading()
                .code(DEFAULT_CODE)
                .description(DEFAULT_DESCRIPTION)
                .notes(DEFAULT_NOTES)
                .parent(DEFAULT_PARENT)
                .annotation(DEFAULT_ANNOTATION);
        return heading;
    }

    @Before
    public void initTest() {
        headingSearchRepository.deleteAll();
        heading = createEntity(em);
    }

    @Test
    @Transactional
    public void createHeading() throws Exception {
        int databaseSizeBeforeCreate = headingRepository.findAll().size();

        // Create the Heading

        restHeadingMockMvc.perform(post("/api/headings")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(heading)))
                .andExpect(status().isCreated());

        // Validate the Heading in the database
        List<Heading> headings = headingRepository.findAll();
        assertThat(headings).hasSize(databaseSizeBeforeCreate + 1);
        Heading testHeading = headings.get(headings.size() - 1);
        assertThat(testHeading.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testHeading.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testHeading.getNotes()).isEqualTo(DEFAULT_NOTES);
        assertThat(testHeading.getParent()).isEqualTo(DEFAULT_PARENT);
        assertThat(testHeading.getAnnotation()).isEqualTo(DEFAULT_ANNOTATION);

        // Validate the Heading in ElasticSearch
        Heading headingEs = headingSearchRepository.findOne(testHeading.getId());
        assertThat(headingEs).isEqualToComparingFieldByField(testHeading);
    }

    @Test
    @Transactional
    public void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = headingRepository.findAll().size();
        // set the field null
        heading.setCode(null);

        // Create the Heading, which fails.

        restHeadingMockMvc.perform(post("/api/headings")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(heading)))
                .andExpect(status().isBadRequest());

        List<Heading> headings = headingRepository.findAll();
        assertThat(headings).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllHeadings() throws Exception {
        // Initialize the database
        headingRepository.saveAndFlush(heading);

        // Get all the headings
        restHeadingMockMvc.perform(get("/api/headings?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(heading.getId().intValue())))
                .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES.toString())))
                .andExpect(jsonPath("$.[*].parent").value(hasItem(DEFAULT_PARENT.toString())))
                .andExpect(jsonPath("$.[*].annotation").value(hasItem(DEFAULT_ANNOTATION.toString())));
    }

    @Test
    @Transactional
    public void getHeading() throws Exception {
        // Initialize the database
        headingRepository.saveAndFlush(heading);

        // Get the heading
        restHeadingMockMvc.perform(get("/api/headings/{id}", heading.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(heading.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.notes").value(DEFAULT_NOTES.toString()))
            .andExpect(jsonPath("$.parent").value(DEFAULT_PARENT.toString()))
            .andExpect(jsonPath("$.annotation").value(DEFAULT_ANNOTATION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingHeading() throws Exception {
        // Get the heading
        restHeadingMockMvc.perform(get("/api/headings/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateHeading() throws Exception {
        // Initialize the database
        headingRepository.saveAndFlush(heading);
        headingSearchRepository.save(heading);
        int databaseSizeBeforeUpdate = headingRepository.findAll().size();

        // Update the heading
        Heading updatedHeading = headingRepository.findOne(heading.getId());
        updatedHeading
                .code(UPDATED_CODE)
                .description(UPDATED_DESCRIPTION)
                .notes(UPDATED_NOTES)
                .parent(UPDATED_PARENT)
                .annotation(UPDATED_ANNOTATION);

        restHeadingMockMvc.perform(put("/api/headings")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedHeading)))
                .andExpect(status().isOk());

        // Validate the Heading in the database
        List<Heading> headings = headingRepository.findAll();
        assertThat(headings).hasSize(databaseSizeBeforeUpdate);
        Heading testHeading = headings.get(headings.size() - 1);
        assertThat(testHeading.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testHeading.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testHeading.getNotes()).isEqualTo(UPDATED_NOTES);
        assertThat(testHeading.getParent()).isEqualTo(UPDATED_PARENT);
        assertThat(testHeading.getAnnotation()).isEqualTo(UPDATED_ANNOTATION);

        // Validate the Heading in ElasticSearch
        Heading headingEs = headingSearchRepository.findOne(testHeading.getId());
        assertThat(headingEs).isEqualToComparingFieldByField(testHeading);
    }

    @Test
    @Transactional
    public void deleteHeading() throws Exception {
        // Initialize the database
        headingRepository.saveAndFlush(heading);
        headingSearchRepository.save(heading);
        int databaseSizeBeforeDelete = headingRepository.findAll().size();

        // Get the heading
        restHeadingMockMvc.perform(delete("/api/headings/{id}", heading.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean headingExistsInEs = headingSearchRepository.exists(heading.getId());
        assertThat(headingExistsInEs).isFalse();

        // Validate the database is empty
        List<Heading> headings = headingRepository.findAll();
        assertThat(headings).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchHeading() throws Exception {
        // Initialize the database
        headingRepository.saveAndFlush(heading);
        headingSearchRepository.save(heading);

        // Search the heading
        restHeadingMockMvc.perform(get("/api/_search/headings?query=id:" + heading.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(heading.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES.toString())))
            .andExpect(jsonPath("$.[*].parent").value(hasItem(DEFAULT_PARENT.toString())))
            .andExpect(jsonPath("$.[*].annotation").value(hasItem(DEFAULT_ANNOTATION.toString())));
    }
}
