package uk.gov.ofwat.fountain.modelbuilder.web.rest;

import uk.gov.ofwat.fountain.modelbuilder.ModelBuilderApp;

import uk.gov.ofwat.fountain.modelbuilder.domain.TransferBlockItem;
import uk.gov.ofwat.fountain.modelbuilder.repository.TransferBlockItemRepository;
import uk.gov.ofwat.fountain.modelbuilder.repository.search.TransferBlockItemSearchRepository;

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
 * Test class for the TransferBlockItemResource REST controller.
 *
 * @see TransferBlockItemResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ModelBuilderApp.class)
public class TransferBlockItemResourceIntTest {

    private static final String DEFAULT_ITEM_CODE = "AAAAA";
    private static final String UPDATED_ITEM_CODE = "BBBBB";

    private static final String DEFAULT_COMPANY_TYPE = "AAAAA";
    private static final String UPDATED_COMPANY_TYPE = "BBBBB";

    @Inject
    private TransferBlockItemRepository transferBlockItemRepository;

    @Inject
    private TransferBlockItemSearchRepository transferBlockItemSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restTransferBlockItemMockMvc;

    private TransferBlockItem transferBlockItem;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TransferBlockItemResource transferBlockItemResource = new TransferBlockItemResource();
        ReflectionTestUtils.setField(transferBlockItemResource, "transferBlockItemSearchRepository", transferBlockItemSearchRepository);
        ReflectionTestUtils.setField(transferBlockItemResource, "transferBlockItemRepository", transferBlockItemRepository);
        this.restTransferBlockItemMockMvc = MockMvcBuilders.standaloneSetup(transferBlockItemResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TransferBlockItem createEntity(EntityManager em) {
        TransferBlockItem transferBlockItem = new TransferBlockItem()
                .itemCode(DEFAULT_ITEM_CODE)
                .companyType(DEFAULT_COMPANY_TYPE);
        return transferBlockItem;
    }

    @Before
    public void initTest() {
        transferBlockItemSearchRepository.deleteAll();
        transferBlockItem = createEntity(em);
    }

    @Test
    @Transactional
    public void createTransferBlockItem() throws Exception {
        int databaseSizeBeforeCreate = transferBlockItemRepository.findAll().size();

        // Create the TransferBlockItem

        restTransferBlockItemMockMvc.perform(post("/api/transfer-block-items")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(transferBlockItem)))
                .andExpect(status().isCreated());

        // Validate the TransferBlockItem in the database
        List<TransferBlockItem> transferBlockItems = transferBlockItemRepository.findAll();
        assertThat(transferBlockItems).hasSize(databaseSizeBeforeCreate + 1);
        TransferBlockItem testTransferBlockItem = transferBlockItems.get(transferBlockItems.size() - 1);
        assertThat(testTransferBlockItem.getItemCode()).isEqualTo(DEFAULT_ITEM_CODE);
        assertThat(testTransferBlockItem.getCompanyType()).isEqualTo(DEFAULT_COMPANY_TYPE);

        // Validate the TransferBlockItem in ElasticSearch
        TransferBlockItem transferBlockItemEs = transferBlockItemSearchRepository.findOne(testTransferBlockItem.getId());
        assertThat(transferBlockItemEs).isEqualToComparingFieldByField(testTransferBlockItem);
    }

    @Test
    @Transactional
    public void checkItemCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = transferBlockItemRepository.findAll().size();
        // set the field null
        transferBlockItem.setItemCode(null);

        // Create the TransferBlockItem, which fails.

        restTransferBlockItemMockMvc.perform(post("/api/transfer-block-items")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(transferBlockItem)))
                .andExpect(status().isBadRequest());

        List<TransferBlockItem> transferBlockItems = transferBlockItemRepository.findAll();
        assertThat(transferBlockItems).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTransferBlockItems() throws Exception {
        // Initialize the database
        transferBlockItemRepository.saveAndFlush(transferBlockItem);

        // Get all the transferBlockItems
        restTransferBlockItemMockMvc.perform(get("/api/transfer-block-items?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(transferBlockItem.getId().intValue())))
                .andExpect(jsonPath("$.[*].itemCode").value(hasItem(DEFAULT_ITEM_CODE.toString())))
                .andExpect(jsonPath("$.[*].companyType").value(hasItem(DEFAULT_COMPANY_TYPE.toString())));
    }

    @Test
    @Transactional
    public void getTransferBlockItem() throws Exception {
        // Initialize the database
        transferBlockItemRepository.saveAndFlush(transferBlockItem);

        // Get the transferBlockItem
        restTransferBlockItemMockMvc.perform(get("/api/transfer-block-items/{id}", transferBlockItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(transferBlockItem.getId().intValue()))
            .andExpect(jsonPath("$.itemCode").value(DEFAULT_ITEM_CODE.toString()))
            .andExpect(jsonPath("$.companyType").value(DEFAULT_COMPANY_TYPE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingTransferBlockItem() throws Exception {
        // Get the transferBlockItem
        restTransferBlockItemMockMvc.perform(get("/api/transfer-block-items/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTransferBlockItem() throws Exception {
        // Initialize the database
        transferBlockItemRepository.saveAndFlush(transferBlockItem);
        transferBlockItemSearchRepository.save(transferBlockItem);
        int databaseSizeBeforeUpdate = transferBlockItemRepository.findAll().size();

        // Update the transferBlockItem
        TransferBlockItem updatedTransferBlockItem = transferBlockItemRepository.findOne(transferBlockItem.getId());
        updatedTransferBlockItem
                .itemCode(UPDATED_ITEM_CODE)
                .companyType(UPDATED_COMPANY_TYPE);

        restTransferBlockItemMockMvc.perform(put("/api/transfer-block-items")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedTransferBlockItem)))
                .andExpect(status().isOk());

        // Validate the TransferBlockItem in the database
        List<TransferBlockItem> transferBlockItems = transferBlockItemRepository.findAll();
        assertThat(transferBlockItems).hasSize(databaseSizeBeforeUpdate);
        TransferBlockItem testTransferBlockItem = transferBlockItems.get(transferBlockItems.size() - 1);
        assertThat(testTransferBlockItem.getItemCode()).isEqualTo(UPDATED_ITEM_CODE);
        assertThat(testTransferBlockItem.getCompanyType()).isEqualTo(UPDATED_COMPANY_TYPE);

        // Validate the TransferBlockItem in ElasticSearch
        TransferBlockItem transferBlockItemEs = transferBlockItemSearchRepository.findOne(testTransferBlockItem.getId());
        assertThat(transferBlockItemEs).isEqualToComparingFieldByField(testTransferBlockItem);
    }

    @Test
    @Transactional
    public void deleteTransferBlockItem() throws Exception {
        // Initialize the database
        transferBlockItemRepository.saveAndFlush(transferBlockItem);
        transferBlockItemSearchRepository.save(transferBlockItem);
        int databaseSizeBeforeDelete = transferBlockItemRepository.findAll().size();

        // Get the transferBlockItem
        restTransferBlockItemMockMvc.perform(delete("/api/transfer-block-items/{id}", transferBlockItem.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean transferBlockItemExistsInEs = transferBlockItemSearchRepository.exists(transferBlockItem.getId());
        assertThat(transferBlockItemExistsInEs).isFalse();

        // Validate the database is empty
        List<TransferBlockItem> transferBlockItems = transferBlockItemRepository.findAll();
        assertThat(transferBlockItems).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchTransferBlockItem() throws Exception {
        // Initialize the database
        transferBlockItemRepository.saveAndFlush(transferBlockItem);
        transferBlockItemSearchRepository.save(transferBlockItem);

        // Search the transferBlockItem
        restTransferBlockItemMockMvc.perform(get("/api/_search/transfer-block-items?query=id:" + transferBlockItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(transferBlockItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].itemCode").value(hasItem(DEFAULT_ITEM_CODE.toString())))
            .andExpect(jsonPath("$.[*].companyType").value(hasItem(DEFAULT_COMPANY_TYPE.toString())));
    }
}
