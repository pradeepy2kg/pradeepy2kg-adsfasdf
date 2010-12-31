package lk.rgd.common.core.index;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.CommonsHttpSolrServer;
import org.apache.solr.client.solrj.response.SolrPingResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author asankha
 */
public class SolrIndexManager {

    private static final Logger logger = LoggerFactory.getLogger(SolrIndexManager.class);

    private SolrServer birthServer;
    private SolrServer deathServer;
    private SolrServer prsServer;
    private String solrBirthURL;
    private String solrDeathURL;
    private String solrPRSURL;

    public SolrIndexManager(String solrBirthURL, String solrPRSURL, String solrDeathURL) {
        this.solrBirthURL = solrBirthURL;
        this.solrPRSURL = solrPRSURL;
        this.solrDeathURL = solrDeathURL;
    }

    private synchronized void connect() {

        logger.info("Connecting to Solr indexer services..");

        try {
            birthServer = new CommonsHttpSolrServer(solrBirthURL);
            deathServer = new CommonsHttpSolrServer(solrDeathURL);
            prsServer = new CommonsHttpSolrServer(solrPRSURL);

            SolrPingResponse pingResponse = birthServer.ping();
            if (pingResponse.getStatus() == 0) {
                logger.info("Successfully connected to Birth Registration Solr instance at : " + solrBirthURL);
            }

            pingResponse = deathServer.ping();
            if (pingResponse.getStatus() == 0) {
                logger.info("Successfully connected to Death Registration Solr instance at : " + solrDeathURL);
            }

            pingResponse = prsServer.ping();
            if (pingResponse.getStatus() == 0) {
                logger.info("Successfully connected to Population Registry Solr instance at : " + solrPRSURL);
            }

        } catch (Exception e) {
            logger.error("Failed to connect to : " + solrBirthURL + " or : " + solrDeathURL + " or : " + solrPRSURL +
                " - " + e.getMessage());
            birthServer = null;
            deathServer = null;
            prsServer = null;
        }
    }

    public synchronized SolrServer getBirthServer() {
        if (birthServer == null) {
            connect();
        }
        return birthServer;
    }

    public synchronized SolrServer getDeathServer() {
        if (deathServer == null) {
            connect();
        }
        return deathServer;
    }

    public synchronized SolrServer getPRSServer() {
        if (prsServer == null) {
            connect();
        }
        return prsServer;
    }
}

