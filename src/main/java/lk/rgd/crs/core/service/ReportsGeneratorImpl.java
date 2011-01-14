package lk.rgd.crs.core.service;

import lk.rgd.common.api.dao.DistrictDAO;
import lk.rgd.common.api.dao.DSDivisionDAO;
import lk.rgd.common.api.domain.DSDivision;
import lk.rgd.common.api.domain.User;
import lk.rgd.common.api.service.UserManager;
import lk.rgd.crs.api.service.ReportsGenerator;
import lk.rgd.crs.api.service.BirthRegistrationService;
import lk.rgd.crs.api.bean.BirthIslandWideStatistics;
import lk.rgd.crs.api.bean.BirthDistrictStatistics;
import lk.rgd.crs.api.domain.BirthDeclaration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Calendar;
import java.util.Date;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
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
     *    Generate a complete statistics object containing whole islandwide
     * @return  BirthIslandWideStatistics
     */
    public BirthIslandWideStatistics generate() {
        List <DSDivision> dsDivisions = dsDivisionDAO.findAll();
        User systemUser = userManagementService.getSystemUser();
        List <BirthDeclaration> birthRecords;

        Calendar c = Calendar.getInstance();
        Date endDate = c.getTime();
        c.set(c.get(Calendar.YEAR), 0, 0);
        Date startDate = c.getTime();

        int all=0, allMales=0, allFemales=0;
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
     *  Creates a Standard CSV file from the generated IslandWide stats.
     *  currently assumes. stats are already geneated.
     * // todo check if a CSV file already generated and avaialble for the given year.
     * @return String the path and name of the created CSV file.
     */
    public String createReport() {
        StringBuilder csv = new StringBuilder();
        csv.append("District,Total,Male,Female\n");
        csv.append("Sri Lanka,");
        csv.append(statistics.getTotal()+",");
        csv.append(statistics.getMaleTotal()+",");
        csv.append(statistics.getFemaleTotal()+",\n");

        int length = statistics.totals.size();
        for (int i=0; i<length; i++) {
            BirthDistrictStatistics districtStats = statistics.totals.get(i);
            csv.append(districtStats.getTotal());
            csv.append(",");
            csv.append(districtStats.getMaleTotal());
            csv.append(",");
            csv.append(districtStats.getFemaleTotal());
            csv.append("\n");
        }

        File file = new File("districtTotals.csv"); //todo define and enforce location+filename rules
        try {
            FileOutputStream out = new FileOutputStream(file);
            out.write(csv.toString().getBytes());
            out.close();
        } catch (IOException e) {
            logger.error("Error writing the CSV - {} {}", file.getPath()+file.getName(), e.getMessage());
        }

        return null;
    }
}