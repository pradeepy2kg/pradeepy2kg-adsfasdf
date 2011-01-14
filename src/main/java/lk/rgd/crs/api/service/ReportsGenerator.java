package lk.rgd.crs.api.service;

import lk.rgd.crs.api.bean.BirthIslandWideStatistics;

/**
 * Define an interface to use Spring to read info within a transaction as Person-citizenship is a lazy load collection
 * @author Ashoka Ekanayaka
 */
public interface ReportsGenerator {

    /**
     *    Generate a complete statistics object containing whole islandwide stats
     * @return  BirthIslandWideStatistics
     */
    public BirthIslandWideStatistics generate();

    /**
     *  Creates a Standard CSV file from the generated IslandWide stats.
     *  currently assumes. stats are already geneated.
     * // todo check if a CSV file already generated and avaialble for the given year.
     * @return String the path and name of the created CSV file.
     */
    public String createReport();
}