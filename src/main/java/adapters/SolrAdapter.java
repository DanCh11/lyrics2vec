package adapters;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;

import java.io.IOException;
import java.util.Collection;

public class SolrAdapter implements AbstractSolrRepository {
    private final String address;

    public SolrAdapter(String address) {
        this.address = address;
    }

    private HttpSolrClient solrClient() {
        return new HttpSolrClient.Builder(address)
                .withConnectionTimeout(10000)
                .build();
    }

    public void indexDocument(Object document) {
        try {
            solrClient().addBean(document);
            solrClient().commit();
        } catch (SolrServerException | IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void indexDocuments(Collection<Object> objects) {
        try {
            solrClient().addBeans(objects);
            solrClient().commit();
        } catch (SolrServerException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public SolrDocument findById(String id) {
        try {
            return solrClient().getById(id);
        } catch (SolrServerException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public SolrDocumentList findByValue(String name, String value) {
        try {
            SolrQuery query = new SolrQuery();
            query.set(name, value);
            QueryResponse response = solrClient().query(query);

            return response.getResults();

        } catch (SolrServerException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteDocumentById(String id) {
        try {
            solrClient().deleteById(id);
            solrClient().commit();

        } catch (SolrServerException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteDocumentByQuery(String query) {
        try {
            solrClient().deleteByQuery(query);
            solrClient().commit();

        } catch (SolrServerException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
