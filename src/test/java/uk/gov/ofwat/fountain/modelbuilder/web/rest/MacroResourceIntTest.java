package uk.gov.ofwat.fountain.modelbuilder.web.rest;

import uk.gov.ofwat.fountain.modelbuilder.ModelBuilderApp;

import uk.gov.ofwat.fountain.modelbuilder.domain.Macro;
import uk.gov.ofwat.fountain.modelbuilder.repository.MacroRepository;
import uk.gov.ofwat.fountain.modelbuilder.repository.search.MacroSearchRepository;

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
 * Test class for the MacroResource REST controller.
 *
 * @see MacroResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ModelBuilderApp.class)
public class MacroResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";

    private static final String DEFAULT_BLOCK = "AAAAA";
    private static final String UPDATED_BLOCK = "BBBBB";

    private static final String DEFAULT_CONDITIONAL_ITEM_CODE = "AAAAA";
    private static final String UPDATED_CONDITIONAL_ITEM_CODE = "BBBBB";

    private static final String DEFAULT_PAGE_CODE = "AAAAA";
    private static final String UPDATED_PAGE_CODE = "BBBBB";

    @Inject
    private MacroRepository macroRepository;

    @Inject
    private MacroSearchRepository macroSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restMacroMockMvc;

    private Macro macro;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        MacroResource macroResource = new MacroResource();
        ReflectionTestUtils.setField(macroResource, "macroSearchRepository", macroSearchRepository);
        ReflectionTestUtils.setField(macroResource, "macroRepository", macroRepository);
        this.restMacroMockMvc = MockMvcBuilders.standaloneSetup(macroResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Macro createEntity(EntityManager em) {
        Macro macro = new Macro()
                .name(DEFAULT_NAME)
                .description(DEFAULT_DESCRIPTION)
                .block(DEFAULT_BLOCK)
                .conditionalItemCode(DEFAULT_CONDITIONAL_ITEM_CODE)
                .pageCode(DEFAULT_PAGE_CODE);
        return macro;
    }

    @Before
    public void initTest() {
        macroSearchRepository.deleteAll();
        macro = createEntity(em);
    }

    @Test
    @Transactional
    public void createMacro() throws Exception {
        int databaseSizeBeforeCreate = macroRepository.findAll().size();

        // Create the Macro

        restMacroMockMvc.perform(post("/api/macros")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(macro)))
                .andExpect(status().isCreated());

        // Validate the Macro in the database
        List<Macro> macros = macroRepository.findAll();
        assertThat(macros).hasSize(databaseSizeBeforeCreate + 1);
        Macro testMacro = macros.get(macros.size() - 1);
        assertThat(testMacro.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testMacro.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testMacro.getBlock()).isEqualTo(DEFAULT_BLOCK);
        assertThat(testMacro.getConditionalItemCode()).isEqualTo(DEFAULT_CONDITIONAL_ITEM_CODE);
        assertThat(testMacro.getPageCode()).isEqualTo(DEFAULT_PAGE_CODE);

        // Validate the Macro in ElasticSearch
        Macro macroEs = macroSearchRepository.findOne(testMacro.getId());
        assertThat(macroEs).isEqualToComparingFieldByField(testMacro);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = macroRepository.findAll().size();
        // set the field null
        macro.setName(null);

        // Create the Macro, which fails.

        restMacroMockMvc.perform(post("/api/macros")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(macro)))
                .andExpect(status().isBadRequest());

        List<Macro> macros = macroRepository.findAll();
        assertThat(macros).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = macroRepository.findAll().size();
        // set the field null
        macro.setDescription(null);

        // Create the Macro, which fails.

        restMacroMockMvc.perform(post("/api/macros")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(macro)))
                .andExpect(status().isBadRequest());

        List<Macro> macros = macroRepository.findAll();
        assertThat(macros).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkBlockIsRequired() throws Exception {
        int databaseSizeBeforeTest = macroRepository.findAll().size();
        // set the field null
        macro.setBlock(null);

        // Create the Macro, which fails.

        restMacroMockMvc.perform(post("/api/macros")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(macro)))
                .andExpect(status().isBadRequest());

        List<Macro> macros = macroRepository.findAll();
        assertThat(macros).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllMacros() throws Exception {
        // Initialize the database
        macroRepository.saveAndFlush(macro);

        // Get all the macros
        restMacroMockMvc.perform(get("/api/macros?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(macro.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].block").value(hasItem(DEFAULT_BLOCK.toString())))
                .andExpect(jsonPath("$.[*].conditionalItemCode").value(hasItem(DEFAULT_CONDITIONAL_ITEM_CODE.toString())))
                .andExpect(jsonPath("$.[*].pageCode").value(hasItem(DEFAULT_PAGE_CODE.toString())));
    }

    @Test
    @Transactional
    public void getMacro() throws Exception {
        // Initialize the database
        macroRepository.saveAndFlush(macro);

        // Get the macro
        restMacroMockMvc.perform(get("/api/macros/{id}", macro.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(macro.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.block").value(DEFAULT_BLOCK.toString()))
            .andExpect(jsonPath("$.conditionalItemCode").value(DEFAULT_CONDITIONAL_ITEM_CODE.toString()))
            .andExpect(jsonPath("$.pageCode").value(DEFAULT_PAGE_CODE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingMacro() throws Exception {
        // Get the macro
        restMacroMockMvc.perform(get("/api/macros/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMacro() throws Exception {
        // Initialize the database
        macroRepository.saveAndFlush(macro);
        macroSearchRepository.save(macro);
        int databaseSizeBeforeUpdate = macroRepository.findAll().size();

        // Update the macro
        Macro updatedMacro = macroRepository.findOne(macro.getId());
        updatedMacro
                .name(UPDATED_NAME)
                .description(UPDATED_DESCRIPTION)
                .block(UPDATED_BLOCK)
                .conditionalItemCode(UPDATED_CONDITIONAL_ITEM_CODE)
                .pageCode(UPDATED_PAGE_CODE);

        restMacroMockMvc.perform(put("/api/macros")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedMacro)))
                .andExpect(status().isOk());

        // Validate the Macro in the database
        List<Macro> macros = macroRepository.findAll();
        assertThat(macros).hasSize(databaseSizeBeforeUpdate);
        Macro testMacro = macros.get(macros.size() - 1);
        assertThat(testMacro.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testMacro.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testMacro.getBlock()).isEqualTo(UPDATED_BLOCK);
        assertThat(testMacro.getConditionalItemCode()).isEqualTo(UPDATED_CONDITIONAL_ITEM_CODE);
        assertThat(testMacro.getPageCode()).isEqualTo(UPDATED_PAGE_CODE);

        // Validate the Macro in ElasticSearch
        Macro macroEs = macroSearchRepository.findOne(testMacro.getId());
        assertThat(macroEs).isEqualToComparingFieldByField(testMacro);
    }

    @Test
    @Transactional
    public void deleteMacro() throws Exception {
        // Initialize the database
        macroRepository.saveAndFlush(macro);
        macroSearchRepository.save(macro);
        int databaseSizeBeforeDelete = macroRepository.findAll().size();

        // Get the macro
        restMacroMockMvc.perform(delete("/api/macros/{id}", macro.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean macroExistsInEs = macroSearchRepository.exists(macro.getId());
        assertThat(macroExistsInEs).isFalse();

        // Validate the database is empty
        List<Macro> macros = macroRepository.findAll();
        assertThat(macros).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchMacro() throws Exception {
        // Initialize the database
        macroRepository.saveAndFlush(macro);
        macroSearchRepository.save(macro);

        // Search the macro
        restMacroMockMvc.perform(get("/api/_search/macros?query=id:" + macro.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(macro.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].block").value(hasItem(DEFAULT_BLOCK.toString())))
            .andExpect(jsonPath("$.[*].conditionalItemCode").value(hasItem(DEFAULT_CONDITIONAL_ITEM_CODE.toString())))
            .andExpect(jsonPath("$.[*].pageCode").value(hasItem(DEFAULT_PAGE_CODE.toString())));
    }
}
