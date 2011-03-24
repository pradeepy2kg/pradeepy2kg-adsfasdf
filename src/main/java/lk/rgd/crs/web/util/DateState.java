package lk.rgd.crs.web.util;

/**
 * @author Chathuranga Withana
 */
public enum DateState {
    BD_OK(0),       /** 0 - no issue in late or belated registrations */
    BD_LATE(1),     /** 1 - late birth declaration */
    BD_BELATED(2),  /** 2 - belated birth declaration */
    ERROR(3);       /** 3 - date difference is minus */

    private final int stateId;

    DateState(int stateId) {
        this.stateId = stateId;
    }

    public int getStateId() {
        return stateId;
    }
}
