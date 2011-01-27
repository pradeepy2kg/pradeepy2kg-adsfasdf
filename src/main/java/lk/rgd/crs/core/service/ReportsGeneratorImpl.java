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
import lk.rgd.crs.api.domain.ChildInfo;
import lk.rgd.crs.api.domain.MarriageInfo;
import lk.rgd.crs.api.domain.ParentInfo;
import lk.rgd.crs.api.service.ReportsGenerator;
import lk.rgd.crs.api.service.BirthRegistrationService;
import lk.rgd.crs.api.domain.BirthDeclaration;
import lk.rgd.crs.web.ReportCodes;
import lk.rgd.prs.api.domain.Marriage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    int total_arr[];
    private int year;
    private BirthDistrictYearStatistics[] districtYearStatisticsList;

    public ReportsGeneratorImpl(BirthRegistrationService birthRegister, DistrictDAO districtDAO, DSDivisionDAO dsDivisionDAO, RaceDAO raceDAO, UserManager service) {
        this.birthRegister = birthRegister;
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
                BirthDeclaration.State.ARCHIVED_CERT_PRINTED, systemUser);   // returns all records so far in this year
            int dsIndex = dsDivision.getDsDivisionUKey(); // todo support tracking at DSDivision level
            int districtIndex = dsDivision.getDistrict().getDistrictUKey();
            BirthDistrictStatistics districtStats = statistics.totals.get(districtIndex);
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
                BirthDeclaration.State.ARCHIVED_CERT_PRINTED, systemUser);   // returns all records so far in this year

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

    public BirthIslandWideStatistics generate_2_3(int year, User user) {

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
    public BirthIslandWideStatistics generate_2_5(int year, User user) {  //TODO - find more efficient way to do this. statistics object become useless if i use this type of method
        this.year = year;
        if (statistics.isPopulated) {
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
        statistics.isPopulated = true;
        return statistics;
    }

    /**
     * @inheritDoc
     */
    public BirthIslandWideStatistics generate_2_4(int year, User user) {
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

    public BirthIslandWideStatistics generate_2_11(int year, User user) {
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

    public BirthIslandWideStatistics generate_2_10(int year, User user) {
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
                        }
                    }
                }
            }
        }

        return statistics;
    }

    public BirthIslandWideStatistics generate_2_12(int year, User user) {
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
                for (int p = 0, n = BirthIslandWideStatistics.NO_OF_DISTRICTS; p < n; p++) {
                    for (int q = 0; q < BirthRaceStatistics.NO_OF_AGE_GROUPS; q++) {
                        age_district_total[p][q] = 0;
                    }
                }
                for (i = 0; i < BirthIslandWideStatistics.NO_OF_DISTRICTS; i++) {
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

                districtStat = statistics.totals;
                i = 0;
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
            case ReportCodes.TABLE_2_2A:
                filename = ReportCodes.TABLE_2_2A_NAME + ".csv";

                districtStat = statistics.totals;
                i = 0;
                for (BirthDistrictStatistics bds : districtStat) {
                    District district = districtDAO.getDistrict(i);
                    String districtName = "Unknown";
                    if (district != null) {
                        districtName = district.getEnDistrictName();
                    }
                    csv.append(districtName + ",");
                    List<BirthMonthlyStatistics> birthMonthlyStat = bds.monthlyTotals;
                    int month_t = 0;
                    for (BirthMonthlyStatistics bms : birthMonthlyStat) {
                        csv.append(bms.getTotalBirthFromMonths() + ",");
                        month_t += bms.getTotalBirthFromMonths();
                    }
                    csv.append(month_t + "\n");
                    i++;
                }
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
                    csv.append((sector_leg_total[k][2][2] / checkZero(total_arr[0])) * 100.0 + ",");
                    csv.append(sector_leg_total[k][2][0] + ",,");
                    csv.append((sector_leg_total[k][2][0] / checkZero(total_arr[0])) * 100.0 + ",");
                    csv.append(sector_leg_total[k][2][1] + ",");
                    csv.append((sector_leg_total[k][2][1] / checkZero(total_arr[0])) * 100.0 + ",");

                    csv.append(sector_leg_total[k][0][2] + ",");
                    csv.append((sector_leg_total[k][0][2] / checkZero(total_arr[0])) * 100.0 + ",");
                    csv.append(sector_leg_total[k][0][0] + ",");
                    csv.append((sector_leg_total[k][0][0] / checkZero(total_arr[0])) * 100.0 + ",");
                    csv.append(sector_leg_total[k][0][1] + ",");
                    csv.append((sector_leg_total[k][0][1] / checkZero(total_arr[0])) * 100.0 + ",");

                    csv.append(sector_leg_total[k][1][2] + ",");
                    csv.append((sector_leg_total[k][1][2] / checkZero(total_arr[0])) * 100.0 + ",");
                    csv.append(sector_leg_total[k][1][0] + ",");
                    csv.append((sector_leg_total[k][1][0] / checkZero(total_arr[0])) * 100.0 + ",");
                    csv.append(sector_leg_total[k][1][1] + ",");
                    csv.append((sector_leg_total[k][1][1] / checkZero(total_arr[0])) * 100.0 + ",");
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
                for (int i = 0; i < BirthMonthlyStatistics.NO_OF_RACES; i++) {
                    for (int j = 0; j < BirthRaceStatistics.NO_OF_AGE_GROUPS; j++) {
                        age_race_total[i][j] = 0;
                    }
                }
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
            case ReportCodes.TABLE_2_2A:
                csv.append("District,January,February,March,April,May,June,July,August,September,October,November,December,Total\n");
                csv.append("Sri Lanka");

                districtStat = statistics.totals;
                int arr[] = new int[12];
                for (BirthDistrictStatistics bds : districtStat) {
                    List<BirthMonthlyStatistics> birthMonthlyStat = bds.monthlyTotals;
                    int k = 0;
                    for (BirthMonthlyStatistics bms : birthMonthlyStat) {
                        arr[k] += bms.getTotalBirthFromMonths();
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