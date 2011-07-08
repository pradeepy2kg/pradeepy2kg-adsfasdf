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
import lk.rgd.crs.api.dao.*;
import lk.rgd.crs.api.domain.BDDivision;
import lk.rgd.crs.api.domain.Court;
import lk.rgd.crs.api.domain.GNDivision;
import lk.rgd.crs.api.domain.MRDivision;
import lk.rgd.crs.api.service.MasterDataManagementService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
    private final GNDivisionDAO gnDivisionDAO;
    private final BirthDeclarationDAO birthDeclarationDAO;
    private final DeathRegisterDAO deathRegisterDAO;
    private final MarriageRegistrationDAO marriageRegistrationDAO;
    private final AdoptionOrderDAO adoptionOrderDAO;

    public MasterDataManagementServiceImpl(BDDivisionDAO bdDivisionDAO, MRDivisionDAO mrDivisionDAO,
        DSDivisionDAO dsDivisionDAO, DistrictDAO districtDAO, LocationDAO locationDAO, CourtDAO courtDAO,
        GNDivisionDAO gnDivisionDAO, BirthDeclarationDAO birthDeclarationDAO, DeathRegisterDAO deathRegisterDAO,
        MarriageRegistrationDAO marriageRegistrationDAO, AdoptionOrderDAO adoptionOrderDAO) {
        this.bdDivisionDAO = bdDivisionDAO;
        this.mrDivisionDAO = mrDivisionDAO;
        this.dsDivisionDAO = dsDivisionDAO;
        this.districtDAO = districtDAO;
        this.locationDAO = locationDAO;
        this.courtDAO = courtDAO;
        this.gnDivisionDAO = gnDivisionDAO;
        this.birthDeclarationDAO = birthDeclarationDAO;
        this.deathRegisterDAO = deathRegisterDAO;
        this.marriageRegistrationDAO = marriageRegistrationDAO;
        this.adoptionOrderDAO = adoptionOrderDAO;
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void addBDDivision(BDDivision bdDivision, User user) {
        validateMinimalRequirements(bdDivision);

        if (user.isAuthorized(Permission.SERVICE_MASTER_DATA_MANAGEMENT)) {
            try {
                List<BDDivision> divisions = bdDivisionDAO.getBDDivisionByCode(bdDivision.getDivisionId(),
                    bdDivision.getDsDivision());
                bdDivision.setActive(divisions.isEmpty());
                bdDivisionDAO.add(bdDivision, user);
            } catch (Exception e) {
                logger.error("Attempt to add BD Division : " + bdDivision.getEnDivisionName() + " failed", e);
            }
        } else {
            logger.error("User : " + user.getUserId() +
                " was not allowed to add a new BD Division : " + bdDivision.getEnDivisionName());
        }
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateBDDivision(int bdDivisionUKey, BDDivision bdDivision, User user) {
        validateMinimalRequirements(bdDivision);

        if (user.isAuthorized(Permission.SERVICE_MASTER_DATA_MANAGEMENT)) {
            logger.debug("Attempt to edit BD Division with key : {}", bdDivisionUKey);
            // validate BD Division usage before edit
            if (!isEligibleToUpdateBDDivision(bdDivisionUKey)) {
                handleException("BD Division with key : " + bdDivisionUKey + " have mapping db records.",
                    ErrorCodes.ILLEGAL_STATE);
            }
            BDDivision current = bdDivisionDAO.getBDDivisionByPK(bdDivisionUKey);

            if (current != null) {
                current.setEnDivisionName(bdDivision.getEnDivisionName());
                current.setSiDivisionName(bdDivision.getSiDivisionName());
                current.setTaDivisionName(bdDivision.getTaDivisionName());
                bdDivisionDAO.update(current, user);
                logger.info("BD Division with key : {} updated successfully", bdDivisionUKey);
            }
        } else {
            logger.error("User : {} was not allowed to edit BD Division with key : {}", user.getUserId(),
                bdDivisionUKey);
        }
    }

    private void validateMinimalRequirements(BDDivision bdDivision) {
        if (isEmptyString(bdDivision.getEnDivisionName()) ||
            isEmptyString(bdDivision.getSiDivisionName()) ||
            isEmptyString(bdDivision.getTaDivisionName())) {
            throw new CRSRuntimeException(
                "One or more names of the BD Division is invalid - check all languages", ErrorCodes.INVALID_DATA);
        }
    }

    private boolean isEligibleToUpdateBDDivision(int bdDivisionUKey) {
        long size = birthDeclarationDAO.findBDDivisionUsageInBirthRecords(bdDivisionUKey);
        size += deathRegisterDAO.findBDDivisionUsageInDeathRecords(bdDivisionUKey);
        // TODO complete this
        return size == 0;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void addGNDivision(GNDivision gnDivision, User user) {
        validateMinimalRequirements(gnDivision);

        if (user.isAuthorized(Permission.SERVICE_MASTER_DATA_MANAGEMENT)) {
            try {
                List<GNDivision> divisions = gnDivisionDAO.getGNDivisionByCodeAndDSDivision(
                    gnDivision.getGnDivisionId(), gnDivision.getDsDivision());
                gnDivision.setActive(divisions.isEmpty());
                gnDivisionDAO.add(gnDivision, user);
                logger.debug("successfully added GNDivision with code number : {}", gnDivision.getGnDivisionId());
            } catch (Exception e) {
                logger.error("Attempt to add GN Division : " + gnDivision.getEnGNDivisionName() + " failed", e);
            }
        } else {
            logger.error("User : " + user.getUserId() + " was not allowed to add a new GN Division : " +
                gnDivision.getEnGNDivisionName());
        }
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateGNDivision(int gnDivisionUKey, GNDivision gnDivision, User user) {
        validateMinimalRequirements(gnDivision);

        if (user.isAuthorized(Permission.SERVICE_MASTER_DATA_MANAGEMENT)) {
            logger.debug("Attempt to edit GN Division with key : {}", gnDivisionUKey);
            // validate GN Division usage before edit
            if (!isEligibleToUpdateGNDivision(gnDivisionUKey)) {
                handleException("GN Division with key : " + gnDivisionUKey + " have mapping db records.",
                    ErrorCodes.ILLEGAL_STATE);
            }

            GNDivision current = gnDivisionDAO.getGNDivisionByPK(gnDivisionUKey);

            if (current != null) {
                current.setEnGNDivisionName(gnDivision.getEnGNDivisionName());
                current.setSiGNDivisionName(gnDivision.getSiGNDivisionName());
                current.setTaGNDivisionName(gnDivision.getTaGNDivisionName());
                gnDivisionDAO.update(current, user);
                logger.info("GN Division with key : {} updated successfully", gnDivisionUKey);
            }
        } else {
            logger.error("User : {} was not allowed to edit GN Division with key : {}", user.getUserId(),
                gnDivisionUKey);
        }
    }

    private void validateMinimalRequirements(GNDivision gnDivision) {
        if (isEmptyString(gnDivision.getEnGNDivisionName()) ||
            isEmptyString(gnDivision.getSiGNDivisionName()) ||
            isEmptyString(gnDivision.getTaGNDivisionName())) {
            throw new CRSRuntimeException(
                "One or more names of the GN Division is invalid - check all languages", ErrorCodes.INVALID_DATA);
        }
    }

    private boolean isEligibleToUpdateGNDivision(int gnDivisionUKey) {
        long size = birthDeclarationDAO.findGNDivisionUsageInBirthRecords(gnDivisionUKey);
        return size > 0 ? false : deathRegisterDAO.findGNDivisionUsageInDeathRecords(gnDivisionUKey) == 0;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void activateOrInactiveGNDivision(int gnDivisionUKey, boolean active, User user) {
        updateGNActivation(gnDivisionUKey, active, user);
    }

    private void updateGNActivation(int gnDivisionUKey, boolean activate, User user) {

        if (user.isAuthorized(Permission.SERVICE_MASTER_DATA_MANAGEMENT)) {
            try {
                GNDivision existing = gnDivisionDAO.getGNDivisionByPK(gnDivisionUKey);
                if (existing != null) {
                    existing.setActive(activate);
                    gnDivisionDAO.update(existing, user);
                    logger.info("GN Division : {} " + (activate ? "" : "in-") + "activated by : {}",
                        existing.getEnGNDivisionName(), user.getUserId());
                }
            } catch (Exception e) {
                logger.error("Attempt to " + (activate ? "" : "in-") + "activate GN Division with key : " +
                    gnDivisionUKey + " failed", e);
            }
        } else {
            logger.error("User : " + user.getUserId() +
                " was not allowed to activate/inactivate BD Division with key : " + gnDivisionUKey);
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
        validateMinimalRequirements(mrDivision);

        if (user.isAuthorized(Permission.SERVICE_MASTER_DATA_MANAGEMENT)) {
            try {
                List<MRDivision> divisions = mrDivisionDAO.getMRDivisionByCode(mrDivision.getDivisionId(),
                    mrDivision.getDsDivision());
                mrDivision.setActive(divisions.isEmpty());

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
    public void updateMRDivision(int mrDivisionUKey, MRDivision mrDivision, User user) {
        validateMinimalRequirements(mrDivision);

        if (user.isAuthorized(Permission.SERVICE_MASTER_DATA_MANAGEMENT)) {
            logger.debug("Attempt to edit MR Division with key : {}", mrDivisionUKey);
            // validate MR Division usage before edit
            if (!isEligibleToUpdateMRDivision(mrDivisionUKey)) {
                handleException("MR Division with key : " + mrDivisionUKey + " have mapping db records.",
                    ErrorCodes.ILLEGAL_STATE);
            }
            MRDivision current = mrDivisionDAO.getMRDivisionByPK(mrDivisionUKey);

            if (current != null) {
                current.setEnDivisionName(mrDivision.getEnDivisionName());
                current.setSiDivisionName(mrDivision.getSiDivisionName());
                current.setTaDivisionName(mrDivision.getTaDivisionName());
                mrDivisionDAO.update(current, user);
                logger.info("MR Division with key : {} updated successfully", current.getMrDivisionUKey());
            }
        } else {
            logger.error("User : {} was not allowed to edit MR Division : {}", user.getUserId(), mrDivisionUKey);
        }
    }

    private void validateMinimalRequirements(MRDivision mrDivision) {
        if (isEmptyString(mrDivision.getEnDivisionName()) ||
            isEmptyString(mrDivision.getSiDivisionName()) ||
            isEmptyString(mrDivision.getTaDivisionName())) {
            throw new CRSRuntimeException(
                "One or more names of the MR Division is invalid - check all languages", ErrorCodes.INVALID_DATA);
        }
    }

    private boolean isEligibleToUpdateMRDivision(int mrDivisionUKey) {
        long size = marriageRegistrationDAO.findMRDivisionUsageInMarriageRecords(mrDivisionUKey);
        return size == 0;
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
        validateMinimalRequirements(dsDivision);

        if (user.isAuthorized(Permission.SERVICE_MASTER_DATA_MANAGEMENT)) {
            try {
                List<DSDivision> divisions = dsDivisionDAO.getDSDivisionByCode(dsDivision.getDivisionId(),
                    dsDivision.getDistrict());
                dsDivision.setActive(divisions.isEmpty());
                dsDivisionDAO.add(dsDivision, user);
            } catch (Exception e) {
                logger.error("Attempt to add DS Division : " + dsDivision.getEnDivisionName() + " failed", e);
            }
        } else {
            logger.error("User : " + user.getUserId() +
                " was not allowed to add a new DS Division : " + dsDivision.getEnDivisionName());
        }
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateDSDivision(DSDivision dsDivision, User user) {
        validateMinimalRequirements(dsDivision);

        final int dsDivisionUKey = dsDivision.getDsDivisionUKey();
        if (user.isAuthorized(Permission.SERVICE_MASTER_DATA_MANAGEMENT)) {

            logger.debug("Attempt to edit DS Division with key : {}", dsDivisionUKey);
            // validate DS Division usage before edit
            if (!isEligibleToUpdateDSDivision(dsDivisionUKey)) {
                handleException("DS Division with key : " + dsDivisionUKey + " have mapping db records.",
                    ErrorCodes.ILLEGAL_STATE);
            }
            DSDivision current = dsDivisionDAO.getDSDivisionByPK(dsDivision.getDsDivisionUKey());

            if (current != null) {
                current.setEnDivisionName(dsDivision.getEnDivisionName());
                current.setSiDivisionName(dsDivision.getSiDivisionName());
                current.setTaDivisionName(dsDivision.getTaDivisionName());
                dsDivisionDAO.update(current, user);
                logger.info("DS Division with key : {} updated successfully", dsDivisionUKey);
            }
        } else {
            logger.error("User : " + user.getUserId() + " was not allowed to edit DS Division with key: " +
                dsDivisionUKey);
        }
    }

    private void validateMinimalRequirements(DSDivision dsDivision) {
        if (isEmptyString(dsDivision.getEnDivisionName()) ||
            isEmptyString(dsDivision.getSiDivisionName()) ||
            isEmptyString(dsDivision.getTaDivisionName())) {
            throw new CRSRuntimeException(
                "One or more names of the DS Division is invalid - check all languages", ErrorCodes.INVALID_DATA);
        }
    }

    private boolean isEligibleToUpdateDSDivision(int dsDivisionUKey) {
        // TODO not complete
        long size = birthDeclarationDAO.findDSDivisionUsageInBirthRecords(dsDivisionUKey);
        return size == 0;
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
        validateMinimalRequirements(location);

        if (user.isAuthorized(Permission.SERVICE_MASTER_DATA_MANAGEMENT)) {
            try {
                List<Location> locations = locationDAO.getLocationByCodeAndByDSDivisionID(location.getLocationCode(),
                    location.getDsDivisionId());
                location.getLifeCycleInfo().setActive(locations.isEmpty());

                locationDAO.add(location, user);
            } catch (Exception e) {
                logger.error("Attempt to add Location : " + location.getEnLocationName() + " failed", e);
            }
        } else {
            logger.error("User : " + user.getUserId() +
                " was not allowed to add a new Location : " + location.getEnLocationName());
        }
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateLocation(int locationUKey, Location location, User user) {
        validateMinimalRequirements(location);

        if (user.isAuthorized(Permission.SERVICE_MASTER_DATA_MANAGEMENT)) {
            logger.debug("Attempt to edit Location with key : {}", locationUKey);
            // validate Location usage before edit
            if (!isEligibleToUpdateLocation(locationUKey)) {
                handleException("Location with key : " + locationUKey + " have mapping db records.",
                    ErrorCodes.ILLEGAL_STATE);
            }
            Location current = locationDAO.getLocation(locationUKey);

            if (current != null) {
                current.setSiLocationName(location.getSiLocationName());
                current.setEnLocationName(location.getEnLocationName());
                current.setTaLocationName(location.getTaLocationName());
                locationDAO.update(current, user);
                logger.info("Location with key : {} updated successfully", locationUKey);
            }
        } else {
            logger.error("User : {} was not allowed to edit Location with key : ", user.getUserId(), locationUKey);
        }
    }

    private void validateMinimalRequirements(Location location) {
        if (isEmptyString(location.getLocationCode())) {
            throw new CRSRuntimeException("Location code is empty", ErrorCodes.INVALID_DATA);
        }
        if (isEmptyString(location.getEnLocationName()) ||
            isEmptyString(location.getSiLocationName()) ||
            isEmptyString(location.getTaLocationName())) {
            throw new CRSRuntimeException(
                "One or more names of the Location names is invalid - check all languages", ErrorCodes.INVALID_DATA);
        }
    }

    private boolean isEligibleToUpdateLocation(int locationUKey) {
        // TODO not complete
        long size = birthDeclarationDAO.findLocationUsageInBirthRecords(locationUKey);
        size += deathRegisterDAO.findLocationUsageInDeathRecords(locationUKey);
        size += marriageRegistrationDAO.findLocationUsageInMarriageRecords(locationUKey);
        return size == 0;
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
        validateMinimalRequirements(court);

        if (user.isAuthorized(Permission.SERVICE_MASTER_DATA_MANAGEMENT)) {
            try {
                courtDAO.add(court, user);
            } catch (Exception e) {
                logger.error("Attempt to add Court : " + court.getEnCourtName() + " failed", e);
            }
        } else {
            logger.error("User : " + user.getUserId() + " was not allowed to add a new Court : " +
                court.getEnCourtName());
        }
    }

    /**
     * @inheritDoc
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateCourt(int courtUKey, Court court, User user) {
        validateMinimalRequirements(court);

        if (user.isAuthorized(Permission.SERVICE_MASTER_DATA_MANAGEMENT)) {
            logger.debug("Attempt to edit Court with key : {}", courtUKey);
            // validate Court usage before edit
            if (!isEligibleToUpdateCourt(courtUKey)) {
                handleException("Court with key : " + courtUKey + " have mapping db records.",
                    ErrorCodes.ILLEGAL_STATE);
            }
            Court current = courtDAO.getCourt(courtUKey);

            if (current != null) {
                current.setEnCourtName(court.getEnCourtName());
                current.setSiCourtName(court.getSiCourtName());
                current.setTaCourtName(court.getTaCourtName());
                courtDAO.update(current, user);
                logger.info("Court with key : {} updated successfully", court.getCourtUKey());
            }
        } else {
            logger.error("User : {} was not allowed to edit Court with key : {}", user.getUserId(),
                court.getCourtUKey());
        }
    }

    private void validateMinimalRequirements(Court court) {
        if (isEmptyString(court.getEnCourtName()) ||
            isEmptyString(court.getSiCourtName()) ||
            isEmptyString(court.getTaCourtName())) {
            throw new CRSRuntimeException(
                "One or more names of the Court names is invalid - check all languages", ErrorCodes.INVALID_DATA);
        }
    }

    private boolean isEligibleToUpdateCourt(int courtUKey) {
        return adoptionOrderDAO.findCourtUsageInAdoptions(courtUKey) == 0;
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

    private void handleException(String message, int code) {
        logger.error(message);
        throw new CRSRuntimeException(message, code);
    }

    private static final boolean isEmptyString(String s) {
        return s == null || s.trim().length() == 0;
    }
}
