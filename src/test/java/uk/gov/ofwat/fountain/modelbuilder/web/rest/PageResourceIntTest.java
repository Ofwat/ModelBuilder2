package uk.gov.ofwat.fountain.modelbuilder.web.rest;

import uk.gov.ofwat.fountain.modelbuilder.ModelBuilderApp;

import uk.gov.ofwat.fountain.modelbuilder.domain.Page;
import uk.gov.ofwat.fountain.modelbuilder.repository.PageRepository;
import uk.gov.ofwat.fountain.modelbuilder.repository.search.PageSearchRepository;

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
 * Test class for the PageResource REST controller.
 *
 * @see PageResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ModelBuilderApp.class)
public class PageResourceIntTest {

    @Inject
    private PageRepository pageRepository;

    @Inject
    private PageSearchRepository pageSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restPageMockMvc;

    private Page page;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PageResource pageResource = new PageResource();
        ReflectionTestUtils.setField(pageResource, "pageSearchRepository", pageSearchRepository);
        ReflectionTestUtils.setField(pageResource, "pageRepository", pageRepository);
        this.restPageMockMvc = MockMvcBuilders.standaloneSetup(pageResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Page createEntity(EntityManager em) {
        Page page = new Page();
        return page;
    }

    @Before
    public void initTest() {
        pageSearchRepository.deleteAll();
        page = createEntity(em);
    }

    @Test
    @Transactional
    public void createPage() throws Exception {
        int databaseSizeBeforeCreate = pageRepository.findAll().size();

        // Create the Page

        restPageMockMvc.perform(post("/api/pages")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(page)))
                .andExpect(status().isCreated());

        // Validate the Page in the database
        List<Page> pages = pageRepository.findAll();
        assertThat(pages).hasSize(databaseSizeBeforeCreate + 1);
        Page testPage = pages.get(pages.size() - 1);

        // Validate the Page in ElasticSearch
        Page pageEs = pageSearchRepository.findOne(testPage.getId());
        assertThat(pageEs).isEqualToComparingFieldByField(testPage);
    }

    @Test
    @Transactional
    public void getAllPages() throws Exception {
        // Initialize the database
        pageRepository.saveAndFlush(page);

        // Get all the pages
        restPageMockMvc.perform(get("/api/pages?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(page.getId().intValue())));
    }

    @Test
    @Transactional
    public void getPage() throws Exception {
        // Initialize the database
        pageRepository.saveAndFlush(page);

        // Get the page
        restPageMockMvc.perform(get("/api/pages/{id}", page.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(page.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingPage() throws Exception {
        // Get the page
        restPageMockMvc.perform(get("/api/pages/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePage() throws Exception {
        // Initialize the database
        pageRepository.saveAndFlush(page);
        pageSearchRepository.save(page);
        int databaseSizeBeforeUpdate = pageRepository.findAll().size();

        // Update the page
        Page updatedPage = pageRepository.findOne(page.getId());

        restPageMockMvc.perform(put("/api/pages")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedPage)))
                .andExpect(status().isOk());

        // Validate the Page in the database
        List<Page> pages = pageRepository.findAll();
        assertThat(pages).hasSize(databaseSizeBeforeUpdate);
        Page testPage = pages.get(pages.size() - 1);

        // Validate the Page in ElasticSearch
        Page pageEs = pageSearchRepository.findOne(testPage.getId());
        assertThat(pageEs).isEqualToComparingFieldByField(testPage);
    }

    @Test
    @Transactional
    public void deletePage() throws Exception {
        // Initialize the database
        pageRepository.saveAndFlush(page);
        pageSearchRepository.save(page);
        int databaseSizeBeforeDelete = pageRepository.findAll().size();

        // Get the page
        restPageMockMvc.perform(delete("/api/pages/{id}", page.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean pageExistsInEs = pageSearchRepository.exists(page.getId());
        assertThat(pageExistsInEs).isFalse();

        // Validate the database is empty
        List<Page> pages = pageRepository.findAll();
        assertThat(pages).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchPage() throws Exception {
        // Initialize the database
        pageRepository.saveAndFlush(page);
        pageSearchRepository.save(page);

        // Search the page
        restPageMockMvc.perform(get("/api/_search/pages?query=id:" + page.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(page.getId().intValue())));
    }
}
