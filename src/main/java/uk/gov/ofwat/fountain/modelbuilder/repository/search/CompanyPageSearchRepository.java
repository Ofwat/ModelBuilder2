package uk.gov.ofwat.fountain.modelbuilder.repository.search;

import uk.gov.ofwat.fountain.modelbuilder.domain.CompanyPage;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the CompanyPage entity.
 */
public interface CompanyPageSearchRepository extends ElasticsearchRepository<CompanyPage, Long> {
}
