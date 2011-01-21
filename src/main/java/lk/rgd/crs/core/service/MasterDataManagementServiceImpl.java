package lk.rgd.crs.core.service;

import lk.rgd.ErrorCodes;
import lk.rgd.Permission;
import lk.rgd.common.api.dao.DSDivisionDAO;
import lk.rgd.common.api.dao.DistrictDAO;
import lk.rgd.common.api.dao.LocationDAO;
import lk.rgd.common.api.domain.DSDivision;
import lk.rgd.common.api.domain.District;
import lk.rgd.common.api.domain.Location;
import lk.rgd.common.api.domain.User;
import lk.rgd.crs.CRSRuntimeException;
import lk.rgd.crs.api.dao.BDDivisionDAO;
import lk.rgd.crs.api.dao.MRDivisionDAO;
import lk.rgd.crs.api.dao.CourtDAO;
import lk.rgd.crs.api.domain.BDDivision;
import lk.rgd.crs.api.domain.MRDivision;
import lk.rgd.crs.api.domain.Court;
import lk.rgd.crs.api.service.MasterDataManagementService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Performs management of master data
 *
 * @author asankha
 */
public class MasterDataManagementServiceImpl implements MasterDataManagementService {

    private static final Logger logger = LoggerFactory.getLogger(MasterDataManagementServiceImpl.class);

    private final BDDivisionDAO bdDivisionDAO;
    private final MRDivisionDAO mrDivisionDAO;
    private final DSDivisionDAO dsDivisionDAO;
    private final DistrictDAO districtDAO;
    private final LocationDAO locationDAO;
    private final CourtDAO courtDAO;

    public MasterDataManagementServiceImpl(BDDivisionDAO bdDivisionDAO, MRDivisionDAO mrDivisionDAO,
                                           DSDivisionDAO dsDivisionDAO, DistrictDAO districtDAO, LocationDAO locationDAO, CourtDAO courtDAO) {
        this.bdDivisionDAO = bdDivisionDAO;
        this.mrDivisionDAO = mrDivisionDAO;
        this.dsDivisionDAO = dsDivisionDAO;
        this.districtDAO = districtDAO;
        this.locationDAO = locationDAO;
        this.courtDAO = courtDAO;
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void addBDDivision(BDDivision bdDivision, User user) {

        if (isEmptyString(bdDivision.getEnDivisionName()) ||
                isEmptyString(bdDivision.getEnDivisionName()) ||
                isEmptyString(bdDivision.getEnDivisionName())) {
            throw new CRSRuntimeException(
                    "One or more names of the BD Division is invalid - check all languages", ErrorCodes.INVALID_DATA);
        }

        if (user.isAuthorized(Permission.SERVICE_MASTER_DATA_MANAGEMENT)) {
            try {
                bdDivisionDAO.add(bdDivision, user);
            } catch (Exception e) {
                logger.error("Attempt to add BD Division : " + bdDivision.getEnDivisionName() + " failed", e);
            }
        } else {
            logger.error("User : " + user.getUserId() +
                    " was not allowed to add a new BD Division : " + bdDivision.getEnDivisionName());
        }
    }
    @Transactional(propagation = Propagation.REQUIRED)
    public void activateOrInactiveBDDivision(int bdDivisionUKey, boolean active, User user) {
        updateBDActivation(bdDivisionUKey, active, user);
    }

    private void updateBDActivation(int bdDivisionUKey, boolean activate, User user) {

        if (user.isAuthorized(Permission.SERVICE_MASTER_DATA_MANAGEMENT)) {
            try {
                BDDivision existing = bdDivisionDAO.getBDDivisionByPK(bdDivisionUKey);
                if (existing != null) {
                    existing.setActive(activate);
                    bdDivisionDAO.update(existing, user);
                    logger.info("BD Division : {} inactivated by : {}", existing.getEnDivisionName(), user.getUserId());
                }
            } catch (Exception e) {
                logger.error("Attempt to inactivate BD Division with key : " + bdDivisionUKey + " failed", e);
            }
        } else {
            logger.error("User : " + user.getUserId() +
                    " was not allowed to activate/inactivate BD Division with key : " + bdDivisionUKey);
        }
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void addMRDivision(MRDivision mrDivision, User user) {

        if (isEmptyString(mrDivision.getEnDivisionName()) ||
                isEmptyString(mrDivision.getEnDivisionName()) ||
                isEmptyString(mrDivision.getEnDivisionName())) {
            throw new CRSRuntimeException(
                    "One or more names of the MR Division is invalid - check all languages", ErrorCodes.INVALID_DATA);
        }

        if (user.isAuthorized(Permission.SERVICE_MASTER_DATA_MANAGEMENT)) {
            try {
                mrDivisionDAO.add(mrDivision, user);
            } catch (Exception e) {
                logger.error("Attempt to add MR Division : " + mrDivision.getEnDivisionName() + " failed", e);
            }
        } else {
            logger.error("User : " + user.getUserId() +
                    " was not allowed to add a new MR Division : " + mrDivision.getEnDivisionName());
        }
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void activateOrInactivateMRDivision(int mrDivisionUKey, boolean activate, User user) {
        updateMRActivation(mrDivisionUKey, activate, user);
    }

    private void updateMRActivation(int mrDivisionUKey, boolean activate, User user) {
        if (user.isAuthorized(Permission.SERVICE_MASTER_DATA_MANAGEMENT)) {
            try {
                MRDivision existing = mrDivisionDAO.getMRDivisionByPK(mrDivisionUKey);
                if (existing != null) {
                    existing.setActive(activate);
                    mrDivisionDAO.update(existing, user);
                    logger.info("MR Division : {} inactivated by : {}", existing.getEnDivisionName(), user.getUserId());
                }
            } catch (Exception e) {
                logger.error("Attempt to inactivate MR Division with key : " + mrDivisionUKey + " failed", e);
            }
        } else {
            logger.error("User : " + user.getUserId() +
                    " was not allowed to activate/inactivate MR Division with key : " + mrDivisionUKey);
        }
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void addDSDivision(DSDivision dsDivision, User user) {

        if (isEmptyString(dsDivision.getEnDivisionName()) ||
                isEmptyString(dsDivision.getEnDivisionName()) ||
                isEmptyString(dsDivision.getEnDivisionName())) {
            throw new CRSRuntimeException(
                    "One or more names of the DS Division is invalid - check all languages", ErrorCodes.INVALID_DATA);
        }

        if (user.isAuthorized(Permission.SERVICE_MASTER_DATA_MANAGEMENT)) {
            try {
                dsDivisionDAO.add(dsDivision, user);
            } catch (Exception e) {
                logger.error("Attempt to add DS Division : " + dsDivision.getEnDivisionName() + " failed", e);
            }
        } else {
            logger.error("User : " + user.getUserId() +
                    " was not allowed to add a new DS Division : " + dsDivision.getEnDivisionName());
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void activateOrInactivateDSDivision(int dsDivisionUKey, boolean active, User user) {
        updateDSActivation(dsDivisionUKey, active, user);
    }
    private void updateDSActivation(int dsDivisionUKey, boolean activate, User user) {

        if (user.isAuthorized(Permission.SERVICE_MASTER_DATA_MANAGEMENT)) {
            try {
                DSDivision existing = dsDivisionDAO.getDSDivisionByPK(dsDivisionUKey);
                if (existing != null) {
                    existing.setActive(activate);
                    dsDivisionDAO.update(existing, user);
                    logger.info("DS Division : {} " + (activate ? "" : "in-") + "activated by : {}",
                            existing.getEnDivisionName(), user.getUserId());
                }
            } catch (Exception e) {
                logger.error("Attempt to " + (activate ? "" : "in-") + "activate DS Division with key : "
                        + dsDivisionUKey + " failed", e);
            }
        } else {
            logger.error("User : " + user.getUserId() +
                    " was not allowed to activate/inactivate DS Division with key : " + dsDivisionUKey);
        }
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void addDistrict(District district, User user) {

        if (isEmptyString(district.getEnDistrictName()) ||
                isEmptyString(district.getSiDistrictName()) ||
                isEmptyString(district.getTaDistrictName())) {
            throw new CRSRuntimeException(
                    "One or more names of the District is invalid - check all languages", ErrorCodes.INVALID_DATA);
        }

        if (user.isAuthorized(Permission.SERVICE_MASTER_DATA_MANAGEMENT)) {
            try {
                districtDAO.add(district, user);
            } catch (Exception e) {
                logger.error("Attempt to add District : " + district.getEnDistrictName() + " failed", e);
            }
        } else {
            logger.error("User : " + user.getUserId() +
                    " was not allowed to add a new District : " + district.getEnDistrictName());
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void activateOrInactivateDistrict(int districtUKey, boolean active, User user) {
        updateDistrictActivation(districtUKey, active, user);
    }
    private void updateDistrictActivation(int districtUKey, boolean activate, User user) {

        if (user.isAuthorized(Permission.SERVICE_MASTER_DATA_MANAGEMENT)) {
            try {
                District existing = districtDAO.getDistrict(districtUKey);
                if (existing != null) {
                    existing.setActive(activate);
                    districtDAO.update(existing, user);
                    logger.info("District : {} inactivated by : {}", existing.getEnDistrictName(), user.getUserId());
                }
            } catch (Exception e) {
                logger.error("Attempt to inactivate District with key : " + districtUKey + " failed", e);
            }
        } else {
            logger.error("User : " + user.getUserId() +
                    " was not allowed to activate/inactivate District with key : " + districtUKey);
        }
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void addLocation(Location location, User user) {
        if (isEmptyString(location.getEnLocationName()) ||
                isEmptyString(location.getSiLocationName()) ||
                isEmptyString(location.getTaLocationName())) {
            throw new CRSRuntimeException(
                    "One or more names of the Location names is invalid - check all languages", ErrorCodes.INVALID_DATA);
        }

        if (user.isAuthorized(Permission.SERVICE_MASTER_DATA_MANAGEMENT)) {
            try {
                locationDAO.add(location, user);
            } catch (Exception e) {
                logger.error("Attempt to add Location : " + location.getEnLocationName() + " failed", e);
            }
        } else {
            logger.error("User : " + user.getUserId() +
                    " was not allowed to add a new Location : " + location.getEnLocationName());
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void activateOrInactivateLocation(int locationUKey, boolean activate, User user) {
        updateLocationActivation(locationUKey, activate, user);
    }

    private void updateLocationActivation(int locationUKey, boolean activate, User user) {

        if (user.isAuthorized(Permission.SERVICE_MASTER_DATA_MANAGEMENT)) {
            try {
                Location existing = locationDAO.getLocation(locationUKey);
                if (existing != null) {
                    existing.getLifeCycleInfo().setActive(activate);
                    locationDAO.update(existing, user);
                    logger.info("Location : {} " + (activate ? "" : "in-") + "activated by : {}",
                            existing.getEnLocationName(), user.getUserId());
                }
            } catch (Exception e) {
                logger.error("Attempt to " + (activate ? "" : "in-") + "activate Location with key : "
                        + locationUKey + " failed", e);
            }
        } else {
            logger.error("User : " + user.getUserId() +
                    " was not allowed to activate/inactivate Location with key : " + locationUKey);
        }
    }


    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void addCourt(Court court, User user) {
        if (isEmptyString(court.getEnCourtName()) ||
                isEmptyString(court.getSiCourtName()) ||
                isEmptyString(court.getTaCourtName())) {
            throw new CRSRuntimeException(
                    "One or more names of the Court names is invalid - check all languages", ErrorCodes.INVALID_DATA);
        }

        if (user.isAuthorized(Permission.SERVICE_MASTER_DATA_MANAGEMENT)) {
            try {
                courtDAO.add(court, user);
            } catch (Exception e) {
                logger.error("Attempt to add Court : " + court.getEnCourtName() + " failed", e);
            }
        } else {
            logger.error("User : " + user.getUserId() +
                    " was not allowed to add a new Location : " + court.getEnCourtName());
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void activateOrInactivateCourt(int courtUKey, boolean activate, User user) {
        updateCourtActivation(courtUKey, activate, user);
    }

    private void updateCourtActivation(int courtUKey, boolean activate, User user) {
        if (user.isAuthorized(Permission.SERVICE_MASTER_DATA_MANAGEMENT)) {
            try {
                Court existing = courtDAO.getCourt(courtUKey);
                if (existing != null) {
                    existing.setActive(activate);
                    courtDAO.update(existing, user);
                    logger.info("Court : {} " + (activate ? "" : "in-") + "activated by : {}",
                            existing.getEnCourtName(), user.getUserId());
                }
            } catch (Exception e) {
                logger.error("Attempt to " + (activate ? "" : "in-") + "activate court with key : "
                        + courtUKey + " failed", e);
            }
        } else {
            logger.error("User : " + user.getUserId() +
                    " was not allowed to activate/inactivate Location with key : " + courtUKey);
        }
    }

    private static final boolean isEmptyString(String s) {
        return s == null || s.trim().length() == 0;
    }
}
