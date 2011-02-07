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
import lk.rgd.crs.api.domain.*;
import lk.rgd.crs.api.service.DeathRegistrationService;
import lk.rgd.crs.api.service.ReportsGenerator;
import lk.rgd.crs.api.service.BirthRegistrationService;
import lk.rgd.crs.web.ReportCodes;
import lk.rgd.prs.api.domain.Marriage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.NumberFormat;
import java.util.ArrayList;
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
    private final DeathRegistrationService deathRegister;
    private final DistrictDAO districtDAO;
    private final RaceDAO raceDAO;
    private final DSDivisionDAO dsDivisionDAO;
    private final UserManager userManagementService;
    BirthIslandWideStatistics statistics = BirthIslandWideStatistics.getInstance();
    private int[][] age_race_total;
    private int[][] table_2_4;
    private int[][] age_district_total;
    private int[][][] month_race_total;
    private int[][][] sector_leg_total;
    private int[][] table_2_2;
    private int[][][] table_2_6;
    private int[][] table_2_2a;
    int total_arr[];
    private int year;
    private BirthDistrictYearStatistics[] districtYearStatisticsList;
    NumberFormat nf = NumberFormat.getInstance();

    public enum DeathColumn {
        TOTAL,
        MALE,
        FEMALE,
        TOTAL_UNDER_1_YEAR,
        MALE_UNDER_1_YEAR,
        FEMALE_UNDER_1_YEAR,
        TOTAL_UNDER_1_WEEK,
        MALE_UNDER_1_WEEK,
        FEMALE_UNDER_1_WEEK,
        TOTAL_UNDER_1_MONTH,
        MALE_UNDER_1_MONTH,
        FEMALE_UNDER_1_MONTH,
        TOTAL_UNDER_3_MONTH,
        MALE_UNDER_3_MONTH,
        FEMALE_UNDER_3_MONTH,
        TOTAL_UNDER_6_MONTH,
        MALE_UNDER_6_MONTH,
        FEMALE_UNDER_6_MONTH,
        TOTAL_UNDER_9_MONTH,
        MALE_UNDER_9_MONTH,
        FEMALE_UNDER_9_MONTH,
        TOTAL_UNDER_12_MONTH,
        MALE_UNDER_12_MONTH,
        FEMALE_UNDER_12_MONTH,
        SINHALESE_TOTAL,       //24
        SRI_LANKAN_TAMIL_TOTAL,
        INDIAN_TAMIL_TOTAL,
        SRI_LANKAN_MOOR_TOTAL,
        BURGHER_TOTAL,
        MALAY_TOTAL,
        SRI_LANKAN_CHETTY_TOTAL,
        BHARATHA_TOTAL,
        INDIAN_MOOR_TOTAL,
        PAKISTAN_TOTAL,
        OTHER_FOREIGNERS_TOTAL,
        OTHER_SL_TOTAL,
        UNKNOWN_RACE_TOTAL,
        SINHALESE_MALE,         // 37
        SRI_LANKAN_TAMIL_MALE,
        INDIAN_TAMIL_MALE,
        SRI_LANKAN_MOOR_MALE,
        BURGHER_MALE,
        MALAY_MALE,
        SRI_LANKAN_CHETTY_MALE,
        BHARATHA_MALE,
        INDIAN_MOOR_MALE,
        PAKISTAN_MALE,
        OTHER_FOREIGNERS_MALE,
        OTHER_SL_MALE,
        UNKNOWN_RACE_MALE,
        SRI_LANKAN_TAMIL_FEMALE,     //50
        SINHALESE_FEMALE,
        INDIAN_TAMIL_FEMALE,
        SRI_LANKAN_MOOR_FEMALE,
        BURGHER_FEMALE,
        MALAY_FEMALE,
        SRI_LANKAN_CHETTY_FEMALE,
        BHARATHA_FEMALE,
        INDIAN_MOOR_FEMALE,
        PAKISTAN_FEMALE,
        OTHER_FOREIGNERS_FEMALE,
        OTHER_SL_FEMALE,
        UNKNOWN_RACE_FEMALE,
        JANUARY_TOTAL,              //63
        FEBRUARY_TOTAL,
        MARCH_TOTAL,
        APRIL_TOTAL,
        MAY_TOTAL,
        JUNE_TOTAL,
        JULY_TOTAL,
        AUGUST_TOTAL,
        SEPTEMBER_TOTAL,
        OCTOBER_TOTAL,
        NOVEMBER_TOTAL,
        DECEMBER_TOTAL,

        JANUARY_MALE,           //75
        FEBRUARY_MALE,
        MARCH_MALE,
        APRIL_MALE,
        MAY_MALE,
        JUNE_MALE,
        JULY_MALE,
        AUGUST_MALE,
        SEPTEMBER_MALE,
        OCTOBER_MALE,
        NOVEMBER_MALE,
        DECEMBER_MALE,

        JANUARY_FEMALE,             // 87
        FEBRUARY_FEMALE,
        MARCH_FEMALE,
        APRIL_FEMALE,
        MAY_FEMALE,
        JUNE_FEMALE,
        JULY_FEMALE,
        AUGUST_FEMALE,
        SEPTEMBER_FEMALE,
        OCTOBER_FEMALE,
        NOVEMBER_FEMALE,
        DECEMBER_FEMALE
    }

    public enum DeathReport2Column {
        TOTAL_UNDER_1_MONTH,
        MALE_UNDER_1_MONTH,
        FEMALE_UNDER_1_MONTH,
        TOTAL_UNDER_1_WEEK,
        MALE_UNDER_1_WEEK,
        FEMALE_UNDER_1_WEEK,
        TOTAL_1_WEEK_UNDER_1_MONTH,
        MALE_1_WEEK_UNDER_1_MONTH,
        FEMALE_1_WEEK_UNDER_1_MONTH
    }

    public ReportsGeneratorImpl(BirthRegistrationService birthRegister, DeathRegistrationService deathRegister, DistrictDAO districtDAO, DSDivisionDAO dsDivisionDAO, RaceDAO raceDAO, UserManager service) {
        this.birthRegister = birthRegister;
        this.deathRegister = deathRegister;
        this.districtDAO = districtDAO;
        this.dsDivisionDAO = dsDivisionDAO;
        this.raceDAO = raceDAO;
        this.userManagementService = service;
        age_race_total = new int[BirthMonthlyStatistics.NO_OF_RACES][BirthRaceStatistics.NO_OF_AGE_GROUPS];
        table_2_4 = new int[26][15];
        age_district_total = new int[BirthIslandWideStatistics.NO_OF_DISTRICTS][BirthRaceStatistics.NO_OF_AGE_GROUPS];
        month_race_total = new int[BirthDistrictStatistics.NO_OF_MONTHS][BirthMonthlyStatistics.NO_OF_RACES][3];
        sector_leg_total = new int[38][3][3];
        total_arr = new int[9];
        districtYearStatisticsList = new BirthDistrictYearStatistics[26];
        table_2_2 = new int[BirthIslandWideStatistics.NO_OF_DISTRICTS + 1][3];
        table_2_6 = new int[BirthIslandWideStatistics.NO_OF_DISTRICTS][12][3];
        table_2_2a = new int[BirthIslandWideStatistics.NO_OF_DISTRICTS][12];
        nf.setMaximumFractionDigits(2);
    }

    /**
     * @inheritDoc
     */
    public BirthIslandWideStatistics generate_2_2(int year, User user, boolean clearCache) {
        table_2_2 = new int[BirthIslandWideStatistics.NO_OF_DISTRICTS + 1][3];
        this.year = year;
        if (!user.isAuthorized(Permission.GENERATE_REPORTS)) {
            handleException(user.getUserId() + " doesn't have permission to generate the report",
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
                BirthDeclaration.State.ARCHIVED_CERT_PRINTED, systemUser);

            int districtIndex = dsDivision.getDistrict().getDistrictUKey();
            int males = 0, females = 0;
            for (BirthDeclaration bd : birthRecords) {
                if (bd.getChild() != null) {
                    int gender = bd.getChild().getChildGender();
                    if (gender == 0) {
                        males++;
                    } else if (gender == 1) {
                        females++;
                    }
                }
            }

            int dsDivTotal = birthRecords.size();
            all += dsDivTotal;
            allFemales += females;
            allMales += males;

            table_2_2[districtIndex - 1][0] += males;
            table_2_2[districtIndex - 1][1] += females;
            table_2_2[districtIndex - 1][2] += dsDivTotal;
        }

        table_2_2[BirthIslandWideStatistics.NO_OF_DISTRICTS][0] += allMales;
        table_2_2[BirthIslandWideStatistics.NO_OF_DISTRICTS][1] += allFemales;
        table_2_2[BirthIslandWideStatistics.NO_OF_DISTRICTS][2] += all;

        return statistics;

    }

    /**
     * @inheritDoc
     */
    public BirthIslandWideStatistics generate_2_2a(int year, User user, boolean clearCache) {
        table_2_2a = new int[BirthIslandWideStatistics.NO_OF_DISTRICTS][12];
        this.year = year;
        if (!user.isAuthorized(Permission.GENERATE_REPORTS)) {
            handleException(user.getUserId() + " doesn't have permission to generate the report",
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
                BirthDeclaration.State.ARCHIVED_CERT_PRINTED, systemUser);

            int districtIndex = dsDivision.getDistrict().getDistrictUKey() - 1;
            for (BirthDeclaration bd : birthRecords) {
                ChildInfo childInfo = bd.getChild();
                if (childInfo != null) {
                    Date date = childInfo.getDateOfBirth();
                    if (date != null) {
                        int month = date.getMonth();
                        table_2_2a[districtIndex][month] += 1;
                    }
                }
            }
        }
        return statistics;
    }

    /**
     * @inheritDoc
     */
    public BirthIslandWideStatistics generate_2_8(int year, User user, boolean clearCache) {
        this.year = year;
        if (clearCache) {
            statistics.is2_8Populated = false;
        }
        if (statistics.is2_8Populated) {
            return statistics;
        }

        if (!user.isAuthorized(Permission.GENERATE_REPORTS)) {
            handleException(user.getUserId() + " doesn't have permission to generate the report",
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
                BirthDeclaration.State.ARCHIVED_CERT_PRINTED, systemUser);

            int districtIndex = dsDivision.getDistrict().getDistrictUKey();
            BirthDistrictStatistics districtStats = statistics.totals.get(districtIndex);
            int males = 0, females = 0, legBirths = 0, illegBirths = 0, hospitalBirths = 0;
            for (BirthDeclaration bd : birthRecords) {
                ChildInfo cInfo = bd.getChild();
                MarriageInfo mInfo = bd.getMarriage();

                int gender = cInfo.getChildGender();
                if (gender == 0) {
                    males++;
                } else if (gender == 1) {
                    females++;
                }

                if (mInfo != null) {
                    if (mInfo.getParentsMarried() != null && mInfo.getDateOfMarriage() != null && bd.getChild() != null
                        && bd.getChild().getDateOfBirth() != null) {
                        if ((mInfo.getParentsMarried() == MarriageInfo.MarriedStatus.MARRIED)
                            || mInfo.getDateOfMarriage().before(cInfo.getDateOfBirth())) {
                            legBirths++;
                        } else {
                            illegBirths++;
                        }
                    }
                }
                if (cInfo != null) {
                    if (cInfo.getBirthAtHospital()) {
                        hospitalBirths++;
                    }
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
                if (districtStats.getMaleTotal() != 0) {
                    divide = districtStats.getMaleTotal();
                } else {
                    divide = 1;
                }
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
            if (statistics.getMaleTotal() != 0) {
                divide = statistics.getMaleTotal();
            } else {
                divide = 1;
            }
        }
        statistics.setProportion((statistics.getMaleTotal() / divide) * 100.0f);
        statistics.is2_8Populated = true;

        return statistics;
    }

    /**
     * @inheritDoc
     */
    public BirthIslandWideStatistics generate_2_3(int year, User user, boolean clearCache) {
        if (clearCache) {
            statistics.is2_3Populated = false;
        }
        this.year = year;
        if (statistics.is2_3Populated) {
            return statistics;
        }

        if (!user.isAuthorized(Permission.GENERATE_REPORTS)) {
            handleException(user.getUserId() + " doesn't have permission to generate the report",
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
                BirthDeclaration.State.ARCHIVED_CERT_PRINTED, systemUser);

            int districtIndex = dsDivision.getDistrict().getDistrictUKey();
            BirthDistrictStatistics districtStats = statistics.totals.get(districtIndex);

            for (BirthDeclaration bd : birthRecords) {
                ChildInfo childInfo;
                if (bd.getChild() != null && bd.getChild().getChildRank() != null) {
                    childInfo = bd.getChild();
                    int rank = childInfo.getChildRank();
                    int gender = childInfo.getChildGender();

                    for (BirthChildRankStatistics brs : districtStats.birthOrder) {
                        if (brs.getRank() == rank) {
                            int valid = 0;
                            if (gender == 0) {
                                brs.setMaleTotal(brs.getMaleTotal() + 1);
                                valid++;
                            } else {
                                brs.setFemaleTotal(brs.getFemaleTotal() + 1);
                                valid++;
                            }
                            if (valid == 1) {
                                brs.setTotal(brs.getTotal() + 1);
                            }
                        }
                    }
                }
            }
        }

        statistics.is2_3Populated = true;
        return statistics;
    }

    /**
     * @inheritDoc
     */
    public BirthIslandWideStatistics generate_2_5_new(int year, User user, boolean c) { // got
        this.year = year;
        age_race_total = new int[BirthMonthlyStatistics.NO_OF_RACES][BirthRaceStatistics.NO_OF_AGE_GROUPS];
        if (!user.isAuthorized(Permission.GENERATE_REPORTS)) {
            handleException(user.getUserId() + " doesn't have permission to generate the report",
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
                BirthDeclaration.State.ARCHIVED_CERT_PRINTED, systemUser);

            for (BirthDeclaration bd : birthRecords) {
                int age = 0;
                int race = 0;
                if (bd.getParent() != null) {
                    age = bd.getParent().getMotherAgeAtBirth();
                    race = bd.getParent().getFatherRace().getRaceId();
                }
                age = age / 5;
                if (age < 3) {
                    age_race_total[race - 1][0] += 1;
                } else if (age < 11) {
                    age_race_total[race - 1][age - 2] += 1;
                } else {
                    age_race_total[race - 1][BirthRaceStatistics.NO_OF_AGE_GROUPS - 1] += 1;
                }
            }
        }

        return statistics;
    }

    /**
     * @inheritDoc
     */
    public BirthIslandWideStatistics generate_2_7(int year, User user, boolean clearCache) {
        this.year = year;
        age_district_total = new int[BirthIslandWideStatistics.NO_OF_DISTRICTS][BirthRaceStatistics.NO_OF_AGE_GROUPS];

        if (!user.isAuthorized(Permission.GENERATE_REPORTS)) {
            handleException(user.getUserId() + " doesn't have permission to generate the report",
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
                BirthDeclaration.State.ARCHIVED_CERT_PRINTED, systemUser);
            int district = dsDivision.getDistrict().getDistrictUKey();

            for (BirthDeclaration bd : birthRecords) {
                int age = 0;
                if (bd.getParent() != null) {
                    age = bd.getParent().getMotherAgeAtBirth();
                }
                age = age / 5;
                if (age < 3) {
                    age_district_total[district - 1][0] += 1;
                } else if (age < 11) {
                    age_district_total[district - 1][age - 2] += 1;
                } else {
                    age_district_total[district - 1][BirthRaceStatistics.NO_OF_AGE_GROUPS - 1] += 1;
                }
            }
        }
        return statistics;
    }


    /*
    public BirthIslandWideStatistics generate_2_5(int year, User user, boolean clearCache) {  
        this.year = year;
        if (clearCache) {
            statistics.is2_5Populated = false;
        }
        if (statistics.is2_5Populated) {
            return statistics;
        }

        if (!user.isAuthorized(Permission.GENERATE_REPORTS)) {
            handleException(user.getUserId() + " doesn't have permission to generate the report",
                ErrorCodes.PERMISSION_DENIED);
        }

        List<DSDivision> dsDivisions = dsDivisionDAO.findAll();
        User systemUser = userManagementService.getSystemUser();
        List<BirthDeclaration> birthRecords;

        Calendar cal = Calendar.getInstance();

        *//* January first of the year *//*
        cal.set(year, 0, 1);
        Date startDate = cal.getTime();

        *//* December 31st of the year *//*
        cal.set(year, 11, 31);
        Date endDate = cal.getTime();

        for (DSDivision dsDivision : dsDivisions) {
            birthRecords = birthRegister.getByDSDivisionAndStatusAndBirthDateRange(dsDivision, startDate, endDate,
                BirthDeclaration.State.ARCHIVED_CERT_PRINTED, systemUser);

            int districtIndex = dsDivision.getDistrict().getDistrictUKey();
            BirthDistrictStatistics districtStats = statistics.totals.get(districtIndex);

            for (BirthDeclaration bd : birthRecords) {
                //Calendar calender = Calendar.getInstance();
                Date date = bd.getChild().getDateOfBirth();
                int month = date.getMonth();

                BirthMonthlyStatistics birthMonthlyStatistics = districtStats.monthlyTotals.get(month);
                BirthRaceStatistics birthRaceStatistics = birthMonthlyStatistics.raceTotals.get(0);
                ParentInfo pInfo = bd.getParent();
                if (pInfo != null && pInfo.getFatherRace() != null) {
                    birthRaceStatistics = birthMonthlyStatistics.raceTotals.get(pInfo.getFatherRace().getRaceId());
                }
                BirthAgeGroupStatistics birthAgeGroupStatistics;
                int age = 0;
                if (pInfo != null && pInfo.getMotherAgeAtBirth() != null) {
                    age = pInfo.getMotherAgeAtBirth() / 5;
                }
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

                if (bd.getParent() != null && bd.getParent().getFatherRace() != null) {
                    birthMonthlyStatistics.raceTotals.set(bd.getParent().getFatherRace().getRaceId(), birthRaceStatistics);
                }
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
        statistics.is2_5Populated = true;
        return statistics;
    }*/

    /**
     * @inheritDoc
     */
    public BirthIslandWideStatistics generate_2_6(int year, User user, boolean clearCache) {
        table_2_6 = new int[BirthIslandWideStatistics.NO_OF_DISTRICTS][12][3];
        this.year = year;

        if (!user.isAuthorized(Permission.GENERATE_REPORTS)) {
            handleException(user.getUserId() + " doesn't have permission to generate the report",
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
                BirthDeclaration.State.ARCHIVED_CERT_PRINTED, systemUser);
            int districtId = dsDivision.getDistrict().getDistrictUKey() - 1;
            for (BirthDeclaration birthDeclaration : birthRecords) {
                ParentInfo parentInfo = birthDeclaration.getParent();
                ChildInfo childInfo = birthDeclaration.getChild();
                int month = 12;
                int gender = 0;
                if (parentInfo != null && childInfo != null) {
                    month = childInfo.getDateOfBirth().getMonth();
                    gender = childInfo.getChildGender();

                    table_2_6[districtId][month][gender] += 1;
                    table_2_6[districtId][month][2] += 1;
                }
            }
        }

        return statistics;
    }

    /**
     * @inheritDoc
     */
    public BirthIslandWideStatistics generate_2_4(int year, User user, boolean clearCache) {
        table_2_4 = new int[26][15];
        this.year = year;
        if (!user.isAuthorized(Permission.GENERATE_REPORTS)) {
            handleException(user.getUserId() + " doesn't have permission to generate the report",
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
                BirthDeclaration.State.ARCHIVED_CERT_PRINTED, systemUser);
            int districtId = dsDivision.getDistrict().getDistrictUKey() - 1;
            for (BirthDeclaration birthDeclaration : birthRecords) {
                int raceId = 0;
                try {
                    if (birthDeclaration.getParent() != null && birthDeclaration.getParent().getMotherRace() != null) {
                        raceId = birthDeclaration.getParent().getMotherRace().getRaceId() - 1;
                    }
                } catch (NullPointerException e) {
                    raceId = 0;
                }
                table_2_4[districtId][raceId] += 1;
            }
        }

        return statistics;
    }

    /**
     * @inheritDoc
     */
    public BirthIslandWideStatistics generate_2_11(int year, User user, boolean clearCache) {
        month_race_total = new int[BirthDistrictStatistics.NO_OF_MONTHS][BirthMonthlyStatistics.NO_OF_RACES][3];
        this.year = year;
        if (!user.isAuthorized(Permission.GENERATE_REPORTS)) {
            handleException(user.getUserId() + " doesn't have permission to generate the report",
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
                BirthDeclaration.State.ARCHIVED_CERT_PRINTED, systemUser);
            for (BirthDeclaration birthDeclaration : birthRecords) {
                ChildInfo childInfo = birthDeclaration.getChild();
                ParentInfo parentInfo = birthDeclaration.getParent();
                int monthId = childInfo.getDateOfBirth().getMonth();

                if (childInfo != null && parentInfo != null) {
                    int raceId = 13;
                    if (parentInfo.getFatherRace() != null) {
                        raceId = parentInfo.getFatherRace().getRaceId();
                    }
                    int gender = childInfo.getChildGender();
                    if (gender == 0 || gender == 1) {
                        month_race_total[monthId][raceId - 1][gender] += 1;
                        month_race_total[monthId][raceId - 1][2] += 1;
                    }
                }
            }
        }
        return statistics;
    }

    /**
     * @inheritDoc
     */
    public BirthIslandWideStatistics generate_2_10(int year, User user, boolean clearCache) {
        sector_leg_total = new int[38][3][3];
        this.year = year;
        if (!user.isAuthorized(Permission.GENERATE_REPORTS)) {
            handleException(user.getUserId() + " doesn't have permission to generate the report",
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
                BirthDeclaration.State.ARCHIVED_CERT_PRINTED, systemUser);
            for (BirthDeclaration birthDeclaration : birthRecords) {
                int ageId = 0;
                ChildInfo childInfo = birthDeclaration.getChild();
                ParentInfo parentInfo = birthDeclaration.getParent();
                MarriageInfo mInfo = birthDeclaration.getMarriage();

                if (childInfo != null && parentInfo != null) {
                    try {
                        ageId = parentInfo.getMotherAgeAtBirth();
                    } catch (Exception e) {
                        ageId = 0;
                    }
                    if (ageId > 50) {
                        ageId = 50;
                    } else if (ageId >= 13) {
                        ageId -= 13;
                    }

                    if (mInfo != null) {
                        if (mInfo.getParentsMarried() != null && mInfo.getDateOfMarriage() != null && childInfo.getDateOfBirth() != null) {
                            if ((mInfo.getParentsMarried() == MarriageInfo.MarriedStatus.MARRIED)
                                || mInfo.getDateOfMarriage().before(childInfo.getDateOfBirth())) {
                                if (childInfo.getChildGender() == 0) {
                                    sector_leg_total[ageId][0][0] += 1;     // 0 - legitimacy    // 1 - illegitimacy
                                    sector_leg_total[ageId][0][2] += 1;     // 0 - male           // 1 - female
                                    sector_leg_total[ageId][2][2] += 1;                     // 2 - total
                                } else if (childInfo.getChildGender() == 1) {
                                    sector_leg_total[ageId][0][1] += 1;
                                    sector_leg_total[ageId][0][2] += 1;
                                    sector_leg_total[ageId][2][2] += 1;
                                }
                            } else {
                                if (childInfo.getChildGender() == 0) {
                                    sector_leg_total[ageId][1][0] += 1;
                                    sector_leg_total[ageId][1][2] += 1;
                                    sector_leg_total[ageId][2][2] += 1;
                                } else if (childInfo.getChildGender() == 1) {
                                    sector_leg_total[ageId][1][1] += 1;
                                    sector_leg_total[ageId][1][2] += 1;
                                    sector_leg_total[ageId][2][2] += 1;
                                }
                            }
                        } else if (mInfo.getParentsMarried() == null || mInfo.getDateOfMarriage() == null) {
                            if (childInfo.getChildGender() == 0) {
                                sector_leg_total[ageId][1][0] += 1;
                                sector_leg_total[ageId][1][2] += 1;
                                sector_leg_total[ageId][2][2] += 1;
                            } else if (childInfo.getChildGender() == 1) {
                                sector_leg_total[ageId][1][1] += 1;
                                sector_leg_total[ageId][1][2] += 1;
                                sector_leg_total[ageId][2][2] += 1;
                            }
                        }
                    }
                }
            }
        }

        return statistics;
    }

    /**
     * @inheritDoc
     */
    public BirthIslandWideStatistics generate_2_12(int year, User user, boolean clearCache) {
        districtYearStatisticsList = new BirthDistrictYearStatistics[26];
        for (int k = 0; k < districtYearStatisticsList.length; k++) {
            districtYearStatisticsList[k] = new BirthDistrictYearStatistics();
        }
        this.year = year;
        if (!user.isAuthorized(Permission.GENERATE_REPORTS)) {
            handleException(user.getUserId() + " doesn't have permission to generate the report",
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
                BirthDeclaration.State.ARCHIVED_CERT_PRINTED, systemUser);
            int districtIndex = dsDivision.getDistrict().getDistrictUKey() - 1;
            for (BirthDeclaration birthDeclaration : birthRecords) {
                ChildInfo childInfo = birthDeclaration.getChild();
                if (childInfo != null) {
                    int gender = childInfo.getChildGender();
                    Date date = childInfo.getDateOfBirth();
                    int month = 0;
                    int birthYear = 0;
                    int lastYear = 0;
                    if (date != null) {
                        cal.setTime(date);
                        month = cal.get(Calendar.MONTH);
                        birthYear = cal.get(Calendar.YEAR);
                        lastYear = year - 1;
                    }
                    int a = 0, b = 0, c = 0;
                    if (birthYear == year) { // during this year
                        districtYearStatisticsList[districtIndex].during_this_year_month_array[month][gender] += 1;
                        districtYearStatisticsList[districtIndex].during_this_year_month_array[month][2] += 1;
                        districtYearStatisticsList[districtIndex].total_year_month_array[month][gender] += districtYearStatisticsList[districtIndex].during_this_year_month_array[month][gender];
                        districtYearStatisticsList[districtIndex].total_year_month_array[month][2] += districtYearStatisticsList[districtIndex].during_this_year_month_array[month][2];
                    } else if (birthYear == lastYear) { // during last year
                        districtYearStatisticsList[districtIndex].during_last_year_month_array[month][gender] += 1;
                        districtYearStatisticsList[districtIndex].during_last_year_month_array[month][2] += 1;
                        districtYearStatisticsList[districtIndex].total_year_month_array[month][gender] += districtYearStatisticsList[districtIndex].during_last_year_month_array[month][gender];
                        districtYearStatisticsList[districtIndex].total_year_month_array[month][2] += districtYearStatisticsList[districtIndex].during_last_year_month_array[month][2];
                    } else if (birthYear < lastYear) { // before last year
                        districtYearStatisticsList[districtIndex].before_last_year_month_array[month][gender] += 1;
                        districtYearStatisticsList[districtIndex].before_last_year_month_array[month][2] += 1;
                        districtYearStatisticsList[districtIndex].total_year_month_array[month][gender] += districtYearStatisticsList[districtIndex].before_last_year_month_array[month][gender];
                        districtYearStatisticsList[districtIndex].total_year_month_array[month][2] += districtYearStatisticsList[districtIndex].before_last_year_month_array[month][2];
                    }
                }
            }
        }

        return statistics;
    }

    public void generateDeathReport2(int year, User user, boolean clearCache) {

        this.year = year;
        if (!user.isAuthorized(Permission.GENERATE_REPORTS)) {
            handleException(user.getUserId() + " doesn't have permission to generate the report",
                ErrorCodes.PERMISSION_DENIED);
        }

        List<DSDivision> dsDivisionList = dsDivisionDAO.findAll();
        User systemUser = userManagementService.getSystemUser();
        List<DeathRegister> deathRecords;
        int array[][][] = new int[BirthIslandWideStatistics.NO_OF_DISTRICTS][BirthMonthlyStatistics.NO_OF_RACES][DeathReport2Column.values().length];

        Calendar cal = Calendar.getInstance();

        /* January first of the year */
        cal.set(year, 0, 1);
        Date startDate = cal.getTime();

        /* December 31st of the year */
        cal.set(year, 11, 31);
        Date endDate = cal.getTime();

        StringBuilder csv = new StringBuilder();
        String filename = "DeathReports2.csv";

        for (DSDivision dsDivision : dsDivisionList) {
            deathRecords = deathRegister.getByDSDivisionAndStatusAndRegistrationDateRange(
                dsDivision, startDate, endDate, DeathRegister.State.ARCHIVED_CERT_GENERATED, user);

            District district = dsDivision.getDistrict();
            for (DeathRegister deathRegister : deathRecords) {
                if (deathRegister.getDeathPerson() != null) {
                    Date birthDate = deathRegister.getDeathPerson().getDeathPersonDOB();
                    Date deathDate = deathRegister.getDeath().getDateOfDeath();
                    cal.setTime(deathDate);
                    cal.add(Calendar.DATE, -7);
                    Date beforeSevenDaysFromDeath = cal.getTime();
                    cal.setTime(deathDate);
                    cal.add(Calendar.MONTH, -1);
                    Date beforeOneMonthFromDeath = cal.getTime();
                    int gender = deathRegister.getDeathPerson().getDeathPersonGender();
                    Race race = deathRegister.getDeathPerson().getDeathPersonRace();
                    int districtId = district.getDistrictUKey();
                    int raceId = 13;
                    if (race != null) {
                        raceId = race.getRaceId();
                    }

                    if (birthDate.after(beforeSevenDaysFromDeath)) {
                        if(gender == 0) {
                            array[districtId - 1][raceId - 1][DeathReport2Column.MALE_UNDER_1_WEEK.ordinal()] += 1;
                            array[districtId - 1][raceId - 1][DeathReport2Column.TOTAL_UNDER_1_WEEK.ordinal()] += 1;

                            array[districtId - 1][raceId - 1][DeathReport2Column.MALE_UNDER_1_MONTH.ordinal()] += 1;
                            array[districtId - 1][raceId - 1][DeathReport2Column.TOTAL_UNDER_1_MONTH.ordinal()] += 1;
                        } else if (gender == 1) {
                            array[districtId - 1][raceId - 1][DeathReport2Column.FEMALE_UNDER_1_WEEK.ordinal()] += 1;
                            array[districtId - 1][raceId - 1][DeathReport2Column.TOTAL_UNDER_1_WEEK.ordinal()] += 1;

                            array[districtId - 1][raceId - 1][DeathReport2Column.FEMALE_UNDER_1_MONTH.ordinal()] += 1;
                            array[districtId - 1][raceId - 1][DeathReport2Column.TOTAL_UNDER_1_MONTH.ordinal()] += 1;
                        }
                    } else if (birthDate.after(beforeOneMonthFromDeath)) {
                        if(gender == 0) {
                            array[districtId - 1][raceId - 1][DeathReport2Column.MALE_1_WEEK_UNDER_1_MONTH.ordinal()] += 1;
                            array[districtId - 1][raceId - 1][DeathReport2Column.TOTAL_1_WEEK_UNDER_1_MONTH.ordinal()] += 1;

                            array[districtId - 1][raceId - 1][DeathReport2Column.MALE_UNDER_1_MONTH.ordinal()] += 1;
                            array[districtId - 1][raceId - 1][DeathReport2Column.TOTAL_UNDER_1_MONTH.ordinal()] += 1;
                        } else if (gender == 1) {
                            array[districtId - 1][raceId - 1][DeathReport2Column.FEMALE_1_WEEK_UNDER_1_MONTH.ordinal()] += 1;
                            array[districtId - 1][raceId - 1][DeathReport2Column.TOTAL_1_WEEK_UNDER_1_MONTH.ordinal()] += 1;

                            array[districtId - 1][raceId - 1][DeathReport2Column.FEMALE_UNDER_1_MONTH.ordinal()] += 1;
                            array[districtId - 1][raceId - 1][DeathReport2Column.TOTAL_UNDER_1_MONTH.ordinal()] += 1;
                        }
                    }
                }
            }
        }

        csv.append("DISTRICT,TOTAL UNDER 1 MONTH - TOTAL,TOTAL UNDER 1 MONTH - MALE,TOTAL UNDER 1 MONTH - FEMALE," +
            "1 WEEK AND UNDER - TOTAL,1 WEEK AND UNDER - MALE,1 WEEK AND UNDER - FEMALE," +
            "1 WEEK AND UNDER 1 MONTH - TOTAL,1 WEEK AND UNDER 1 MONTH - MALE,1 WEEK AND UNDER 1 MONTH - FEMALE\n");

        for (int i = 0; i < BirthIslandWideStatistics.NO_OF_DISTRICTS; i++) {
            String districtName = "UNKNOWN DISTRICT";
            District district = districtDAO.getDistrict(i + 1);
            if (district != null) {
                districtName = district.getEnDistrictName();
            }
            csv.append(districtName + "\n");

            for (int j = 0; j < BirthMonthlyStatistics.NO_OF_RACES; j++) {
                String raceName = "UNKNOWN RACE";
                Race race = raceDAO.getRace(j + 1);
                if (race != null) {
                    raceName = race.getEnRaceName();
                }
                csv.append(raceName + ",");
                csv.append(array[i][j][DeathReport2Column.TOTAL_UNDER_1_MONTH.ordinal()] + "," +
                        array[i][j][DeathReport2Column.MALE_UNDER_1_MONTH.ordinal()] + "," +
                        array[i][j][DeathReport2Column.FEMALE_UNDER_1_MONTH.ordinal()] + "," +
                        array[i][j][DeathReport2Column.TOTAL_UNDER_1_WEEK.ordinal()] + "," +
                        array[i][j][DeathReport2Column.MALE_UNDER_1_WEEK.ordinal()] + "," +
                        array[i][j][DeathReport2Column.FEMALE_UNDER_1_WEEK.ordinal()] + "," +
                        array[i][j][DeathReport2Column.TOTAL_1_WEEK_UNDER_1_MONTH.ordinal()] + "," +
                        array[i][j][DeathReport2Column.MALE_1_WEEK_UNDER_1_MONTH.ordinal()] + "," +
                        array[i][j][DeathReport2Column.FEMALE_1_WEEK_UNDER_1_MONTH.ordinal()] + ",\n" 
                );
            }
        }

        String dirPath = "reports" + File.separator + year;
        File dir = new File(dirPath);
        dir.mkdirs();

        String filePath = dirPath + File.separator + filename;
        File file = new File(filePath);

        try {
            FileOutputStream out = new FileOutputStream(file);
            out.write(csv.toString().getBytes());
            out.close();
        } catch (IOException e) {
            logger.error("Error writing the CSV - {} {}", file.getPath() + file.getName(), e.getMessage());
        }

    }

    public void generateDeathReport(int year, User user, boolean clearCache) {

        this.year = year;
        if (!user.isAuthorized(Permission.GENERATE_REPORTS)) {
            handleException(user.getUserId() + " doesn't have permission to generate the report",
                ErrorCodes.PERMISSION_DENIED);
        }

        List<DSDivision> dsDivisionList = dsDivisionDAO.findAll();
        User systemUser = userManagementService.getSystemUser();
        List<DeathRegister> deathRecords;

        Calendar cal = Calendar.getInstance();

        /* January first of the year */
        cal.set(year, 0, 1);
        Date startDate = cal.getTime();

        /* December 31st of the year */
        cal.set(year, 11, 31);
        Date endDate = cal.getTime();

        StringBuilder csv = new StringBuilder();
        String filename = "DeathReports.csv";
        int array[][] = new int[BirthIslandWideStatistics.NO_OF_DISTRICTS][DeathColumn.values().length];
        csv.append("DISTRICT,TOTAL,MALE,FEMALE," +
            "TOTAL UNDER 1 YEAR,MALE UNDER 1 YEAR,FEMALE UNDER 1 YEAR," +
            "TOTAL 1 WEEK & UNDER 1 WEEK,MALE 1 WEEK & UNDER 1 WEEK,FEMALE 1 WEEK & UNDER 1 WEEK," +
            "TOTAL 1 WEEK & UNDER 1 MONTH,MALE 1 WEEK & UNDER 1 MONTH,FEMALE 1 WEEK & UNDER 1 MONTH," +
            "TOTAL 1 MONTH & UNDER 3 MONTH,MALE 1 MONTH & UNDER 3 MONTH,FEMALE 1 MONTH & UNDER 3 MONTH," +
            "TOTAL 3 MONTH & UNDER 6 MONTH,MALE 3 MONTH & UNDER 6 MONTH,FEMALE 3 MONTH & UNDER 6 MONTH," +
            "TOTAL 6 MONTH & UNDER 9 MONTH,MALE 6 MONTH & UNDER 9 MONTH,FEMALE 6 MONTH & UNDER 9 MONTH," +
            "TOTAL 9 MONTH & UNDER 1 YEAR,MALE 9 MONTH & UNDER 1 YEAR,FEMALE 9 MONTH & UNDER 1 YEAR,");
        for (int i = 0; i < BirthMonthlyStatistics.NO_OF_RACES; i++) {
            String raceName = "UNKNOWN RACE";
            Race race = raceDAO.getRace(i + 1);
            if (race != null) {
                raceName = race.getEnRaceName().toUpperCase();
            }
            csv.append(raceName + " TOTAL," + raceName + " MALE," + raceName + " FEMALE,");
        }
        csv.append("JANUARY_TOTAL,JANUARY_MALE,JANUARY_FEMALE," +
            "FEBRUARY_TOTAL,FEBRUARY_MALE,FEBRUARY_FEMALE," +
            "MARCH_TOTAL,MARCH_MALE,MARCH_FEMALE," +
            "APRIL_TOTAL,APRIL_MALE,APRIL_FEMALE," +
            "MAY_TOTAL,MAY_MALE,MAY_FEMALE," +
            "JUNE_TOTAL,JUNE_MALE,JUNE_FEMALE," +
            "JULY_TOTAL,JULY_MALE,JULY_FEMALE," +
            "AUGUST_TOTAL,AUGUST_MALE,AUGUST_FEMALE," +
            "SEPTEMBER_TOTAL,SEPTEMBER_MALE,SEPTEMBER_FEMALE," +
            "OCTOBER_TOTAL,OCTOBER_MALE,OCTOBER_FEMALE," +
            "NOVEMBER_TOTAL,NOVEMBER_MALE,NOVEMBER_FEMALE," +
            "DECEMBER_TOTAL,DECEMBER_MALE,DECEMBER_FEMALE,");
        csv.append("\n");

        for (DSDivision dsDivision : dsDivisionList) {
            deathRecords = deathRegister.getByDSDivisionAndStatusAndRegistrationDateRange(
                dsDivision, startDate, endDate, DeathRegister.State.ARCHIVED_CERT_GENERATED, user);

            District district = dsDivision.getDistrict();
            for (DeathRegister deathRegister : deathRecords) {
                if (deathRegister.getDeathPerson() != null) {
                    // ---------- TOTAL, MALE, FEMALE ---------- //
                    array[district.getDistrictUKey() - 1][deathRegister.getDeathPerson().getDeathPersonGender()] += 1;
                    array[district.getDistrictUKey() - 1][DeathColumn.TOTAL.ordinal()] += 1;

                    // -------- UNDER 1 WEEK / 1 MONTH --------- //
                    Date birthDate = deathRegister.getDeathPerson().getDeathPersonDOB();
                    Date deathDate = deathRegister.getDeath().getDateOfDeath();
                    cal.setTime(deathDate);
                    cal.add(Calendar.DATE, -7);
                    Date beforeSevenDaysFromDeath = cal.getTime();
                    cal.setTime(deathDate);
                    cal.add(Calendar.MONTH, -1);
                    Date beforeOneMonthFromDeath = cal.getTime();
                    cal.setTime(deathDate);
                    cal.add(Calendar.MONTH, -3);
                    Date beforeThreeMonthFromDeath = cal.getTime();
                    cal.setTime(deathDate);
                    cal.add(Calendar.MONTH, -6);
                    Date beforeSixMonthFromDeath = cal.getTime();
                    cal.setTime(deathDate);
                    cal.add(Calendar.MONTH, -9);
                    Date beforeNineMonthFromDeath = cal.getTime();
                    cal.setTime(deathDate);
                    cal.add(Calendar.YEAR, -1);
                    Date beforeTwelveMonthFromDeath = cal.getTime();

                    if (birthDate != null && deathDate != null) {
                        if (birthDate.after(beforeSevenDaysFromDeath)) {
                            int gender = deathRegister.getDeathPerson().getDeathPersonGender();
                            if (gender == 0) { // male
                                array[district.getDistrictUKey() - 1][DeathColumn.TOTAL_UNDER_1_WEEK.ordinal()] += 1;  // increment week total
                                array[district.getDistrictUKey() - 1][DeathColumn.MALE_UNDER_1_WEEK.ordinal()] += 1;

                                array[district.getDistrictUKey() - 1][DeathColumn.TOTAL_UNDER_1_YEAR.ordinal()] += 1;  // Year Totals...
                                array[district.getDistrictUKey() - 1][DeathColumn.MALE_UNDER_1_YEAR.ordinal()] += 1;
                            } else if (gender == 1) { // female
                                array[district.getDistrictUKey() - 1][DeathColumn.TOTAL_UNDER_1_WEEK.ordinal()] += 1;  // increment week total
                                array[district.getDistrictUKey() - 1][DeathColumn.FEMALE_UNDER_1_WEEK.ordinal()] += 1;

                                array[district.getDistrictUKey() - 1][DeathColumn.TOTAL_UNDER_1_YEAR.ordinal()] += 1;  // Year Totals...
                                array[district.getDistrictUKey() - 1][DeathColumn.FEMALE_UNDER_1_YEAR.ordinal()] += 1;
                            }
                        } // week totals completes...
                        else if (birthDate.after(beforeOneMonthFromDeath)) {
                            int gender = deathRegister.getDeathPerson().getDeathPersonGender();
                            if (gender == 0) { // male
                                array[district.getDistrictUKey() - 1][DeathColumn.TOTAL_UNDER_1_MONTH.ordinal()] += 1;  // increment month total
                                array[district.getDistrictUKey() - 1][DeathColumn.MALE_UNDER_1_MONTH.ordinal()] += 1;

                                array[district.getDistrictUKey() - 1][DeathColumn.TOTAL_UNDER_1_YEAR.ordinal()] += 1;  // Year Totals...
                                array[district.getDistrictUKey() - 1][DeathColumn.MALE_UNDER_1_YEAR.ordinal()] += 1;
                            } else if (gender == 1) { // female
                                array[district.getDistrictUKey() - 1][DeathColumn.TOTAL_UNDER_1_MONTH.ordinal()] += 1;  // increment month total
                                array[district.getDistrictUKey() - 1][DeathColumn.FEMALE_UNDER_1_MONTH.ordinal()] += 1;

                                array[district.getDistrictUKey() - 1][DeathColumn.TOTAL_UNDER_1_YEAR.ordinal()] += 1;  // Year Totals...
                                array[district.getDistrictUKey() - 1][DeathColumn.FEMALE_UNDER_1_YEAR.ordinal()] += 1;
                            }
                        } // month totals completes...
                        else if (birthDate.after(beforeThreeMonthFromDeath)) {
                            int gender = deathRegister.getDeathPerson().getDeathPersonGender();
                            if (gender == 0) {
                                array[district.getDistrictUKey() - 1][DeathColumn.TOTAL_UNDER_3_MONTH.ordinal()] += 1;  // increment 3 month total
                                array[district.getDistrictUKey() - 1][DeathColumn.MALE_UNDER_3_MONTH.ordinal()] += 1;

                                array[district.getDistrictUKey() - 1][DeathColumn.TOTAL_UNDER_1_YEAR.ordinal()] += 1;  // Year Totals...
                                array[district.getDistrictUKey() - 1][DeathColumn.MALE_UNDER_1_YEAR.ordinal()] += 1;
                            } else if (gender == 1) {
                                array[district.getDistrictUKey() - 1][DeathColumn.TOTAL_UNDER_3_MONTH.ordinal()] += 1;  // increment 3 month total
                                array[district.getDistrictUKey() - 1][DeathColumn.FEMALE_UNDER_3_MONTH.ordinal()] += 1;

                                array[district.getDistrictUKey() - 1][DeathColumn.TOTAL_UNDER_1_YEAR.ordinal()] += 1;  // Year Totals...
                                array[district.getDistrictUKey() - 1][DeathColumn.FEMALE_UNDER_1_YEAR.ordinal()] += 1;
                            }
                        } // 3 months totals completes...
                        else if (birthDate.after(beforeSixMonthFromDeath)) {
                            int gender = deathRegister.getDeathPerson().getDeathPersonGender();
                            if (gender == 0) {
                                array[district.getDistrictUKey() - 1][DeathColumn.TOTAL_UNDER_6_MONTH.ordinal()] += 1;  // increment 6 month total
                                array[district.getDistrictUKey() - 1][DeathColumn.MALE_UNDER_6_MONTH.ordinal()] += 1;

                                array[district.getDistrictUKey() - 1][DeathColumn.TOTAL_UNDER_1_YEAR.ordinal()] += 1;  // Year Totals...
                                array[district.getDistrictUKey() - 1][DeathColumn.MALE_UNDER_1_YEAR.ordinal()] += 1;
                            } else if (gender == 1) {
                                array[district.getDistrictUKey() - 1][DeathColumn.TOTAL_UNDER_6_MONTH.ordinal()] += 1;  // increment 6 month total
                                array[district.getDistrictUKey() - 1][DeathColumn.FEMALE_UNDER_6_MONTH.ordinal()] += 1;

                                array[district.getDistrictUKey() - 1][DeathColumn.TOTAL_UNDER_1_YEAR.ordinal()] += 1;  // Year Totals...
                                array[district.getDistrictUKey() - 1][DeathColumn.FEMALE_UNDER_1_YEAR.ordinal()] += 1;
                            }
                        } // 6 months totals completes...
                        else if (birthDate.after(beforeNineMonthFromDeath)) {
                            int gender = deathRegister.getDeathPerson().getDeathPersonGender();
                            if (gender == 0) {
                                array[district.getDistrictUKey() - 1][DeathColumn.TOTAL_UNDER_9_MONTH.ordinal()] += 1;  // increment 9 month total
                                array[district.getDistrictUKey() - 1][DeathColumn.MALE_UNDER_9_MONTH.ordinal()] += 1;

                                array[district.getDistrictUKey() - 1][DeathColumn.TOTAL_UNDER_1_YEAR.ordinal()] += 1;  // Year Totals...
                                array[district.getDistrictUKey() - 1][DeathColumn.MALE_UNDER_1_YEAR.ordinal()] += 1;
                            } else if (gender == 1) {
                                array[district.getDistrictUKey() - 1][DeathColumn.TOTAL_UNDER_9_MONTH.ordinal()] += 1;  // increment 9 month total
                                array[district.getDistrictUKey() - 1][DeathColumn.FEMALE_UNDER_9_MONTH.ordinal()] += 1;

                                array[district.getDistrictUKey() - 1][DeathColumn.TOTAL_UNDER_1_YEAR.ordinal()] += 1;  // Year Totals...
                                array[district.getDistrictUKey() - 1][DeathColumn.FEMALE_UNDER_1_YEAR.ordinal()] += 1;
                            }
                        } // 9 months totals completes...
                        else if (birthDate.after(beforeTwelveMonthFromDeath)) {
                            int gender = deathRegister.getDeathPerson().getDeathPersonGender();
                            if (gender == 0) {
                                array[district.getDistrictUKey() - 1][DeathColumn.TOTAL_UNDER_12_MONTH.ordinal()] += 1;  // increment 12 month total
                                array[district.getDistrictUKey() - 1][DeathColumn.MALE_UNDER_12_MONTH.ordinal()] += 1;

                                array[district.getDistrictUKey() - 1][DeathColumn.TOTAL_UNDER_1_YEAR.ordinal()] += 1;  // Year Totals...
                                array[district.getDistrictUKey() - 1][DeathColumn.MALE_UNDER_1_YEAR.ordinal()] += 1;
                            } else if (gender == 1) {
                                array[district.getDistrictUKey() - 1][DeathColumn.TOTAL_UNDER_12_MONTH.ordinal()] += 1;  // increment 12 month total
                                array[district.getDistrictUKey() - 1][DeathColumn.FEMALE_UNDER_12_MONTH.ordinal()] += 1;

                                array[district.getDistrictUKey() - 1][DeathColumn.TOTAL_UNDER_1_YEAR.ordinal()] += 1;  // Year Totals...
                                array[district.getDistrictUKey() - 1][DeathColumn.FEMALE_UNDER_1_YEAR.ordinal()] += 1;
                            }
                        } // 12 months totals completes...
                    }
                    Race race = deathRegister.getDeathPerson().getDeathPersonRace();
                    int gender = deathRegister.getDeathPerson().getDeathPersonGender();
                    boolean raceUnknown = true;
                    if (race != null) {
                        raceUnknown = false;
                        int race_t = race.getRaceId() + 23;
                        int race_m = race_t + 13;
                        int race_f = race_m + 13;

                        array[district.getDistrictUKey() - 1][race_t] += 1;
                        if (gender == 0) {
                            array[district.getDistrictUKey() - 1][race_m] += 1;
                        } else if (gender == 1) {
                            array[district.getDistrictUKey() - 1][race_f] += 1;
                        }
                    }
                    if (raceUnknown) {
                        array[district.getDistrictUKey() - 1][DeathColumn.UNKNOWN_RACE_TOTAL.ordinal()] += 1;
                        if (gender == 0) {
                            array[district.getDistrictUKey() - 1][DeathColumn.UNKNOWN_RACE_MALE.ordinal()] += 1;
                        } else if (gender == 1) {
                            array[district.getDistrictUKey() - 1][DeathColumn.UNKNOWN_RACE_FEMALE.ordinal()] += 1;
                        }
                    }
                    // races completes...

                    Date date = deathRegister.getDeath().getDateOfDeath();
                    if (date != null) {
                        int month_t = date.getMonth() + 63;
                        int month_m = month_t + 12;
                        int month_f = month_m + 12;

                        array[district.getDistrictUKey() - 1][month_t] += 1;
                        if (gender == 0) {
                            array[district.getDistrictUKey() - 1][month_m] += 1;
                        } else if (gender == 1) {
                            array[district.getDistrictUKey() - 1][month_f] += 1;
                        }
                    }
                }
            }
        }

        for (int i = 0; i < BirthIslandWideStatistics.NO_OF_DISTRICTS; i++) {
            String districtName = "Unknown";
            if (districtDAO.getDistrict(i + 1) != null) {
                districtName = districtDAO.getDistrict(i + 1).getEnDistrictName();
            }
            csv.append(districtName + "," + array[i][DeathColumn.TOTAL.ordinal()] + "," + array[i][DeathColumn.MALE.ordinal()] + "," + array[i][DeathColumn.FEMALE.ordinal()] +
                "," + array[i][DeathColumn.TOTAL_UNDER_1_YEAR.ordinal()] + "," + array[i][DeathColumn.MALE_UNDER_1_YEAR.ordinal()] + "," + array[i][DeathColumn.FEMALE_UNDER_1_YEAR.ordinal()] +
                "," + array[i][DeathColumn.TOTAL_UNDER_1_WEEK.ordinal()] + "," + array[i][DeathColumn.MALE_UNDER_1_WEEK.ordinal()] + "," + array[i][DeathColumn.FEMALE_UNDER_1_WEEK.ordinal()] +
                "," + array[i][DeathColumn.TOTAL_UNDER_1_MONTH.ordinal()] + "," + array[i][DeathColumn.MALE_UNDER_1_MONTH.ordinal()] + "," + array[i][DeathColumn.FEMALE_UNDER_1_MONTH.ordinal()] +
                "," + array[i][DeathColumn.TOTAL_UNDER_3_MONTH.ordinal()] + "," + array[i][DeathColumn.MALE_UNDER_3_MONTH.ordinal()] + "," + array[i][DeathColumn.FEMALE_UNDER_3_MONTH.ordinal()] +
                "," + array[i][DeathColumn.TOTAL_UNDER_6_MONTH.ordinal()] + "," + array[i][DeathColumn.MALE_UNDER_6_MONTH.ordinal()] + "," + array[i][DeathColumn.FEMALE_UNDER_6_MONTH.ordinal()] +
                "," + array[i][DeathColumn.TOTAL_UNDER_9_MONTH.ordinal()] + "," + array[i][DeathColumn.MALE_UNDER_9_MONTH.ordinal()] + "," + array[i][DeathColumn.FEMALE_UNDER_9_MONTH.ordinal()] +
                "," + array[i][DeathColumn.TOTAL_UNDER_12_MONTH.ordinal()] + "," + array[i][DeathColumn.MALE_UNDER_12_MONTH.ordinal()] + "," + array[i][DeathColumn.FEMALE_UNDER_12_MONTH.ordinal()] +
                "," + array[i][DeathColumn.SINHALESE_TOTAL.ordinal()] + "," + array[i][DeathColumn.SINHALESE_MALE.ordinal()] + "," + array[i][DeathColumn.SINHALESE_FEMALE.ordinal()] +
                "," + array[i][DeathColumn.SRI_LANKAN_TAMIL_TOTAL.ordinal()] + "," + array[i][DeathColumn.SRI_LANKAN_TAMIL_MALE.ordinal()] + "," + array[i][DeathColumn.SRI_LANKAN_TAMIL_FEMALE.ordinal()] +
                "," + array[i][DeathColumn.INDIAN_TAMIL_TOTAL.ordinal()] + "," + array[i][DeathColumn.INDIAN_TAMIL_MALE.ordinal()] + "," + array[i][DeathColumn.INDIAN_TAMIL_FEMALE.ordinal()] +
                "," + array[i][DeathColumn.SRI_LANKAN_MOOR_TOTAL.ordinal()] + "," + array[i][DeathColumn.SRI_LANKAN_MOOR_MALE.ordinal()] + "," + array[i][DeathColumn.SRI_LANKAN_MOOR_FEMALE.ordinal()] +
                "," + array[i][DeathColumn.BURGHER_TOTAL.ordinal()] + "," + array[i][DeathColumn.BURGHER_MALE.ordinal()] + "," + array[i][DeathColumn.BURGHER_FEMALE.ordinal()] +
                "," + array[i][DeathColumn.MALAY_TOTAL.ordinal()] + "," + array[i][DeathColumn.MALAY_MALE.ordinal()] + "," + array[i][DeathColumn.MALAY_FEMALE.ordinal()] +
                "," + array[i][DeathColumn.SRI_LANKAN_CHETTY_TOTAL.ordinal()] + "," + array[i][DeathColumn.SRI_LANKAN_CHETTY_MALE.ordinal()] + "," + array[i][DeathColumn.SRI_LANKAN_CHETTY_FEMALE.ordinal()] +
                "," + array[i][DeathColumn.BHARATHA_TOTAL.ordinal()] + "," + array[i][DeathColumn.BHARATHA_MALE.ordinal()] + "," + array[i][DeathColumn.BHARATHA_FEMALE.ordinal()] +
                "," + array[i][DeathColumn.INDIAN_MOOR_TOTAL.ordinal()] + "," + array[i][DeathColumn.INDIAN_MOOR_MALE.ordinal()] + "," + array[i][DeathColumn.INDIAN_MOOR_FEMALE.ordinal()] +
                "," + array[i][DeathColumn.PAKISTAN_TOTAL.ordinal()] + "," + array[i][DeathColumn.PAKISTAN_MALE.ordinal()] + "," + array[i][DeathColumn.PAKISTAN_FEMALE.ordinal()] +
                "," + array[i][DeathColumn.OTHER_FOREIGNERS_TOTAL.ordinal()] + "," + array[i][DeathColumn.OTHER_FOREIGNERS_MALE.ordinal()] + "," + array[i][DeathColumn.OTHER_FOREIGNERS_FEMALE.ordinal()] +
                "," + array[i][DeathColumn.OTHER_SL_TOTAL.ordinal()] + "," + array[i][DeathColumn.OTHER_SL_MALE.ordinal()] + "," + array[i][DeathColumn.OTHER_SL_FEMALE.ordinal()] +
                "," + array[i][DeathColumn.UNKNOWN_RACE_TOTAL.ordinal()] + "," + array[i][DeathColumn.UNKNOWN_RACE_MALE.ordinal()] + "," + array[i][DeathColumn.UNKNOWN_RACE_FEMALE.ordinal()] +
                "," + array[i][DeathColumn.JANUARY_TOTAL.ordinal()] + "," + array[i][DeathColumn.JANUARY_MALE.ordinal()] + "," + array[i][DeathColumn.JANUARY_FEMALE.ordinal()] +
                "," + array[i][DeathColumn.FEBRUARY_TOTAL.ordinal()] + "," + array[i][DeathColumn.FEBRUARY_MALE.ordinal()] + "," + array[i][DeathColumn.FEBRUARY_FEMALE.ordinal()] +
                "," + array[i][DeathColumn.MARCH_TOTAL.ordinal()] + "," + array[i][DeathColumn.MARCH_MALE.ordinal()] + "," + array[i][DeathColumn.MARCH_FEMALE.ordinal()] +
                "," + array[i][DeathColumn.APRIL_TOTAL.ordinal()] + "," + array[i][DeathColumn.APRIL_MALE.ordinal()] + "," + array[i][DeathColumn.APRIL_FEMALE.ordinal()] +
                "," + array[i][DeathColumn.MAY_TOTAL.ordinal()] + "," + array[i][DeathColumn.MAY_MALE.ordinal()] + "," + array[i][DeathColumn.MAY_FEMALE.ordinal()] +
                "," + array[i][DeathColumn.JUNE_TOTAL.ordinal()] + "," + array[i][DeathColumn.JUNE_MALE.ordinal()] + "," + array[i][DeathColumn.JUNE_FEMALE.ordinal()] +
                "," + array[i][DeathColumn.JULY_TOTAL.ordinal()] + "," + array[i][DeathColumn.JULY_MALE.ordinal()] + "," + array[i][DeathColumn.JULY_FEMALE.ordinal()] +
                "," + array[i][DeathColumn.AUGUST_TOTAL.ordinal()] + "," + array[i][DeathColumn.AUGUST_MALE.ordinal()] + "," + array[i][DeathColumn.AUGUST_FEMALE.ordinal()] +
                "," + array[i][DeathColumn.SEPTEMBER_TOTAL.ordinal()] + "," + array[i][DeathColumn.SEPTEMBER_MALE.ordinal()] + "," + array[i][DeathColumn.SEPTEMBER_FEMALE.ordinal()] +
                "," + array[i][DeathColumn.OCTOBER_TOTAL.ordinal()] + "," + array[i][DeathColumn.OCTOBER_MALE.ordinal()] + "," + array[i][DeathColumn.OCTOBER_FEMALE.ordinal()] +
                "," + array[i][DeathColumn.NOVEMBER_TOTAL.ordinal()] + "," + array[i][DeathColumn.NOVEMBER_MALE.ordinal()] + "," + array[i][DeathColumn.NOVEMBER_FEMALE.ordinal()] +
                "," + array[i][DeathColumn.DECEMBER_TOTAL.ordinal()] + "," + array[i][DeathColumn.DECEMBER_MALE.ordinal()] + "," + array[i][DeathColumn.DECEMBER_FEMALE.ordinal()] +
                "," + "\n");
        }

        String dirPath = "reports" + File.separator + year;
        File dir = new File(dirPath);
        dir.mkdirs();

        String filePath = dirPath + File.separator + filename;
        File file = new File(filePath);

        try {
            FileOutputStream out = new FileOutputStream(file);
            out.write(csv.toString().getBytes());
            out.close();
        } catch (IOException e) {
            logger.error("Error writing the CSV - {} {}", file.getPath() + file.getName(), e.getMessage());
        }
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
            handleException(user.getUserId() + " doesn't have permission to create the report",
                ErrorCodes.PERMISSION_DENIED);
        }
        StringBuilder csv = getReportHeader(headerCode);
        String filename = "unknown";

        int length = statistics.totals.size();
        List<BirthDistrictStatistics> districtStat;
        int i = 0;
        switch (headerCode) {
            case ReportCodes.TABLE_2_2:
                filename = ReportCodes.TABLE_2_2_NAME + ".csv";
                for (i = 0; i < BirthIslandWideStatistics.NO_OF_DISTRICTS; i++) {
                    District district = districtDAO.getDistrict(i + 1);
                    String districtId = "Unknown";
                    if (district != null) {
                        districtId = district.getEnDistrictName();
                    }
                    csv.append(districtId);
                    csv.append(",");
                    csv.append(table_2_2[i][2]);
                    csv.append(",");
                    csv.append(table_2_2[i][0]);
                    csv.append(",");
                    csv.append(table_2_2[i][1]);
                    csv.append("\n");
                }
                break;
            case ReportCodes.TABLE_2_8:
                filename = ReportCodes.TABLE_2_8_NAME + ".csv";
                for (i = 0; i < length; i++) {
                    BirthDistrictStatistics districtStats = statistics.totals.get(i);
                    District district = districtDAO.getDistrict(i);
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
                for (i = 0; i < BirthMonthlyStatistics.NO_OF_RACES; i++) {
                    Race race = raceDAO.getRace(i);
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
                for (i = 0; i < 26; i++) {
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

                for (i = 0; i < BirthIslandWideStatistics.NO_OF_DISTRICTS; i++) {
                    District district = districtDAO.getDistrict(i + 1);
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

                for (int p = 0; p < BirthIslandWideStatistics.NO_OF_DISTRICTS; p++) {
                    String disName = "Unknown";
                    District district = districtDAO.getDistrict(p + 1);
                    if (district != null) {
                        disName = district.getEnDistrictName();
                    }
                    csv.append(disName + ",");
                    int districtTotal_m = 0, districtTotal_f = 0, districtTotal_all = 0;
                    for (int q = 0; q < 12; q++) {
                        csv.append(table_2_6[p][q][2] + ",");
                        districtTotal_all += table_2_6[p][q][2];
                        csv.append(table_2_6[p][q][0] + ",");
                        districtTotal_m += table_2_6[p][q][0];
                        csv.append(table_2_6[p][q][1] + ",");
                        districtTotal_f += table_2_6[p][q][1];
                    }
                    csv.append(districtTotal_all + ",");
                    csv.append(districtTotal_m + ",");
                    csv.append(districtTotal_f + ",");
                    csv.append("\n");
                }

                break;
            case ReportCodes.TABLE_2_2A:
                filename = ReportCodes.TABLE_2_2A_NAME + ".csv";

                for (int p = 0; p < BirthIslandWideStatistics.NO_OF_DISTRICTS; p++) {
                    int t = 0;
                    District district = districtDAO.getDistrict(p + 1);
                    String districtName = "Unknown";
                    if (district != null) {
                        districtName = district.getEnDistrictName();
                    }
                    csv.append(districtName + ",");
                    for (int q = 0; q < BirthDistrictStatistics.NO_OF_MONTHS; q++) {
                        csv.append(table_2_2a[p][q] + ",");
                        t += table_2_2a[p][q];
                    }
                    csv.append(t + ",");
                    csv.append("\n");
                }
                csv.append("\n");
                break;
            case ReportCodes.TABLE_2_3:
                filename = ReportCodes.TABLE_2_3_NAME + ".csv";

                districtStat = statistics.totals;
                i = 0;
                for (BirthDistrictStatistics bds : districtStat) {
                    District district = districtDAO.getDistrict(i);
                    String districtName = "Unknown";
                    if (district != null) {
                        districtName = district.getEnDistrictName();
                    }
                    csv.append(districtName + ",");
                    int dis_total = 0, dis_male = 0, dis_female = 0;
                    List<BirthChildRankStatistics> bcs = bds.birthOrder;
                    for (BirthChildRankStatistics rankStat : bcs) {
                        csv.append(rankStat.getMaleTotal() + ",");
                        csv.append(rankStat.getFemaleTotal() + ",");
                        csv.append(rankStat.getTotal() + ",");

                        dis_total += rankStat.getTotal();
                        dis_female += rankStat.getFemaleTotal();
                        dis_male += rankStat.getMaleTotal();
                    }
                    csv.append(dis_male + "," + dis_female + "," + dis_total);
                    csv.append("\n");
                    i++;
                }
                break;
            case ReportCodes.TABLE_2_11:
                filename = ReportCodes.TABLE_2_11_NAME + ".csv";
                String months[] = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
                for (int u = 0; u < months.length; u++) {
                    int rTotal = 0, rMale = 0, rFemale = 0;
                    csv.append(months[u]);
                    for (int v = 0; v < BirthMonthlyStatistics.NO_OF_RACES; v++) {
                        csv.append("," + month_race_total[u][v][0]);
                        csv.append("," + month_race_total[u][v][1]);
                        csv.append("," + month_race_total[u][v][2]);
                        rTotal += month_race_total[u][v][2];
                        rMale += month_race_total[u][v][0];
                        rFemale += month_race_total[u][v][1];
                    }
                    csv.append("," + rMale);
                    csv.append("," + rFemale);
                    csv.append("," + rTotal);
                    csv.append("\n");
                }
                break;
            case ReportCodes.TABLE_2_10:
                filename = ReportCodes.TABLE_2_10_NAME + ".csv";

                for (int k = 0; k < 38; k++) {
                    csv.append((k + 13) + ",");

                    csv.append(sector_leg_total[k][2][2] + ",");
                    csv.append(nf.format((sector_leg_total[k][2][2] / checkZero(total_arr[0])) * 100.0) + ",");
                    csv.append(sector_leg_total[k][2][0] + ",");
                    csv.append(nf.format((sector_leg_total[k][2][0] / checkZero(total_arr[0])) * 100.0) + ",");
                    csv.append(sector_leg_total[k][2][1] + ",");
                    csv.append(nf.format((sector_leg_total[k][2][1] / checkZero(total_arr[0])) * 100.0) + ",");

                    csv.append(sector_leg_total[k][0][2] + ",");
                    csv.append(nf.format((sector_leg_total[k][0][2] / checkZero(total_arr[0])) * 100.0) + ",");
                    csv.append(sector_leg_total[k][0][0] + ",");
                    csv.append(nf.format((sector_leg_total[k][0][0] / checkZero(total_arr[0])) * 100.0) + ",");
                    csv.append(sector_leg_total[k][0][1] + ",");
                    csv.append(nf.format((sector_leg_total[k][0][1] / checkZero(total_arr[0])) * 100.0) + ",");

                    csv.append(sector_leg_total[k][1][2] + ",");
                    csv.append(nf.format((sector_leg_total[k][1][2] / checkZero(total_arr[0])) * 100.0) + ",");
                    csv.append(sector_leg_total[k][1][0] + ",");
                    csv.append(nf.format((sector_leg_total[k][1][0] / checkZero(total_arr[0])) * 100.0) + ",");
                    csv.append(sector_leg_total[k][1][1] + ",");
                    csv.append(nf.format((sector_leg_total[k][1][1] / checkZero(total_arr[0])) * 100.0) + ",");
                    csv.append("\n");
                }

                break;
            case ReportCodes.TABLE_2_12:
                filename = ReportCodes.TABLE_2_12_NAME + ".csv";

                for (int k = 1; k < districtYearStatisticsList.length; k++) {
                    District district = districtDAO.getDistrict(k);
                    String districtName = "Unknown";
                    if (district != null) {
                        districtName = district.getEnDistrictName();
                    }
                    int all = 0, male = 0, female = 0;
                    csv.append(districtName + "\n");
                    csv.append("Before " + (year - 1) + ",");
                    for (int l = 0; l < 12; l++) {
                        csv.append(districtYearStatisticsList[k - 1].before_last_year_month_array[l][2] + ",");
                        csv.append(districtYearStatisticsList[k - 1].before_last_year_month_array[l][0] + ",");
                        csv.append(districtYearStatisticsList[k - 1].before_last_year_month_array[l][1] + ",");
                        all += districtYearStatisticsList[k - 1].before_last_year_month_array[l][2];
                        male += districtYearStatisticsList[k - 1].before_last_year_month_array[l][0];
                        female += districtYearStatisticsList[k - 1].before_last_year_month_array[l][1];
                    }
                    csv.append(all + ",");
                    csv.append(male + ",");
                    csv.append(female + ",");
                    all = 0;
                    male = 0;
                    female = 0;
                    csv.append("\n");
                    csv.append("During " + (year - 1) + ",");
                    for (int l = 0; l < 12; l++) {
                        csv.append(districtYearStatisticsList[k - 1].during_last_year_month_array[l][2] + ",");
                        csv.append(districtYearStatisticsList[k - 1].during_last_year_month_array[l][0] + ",");
                        csv.append(districtYearStatisticsList[k - 1].during_last_year_month_array[l][1] + ",");
                        all += districtYearStatisticsList[k - 1].during_last_year_month_array[l][2];
                        male += districtYearStatisticsList[k - 1].during_last_year_month_array[l][0];
                        female += districtYearStatisticsList[k - 1].during_last_year_month_array[l][1];
                    }
                    csv.append(all + ",");
                    csv.append(male + ",");
                    csv.append(female + ",");
                    all = 0;
                    male = 0;
                    female = 0;
                    csv.append("\n");
                    csv.append("During " + year + ",");
                    for (int l = 0; l < 12; l++) {
                        csv.append(districtYearStatisticsList[k - 1].during_this_year_month_array[l][2] + ",");
                        csv.append(districtYearStatisticsList[k - 1].during_this_year_month_array[l][0] + ",");
                        csv.append(districtYearStatisticsList[k - 1].during_this_year_month_array[l][1] + ",");
                        all += districtYearStatisticsList[k - 1].during_this_year_month_array[l][2];
                        male += districtYearStatisticsList[k - 1].during_this_year_month_array[l][0];
                        female += districtYearStatisticsList[k - 1].during_this_year_month_array[l][1];
                    }
                    csv.append(all + ",");
                    csv.append(male + ",");
                    csv.append(female + ",");
                    csv.append("\n");
                    csv.append("Total,");

                    for (int l = 0; l < 12; l++) {
                        csv.append(districtYearStatisticsList[k - 1].total_year_month_array[l][2] + ",");
                        csv.append(districtYearStatisticsList[k - 1].total_year_month_array[l][0] + ",");
                        csv.append(districtYearStatisticsList[k - 1].total_year_month_array[l][1] + ",");
                    }

                    csv.append("\n");
                }

                break;
        }

        String dirPath = "reports" + File.separator + year;
        File dir = new File(dirPath);
        dir.mkdirs();

        String filePath = dirPath + File.separator + filename;
        File file = new File(filePath);

        try {
            FileOutputStream out = new FileOutputStream(file);
            out.write(csv.toString().getBytes());
            out.close();
        } catch (IOException e) {
            logger.error("Error writing the CSV - {} {}", file.getPath() + file.getName(), e.getMessage());
        }

        return null;
    }

    private float checkZero(int i) {
        if (i == 0) {
            i = 1;
        }
        return (float) i;
    }

    private void handleException(String message, int code) {
        logger.error(message);
        throw new RGDRuntimeException(message, code);
    }

    private StringBuilder getReportHeader(int code) {
        StringBuilder csv = new StringBuilder();
        List<BirthDistrictStatistics> districtStat;
        switch (code) {
            case ReportCodes.TABLE_2_2:
                csv.append("District,Total,Male,Female\n");
                csv.append("Sri Lanka,");
                csv.append(table_2_2[BirthIslandWideStatistics.NO_OF_DISTRICTS][2] + ",");
                csv.append(table_2_2[BirthIslandWideStatistics.NO_OF_DISTRICTS][0] + ",");
                csv.append(table_2_2[BirthIslandWideStatistics.NO_OF_DISTRICTS][1] + ",\n");
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
                int total = 0;
                csv.append("Race,Less than 15,15-19,20-24,25-29,30-34,35-39,40-44,45-49,50 & above,All Ages\n");
                csv.append("All Race,");

                int islandTotal = 0;
                for (int m = 0; m < BirthRaceStatistics.NO_OF_AGE_GROUPS; m++) {
                    int raceTotal = 0;
                    for (int k = 0; k < BirthMonthlyStatistics.NO_OF_RACES; k++) {
                        raceTotal += age_race_total[k][m];
                    }
                    islandTotal += raceTotal;
                    csv.append(raceTotal + ",");
                }
                csv.append(islandTotal);
                csv.append("\n");
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
                int slTotal = 0;
                for (int n = 0; n < BirthRaceStatistics.NO_OF_AGE_GROUPS; n++) {
                    int ageTotal = 0;
                    for (int m = 0; m < BirthIslandWideStatistics.NO_OF_DISTRICTS; m++) {
                        ageTotal += age_district_total[m][n];
                    }
                    slTotal += ageTotal;
                    csv.append(ageTotal + ",");
                }
                csv.append(slTotal + ",");
                csv.append("\n");
                break;
            case ReportCodes.TABLE_2_6:
                csv.append(",,January,,,February,,,March,,,April,,,May,,,June,,,July,,,August,,,September,,,October,,,November,,,December,,,Total,,\n");
                csv.append("District,total,male,female,");
                for (int i = 0; i < 12; i++) {
                    csv.append("total,male,female,");
                }
                csv.append("\nSri Lanka,");
                int m[][] = new int[12][3];

                for (int q = 0; q < 12; q++) {
                    for (int p = 0; p < BirthDistrictStatistics.NO_OF_MONTHS; p++) {
                        m[q][2] += table_2_6[p][q][2];
                        m[q][0] += table_2_6[p][q][0];
                        m[q][1] += table_2_6[p][q][1];
                    }
                }
                int districtTotal_m = 0, districtTotal_f = 0, districtTotal_all = 0;
                for (int p = 0; p < 12; p++) {
                    csv.append(m[p][2] + ",");
                    districtTotal_all += m[p][2];
                    csv.append(m[p][0] + ",");
                    districtTotal_m += m[p][0];
                    csv.append(m[p][1] + ",");
                    districtTotal_f += m[p][1];
                }
                csv.append(districtTotal_all + ",");
                csv.append(districtTotal_m + ",");
                csv.append(districtTotal_f + ",");
                csv.append("\n");

                break;
            case ReportCodes.TABLE_2_2A:
                csv.append("District,January,February,March,April,May,June,July,August,September,October,November,December,Total\n");
                csv.append("Sri Lanka");

                int arr[] = new int[12];
                for (int q = 0; q < BirthDistrictStatistics.NO_OF_MONTHS; q++) {
                    for (int p = 0; p < BirthIslandWideStatistics.NO_OF_DISTRICTS; p++) {
                        arr[q] += table_2_2a[p][q];
                    }
                }
                int t = 0;
                for (int k = 0; k < arr.length; k++) {
                    csv.append("," + arr[k]);
                    t += arr[k];
                }
                csv.append("," + t);
                csv.append("\n");
                break;
            case ReportCodes.TABLE_2_3:
                districtStat = statistics.totals;
                csv.append("District,");
                int array[][] = new int[10][3];

                for (BirthDistrictStatistics district : districtStat) {
                    List<BirthChildRankStatistics> rankList = district.birthOrder;
                    for (BirthChildRankStatistics bcs : rankList) {
                        int rank = bcs.getRank();
                        array[rank][0] += bcs.getMaleTotal();
                        array[rank][1] += bcs.getFemaleTotal();
                        array[rank][2] += bcs.getTotal();
                    }
                }
                for (int b = 0; b < 10; b++) {
                    if (b == 0) {
                        csv.append(",Unknown,,");
                    } else {
                        csv.append(",rank : " + b + ",,");
                    }
                }
                csv.append(",Total,,");
                csv.append("\n,");
                for (int c = 0; c < 10; c++) {
                    csv.append("male,female,total,");
                }
                csv.append("male,female,total");

                int dis_male = 0, dis_female = 0, dis_total = 0;
                csv.append("\nSri Lanka");
                for (int k = 0; k < 10; k++) {
                    for (int l = 0; l < 3; l++) {
                        csv.append("," + array[k][l]);
                        if (l == 2) {
                            dis_total += array[k][l];
                        }
                        if (l == 0) {
                            dis_male += array[k][l];
                        }
                        if (l == 1) {
                            dis_female += array[k][l];
                        }
                    }
                }
                csv.append("," + dis_male + "," + dis_female + "," + dis_total);
                csv.append("\n");
                break;
            case ReportCodes.TABLE_2_11:
                csv.append("Month");
                for (int c = 1; c < BirthMonthlyStatistics.NO_OF_RACES + 1; c++) {
                    csv.append(",," + raceDAO.getNameByPK(c, "en") + ",");
                }
                csv.append(",,Total,");
                csv.append("\n,");
                for (int c = 1; c < BirthMonthlyStatistics.NO_OF_RACES + 1; c++) {
                    csv.append("male,female,total,");
                }
                csv.append("male,female,total,");
                csv.append("\n");
                csv.append("All Months");
                int oMale = 0, oFemale = 0, oTotal = 0;
                for (int d = 0; d < BirthMonthlyStatistics.NO_OF_RACES; d++) {
                    int rTotal = 0, rMale = 0, rFemale = 0;
                    for (int c = 0; c < BirthDistrictStatistics.NO_OF_MONTHS; c++) {
                        rMale += month_race_total[c][d][0];
                        rFemale += month_race_total[c][d][1];
                        rTotal += month_race_total[c][d][2];
                    }
                    csv.append("," + rMale);
                    csv.append("," + rFemale);
                    csv.append("," + rTotal);
                    oMale += rMale;
                    oFemale += rFemale;
                    oTotal += rTotal;
                }
                csv.append("," + oMale);
                csv.append("," + oFemale);
                csv.append("," + oTotal);
                csv.append("\n");
                break;
            case ReportCodes.TABLE_2_10:
                csv.append("Sector/age of Mother,,,,Total,,,,,,Legitimacy Births,,,,,,Illegitimacy Births,,,\n");
                csv.append(",,Total,,Male,,Female,,Total,,Male,,Female,,Total,,Male,,Female\n");
                csv.append(",NO,%,NO,%,NO,%,NO,%,NO,%,NO,%,NO,%,NO,%,NO,%\n");
                csv.append("All sector total");
                total_arr = new int[9];
                for (int k = 0; k < 38; k++) {
                    total_arr[0] += sector_leg_total[k][2][2];
                    total_arr[1] += sector_leg_total[k][2][0];
                    total_arr[2] += sector_leg_total[k][2][1];

                    total_arr[3] += sector_leg_total[k][0][2];
                    total_arr[4] += sector_leg_total[k][0][0];
                    total_arr[5] += sector_leg_total[k][0][1];

                    total_arr[6] += sector_leg_total[k][1][2];
                    total_arr[7] += sector_leg_total[k][1][0];
                    total_arr[8] += sector_leg_total[k][1][1];
                }
                for (int k = 0; k < total_arr.length; k++) {
                    csv.append("," + total_arr[k]);
                    csv.append(",100.0");
                }
                csv.append("\n");
                break;
            case ReportCodes.TABLE_2_12:
                csv.append("District / Year of Occurrence,,January,,,February,,,March,,,April,,,May,,,June,,,July,,,August,,,September,,,October,,,November,,,December,,,Total,\n");
                for (int k = 0; k < 13; k++) {
                    csv.append(",Total,Male,Female");
                }
                csv.append("\n");
                break;
        }

        return csv;
    }
}