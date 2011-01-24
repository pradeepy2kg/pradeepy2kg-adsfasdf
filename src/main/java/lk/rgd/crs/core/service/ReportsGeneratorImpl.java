package lk.rgd.crs.core.service;

import lk.rgd.ErrorCodes;
import lk.rgd.Permission;
import lk.rgd.common.RGDRuntimeException;
import lk.rgd.common.api.dao.DistrictDAO;
import lk.rgd.common.api.dao.DSDivisionDAO;
import lk.rgd.common.api.dao.RaceDAO;
import lk.rgd.common.api.domain.DSDivision;
import lk.rgd.common.api.domain.District;
import lk.rgd.common.api.domain.Race;
import lk.rgd.common.api.domain.User;
import lk.rgd.common.api.service.UserManager;
import lk.rgd.crs.api.bean.*;
import lk.rgd.crs.api.domain.MarriageInfo;
import lk.rgd.crs.api.service.ReportsGenerator;
import lk.rgd.crs.api.service.BirthRegistrationService;
import lk.rgd.crs.api.domain.BirthDeclaration;
import lk.rgd.crs.web.ReportCodes;
import lk.rgd.prs.api.domain.Marriage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Calendar;
import java.util.Date;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author Ashoka Ekanayaka
 */
public class ReportsGeneratorImpl implements ReportsGenerator {
    private static final Logger logger = LoggerFactory.getLogger(ReportsGeneratorImpl.class);

    private final BirthRegistrationService birthRegister;
    private final DistrictDAO districtDAO;
    private final RaceDAO raceDAO;
    private final DSDivisionDAO dsDivisionDAO;
    private final UserManager userManagementService;
    BirthIslandWideStatistics statistics = BirthIslandWideStatistics.getInstance();
    private int[][] age_race_total;
    private int[][] table_2_4;
    private int[][] age_district_total;
    private int year;

    public ReportsGeneratorImpl(BirthRegistrationService birthRegister, DistrictDAO districtDAO, DSDivisionDAO dsDivisionDAO, RaceDAO raceDAO, UserManager service) {
        this.birthRegister = birthRegister;
        this.districtDAO = districtDAO;
        this.dsDivisionDAO = dsDivisionDAO;
        this.raceDAO = raceDAO;
        this.userManagementService = service;
        age_race_total = new int[BirthMonthlyStatistics.NO_OF_RACES][BirthRaceStatistics.NO_OF_AGE_GROUPS];
        table_2_4 = new int[26][15];
        age_district_total = new int[BirthIslandWideStatistics.NO_OF_DISTRICTS][BirthRaceStatistics.NO_OF_AGE_GROUPS];
    }

    /**
     * Generate a complete statistics object containing whole islandwide
     *
     * @param user
     * @return BirthIslandWideStatistics  @param year
     */
    public BirthIslandWideStatistics generate_2_2(int year, User user) { //TODO - find more efficient way to do this. statistics object is useless if i use this type of method
        this.year = year;
        if (!user.isAuthorized(Permission.GENERATE_REPORTS)) {
            handleException(user.getUserName() + " doesn't have permission to generate the report",
                ErrorCodes.PERMISSION_DENIED);
        }

        List<DSDivision> dsDivisions = dsDivisionDAO.findAll();
        User systemUser = userManagementService.getSystemUser();
        List<BirthDeclaration> birthRecords;

        Calendar cal = Calendar.getInstance();

        /* January first of the year */
        cal.set(year, 0, 1);
        Date startDate = cal.getTime();

        /* December 31st of the year */
        cal.set(year, 11, 31);
        Date endDate = cal.getTime();

        int all = 0, allMales = 0, allFemales = 0;
        for (DSDivision dsDivision : dsDivisions) {
            birthRecords = birthRegister.getByDSDivisionAndStatusAndBirthDateRange(dsDivision, startDate, endDate,
                BirthDeclaration.State.ARCHIVED_CERT_GENERATED, systemUser);   // returns all records so far in this year
            int dsIndex = dsDivision.getDsDivisionUKey(); // todo support tracking at DSDivision level
            int districtIndex = dsDivision.getDistrict().getDistrictUKey();
            BirthDistrictStatistics districtStats = statistics.totals.get(districtIndex);
            int males = 0, females = 0;
            for (BirthDeclaration bd : birthRecords) {
                int gender = bd.getChild().getChildGender();
                if (gender == 0) {
                    males++;
                } else if (gender == 1) {
                    females++;
                }
            }

            int dsDivTotal = birthRecords.size();
            all += dsDivTotal;
            allFemales += females;
            allMales += males;

            districtStats.setTotal(districtStats.getTotal() + dsDivTotal);
            districtStats.setMaleTotal(districtStats.getMaleTotal() + males);
            districtStats.setFemaleTotal(districtStats.getFemaleTotal() + females);

            statistics.totals.set(districtIndex, districtStats);
        }

        statistics.setTotal(statistics.getTotal() + all);
        statistics.setMaleTotal(statistics.getMaleTotal() + allMales);
        statistics.setFemaleTotal(statistics.getFemaleTotal() + allFemales);

        logger.debug("IslandWide Stats generated : total - {}, total males - {}",
            statistics.getTotal(), statistics.getMaleTotal());

        return statistics;

    }

    /**
     * @inheritDoc
     */
    public BirthIslandWideStatistics generate_2_8(int year, User user) { //TODO - find more efficient way to do this. statistics object become useless if i use this type of method
        this.year = year;
        if (!user.isAuthorized(Permission.GENERATE_REPORTS)) {
            handleException(user.getUserName() + " doesn't have permission to generate the report",
                ErrorCodes.PERMISSION_DENIED);
        }

        List<DSDivision> dsDivisions = dsDivisionDAO.findAll();
        User systemUser = userManagementService.getSystemUser();
        List<BirthDeclaration> birthRecords;

        Calendar cal = Calendar.getInstance();

        /* January first of the year */
        cal.set(year, 0, 1);
        Date startDate = cal.getTime();

        /* December 31st of the year */
        cal.set(year, 11, 31);
        Date endDate = cal.getTime();

        int all = 0, allMales = 0, allFemales = 0, allLegBirths = 0, allIllegBirths = 0, allHospitalBirths = 0;
        for (DSDivision dsDivision : dsDivisions) {
            birthRecords = birthRegister.getByDSDivisionAndStatusAndBirthDateRange(dsDivision, startDate, endDate,
                BirthDeclaration.State.ARCHIVED_CERT_GENERATED, systemUser);   // returns all records so far in this year

            int districtIndex = dsDivision.getDistrict().getDistrictUKey();
            BirthDistrictStatistics districtStats = statistics.totals.get(districtIndex);
            int males = 0, females = 0, legBirths = 0, illegBirths = 0, hospitalBirths = 0;
            for (BirthDeclaration bd : birthRecords) {
                int gender = bd.getChild().getChildGender();
                if (gender == 0) {
                    males++;
                } else if (gender == 1) {
                    females++;
                }
                if ((bd.getMarriage().getParentsMarried() == MarriageInfo.MarriedStatus.MARRIED)
                    || bd.getMarriage().getDateOfMarriage().before(bd.getChild().getDateOfBirth())) {
                    legBirths++;
                } else {
                    illegBirths++;
                }
                if (bd.getChild().getBirthAtHospital()) {
                    hospitalBirths++;
                }
            }

            int dsDivTotal = birthRecords.size();
            all += dsDivTotal;
            allFemales += females;
            allMales += males;
            allLegBirths += legBirths;
            allIllegBirths += illegBirths;
            allHospitalBirths += hospitalBirths;

            districtStats.setTotal(districtStats.getTotal() + dsDivTotal);
            districtStats.setMaleTotal(districtStats.getMaleTotal() + males);
            districtStats.setFemaleTotal(districtStats.getFemaleTotal() + females);
            districtStats.setLegitimacyBirths(districtStats.getLegitimacyBirths() + legBirths);
            districtStats.setIllegitimacyBirths(districtStats.getIllegitimacyBirths() + illegBirths);
            districtStats.setHospitalBirths(districtStats.getHospitalBirths() + hospitalBirths);
            float divide = (float) districtStats.getFemaleTotal();
            if (divide == 0) {
                divide = 1;
            }
            districtStats.setProportion((districtStats.getMaleTotal() / divide) * 100.0f);

            statistics.totals.set(districtIndex, districtStats);
        }

        statistics.setTotal(statistics.getTotal() + all);
        statistics.setMaleTotal(statistics.getMaleTotal() + allMales);
        statistics.setFemaleTotal(statistics.getFemaleTotal() + allFemales);
        statistics.setLegitimacyBirths(statistics.getLegitimacyBirths() + allLegBirths);
        statistics.setIllegitimacyBirths(statistics.getIllegitimacyBirths() + allIllegBirths);
        statistics.setHospitalBirths(statistics.getHospitalBirths() + allHospitalBirths);
        float divide = (float) statistics.getFemaleTotal();
        if (divide == 0) {
            divide = 1;
        }
        statistics.setProportion((statistics.getMaleTotal() / divide) * 100.0f);

        return statistics;
    }

    /**
     * @inheritDoc
     */
    public BirthIslandWideStatistics generate_2_5(int year, User user) {  //TODO - find more efficient way to do this. statistics object become useless if i use this type of method
        this.year = year;
        if (!user.isAuthorized(Permission.GENERATE_REPORTS)) {
            handleException(user.getUserName() + " doesn't have permission to generate the report",
                ErrorCodes.PERMISSION_DENIED);
        }

        List<DSDivision> dsDivisions = dsDivisionDAO.findAll();
        User systemUser = userManagementService.getSystemUser();
        List<BirthDeclaration> birthRecords;

        Calendar cal = Calendar.getInstance();

        /* January first of the year */
        cal.set(year, 0, 1);
        Date startDate = cal.getTime();

        /* December 31st of the year */
        cal.set(year, 11, 31);
        Date endDate = cal.getTime();

        for (DSDivision dsDivision : dsDivisions) {
            birthRecords = birthRegister.getByDSDivisionAndStatusAndBirthDateRange(dsDivision, startDate, endDate,
                BirthDeclaration.State.ARCHIVED_CERT_GENERATED, systemUser);

            int districtIndex = dsDivision.getDistrict().getDistrictUKey();
            BirthDistrictStatistics districtStats = statistics.totals.get(districtIndex);

            for (BirthDeclaration bd : birthRecords) {
                Calendar calender = Calendar.getInstance();
                calender.setTime(bd.getChild().getDateOfBirth());
                int month = calender.get(Calendar.MONTH);

                BirthMonthlyStatistics birthMonthlyStatistics = districtStats.monthlyTotals.get(month);
                BirthRaceStatistics birthRaceStatistics = birthMonthlyStatistics.raceTotals.get(bd.getParent().getFatherRace().getRaceId());
                BirthAgeGroupStatistics birthAgeGroupStatistics;
                int age = bd.getParent().getMotherAgeAtBirth() / 5;
                if (age < 3) {
                    birthAgeGroupStatistics = birthRaceStatistics.ageGroupTotals.get(2);
                } else if (age < 11) {
                    birthAgeGroupStatistics = birthRaceStatistics.ageGroupTotals.get(age);
                } else {
                    birthAgeGroupStatistics = birthRaceStatistics.ageGroupTotals.get(10);
                }

                switch (bd.getChild().getChildGender()) {
                    case 0:
                        birthAgeGroupStatistics.setMaleBirths(birthAgeGroupStatistics.getMaleBirths() + 1);
                        birthAgeGroupStatistics.setTotalBirths(birthAgeGroupStatistics.getTotalBirths() + 1);
                        break;
                    case 1:
                        birthAgeGroupStatistics.setFemaleBirths(birthAgeGroupStatistics.getFemaleBirths() + 1);
                        birthAgeGroupStatistics.setTotalBirths(birthAgeGroupStatistics.getTotalBirths() + 1);
                }
                if (age < 3) {
                    birthRaceStatistics.ageGroupTotals.set(2, birthAgeGroupStatistics);
                    birthRaceStatistics.setMaleBirthFromRaces(birthRaceStatistics.getMaleBirthFromRaces() + birthAgeGroupStatistics.getMaleBirths());
                    birthRaceStatistics.setFemaleBirthFromRaces(birthRaceStatistics.getFemaleBirthFromRaces() + birthAgeGroupStatistics.getFemaleBirths());
                    birthRaceStatistics.setTotalBirthFromRaces(birthRaceStatistics.getTotalBirthFromRaces() + birthAgeGroupStatistics.getTotalBirths());
                } else if (age < 11) {
                    birthRaceStatistics.ageGroupTotals.set(age, birthAgeGroupStatistics);
                    birthRaceStatistics.setMaleBirthFromRaces(birthRaceStatistics.getMaleBirthFromRaces() + birthAgeGroupStatistics.getMaleBirths());
                    birthRaceStatistics.setFemaleBirthFromRaces(birthRaceStatistics.getFemaleBirthFromRaces() + birthAgeGroupStatistics.getFemaleBirths());
                    birthRaceStatistics.setTotalBirthFromRaces(birthRaceStatistics.getTotalBirthFromRaces() + birthAgeGroupStatistics.getTotalBirths());
                } else {
                    birthRaceStatistics.ageGroupTotals.set(10, birthAgeGroupStatistics);
                    birthRaceStatistics.setMaleBirthFromRaces(birthRaceStatistics.getMaleBirthFromRaces() + birthAgeGroupStatistics.getMaleBirths());
                    birthRaceStatistics.setFemaleBirthFromRaces(birthRaceStatistics.getFemaleBirthFromRaces() + birthAgeGroupStatistics.getFemaleBirths());
                    birthRaceStatistics.setTotalBirthFromRaces(birthRaceStatistics.getTotalBirthFromRaces() + birthAgeGroupStatistics.getTotalBirths());
                }

                birthMonthlyStatistics.raceTotals.set(bd.getParent().getFatherRace().getRaceId(), birthRaceStatistics);
                birthMonthlyStatistics.setMaleBirthFromMonths(birthMonthlyStatistics.getMaleBirthFromMonths() + birthRaceStatistics.getMaleBirthFromRaces());
                birthMonthlyStatistics.setFemaleBirthFromMonths(birthMonthlyStatistics.getFemaleBirthFromMonths() + birthRaceStatistics.getFemaleBirthFromRaces());
                birthMonthlyStatistics.setTotalBirthFromMonths(birthMonthlyStatistics.getTotalBirthFromMonths() + birthRaceStatistics.getTotalBirthFromRaces());

                districtStats.monthlyTotals.set(month, birthMonthlyStatistics);
                districtStats.setMaleBirthFromDistricts(districtStats.getMaleBirthFromDistricts() + birthMonthlyStatistics.getMaleBirthFromMonths());
                districtStats.setFemaleBirthFromDistricts(districtStats.getFemaleBirthFromDistricts() + birthMonthlyStatistics.getFemaleBirthFromMonths());
                districtStats.setTotalBirthFromDistricts(districtStats.getTotalBirthFromDistricts() + birthMonthlyStatistics.getTotalBirthFromMonths());

            }
            statistics.totals.set(districtIndex, districtStats);
        }

        return statistics;
    }

    /**
     * @inheritDoc
     */
    public BirthIslandWideStatistics generate_2_4(int year, User user) {
        this.year = year;
        if (!user.isAuthorized(Permission.GENERATE_REPORTS)) {
            handleException(user.getUserName() + " doesn't have permission to generate the report",
                ErrorCodes.PERMISSION_DENIED);
        }

        List<DSDivision> dsDivisionList = dsDivisionDAO.findAll();
        User systemUser = userManagementService.getSystemUser();
        List<BirthDeclaration> birthRecords;

        Calendar cal = Calendar.getInstance();

        /* January first of the year */
        cal.set(year, 0, 1);
        Date startDate = cal.getTime();

        /* December 31st of the year */
        cal.set(year, 11, 31);
        Date endDate = cal.getTime();

        for (DSDivision dsDivision : dsDivisionList) {
            birthRecords = birthRegister.getByDSDivisionAndStatusAndBirthDateRange(dsDivision, startDate, endDate,
                BirthDeclaration.State.ARCHIVED_CERT_GENERATED, systemUser);
            int districtId = dsDivision.getDistrictId() - 1;
            for (BirthDeclaration birthDeclaration : birthRecords) {
                int raceId = birthDeclaration.getParent().getMotherRace().getRaceId() - 1;
                table_2_4[districtId][raceId] += 1;
            }

        }

        return statistics;
    }

    /**
     * Creates a Standard CSV file from the generated IslandWide stats.
     * currently assumes. stats are already geneated.
     * // todo check if a CSV file already generated and avaialble for the given year.
     *
     * @param user
     * @param headerCode
     * @return String the path and name of the created CSV file.
     */

    public String createReport(User user, int headerCode) {
        if (!user.isAuthorized(Permission.GENERATE_REPORTS)) {
            handleException(user.getUserName() + " doesn't have permission to create the report",
                ErrorCodes.PERMISSION_DENIED);
        }
        StringBuilder csv = getReportHeader(headerCode);
        String filename = "unknown";

        int length = statistics.totals.size();

        switch (headerCode) {
            case ReportCodes.TABLE_2_2:
                filename = ReportCodes.TABLE_2_2_NAME + ".csv";
                for (int i = 0; i < length; i++) {
                    BirthDistrictStatistics districtStats = statistics.totals.get(i);
                    District district = districtDAO.getDistrict(i + 1);
                    String districtId = "Unknown";
                    if (district != null) {
                        districtId = district.getEnDistrictName();
                    }
                    csv.append(districtId);
                    csv.append(",");
                    csv.append(districtStats.getTotal());
                    csv.append(",");
                    csv.append(districtStats.getMaleTotal());
                    csv.append(",");
                    csv.append(districtStats.getFemaleTotal());
                    csv.append("\n");
                }
                break;
            case ReportCodes.TABLE_2_8:
                filename = ReportCodes.TABLE_2_8_NAME + ".csv";
                for (int i = 0; i < length; i++) {
                    BirthDistrictStatistics districtStats = statistics.totals.get(i);
                    District district = districtDAO.getDistrict(i + 1);
                    String districtId = "Unknown";
                    if (district != null) {
                        districtId = district.getEnDistrictName();
                    }
                    csv.append(districtId);
                    csv.append(",");
                    csv.append(districtStats.getTotal());
                    csv.append(",");
                    csv.append(districtStats.getMaleTotal());
                    csv.append(",");
                    csv.append(districtStats.getFemaleTotal());
                    csv.append(",");
                    csv.append(districtStats.getProportion());
                    csv.append(",");
                    csv.append(districtStats.getLegitimacyBirths());
                    csv.append(",");
                    csv.append(districtStats.getIllegitimacyBirths());
                    csv.append(",");
                    csv.append(districtStats.getHospitalBirths());
                    csv.append("\n");
                }
                break;
            case ReportCodes.TABLE_2_5:
                filename = ReportCodes.TABLE_2_5_NAME + ".csv";
                for (int i = 0; i < BirthMonthlyStatistics.NO_OF_RACES; i++) {
                    Race race = raceDAO.getRace(i + 1);
                    String raceId = "Unknown-Race";
                    if (race != null) {
                        raceId = race.getEnRaceName();
                    }

                    csv.append(raceId);
                    csv.append(",");

                    int total = 0;
                    for (int j = 0; j < BirthRaceStatistics.NO_OF_AGE_GROUPS; j++) {
                        csv.append(age_race_total[i][j]);
                        csv.append(",");
                        total += age_race_total[i][j];
                    }
                    csv.append(total);
                    csv.append("\n");
                }
                break;
            case ReportCodes.TABLE_2_4:
                filename = ReportCodes.TABLE_2_4_NAME + ".csv";
                int total = 0;
                for (int i = 0; i < 26; i++) {
                    District district = districtDAO.getDistrict(i + 1);
                    String districtId = "Unknown";
                    if (district != null) {
                        districtId = district.getEnDistrictName();
                    }
                    csv.append(districtId + ",");
                    total = 0;
                    for (int j = 0; j < 13; j++) {
                        csv.append(table_2_4[i][j] + ",");
                        total += table_2_4[i][j];
                    }
                    csv.append(total + "\n");
                }
                break;
            case ReportCodes.TABLE_2_7:
                filename = ReportCodes.TABLE_2_7_NAME + ".csv";
                for (int i = 0; i < BirthIslandWideStatistics.NO_OF_DISTRICTS; i++) {
                    District district = districtDAO.getDistrict(i);
                    String districtName = "Unknown";
                    if (district != null) {
                        districtName = district.getEnDistrictName();
                    }
                    csv.append(districtName);
                    csv.append(",");

                    int Total = 0;
                    for (int j = 0; j < BirthRaceStatistics.NO_OF_AGE_GROUPS; j++) {
                        csv.append(age_district_total[i][j]);
                        csv.append(",");
                        Total += age_district_total[i][j];
                    }
                    csv.append(Total);
                    csv.append("\n");
                }
                break;
            case ReportCodes.TABLE_2_6:
                filename = ReportCodes.TABLE_2_6_NAME + ".csv";

                List<BirthDistrictStatistics> districtStat = statistics.totals;
                int i = 0;
                for (BirthDistrictStatistics bds : districtStat) {
                    District district = districtDAO.getDistrict(i);
                    String districtName = "Unknown";
                    if (district != null) {
                        districtName = district.getEnDistrictName();
                    }
                    csv.append(districtName);
                    List<BirthMonthlyStatistics> birthMonthlyStat = bds.monthlyTotals;
                    int month_t = 0, month_m = 0, month_f = 0;
                    for (BirthMonthlyStatistics bms : birthMonthlyStat) {
                        csv.append("," + bms.getTotalBirthFromMonths());
                        month_t += bms.getTotalBirthFromMonths();
                        csv.append("," + bms.getMaleBirthFromMonths());
                        month_m += bms.getMaleBirthFromMonths();
                        csv.append("," + bms.getFemaleBirthFromMonths());
                        month_f += bms.getFemaleBirthFromMonths();
                    }
                    csv.append("," + month_t);
                    csv.append("," + month_m);
                    csv.append("," + month_f);
                    csv.append("\n");
                    i++;

                }
                break;

        }

        String dirPath = "reports" + File.separator + year;
        File dir = new File(dirPath);
        dir.mkdirs();

        String filePath = dirPath + File.separator + filename;
        File file = new File(filePath);

        try

        {
            FileOutputStream out = new FileOutputStream(file);
            out.write(csv.toString().getBytes());
            out.close();
        } catch (
            IOException e
            )

        {
            logger.error("Error writing the CSV - {} {}", file.getPath() + file.getName(), e.getMessage());
        }

        return null;
    }

    private void handleException(String message, int code) {
        logger.error(message);
        throw new RGDRuntimeException(message, code);
    }

    private StringBuilder getReportHeader(int code) {
        StringBuilder csv = new StringBuilder();
        //int[][] age_race_male = new int[13][9];     // age 2 - 10 , race 1 - 13
        //int[][] age_race_female = new int[13][9];     // age 2 - 10 , race 1 - 13
        List<BirthDistrictStatistics> districtStat;
        switch (code) {
            case ReportCodes.TABLE_2_2:
                csv.append("District,Total,Male,Female\n");
                csv.append("Sri Lanka,");
                csv.append(statistics.getTotal() + ",");
                csv.append(statistics.getMaleTotal() + ",");
                csv.append(statistics.getFemaleTotal() + ",\n");
                break;
            case ReportCodes.TABLE_2_8:
                csv.append("District,Total,Male,Female,Proportion of male births per 100 female births," +
                    "Legitimacy Births,Illegitimacy Births,Hospital Births\n");
                csv.append("Sri Lanka,");
                csv.append(statistics.getTotal() + ",");
                csv.append(statistics.getMaleTotal() + ",");
                csv.append(statistics.getFemaleTotal() + ",");
                csv.append(statistics.getProportion() + ",");
                csv.append(statistics.getLegitimacyBirths() + ",");
                csv.append(statistics.getIllegitimacyBirths() + ",");
                csv.append(statistics.getHospitalBirths() + ",\n");
                break;
            case ReportCodes.TABLE_2_5:
                csv.append("Race,Less than 15,15-19,20-24,25-29,30-34,35-39,40-44,45-49,50 & above,All Ages\n");
                csv.append("All Race,");

                districtStat = statistics.totals;
                for (BirthDistrictStatistics bds : districtStat) {
                    List<BirthMonthlyStatistics> birthMonthlyStat = bds.monthlyTotals;
                    for (BirthMonthlyStatistics bms : birthMonthlyStat) {
                        int race = 0;
                        List<BirthRaceStatistics> birthRaceStat = bms.raceTotals;
                        for (BirthRaceStatistics brs : birthRaceStat) {
                            int age = 0;
                            List<BirthAgeGroupStatistics> birthAgeGroupStat = brs.ageGroupTotals;
                            for (BirthAgeGroupStatistics bags : birthAgeGroupStat) {
                                //age_race_male[race][age] = age_race_male[race][age] + bags.getMaleBirths();
                                //age_race_female[race][age] = age_race_female[race][age] + bags.getFemaleBirths();
                                age_race_total[race][age] = age_race_total[race][age] + bags.getTotalBirths();
                                age++;
                            }
                            race++;
                        }
                    }
                }
                int total = 0;
                for (int i = 0; i < BirthRaceStatistics.NO_OF_AGE_GROUPS; i++) {
                    csv.append(age_race_total[0][i] + ",");
                    total += age_race_total[0][i];
                }
                csv.append(total + ",");
                csv.append(",\n");
                break;
            case ReportCodes.TABLE_2_4:

                csv.append("District,");
                for (int i = 0; i < 13; i++) {
                    csv.append(raceDAO.getRace(i + 1).getEnRaceName() + ",");
                }
                csv.append("All Ethnic Groups\n");
                csv.append("Sri Lanka,");
                for (int i = 0; i < 13; i++) {
                    total = 0;
                    for (int j = 0; j < 26; j++) {
                        total += table_2_4[j][i];
                    }
                    csv.append(total + ",");
                }
                total = 0;
                for (int i = 0; i < 13; i++) {
                    total += table_2_4[0][i];
                }
                csv.append(total + ",");
                csv.append("\n");

                break;
            case ReportCodes.TABLE_2_7:

                csv.append("District,Less than 15,15-19,20-24,25-29,30-34,35-39,40-44,45-49,50 & above,All Ages\n");
                csv.append("Sri Lanka,");
                int dist = 0;
                districtStat = statistics.totals;
                for (BirthDistrictStatistics bds : districtStat) {
                    List<BirthMonthlyStatistics> birthMonthlyStat = bds.monthlyTotals;
                    for (BirthMonthlyStatistics bms : birthMonthlyStat) {
                        List<BirthRaceStatistics> birthRaceStat = bms.raceTotals;
                        for (BirthRaceStatistics brs : birthRaceStat) {
                            int age = 0;
                            List<BirthAgeGroupStatistics> birthAgeGroupStat = brs.ageGroupTotals;
                            for (BirthAgeGroupStatistics bags : birthAgeGroupStat) {
                                age_district_total[dist][age] = age_district_total[dist][age] + bags.getTotalBirths();
                                age++;
                            }

                        }
                    }
                    dist++;
                }
                int Total = 0;
                for (int i = 0; i < BirthRaceStatistics.NO_OF_AGE_GROUPS; i++) {
                    csv.append(age_district_total[0][i] + ",");
                    Total += age_district_total[0][i];
                }
                csv.append(Total + ",");
                csv.append(",\n");
                break;
            case ReportCodes.TABLE_2_6:
                csv.append(",,January,,,February,,,March,,,April,,,May,,,June,,,July,,,August,,,September,,,October,,,November,,,December,,,Total,,\n");
                csv.append("District,total,male,female,");
                for (int i = 0; i < 12; i++) {
                    csv.append("total,male,female,");
                }
                csv.append("\nSri Lanka,");
                int m[][] = new int[12][3];
                districtStat = statistics.totals;
                int i = 0, j = 0;
                for (BirthDistrictStatistics bds : districtStat) {
                    List<BirthMonthlyStatistics> birthMonthlyStat = bds.monthlyTotals;
                    j = 0;
                    for (BirthMonthlyStatistics bms : birthMonthlyStat) {
                        m[j][0] += bms.getTotalBirthFromMonths();
                        m[j][1] += bms.getMaleBirthFromMonths();
                        m[j][2] += bms.getFemaleBirthFromMonths();
                        j++;
                    }
                    i++;
                }
                int tot = 0, male = 0, female = 0;
                for (i = 0; i < 12; i++) {
                    for (j = 0; j < 3; j++) {
                        csv.append(m[i][j] + ",");
                        switch (j) {
                            case 0:
                                tot += m[i][j];
                                break;
                            case 1:
                                male += m[i][j];
                                break;
                            case 2:
                                female += m[i][j];
                                break;
                        }
                    }
                }
                csv.append(tot + "," + male + "," + female);
                csv.append("\n");

                break;
        }

        return csv;
    }
}