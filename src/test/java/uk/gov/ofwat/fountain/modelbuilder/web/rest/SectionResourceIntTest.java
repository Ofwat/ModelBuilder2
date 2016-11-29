package uk.gov.ofwat.fountain.modelbuilder.web.rest;

import uk.gov.ofwat.fountain.modelbuilder.ModelBuilderApp;

import uk.gov.ofwat.fountain.modelbuilder.domain.Section;
import uk.gov.ofwat.fountain.modelbuilder.repository.SectionRepository;
import uk.gov.ofwat.fountain.modelbuilder.repository.search.SectionSearchRepository;

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
 * Test class for the SectionResource REST controller.
 *
 * @see SectionResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ModelBuilderApp.class)
public class SectionResourceIntTest {

    @Inject
    private SectionRepository sectionRepository;

    @Inject
    private SectionSearchRepository sectionSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restSectionMockMvc;

    private Section section;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SectionResource sectionResource = new SectionResource();
        ReflectionTestUtils.setField(sectionResource, "sectionSearchRepository", sectionSearchRepository);
        ReflectionTestUtils.setField(sectionResource, "sectionRepository", sectionRepository);
        this.restSectionMockMvc = MockMvcBuilders.standaloneSetup(sectionResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Section createEntity(EntityManager em) {
        Section section = new Section();
        return section;
    }

    @Before
    public void initTest() {
        sectionSearchRepository.deleteAll();
        section = createEntity(em);
    }

    @Test
    @Transactional
    public void createSection() throws Exception {
        int databaseSizeBeforeCreate = sectionRepository.findAll().size();

        // Create the Section

        restSectionMockMvc.perform(post("/api/sections")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(section)))
                .andExpect(status().isCreated());

        // Validate the Section in the database
        List<Section> sections = sectionRepository.findAll();
        assertThat(sections).hasSize(databaseSizeBeforeCreate + 1);
        Section testSection = sections.get(sections.size() - 1);

        // Validate the Section in ElasticSearch
        Section sectionEs = sectionSearchRepository.findOne(testSection.getId());
        assertThat(sectionEs).isEqualToComparingFieldByField(testSection);
    }

    @Test
    @Transactional
    public void getAllSections() throws Exception {
        // Initialize the database
        sectionRepository.saveAndFlush(section);

        // Get all the sections
        restSectionMockMvc.perform(get("/api/sections?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(section.getId().intValue())));
    }

    @Test
    @Transactional
    public void getSection() throws Exception {
        // Initialize the database
        sectionRepository.saveAndFlush(section);

        // Get the section
        restSectionMockMvc.perform(get("/api/sections/{id}", section.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(section.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingSection() throws Exception {
        // Get the section
        restSectionMockMvc.perform(get("/api/sections/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSection() throws Exception {
        // Initialize the database
        sectionRepository.saveAndFlush(section);
        sectionSearchRepository.save(section);
        int databaseSizeBeforeUpdate = sectionRepository.findAll().size();

        // Update the section
        Section updatedSection = sectionRepository.findOne(section.getId());

        restSectionMockMvc.perform(put("/api/sections")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedSection)))
                .andExpect(status().isOk());

        // Validate the Section in the database
        List<Section> sections = sectionRepository.findAll();
        assertThat(sections).hasSize(databaseSizeBeforeUpdate);
        Section testSection = sections.get(sections.size() - 1);

        // Validate the Section in ElasticSearch
        Section sectionEs = sectionSearchRepository.findOne(testSection.getId());
        assertThat(sectionEs).isEqualToComparingFieldByField(testSection);
    }

    @Test
    @Transactional
    public void deleteSection() throws Exception {
        // Initialize the database
        sectionRepository.saveAndFlush(section);
        sectionSearchRepository.save(section);
        int databaseSizeBeforeDelete = sectionRepository.findAll().size();

        // Get the section
        restSectionMockMvc.perform(delete("/api/sections/{id}", section.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean sectionExistsInEs = sectionSearchRepository.exists(section.getId());
        assertThat(sectionExistsInEs).isFalse();

        // Validate the database is empty
        List<Section> sections = sectionRepository.findAll();
        assertThat(sections).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchSection() throws Exception {
        // Initialize the database
        sectionRepository.saveAndFlush(section);
        sectionSearchRepository.save(section);

        // Search the section
        restSectionMockMvc.perform(get("/api/_search/sections?query=id:" + section.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(section.getId().intValue())));
    }
}
