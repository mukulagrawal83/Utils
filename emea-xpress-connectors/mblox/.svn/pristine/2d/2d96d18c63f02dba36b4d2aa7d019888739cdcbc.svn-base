package com.fisglobal.xpress.emea.mblox;

import org.apache.log4j.Logger;

import javax.jms.JMSException;
import java.util.Calendar;
import java.util.Date;

/**
 * @author e1050475
 */
public class NotificationServiceImpl implements NotificationService {

    private static final String COLON = ":";
    private BlackOutTime globalBlackOutTime;
    private static final Logger LOGGER = Logger.getLogger(NotificationServiceImpl.class);


    public NotificationMessage sendSMS(NotificationMessage textMessage) throws NotificationMessageException, JMSException {
        LOGGER.debug("Validating message");
        //Validate the message details
        NotificationValidator.validateTextMessage(textMessage);

        LOGGER.debug("applying blackout message");
        //Apply global blackout
        applyBlackTimePeriod(textMessage, this.globalBlackOutTime);
        LOGGER.debug("returning message");
        return textMessage;
    }

    public BlackOutTime getGlobalBlackOutTime() {
        return globalBlackOutTime;
    }

    public void setGlobalBlackOutTime(BlackOutTime globalBlackOutTime) {
        this.globalBlackOutTime = globalBlackOutTime;
    }

    private void applyBlackTimePeriod(NotificationMessage notificationMessage, BlackOutTime blackOutTime) {
        BlackOutTimePeriod blackOutTimePeriod = null;
        Calendar calendar = Calendar.getInstance();
        if (notificationMessage.getProperties().containsKey(NotificationMessage.BLACK_OUT_PERIOD)) {
            //apply message specific blackout time
            blackOutTimePeriod = computedTime(calendar, (BlackOutTime) notificationMessage.getProperties().get(NotificationMessage.BLACK_OUT_PERIOD));
        } else {
            //apply global blackout timer period
            blackOutTimePeriod = computedTime(calendar, blackOutTime);
        }
        notificationMessage.getProperties().remove(NotificationMessage.BLACK_OUT_PERIOD);
        notificationMessage.getProperties().put(NotificationMessage.BLACK_OUT_FROM, blackOutTimePeriod.getPeriodFrom().getTime());
        notificationMessage.getProperties().put(NotificationMessage.BLACK_OUT_TO, blackOutTimePeriod.getPeriodTo().getTime());
    }

    private BlackOutTimePeriod computedTime(Calendar calendar, BlackOutTime blackOutTime) {
        BlackOutTimePeriod blackOutTimePeriod = new BlackOutTimePeriod();
        if (blackOutTime.isOverLap()) {
            String[] blackOutPattern = blackOutTime.getFromPeriod().split(COLON);
            calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(blackOutPattern[0]));
            calendar.set(Calendar.MINUTE, Integer.parseInt(blackOutPattern[1]));
            calendar.set(Calendar.SECOND, 0);
            blackOutTimePeriod.setPeriodFrom(calendar.getTime());

            blackOutPattern = blackOutTime.getToPeriod().split(COLON);
            if (isLastDayOfMonth(calendar.getTime())) {
                calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(blackOutPattern[0]));
                calendar.set(Calendar.MINUTE, Integer.parseInt(blackOutPattern[1]));
                calendar.set(Calendar.DAY_OF_MONTH, 1);
                calendar.set(Calendar.SECOND, 0);
                calendar.add(Calendar.MONTH, 1);
            } else {
                calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(blackOutPattern[0]));
                calendar.set(Calendar.MINUTE, Integer.parseInt(blackOutPattern[1]));
                calendar.add(Calendar.DAY_OF_MONTH, 1);
                calendar.set(Calendar.SECOND, 0);
            }
            blackOutTimePeriod.setPeriodTo(calendar.getTime());
        } else {
            String[] blackOutPattern = blackOutTime.getFromPeriod().split(COLON);
            calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(blackOutPattern[0]));
            calendar.set(Calendar.MINUTE, Integer.parseInt(blackOutPattern[1]));
            calendar.set(Calendar.SECOND, 0);
            blackOutTimePeriod.setPeriodFrom(calendar.getTime());

            blackOutPattern = blackOutTime.getToPeriod().split(COLON);
            calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(blackOutPattern[0]));
            calendar.set(Calendar.MINUTE, Integer.parseInt(blackOutPattern[1]));
            blackOutTimePeriod.setPeriodTo(calendar.getTime());
        }
        return blackOutTimePeriod;
    }

    private boolean isLastDayOfMonth(Date currentDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        calendar.set(Calendar.DATE, calendar.getMaximum(Calendar.DATE));
        int maxDay = calendar.get(Calendar.DAY_OF_MONTH);
        return day == maxDay;
    }

    private class BlackOutTimePeriod {
        private Date periodFrom;
        private Date periodTo;

        public Date getPeriodFrom() {
            return periodFrom;
        }

        public void setPeriodFrom(Date periodFrom) {
            this.periodFrom = periodFrom;
        }

        public Date getPeriodTo() {
            return periodTo;
        }

        public void setPeriodTo(Date periodTo) {
            this.periodTo = periodTo;
        }
    }
}
