package uk.gov.ofwat.fountain.modelbuilder.web.rest;

import uk.gov.ofwat.fountain.modelbuilder.ModelBuilderApp;

import uk.gov.ofwat.fountain.modelbuilder.domain.PageDetails;
import uk.gov.ofwat.fountain.modelbuilder.repository.PageDetailsRepository;
import uk.gov.ofwat.fountain.modelbuilder.repository.search.PageDetailsSearchRepository;

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
 * Test class for the PageDetailsResource REST controller.
 *
 * @see PageDetailsResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ModelBuilderApp.class)
public class PageDetailsResourceIntTest {

    private static final String DEFAULT_CODE = "AAAAA";
    private static final String UPDATED_CODE = "BBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";

    private static final String DEFAULT_TEXT = "AAAAA";
    private static final String UPDATED_TEXT = "BBBBB";

    private static final String DEFAULT_COMPANY_TYPE = "AAAAA";
    private static final String UPDATED_COMPANY_TYPE = "BBBBB";

    private static final String DEFAULT_HEADING = "AAAAA";
    private static final String UPDATED_HEADING = "BBBBB";

    private static final Boolean DEFAULT_COMMERCIAL_IN_CONFIDENCE = false;
    private static final Boolean UPDATED_COMMERCIAL_IN_CONFIDENCE = true;

    private static final Boolean DEFAULT_HIDDEN = false;
    private static final Boolean UPDATED_HIDDEN = true;

    private static final String DEFAULT_TEXT_CODE = "AAAAA";
    private static final String UPDATED_TEXT_CODE = "BBBBB";

    @Inject
    private PageDetailsRepository pageDetailsRepository;

    @Inject
    private PageDetailsSearchRepository pageDetailsSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restPageDetailsMockMvc;

    private PageDetails pageDetails;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PageDetailsResource pageDetailsResource = new PageDetailsResource();
        ReflectionTestUtils.setField(pageDetailsResource, "pageDetailsSearchRepository", pageDetailsSearchRepository);
        ReflectionTestUtils.setField(pageDetailsResource, "pageDetailsRepository", pageDetailsRepository);
        this.restPageDetailsMockMvc = MockMvcBuilders.standaloneSetup(pageDetailsResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PageDetails createEntity(EntityManager em) {
        PageDetails pageDetails = new PageDetails()
                .code(DEFAULT_CODE)
                .description(DEFAULT_DESCRIPTION)
                .text(DEFAULT_TEXT)
                .companyType(DEFAULT_COMPANY_TYPE)
                .heading(DEFAULT_HEADING)
                .commercialInConfidence(DEFAULT_COMMERCIAL_IN_CONFIDENCE)
                .hidden(DEFAULT_HIDDEN)
                .textCode(DEFAULT_TEXT_CODE);
        return pageDetails;
    }

    @Before
    public void initTest() {
        pageDetailsSearchRepository.deleteAll();
        pageDetails = createEntity(em);
    }

    @Test
    @Transactional
    public void createPageDetails() throws Exception {
        int databaseSizeBeforeCreate = pageDetailsRepository.findAll().size();

        // Create the PageDetails

        restPageDetailsMockMvc.perform(post("/api/page-details")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(pageDetails)))
                .andExpect(status().isCreated());

        // Validate the PageDetails in the database
        List<PageDetails> pageDetails = pageDetailsRepository.findAll();
        assertThat(pageDetails).hasSize(databaseSizeBeforeCreate + 1);
        PageDetails testPageDetails = pageDetails.get(pageDetails.size() - 1);
        assertThat(testPageDetails.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testPageDetails.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testPageDetails.getText()).isEqualTo(DEFAULT_TEXT);
        assertThat(testPageDetails.getCompanyType()).isEqualTo(DEFAULT_COMPANY_TYPE);
        assertThat(testPageDetails.getHeading()).isEqualTo(DEFAULT_HEADING);
        assertThat(testPageDetails.isCommercialInConfidence()).isEqualTo(DEFAULT_COMMERCIAL_IN_CONFIDENCE);
        assertThat(testPageDetails.isHidden()).isEqualTo(DEFAULT_HIDDEN);
        assertThat(testPageDetails.getTextCode()).isEqualTo(DEFAULT_TEXT_CODE);

        // Validate the PageDetails in ElasticSearch
        PageDetails pageDetailsEs = pageDetailsSearchRepository.findOne(testPageDetails.getId());
        assertThat(pageDetailsEs).isEqualToComparingFieldByField(testPageDetails);
    }

    @Test
    @Transactional
    public void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = pageDetailsRepository.findAll().size();
        // set the field null
        pageDetails.setCode(null);

        // Create the PageDetails, which fails.

        restPageDetailsMockMvc.perform(post("/api/page-details")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(pageDetails)))
                .andExpect(status().isBadRequest());

        List<PageDetails> pageDetails = pageDetailsRepository.findAll();
        assertThat(pageDetails).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = pageDetailsRepository.findAll().size();
        // set the field null
        pageDetails.setDescription(null);

        // Create the PageDetails, which fails.

        restPageDetailsMockMvc.perform(post("/api/page-details")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(pageDetails)))
                .andExpect(status().isBadRequest());

        List<PageDetails> pageDetails = pageDetailsRepository.findAll();
        assertThat(pageDetails).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPageDetails() throws Exception {
        // Initialize the database
        pageDetailsRepository.saveAndFlush(pageDetails);

        // Get all the pageDetails
        restPageDetailsMockMvc.perform(get("/api/page-details?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(pageDetails.getId().intValue())))
                .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].text").value(hasItem(DEFAULT_TEXT.toString())))
                .andExpect(jsonPath("$.[*].companyType").value(hasItem(DEFAULT_COMPANY_TYPE.toString())))
                .andExpect(jsonPath("$.[*].heading").value(hasItem(DEFAULT_HEADING.toString())))
                .andExpect(jsonPath("$.[*].commercialInConfidence").value(hasItem(DEFAULT_COMMERCIAL_IN_CONFIDENCE.booleanValue())))
                .andExpect(jsonPath("$.[*].hidden").value(hasItem(DEFAULT_HIDDEN.booleanValue())))
                .andExpect(jsonPath("$.[*].textCode").value(hasItem(DEFAULT_TEXT_CODE.toString())));
    }

    @Test
    @Transactional
    public void getPageDetails() throws Exception {
        // Initialize the database
        pageDetailsRepository.saveAndFlush(pageDetails);

        // Get the pageDetails
        restPageDetailsMockMvc.perform(get("/api/page-details/{id}", pageDetails.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(pageDetails.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.text").value(DEFAULT_TEXT.toString()))
            .andExpect(jsonPath("$.companyType").value(DEFAULT_COMPANY_TYPE.toString()))
            .andExpect(jsonPath("$.heading").value(DEFAULT_HEADING.toString()))
            .andExpect(jsonPath("$.commercialInConfidence").value(DEFAULT_COMMERCIAL_IN_CONFIDENCE.booleanValue()))
            .andExpect(jsonPath("$.hidden").value(DEFAULT_HIDDEN.booleanValue()))
            .andExpect(jsonPath("$.textCode").value(DEFAULT_TEXT_CODE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPageDetails() throws Exception {
        // Get the pageDetails
        restPageDetailsMockMvc.perform(get("/api/page-details/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePageDetails() throws Exception {
        // Initialize the database
        pageDetailsRepository.saveAndFlush(pageDetails);
        pageDetailsSearchRepository.save(pageDetails);
        int databaseSizeBeforeUpdate = pageDetailsRepository.findAll().size();

        // Update the pageDetails
        PageDetails updatedPageDetails = pageDetailsRepository.findOne(pageDetails.getId());
        updatedPageDetails
                .code(UPDATED_CODE)
                .description(UPDATED_DESCRIPTION)
                .text(UPDATED_TEXT)
                .companyType(UPDATED_COMPANY_TYPE)
                .heading(UPDATED_HEADING)
                .commercialInConfidence(UPDATED_COMMERCIAL_IN_CONFIDENCE)
                .hidden(UPDATED_HIDDEN)
                .textCode(UPDATED_TEXT_CODE);

        restPageDetailsMockMvc.perform(put("/api/page-details")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedPageDetails)))
                .andExpect(status().isOk());

        // Validate the PageDetails in the database
        List<PageDetails> pageDetails = pageDetailsRepository.findAll();
        assertThat(pageDetails).hasSize(databaseSizeBeforeUpdate);
        PageDetails testPageDetails = pageDetails.get(pageDetails.size() - 1);
        assertThat(testPageDetails.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testPageDetails.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testPageDetails.getText()).isEqualTo(UPDATED_TEXT);
        assertThat(testPageDetails.getCompanyType()).isEqualTo(UPDATED_COMPANY_TYPE);
        assertThat(testPageDetails.getHeading()).isEqualTo(UPDATED_HEADING);
        assertThat(testPageDetails.isCommercialInConfidence()).isEqualTo(UPDATED_COMMERCIAL_IN_CONFIDENCE);
        assertThat(testPageDetails.isHidden()).isEqualTo(UPDATED_HIDDEN);
        assertThat(testPageDetails.getTextCode()).isEqualTo(UPDATED_TEXT_CODE);

        // Validate the PageDetails in ElasticSearch
        PageDetails pageDetailsEs = pageDetailsSearchRepository.findOne(testPageDetails.getId());
        assertThat(pageDetailsEs).isEqualToComparingFieldByField(testPageDetails);
    }

    @Test
    @Transactional
    public void deletePageDetails() throws Exception {
        // Initialize the database
        pageDetailsRepository.saveAndFlush(pageDetails);
        pageDetailsSearchRepository.save(pageDetails);
        int databaseSizeBeforeDelete = pageDetailsRepository.findAll().size();

        // Get the pageDetails
        restPageDetailsMockMvc.perform(delete("/api/page-details/{id}", pageDetails.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean pageDetailsExistsInEs = pageDetailsSearchRepository.exists(pageDetails.getId());
        assertThat(pageDetailsExistsInEs).isFalse();

        // Validate the database is empty
        List<PageDetails> pageDetails = pageDetailsRepository.findAll();
        assertThat(pageDetails).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchPageDetails() throws Exception {
        // Initialize the database
        pageDetailsRepository.saveAndFlush(pageDetails);
        pageDetailsSearchRepository.save(pageDetails);

        // Search the pageDetails
        restPageDetailsMockMvc.perform(get("/api/_search/page-details?query=id:" + pageDetails.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pageDetails.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].text").value(hasItem(DEFAULT_TEXT.toString())))
            .andExpect(jsonPath("$.[*].companyType").value(hasItem(DEFAULT_COMPANY_TYPE.toString())))
            .andExpect(jsonPath("$.[*].heading").value(hasItem(DEFAULT_HEADING.toString())))
            .andExpect(jsonPath("$.[*].commercialInConfidence").value(hasItem(DEFAULT_COMMERCIAL_IN_CONFIDENCE.booleanValue())))
            .andExpect(jsonPath("$.[*].hidden").value(hasItem(DEFAULT_HIDDEN.booleanValue())))
            .andExpect(jsonPath("$.[*].textCode").value(hasItem(DEFAULT_TEXT_CODE.toString())));
    }
}
