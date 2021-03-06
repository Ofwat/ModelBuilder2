package uk.gov.ofwat.fountain.modelbuilder.web.rest;

import uk.gov.ofwat.fountain.modelbuilder.ModelBuilderApp;

import uk.gov.ofwat.fountain.modelbuilder.domain.Cell;
import uk.gov.ofwat.fountain.modelbuilder.repository.CellRepository;
import uk.gov.ofwat.fountain.modelbuilder.repository.search.CellSearchRepository;

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
 * Test class for the CellResource REST controller.
 *
 * @see CellResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ModelBuilderApp.class)
public class CellResourceIntTest {

    private static final String DEFAULT_CODE = "AAAAA";
    private static final String UPDATED_CODE = "BBBBB";

    private static final String DEFAULT_YEAR = "AAAAA";
    private static final String UPDATED_YEAR = "BBBBB";

    private static final String DEFAULT_EQUATION = "AAAAA";
    private static final String UPDATED_EQUATION = "BBBBB";

    private static final String DEFAULT_TYPE = "AAAAA";
    private static final String UPDATED_TYPE = "BBBBB";

    private static final String DEFAULT_CG_TYPE = "AAAAA";
    private static final String UPDATED_CG_TYPE = "BBBBB";

    @Inject
    private CellRepository cellRepository;

    @Inject
    private CellSearchRepository cellSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restCellMockMvc;

    private Cell cell;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CellResource cellResource = new CellResource();
        ReflectionTestUtils.setField(cellResource, "cellSearchRepository", cellSearchRepository);
        ReflectionTestUtils.setField(cellResource, "cellRepository", cellRepository);
        this.restCellMockMvc = MockMvcBuilders.standaloneSetup(cellResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cell createEntity(EntityManager em) {
        Cell cell = new Cell()
                .code(DEFAULT_CODE)
                .year(DEFAULT_YEAR)
                .equation(DEFAULT_EQUATION)
                .type(DEFAULT_TYPE)
                .cgType(DEFAULT_CG_TYPE);
        return cell;
    }

    @Before
    public void initTest() {
        cellSearchRepository.deleteAll();
        cell = createEntity(em);
    }

    @Test
    @Transactional
    public void createCell() throws Exception {
        int databaseSizeBeforeCreate = cellRepository.findAll().size();

        // Create the Cell

        restCellMockMvc.perform(post("/api/cells")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(cell)))
                .andExpect(status().isCreated());

        // Validate the Cell in the database
        List<Cell> cells = cellRepository.findAll();
        assertThat(cells).hasSize(databaseSizeBeforeCreate + 1);
        Cell testCell = cells.get(cells.size() - 1);
        assertThat(testCell.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testCell.getYear()).isEqualTo(DEFAULT_YEAR);
        assertThat(testCell.getEquation()).isEqualTo(DEFAULT_EQUATION);
        assertThat(testCell.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testCell.getCgType()).isEqualTo(DEFAULT_CG_TYPE);

        // Validate the Cell in ElasticSearch
        Cell cellEs = cellSearchRepository.findOne(testCell.getId());
        assertThat(cellEs).isEqualToComparingFieldByField(testCell);
    }

    @Test
    @Transactional
    public void checkYearIsRequired() throws Exception {
        int databaseSizeBeforeTest = cellRepository.findAll().size();
        // set the field null
        cell.setYear(null);

        // Create the Cell, which fails.

        restCellMockMvc.perform(post("/api/cells")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(cell)))
                .andExpect(status().isBadRequest());

        List<Cell> cells = cellRepository.findAll();
        assertThat(cells).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = cellRepository.findAll().size();
        // set the field null
        cell.setType(null);

        // Create the Cell, which fails.

        restCellMockMvc.perform(post("/api/cells")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(cell)))
                .andExpect(status().isBadRequest());

        List<Cell> cells = cellRepository.findAll();
        assertThat(cells).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCells() throws Exception {
        // Initialize the database
        cellRepository.saveAndFlush(cell);

        // Get all the cells
        restCellMockMvc.perform(get("/api/cells?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(cell.getId().intValue())))
                .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
                .andExpect(jsonPath("$.[*].year").value(hasItem(DEFAULT_YEAR.toString())))
                .andExpect(jsonPath("$.[*].equation").value(hasItem(DEFAULT_EQUATION.toString())))
                .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
                .andExpect(jsonPath("$.[*].cgType").value(hasItem(DEFAULT_CG_TYPE.toString())));
    }

    @Test
    @Transactional
    public void getCell() throws Exception {
        // Initialize the database
        cellRepository.saveAndFlush(cell);

        // Get the cell
        restCellMockMvc.perform(get("/api/cells/{id}", cell.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(cell.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
            .andExpect(jsonPath("$.year").value(DEFAULT_YEAR.toString()))
            .andExpect(jsonPath("$.equation").value(DEFAULT_EQUATION.toString()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.cgType").value(DEFAULT_CG_TYPE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCell() throws Exception {
        // Get the cell
        restCellMockMvc.perform(get("/api/cells/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCell() throws Exception {
        // Initialize the database
        cellRepository.saveAndFlush(cell);
        cellSearchRepository.save(cell);
        int databaseSizeBeforeUpdate = cellRepository.findAll().size();

        // Update the cell
        Cell updatedCell = cellRepository.findOne(cell.getId());
        updatedCell
                .code(UPDATED_CODE)
                .year(UPDATED_YEAR)
                .equation(UPDATED_EQUATION)
                .type(UPDATED_TYPE)
                .cgType(UPDATED_CG_TYPE);

        restCellMockMvc.perform(put("/api/cells")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedCell)))
                .andExpect(status().isOk());

        // Validate the Cell in the database
        List<Cell> cells = cellRepository.findAll();
        assertThat(cells).hasSize(databaseSizeBeforeUpdate);
        Cell testCell = cells.get(cells.size() - 1);
        assertThat(testCell.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testCell.getYear()).isEqualTo(UPDATED_YEAR);
        assertThat(testCell.getEquation()).isEqualTo(UPDATED_EQUATION);
        assertThat(testCell.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testCell.getCgType()).isEqualTo(UPDATED_CG_TYPE);

        // Validate the Cell in ElasticSearch
        Cell cellEs = cellSearchRepository.findOne(testCell.getId());
        assertThat(cellEs).isEqualToComparingFieldByField(testCell);
    }

    @Test
    @Transactional
    public void deleteCell() throws Exception {
        // Initialize the database
        cellRepository.saveAndFlush(cell);
        cellSearchRepository.save(cell);
        int databaseSizeBeforeDelete = cellRepository.findAll().size();

        // Get the cell
        restCellMockMvc.perform(delete("/api/cells/{id}", cell.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean cellExistsInEs = cellSearchRepository.exists(cell.getId());
        assertThat(cellExistsInEs).isFalse();

        // Validate the database is empty
        List<Cell> cells = cellRepository.findAll();
        assertThat(cells).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchCell() throws Exception {
        // Initialize the database
        cellRepository.saveAndFlush(cell);
        cellSearchRepository.save(cell);

        // Search the cell
        restCellMockMvc.perform(get("/api/_search/cells?query=id:" + cell.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cell.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].year").value(hasItem(DEFAULT_YEAR.toString())))
            .andExpect(jsonPath("$.[*].equation").value(hasItem(DEFAULT_EQUATION.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].cgType").value(hasItem(DEFAULT_CG_TYPE.toString())));
    }
}
