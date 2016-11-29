package uk.gov.ofwat.fountain.modelbuilder.web.rest;

import com.codahale.metrics.annotation.Timed;
import uk.gov.ofwat.fountain.modelbuilder.domain.Cell;

import uk.gov.ofwat.fountain.modelbuilder.repository.CellRepository;
import uk.gov.ofwat.fountain.modelbuilder.repository.search.CellSearchRepository;
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
 * REST controller for managing Cell.
 */
@RestController
@RequestMapping("/api")
public class CellResource {

    private final Logger log = LoggerFactory.getLogger(CellResource.class);
        
    @Inject
    private CellRepository cellRepository;

    @Inject
    private CellSearchRepository cellSearchRepository;

    /**
     * POST  /cells : Create a new cell.
     *
     * @param cell the cell to create
     * @return the ResponseEntity with status 201 (Created) and with body the new cell, or with status 400 (Bad Request) if the cell has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/cells")
    @Timed
    public ResponseEntity<Cell> createCell(@Valid @RequestBody Cell cell) throws URISyntaxException {
        log.debug("REST request to save Cell : {}", cell);
        if (cell.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("cell", "idexists", "A new cell cannot already have an ID")).body(null);
        }
        Cell result = cellRepository.save(cell);
        cellSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/cells/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("cell", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /cells : Updates an existing cell.
     *
     * @param cell the cell to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated cell,
     * or with status 400 (Bad Request) if the cell is not valid,
     * or with status 500 (Internal Server Error) if the cell couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/cells")
    @Timed
    public ResponseEntity<Cell> updateCell(@Valid @RequestBody Cell cell) throws URISyntaxException {
        log.debug("REST request to update Cell : {}", cell);
        if (cell.getId() == null) {
            return createCell(cell);
        }
        Cell result = cellRepository.save(cell);
        cellSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("cell", cell.getId().toString()))
            .body(result);
    }

    /**
     * GET  /cells : get all the cells.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of cells in body
     */
    @GetMapping("/cells")
    @Timed
    public List<Cell> getAllCells() {
        log.debug("REST request to get all Cells");
        List<Cell> cells = cellRepository.findAll();
        return cells;
    }

    /**
     * GET  /cells/:id : get the "id" cell.
     *
     * @param id the id of the cell to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the cell, or with status 404 (Not Found)
     */
    @GetMapping("/cells/{id}")
    @Timed
    public ResponseEntity<Cell> getCell(@PathVariable Long id) {
        log.debug("REST request to get Cell : {}", id);
        Cell cell = cellRepository.findOne(id);
        return Optional.ofNullable(cell)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /cells/:id : delete the "id" cell.
     *
     * @param id the id of the cell to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/cells/{id}")
    @Timed
    public ResponseEntity<Void> deleteCell(@PathVariable Long id) {
        log.debug("REST request to delete Cell : {}", id);
        cellRepository.delete(id);
        cellSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("cell", id.toString())).build();
    }

    /**
     * SEARCH  /_search/cells?query=:query : search for the cell corresponding
     * to the query.
     *
     * @param query the query of the cell search 
     * @return the result of the search
     */
    @GetMapping("/_search/cells")
    @Timed
    public List<Cell> searchCells(@RequestParam String query) {
        log.debug("REST request to search Cells for query {}", query);
        return StreamSupport
            .stream(cellSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }


}
