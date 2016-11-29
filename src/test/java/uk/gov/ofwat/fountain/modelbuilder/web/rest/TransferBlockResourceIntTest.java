package uk.gov.ofwat.fountain.modelbuilder.web.rest;

import uk.gov.ofwat.fountain.modelbuilder.ModelBuilderApp;

import uk.gov.ofwat.fountain.modelbuilder.domain.TransferBlock;
import uk.gov.ofwat.fountain.modelbuilder.repository.TransferBlockRepository;
import uk.gov.ofwat.fountain.modelbuilder.repository.search.TransferBlockSearchRepository;

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
 * Test class for the TransferBlockResource REST controller.
 *
 * @see TransferBlockResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ModelBuilderApp.class)
public class TransferBlockResourceIntTest {

    @Inject
    private TransferBlockRepository transferBlockRepository;

    @Inject
    private TransferBlockSearchRepository transferBlockSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restTransferBlockMockMvc;

    private TransferBlock transferBlock;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TransferBlockResource transferBlockResource = new TransferBlockResource();
        ReflectionTestUtils.setField(transferBlockResource, "transferBlockSearchRepository", transferBlockSearchRepository);
        ReflectionTestUtils.setField(transferBlockResource, "transferBlockRepository", transferBlockRepository);
        this.restTransferBlockMockMvc = MockMvcBuilders.standaloneSetup(transferBlockResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TransferBlock createEntity(EntityManager em) {
        TransferBlock transferBlock = new TransferBlock();
        return transferBlock;
    }

    @Before
    public void initTest() {
        transferBlockSearchRepository.deleteAll();
        transferBlock = createEntity(em);
    }

    @Test
    @Transactional
    public void createTransferBlock() throws Exception {
        int databaseSizeBeforeCreate = transferBlockRepository.findAll().size();

        // Create the TransferBlock

        restTransferBlockMockMvc.perform(post("/api/transfer-blocks")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(transferBlock)))
                .andExpect(status().isCreated());

        // Validate the TransferBlock in the database
        List<TransferBlock> transferBlocks = transferBlockRepository.findAll();
        assertThat(transferBlocks).hasSize(databaseSizeBeforeCreate + 1);
        TransferBlock testTransferBlock = transferBlocks.get(transferBlocks.size() - 1);

        // Validate the TransferBlock in ElasticSearch
        TransferBlock transferBlockEs = transferBlockSearchRepository.findOne(testTransferBlock.getId());
        assertThat(transferBlockEs).isEqualToComparingFieldByField(testTransferBlock);
    }

    @Test
    @Transactional
    public void getAllTransferBlocks() throws Exception {
        // Initialize the database
        transferBlockRepository.saveAndFlush(transferBlock);

        // Get all the transferBlocks
        restTransferBlockMockMvc.perform(get("/api/transfer-blocks?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(transferBlock.getId().intValue())));
    }

    @Test
    @Transactional
    public void getTransferBlock() throws Exception {
        // Initialize the database
        transferBlockRepository.saveAndFlush(transferBlock);

        // Get the transferBlock
        restTransferBlockMockMvc.perform(get("/api/transfer-blocks/{id}", transferBlock.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(transferBlock.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingTransferBlock() throws Exception {
        // Get the transferBlock
        restTransferBlockMockMvc.perform(get("/api/transfer-blocks/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTransferBlock() throws Exception {
        // Initialize the database
        transferBlockRepository.saveAndFlush(transferBlock);
        transferBlockSearchRepository.save(transferBlock);
        int databaseSizeBeforeUpdate = transferBlockRepository.findAll().size();

        // Update the transferBlock
        TransferBlock updatedTransferBlock = transferBlockRepository.findOne(transferBlock.getId());

        restTransferBlockMockMvc.perform(put("/api/transfer-blocks")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedTransferBlock)))
                .andExpect(status().isOk());

        // Validate the TransferBlock in the database
        List<TransferBlock> transferBlocks = transferBlockRepository.findAll();
        assertThat(transferBlocks).hasSize(databaseSizeBeforeUpdate);
        TransferBlock testTransferBlock = transferBlocks.get(transferBlocks.size() - 1);

        // Validate the TransferBlock in ElasticSearch
        TransferBlock transferBlockEs = transferBlockSearchRepository.findOne(testTransferBlock.getId());
        assertThat(transferBlockEs).isEqualToComparingFieldByField(testTransferBlock);
    }

    @Test
    @Transactional
    public void deleteTransferBlock() throws Exception {
        // Initialize the database
        transferBlockRepository.saveAndFlush(transferBlock);
        transferBlockSearchRepository.save(transferBlock);
        int databaseSizeBeforeDelete = transferBlockRepository.findAll().size();

        // Get the transferBlock
        restTransferBlockMockMvc.perform(delete("/api/transfer-blocks/{id}", transferBlock.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean transferBlockExistsInEs = transferBlockSearchRepository.exists(transferBlock.getId());
        assertThat(transferBlockExistsInEs).isFalse();

        // Validate the database is empty
        List<TransferBlock> transferBlocks = transferBlockRepository.findAll();
        assertThat(transferBlocks).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchTransferBlock() throws Exception {
        // Initialize the database
        transferBlockRepository.saveAndFlush(transferBlock);
        transferBlockSearchRepository.save(transferBlock);

        // Search the transferBlock
        restTransferBlockMockMvc.perform(get("/api/_search/transfer-blocks?query=id:" + transferBlock.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(transferBlock.getId().intValue())));
    }
}
