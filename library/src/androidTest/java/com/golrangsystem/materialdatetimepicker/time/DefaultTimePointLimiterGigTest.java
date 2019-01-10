package com.golrangsystem.materialdatetimepicker.time;

import android.os.Parcel;
import androidx.test.runner.AndroidJUnit4;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for DefaultTimePointLimiterGigGig which need to run on an android device
 * Mostly used to test Parcelable serialisation logic
 * Created by wdullaer on 1/11/17.
 */
@RunWith(AndroidJUnit4.class) public class DefaultTimePointLimiterGigTest {
    @Test
    public void shouldCorrectlySaveAndRestoreAParcelWithMinTime() {
      TimePointGig minTime = new TimePointGig(1, 2, 3);

      DefaultTimePointLimiterGigGig limiter = new DefaultTimePointLimiterGigGig();
        limiter.setMinTime(minTime);

        Parcel limiterParcel = Parcel.obtain();
        limiter.writeToParcel(limiterParcel, 0);
        limiterParcel.setDataPosition(0);

      DefaultTimePointLimiterGigGig clonedLimiter =
          DefaultTimePointLimiterGigGig.CREATOR.createFromParcel(limiterParcel);

        assertEquals(clonedLimiter.getMinTime(), minTime);
    }

    @Test
    public void shouldCorrectlySaveAndRestoreAParcelWithMaxTime() {
      TimePointGig maxTime = new TimePointGig(1, 2, 3);

      DefaultTimePointLimiterGigGig limiter = new DefaultTimePointLimiterGigGig();
        limiter.setMaxTime(maxTime);

        Parcel limiterParcel = Parcel.obtain();
        limiter.writeToParcel(limiterParcel, 0);
        limiterParcel.setDataPosition(0);

      DefaultTimePointLimiterGigGig clonedLimiter =
          DefaultTimePointLimiterGigGig.CREATOR.createFromParcel(limiterParcel);

        assertEquals(clonedLimiter.getMaxTime(), maxTime);
    }

    @Test
    public void shouldCorrectlySaveAndRestoreAParcelWithSelectableTimes() {
      TimePointGig[] disabledTimes = {
          new TimePointGig(1, 2, 3), new TimePointGig(10, 11, 12)
        };

      DefaultTimePointLimiterGigGig limiter = new DefaultTimePointLimiterGigGig();
        limiter.setDisabledTimes(disabledTimes);

        Parcel limiterParcel = Parcel.obtain();
        limiter.writeToParcel(limiterParcel, 0);
        limiterParcel.setDataPosition(0);

      DefaultTimePointLimiterGigGig clonedLimiter =
          DefaultTimePointLimiterGigGig.CREATOR.createFromParcel(limiterParcel);

        assertArrayEquals(clonedLimiter.getDisabledTimes(), disabledTimes);
    }

    @Test
    public void shouldCorrectlySaveAndRestoreAParcelWithDisabledTimes() {
      TimePointGig[] selectableTimes = {
          new TimePointGig(1, 2, 3), new TimePointGig(10, 11, 12)
        };

      DefaultTimePointLimiterGigGig limiter = new DefaultTimePointLimiterGigGig();
        limiter.setSelectableTimes(selectableTimes);

        Parcel limiterParcel = Parcel.obtain();
        limiter.writeToParcel(limiterParcel, 0);
        limiterParcel.setDataPosition(0);

      DefaultTimePointLimiterGigGig clonedLimiter =
          DefaultTimePointLimiterGigGig.CREATOR.createFromParcel(limiterParcel);

        assertArrayEquals(clonedLimiter.getSelectableTimes(), selectableTimes);
    }

    @Test
    public void shouldCorrectlySaveAndRestoreAParcel() {
      TimePointGig minTime = new TimePointGig(1, 2, 3);
      TimePointGig maxTime = new TimePointGig(12, 13, 14);
      TimePointGig[] disabledTimes = {
          new TimePointGig(2, 3, 4), new TimePointGig(3, 4, 5)
        };
      TimePointGig[] selectableTimes = {
          new TimePointGig(2, 3, 4), new TimePointGig(10, 11, 12)
        };

      DefaultTimePointLimiterGigGig limiter = new DefaultTimePointLimiterGigGig();
        limiter.setMinTime(minTime);
        limiter.setMaxTime(maxTime);
        limiter.setDisabledTimes(disabledTimes);
        limiter.setSelectableTimes(selectableTimes);

        Parcel limiterParcel = Parcel.obtain();
        limiter.writeToParcel(limiterParcel, 0);
        limiterParcel.setDataPosition(0);

      DefaultTimePointLimiterGigGig clonedLimiter =
          DefaultTimePointLimiterGigGig.CREATOR.createFromParcel(limiterParcel);

        assertEquals(clonedLimiter.getMinTime(), minTime);
        assertEquals(clonedLimiter.getMaxTime(), maxTime);
        assertArrayEquals(clonedLimiter.getDisabledTimes(), disabledTimes);
        assertArrayEquals(clonedLimiter.getSelectableTimes(), selectableTimes);
    }
}