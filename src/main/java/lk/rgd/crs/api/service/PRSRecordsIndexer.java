package lk.rgd.crs.api.service;

import lk.rgd.prs.api.domain.Person;
import org.apache.solr.client.solrj.SolrServerException;

import java.io.IOException;

/**
 * Define an interface to use Spring to read info within a transaction as Person-citizenship is a lazy load collection
 * @author asankha
 */
public interface PRSRecordsIndexer {

    public boolean indexAll();

    public boolean deleteIndex();

    public boolean optimizeIndex();

    void updateIndex(Person person);
}
