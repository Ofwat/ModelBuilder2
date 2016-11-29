package uk.gov.ofwat.fountain.modelbuilder.web.rest;

import uk.gov.ofwat.fountain.modelbuilder.ModelBuilderApp;

import uk.gov.ofwat.fountain.modelbuilder.domain.AuditDetails;
import uk.gov.ofwat.fountain.modelbuilder.repository.AuditDetailsRepository;
import uk.gov.ofwat.fountain.modelbuilder.repository.search.AuditDetailsSearchRepository;

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
 * Test class for the AuditDetailsResource REST controller.
 *
 * @see AuditDetailsResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ModelBuilderApp.class)
public class AuditDetailsResourceIntTest {

    private static final String DEFAULT_USERNAME = "AAAAA";
    private static final String UPDATED_USERNAME = "BBBBB";

    private static final String DEFAULT_TIMESTAMP = "AAAAA";
    private static final String UPDATED_TIMESTAMP = "BBBBB";

    private static final String DEFAULT_REASON = "AAAAA";
    private static final String UPDATED_REASON = "BBBBB";

    @Inject
    private AuditDetailsRepository auditDetailsRepository;

    @Inject
    private AuditDetailsSearchRepository auditDetailsSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restAuditDetailsMockMvc;

    private AuditDetails auditDetails;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        AuditDetailsResource auditDetailsResource = new AuditDetailsResource();
        ReflectionTestUtils.setField(auditDetailsResource, "auditDetailsSearchRepository", auditDetailsSearchRepository);
        ReflectionTestUtils.setField(auditDetailsResource, "auditDetailsRepository", auditDetailsRepository);
        this.restAuditDetailsMockMvc = MockMvcBuilders.standaloneSetup(auditDetailsResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AuditDetails createEntity(EntityManager em) {
        AuditDetails auditDetails = new AuditDetails()
                .username(DEFAULT_USERNAME)
                .timestamp(DEFAULT_TIMESTAMP)
                .reason(DEFAULT_REASON);
        return auditDetails;
    }

    @Before
    public void initTest() {
        auditDetailsSearchRepository.deleteAll();
        auditDetails = createEntity(em);
    }

    @Test
    @Transactional
    public void createAuditDetails() throws Exception {
        int databaseSizeBeforeCreate = auditDetailsRepository.findAll().size();

        // Create the AuditDetails

        restAuditDetailsMockMvc.perform(post("/api/audit-details")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(auditDetails)))
                .andExpect(status().isCreated());

        // Validate the AuditDetails in the database
        List<AuditDetails> auditDetails = auditDetailsRepository.findAll();
        assertThat(auditDetails).hasSize(databaseSizeBeforeCreate + 1);
        AuditDetails testAuditDetails = auditDetails.get(auditDetails.size() - 1);
        assertThat(testAuditDetails.getUsername()).isEqualTo(DEFAULT_USERNAME);
        assertThat(testAuditDetails.getTimestamp()).isEqualTo(DEFAULT_TIMESTAMP);
        assertThat(testAuditDetails.getReason()).isEqualTo(DEFAULT_REASON);

        // Validate the AuditDetails in ElasticSearch
        AuditDetails auditDetailsEs = auditDetailsSearchRepository.findOne(testAuditDetails.getId());
        assertThat(auditDetailsEs).isEqualToComparingFieldByField(testAuditDetails);
    }

    @Test
    @Transactional
    public void getAllAuditDetails() throws Exception {
        // Initialize the database
        auditDetailsRepository.saveAndFlush(auditDetails);

        // Get all the auditDetails
        restAuditDetailsMockMvc.perform(get("/api/audit-details?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(auditDetails.getId().intValue())))
                .andExpect(jsonPath("$.[*].username").value(hasItem(DEFAULT_USERNAME.toString())))
                .andExpect(jsonPath("$.[*].timestamp").value(hasItem(DEFAULT_TIMESTAMP.toString())))
                .andExpect(jsonPath("$.[*].reason").value(hasItem(DEFAULT_REASON.toString())));
    }

    @Test
    @Transactional
    public void getAuditDetails() throws Exception {
        // Initialize the database
        auditDetailsRepository.saveAndFlush(auditDetails);

        // Get the auditDetails
        restAuditDetailsMockMvc.perform(get("/api/audit-details/{id}", auditDetails.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(auditDetails.getId().intValue()))
            .andExpect(jsonPath("$.username").value(DEFAULT_USERNAME.toString()))
            .andExpect(jsonPath("$.timestamp").value(DEFAULT_TIMESTAMP.toString()))
            .andExpect(jsonPath("$.reason").value(DEFAULT_REASON.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingAuditDetails() throws Exception {
        // Get the auditDetails
        restAuditDetailsMockMvc.perform(get("/api/audit-details/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAuditDetails() throws Exception {
        // Initialize the database
        auditDetailsRepository.saveAndFlush(auditDetails);
        auditDetailsSearchRepository.save(auditDetails);
        int databaseSizeBeforeUpdate = auditDetailsRepository.findAll().size();

        // Update the auditDetails
        AuditDetails updatedAuditDetails = auditDetailsRepository.findOne(auditDetails.getId());
        updatedAuditDetails
                .username(UPDATED_USERNAME)
                .timestamp(UPDATED_TIMESTAMP)
                .reason(UPDATED_REASON);

        restAuditDetailsMockMvc.perform(put("/api/audit-details")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedAuditDetails)))
                .andExpect(status().isOk());

        // Validate the AuditDetails in the database
        List<AuditDetails> auditDetails = auditDetailsRepository.findAll();
        assertThat(auditDetails).hasSize(databaseSizeBeforeUpdate);
        AuditDetails testAuditDetails = auditDetails.get(auditDetails.size() - 1);
        assertThat(testAuditDetails.getUsername()).isEqualTo(UPDATED_USERNAME);
        assertThat(testAuditDetails.getTimestamp()).isEqualTo(UPDATED_TIMESTAMP);
        assertThat(testAuditDetails.getReason()).isEqualTo(UPDATED_REASON);

        // Validate the AuditDetails in ElasticSearch
        AuditDetails auditDetailsEs = auditDetailsSearchRepository.findOne(testAuditDetails.getId());
        assertThat(auditDetailsEs).isEqualToComparingFieldByField(testAuditDetails);
    }

    @Test
    @Transactional
    public void deleteAuditDetails() throws Exception {
        // Initialize the database
        auditDetailsRepository.saveAndFlush(auditDetails);
        auditDetailsSearchRepository.save(auditDetails);
        int databaseSizeBeforeDelete = auditDetailsRepository.findAll().size();

        // Get the auditDetails
        restAuditDetailsMockMvc.perform(delete("/api/audit-details/{id}", auditDetails.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean auditDetailsExistsInEs = auditDetailsSearchRepository.exists(auditDetails.getId());
        assertThat(auditDetailsExistsInEs).isFalse();

        // Validate the database is empty
        List<AuditDetails> auditDetails = auditDetailsRepository.findAll();
        assertThat(auditDetails).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchAuditDetails() throws Exception {
        // Initialize the database
        auditDetailsRepository.saveAndFlush(auditDetails);
        auditDetailsSearchRepository.save(auditDetails);

        // Search the auditDetails
        restAuditDetailsMockMvc.perform(get("/api/_search/audit-details?query=id:" + auditDetails.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(auditDetails.getId().intValue())))
            .andExpect(jsonPath("$.[*].username").value(hasItem(DEFAULT_USERNAME.toString())))
            .andExpect(jsonPath("$.[*].timestamp").value(hasItem(DEFAULT_TIMESTAMP.toString())))
            .andExpect(jsonPath("$.[*].reason").value(hasItem(DEFAULT_REASON.toString())));
    }
}
