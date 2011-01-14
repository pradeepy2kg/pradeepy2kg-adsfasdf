package lk.rgd.crs.core.service;

import lk.rgd.common.api.dao.DistrictDAO;
import lk.rgd.common.api.dao.DSDivisionDAO;
import lk.rgd.common.api.domain.DSDivision;
import lk.rgd.common.api.domain.User;
import lk.rgd.common.api.service.UserManager;
import lk.rgd.crs.api.service.ReportsGenerator;
import lk.rgd.crs.api.service.BirthRegistrationService;
import lk.rgd.crs.api.bean.BirthIslandWideStatistics;
import lk.rgd.crs.api.domain.BirthDeclaration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Calendar;
import java.util.Date;

/**
 * @author asankha
 */
public class ReportsGeneratorImpl implements ReportsGenerator {
    private static final Logger logger = LoggerFactory.getLogger(ReportsGeneratorImpl.class);

    private final BirthRegistrationService birthRegister;
    private final DistrictDAO districtDAO;
    private final DSDivisionDAO dsDivisionDAO;
    private final UserManager userManagementService;

    public ReportsGeneratorImpl(BirthRegistrationService birthRegister, DistrictDAO districtDAO, DSDivisionDAO dsDivisionDAO, UserManager service) {
        this.birthRegister = birthRegister;
        this.districtDAO = districtDAO;
        this.dsDivisionDAO = dsDivisionDAO;
        this.userManagementService = service;
    }

    public BirthIslandWideStatistics generate() {
        BirthIslandWideStatistics statistics = BirthIslandWideStatistics.getInstance();
        List <DSDivision> dsDivisions = dsDivisionDAO.findAll();
        User systemUser = userManagementService.getSystemUser();
        List <BirthDeclaration> birthRecords;

        Calendar c = Calendar.getInstance();
        Date endDate = c.getTime();
        c.set(c.get(Calendar.YEAR), 0, 0);
        Date startDate = c.getTime();
        for (DSDivision dsDivision : dsDivisions) {
            birthRecords = birthRegister.getByDSDivisionAndStatusAndBirthDateRange(dsDivision, startDate, endDate,
                    BirthDeclaration.State.ARCHIVED_CERT_GENERATED, systemUser);   // returns all records so far in this year
            int dsIndex = dsDivision.getDsDivisionUKey(); // index to track the placeholder in the list inside statistics object
            int districtIndex = dsDivision.getDistrict().getDistrictUKey();
            for (BirthDeclaration bd : birthRecords) {
                //statistics.totals.set();
                //todo update the totals in statistics object for these indexes.
            }
        }
        return statistics;
    }
}