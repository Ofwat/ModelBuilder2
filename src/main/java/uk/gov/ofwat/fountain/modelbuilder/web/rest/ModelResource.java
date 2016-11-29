package uk.gov.ofwat.fountain.modelbuilder.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;
import uk.gov.ofwat.fountain.modelbuilder.domain.Model;

import uk.gov.ofwat.fountain.modelbuilder.repository.ModelDetailsRepository;
import uk.gov.ofwat.fountain.modelbuilder.repository.ModelRepository;
import uk.gov.ofwat.fountain.modelbuilder.repository.search.ModelSearchRepository;
import uk.gov.ofwat.fountain.modelbuilder.service.FountainModelParserService;
import uk.gov.ofwat.fountain.modelbuilder.service.ModelCopyService;
import uk.gov.ofwat.fountain.modelbuilder.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Model.
 */
@RestController
@RequestMapping("/api")
public class ModelResource {

    private final Logger log = LoggerFactory.getLogger(ModelResource.class);

    @Inject
    private ModelCopyService modelCopyService;

    @Inject
    private ModelRepository modelRepository;

    @Inject
    private ModelDetailsRepository modelDetailsRepository;

    @Inject
    private ModelSearchRepository modelSearchRepository;

    @Inject
    private FountainModelParserService fountainModelParserService;

    @PersistenceContext
    EntityManager entityManager;

    /**
     * POST  /models : Create a new model.
     *
     * @param model the model to create
     * @return the ResponseEntity with status 201 (Created) and with body the new model, or with status 400 (Bad Request) if the model has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/models")
    @Timed
    public ResponseEntity<Model> createModel(@RequestBody Model model) throws URISyntaxException {
        log.debug("REST request to save Model : {}", model);
        if (model.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("model", "idexists", "A new model cannot already have an ID")).body(null);
        }
        Model result = modelRepository.save(model);
        modelSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/models/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("model", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /models : Updates an existing model.
     *
     * @param model the model to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated model,
     * or with status 400 (Bad Request) if the model is not valid,
     * or with status 500 (Internal Server Error) if the model couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/models")
    @Timed
    public ResponseEntity<Model> updateModel(@RequestBody Model model) throws URISyntaxException {
        log.debug("REST request to update Model : {}", model);
        if (model.getId() == null) {
            return createModel(model);
        }
        Model result = modelRepository.save(model);
        modelSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("model", model.getId().toString()))
            .body(result);
    }

    /**
     * GET  /models : get all the models.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of models in body
     */
    @GetMapping("/models")
    @Timed
    public List<Model> getAllModels() {
        log.debug("REST request to get all Models");
        List<Model> models = modelRepository.findAll();
        return models;
    }

    /**
     * GET  /models/:id : get the "id" model.
     *
     * @param id the id of the model to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the model, or with status 404 (Not Found)
     */
    @GetMapping("/models/{id}")
    @Timed
    public ResponseEntity<Model> getModel(@PathVariable Long id) {
        log.debug("REST request to get Model : {}", id);
        Model model = modelRepository.findOne(id);
        return Optional.ofNullable(model)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /models/:id : delete the "id" model.
     *
     * @param id the id of the model to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/models/{id}")
    @Timed
    public ResponseEntity<Void> deleteModel(@PathVariable Long id) {
        log.debug("REST request to delete Model : {}", id);
        modelRepository.delete(id);
        modelSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("model", id.toString())).build();
    }

    /**
     * PUT  /copy/:id -> Creates a copy of an existing model.
     */
    @GetMapping("/models/copy/{modelId}")
    @Timed
    public ResponseEntity copyModel(@PathVariable Long modelId, @RequestBody Map map) throws URISyntaxException {
        log.debug("REST request to copy Model : {}", modelId);
        Model existingModel = modelRepository.findOne(modelId);
        if(existingModel != null){
            String newModelName = (String)map.get("name");
            String newModelCode = (String)map.get("code");
            Model newModel = modelCopyService.copyModel(existingModel, newModelName, newModelCode);
            return ResponseEntity.ok().headers(HeaderUtil.createEntityCreationAlert("model", newModel.getId().toString())).body(newModel);
        }else{
            return ResponseEntity.notFound().headers(HeaderUtil.createFailureAlert("model", modelId.toString(), "Unable to clone Model")).build();
        }
    }

    /**
     * UPLOAD A MODEL /models/upload -> Upload and parse a new Model.
     */
    @PostMapping("/models/upload")
    @Timed
    public ResponseEntity uploadModel(@RequestParam("name") String name,
                                             @RequestParam("file") MultipartFile file){
        log.info("Uploading a new model from file " + name);
        if (!file.isEmpty()) {
            String outputFilePath = "/Users/jtoddington/workspace/fountain/workspace/temp/" + name;
            try {
                BufferedOutputStream stream = new BufferedOutputStream(
                    new FileOutputStream(new File(outputFilePath)));
                FileCopyUtils.copy(file.getInputStream(), stream);
                stream.close();

                //Now call the add Model Service!
                File modelXml = new File(outputFilePath);
                log.info("We are going to load model file from: " + modelXml.getAbsolutePath());
                Model model = fountainModelParserService.parseModelFromXmlFile(modelXml);
                return ResponseEntity.status(HttpStatus.CREATED).headers(HeaderUtil.createEntityCreationAlert("model", model.getId().toString())).build();
            }
            catch (Exception e) {
                log.error("Unable to write uploaded model with name " + name + " to path " + outputFilePath);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).headers(HeaderUtil.createEntityDuplicate("Model", "testjt")).build();
            }
        }
        else {
            log.error("Unable to write uploaded model with name " +  name + ". Empty file.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * SEARCH  /_search/models/:query -> search for the model corresponding
     * to the query.
     *
     * @param query the query of the model search
     * @return the result of the search
     */
    @GetMapping("/_search/models")
    @Timed
    public List<Model> searchModels(@RequestParam String query) {
        log.debug("REST request to search Models for query {}", query);
        return StreamSupport
            .stream(modelSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }


}
