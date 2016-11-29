package uk.gov.ofwat.fountain.modelbuilder.web.rest;

import uk.gov.ofwat.fountain.modelbuilder.ModelBuilderApp;

import uk.gov.ofwat.fountain.modelbuilder.domain.YearCode;
import uk.gov.ofwat.fountain.modelbuilder.repository.YearCodeRepository;
import uk.gov.ofwat.fountain.modelbuilder.repository.search.YearCodeSearchRepository;

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
 * Test class for the YearCodeResource REST controller.
 *
 * @see YearCodeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ModelBuilderApp.class)
public class YearCodeResourceIntTest {

    private static final String DEFAULT_YEAR_CODE = "AAAAA";
    private static final String UPDATED_YEAR_CODE = "BBBBB";

    @Inject
    private YearCodeRepository yearCodeRepository;

    @Inject
    private YearCodeSearchRepository yearCodeSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restYearCodeMockMvc;

    private YearCode yearCode;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        YearCodeResource yearCodeResource = new YearCodeResource();
        ReflectionTestUtils.setField(yearCodeResource, "yearCodeSearchRepository", yearCodeSearchRepository);
        ReflectionTestUtils.setField(yearCodeResource, "yearCodeRepository", yearCodeRepository);
        this.restYearCodeMockMvc = MockMvcBuilders.standaloneSetup(yearCodeResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static YearCode createEntity(EntityManager em) {
        YearCode yearCode = new YearCode()
                .yearCode(DEFAULT_YEAR_CODE);
        return yearCode;
    }

    @Before
    public void initTest() {
        yearCodeSearchRepository.deleteAll();
        yearCode = createEntity(em);
    }

    @Test
    @Transactional
    public void createYearCode() throws Exception {
        int databaseSizeBeforeCreate = yearCodeRepository.findAll().size();

        // Create the YearCode

        restYearCodeMockMvc.perform(post("/api/year-codes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(yearCode)))
                .andExpect(status().isCreated());

        // Validate the YearCode in the database
        List<YearCode> yearCodes = yearCodeRepository.findAll();
        assertThat(yearCodes).hasSize(databaseSizeBeforeCreate + 1);
        YearCode testYearCode = yearCodes.get(yearCodes.size() - 1);
        assertThat(testYearCode.getYearCode()).isEqualTo(DEFAULT_YEAR_CODE);

        // Validate the YearCode in ElasticSearch
        YearCode yearCodeEs = yearCodeSearchRepository.findOne(testYearCode.getId());
        assertThat(yearCodeEs).isEqualToComparingFieldByField(testYearCode);
    }

    @Test
    @Transactional
    public void checkYearCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = yearCodeRepository.findAll().size();
        // set the field null
        yearCode.setYearCode(null);

        // Create the YearCode, which fails.

        restYearCodeMockMvc.perform(post("/api/year-codes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(yearCode)))
                .andExpect(status().isBadRequest());

        List<YearCode> yearCodes = yearCodeRepository.findAll();
        assertThat(yearCodes).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllYearCodes() throws Exception {
        // Initialize the database
        yearCodeRepository.saveAndFlush(yearCode);

        // Get all the yearCodes
        restYearCodeMockMvc.perform(get("/api/year-codes?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(yearCode.getId().intValue())))
                .andExpect(jsonPath("$.[*].yearCode").value(hasItem(DEFAULT_YEAR_CODE.toString())));
    }

    @Test
    @Transactional
    public void getYearCode() throws Exception {
        // Initialize the database
        yearCodeRepository.saveAndFlush(yearCode);

        // Get the yearCode
        restYearCodeMockMvc.perform(get("/api/year-codes/{id}", yearCode.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(yearCode.getId().intValue()))
            .andExpect(jsonPath("$.yearCode").value(DEFAULT_YEAR_CODE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingYearCode() throws Exception {
        // Get the yearCode
        restYearCodeMockMvc.perform(get("/api/year-codes/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateYearCode() throws Exception {
        // Initialize the database
        yearCodeRepository.saveAndFlush(yearCode);
        yearCodeSearchRepository.save(yearCode);
        int databaseSizeBeforeUpdate = yearCodeRepository.findAll().size();

        // Update the yearCode
        YearCode updatedYearCode = yearCodeRepository.findOne(yearCode.getId());
        updatedYearCode
                .yearCode(UPDATED_YEAR_CODE);

        restYearCodeMockMvc.perform(put("/api/year-codes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedYearCode)))
                .andExpect(status().isOk());

        // Validate the YearCode in the database
        List<YearCode> yearCodes = yearCodeRepository.findAll();
        assertThat(yearCodes).hasSize(databaseSizeBeforeUpdate);
        YearCode testYearCode = yearCodes.get(yearCodes.size() - 1);
        assertThat(testYearCode.getYearCode()).isEqualTo(UPDATED_YEAR_CODE);

        // Validate the YearCode in ElasticSearch
        YearCode yearCodeEs = yearCodeSearchRepository.findOne(testYearCode.getId());
        assertThat(yearCodeEs).isEqualToComparingFieldByField(testYearCode);
    }

    @Test
    @Transactional
    public void deleteYearCode() throws Exception {
        // Initialize the database
        yearCodeRepository.saveAndFlush(yearCode);
        yearCodeSearchRepository.save(yearCode);
        int databaseSizeBeforeDelete = yearCodeRepository.findAll().size();

        // Get the yearCode
        restYearCodeMockMvc.perform(delete("/api/year-codes/{id}", yearCode.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean yearCodeExistsInEs = yearCodeSearchRepository.exists(yearCode.getId());
        assertThat(yearCodeExistsInEs).isFalse();

        // Validate the database is empty
        List<YearCode> yearCodes = yearCodeRepository.findAll();
        assertThat(yearCodes).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchYearCode() throws Exception {
        // Initialize the database
        yearCodeRepository.saveAndFlush(yearCode);
        yearCodeSearchRepository.save(yearCode);

        // Search the yearCode
        restYearCodeMockMvc.perform(get("/api/_search/year-codes?query=id:" + yearCode.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(yearCode.getId().intValue())))
            .andExpect(jsonPath("$.[*].yearCode").value(hasItem(DEFAULT_YEAR_CODE.toString())));
    }
}
