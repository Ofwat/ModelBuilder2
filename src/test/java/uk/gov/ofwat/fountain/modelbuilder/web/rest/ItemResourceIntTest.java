package uk.gov.ofwat.fountain.modelbuilder.web.rest;

import uk.gov.ofwat.fountain.modelbuilder.ModelBuilderApp;

import uk.gov.ofwat.fountain.modelbuilder.domain.Item;
import uk.gov.ofwat.fountain.modelbuilder.repository.ItemRepository;
import uk.gov.ofwat.fountain.modelbuilder.repository.search.ItemSearchRepository;

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
 * Test class for the ItemResource REST controller.
 *
 * @see ItemResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ModelBuilderApp.class)
public class ItemResourceIntTest {

    private static final String DEFAULT_CODE = "AAAAA";
    private static final String UPDATED_CODE = "BBBBB";

    private static final String DEFAULT_VERSION_NUMBER = "AAAAA";
    private static final String UPDATED_VERSION_NUMBER = "BBBBB";

    private static final String DEFAULT_PRICEBASE_CODE = "AAAAA";
    private static final String UPDATED_PRICEBASE_CODE = "BBBBB";

    private static final String DEFAULT_PURPOSE_CODE = "AAAAA";
    private static final String UPDATED_PURPOSE_CODE = "BBBBB";

    private static final String DEFAULT_TEXT_CODE = "AAAAA";
    private static final String UPDATED_TEXT_CODE = "BBBBB";

    @Inject
    private ItemRepository itemRepository;

    @Inject
    private ItemSearchRepository itemSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restItemMockMvc;

    private Item item;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ItemResource itemResource = new ItemResource();
        ReflectionTestUtils.setField(itemResource, "itemSearchRepository", itemSearchRepository);
        ReflectionTestUtils.setField(itemResource, "itemRepository", itemRepository);
        this.restItemMockMvc = MockMvcBuilders.standaloneSetup(itemResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Item createEntity(EntityManager em) {
        Item item = new Item()
                .code(DEFAULT_CODE)
                .versionNumber(DEFAULT_VERSION_NUMBER)
                .pricebaseCode(DEFAULT_PRICEBASE_CODE)
                .purposeCode(DEFAULT_PURPOSE_CODE)
                .textCode(DEFAULT_TEXT_CODE);
        return item;
    }

    @Before
    public void initTest() {
        itemSearchRepository.deleteAll();
        item = createEntity(em);
    }

    @Test
    @Transactional
    public void createItem() throws Exception {
        int databaseSizeBeforeCreate = itemRepository.findAll().size();

        // Create the Item

        restItemMockMvc.perform(post("/api/items")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(item)))
                .andExpect(status().isCreated());

        // Validate the Item in the database
        List<Item> items = itemRepository.findAll();
        assertThat(items).hasSize(databaseSizeBeforeCreate + 1);
        Item testItem = items.get(items.size() - 1);
        assertThat(testItem.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testItem.getVersionNumber()).isEqualTo(DEFAULT_VERSION_NUMBER);
        assertThat(testItem.getPricebaseCode()).isEqualTo(DEFAULT_PRICEBASE_CODE);
        assertThat(testItem.getPurposeCode()).isEqualTo(DEFAULT_PURPOSE_CODE);
        assertThat(testItem.getTextCode()).isEqualTo(DEFAULT_TEXT_CODE);

        // Validate the Item in ElasticSearch
        Item itemEs = itemSearchRepository.findOne(testItem.getId());
        assertThat(itemEs).isEqualToComparingFieldByField(testItem);
    }

    @Test
    @Transactional
    public void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = itemRepository.findAll().size();
        // set the field null
        item.setCode(null);

        // Create the Item, which fails.

        restItemMockMvc.perform(post("/api/items")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(item)))
                .andExpect(status().isBadRequest());

        List<Item> items = itemRepository.findAll();
        assertThat(items).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkVersionNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = itemRepository.findAll().size();
        // set the field null
        item.setVersionNumber(null);

        // Create the Item, which fails.

        restItemMockMvc.perform(post("/api/items")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(item)))
                .andExpect(status().isBadRequest());

        List<Item> items = itemRepository.findAll();
        assertThat(items).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllItems() throws Exception {
        // Initialize the database
        itemRepository.saveAndFlush(item);

        // Get all the items
        restItemMockMvc.perform(get("/api/items?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(item.getId().intValue())))
                .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
                .andExpect(jsonPath("$.[*].versionNumber").value(hasItem(DEFAULT_VERSION_NUMBER.toString())))
                .andExpect(jsonPath("$.[*].pricebaseCode").value(hasItem(DEFAULT_PRICEBASE_CODE.toString())))
                .andExpect(jsonPath("$.[*].purposeCode").value(hasItem(DEFAULT_PURPOSE_CODE.toString())))
                .andExpect(jsonPath("$.[*].textCode").value(hasItem(DEFAULT_TEXT_CODE.toString())));
    }

    @Test
    @Transactional
    public void getItem() throws Exception {
        // Initialize the database
        itemRepository.saveAndFlush(item);

        // Get the item
        restItemMockMvc.perform(get("/api/items/{id}", item.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(item.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
            .andExpect(jsonPath("$.versionNumber").value(DEFAULT_VERSION_NUMBER.toString()))
            .andExpect(jsonPath("$.pricebaseCode").value(DEFAULT_PRICEBASE_CODE.toString()))
            .andExpect(jsonPath("$.purposeCode").value(DEFAULT_PURPOSE_CODE.toString()))
            .andExpect(jsonPath("$.textCode").value(DEFAULT_TEXT_CODE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingItem() throws Exception {
        // Get the item
        restItemMockMvc.perform(get("/api/items/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateItem() throws Exception {
        // Initialize the database
        itemRepository.saveAndFlush(item);
        itemSearchRepository.save(item);
        int databaseSizeBeforeUpdate = itemRepository.findAll().size();

        // Update the item
        Item updatedItem = itemRepository.findOne(item.getId());
        updatedItem
                .code(UPDATED_CODE)
                .versionNumber(UPDATED_VERSION_NUMBER)
                .pricebaseCode(UPDATED_PRICEBASE_CODE)
                .purposeCode(UPDATED_PURPOSE_CODE)
                .textCode(UPDATED_TEXT_CODE);

        restItemMockMvc.perform(put("/api/items")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedItem)))
                .andExpect(status().isOk());

        // Validate the Item in the database
        List<Item> items = itemRepository.findAll();
        assertThat(items).hasSize(databaseSizeBeforeUpdate);
        Item testItem = items.get(items.size() - 1);
        assertThat(testItem.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testItem.getVersionNumber()).isEqualTo(UPDATED_VERSION_NUMBER);
        assertThat(testItem.getPricebaseCode()).isEqualTo(UPDATED_PRICEBASE_CODE);
        assertThat(testItem.getPurposeCode()).isEqualTo(UPDATED_PURPOSE_CODE);
        assertThat(testItem.getTextCode()).isEqualTo(UPDATED_TEXT_CODE);

        // Validate the Item in ElasticSearch
        Item itemEs = itemSearchRepository.findOne(testItem.getId());
        assertThat(itemEs).isEqualToComparingFieldByField(testItem);
    }

    @Test
    @Transactional
    public void deleteItem() throws Exception {
        // Initialize the database
        itemRepository.saveAndFlush(item);
        itemSearchRepository.save(item);
        int databaseSizeBeforeDelete = itemRepository.findAll().size();

        // Get the item
        restItemMockMvc.perform(delete("/api/items/{id}", item.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean itemExistsInEs = itemSearchRepository.exists(item.getId());
        assertThat(itemExistsInEs).isFalse();

        // Validate the database is empty
        List<Item> items = itemRepository.findAll();
        assertThat(items).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchItem() throws Exception {
        // Initialize the database
        itemRepository.saveAndFlush(item);
        itemSearchRepository.save(item);

        // Search the item
        restItemMockMvc.perform(get("/api/_search/items?query=id:" + item.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(item.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].versionNumber").value(hasItem(DEFAULT_VERSION_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].pricebaseCode").value(hasItem(DEFAULT_PRICEBASE_CODE.toString())))
            .andExpect(jsonPath("$.[*].purposeCode").value(hasItem(DEFAULT_PURPOSE_CODE.toString())))
            .andExpect(jsonPath("$.[*].textCode").value(hasItem(DEFAULT_TEXT_CODE.toString())));
    }
}
