package lk.rgd.crs.core.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ResourceBundle;
import java.util.ArrayList;
import java.util.List;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import lk.rgd.AppConstants;
import lk.rgd.ErrorCodes;
import lk.rgd.common.api.domain.Race;
import lk.rgd.common.api.domain.Country;
import lk.rgd.crs.CRSRuntimeException;
import lk.rgd.crs.api.domain.DeathAlteration;
import lk.rgd.crs.api.domain.DeathRegister;

/**
 * basic back end valiation class for death alterations
 *
 * @author amith jayasekara
 */
public class DeathAlterationValidator {
    private static final Logger logger = LoggerFactory.getLogger(DeathAlterationValidator.class);

    private static final ResourceBundle rb_si =
            ResourceBundle.getBundle("messages/death_validation_messages", AppConstants.LK_SI);
    private static final ResourceBundle rb_ta =
            ResourceBundle.getBundle("messages/death_validation_messages", AppConstants.LK_TA);
    private static final ResourceBundle rb_en =
            ResourceBundle.getBundle("messages/death_validation_messages", AppConstants.LK_EN);

    public void validateMinimulCondiations(DeathAlteration deathAlteration, DeathRegister deathRegister) {
        boolean pass = compareObjects(deathAlteration, deathRegister);
        if (!pass)
            handleException("incomplete death alteration ", ErrorCodes.INCOMPLETE_ALTERATION);
    }

    private boolean compareObjects(DeathAlteration deathAlteration, DeathRegister deathRegister) {
        DateFormat df = new SimpleDateFormat("yyyy-mm-dd");
        String dateEx = null;
        String dateAlt = null;
        if (deathRegister.getDeath().getDateOfDeath() != null)
            dateEx = df.format(deathRegister.getDeath().getDateOfDeath());
        if (deathAlteration.getDeathInfo().getDateOfDeath() != null)
            dateAlt = df.format(deathAlteration.getDeathInfo().getDateOfDeath());

        if (checkAlteration(dateEx, dateAlt, 0))
            return true;
        if (checkAlteration(deathRegister.getDeath().getTimeOfDeath(), deathAlteration.getDeathInfo().getTimeOfDeath(), 0))
            return true;
        if (checkAlteration(deathRegister.getDeath().getPlaceOfDeath(), deathAlteration.getDeathInfo().getPlaceOfDeath(), 0))
            return true;
        if (checkAlteration(deathRegister.getDeath().getPlaceOfDeathInEnglish(), deathAlteration.getDeathInfo().getPlaceOfDeathInEnglish(), 0))
            return true;
        if (checkAlteration(deathRegister.getDeath().isCauseOfDeathEstablished(), deathAlteration.getDeathInfo().isCauseOfDeathEstablished(), 3))
            return true;
        if (checkAlteration(deathRegister.getDeath().getCauseOfDeath(), deathAlteration.getDeathInfo().getCauseOfDeath(), 0))
            return true;
        if (checkAlteration(deathRegister.getDeath().getIcdCodeOfCause(), deathAlteration.getDeathInfo().getIcdCodeOfCause(), 0))
            return true;
        if (checkAlteration(deathRegister.getDeath().getPlaceOfBurial(), deathAlteration.getDeathInfo().getPlaceOfBurial(), 0))
            return true;
        if (checkAlteration(deathRegister.getDeathPerson().getDeathPersonCountry(), deathAlteration.getDeathPerson().getDeathPersonCountry(), 4))
            return true;
        if (checkAlteration(deathRegister.getDeathPerson().getDeathPersonPassportNo(), deathAlteration.getDeathPerson().getDeathPersonPassportNo(), 0))
            return true;
        if (checkAlteration(deathRegister.getDeathPerson().getDeathPersonAge(), deathAlteration.getDeathPerson().getDeathPersonAge(), 1))
            return true;
        if (checkAlteration(deathRegister.getDeathPerson().getDeathPersonGender(), deathAlteration.getDeathPerson().getDeathPersonGender(), 1))
            return true;
        if (checkAlteration(deathRegister.getDeathPerson().getDeathPersonRace(), deathAlteration.getDeathPerson().getDeathPersonRace(), 6))
            return true;
        if (checkAlteration(deathRegister.getDeathPerson().getDeathPersonNameOfficialLang(), deathAlteration.getDeathPerson().getDeathPersonNameOfficialLang(), 0))
            return true;
        if (checkAlteration(deathRegister.getDeathPerson().getDeathPersonNameInEnglish(), deathAlteration.getDeathPerson().getDeathPersonNameInEnglish(), 0))
            return true;
        if (checkAlteration(deathRegister.getDeathPerson().getDeathPersonPermanentAddress(), deathAlteration.getDeathPerson().getDeathPersonPermanentAddress(), 0))
            return true;
        if (checkAlteration(deathRegister.getDeathPerson().getDeathPersonFatherPINorNIC(), deathAlteration.getDeathPerson().getDeathPersonFatherPINorNIC(), 0))
            return true;
        if (checkAlteration(deathRegister.getDeathPerson().getDeathPersonFatherFullName(), deathAlteration.getDeathPerson().getDeathPersonFatherFullName(), 0))
            return true;
        if (checkAlteration(deathRegister.getDeathPerson().getDeathPersonMotherPINorNIC(), deathAlteration.getDeathPerson().getDeathPersonMotherPINorNIC(), 0))
            return true;
        if (checkAlteration(deathRegister.getDeathPerson().getDeathPersonMotherFullName(), deathAlteration.getDeathPerson().getDeathPersonMotherFullName(), 0))
            return true;
        return false;
    }


    private boolean checkAlteration(Object deathRegistreValue, Object deathAlterationValue, int type) {
        switch (type) {
            case 0:
                if (deathAlterationValue != null & deathRegistreValue != null) {
                    if (compareStiring((String) deathRegistreValue, (String) deathAlterationValue)) {
                        return true;
                    }
                } else {
                    if (!(deathAlterationValue == null || deathAlterationValue.toString().length() == 0) &
                            (deathRegistreValue == null || deathRegistreValue.toString().length() == 0))
                        return true;
                }
                break;
            case 1:
                if (deathAlterationValue != null & deathRegistreValue != null) {
                    if (compareInteger((Integer) deathRegistreValue, (Integer) deathAlterationValue)) {
                        return true;
                    }
                } else {
                    if (!(deathAlterationValue == null & deathRegistreValue == null))
                        return true;
                }
                break;
            case 2:
                if (deathAlterationValue != null & deathRegistreValue != null) {
                    if (compareLong((Long) deathRegistreValue, (Long) deathAlterationValue)) {
                        return true;
                    }
                } else {
                    if (!(deathAlterationValue == null & deathRegistreValue == null))
                        return true;
                }
                break;
            case 3:
                if (deathAlterationValue != null & deathRegistreValue != null) {
                    if (compareBoolean((Boolean) deathRegistreValue, (Boolean) deathAlterationValue)) {
                        return true;
                    }
                } else {
                    if (!(deathAlterationValue == null & deathRegistreValue == null))
                        return true;
                }
                break;
            case 4:

                Country ex = (Country) deathRegistreValue;
                Country cu = (Country) deathAlterationValue;
                if (ex != null & cu != null) {
                    if (compareCountry(ex, cu)) {
                        return true;
                    }
                } else {
                    if (!(ex == null & cu == null)) {
                        return true;
                    }
                }
                break;
            case 6:

                Race exRace = (Race) deathRegistreValue;
                Race cuRace = (Race) deathAlterationValue;
                if (exRace != null & cuRace != null) {
                    if (compareRaces((Race) deathRegistreValue, (Race) deathAlterationValue)) {
                        return true;
                    }
                } else {
                    if (!(exRace == null & cuRace == null)) {
                        return true;
                    }
                }
                break;
        }
        return false;
    }

    private boolean compareStiring(String exsisting, String current) {
        if (current != null) {
            int value = exsisting.compareTo(current.trim());
            if (value == 0) {
                return false;
            } else {
                return true;
            }
        }
        return true;
    }

    private boolean compareInteger(Integer ex, Integer cu) {
        if (ex.compareTo(cu) == 0) {
            return false;
        } else {
            return true;
        }
    }

    private boolean compareBoolean(Boolean ex, Boolean cu) {
        if (ex.compareTo(cu) == 0) {
            return false;
        } else {
            return true;
        }
    }

    private boolean compareLong(Long ex, Long cu) {
        if (ex.compareTo(cu) == 0)
            return false;
        else {
            return true;
        }
    }

    private boolean compareCountry(Country ex, Country cu) {
        if (ex.getCountryId() == cu.getCountryId())
            return false;
        return true;
    }

    private boolean compareRaces(Race ex, Race cu) {
        if (ex.getRaceId() == cu.getRaceId())
            return false;
        return true;
    }

    private static void handleException(String msg, int errorCode) {
        logger.error(msg);
        throw new CRSRuntimeException(msg, errorCode);
    }
}
