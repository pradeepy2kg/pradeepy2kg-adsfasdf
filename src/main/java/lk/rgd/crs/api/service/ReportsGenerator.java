package lk.rgd.crs.api.service;

import lk.rgd.common.api.domain.User;
import lk.rgd.crs.api.bean.BirthIslandWideStatistics;

/**
 * Define an interface to use Spring to read info within a transaction as Person-citizenship is a lazy load collection
 *
 * @author Ashoka Ekanayaka
 */
public interface ReportsGenerator {

    /**
     * Generate a complete statistics object containing whole islandwide stats
     *
     * @return BirthIslandWideStatistics  @param year  @param user
     */
    public BirthIslandWideStatistics generate_2_2(int year, User user, boolean clearCache);

    /**
     * Generate a complete statistics object containing whole islandwide stats
     *
     * @param year
     * @param user
     * @return BirthIslandWideStatistics
     */
    public BirthIslandWideStatistics generate_2_8(int year, User user, boolean clearCache);

    /**
     * Generate a complete statistics object containing whole islandwide stats
     *
     * @param year
     * @param user
     * @return
     */
    public BirthIslandWideStatistics generate_2_5_new(int year, User user, boolean clearCache);

    /**
     * @param year
     * @param user
     * @param clearCache
     * @return
     */
    public BirthIslandWideStatistics generate_2_2a(int year, User user, boolean clearCache);

    /**
     * @param year
     * @param user
     * @param clearCache
     * @return
     */
    public BirthIslandWideStatistics generate_2_7(int year, User user, boolean clearCache);

    /**
     * @param year
     * @param user
     * @param clearCache
     * @return
     */
    public BirthIslandWideStatistics generate_2_6(int year, User user, boolean clearCache);

    /**
     * @param year
     * @param user
     * @return
     */
    public BirthIslandWideStatistics generate_2_3(int year, User user, boolean clearCache);

    /**
     * Generate a complete statistics object containing whole islandwide stats
     *
     * @param year
     * @param user
     * @return
     */
    public BirthIslandWideStatistics generate_2_4(int year, User user, boolean clearCache);

    /**
     * @param year
     * @param user
     * @return
     */
    public BirthIslandWideStatistics generate_2_11(int year, User user, boolean clearCache);

    /**
     * @param year
     * @param user
     * @return
     */
    public BirthIslandWideStatistics generate_2_10(int year, User user, boolean clearCache);

    /**
     * @param year
     * @param user
     * @return
     */
    public BirthIslandWideStatistics generate_2_12(int year, User user, boolean clearCache);

    /**
     * @param year
     * @param user
     * @param clearCache
     */
    public void generateDeathReport(int year, User user, boolean clearCache);

    /**
     * @param year
     * @param user
     * @param clearCache
     */
    public void generateDeathReport2(int year, User user, boolean clearCache);

    /**
     * @param year
     * @param user
     * @param clearCache
     */
    public void populateDeathStatistics(int year, User user, boolean clearCache);

    /**
     * Creates a Standard CSV file from the generated IslandWide stats.
     * currently assumes. stats are already geneated.
     * // todo check if a CSV file already generated and avaialble for the given year.
     *
     * @param headerCode
     * @return String the path and name of the created CSV file.  @param user
     */
    public String createReport(User user, int headerCode);

    /**
     * @param user
     * @param headerCode
     * @return
     */
    public void createDeathReport_all(User user, int headerCode);

    /**
     * @param user
     * @param headerCode
     */
    public void createDeathReport_4_6(User user, int headerCode);

    /**
     * @param user
     * @param headerCode
     */
    public void createDeathReport_4_2(User user, int headerCode);

    /**
     * @param user
     * @param headerCode
     */
    public void createDeathReport_4_4(User user, int headerCode);

    /**
     * @param user
     * @param clearCache
     */
    public void createBirthRawDataTable(int year, User user, boolean clearCache);

    /**
     * @param year
     * @param user
     * @param clearCache
     */
    public void createDeathRawDataTable(int year, User user, boolean clearCache);

}