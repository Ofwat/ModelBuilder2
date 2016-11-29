package uk.gov.ofwat.fountain.modelbuilder.web.rest;

import com.codahale.metrics.annotation.Timed;
import uk.gov.ofwat.fountain.modelbuilder.domain.TransferBlockItem;

import uk.gov.ofwat.fountain.modelbuilder.repository.TransferBlockItemRepository;
import uk.gov.ofwat.fountain.modelbuilder.repository.search.TransferBlockItemSearchRepository;
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
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing TransferBlockItem.
 */
@RestController
@RequestMapping("/api")
public class TransferBlockItemResource {

    private final Logger log = LoggerFactory.getLogger(TransferBlockItemResource.class);
        
    @Inject
    private TransferBlockItemRepository transferBlockItemRepository;

    @Inject
    private TransferBlockItemSearchRepository transferBlockItemSearchRepository;

    /**
     * POST  /transfer-block-items : Create a new transferBlockItem.
     *
     * @param transferBlockItem the transferBlockItem to create
     * @return the ResponseEntity with status 201 (Created) and with body the new transferBlockItem, or with status 400 (Bad Request) if the transferBlockItem has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/transfer-block-items")
    @Timed
    public ResponseEntity<TransferBlockItem> createTransferBlockItem(@Valid @RequestBody TransferBlockItem transferBlockItem) throws URISyntaxException {
        log.debug("REST request to save TransferBlockItem : {}", transferBlockItem);
        if (transferBlockItem.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("transferBlockItem", "idexists", "A new transferBlockItem cannot already have an ID")).body(null);
        }
        TransferBlockItem result = transferBlockItemRepository.save(transferBlockItem);
        transferBlockItemSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/transfer-block-items/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("transferBlockItem", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /transfer-block-items : Updates an existing transferBlockItem.
     *
     * @param transferBlockItem the transferBlockItem to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated transferBlockItem,
     * or with status 400 (Bad Request) if the transferBlockItem is not valid,
     * or with status 500 (Internal Server Error) if the transferBlockItem couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/transfer-block-items")
    @Timed
    public ResponseEntity<TransferBlockItem> updateTransferBlockItem(@Valid @RequestBody TransferBlockItem transferBlockItem) throws URISyntaxException {
        log.debug("REST request to update TransferBlockItem : {}", transferBlockItem);
        if (transferBlockItem.getId() == null) {
            return createTransferBlockItem(transferBlockItem);
        }
        TransferBlockItem result = transferBlockItemRepository.save(transferBlockItem);
        transferBlockItemSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("transferBlockItem", transferBlockItem.getId().toString()))
            .body(result);
    }

    /**
     * GET  /transfer-block-items : get all the transferBlockItems.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of transferBlockItems in body
     */
    @GetMapping("/transfer-block-items")
    @Timed
    public List<TransferBlockItem> getAllTransferBlockItems() {
        log.debug("REST request to get all TransferBlockItems");
        List<TransferBlockItem> transferBlockItems = transferBlockItemRepository.findAll();
        return transferBlockItems;
    }

    /**
     * GET  /transfer-block-items/:id : get the "id" transferBlockItem.
     *
     * @param id the id of the transferBlockItem to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the transferBlockItem, or with status 404 (Not Found)
     */
    @GetMapping("/transfer-block-items/{id}")
    @Timed
    public ResponseEntity<TransferBlockItem> getTransferBlockItem(@PathVariable Long id) {
        log.debug("REST request to get TransferBlockItem : {}", id);
        TransferBlockItem transferBlockItem = transferBlockItemRepository.findOne(id);
        return Optional.ofNullable(transferBlockItem)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /transfer-block-items/:id : delete the "id" transferBlockItem.
     *
     * @param id the id of the transferBlockItem to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/transfer-block-items/{id}")
    @Timed
    public ResponseEntity<Void> deleteTransferBlockItem(@PathVariable Long id) {
        log.debug("REST request to delete TransferBlockItem : {}", id);
        transferBlockItemRepository.delete(id);
        transferBlockItemSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("transferBlockItem", id.toString())).build();
    }

    /**
     * SEARCH  /_search/transfer-block-items?query=:query : search for the transferBlockItem corresponding
     * to the query.
     *
     * @param query the query of the transferBlockItem search 
     * @return the result of the search
     */
    @GetMapping("/_search/transfer-block-items")
    @Timed
    public List<TransferBlockItem> searchTransferBlockItems(@RequestParam String query) {
        log.debug("REST request to search TransferBlockItems for query {}", query);
        return StreamSupport
            .stream(transferBlockItemSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }


}
