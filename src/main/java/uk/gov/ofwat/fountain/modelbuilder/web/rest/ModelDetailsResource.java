package uk.gov.ofwat.fountain.modelbuilder.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import uk.gov.ofwat.fountain.modelbuilder.domain.ModelDetails;

import uk.gov.ofwat.fountain.modelbuilder.repository.ModelDetailsRepository;
import uk.gov.ofwat.fountain.modelbuilder.repository.search.ModelDetailsSearchRepository;
import uk.gov.ofwat.fountain.modelbuilder.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing ModelDetails.
 */
@RestController
@RequestMapping("/api")
public class ModelDetailsResource {

    private final Logger log = LoggerFactory.getLogger(ModelDetailsResource.class);

    @Inject
    private ModelDetailsRepository modelDetailsRepository;

    @Inject
    private ModelDetailsSearchRepository modelDetailsSearchRepository;

    /**
     * POST  /model-details : Create a new modelDetails.
     *
     * @param modelDetails the modelDetails to create
     * @return the ResponseEntity with status 201 (Created) and with body the new modelDetails, or with status 400 (Bad Request) if the modelDetails has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/model-details")
    @Timed
    public ResponseEntity<ModelDetails> createModelDetails(@Valid @RequestBody ModelDetails modelDetails) throws URISyntaxException {
        log.debug("REST request to save ModelDetails : {}", modelDetails);
        if (modelDetails.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("modelDetails", "idexists", "A new modelDetails cannot already have an ID")).body(null);
        }
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        modelDetails.setCreated(ZonedDateTime.now());
        modelDetails.setCreatedBy(auth.getName());
        ModelDetails result = modelDetailsRepository.save(modelDetails);
        modelDetailsSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/model-details/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("modelDetails", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /model-details : Updates an existing modelDetails.
     *
     * @param modelDetails the modelDetails to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated modelDetails,
     * or with status 400 (Bad Request) if the modelDetails is not valid,
     * or with status 500 (Internal Server Error) if the modelDetails couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/model-details")
    @Timed
    public ResponseEntity<ModelDetails> updateModelDetails(@Valid @RequestBody ModelDetails modelDetails) throws URISyntaxException {
        log.debug("REST request to update ModelDetails : {}", modelDetails);
        if (modelDetails.getId() == null) {
            return createModelDetails(modelDetails);
        }
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        modelDetails.setLastModified(ZonedDateTime.now());
        modelDetails.setLastModifiedBy(auth.getName());
        ModelDetails result = modelDetailsRepository.save(modelDetails);
        modelDetailsSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("modelDetails", modelDetails.getId().toString()))
            .body(result);
    }

    /**
     * GET  /model-details : get all the modelDetails.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of modelDetails in body
     */
    @GetMapping("/model-details")
    @Timed
    public List<ModelDetails> getAllModelDetails() {
        log.debug("REST request to get all ModelDetails");
        List<ModelDetails> modelDetails = modelDetailsRepository.findAll();
        return modelDetails;
    }

    /**
     * GET  /model-details/:id : get the "id" modelDetails.
     *
     * @param id the id of the modelDetails to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the modelDetails, or with status 404 (Not Found)
     */
    @GetMapping("/model-details/{id}")
    @Timed
    public ResponseEntity<ModelDetails> getModelDetails(@PathVariable Long id) {
        log.debug("REST request to get ModelDetails : {}", id);
        ModelDetails modelDetails = modelDetailsRepository.findOne(id);
        return Optional.ofNullable(modelDetails)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /model-details/:id : delete the "id" modelDetails.
     *
     * @param id the id of the modelDetails to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/model-details/{id}")
    @Timed
    public ResponseEntity<Void> deleteModelDetails(@PathVariable Long id) {
        log.debug("REST request to delete ModelDetails : {}", id);
        modelDetailsRepository.delete(id);
        modelDetailsSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("modelDetails", id.toString())).build();
    }

    /**
     * SEARCH  /_search/model-details?query=:query : search for the modelDetails corresponding
     * to the query.
     *
     * @param query the query of the modelDetails search
     * @return the result of the search
     */
    @GetMapping("/_search/model-details")
    @Timed
    public List<ModelDetails> searchModelDetails(@RequestParam String query) {
        log.debug("REST request to search ModelDetails for query {}", query);
        return StreamSupport
            .stream(modelDetailsSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }


}
