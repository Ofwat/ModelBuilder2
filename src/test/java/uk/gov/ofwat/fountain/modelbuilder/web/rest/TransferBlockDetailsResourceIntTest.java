package uk.gov.ofwat.fountain.modelbuilder.web.rest;

import uk.gov.ofwat.fountain.modelbuilder.ModelBuilderApp;

import uk.gov.ofwat.fountain.modelbuilder.domain.TransferBlockDetails;
import uk.gov.ofwat.fountain.modelbuilder.repository.TransferBlockDetailsRepository;
import uk.gov.ofwat.fountain.modelbuilder.repository.search.TransferBlockDetailsSearchRepository;

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
 * Test class for the TransferBlockDetailsResource REST controller.
 *
 * @see TransferBlockDetailsResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ModelBuilderApp.class)
public class TransferBlockDetailsResourceIntTest {

    private static final String DEFAULT_FROM_MODEL_CODE = "AAAAA";
    private static final String UPDATED_FROM_MODEL_CODE = "BBBBB";

    private static final String DEFAULT_FROM_VERSION_CODE = "AAAAA";
    private static final String UPDATED_FROM_VERSION_CODE = "BBBBB";

    private static final String DEFAULT_FROM_PAGE_CODE = "AAAAA";
    private static final String UPDATED_FROM_PAGE_CODE = "BBBBB";

    private static final String DEFAULT_TO_MODEL_CODE = "AAAAA";
    private static final String UPDATED_TO_MODEL_CODE = "BBBBB";

    private static final String DEFAULT_TO_VERSION_CODE = "AAAAA";
    private static final String UPDATED_TO_VERSION_CODE = "BBBBB";

    private static final String DEFAULT_TO_PAGE_CODE = "AAAAA";
    private static final String UPDATED_TO_PAGE_CODE = "BBBBB";

    private static final String DEFAULT_TO_MACRO_CODE = "AAAAA";
    private static final String UPDATED_TO_MACRO_CODE = "BBBBB";

    @Inject
    private TransferBlockDetailsRepository transferBlockDetailsRepository;

    @Inject
    private TransferBlockDetailsSearchRepository transferBlockDetailsSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restTransferBlockDetailsMockMvc;

    private TransferBlockDetails transferBlockDetails;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TransferBlockDetailsResource transferBlockDetailsResource = new TransferBlockDetailsResource();
        ReflectionTestUtils.setField(transferBlockDetailsResource, "transferBlockDetailsSearchRepository", transferBlockDetailsSearchRepository);
        ReflectionTestUtils.setField(transferBlockDetailsResource, "transferBlockDetailsRepository", transferBlockDetailsRepository);
        this.restTransferBlockDetailsMockMvc = MockMvcBuilders.standaloneSetup(transferBlockDetailsResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TransferBlockDetails createEntity(EntityManager em) {
        TransferBlockDetails transferBlockDetails = new TransferBlockDetails()
                .fromModelCode(DEFAULT_FROM_MODEL_CODE)
                .fromVersionCode(DEFAULT_FROM_VERSION_CODE)
                .fromPageCode(DEFAULT_FROM_PAGE_CODE)
                .toModelCode(DEFAULT_TO_MODEL_CODE)
                .toVersionCode(DEFAULT_TO_VERSION_CODE)
                .toPageCode(DEFAULT_TO_PAGE_CODE)
                .toMacroCode(DEFAULT_TO_MACRO_CODE);
        return transferBlockDetails;
    }

    @Before
    public void initTest() {
        transferBlockDetailsSearchRepository.deleteAll();
        transferBlockDetails = createEntity(em);
    }

    @Test
    @Transactional
    public void createTransferBlockDetails() throws Exception {
        int databaseSizeBeforeCreate = transferBlockDetailsRepository.findAll().size();

        // Create the TransferBlockDetails

        restTransferBlockDetailsMockMvc.perform(post("/api/transfer-block-details")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(transferBlockDetails)))
                .andExpect(status().isCreated());

        // Validate the TransferBlockDetails in the database
        List<TransferBlockDetails> transferBlockDetails = transferBlockDetailsRepository.findAll();
        assertThat(transferBlockDetails).hasSize(databaseSizeBeforeCreate + 1);
        TransferBlockDetails testTransferBlockDetails = transferBlockDetails.get(transferBlockDetails.size() - 1);
        assertThat(testTransferBlockDetails.getFromModelCode()).isEqualTo(DEFAULT_FROM_MODEL_CODE);
        assertThat(testTransferBlockDetails.getFromVersionCode()).isEqualTo(DEFAULT_FROM_VERSION_CODE);
        assertThat(testTransferBlockDetails.getFromPageCode()).isEqualTo(DEFAULT_FROM_PAGE_CODE);
        assertThat(testTransferBlockDetails.getToModelCode()).isEqualTo(DEFAULT_TO_MODEL_CODE);
        assertThat(testTransferBlockDetails.getToVersionCode()).isEqualTo(DEFAULT_TO_VERSION_CODE);
        assertThat(testTransferBlockDetails.getToPageCode()).isEqualTo(DEFAULT_TO_PAGE_CODE);
        assertThat(testTransferBlockDetails.getToMacroCode()).isEqualTo(DEFAULT_TO_MACRO_CODE);

        // Validate the TransferBlockDetails in ElasticSearch
        TransferBlockDetails transferBlockDetailsEs = transferBlockDetailsSearchRepository.findOne(testTransferBlockDetails.getId());
        assertThat(transferBlockDetailsEs).isEqualToComparingFieldByField(testTransferBlockDetails);
    }

    @Test
    @Transactional
    public void getAllTransferBlockDetails() throws Exception {
        // Initialize the database
        transferBlockDetailsRepository.saveAndFlush(transferBlockDetails);

        // Get all the transferBlockDetails
        restTransferBlockDetailsMockMvc.perform(get("/api/transfer-block-details?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(transferBlockDetails.getId().intValue())))
                .andExpect(jsonPath("$.[*].fromModelCode").value(hasItem(DEFAULT_FROM_MODEL_CODE.toString())))
                .andExpect(jsonPath("$.[*].fromVersionCode").value(hasItem(DEFAULT_FROM_VERSION_CODE.toString())))
                .andExpect(jsonPath("$.[*].fromPageCode").value(hasItem(DEFAULT_FROM_PAGE_CODE.toString())))
                .andExpect(jsonPath("$.[*].toModelCode").value(hasItem(DEFAULT_TO_MODEL_CODE.toString())))
                .andExpect(jsonPath("$.[*].toVersionCode").value(hasItem(DEFAULT_TO_VERSION_CODE.toString())))
                .andExpect(jsonPath("$.[*].toPageCode").value(hasItem(DEFAULT_TO_PAGE_CODE.toString())))
                .andExpect(jsonPath("$.[*].toMacroCode").value(hasItem(DEFAULT_TO_MACRO_CODE.toString())));
    }

    @Test
    @Transactional
    public void getTransferBlockDetails() throws Exception {
        // Initialize the database
        transferBlockDetailsRepository.saveAndFlush(transferBlockDetails);

        // Get the transferBlockDetails
        restTransferBlockDetailsMockMvc.perform(get("/api/transfer-block-details/{id}", transferBlockDetails.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(transferBlockDetails.getId().intValue()))
            .andExpect(jsonPath("$.fromModelCode").value(DEFAULT_FROM_MODEL_CODE.toString()))
            .andExpect(jsonPath("$.fromVersionCode").value(DEFAULT_FROM_VERSION_CODE.toString()))
            .andExpect(jsonPath("$.fromPageCode").value(DEFAULT_FROM_PAGE_CODE.toString()))
            .andExpect(jsonPath("$.toModelCode").value(DEFAULT_TO_MODEL_CODE.toString()))
            .andExpect(jsonPath("$.toVersionCode").value(DEFAULT_TO_VERSION_CODE.toString()))
            .andExpect(jsonPath("$.toPageCode").value(DEFAULT_TO_PAGE_CODE.toString()))
            .andExpect(jsonPath("$.toMacroCode").value(DEFAULT_TO_MACRO_CODE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingTransferBlockDetails() throws Exception {
        // Get the transferBlockDetails
        restTransferBlockDetailsMockMvc.perform(get("/api/transfer-block-details/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTransferBlockDetails() throws Exception {
        // Initialize the database
        transferBlockDetailsRepository.saveAndFlush(transferBlockDetails);
        transferBlockDetailsSearchRepository.save(transferBlockDetails);
        int databaseSizeBeforeUpdate = transferBlockDetailsRepository.findAll().size();

        // Update the transferBlockDetails
        TransferBlockDetails updatedTransferBlockDetails = transferBlockDetailsRepository.findOne(transferBlockDetails.getId());
        updatedTransferBlockDetails
                .fromModelCode(UPDATED_FROM_MODEL_CODE)
                .fromVersionCode(UPDATED_FROM_VERSION_CODE)
                .fromPageCode(UPDATED_FROM_PAGE_CODE)
                .toModelCode(UPDATED_TO_MODEL_CODE)
                .toVersionCode(UPDATED_TO_VERSION_CODE)
                .toPageCode(UPDATED_TO_PAGE_CODE)
                .toMacroCode(UPDATED_TO_MACRO_CODE);

        restTransferBlockDetailsMockMvc.perform(put("/api/transfer-block-details")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedTransferBlockDetails)))
                .andExpect(status().isOk());

        // Validate the TransferBlockDetails in the database
        List<TransferBlockDetails> transferBlockDetails = transferBlockDetailsRepository.findAll();
        assertThat(transferBlockDetails).hasSize(databaseSizeBeforeUpdate);
        TransferBlockDetails testTransferBlockDetails = transferBlockDetails.get(transferBlockDetails.size() - 1);
        assertThat(testTransferBlockDetails.getFromModelCode()).isEqualTo(UPDATED_FROM_MODEL_CODE);
        assertThat(testTransferBlockDetails.getFromVersionCode()).isEqualTo(UPDATED_FROM_VERSION_CODE);
        assertThat(testTransferBlockDetails.getFromPageCode()).isEqualTo(UPDATED_FROM_PAGE_CODE);
        assertThat(testTransferBlockDetails.getToModelCode()).isEqualTo(UPDATED_TO_MODEL_CODE);
        assertThat(testTransferBlockDetails.getToVersionCode()).isEqualTo(UPDATED_TO_VERSION_CODE);
        assertThat(testTransferBlockDetails.getToPageCode()).isEqualTo(UPDATED_TO_PAGE_CODE);
        assertThat(testTransferBlockDetails.getToMacroCode()).isEqualTo(UPDATED_TO_MACRO_CODE);

        // Validate the TransferBlockDetails in ElasticSearch
        TransferBlockDetails transferBlockDetailsEs = transferBlockDetailsSearchRepository.findOne(testTransferBlockDetails.getId());
        assertThat(transferBlockDetailsEs).isEqualToComparingFieldByField(testTransferBlockDetails);
    }

    @Test
    @Transactional
    public void deleteTransferBlockDetails() throws Exception {
        // Initialize the database
        transferBlockDetailsRepository.saveAndFlush(transferBlockDetails);
        transferBlockDetailsSearchRepository.save(transferBlockDetails);
        int databaseSizeBeforeDelete = transferBlockDetailsRepository.findAll().size();

        // Get the transferBlockDetails
        restTransferBlockDetailsMockMvc.perform(delete("/api/transfer-block-details/{id}", transferBlockDetails.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean transferBlockDetailsExistsInEs = transferBlockDetailsSearchRepository.exists(transferBlockDetails.getId());
        assertThat(transferBlockDetailsExistsInEs).isFalse();

        // Validate the database is empty
        List<TransferBlockDetails> transferBlockDetails = transferBlockDetailsRepository.findAll();
        assertThat(transferBlockDetails).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchTransferBlockDetails() throws Exception {
        // Initialize the database
        transferBlockDetailsRepository.saveAndFlush(transferBlockDetails);
        transferBlockDetailsSearchRepository.save(transferBlockDetails);

        // Search the transferBlockDetails
        restTransferBlockDetailsMockMvc.perform(get("/api/_search/transfer-block-details?query=id:" + transferBlockDetails.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(transferBlockDetails.getId().intValue())))
            .andExpect(jsonPath("$.[*].fromModelCode").value(hasItem(DEFAULT_FROM_MODEL_CODE.toString())))
            .andExpect(jsonPath("$.[*].fromVersionCode").value(hasItem(DEFAULT_FROM_VERSION_CODE.toString())))
            .andExpect(jsonPath("$.[*].fromPageCode").value(hasItem(DEFAULT_FROM_PAGE_CODE.toString())))
            .andExpect(jsonPath("$.[*].toModelCode").value(hasItem(DEFAULT_TO_MODEL_CODE.toString())))
            .andExpect(jsonPath("$.[*].toVersionCode").value(hasItem(DEFAULT_TO_VERSION_CODE.toString())))
            .andExpect(jsonPath("$.[*].toPageCode").value(hasItem(DEFAULT_TO_PAGE_CODE.toString())))
            .andExpect(jsonPath("$.[*].toMacroCode").value(hasItem(DEFAULT_TO_MACRO_CODE.toString())));
    }
}
