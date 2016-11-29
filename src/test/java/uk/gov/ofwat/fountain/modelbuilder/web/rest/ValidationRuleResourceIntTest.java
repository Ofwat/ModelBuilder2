package uk.gov.ofwat.fountain.modelbuilder.web.rest;

import uk.gov.ofwat.fountain.modelbuilder.ModelBuilderApp;

import uk.gov.ofwat.fountain.modelbuilder.domain.ValidationRule;
import uk.gov.ofwat.fountain.modelbuilder.repository.ValidationRuleRepository;
import uk.gov.ofwat.fountain.modelbuilder.repository.search.ValidationRuleSearchRepository;

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
 * Test class for the ValidationRuleResource REST controller.
 *
 * @see ValidationRuleResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ModelBuilderApp.class)
public class ValidationRuleResourceIntTest {

    @Inject
    private ValidationRuleRepository validationRuleRepository;

    @Inject
    private ValidationRuleSearchRepository validationRuleSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restValidationRuleMockMvc;

    private ValidationRule validationRule;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ValidationRuleResource validationRuleResource = new ValidationRuleResource();
        ReflectionTestUtils.setField(validationRuleResource, "validationRuleSearchRepository", validationRuleSearchRepository);
        ReflectionTestUtils.setField(validationRuleResource, "validationRuleRepository", validationRuleRepository);
        this.restValidationRuleMockMvc = MockMvcBuilders.standaloneSetup(validationRuleResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ValidationRule createEntity(EntityManager em) {
        ValidationRule validationRule = new ValidationRule();
        return validationRule;
    }

    @Before
    public void initTest() {
        validationRuleSearchRepository.deleteAll();
        validationRule = createEntity(em);
    }

    @Test
    @Transactional
    public void createValidationRule() throws Exception {
        int databaseSizeBeforeCreate = validationRuleRepository.findAll().size();

        // Create the ValidationRule

        restValidationRuleMockMvc.perform(post("/api/validation-rules")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(validationRule)))
                .andExpect(status().isCreated());

        // Validate the ValidationRule in the database
        List<ValidationRule> validationRules = validationRuleRepository.findAll();
        assertThat(validationRules).hasSize(databaseSizeBeforeCreate + 1);
        ValidationRule testValidationRule = validationRules.get(validationRules.size() - 1);

        // Validate the ValidationRule in ElasticSearch
        ValidationRule validationRuleEs = validationRuleSearchRepository.findOne(testValidationRule.getId());
        assertThat(validationRuleEs).isEqualToComparingFieldByField(testValidationRule);
    }

    @Test
    @Transactional
    public void getAllValidationRules() throws Exception {
        // Initialize the database
        validationRuleRepository.saveAndFlush(validationRule);

        // Get all the validationRules
        restValidationRuleMockMvc.perform(get("/api/validation-rules?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(validationRule.getId().intValue())));
    }

    @Test
    @Transactional
    public void getValidationRule() throws Exception {
        // Initialize the database
        validationRuleRepository.saveAndFlush(validationRule);

        // Get the validationRule
        restValidationRuleMockMvc.perform(get("/api/validation-rules/{id}", validationRule.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(validationRule.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingValidationRule() throws Exception {
        // Get the validationRule
        restValidationRuleMockMvc.perform(get("/api/validation-rules/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateValidationRule() throws Exception {
        // Initialize the database
        validationRuleRepository.saveAndFlush(validationRule);
        validationRuleSearchRepository.save(validationRule);
        int databaseSizeBeforeUpdate = validationRuleRepository.findAll().size();

        // Update the validationRule
        ValidationRule updatedValidationRule = validationRuleRepository.findOne(validationRule.getId());

        restValidationRuleMockMvc.perform(put("/api/validation-rules")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedValidationRule)))
                .andExpect(status().isOk());

        // Validate the ValidationRule in the database
        List<ValidationRule> validationRules = validationRuleRepository.findAll();
        assertThat(validationRules).hasSize(databaseSizeBeforeUpdate);
        ValidationRule testValidationRule = validationRules.get(validationRules.size() - 1);

        // Validate the ValidationRule in ElasticSearch
        ValidationRule validationRuleEs = validationRuleSearchRepository.findOne(testValidationRule.getId());
        assertThat(validationRuleEs).isEqualToComparingFieldByField(testValidationRule);
    }

    @Test
    @Transactional
    public void deleteValidationRule() throws Exception {
        // Initialize the database
        validationRuleRepository.saveAndFlush(validationRule);
        validationRuleSearchRepository.save(validationRule);
        int databaseSizeBeforeDelete = validationRuleRepository.findAll().size();

        // Get the validationRule
        restValidationRuleMockMvc.perform(delete("/api/validation-rules/{id}", validationRule.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean validationRuleExistsInEs = validationRuleSearchRepository.exists(validationRule.getId());
        assertThat(validationRuleExistsInEs).isFalse();

        // Validate the database is empty
        List<ValidationRule> validationRules = validationRuleRepository.findAll();
        assertThat(validationRules).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchValidationRule() throws Exception {
        // Initialize the database
        validationRuleRepository.saveAndFlush(validationRule);
        validationRuleSearchRepository.save(validationRule);

        // Search the validationRule
        restValidationRuleMockMvc.perform(get("/api/_search/validation-rules?query=id:" + validationRule.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(validationRule.getId().intValue())));
    }
}
