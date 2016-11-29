package uk.gov.ofwat.fountain.modelbuilder.web.rest;

import uk.gov.ofwat.fountain.modelbuilder.ModelBuilderApp;

import uk.gov.ofwat.fountain.modelbuilder.domain.LineDetails;
import uk.gov.ofwat.fountain.modelbuilder.repository.LineDetailsRepository;
import uk.gov.ofwat.fountain.modelbuilder.repository.search.LineDetailsSearchRepository;

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
 * Test class for the LineDetailsResource REST controller.
 *
 * @see LineDetailsResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ModelBuilderApp.class)
public class LineDetailsResourceIntTest {

    private static final Boolean DEFAULT_HEADING = false;
    private static final Boolean UPDATED_HEADING = true;

    private static final String DEFAULT_CODE = "AAAAA";
    private static final String UPDATED_CODE = "BBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";

    private static final String DEFAULT_EQUATION = "AAAAA";
    private static final String UPDATED_EQUATION = "BBBBB";

    private static final String DEFAULT_LINE_NUMBER = "AAAAA";
    private static final String UPDATED_LINE_NUMBER = "BBBBB";

    private static final String DEFAULT_RULE_TEXT = "AAAAA";
    private static final String UPDATED_RULE_TEXT = "BBBBB";

    private static final String DEFAULT_TYPE = "AAAAA";
    private static final String UPDATED_TYPE = "BBBBB";

    private static final String DEFAULT_COMPANY_TYPE = "AAAAA";
    private static final String UPDATED_COMPANY_TYPE = "BBBBB";

    private static final Boolean DEFAULT_USE_CONFIDENCE_GRADE = false;
    private static final Boolean UPDATED_USE_CONFIDENCE_GRADE = true;

    private static final String DEFAULT_VALIDATION_RULE_CODE = "AAAAA";
    private static final String UPDATED_VALIDATION_RULE_CODE = "BBBBB";

    private static final String DEFAULT_TEXT_CODE = "AAAAA";
    private static final String UPDATED_TEXT_CODE = "BBBBB";

    private static final String DEFAULT_UNIT = "AAAAA";
    private static final String UPDATED_UNIT = "BBBBB";

    private static final Integer DEFAULT_DECIMAL_PLACES = 1;
    private static final Integer UPDATED_DECIMAL_PLACES = 2;

    @Inject
    private LineDetailsRepository lineDetailsRepository;

    @Inject
    private LineDetailsSearchRepository lineDetailsSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restLineDetailsMockMvc;

    private LineDetails lineDetails;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        LineDetailsResource lineDetailsResource = new LineDetailsResource();
        ReflectionTestUtils.setField(lineDetailsResource, "lineDetailsSearchRepository", lineDetailsSearchRepository);
        ReflectionTestUtils.setField(lineDetailsResource, "lineDetailsRepository", lineDetailsRepository);
        this.restLineDetailsMockMvc = MockMvcBuilders.standaloneSetup(lineDetailsResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LineDetails createEntity(EntityManager em) {
        LineDetails lineDetails = new LineDetails()
                .heading(DEFAULT_HEADING)
                .code(DEFAULT_CODE)
                .description(DEFAULT_DESCRIPTION)
                .equation(DEFAULT_EQUATION)
                .lineNumber(DEFAULT_LINE_NUMBER)
                .ruleText(DEFAULT_RULE_TEXT)
                .type(DEFAULT_TYPE)
                .companyType(DEFAULT_COMPANY_TYPE)
                .useConfidenceGrade(DEFAULT_USE_CONFIDENCE_GRADE)
                .validationRuleCode(DEFAULT_VALIDATION_RULE_CODE)
                .textCode(DEFAULT_TEXT_CODE)
                .unit(DEFAULT_UNIT)
                .decimalPlaces(DEFAULT_DECIMAL_PLACES);
        return lineDetails;
    }

    @Before
    public void initTest() {
        lineDetailsSearchRepository.deleteAll();
        lineDetails = createEntity(em);
    }

    @Test
    @Transactional
    public void createLineDetails() throws Exception {
        int databaseSizeBeforeCreate = lineDetailsRepository.findAll().size();

        // Create the LineDetails

        restLineDetailsMockMvc.perform(post("/api/line-details")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(lineDetails)))
                .andExpect(status().isCreated());

        // Validate the LineDetails in the database
        List<LineDetails> lineDetails = lineDetailsRepository.findAll();
        assertThat(lineDetails).hasSize(databaseSizeBeforeCreate + 1);
        LineDetails testLineDetails = lineDetails.get(lineDetails.size() - 1);
        assertThat(testLineDetails.isHeading()).isEqualTo(DEFAULT_HEADING);
        assertThat(testLineDetails.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testLineDetails.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testLineDetails.getEquation()).isEqualTo(DEFAULT_EQUATION);
        assertThat(testLineDetails.getLineNumber()).isEqualTo(DEFAULT_LINE_NUMBER);
        assertThat(testLineDetails.getRuleText()).isEqualTo(DEFAULT_RULE_TEXT);
        assertThat(testLineDetails.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testLineDetails.getCompanyType()).isEqualTo(DEFAULT_COMPANY_TYPE);
        assertThat(testLineDetails.isUseConfidenceGrade()).isEqualTo(DEFAULT_USE_CONFIDENCE_GRADE);
        assertThat(testLineDetails.getValidationRuleCode()).isEqualTo(DEFAULT_VALIDATION_RULE_CODE);
        assertThat(testLineDetails.getTextCode()).isEqualTo(DEFAULT_TEXT_CODE);
        assertThat(testLineDetails.getUnit()).isEqualTo(DEFAULT_UNIT);
        assertThat(testLineDetails.getDecimalPlaces()).isEqualTo(DEFAULT_DECIMAL_PLACES);

        // Validate the LineDetails in ElasticSearch
        LineDetails lineDetailsEs = lineDetailsSearchRepository.findOne(testLineDetails.getId());
        assertThat(lineDetailsEs).isEqualToComparingFieldByField(testLineDetails);
    }

    @Test
    @Transactional
    public void getAllLineDetails() throws Exception {
        // Initialize the database
        lineDetailsRepository.saveAndFlush(lineDetails);

        // Get all the lineDetails
        restLineDetailsMockMvc.perform(get("/api/line-details?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(lineDetails.getId().intValue())))
                .andExpect(jsonPath("$.[*].heading").value(hasItem(DEFAULT_HEADING.booleanValue())))
                .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].equation").value(hasItem(DEFAULT_EQUATION.toString())))
                .andExpect(jsonPath("$.[*].lineNumber").value(hasItem(DEFAULT_LINE_NUMBER.toString())))
                .andExpect(jsonPath("$.[*].ruleText").value(hasItem(DEFAULT_RULE_TEXT.toString())))
                .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
                .andExpect(jsonPath("$.[*].companyType").value(hasItem(DEFAULT_COMPANY_TYPE.toString())))
                .andExpect(jsonPath("$.[*].useConfidenceGrade").value(hasItem(DEFAULT_USE_CONFIDENCE_GRADE.booleanValue())))
                .andExpect(jsonPath("$.[*].validationRuleCode").value(hasItem(DEFAULT_VALIDATION_RULE_CODE.toString())))
                .andExpect(jsonPath("$.[*].textCode").value(hasItem(DEFAULT_TEXT_CODE.toString())))
                .andExpect(jsonPath("$.[*].unit").value(hasItem(DEFAULT_UNIT.toString())))
                .andExpect(jsonPath("$.[*].decimalPlaces").value(hasItem(DEFAULT_DECIMAL_PLACES)));
    }

    @Test
    @Transactional
    public void getLineDetails() throws Exception {
        // Initialize the database
        lineDetailsRepository.saveAndFlush(lineDetails);

        // Get the lineDetails
        restLineDetailsMockMvc.perform(get("/api/line-details/{id}", lineDetails.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(lineDetails.getId().intValue()))
            .andExpect(jsonPath("$.heading").value(DEFAULT_HEADING.booleanValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.equation").value(DEFAULT_EQUATION.toString()))
            .andExpect(jsonPath("$.lineNumber").value(DEFAULT_LINE_NUMBER.toString()))
            .andExpect(jsonPath("$.ruleText").value(DEFAULT_RULE_TEXT.toString()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.companyType").value(DEFAULT_COMPANY_TYPE.toString()))
            .andExpect(jsonPath("$.useConfidenceGrade").value(DEFAULT_USE_CONFIDENCE_GRADE.booleanValue()))
            .andExpect(jsonPath("$.validationRuleCode").value(DEFAULT_VALIDATION_RULE_CODE.toString()))
            .andExpect(jsonPath("$.textCode").value(DEFAULT_TEXT_CODE.toString()))
            .andExpect(jsonPath("$.unit").value(DEFAULT_UNIT.toString()))
            .andExpect(jsonPath("$.decimalPlaces").value(DEFAULT_DECIMAL_PLACES));
    }

    @Test
    @Transactional
    public void getNonExistingLineDetails() throws Exception {
        // Get the lineDetails
        restLineDetailsMockMvc.perform(get("/api/line-details/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLineDetails() throws Exception {
        // Initialize the database
        lineDetailsRepository.saveAndFlush(lineDetails);
        lineDetailsSearchRepository.save(lineDetails);
        int databaseSizeBeforeUpdate = lineDetailsRepository.findAll().size();

        // Update the lineDetails
        LineDetails updatedLineDetails = lineDetailsRepository.findOne(lineDetails.getId());
        updatedLineDetails
                .heading(UPDATED_HEADING)
                .code(UPDATED_CODE)
                .description(UPDATED_DESCRIPTION)
                .equation(UPDATED_EQUATION)
                .lineNumber(UPDATED_LINE_NUMBER)
                .ruleText(UPDATED_RULE_TEXT)
                .type(UPDATED_TYPE)
                .companyType(UPDATED_COMPANY_TYPE)
                .useConfidenceGrade(UPDATED_USE_CONFIDENCE_GRADE)
                .validationRuleCode(UPDATED_VALIDATION_RULE_CODE)
                .textCode(UPDATED_TEXT_CODE)
                .unit(UPDATED_UNIT)
                .decimalPlaces(UPDATED_DECIMAL_PLACES);

        restLineDetailsMockMvc.perform(put("/api/line-details")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedLineDetails)))
                .andExpect(status().isOk());

        // Validate the LineDetails in the database
        List<LineDetails> lineDetails = lineDetailsRepository.findAll();
        assertThat(lineDetails).hasSize(databaseSizeBeforeUpdate);
        LineDetails testLineDetails = lineDetails.get(lineDetails.size() - 1);
        assertThat(testLineDetails.isHeading()).isEqualTo(UPDATED_HEADING);
        assertThat(testLineDetails.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testLineDetails.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testLineDetails.getEquation()).isEqualTo(UPDATED_EQUATION);
        assertThat(testLineDetails.getLineNumber()).isEqualTo(UPDATED_LINE_NUMBER);
        assertThat(testLineDetails.getRuleText()).isEqualTo(UPDATED_RULE_TEXT);
        assertThat(testLineDetails.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testLineDetails.getCompanyType()).isEqualTo(UPDATED_COMPANY_TYPE);
        assertThat(testLineDetails.isUseConfidenceGrade()).isEqualTo(UPDATED_USE_CONFIDENCE_GRADE);
        assertThat(testLineDetails.getValidationRuleCode()).isEqualTo(UPDATED_VALIDATION_RULE_CODE);
        assertThat(testLineDetails.getTextCode()).isEqualTo(UPDATED_TEXT_CODE);
        assertThat(testLineDetails.getUnit()).isEqualTo(UPDATED_UNIT);
        assertThat(testLineDetails.getDecimalPlaces()).isEqualTo(UPDATED_DECIMAL_PLACES);

        // Validate the LineDetails in ElasticSearch
        LineDetails lineDetailsEs = lineDetailsSearchRepository.findOne(testLineDetails.getId());
        assertThat(lineDetailsEs).isEqualToComparingFieldByField(testLineDetails);
    }

    @Test
    @Transactional
    public void deleteLineDetails() throws Exception {
        // Initialize the database
        lineDetailsRepository.saveAndFlush(lineDetails);
        lineDetailsSearchRepository.save(lineDetails);
        int databaseSizeBeforeDelete = lineDetailsRepository.findAll().size();

        // Get the lineDetails
        restLineDetailsMockMvc.perform(delete("/api/line-details/{id}", lineDetails.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean lineDetailsExistsInEs = lineDetailsSearchRepository.exists(lineDetails.getId());
        assertThat(lineDetailsExistsInEs).isFalse();

        // Validate the database is empty
        List<LineDetails> lineDetails = lineDetailsRepository.findAll();
        assertThat(lineDetails).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchLineDetails() throws Exception {
        // Initialize the database
        lineDetailsRepository.saveAndFlush(lineDetails);
        lineDetailsSearchRepository.save(lineDetails);

        // Search the lineDetails
        restLineDetailsMockMvc.perform(get("/api/_search/line-details?query=id:" + lineDetails.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(lineDetails.getId().intValue())))
            .andExpect(jsonPath("$.[*].heading").value(hasItem(DEFAULT_HEADING.booleanValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].equation").value(hasItem(DEFAULT_EQUATION.toString())))
            .andExpect(jsonPath("$.[*].lineNumber").value(hasItem(DEFAULT_LINE_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].ruleText").value(hasItem(DEFAULT_RULE_TEXT.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].companyType").value(hasItem(DEFAULT_COMPANY_TYPE.toString())))
            .andExpect(jsonPath("$.[*].useConfidenceGrade").value(hasItem(DEFAULT_USE_CONFIDENCE_GRADE.booleanValue())))
            .andExpect(jsonPath("$.[*].validationRuleCode").value(hasItem(DEFAULT_VALIDATION_RULE_CODE.toString())))
            .andExpect(jsonPath("$.[*].textCode").value(hasItem(DEFAULT_TEXT_CODE.toString())))
            .andExpect(jsonPath("$.[*].unit").value(hasItem(DEFAULT_UNIT.toString())))
            .andExpect(jsonPath("$.[*].decimalPlaces").value(hasItem(DEFAULT_DECIMAL_PLACES)));
    }
}
