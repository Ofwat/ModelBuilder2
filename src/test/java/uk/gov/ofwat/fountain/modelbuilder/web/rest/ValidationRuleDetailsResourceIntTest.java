package uk.gov.ofwat.fountain.modelbuilder.web.rest;

import uk.gov.ofwat.fountain.modelbuilder.ModelBuilderApp;

import uk.gov.ofwat.fountain.modelbuilder.domain.ValidationRuleDetails;
import uk.gov.ofwat.fountain.modelbuilder.repository.ValidationRuleDetailsRepository;
import uk.gov.ofwat.fountain.modelbuilder.repository.search.ValidationRuleDetailsSearchRepository;

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
 * Test class for the ValidationRuleDetailsResource REST controller.
 *
 * @see ValidationRuleDetailsResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ModelBuilderApp.class)
public class ValidationRuleDetailsResourceIntTest {

    private static final String DEFAULT_CODE = "AAAAA";
    private static final String UPDATED_CODE = "BBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";

    private static final String DEFAULT_FORMULA = "AAAAA";
    private static final String UPDATED_FORMULA = "BBBBB";

    @Inject
    private ValidationRuleDetailsRepository validationRuleDetailsRepository;

    @Inject
    private ValidationRuleDetailsSearchRepository validationRuleDetailsSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restValidationRuleDetailsMockMvc;

    private ValidationRuleDetails validationRuleDetails;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ValidationRuleDetailsResource validationRuleDetailsResource = new ValidationRuleDetailsResource();
        ReflectionTestUtils.setField(validationRuleDetailsResource, "validationRuleDetailsSearchRepository", validationRuleDetailsSearchRepository);
        ReflectionTestUtils.setField(validationRuleDetailsResource, "validationRuleDetailsRepository", validationRuleDetailsRepository);
        this.restValidationRuleDetailsMockMvc = MockMvcBuilders.standaloneSetup(validationRuleDetailsResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ValidationRuleDetails createEntity(EntityManager em) {
        ValidationRuleDetails validationRuleDetails = new ValidationRuleDetails()
                .code(DEFAULT_CODE)
                .description(DEFAULT_DESCRIPTION)
                .formula(DEFAULT_FORMULA);
        return validationRuleDetails;
    }

    @Before
    public void initTest() {
        validationRuleDetailsSearchRepository.deleteAll();
        validationRuleDetails = createEntity(em);
    }

    @Test
    @Transactional
    public void createValidationRuleDetails() throws Exception {
        int databaseSizeBeforeCreate = validationRuleDetailsRepository.findAll().size();

        // Create the ValidationRuleDetails

        restValidationRuleDetailsMockMvc.perform(post("/api/validation-rule-details")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(validationRuleDetails)))
                .andExpect(status().isCreated());

        // Validate the ValidationRuleDetails in the database
        List<ValidationRuleDetails> validationRuleDetails = validationRuleDetailsRepository.findAll();
        assertThat(validationRuleDetails).hasSize(databaseSizeBeforeCreate + 1);
        ValidationRuleDetails testValidationRuleDetails = validationRuleDetails.get(validationRuleDetails.size() - 1);
        assertThat(testValidationRuleDetails.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testValidationRuleDetails.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testValidationRuleDetails.getFormula()).isEqualTo(DEFAULT_FORMULA);

        // Validate the ValidationRuleDetails in ElasticSearch
        ValidationRuleDetails validationRuleDetailsEs = validationRuleDetailsSearchRepository.findOne(testValidationRuleDetails.getId());
        assertThat(validationRuleDetailsEs).isEqualToComparingFieldByField(testValidationRuleDetails);
    }

    @Test
    @Transactional
    public void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = validationRuleDetailsRepository.findAll().size();
        // set the field null
        validationRuleDetails.setCode(null);

        // Create the ValidationRuleDetails, which fails.

        restValidationRuleDetailsMockMvc.perform(post("/api/validation-rule-details")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(validationRuleDetails)))
                .andExpect(status().isBadRequest());

        List<ValidationRuleDetails> validationRuleDetails = validationRuleDetailsRepository.findAll();
        assertThat(validationRuleDetails).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = validationRuleDetailsRepository.findAll().size();
        // set the field null
        validationRuleDetails.setDescription(null);

        // Create the ValidationRuleDetails, which fails.

        restValidationRuleDetailsMockMvc.perform(post("/api/validation-rule-details")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(validationRuleDetails)))
                .andExpect(status().isBadRequest());

        List<ValidationRuleDetails> validationRuleDetails = validationRuleDetailsRepository.findAll();
        assertThat(validationRuleDetails).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkFormulaIsRequired() throws Exception {
        int databaseSizeBeforeTest = validationRuleDetailsRepository.findAll().size();
        // set the field null
        validationRuleDetails.setFormula(null);

        // Create the ValidationRuleDetails, which fails.

        restValidationRuleDetailsMockMvc.perform(post("/api/validation-rule-details")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(validationRuleDetails)))
                .andExpect(status().isBadRequest());

        List<ValidationRuleDetails> validationRuleDetails = validationRuleDetailsRepository.findAll();
        assertThat(validationRuleDetails).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllValidationRuleDetails() throws Exception {
        // Initialize the database
        validationRuleDetailsRepository.saveAndFlush(validationRuleDetails);

        // Get all the validationRuleDetails
        restValidationRuleDetailsMockMvc.perform(get("/api/validation-rule-details?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(validationRuleDetails.getId().intValue())))
                .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].formula").value(hasItem(DEFAULT_FORMULA.toString())));
    }

    @Test
    @Transactional
    public void getValidationRuleDetails() throws Exception {
        // Initialize the database
        validationRuleDetailsRepository.saveAndFlush(validationRuleDetails);

        // Get the validationRuleDetails
        restValidationRuleDetailsMockMvc.perform(get("/api/validation-rule-details/{id}", validationRuleDetails.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(validationRuleDetails.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.formula").value(DEFAULT_FORMULA.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingValidationRuleDetails() throws Exception {
        // Get the validationRuleDetails
        restValidationRuleDetailsMockMvc.perform(get("/api/validation-rule-details/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateValidationRuleDetails() throws Exception {
        // Initialize the database
        validationRuleDetailsRepository.saveAndFlush(validationRuleDetails);
        validationRuleDetailsSearchRepository.save(validationRuleDetails);
        int databaseSizeBeforeUpdate = validationRuleDetailsRepository.findAll().size();

        // Update the validationRuleDetails
        ValidationRuleDetails updatedValidationRuleDetails = validationRuleDetailsRepository.findOne(validationRuleDetails.getId());
        updatedValidationRuleDetails
                .code(UPDATED_CODE)
                .description(UPDATED_DESCRIPTION)
                .formula(UPDATED_FORMULA);

        restValidationRuleDetailsMockMvc.perform(put("/api/validation-rule-details")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedValidationRuleDetails)))
                .andExpect(status().isOk());

        // Validate the ValidationRuleDetails in the database
        List<ValidationRuleDetails> validationRuleDetails = validationRuleDetailsRepository.findAll();
        assertThat(validationRuleDetails).hasSize(databaseSizeBeforeUpdate);
        ValidationRuleDetails testValidationRuleDetails = validationRuleDetails.get(validationRuleDetails.size() - 1);
        assertThat(testValidationRuleDetails.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testValidationRuleDetails.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testValidationRuleDetails.getFormula()).isEqualTo(UPDATED_FORMULA);

        // Validate the ValidationRuleDetails in ElasticSearch
        ValidationRuleDetails validationRuleDetailsEs = validationRuleDetailsSearchRepository.findOne(testValidationRuleDetails.getId());
        assertThat(validationRuleDetailsEs).isEqualToComparingFieldByField(testValidationRuleDetails);
    }

    @Test
    @Transactional
    public void deleteValidationRuleDetails() throws Exception {
        // Initialize the database
        validationRuleDetailsRepository.saveAndFlush(validationRuleDetails);
        validationRuleDetailsSearchRepository.save(validationRuleDetails);
        int databaseSizeBeforeDelete = validationRuleDetailsRepository.findAll().size();

        // Get the validationRuleDetails
        restValidationRuleDetailsMockMvc.perform(delete("/api/validation-rule-details/{id}", validationRuleDetails.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean validationRuleDetailsExistsInEs = validationRuleDetailsSearchRepository.exists(validationRuleDetails.getId());
        assertThat(validationRuleDetailsExistsInEs).isFalse();

        // Validate the database is empty
        List<ValidationRuleDetails> validationRuleDetails = validationRuleDetailsRepository.findAll();
        assertThat(validationRuleDetails).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchValidationRuleDetails() throws Exception {
        // Initialize the database
        validationRuleDetailsRepository.saveAndFlush(validationRuleDetails);
        validationRuleDetailsSearchRepository.save(validationRuleDetails);

        // Search the validationRuleDetails
        restValidationRuleDetailsMockMvc.perform(get("/api/_search/validation-rule-details?query=id:" + validationRuleDetails.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(validationRuleDetails.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].formula").value(hasItem(DEFAULT_FORMULA.toString())));
    }
}
