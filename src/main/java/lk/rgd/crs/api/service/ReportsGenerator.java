package lk.rgd.crs.api.service;

import lk.rgd.crs.api.bean.BirthIslandWideStatistics;

/**
 * Define an interface to use Spring to read info within a transaction as Person-citizenship is a lazy load collection
 * @author Ashoka Ekanayaka
 */
public interface ReportsGenerator {

    public BirthIslandWideStatistics generate();
}