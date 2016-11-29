package uk.gov.ofwat.fountain.modelbuilder.web.rest;

import uk.gov.ofwat.fountain.modelbuilder.ModelBuilderApp;

import uk.gov.ofwat.fountain.modelbuilder.domain.ModelDetails;
import uk.gov.ofwat.fountain.modelbuilder.repository.ModelDetailsRepository;
import uk.gov.ofwat.fountain.modelbuilder.repository.search.ModelDetailsSearchRepository;

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
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ModelDetailsResource REST controller.
 *
 * @see ModelDetailsResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ModelBuilderApp.class)
public class ModelDetailsResourceIntTest {

    private static final String DEFAULT_CODE = "AAAAA";
    private static final String UPDATED_CODE = "BBBBB";

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";

    private static final String DEFAULT_VERSION = "AAAAA";
    private static final String UPDATED_VERSION = "BBBBB";

    private static final String DEFAULT_TYPE = "AAAAA";
    private static final String UPDATED_TYPE = "BBBBB";

    private static final String DEFAULT_TEXT_CODE = "AAAAA";
    private static final String UPDATED_TEXT_CODE = "BBBBB";

    private static final String DEFAULT_BASE_YEAR_CODE = "AAAAA";
    private static final String UPDATED_BASE_YEAR_CODE = "BBBBB";

    private static final String DEFAULT_REPORT_YEAR_CODE = "AAAAA";
    private static final String UPDATED_REPORT_YEAR_CODE = "BBBBB";

    private static final Boolean DEFAULT_ALLOW_DATA_CHANGES = false;
    private static final Boolean UPDATED_ALLOW_DATA_CHANGES = true;

    private static final String DEFAULT_MODEL_FAMILY_CODE = "AAAAA";
    private static final String UPDATED_MODEL_FAMILY_CODE = "BBBBB";

    private static final Boolean DEFAULT_MODEL_FAMILY_PARENT = false;
    private static final Boolean UPDATED_MODEL_FAMILY_PARENT = true;

    private static final Integer DEFAULT_DISPLAY_ORDER = 1;
    private static final Integer UPDATED_DISPLAY_ORDER = 2;

    private static final String DEFAULT_BRANCH_TAG = "AAAAA";
    private static final String UPDATED_BRANCH_TAG = "BBBBB";

    private static final String DEFAULT_RUN_CODE = "AAAAA";
    private static final String UPDATED_RUN_CODE = "BBBBB";

    private static final ZonedDateTime DEFAULT_LAST_MODIFIED = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_LAST_MODIFIED = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_LAST_MODIFIED_STR = DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(DEFAULT_LAST_MODIFIED);

    private static final ZonedDateTime DEFAULT_CREATED = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_CREATED = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_CREATED_STR = DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(DEFAULT_CREATED);

    private static final String DEFAULT_CREATED_BY = "AAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBB";

    private static final String DEFAULT_LAST_MODIFIED_BY = "AAAAA";
    private static final String UPDATED_LAST_MODIFIED_BY = "BBBBB";

    private static final Integer DEFAULT_FOUNTAIN_MODEL_ID = 1;
    private static final Integer UPDATED_FOUNTAIN_MODEL_ID = 2;

    @Inject
    private ModelDetailsRepository modelDetailsRepository;

    @Inject
    private ModelDetailsSearchRepository modelDetailsSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restModelDetailsMockMvc;

    private ModelDetails modelDetails;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ModelDetailsResource modelDetailsResource = new ModelDetailsResource();
        ReflectionTestUtils.setField(modelDetailsResource, "modelDetailsSearchRepository", modelDetailsSearchRepository);
        ReflectionTestUtils.setField(modelDetailsResource, "modelDetailsRepository", modelDetailsRepository);
        this.restModelDetailsMockMvc = MockMvcBuilders.standaloneSetup(modelDetailsResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ModelDetails createEntity(EntityManager em) {
        ModelDetails modelDetails = new ModelDetails()
                .code(DEFAULT_CODE)
                .name(DEFAULT_NAME)
                .version(DEFAULT_VERSION)
                .type(DEFAULT_TYPE)
                .textCode(DEFAULT_TEXT_CODE)
                .baseYearCode(DEFAULT_BASE_YEAR_CODE)
                .reportYearCode(DEFAULT_REPORT_YEAR_CODE)
                .allowDataChanges(DEFAULT_ALLOW_DATA_CHANGES)
                .modelFamilyCode(DEFAULT_MODEL_FAMILY_CODE)
                .modelFamilyParent(DEFAULT_MODEL_FAMILY_PARENT)
                .displayOrder(DEFAULT_DISPLAY_ORDER)
                .branchTag(DEFAULT_BRANCH_TAG)
                .runCode(DEFAULT_RUN_CODE)
                .lastModified(DEFAULT_LAST_MODIFIED)
                .created(DEFAULT_CREATED)
                .createdBy(DEFAULT_CREATED_BY)
                .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
                .fountainModelId(DEFAULT_FOUNTAIN_MODEL_ID);
        return modelDetails;
    }

    @Before
    public void initTest() {
        modelDetailsSearchRepository.deleteAll();
        modelDetails = createEntity(em);
    }

    @Test
    @Transactional
    public void createModelDetails() throws Exception {
        int databaseSizeBeforeCreate = modelDetailsRepository.findAll().size();

        // Create the ModelDetails

        restModelDetailsMockMvc.perform(post("/api/model-details")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(modelDetails)))
                .andExpect(status().isCreated());

        // Validate the ModelDetails in the database
        List<ModelDetails> modelDetails = modelDetailsRepository.findAll();
        assertThat(modelDetails).hasSize(databaseSizeBeforeCreate + 1);
        ModelDetails testModelDetails = modelDetails.get(modelDetails.size() - 1);
        assertThat(testModelDetails.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testModelDetails.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testModelDetails.getVersion()).isEqualTo(DEFAULT_VERSION);
        assertThat(testModelDetails.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testModelDetails.getTextCode()).isEqualTo(DEFAULT_TEXT_CODE);
        assertThat(testModelDetails.getBaseYearCode()).isEqualTo(DEFAULT_BASE_YEAR_CODE);
        assertThat(testModelDetails.getReportYearCode()).isEqualTo(DEFAULT_REPORT_YEAR_CODE);
        assertThat(testModelDetails.isAllowDataChanges()).isEqualTo(DEFAULT_ALLOW_DATA_CHANGES);
        assertThat(testModelDetails.getModelFamilyCode()).isEqualTo(DEFAULT_MODEL_FAMILY_CODE);
        assertThat(testModelDetails.isModelFamilyParent()).isEqualTo(DEFAULT_MODEL_FAMILY_PARENT);
        assertThat(testModelDetails.getDisplayOrder()).isEqualTo(DEFAULT_DISPLAY_ORDER);
        assertThat(testModelDetails.getBranchTag()).isEqualTo(DEFAULT_BRANCH_TAG);
        assertThat(testModelDetails.getRunCode()).isEqualTo(DEFAULT_RUN_CODE);
        assertThat(testModelDetails.getLastModified()).isEqualTo(DEFAULT_LAST_MODIFIED);
        assertThat(testModelDetails.getCreated()).isEqualTo(DEFAULT_CREATED);
        assertThat(testModelDetails.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testModelDetails.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);
        assertThat(testModelDetails.getFountainModelId()).isEqualTo(DEFAULT_FOUNTAIN_MODEL_ID);

        // Validate the ModelDetails in ElasticSearch
        ModelDetails modelDetailsEs = modelDetailsSearchRepository.findOne(testModelDetails.getId());
        assertThat(modelDetailsEs).isEqualToComparingFieldByField(testModelDetails);
    }

    @Test
    @Transactional
    public void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = modelDetailsRepository.findAll().size();
        // set the field null
        modelDetails.setCode(null);

        // Create the ModelDetails, which fails.

        restModelDetailsMockMvc.perform(post("/api/model-details")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(modelDetails)))
                .andExpect(status().isBadRequest());

        List<ModelDetails> modelDetails = modelDetailsRepository.findAll();
        assertThat(modelDetails).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = modelDetailsRepository.findAll().size();
        // set the field null
        modelDetails.setName(null);

        // Create the ModelDetails, which fails.

        restModelDetailsMockMvc.perform(post("/api/model-details")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(modelDetails)))
                .andExpect(status().isBadRequest());

        List<ModelDetails> modelDetails = modelDetailsRepository.findAll();
        assertThat(modelDetails).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkVersionIsRequired() throws Exception {
        int databaseSizeBeforeTest = modelDetailsRepository.findAll().size();
        // set the field null
        modelDetails.setVersion(null);

        // Create the ModelDetails, which fails.

        restModelDetailsMockMvc.perform(post("/api/model-details")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(modelDetails)))
                .andExpect(status().isBadRequest());

        List<ModelDetails> modelDetails = modelDetailsRepository.findAll();
        assertThat(modelDetails).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = modelDetailsRepository.findAll().size();
        // set the field null
        modelDetails.setType(null);

        // Create the ModelDetails, which fails.

        restModelDetailsMockMvc.perform(post("/api/model-details")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(modelDetails)))
                .andExpect(status().isBadRequest());

        List<ModelDetails> modelDetails = modelDetailsRepository.findAll();
        assertThat(modelDetails).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkModelFamilyParentIsRequired() throws Exception {
        int databaseSizeBeforeTest = modelDetailsRepository.findAll().size();
        // set the field null
        modelDetails.setModelFamilyParent(null);

        // Create the ModelDetails, which fails.

        restModelDetailsMockMvc.perform(post("/api/model-details")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(modelDetails)))
                .andExpect(status().isBadRequest());

        List<ModelDetails> modelDetails = modelDetailsRepository.findAll();
        assertThat(modelDetails).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllModelDetails() throws Exception {
        // Initialize the database
        modelDetailsRepository.saveAndFlush(modelDetails);

        // Get all the modelDetails
        restModelDetailsMockMvc.perform(get("/api/model-details?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(modelDetails.getId().intValue())))
                .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].version").value(hasItem(DEFAULT_VERSION.toString())))
                .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
                .andExpect(jsonPath("$.[*].textCode").value(hasItem(DEFAULT_TEXT_CODE.toString())))
                .andExpect(jsonPath("$.[*].baseYearCode").value(hasItem(DEFAULT_BASE_YEAR_CODE.toString())))
                .andExpect(jsonPath("$.[*].reportYearCode").value(hasItem(DEFAULT_REPORT_YEAR_CODE.toString())))
                .andExpect(jsonPath("$.[*].allowDataChanges").value(hasItem(DEFAULT_ALLOW_DATA_CHANGES.booleanValue())))
                .andExpect(jsonPath("$.[*].modelFamilyCode").value(hasItem(DEFAULT_MODEL_FAMILY_CODE.toString())))
                .andExpect(jsonPath("$.[*].modelFamilyParent").value(hasItem(DEFAULT_MODEL_FAMILY_PARENT.booleanValue())))
                .andExpect(jsonPath("$.[*].displayOrder").value(hasItem(DEFAULT_DISPLAY_ORDER)))
                .andExpect(jsonPath("$.[*].branchTag").value(hasItem(DEFAULT_BRANCH_TAG.toString())))
                .andExpect(jsonPath("$.[*].runCode").value(hasItem(DEFAULT_RUN_CODE.toString())))
                .andExpect(jsonPath("$.[*].lastModified").value(hasItem(DEFAULT_LAST_MODIFIED_STR)))
                .andExpect(jsonPath("$.[*].created").value(hasItem(DEFAULT_CREATED_STR)))
                .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.toString())))
                .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY.toString())))
                .andExpect(jsonPath("$.[*].fountainModelId").value(hasItem(DEFAULT_FOUNTAIN_MODEL_ID)));
    }

    @Test
    @Transactional
    public void getModelDetails() throws Exception {
        // Initialize the database
        modelDetailsRepository.saveAndFlush(modelDetails);

        // Get the modelDetails
        restModelDetailsMockMvc.perform(get("/api/model-details/{id}", modelDetails.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(modelDetails.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.version").value(DEFAULT_VERSION.toString()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.textCode").value(DEFAULT_TEXT_CODE.toString()))
            .andExpect(jsonPath("$.baseYearCode").value(DEFAULT_BASE_YEAR_CODE.toString()))
            .andExpect(jsonPath("$.reportYearCode").value(DEFAULT_REPORT_YEAR_CODE.toString()))
            .andExpect(jsonPath("$.allowDataChanges").value(DEFAULT_ALLOW_DATA_CHANGES.booleanValue()))
            .andExpect(jsonPath("$.modelFamilyCode").value(DEFAULT_MODEL_FAMILY_CODE.toString()))
            .andExpect(jsonPath("$.modelFamilyParent").value(DEFAULT_MODEL_FAMILY_PARENT.booleanValue()))
            .andExpect(jsonPath("$.displayOrder").value(DEFAULT_DISPLAY_ORDER))
            .andExpect(jsonPath("$.branchTag").value(DEFAULT_BRANCH_TAG.toString()))
            .andExpect(jsonPath("$.runCode").value(DEFAULT_RUN_CODE.toString()))
            .andExpect(jsonPath("$.lastModified").value(DEFAULT_LAST_MODIFIED_STR))
            .andExpect(jsonPath("$.created").value(DEFAULT_CREATED_STR))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY.toString()))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY.toString()))
            .andExpect(jsonPath("$.fountainModelId").value(DEFAULT_FOUNTAIN_MODEL_ID));
    }

    @Test
    @Transactional
    public void getNonExistingModelDetails() throws Exception {
        // Get the modelDetails
        restModelDetailsMockMvc.perform(get("/api/model-details/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateModelDetails() throws Exception {
        // Initialize the database
        modelDetailsRepository.saveAndFlush(modelDetails);
        modelDetailsSearchRepository.save(modelDetails);
        int databaseSizeBeforeUpdate = modelDetailsRepository.findAll().size();

        // Update the modelDetails
        ModelDetails updatedModelDetails = modelDetailsRepository.findOne(modelDetails.getId());
        updatedModelDetails
                .code(UPDATED_CODE)
                .name(UPDATED_NAME)
                .version(UPDATED_VERSION)
                .type(UPDATED_TYPE)
                .textCode(UPDATED_TEXT_CODE)
                .baseYearCode(UPDATED_BASE_YEAR_CODE)
                .reportYearCode(UPDATED_REPORT_YEAR_CODE)
                .allowDataChanges(UPDATED_ALLOW_DATA_CHANGES)
                .modelFamilyCode(UPDATED_MODEL_FAMILY_CODE)
                .modelFamilyParent(UPDATED_MODEL_FAMILY_PARENT)
                .displayOrder(UPDATED_DISPLAY_ORDER)
                .branchTag(UPDATED_BRANCH_TAG)
                .runCode(UPDATED_RUN_CODE)
                .lastModified(UPDATED_LAST_MODIFIED)
                .created(UPDATED_CREATED)
                .createdBy(UPDATED_CREATED_BY)
                .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
                .fountainModelId(UPDATED_FOUNTAIN_MODEL_ID);

        restModelDetailsMockMvc.perform(put("/api/model-details")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedModelDetails)))
                .andExpect(status().isOk());

        // Validate the ModelDetails in the database
        List<ModelDetails> modelDetails = modelDetailsRepository.findAll();
        assertThat(modelDetails).hasSize(databaseSizeBeforeUpdate);
        ModelDetails testModelDetails = modelDetails.get(modelDetails.size() - 1);
        assertThat(testModelDetails.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testModelDetails.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testModelDetails.getVersion()).isEqualTo(UPDATED_VERSION);
        assertThat(testModelDetails.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testModelDetails.getTextCode()).isEqualTo(UPDATED_TEXT_CODE);
        assertThat(testModelDetails.getBaseYearCode()).isEqualTo(UPDATED_BASE_YEAR_CODE);
        assertThat(testModelDetails.getReportYearCode()).isEqualTo(UPDATED_REPORT_YEAR_CODE);
        assertThat(testModelDetails.isAllowDataChanges()).isEqualTo(UPDATED_ALLOW_DATA_CHANGES);
        assertThat(testModelDetails.getModelFamilyCode()).isEqualTo(UPDATED_MODEL_FAMILY_CODE);
        assertThat(testModelDetails.isModelFamilyParent()).isEqualTo(UPDATED_MODEL_FAMILY_PARENT);
        assertThat(testModelDetails.getDisplayOrder()).isEqualTo(UPDATED_DISPLAY_ORDER);
        assertThat(testModelDetails.getBranchTag()).isEqualTo(UPDATED_BRANCH_TAG);
        assertThat(testModelDetails.getRunCode()).isEqualTo(UPDATED_RUN_CODE);
        assertThat(testModelDetails.getLastModified()).isEqualTo(UPDATED_LAST_MODIFIED);
        assertThat(testModelDetails.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testModelDetails.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testModelDetails.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);
        assertThat(testModelDetails.getFountainModelId()).isEqualTo(UPDATED_FOUNTAIN_MODEL_ID);

        // Validate the ModelDetails in ElasticSearch
        ModelDetails modelDetailsEs = modelDetailsSearchRepository.findOne(testModelDetails.getId());
        assertThat(modelDetailsEs).isEqualToComparingFieldByField(testModelDetails);
    }

    @Test
    @Transactional
    public void deleteModelDetails() throws Exception {
        // Initialize the database
        modelDetailsRepository.saveAndFlush(modelDetails);
        modelDetailsSearchRepository.save(modelDetails);
        int databaseSizeBeforeDelete = modelDetailsRepository.findAll().size();

        // Get the modelDetails
        restModelDetailsMockMvc.perform(delete("/api/model-details/{id}", modelDetails.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean modelDetailsExistsInEs = modelDetailsSearchRepository.exists(modelDetails.getId());
        assertThat(modelDetailsExistsInEs).isFalse();

        // Validate the database is empty
        List<ModelDetails> modelDetails = modelDetailsRepository.findAll();
        assertThat(modelDetails).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchModelDetails() throws Exception {
        // Initialize the database
        modelDetailsRepository.saveAndFlush(modelDetails);
        modelDetailsSearchRepository.save(modelDetails);

        // Search the modelDetails
        restModelDetailsMockMvc.perform(get("/api/_search/model-details?query=id:" + modelDetails.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(modelDetails.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].version").value(hasItem(DEFAULT_VERSION.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].textCode").value(hasItem(DEFAULT_TEXT_CODE.toString())))
            .andExpect(jsonPath("$.[*].baseYearCode").value(hasItem(DEFAULT_BASE_YEAR_CODE.toString())))
            .andExpect(jsonPath("$.[*].reportYearCode").value(hasItem(DEFAULT_REPORT_YEAR_CODE.toString())))
            .andExpect(jsonPath("$.[*].allowDataChanges").value(hasItem(DEFAULT_ALLOW_DATA_CHANGES.booleanValue())))
            .andExpect(jsonPath("$.[*].modelFamilyCode").value(hasItem(DEFAULT_MODEL_FAMILY_CODE.toString())))
            .andExpect(jsonPath("$.[*].modelFamilyParent").value(hasItem(DEFAULT_MODEL_FAMILY_PARENT.booleanValue())))
            .andExpect(jsonPath("$.[*].displayOrder").value(hasItem(DEFAULT_DISPLAY_ORDER)))
            .andExpect(jsonPath("$.[*].branchTag").value(hasItem(DEFAULT_BRANCH_TAG.toString())))
            .andExpect(jsonPath("$.[*].runCode").value(hasItem(DEFAULT_RUN_CODE.toString())))
            .andExpect(jsonPath("$.[*].lastModified").value(hasItem(DEFAULT_LAST_MODIFIED_STR)))
            .andExpect(jsonPath("$.[*].created").value(hasItem(DEFAULT_CREATED_STR)))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY.toString())))
            .andExpect(jsonPath("$.[*].fountainModelId").value(hasItem(DEFAULT_FOUNTAIN_MODEL_ID)));
    }
}
