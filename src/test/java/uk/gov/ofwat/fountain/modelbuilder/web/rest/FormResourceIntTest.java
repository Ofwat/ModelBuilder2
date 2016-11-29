package uk.gov.ofwat.fountain.modelbuilder.web.rest;

import uk.gov.ofwat.fountain.modelbuilder.ModelBuilderApp;

import uk.gov.ofwat.fountain.modelbuilder.domain.Form;
import uk.gov.ofwat.fountain.modelbuilder.repository.FormRepository;
import uk.gov.ofwat.fountain.modelbuilder.repository.search.FormSearchRepository;

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
 * Test class for the FormResource REST controller.
 *
 * @see FormResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ModelBuilderApp.class)
public class FormResourceIntTest {

    @Inject
    private FormRepository formRepository;

    @Inject
    private FormSearchRepository formSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restFormMockMvc;

    private Form form;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        FormResource formResource = new FormResource();
        ReflectionTestUtils.setField(formResource, "formSearchRepository", formSearchRepository);
        ReflectionTestUtils.setField(formResource, "formRepository", formRepository);
        this.restFormMockMvc = MockMvcBuilders.standaloneSetup(formResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Form createEntity(EntityManager em) {
        Form form = new Form();
        return form;
    }

    @Before
    public void initTest() {
        formSearchRepository.deleteAll();
        form = createEntity(em);
    }

    @Test
    @Transactional
    public void createForm() throws Exception {
        int databaseSizeBeforeCreate = formRepository.findAll().size();

        // Create the Form

        restFormMockMvc.perform(post("/api/forms")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(form)))
                .andExpect(status().isCreated());

        // Validate the Form in the database
        List<Form> forms = formRepository.findAll();
        assertThat(forms).hasSize(databaseSizeBeforeCreate + 1);
        Form testForm = forms.get(forms.size() - 1);

        // Validate the Form in ElasticSearch
        Form formEs = formSearchRepository.findOne(testForm.getId());
        assertThat(formEs).isEqualToComparingFieldByField(testForm);
    }

    @Test
    @Transactional
    public void getAllForms() throws Exception {
        // Initialize the database
        formRepository.saveAndFlush(form);

        // Get all the forms
        restFormMockMvc.perform(get("/api/forms?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(form.getId().intValue())));
    }

    @Test
    @Transactional
    public void getForm() throws Exception {
        // Initialize the database
        formRepository.saveAndFlush(form);

        // Get the form
        restFormMockMvc.perform(get("/api/forms/{id}", form.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(form.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingForm() throws Exception {
        // Get the form
        restFormMockMvc.perform(get("/api/forms/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateForm() throws Exception {
        // Initialize the database
        formRepository.saveAndFlush(form);
        formSearchRepository.save(form);
        int databaseSizeBeforeUpdate = formRepository.findAll().size();

        // Update the form
        Form updatedForm = formRepository.findOne(form.getId());

        restFormMockMvc.perform(put("/api/forms")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedForm)))
                .andExpect(status().isOk());

        // Validate the Form in the database
        List<Form> forms = formRepository.findAll();
        assertThat(forms).hasSize(databaseSizeBeforeUpdate);
        Form testForm = forms.get(forms.size() - 1);

        // Validate the Form in ElasticSearch
        Form formEs = formSearchRepository.findOne(testForm.getId());
        assertThat(formEs).isEqualToComparingFieldByField(testForm);
    }

    @Test
    @Transactional
    public void deleteForm() throws Exception {
        // Initialize the database
        formRepository.saveAndFlush(form);
        formSearchRepository.save(form);
        int databaseSizeBeforeDelete = formRepository.findAll().size();

        // Get the form
        restFormMockMvc.perform(delete("/api/forms/{id}", form.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean formExistsInEs = formSearchRepository.exists(form.getId());
        assertThat(formExistsInEs).isFalse();

        // Validate the database is empty
        List<Form> forms = formRepository.findAll();
        assertThat(forms).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchForm() throws Exception {
        // Initialize the database
        formRepository.saveAndFlush(form);
        formSearchRepository.save(form);

        // Search the form
        restFormMockMvc.perform(get("/api/_search/forms?query=id:" + form.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(form.getId().intValue())));
    }
}
