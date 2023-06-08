package adapters;
;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;

import java.util.Collection;

public interface AbstractSolrRepository {
    public abstract void indexDocument(Object document);
    public abstract void indexDocuments(Collection<Object> objects);
    public abstract SolrDocument findById(String id);
    public abstract SolrDocumentList findByValue(String name, String value);
    public abstract void deleteDocumentById(String id);
    public abstract void deleteDocumentByQuery(String query);
}
