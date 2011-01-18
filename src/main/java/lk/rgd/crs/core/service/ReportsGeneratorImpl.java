package lk.rgd.crs.core.service;

import lk.rgd.ErrorCodes;
import lk.rgd.Permission;
import lk.rgd.common.RGDRuntimeException;
import lk.rgd.common.api.dao.DistrictDAO;
import lk.rgd.common.api.dao.DSDivisionDAO;
import lk.rgd.common.api.domain.DSDivision;
import lk.rgd.common.api.domain.District;
import lk.rgd.common.api.domain.User;
import lk.rgd.common.api.service.UserManager;
import lk.rgd.crs.api.domain.MarriageInfo;
import lk.rgd.crs.api.service.ReportsGenerator;
import lk.rgd.crs.api.service.BirthRegistrationService;
import lk.rgd.crs.api.bean.BirthIslandWideStatistics;
import lk.rgd.crs.api.bean.BirthDistrictStatistics;
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
    private final DSDivisionDAO dsDivisionDAO;
    private final UserManager userManagementService;
    BirthIslandWideStatistics statistics = BirthIslandWideStatistics.getInstance();

    public ReportsGeneratorImpl(BirthRegistrationService birthRegister, DistrictDAO districtDAO, DSDivisionDAO dsDivisionDAO, UserManager service) {
        this.birthRegister = birthRegister;
        this.districtDAO = districtDAO;
        this.dsDivisionDAO = dsDivisionDAO;
        this.userManagementService = service;
    }

    /**
     * Generate a complete statistics object containing whole islandwide
     *
     * @param user
     * @return BirthIslandWideStatistics  @param year
     */
    public BirthIslandWideStatistics generate_2_2(int year, User user) {
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
    public BirthIslandWideStatistics generate_2_8(int year, User user) {
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
                    //districtStats.setLegitimacyBirths(districtStats.getLegitimacyBirths() + 1);
                    legBirths++;
                } else {
                    //districtStats.setIllegitimacyBirths(districtStats.getIllegitimacyBirths() + 1);
                    illegBirths++;
                }
                if (bd.getChild().getBirthAtHospital()) {
                    //districtStats.setHospitalBirths(districtStats.getHospitalBirths() + 1);
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
            float divide = (float)districtStats.getFemaleTotal();
            if(divide == 0){
                divide = 1;
            }
            districtStats.setProportion((districtStats.getMaleTotal()/divide) * 100.0f);

            statistics.totals.set(districtIndex, districtStats);
        }

        statistics.setTotal(statistics.getTotal() + all);
        statistics.setMaleTotal(statistics.getMaleTotal() + allMales);
        statistics.setFemaleTotal(statistics.getFemaleTotal() + allFemales);
        statistics.setLegitimacyBirths(statistics.getLegitimacyBirths() + allLegBirths);
        statistics.setIllegitimacyBirths(statistics.getIllegitimacyBirths() + allIllegBirths);
        statistics.setHospitalBirths(statistics.getHospitalBirths() + allHospitalBirths);
        float divide = (float)statistics.getFemaleTotal();
        if(divide == 0) {
            divide = 1;
        }
        statistics.setProportion((statistics.getMaleTotal()/divide) * 100.0f);

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

        int length = statistics.totals.size();

        if (headerCode == ReportCodes.TABLE_2_2) {
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
        } else if (headerCode == ReportCodes.TABLE_2_8) {
            for(int i = 0; i < length; i++) {
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
        }

        File file = new File("districtTotals.csv"); //todo define and enforce location+filename rules
        try {
            FileOutputStream out = new FileOutputStream(file);
            out.write(csv.toString().getBytes());
            out.close();
        } catch (IOException e) {
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
        }

        return csv;
    }
}