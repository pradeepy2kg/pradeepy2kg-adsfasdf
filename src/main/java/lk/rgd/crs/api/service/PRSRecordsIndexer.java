package lk.rgd.crs.api.service;

/**
 * Define an interface to use Spring to read info within a transaction as Person-citizenship is a lazy load collection
 * @author asankha
 */
public interface PRSRecordsIndexer {

    public boolean indexAll();

}
