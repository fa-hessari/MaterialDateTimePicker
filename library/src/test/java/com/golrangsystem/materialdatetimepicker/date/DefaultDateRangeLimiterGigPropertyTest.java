package com.golrangsystem.materialdatetimepicker.date;

import com.golrangsystem.materialdatetimepicker.UtilsGig;
import com.pholser.junit.quickcheck.Property;
import com.pholser.junit.quickcheck.generator.InRange;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import org.junit.Assert;
import org.junit.runner.RunWith;

/**
 * Property based tests for DefaultDateRangeLimiterGig (quickcheck FTW!)
 * Created by wdullaer on 26/04/17.
 */

@RunWith(JUnitQuickcheck.class) public class DefaultDateRangeLimiterGigPropertyTest {
  final private DatePickerControllerGig controller = new DatePickerControllerGig() {
        @Override
        public void onYearSelected(int year) {}

        @Override
        public void onDayOfMonthSelected(int year, int month, int day) {}

        @Override public void registerOnDateChangedListener(
            DatePickerDialogGig.OnDateChangedListener listener) {
        }

        @Override public void unregisterOnDateChangedListener(
            DatePickerDialogGig.OnDateChangedListener listener) {
        }

        @Override public MonthAdapterGig.CalendarDay getSelectedDay() {
          return new MonthAdapterGig.CalendarDay(Calendar.getInstance(), TimeZone.getDefault());
        }

        @Override
        public boolean isThemeDark() {
            return false;
        }

        @Override
        public int getAccentColor() {
            return 0;
        }

        @Override
        public boolean isHighlighted(int year, int month, int day) {
            return false;
        }

        @Override
        public int getFirstDayOfWeek() {
            return 0;
        }

        @Override
        public int getMinYear() {
            return 0;
        }

        @Override
        public int getMaxYear() {
            return 0;
        }

        @Override
        public Calendar getStartDate() {
            return Calendar.getInstance();
        }

        @Override
        public Calendar getEndDate() {
            return Calendar.getInstance();
        }

        @Override
        public boolean isOutOfRange(int year, int month, int day) {
            return false;
        }

        @Override
        public void tryVibrate() {}

        @Override
        public TimeZone getTimeZone() {
            return TimeZone.getDefault();
        }

        @Override
        public Locale getLocale() {
            return Locale.getDefault();
        }

        @Override public DatePickerDialogGig.Version getVersion() {
          return DatePickerDialogGig.Version.VERSION_2;
        }

        @Override public DatePickerDialogGig.ScrollOrientation getScrollOrientation() {
          return DatePickerDialogGig.ScrollOrientation.HORIZONTAL;
        }
    };

    private static Calendar[] datesToCalendars(Date[] dates) {
        Calendar[] output = new Calendar[dates.length];
        Calendar day = Calendar.getInstance();
        for (int i = 0; i < dates.length; i++) {
            Calendar cal = (Calendar) day.clone();
            cal.setTime(dates[i]);
            output[i] = cal;
        }
        return output;
    }

    @Property
    public void setToNearestShouldBeInSelectableDays(
            @InRange(min = "01/01/1900", max = "12/31/2099", format = "MM/dd/yyyy") Date date,
            @InRange(min = "01/01/1900", max = "12/31/2099", format = "MM/dd/yyyy") Date[] dates
    ) {
      DefaultDateRangeLimiterGig limiter = new DefaultDateRangeLimiterGig();

        Calendar day = Calendar.getInstance();
        day.setTime(date);

        Calendar[] selectables = datesToCalendars(dates);

        limiter.setSelectableDays(selectables);

        // selectableDays are manipulated a bit by the limiter
        selectables = limiter.getSelectableDays();

        // selectables == null when the input is empty
        if (selectables == null) Assert.assertEquals(
                day.getTimeInMillis(),
                limiter.setToNearestDate(day).getTimeInMillis()
        );
        else Assert.assertTrue(Arrays.asList(selectables).contains(limiter.setToNearestDate(day)));
    }

    @Property
    public void setToNearestShouldNeverBeInDisabledDays(
            @InRange(min = "01/01/1900", max = "12/31/2099", format = "MM/dd/yyyy") Date date,
            @InRange(min = "01/01/1900", max = "12/31/2099", format = "MM/dd/yyyy") Date[] dates
    ) {
      DefaultDateRangeLimiterGig limiter = new DefaultDateRangeLimiterGig();

        Calendar day = Calendar.getInstance();
        day.setTime(date);

        Calendar[] disableds = datesToCalendars(dates);

        limiter.setDisabledDays(disableds);
        Assert.assertFalse(Arrays.asList(disableds).contains(limiter.setToNearestDate(day)));
    }

    @Property
    public void setToNearestShouldNeverBeBelowMinDate(
            @InRange(min = "01/01/1900", max = "12/31/2099", format = "MM/dd/yyyy") Date date,
            @InRange(min = "01/01/1900", max = "12/31/2099", format = "MM/dd/yyyy") Date minDate
    ) {
      DefaultDateRangeLimiterGig limiter = new DefaultDateRangeLimiterGig();

        Calendar day = Calendar.getInstance();
        day.setTime(date);

        Calendar minDay = Calendar.getInstance();
        minDay.setTime(minDate);

        limiter.setMinDate(minDay);
      Assert.assertTrue(
          UtilsGig.trimToMidnight(minDay).getTimeInMillis() <= limiter.setToNearestDate(day)
              .getTimeInMillis());
    }

    @Property
    public void setToNearestShouldNeverBeAboveMaxDate(
            @InRange(min = "01/01/1800", max = "12/31/2099", format = "MM/dd/yyyy") Date date,
            @InRange(min = "01/01/1800", max = "12/31/2099", format = "MM/dd/yyyy") Date maxDate
    ) {
      DefaultDateRangeLimiterGig limiter = new DefaultDateRangeLimiterGig();

        Calendar day = Calendar.getInstance();
        day.setTime(date);

        Calendar minDay = Calendar.getInstance();
        minDay.set(Calendar.YEAR, 1800);
        minDay.set(Calendar.MONTH, Calendar.JANUARY);
        minDay.set(Calendar.DAY_OF_MONTH, 1);
      UtilsGig.trimToMidnight(minDay);

        Calendar maxDay = Calendar.getInstance();
        maxDay.setTime(maxDate);

        limiter.setMinDate(minDay);
        limiter.setMaxDate(maxDay);
      Assert.assertTrue(
          UtilsGig.trimToMidnight(maxDay).getTimeInMillis() >= limiter.setToNearestDate(day)
              .getTimeInMillis());
    }

    // TODO write property based tests that enable all options as one
    // TODO ensure generators cover more of the known edge cases in the inputs
}
