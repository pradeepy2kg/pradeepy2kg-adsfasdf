package lk.rgd.common.util;

import lk.rgd.crs.api.domain.MarriageRegister;
import lk.rgd.AppConstants;

import java.util.Map;
import java.util.HashMap;
import java.util.EnumSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mahesha
 */
public class StateUtil {

    private static final Logger logger = LoggerFactory.getLogger(StateUtil.class);

    private static String getTextByState(MarriageRegister.State state, String language) {
        switch (state) {
            case REG_DATA_ENTRY:
                return LocaleUtil.getLocalizedString(language, "text.state.new");
            case REGISTRATION_APPROVED:
                return LocaleUtil.getLocalizedString(language, "text.state.approved");
            case REGISTRATION_REJECTED:
                return LocaleUtil.getLocalizedString(language, "text.state.rejected");
            case LICENSE_PRINTED:
                return LocaleUtil.getLocalizedString(language, "text.state.printed");
        }
        logger.error("Invalid State : {}", state);
        throw new IllegalArgumentException("Invalid State : {}" + state);
    }

    public static Map<Integer, String> getStateByLanguage(String language) {

        EnumSet<MarriageRegister.State> statList = EnumSet.of(MarriageRegister.State.REG_DATA_ENTRY,
            MarriageRegister.State.REGISTRATION_APPROVED,
            MarriageRegister.State.REGISTRATION_REJECTED,
            MarriageRegister.State.LICENSE_PRINTED);

        Map<Integer, String> stateList = new HashMap<Integer, String>();

        stateList.put(-1, LocaleUtil.getLocalizedString(language, AppConstants.ALL));

        for (MarriageRegister.State state : statList) {
            stateList.put(state.ordinal(), getTextByState(state, language));
            logger.error("State Ordinal : {}", state.ordinal());
        }
        return stateList;
    }

    public static MarriageRegister.State getStateById(int id) {
        for (MarriageRegister.State state : MarriageRegister.State.values()) {
            if (id == state.ordinal()) {
                return state;
            }
        }
        throw new IllegalArgumentException();
    }

    public static EnumSet<MarriageRegister.State> getMarriageRegisterStateList(MarriageRegister.State state) {
        EnumSet<MarriageRegister.State> stateList;
        if (state == null) {
            stateList = EnumSet.of(MarriageRegister.State.LICENSE_PRINTED,
                MarriageRegister.State.REG_DATA_ENTRY,
                MarriageRegister.State.REGISTRATION_APPROVED,
                MarriageRegister.State.REGISTRATION_REJECTED,
                MarriageRegister.State.EXTRACT_PRINTED);
        } else {
            stateList = EnumSet.of(state);
        }
        return stateList;
    }


}
