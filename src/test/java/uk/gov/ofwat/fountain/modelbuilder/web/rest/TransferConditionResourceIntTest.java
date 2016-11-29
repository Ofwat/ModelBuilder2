package uk.gov.ofwat.fountain.modelbuilder.web.rest;

import uk.gov.ofwat.fountain.modelbuilder.ModelBuilderApp;

import uk.gov.ofwat.fountain.modelbuilder.domain.TransferCondition;
import uk.gov.ofwat.fountain.modelbuilder.repository.TransferConditionRepository;
import uk.gov.ofwat.fountain.modelbuilder.repository.search.TransferConditionSearchRepository;

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
 * Test class for the TransferConditionResource REST controller.
 *
 * @see TransferConditionResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ModelBuilderApp.class)
public class TransferConditionResourceIntTest {

    private static final String DEFAULT_ITEM_CODE = "AAAAA";
    private static final String UPDATED_ITEM_CODE = "BBBBB";

    private static final String DEFAULT_YEAR_CODE = "AAAAA";
    private static final String UPDATED_YEAR_CODE = "BBBBB";

    private static final String DEFAULT_VALUE = "AAAAA";
    private static final String UPDATED_VALUE = "BBBBB";

    private static final String DEFAULT_FAILURE_MESSAGE = "AAAAA";
    private static final String UPDATED_FAILURE_MESSAGE = "BBBBB";

    @Inject
    private TransferConditionRepository transferConditionRepository;

    @Inject
    private TransferConditionSearchRepository transferConditionSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restTransferConditionMockMvc;

    private TransferCondition transferCondition;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TransferConditionResource transferConditionResource = new TransferConditionResource();
        ReflectionTestUtils.setField(transferConditionResource, "transferConditionSearchRepository", transferConditionSearchRepository);
        ReflectionTestUtils.setField(transferConditionResource, "transferConditionRepository", transferConditionRepository);
        this.restTransferConditionMockMvc = MockMvcBuilders.standaloneSetup(transferConditionResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TransferCondition createEntity(EntityManager em) {
        TransferCondition transferCondition = new TransferCondition()
                .itemCode(DEFAULT_ITEM_CODE)
                .yearCode(DEFAULT_YEAR_CODE)
                .value(DEFAULT_VALUE)
                .failureMessage(DEFAULT_FAILURE_MESSAGE);
        return transferCondition;
    }

    @Before
    public void initTest() {
        transferConditionSearchRepository.deleteAll();
        transferCondition = createEntity(em);
    }

    @Test
    @Transactional
    public void createTransferCondition() throws Exception {
        int databaseSizeBeforeCreate = transferConditionRepository.findAll().size();

        // Create the TransferCondition

        restTransferConditionMockMvc.perform(post("/api/transfer-conditions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(transferCondition)))
                .andExpect(status().isCreated());

        // Validate the TransferCondition in the database
        List<TransferCondition> transferConditions = transferConditionRepository.findAll();
        assertThat(transferConditions).hasSize(databaseSizeBeforeCreate + 1);
        TransferCondition testTransferCondition = transferConditions.get(transferConditions.size() - 1);
        assertThat(testTransferCondition.getItemCode()).isEqualTo(DEFAULT_ITEM_CODE);
        assertThat(testTransferCondition.getYearCode()).isEqualTo(DEFAULT_YEAR_CODE);
        assertThat(testTransferCondition.getValue()).isEqualTo(DEFAULT_VALUE);
        assertThat(testTransferCondition.getFailureMessage()).isEqualTo(DEFAULT_FAILURE_MESSAGE);

        // Validate the TransferCondition in ElasticSearch
        TransferCondition transferConditionEs = transferConditionSearchRepository.findOne(testTransferCondition.getId());
        assertThat(transferConditionEs).isEqualToComparingFieldByField(testTransferCondition);
    }

    @Test
    @Transactional
    public void checkItemCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = transferConditionRepository.findAll().size();
        // set the field null
        transferCondition.setItemCode(null);

        // Create the TransferCondition, which fails.

        restTransferConditionMockMvc.perform(post("/api/transfer-conditions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(transferCondition)))
                .andExpect(status().isBadRequest());

        List<TransferCondition> transferConditions = transferConditionRepository.findAll();
        assertThat(transferConditions).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkYearCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = transferConditionRepository.findAll().size();
        // set the field null
        transferCondition.setYearCode(null);

        // Create the TransferCondition, which fails.

        restTransferConditionMockMvc.perform(post("/api/transfer-conditions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(transferCondition)))
                .andExpect(status().isBadRequest());

        List<TransferCondition> transferConditions = transferConditionRepository.findAll();
        assertThat(transferConditions).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkValueIsRequired() throws Exception {
        int databaseSizeBeforeTest = transferConditionRepository.findAll().size();
        // set the field null
        transferCondition.setValue(null);

        // Create the TransferCondition, which fails.

        restTransferConditionMockMvc.perform(post("/api/transfer-conditions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(transferCondition)))
                .andExpect(status().isBadRequest());

        List<TransferCondition> transferConditions = transferConditionRepository.findAll();
        assertThat(transferConditions).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkFailureMessageIsRequired() throws Exception {
        int databaseSizeBeforeTest = transferConditionRepository.findAll().size();
        // set the field null
        transferCondition.setFailureMessage(null);

        // Create the TransferCondition, which fails.

        restTransferConditionMockMvc.perform(post("/api/transfer-conditions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(transferCondition)))
                .andExpect(status().isBadRequest());

        List<TransferCondition> transferConditions = transferConditionRepository.findAll();
        assertThat(transferConditions).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTransferConditions() throws Exception {
        // Initialize the database
        transferConditionRepository.saveAndFlush(transferCondition);

        // Get all the transferConditions
        restTransferConditionMockMvc.perform(get("/api/transfer-conditions?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(transferCondition.getId().intValue())))
                .andExpect(jsonPath("$.[*].itemCode").value(hasItem(DEFAULT_ITEM_CODE.toString())))
                .andExpect(jsonPath("$.[*].yearCode").value(hasItem(DEFAULT_YEAR_CODE.toString())))
                .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE.toString())))
                .andExpect(jsonPath("$.[*].failureMessage").value(hasItem(DEFAULT_FAILURE_MESSAGE.toString())));
    }

    @Test
    @Transactional
    public void getTransferCondition() throws Exception {
        // Initialize the database
        transferConditionRepository.saveAndFlush(transferCondition);

        // Get the transferCondition
        restTransferConditionMockMvc.perform(get("/api/transfer-conditions/{id}", transferCondition.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(transferCondition.getId().intValue()))
            .andExpect(jsonPath("$.itemCode").value(DEFAULT_ITEM_CODE.toString()))
            .andExpect(jsonPath("$.yearCode").value(DEFAULT_YEAR_CODE.toString()))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE.toString()))
            .andExpect(jsonPath("$.failureMessage").value(DEFAULT_FAILURE_MESSAGE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingTransferCondition() throws Exception {
        // Get the transferCondition
        restTransferConditionMockMvc.perform(get("/api/transfer-conditions/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTransferCondition() throws Exception {
        // Initialize the database
        transferConditionRepository.saveAndFlush(transferCondition);
        transferConditionSearchRepository.save(transferCondition);
        int databaseSizeBeforeUpdate = transferConditionRepository.findAll().size();

        // Update the transferCondition
        TransferCondition updatedTransferCondition = transferConditionRepository.findOne(transferCondition.getId());
        updatedTransferCondition
                .itemCode(UPDATED_ITEM_CODE)
                .yearCode(UPDATED_YEAR_CODE)
                .value(UPDATED_VALUE)
                .failureMessage(UPDATED_FAILURE_MESSAGE);

        restTransferConditionMockMvc.perform(put("/api/transfer-conditions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedTransferCondition)))
                .andExpect(status().isOk());

        // Validate the TransferCondition in the database
        List<TransferCondition> transferConditions = transferConditionRepository.findAll();
        assertThat(transferConditions).hasSize(databaseSizeBeforeUpdate);
        TransferCondition testTransferCondition = transferConditions.get(transferConditions.size() - 1);
        assertThat(testTransferCondition.getItemCode()).isEqualTo(UPDATED_ITEM_CODE);
        assertThat(testTransferCondition.getYearCode()).isEqualTo(UPDATED_YEAR_CODE);
        assertThat(testTransferCondition.getValue()).isEqualTo(UPDATED_VALUE);
        assertThat(testTransferCondition.getFailureMessage()).isEqualTo(UPDATED_FAILURE_MESSAGE);

        // Validate the TransferCondition in ElasticSearch
        TransferCondition transferConditionEs = transferConditionSearchRepository.findOne(testTransferCondition.getId());
        assertThat(transferConditionEs).isEqualToComparingFieldByField(testTransferCondition);
    }

    @Test
    @Transactional
    public void deleteTransferCondition() throws Exception {
        // Initialize the database
        transferConditionRepository.saveAndFlush(transferCondition);
        transferConditionSearchRepository.save(transferCondition);
        int databaseSizeBeforeDelete = transferConditionRepository.findAll().size();

        // Get the transferCondition
        restTransferConditionMockMvc.perform(delete("/api/transfer-conditions/{id}", transferCondition.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean transferConditionExistsInEs = transferConditionSearchRepository.exists(transferCondition.getId());
        assertThat(transferConditionExistsInEs).isFalse();

        // Validate the database is empty
        List<TransferCondition> transferConditions = transferConditionRepository.findAll();
        assertThat(transferConditions).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchTransferCondition() throws Exception {
        // Initialize the database
        transferConditionRepository.saveAndFlush(transferCondition);
        transferConditionSearchRepository.save(transferCondition);

        // Search the transferCondition
        restTransferConditionMockMvc.perform(get("/api/_search/transfer-conditions?query=id:" + transferCondition.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(transferCondition.getId().intValue())))
            .andExpect(jsonPath("$.[*].itemCode").value(hasItem(DEFAULT_ITEM_CODE.toString())))
            .andExpect(jsonPath("$.[*].yearCode").value(hasItem(DEFAULT_YEAR_CODE.toString())))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE.toString())))
            .andExpect(jsonPath("$.[*].failureMessage").value(hasItem(DEFAULT_FAILURE_MESSAGE.toString())));
    }
}
