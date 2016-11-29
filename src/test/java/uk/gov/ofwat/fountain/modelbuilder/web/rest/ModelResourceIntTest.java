package uk.gov.ofwat.fountain.modelbuilder.web.rest;

import uk.gov.ofwat.fountain.modelbuilder.ModelBuilderApp;

import uk.gov.ofwat.fountain.modelbuilder.domain.Model;
import uk.gov.ofwat.fountain.modelbuilder.repository.ModelRepository;
import uk.gov.ofwat.fountain.modelbuilder.repository.search.ModelSearchRepository;

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
 * Test class for the ModelResource REST controller.
 *
 * @see ModelResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ModelBuilderApp.class)
public class ModelResourceIntTest {

    @Inject
    private ModelRepository modelRepository;

    @Inject
    private ModelSearchRepository modelSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restModelMockMvc;

    private Model model;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ModelResource modelResource = new ModelResource();
        ReflectionTestUtils.setField(modelResource, "modelSearchRepository", modelSearchRepository);
        ReflectionTestUtils.setField(modelResource, "modelRepository", modelRepository);
        this.restModelMockMvc = MockMvcBuilders.standaloneSetup(modelResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Model createEntity(EntityManager em) {
        Model model = new Model();
        return model;
    }

    @Before
    public void initTest() {
        modelSearchRepository.deleteAll();
        model = createEntity(em);
    }

    @Test
    @Transactional
    public void createModel() throws Exception {
        int databaseSizeBeforeCreate = modelRepository.findAll().size();

        // Create the Model

        restModelMockMvc.perform(post("/api/models")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(model)))
                .andExpect(status().isCreated());

        // Validate the Model in the database
        List<Model> models = modelRepository.findAll();
        assertThat(models).hasSize(databaseSizeBeforeCreate + 1);
        Model testModel = models.get(models.size() - 1);

        // Validate the Model in ElasticSearch
        Model modelEs = modelSearchRepository.findOne(testModel.getId());
        assertThat(modelEs).isEqualToComparingFieldByField(testModel);
    }

    @Test
    @Transactional
    public void getAllModels() throws Exception {
        // Initialize the database
        modelRepository.saveAndFlush(model);

        // Get all the models
        restModelMockMvc.perform(get("/api/models?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(model.getId().intValue())));
    }

    @Test
    @Transactional
    public void getModel() throws Exception {
        // Initialize the database
        modelRepository.saveAndFlush(model);

        // Get the model
        restModelMockMvc.perform(get("/api/models/{id}", model.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(model.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingModel() throws Exception {
        // Get the model
        restModelMockMvc.perform(get("/api/models/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateModel() throws Exception {
        // Initialize the database
        modelRepository.saveAndFlush(model);
        modelSearchRepository.save(model);
        int databaseSizeBeforeUpdate = modelRepository.findAll().size();

        // Update the model
        Model updatedModel = modelRepository.findOne(model.getId());

        restModelMockMvc.perform(put("/api/models")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedModel)))
                .andExpect(status().isOk());

        // Validate the Model in the database
        List<Model> models = modelRepository.findAll();
        assertThat(models).hasSize(databaseSizeBeforeUpdate);
        Model testModel = models.get(models.size() - 1);

        // Validate the Model in ElasticSearch
        Model modelEs = modelSearchRepository.findOne(testModel.getId());
        assertThat(modelEs).isEqualToComparingFieldByField(testModel);
    }

    @Test
    @Transactional
    public void deleteModel() throws Exception {
        // Initialize the database
        modelRepository.saveAndFlush(model);
        modelSearchRepository.save(model);
        int databaseSizeBeforeDelete = modelRepository.findAll().size();

        // Get the model
        restModelMockMvc.perform(delete("/api/models/{id}", model.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean modelExistsInEs = modelSearchRepository.exists(model.getId());
        assertThat(modelExistsInEs).isFalse();

        // Validate the database is empty
        List<Model> models = modelRepository.findAll();
        assertThat(models).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchModel() throws Exception {
        // Initialize the database
        modelRepository.saveAndFlush(model);
        modelSearchRepository.save(model);

        // Search the model
        restModelMockMvc.perform(get("/api/_search/models?query=id:" + model.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(model.getId().intValue())));
    }
}
