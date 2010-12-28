package lk.rgd.crs.core.service;

import lk.rgd.common.api.dao.AppParametersDAO;
import lk.rgd.common.api.dao.DistrictDAO;
import lk.rgd.common.api.dao.DSDivisionDAO;
import lk.rgd.common.api.domain.District;
import lk.rgd.common.api.domain.DSDivision;
import lk.rgd.common.api.domain.User;
import lk.rgd.common.core.index.SolrIndexManager;
import lk.rgd.common.util.CivilStatusUtil;
import lk.rgd.common.util.GenderUtil;
import lk.rgd.common.util.LifeStatusUtil;
import lk.rgd.crs.api.service.PRSRecordsIndexer;
import lk.rgd.crs.api.service.ReportsGenerator;
import lk.rgd.crs.api.service.BirthRegistrationService;
import lk.rgd.crs.api.bean.BirthIslandWideStatistics;
import lk.rgd.crs.api.domain.BirthDeclaration;
import lk.rgd.crs.core.DatabaseInitializer;
import lk.rgd.prs.api.dao.PersonDAO;
import lk.rgd.prs.api.domain.Address;
import lk.rgd.prs.api.domain.Person;
import lk.rgd.prs.api.domain.PersonCitizenship;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrInputDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.*;
import java.util.Date;
import java.util.List;
import java.util.Calendar;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author asankha
 */
public class ReportsGeneratorImpl implements ReportsGenerator {
    private static final Logger logger = LoggerFactory.getLogger(ReportsGeneratorImpl.class);

    private final BirthRegistrationService register;
    private final DistrictDAO districtDAO;
    private final DSDivisionDAO dsDivisionDAO;

    public ReportsGeneratorImpl(BirthRegistrationService register, DistrictDAO districtDAO, DSDivisionDAO dsDivisionDAO) {
        this.register = register;
        this.districtDAO = districtDAO;
        this.dsDivisionDAO = dsDivisionDAO;
    }

    public BirthIslandWideStatistics generate() {
        BirthIslandWideStatistics statistics = BirthIslandWideStatistics.getInstance();
        List <DSDivision> dsDivisions = dsDivisionDAO.findAll();
        for (DSDivision dsDivision : dsDivisions) {
            //register.getByDSDivisionAndStatusAndBirthDateRange(dsDivision, Calendar.getInstance().getTime(), Calendar.getInstance().getTime(),
            //        BirthDeclaration.State.ARCHIVED_CERT_GENERATED, User);
            // todo get all birth entries for this DS with birth date falls in the given year     
        }
        return statistics;
    }
}