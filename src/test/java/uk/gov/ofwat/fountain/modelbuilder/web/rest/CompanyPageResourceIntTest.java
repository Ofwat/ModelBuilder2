package uk.gov.ofwat.fountain.modelbuilder.web.rest;

import uk.gov.ofwat.fountain.modelbuilder.ModelBuilderApp;

import uk.gov.ofwat.fountain.modelbuilder.domain.CompanyPage;
import uk.gov.ofwat.fountain.modelbuilder.repository.CompanyPageRepository;
import uk.gov.ofwat.fountain.modelbuilder.repository.search.CompanyPageSearchRepository;

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
 * Test class for the CompanyPageResource REST controller.
 *
 * @see CompanyPageResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ModelBuilderApp.class)
public class CompanyPageResourceIntTest {

    private static final String DEFAULT_COMPANY_CODE = "AAAAA";
    private static final String UPDATED_COMPANY_CODE = "BBBBB";

    private static final String DEFAULT_PAGE_CODE = "AAAAA";
    private static final String UPDATED_PAGE_CODE = "BBBBB";

    @Inject
    private CompanyPageRepository companyPageRepository;

    @Inject
    private CompanyPageSearchRepository companyPageSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restCompanyPageMockMvc;

    private CompanyPage companyPage;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CompanyPageResource companyPageResource = new CompanyPageResource();
        ReflectionTestUtils.setField(companyPageResource, "companyPageSearchRepository", companyPageSearchRepository);
        ReflectionTestUtils.setField(companyPageResource, "companyPageRepository", companyPageRepository);
        this.restCompanyPageMockMvc = MockMvcBuilders.standaloneSetup(companyPageResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CompanyPage createEntity(EntityManager em) {
        CompanyPage companyPage = new CompanyPage()
                .companyCode(DEFAULT_COMPANY_CODE)
                .pageCode(DEFAULT_PAGE_CODE);
        return companyPage;
    }

    @Before
    public void initTest() {
        companyPageSearchRepository.deleteAll();
        companyPage = createEntity(em);
    }

    @Test
    @Transactional
    public void createCompanyPage() throws Exception {
        int databaseSizeBeforeCreate = companyPageRepository.findAll().size();

        // Create the CompanyPage

        restCompanyPageMockMvc.perform(post("/api/company-pages")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(companyPage)))
                .andExpect(status().isCreated());

        // Validate the CompanyPage in the database
        List<CompanyPage> companyPages = companyPageRepository.findAll();
        assertThat(companyPages).hasSize(databaseSizeBeforeCreate + 1);
        CompanyPage testCompanyPage = companyPages.get(companyPages.size() - 1);
        assertThat(testCompanyPage.getCompanyCode()).isEqualTo(DEFAULT_COMPANY_CODE);
        assertThat(testCompanyPage.getPageCode()).isEqualTo(DEFAULT_PAGE_CODE);

        // Validate the CompanyPage in ElasticSearch
        CompanyPage companyPageEs = companyPageSearchRepository.findOne(testCompanyPage.getId());
        assertThat(companyPageEs).isEqualToComparingFieldByField(testCompanyPage);
    }

    @Test
    @Transactional
    public void checkCompanyCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = companyPageRepository.findAll().size();
        // set the field null
        companyPage.setCompanyCode(null);

        // Create the CompanyPage, which fails.

        restCompanyPageMockMvc.perform(post("/api/company-pages")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(companyPage)))
                .andExpect(status().isBadRequest());

        List<CompanyPage> companyPages = companyPageRepository.findAll();
        assertThat(companyPages).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPageCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = companyPageRepository.findAll().size();
        // set the field null
        companyPage.setPageCode(null);

        // Create the CompanyPage, which fails.

        restCompanyPageMockMvc.perform(post("/api/company-pages")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(companyPage)))
                .andExpect(status().isBadRequest());

        List<CompanyPage> companyPages = companyPageRepository.findAll();
        assertThat(companyPages).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCompanyPages() throws Exception {
        // Initialize the database
        companyPageRepository.saveAndFlush(companyPage);

        // Get all the companyPages
        restCompanyPageMockMvc.perform(get("/api/company-pages?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(companyPage.getId().intValue())))
                .andExpect(jsonPath("$.[*].companyCode").value(hasItem(DEFAULT_COMPANY_CODE.toString())))
                .andExpect(jsonPath("$.[*].pageCode").value(hasItem(DEFAULT_PAGE_CODE.toString())));
    }

    @Test
    @Transactional
    public void getCompanyPage() throws Exception {
        // Initialize the database
        companyPageRepository.saveAndFlush(companyPage);

        // Get the companyPage
        restCompanyPageMockMvc.perform(get("/api/company-pages/{id}", companyPage.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(companyPage.getId().intValue()))
            .andExpect(jsonPath("$.companyCode").value(DEFAULT_COMPANY_CODE.toString()))
            .andExpect(jsonPath("$.pageCode").value(DEFAULT_PAGE_CODE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCompanyPage() throws Exception {
        // Get the companyPage
        restCompanyPageMockMvc.perform(get("/api/company-pages/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCompanyPage() throws Exception {
        // Initialize the database
        companyPageRepository.saveAndFlush(companyPage);
        companyPageSearchRepository.save(companyPage);
        int databaseSizeBeforeUpdate = companyPageRepository.findAll().size();

        // Update the companyPage
        CompanyPage updatedCompanyPage = companyPageRepository.findOne(companyPage.getId());
        updatedCompanyPage
                .companyCode(UPDATED_COMPANY_CODE)
                .pageCode(UPDATED_PAGE_CODE);

        restCompanyPageMockMvc.perform(put("/api/company-pages")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedCompanyPage)))
                .andExpect(status().isOk());

        // Validate the CompanyPage in the database
        List<CompanyPage> companyPages = companyPageRepository.findAll();
        assertThat(companyPages).hasSize(databaseSizeBeforeUpdate);
        CompanyPage testCompanyPage = companyPages.get(companyPages.size() - 1);
        assertThat(testCompanyPage.getCompanyCode()).isEqualTo(UPDATED_COMPANY_CODE);
        assertThat(testCompanyPage.getPageCode()).isEqualTo(UPDATED_PAGE_CODE);

        // Validate the CompanyPage in ElasticSearch
        CompanyPage companyPageEs = companyPageSearchRepository.findOne(testCompanyPage.getId());
        assertThat(companyPageEs).isEqualToComparingFieldByField(testCompanyPage);
    }

    @Test
    @Transactional
    public void deleteCompanyPage() throws Exception {
        // Initialize the database
        companyPageRepository.saveAndFlush(companyPage);
        companyPageSearchRepository.save(companyPage);
        int databaseSizeBeforeDelete = companyPageRepository.findAll().size();

        // Get the companyPage
        restCompanyPageMockMvc.perform(delete("/api/company-pages/{id}", companyPage.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean companyPageExistsInEs = companyPageSearchRepository.exists(companyPage.getId());
        assertThat(companyPageExistsInEs).isFalse();

        // Validate the database is empty
        List<CompanyPage> companyPages = companyPageRepository.findAll();
        assertThat(companyPages).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchCompanyPage() throws Exception {
        // Initialize the database
        companyPageRepository.saveAndFlush(companyPage);
        companyPageSearchRepository.save(companyPage);

        // Search the companyPage
        restCompanyPageMockMvc.perform(get("/api/_search/company-pages?query=id:" + companyPage.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(companyPage.getId().intValue())))
            .andExpect(jsonPath("$.[*].companyCode").value(hasItem(DEFAULT_COMPANY_CODE.toString())))
            .andExpect(jsonPath("$.[*].pageCode").value(hasItem(DEFAULT_PAGE_CODE.toString())));
    }
}
