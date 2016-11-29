package uk.gov.ofwat.fountain.modelbuilder.web.rest;

import uk.gov.ofwat.fountain.modelbuilder.ModelBuilderApp;

import uk.gov.ofwat.fountain.modelbuilder.domain.CellRange;
import uk.gov.ofwat.fountain.modelbuilder.repository.CellRangeRepository;
import uk.gov.ofwat.fountain.modelbuilder.repository.search.CellRangeSearchRepository;

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
 * Test class for the CellRangeResource REST controller.
 *
 * @see CellRangeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ModelBuilderApp.class)
public class CellRangeResourceIntTest {

    private static final String DEFAULT_START_YEAR = "AAAAA";
    private static final String UPDATED_START_YEAR = "BBBBB";

    private static final String DEFAULT_END_YEAR = "AAAAA";
    private static final String UPDATED_END_YEAR = "BBBBB";

    @Inject
    private CellRangeRepository cellRangeRepository;

    @Inject
    private CellRangeSearchRepository cellRangeSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restCellRangeMockMvc;

    private CellRange cellRange;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CellRangeResource cellRangeResource = new CellRangeResource();
        ReflectionTestUtils.setField(cellRangeResource, "cellRangeSearchRepository", cellRangeSearchRepository);
        ReflectionTestUtils.setField(cellRangeResource, "cellRangeRepository", cellRangeRepository);
        this.restCellRangeMockMvc = MockMvcBuilders.standaloneSetup(cellRangeResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CellRange createEntity(EntityManager em) {
        CellRange cellRange = new CellRange()
                .startYear(DEFAULT_START_YEAR)
                .endYear(DEFAULT_END_YEAR);
        return cellRange;
    }

    @Before
    public void initTest() {
        cellRangeSearchRepository.deleteAll();
        cellRange = createEntity(em);
    }

    @Test
    @Transactional
    public void createCellRange() throws Exception {
        int databaseSizeBeforeCreate = cellRangeRepository.findAll().size();

        // Create the CellRange

        restCellRangeMockMvc.perform(post("/api/cell-ranges")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(cellRange)))
                .andExpect(status().isCreated());

        // Validate the CellRange in the database
        List<CellRange> cellRanges = cellRangeRepository.findAll();
        assertThat(cellRanges).hasSize(databaseSizeBeforeCreate + 1);
        CellRange testCellRange = cellRanges.get(cellRanges.size() - 1);
        assertThat(testCellRange.getStartYear()).isEqualTo(DEFAULT_START_YEAR);
        assertThat(testCellRange.getEndYear()).isEqualTo(DEFAULT_END_YEAR);

        // Validate the CellRange in ElasticSearch
        CellRange cellRangeEs = cellRangeSearchRepository.findOne(testCellRange.getId());
        assertThat(cellRangeEs).isEqualToComparingFieldByField(testCellRange);
    }

    @Test
    @Transactional
    public void checkStartYearIsRequired() throws Exception {
        int databaseSizeBeforeTest = cellRangeRepository.findAll().size();
        // set the field null
        cellRange.setStartYear(null);

        // Create the CellRange, which fails.

        restCellRangeMockMvc.perform(post("/api/cell-ranges")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(cellRange)))
                .andExpect(status().isBadRequest());

        List<CellRange> cellRanges = cellRangeRepository.findAll();
        assertThat(cellRanges).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEndYearIsRequired() throws Exception {
        int databaseSizeBeforeTest = cellRangeRepository.findAll().size();
        // set the field null
        cellRange.setEndYear(null);

        // Create the CellRange, which fails.

        restCellRangeMockMvc.perform(post("/api/cell-ranges")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(cellRange)))
                .andExpect(status().isBadRequest());

        List<CellRange> cellRanges = cellRangeRepository.findAll();
        assertThat(cellRanges).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCellRanges() throws Exception {
        // Initialize the database
        cellRangeRepository.saveAndFlush(cellRange);

        // Get all the cellRanges
        restCellRangeMockMvc.perform(get("/api/cell-ranges?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(cellRange.getId().intValue())))
                .andExpect(jsonPath("$.[*].startYear").value(hasItem(DEFAULT_START_YEAR.toString())))
                .andExpect(jsonPath("$.[*].endYear").value(hasItem(DEFAULT_END_YEAR.toString())));
    }

    @Test
    @Transactional
    public void getCellRange() throws Exception {
        // Initialize the database
        cellRangeRepository.saveAndFlush(cellRange);

        // Get the cellRange
        restCellRangeMockMvc.perform(get("/api/cell-ranges/{id}", cellRange.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(cellRange.getId().intValue()))
            .andExpect(jsonPath("$.startYear").value(DEFAULT_START_YEAR.toString()))
            .andExpect(jsonPath("$.endYear").value(DEFAULT_END_YEAR.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCellRange() throws Exception {
        // Get the cellRange
        restCellRangeMockMvc.perform(get("/api/cell-ranges/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCellRange() throws Exception {
        // Initialize the database
        cellRangeRepository.saveAndFlush(cellRange);
        cellRangeSearchRepository.save(cellRange);
        int databaseSizeBeforeUpdate = cellRangeRepository.findAll().size();

        // Update the cellRange
        CellRange updatedCellRange = cellRangeRepository.findOne(cellRange.getId());
        updatedCellRange
                .startYear(UPDATED_START_YEAR)
                .endYear(UPDATED_END_YEAR);

        restCellRangeMockMvc.perform(put("/api/cell-ranges")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedCellRange)))
                .andExpect(status().isOk());

        // Validate the CellRange in the database
        List<CellRange> cellRanges = cellRangeRepository.findAll();
        assertThat(cellRanges).hasSize(databaseSizeBeforeUpdate);
        CellRange testCellRange = cellRanges.get(cellRanges.size() - 1);
        assertThat(testCellRange.getStartYear()).isEqualTo(UPDATED_START_YEAR);
        assertThat(testCellRange.getEndYear()).isEqualTo(UPDATED_END_YEAR);

        // Validate the CellRange in ElasticSearch
        CellRange cellRangeEs = cellRangeSearchRepository.findOne(testCellRange.getId());
        assertThat(cellRangeEs).isEqualToComparingFieldByField(testCellRange);
    }

    @Test
    @Transactional
    public void deleteCellRange() throws Exception {
        // Initialize the database
        cellRangeRepository.saveAndFlush(cellRange);
        cellRangeSearchRepository.save(cellRange);
        int databaseSizeBeforeDelete = cellRangeRepository.findAll().size();

        // Get the cellRange
        restCellRangeMockMvc.perform(delete("/api/cell-ranges/{id}", cellRange.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean cellRangeExistsInEs = cellRangeSearchRepository.exists(cellRange.getId());
        assertThat(cellRangeExistsInEs).isFalse();

        // Validate the database is empty
        List<CellRange> cellRanges = cellRangeRepository.findAll();
        assertThat(cellRanges).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchCellRange() throws Exception {
        // Initialize the database
        cellRangeRepository.saveAndFlush(cellRange);
        cellRangeSearchRepository.save(cellRange);

        // Search the cellRange
        restCellRangeMockMvc.perform(get("/api/_search/cell-ranges?query=id:" + cellRange.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cellRange.getId().intValue())))
            .andExpect(jsonPath("$.[*].startYear").value(hasItem(DEFAULT_START_YEAR.toString())))
            .andExpect(jsonPath("$.[*].endYear").value(hasItem(DEFAULT_END_YEAR.toString())));
    }
}
