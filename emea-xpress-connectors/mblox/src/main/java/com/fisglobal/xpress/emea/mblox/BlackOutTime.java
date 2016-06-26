package com.fisglobal.xpress.emea.mblox;


import org.apache.commons.lang.StringUtils;

/**
 * @author e1050475
 */
public class BlackOutTime {

    private static final String COLON = ":";
    private static final String TIMEPERIOD_INVALID = "timeperiod.invalid";
    private static final String TIME_PERIOD_IS_INVALID = "time period is invalid";

    private boolean overLap = false;
    private String fromPeriod;
    private String toPeriod;

    /**
     * Format of time should be for example 21:00
     *
     * @param fromPeriod
     * @param toPeriod
     * @throws NotificationMessageException
     */
    public BlackOutTime(String fromPeriod, String toPeriod) throws NotificationMessageException {
        this.fromPeriod = fromPeriod;
        this.toPeriod = toPeriod;
        validateField(fromPeriod);
        validateField(toPeriod);
        String[] fromTimePeriod = fromPeriod.split(COLON);
        String[] toTimePeriod = toPeriod.split(COLON);
        if (Integer.parseInt(fromTimePeriod[0]) > Integer.parseInt(toTimePeriod[0])) {
            overLap = true;
        }
    }

    public String getFromPeriod() {
        return fromPeriod;
    }


    public String getToPeriod() {
        return toPeriod;
    }

    private void validateField(String timePeriod) throws NotificationMessageException {
        if (StringUtils.isBlank(timePeriod)) {
            throw new NotificationMessageException(TIMEPERIOD_INVALID, "time period can't be empty");
        }
        if (!timePeriod.contains(COLON)) {
            throw new NotificationMessageException(TIMEPERIOD_INVALID, TIME_PERIOD_IS_INVALID);
        }
        String[] period = timePeriod.split(COLON);
        try {
            if (Integer.parseInt(period[0]) < 0 || Integer.parseInt(period[0]) > 23) {
                throw new NotificationMessageException(TIMEPERIOD_INVALID, TIME_PERIOD_IS_INVALID);
            }
            if (Integer.parseInt(period[1]) < 0 || Integer.parseInt(period[1]) > 59) {
                throw new NotificationMessageException(TIMEPERIOD_INVALID, TIME_PERIOD_IS_INVALID);
            }

        } catch (NumberFormatException nfe) {
            throw new NotificationMessageException(TIMEPERIOD_INVALID, TIME_PERIOD_IS_INVALID);
        }
    }

    public boolean isOverLap() {
        return overLap;
    }
}
