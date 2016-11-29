package uk.gov.ofwat.fountain.modelbuilder.web.rest;

import uk.gov.ofwat.fountain.modelbuilder.ModelBuilderApp;

import uk.gov.ofwat.fountain.modelbuilder.domain.Transfer;
import uk.gov.ofwat.fountain.modelbuilder.repository.TransferRepository;
import uk.gov.ofwat.fountain.modelbuilder.repository.search.TransferSearchRepository;

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
 * Test class for the TransferResource REST controller.
 *
 * @see TransferResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ModelBuilderApp.class)
public class TransferResourceIntTest {

    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";

    @Inject
    private TransferRepository transferRepository;

    @Inject
    private TransferSearchRepository transferSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restTransferMockMvc;

    private Transfer transfer;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TransferResource transferResource = new TransferResource();
        ReflectionTestUtils.setField(transferResource, "transferSearchRepository", transferSearchRepository);
        ReflectionTestUtils.setField(transferResource, "transferRepository", transferRepository);
        this.restTransferMockMvc = MockMvcBuilders.standaloneSetup(transferResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Transfer createEntity(EntityManager em) {
        Transfer transfer = new Transfer()
                .description(DEFAULT_DESCRIPTION);
        return transfer;
    }

    @Before
    public void initTest() {
        transferSearchRepository.deleteAll();
        transfer = createEntity(em);
    }

    @Test
    @Transactional
    public void createTransfer() throws Exception {
        int databaseSizeBeforeCreate = transferRepository.findAll().size();

        // Create the Transfer

        restTransferMockMvc.perform(post("/api/transfers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(transfer)))
                .andExpect(status().isCreated());

        // Validate the Transfer in the database
        List<Transfer> transfers = transferRepository.findAll();
        assertThat(transfers).hasSize(databaseSizeBeforeCreate + 1);
        Transfer testTransfer = transfers.get(transfers.size() - 1);
        assertThat(testTransfer.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);

        // Validate the Transfer in ElasticSearch
        Transfer transferEs = transferSearchRepository.findOne(testTransfer.getId());
        assertThat(transferEs).isEqualToComparingFieldByField(testTransfer);
    }

    @Test
    @Transactional
    public void getAllTransfers() throws Exception {
        // Initialize the database
        transferRepository.saveAndFlush(transfer);

        // Get all the transfers
        restTransferMockMvc.perform(get("/api/transfers?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(transfer.getId().intValue())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void getTransfer() throws Exception {
        // Initialize the database
        transferRepository.saveAndFlush(transfer);

        // Get the transfer
        restTransferMockMvc.perform(get("/api/transfers/{id}", transfer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(transfer.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingTransfer() throws Exception {
        // Get the transfer
        restTransferMockMvc.perform(get("/api/transfers/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTransfer() throws Exception {
        // Initialize the database
        transferRepository.saveAndFlush(transfer);
        transferSearchRepository.save(transfer);
        int databaseSizeBeforeUpdate = transferRepository.findAll().size();

        // Update the transfer
        Transfer updatedTransfer = transferRepository.findOne(transfer.getId());
        updatedTransfer
                .description(UPDATED_DESCRIPTION);

        restTransferMockMvc.perform(put("/api/transfers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedTransfer)))
                .andExpect(status().isOk());

        // Validate the Transfer in the database
        List<Transfer> transfers = transferRepository.findAll();
        assertThat(transfers).hasSize(databaseSizeBeforeUpdate);
        Transfer testTransfer = transfers.get(transfers.size() - 1);
        assertThat(testTransfer.getDescription()).isEqualTo(UPDATED_DESCRIPTION);

        // Validate the Transfer in ElasticSearch
        Transfer transferEs = transferSearchRepository.findOne(testTransfer.getId());
        assertThat(transferEs).isEqualToComparingFieldByField(testTransfer);
    }

    @Test
    @Transactional
    public void deleteTransfer() throws Exception {
        // Initialize the database
        transferRepository.saveAndFlush(transfer);
        transferSearchRepository.save(transfer);
        int databaseSizeBeforeDelete = transferRepository.findAll().size();

        // Get the transfer
        restTransferMockMvc.perform(delete("/api/transfers/{id}", transfer.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean transferExistsInEs = transferSearchRepository.exists(transfer.getId());
        assertThat(transferExistsInEs).isFalse();

        // Validate the database is empty
        List<Transfer> transfers = transferRepository.findAll();
        assertThat(transfers).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchTransfer() throws Exception {
        // Initialize the database
        transferRepository.saveAndFlush(transfer);
        transferSearchRepository.save(transfer);

        // Search the transfer
        restTransferMockMvc.perform(get("/api/_search/transfers?query=id:" + transfer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(transfer.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }
}
