package uk.gov.ofwat.fountain.modelbuilder.web.rest;

import uk.gov.ofwat.fountain.modelbuilder.ModelBuilderApp;

import uk.gov.ofwat.fountain.modelbuilder.domain.FormDetails;
import uk.gov.ofwat.fountain.modelbuilder.repository.FormDetailsRepository;
import uk.gov.ofwat.fountain.modelbuilder.repository.search.FormDetailsSearchRepository;

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
 * Test class for the FormDetailsResource REST controller.
 *
 * @see FormDetailsResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ModelBuilderApp.class)
public class FormDetailsResourceIntTest {

    private static final String DEFAULT_COMPANY_TYPE = "AAAAA";
    private static final String UPDATED_COMPANY_TYPE = "BBBBB";

    @Inject
    private FormDetailsRepository formDetailsRepository;

    @Inject
    private FormDetailsSearchRepository formDetailsSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restFormDetailsMockMvc;

    private FormDetails formDetails;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        FormDetailsResource formDetailsResource = new FormDetailsResource();
        ReflectionTestUtils.setField(formDetailsResource, "formDetailsSearchRepository", formDetailsSearchRepository);
        ReflectionTestUtils.setField(formDetailsResource, "formDetailsRepository", formDetailsRepository);
        this.restFormDetailsMockMvc = MockMvcBuilders.standaloneSetup(formDetailsResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FormDetails createEntity(EntityManager em) {
        FormDetails formDetails = new FormDetails()
                .companyType(DEFAULT_COMPANY_TYPE);
        return formDetails;
    }

    @Before
    public void initTest() {
        formDetailsSearchRepository.deleteAll();
        formDetails = createEntity(em);
    }

    @Test
    @Transactional
    public void createFormDetails() throws Exception {
        int databaseSizeBeforeCreate = formDetailsRepository.findAll().size();

        // Create the FormDetails

        restFormDetailsMockMvc.perform(post("/api/form-details")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(formDetails)))
                .andExpect(status().isCreated());

        // Validate the FormDetails in the database
        List<FormDetails> formDetails = formDetailsRepository.findAll();
        assertThat(formDetails).hasSize(databaseSizeBeforeCreate + 1);
        FormDetails testFormDetails = formDetails.get(formDetails.size() - 1);
        assertThat(testFormDetails.getCompanyType()).isEqualTo(DEFAULT_COMPANY_TYPE);

        // Validate the FormDetails in ElasticSearch
        FormDetails formDetailsEs = formDetailsSearchRepository.findOne(testFormDetails.getId());
        assertThat(formDetailsEs).isEqualToComparingFieldByField(testFormDetails);
    }

    @Test
    @Transactional
    public void getAllFormDetails() throws Exception {
        // Initialize the database
        formDetailsRepository.saveAndFlush(formDetails);

        // Get all the formDetails
        restFormDetailsMockMvc.perform(get("/api/form-details?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(formDetails.getId().intValue())))
                .andExpect(jsonPath("$.[*].companyType").value(hasItem(DEFAULT_COMPANY_TYPE.toString())));
    }

    @Test
    @Transactional
    public void getFormDetails() throws Exception {
        // Initialize the database
        formDetailsRepository.saveAndFlush(formDetails);

        // Get the formDetails
        restFormDetailsMockMvc.perform(get("/api/form-details/{id}", formDetails.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(formDetails.getId().intValue()))
            .andExpect(jsonPath("$.companyType").value(DEFAULT_COMPANY_TYPE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingFormDetails() throws Exception {
        // Get the formDetails
        restFormDetailsMockMvc.perform(get("/api/form-details/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFormDetails() throws Exception {
        // Initialize the database
        formDetailsRepository.saveAndFlush(formDetails);
        formDetailsSearchRepository.save(formDetails);
        int databaseSizeBeforeUpdate = formDetailsRepository.findAll().size();

        // Update the formDetails
        FormDetails updatedFormDetails = formDetailsRepository.findOne(formDetails.getId());
        updatedFormDetails
                .companyType(UPDATED_COMPANY_TYPE);

        restFormDetailsMockMvc.perform(put("/api/form-details")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedFormDetails)))
                .andExpect(status().isOk());

        // Validate the FormDetails in the database
        List<FormDetails> formDetails = formDetailsRepository.findAll();
        assertThat(formDetails).hasSize(databaseSizeBeforeUpdate);
        FormDetails testFormDetails = formDetails.get(formDetails.size() - 1);
        assertThat(testFormDetails.getCompanyType()).isEqualTo(UPDATED_COMPANY_TYPE);

        // Validate the FormDetails in ElasticSearch
        FormDetails formDetailsEs = formDetailsSearchRepository.findOne(testFormDetails.getId());
        assertThat(formDetailsEs).isEqualToComparingFieldByField(testFormDetails);
    }

    @Test
    @Transactional
    public void deleteFormDetails() throws Exception {
        // Initialize the database
        formDetailsRepository.saveAndFlush(formDetails);
        formDetailsSearchRepository.save(formDetails);
        int databaseSizeBeforeDelete = formDetailsRepository.findAll().size();

        // Get the formDetails
        restFormDetailsMockMvc.perform(delete("/api/form-details/{id}", formDetails.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean formDetailsExistsInEs = formDetailsSearchRepository.exists(formDetails.getId());
        assertThat(formDetailsExistsInEs).isFalse();

        // Validate the database is empty
        List<FormDetails> formDetails = formDetailsRepository.findAll();
        assertThat(formDetails).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchFormDetails() throws Exception {
        // Initialize the database
        formDetailsRepository.saveAndFlush(formDetails);
        formDetailsSearchRepository.save(formDetails);

        // Search the formDetails
        restFormDetailsMockMvc.perform(get("/api/_search/form-details?query=id:" + formDetails.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(formDetails.getId().intValue())))
            .andExpect(jsonPath("$.[*].companyType").value(hasItem(DEFAULT_COMPANY_TYPE.toString())));
    }
}
