package uk.gov.ofwat.fountain.modelbuilder.web.rest;

import uk.gov.ofwat.fountain.modelbuilder.ModelBuilderApp;

import uk.gov.ofwat.fountain.modelbuilder.domain.Line;
import uk.gov.ofwat.fountain.modelbuilder.repository.LineRepository;
import uk.gov.ofwat.fountain.modelbuilder.repository.search.LineSearchRepository;

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
 * Test class for the LineResource REST controller.
 *
 * @see LineResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ModelBuilderApp.class)
public class LineResourceIntTest {

    @Inject
    private LineRepository lineRepository;

    @Inject
    private LineSearchRepository lineSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restLineMockMvc;

    private Line line;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        LineResource lineResource = new LineResource();
        ReflectionTestUtils.setField(lineResource, "lineSearchRepository", lineSearchRepository);
        ReflectionTestUtils.setField(lineResource, "lineRepository", lineRepository);
        this.restLineMockMvc = MockMvcBuilders.standaloneSetup(lineResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Line createEntity(EntityManager em) {
        Line line = new Line();
        return line;
    }

    @Before
    public void initTest() {
        lineSearchRepository.deleteAll();
        line = createEntity(em);
    }

    @Test
    @Transactional
    public void createLine() throws Exception {
        int databaseSizeBeforeCreate = lineRepository.findAll().size();

        // Create the Line

        restLineMockMvc.perform(post("/api/lines")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(line)))
                .andExpect(status().isCreated());

        // Validate the Line in the database
        List<Line> lines = lineRepository.findAll();
        assertThat(lines).hasSize(databaseSizeBeforeCreate + 1);
        Line testLine = lines.get(lines.size() - 1);

        // Validate the Line in ElasticSearch
        Line lineEs = lineSearchRepository.findOne(testLine.getId());
        assertThat(lineEs).isEqualToComparingFieldByField(testLine);
    }

    @Test
    @Transactional
    public void getAllLines() throws Exception {
        // Initialize the database
        lineRepository.saveAndFlush(line);

        // Get all the lines
        restLineMockMvc.perform(get("/api/lines?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(line.getId().intValue())));
    }

    @Test
    @Transactional
    public void getLine() throws Exception {
        // Initialize the database
        lineRepository.saveAndFlush(line);

        // Get the line
        restLineMockMvc.perform(get("/api/lines/{id}", line.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(line.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingLine() throws Exception {
        // Get the line
        restLineMockMvc.perform(get("/api/lines/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLine() throws Exception {
        // Initialize the database
        lineRepository.saveAndFlush(line);
        lineSearchRepository.save(line);
        int databaseSizeBeforeUpdate = lineRepository.findAll().size();

        // Update the line
        Line updatedLine = lineRepository.findOne(line.getId());

        restLineMockMvc.perform(put("/api/lines")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedLine)))
                .andExpect(status().isOk());

        // Validate the Line in the database
        List<Line> lines = lineRepository.findAll();
        assertThat(lines).hasSize(databaseSizeBeforeUpdate);
        Line testLine = lines.get(lines.size() - 1);

        // Validate the Line in ElasticSearch
        Line lineEs = lineSearchRepository.findOne(testLine.getId());
        assertThat(lineEs).isEqualToComparingFieldByField(testLine);
    }

    @Test
    @Transactional
    public void deleteLine() throws Exception {
        // Initialize the database
        lineRepository.saveAndFlush(line);
        lineSearchRepository.save(line);
        int databaseSizeBeforeDelete = lineRepository.findAll().size();

        // Get the line
        restLineMockMvc.perform(delete("/api/lines/{id}", line.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean lineExistsInEs = lineSearchRepository.exists(line.getId());
        assertThat(lineExistsInEs).isFalse();

        // Validate the database is empty
        List<Line> lines = lineRepository.findAll();
        assertThat(lines).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchLine() throws Exception {
        // Initialize the database
        lineRepository.saveAndFlush(line);
        lineSearchRepository.save(line);

        // Search the line
        restLineMockMvc.perform(get("/api/_search/lines?query=id:" + line.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(line.getId().intValue())));
    }
}
