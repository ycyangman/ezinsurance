package ezinsurance.jpa;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel="productDocuments", path="productDocuments")
public interface ProductDocumentRepository extends PagingAndSortingRepository<ProductDocument, Long>{


}
