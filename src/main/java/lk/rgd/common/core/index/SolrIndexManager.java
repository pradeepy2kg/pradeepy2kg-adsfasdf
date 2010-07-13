package lk.rgd.common.core.index;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.CommonsHttpSolrServer;
import org.apache.solr.client.solrj.response.SolrPingResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;

/**
 * @author asankha
 */
public class SolrIndexManager {

    private static final Logger logger = LoggerFactory.getLogger(SolrIndexManager.class);

    private SolrServer server;

    public SolrIndexManager(String solrURL) {

        logger.info("Connecting to Solr indexer service at : {}", solrURL);

        try {
            server = new CommonsHttpSolrServer(solrURL);
            SolrPingResponse pingResponse = server.ping();
            if (pingResponse.getStatus() == 0) {
                logger.info("Successfully connected to Solr at : " + solrURL);
            }
        } catch (Exception e) {
            logger.error("Failed to connect to Solr at : " + solrURL + " - " + e.getMessage());
        }
    }

    public SolrServer getServer() {
        return server;
    }
}
