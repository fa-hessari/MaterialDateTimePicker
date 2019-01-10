package com.golrangsystem.materialdatetimepicker.time;

import org.junit.Assert;
import org.junit.Test;

import static com.golrangsystem.materialdatetimepicker.time.TimePickerDialogGIg.HOUR_INDEX;
import static com.golrangsystem.materialdatetimepicker.time.TimePickerDialogGIg.MINUTE_INDEX;

/**
 * Unit tests for the default implementation of TimePointLimiterGig
 * Mostly used to assert that the rounding logic works
 *
 * Created by wdullaer on 22/06/17.
 */
public class DefaultTimePointLimiterGigTest {
    @Test
    public void isAmDisabledShouldReturnTrueWhenMinTimeIsInTheAfternoon() {
      TimePointGig minTime = new TimePointGig(13);
      DefaultTimePointLimiterGigGig limiter = new DefaultTimePointLimiterGigGig();

        limiter.setMinTime(minTime);

        Assert.assertTrue(limiter.isAmDisabled());
    }

    @Test
    public void isAmDisabledShouldReturnFalseWhenMinTimeIsInTheMorning() {
      TimePointGig minTime = new TimePointGig(8);
      DefaultTimePointLimiterGigGig limiter = new DefaultTimePointLimiterGigGig();

        limiter.setMinTime(minTime);

        Assert.assertFalse(limiter.isAmDisabled());
    }

    @Test
    public void isAmDisabledShouldReturnTrueWhenMinTimeIsMidday() {
      TimePointGig minTime = new TimePointGig(12);
      DefaultTimePointLimiterGigGig limiter = new DefaultTimePointLimiterGigGig();

        limiter.setMinTime(minTime);

        Assert.assertTrue(limiter.isAmDisabled());
    }

    @Test
    public void isAmDisabledShouldReturnFalseWhenMaxTimeIsInTheMorning() {
      TimePointGig maxTime = new TimePointGig(8);
      DefaultTimePointLimiterGigGig limiter = new DefaultTimePointLimiterGigGig();

        limiter.setMaxTime(maxTime);

        Assert.assertFalse(limiter.isAmDisabled());
    }

    @Test
    public void isAmDisabledShouldReturnFalseWhenMaxTimeIsMidday() {
      TimePointGig maxTime = new TimePointGig(12);
      DefaultTimePointLimiterGigGig limiter = new DefaultTimePointLimiterGigGig();

        limiter.setMaxTime(maxTime);

        Assert.assertFalse(limiter.isAmDisabled());
    }

    @Test
    public void isPmDisabledShouldReturnTrueWhenMaxTimeIsInTheMorning() {
      TimePointGig maxTime = new TimePointGig(9);
      DefaultTimePointLimiterGigGig limiter = new DefaultTimePointLimiterGigGig();

        limiter.setMaxTime(maxTime);

        Assert.assertTrue(limiter.isPmDisabled());
    }

    @Test
    public void isPmDisabledShouldReturnFalseWhenMinTimeIsInTheAfternoon() {
      TimePointGig minTime = new TimePointGig(13);
      DefaultTimePointLimiterGigGig limiter = new DefaultTimePointLimiterGigGig();

        limiter.setMinTime(minTime);

        Assert.assertFalse(limiter.isPmDisabled());
    }

    @Test
    public void isPmDisabledShouldReturnFalseWhenMaxTimeIsMidday() {
      TimePointGig maxTime = new TimePointGig(12);
      DefaultTimePointLimiterGigGig limiter = new DefaultTimePointLimiterGigGig();

        limiter.setMaxTime(maxTime);

        Assert.assertFalse(limiter.isPmDisabled());
    }

    @Test
    public void isAmDisabledShouldReturnTrueIfSelectableDaysAreInTheAfternoon() {
      TimePointGig[] selectableDays = {
          new TimePointGig(13), new TimePointGig(22)
        };
      DefaultTimePointLimiterGigGig limiter = new DefaultTimePointLimiterGigGig();

        limiter.setSelectableTimes(selectableDays);

        Assert.assertTrue(limiter.isAmDisabled());
    }

    @Test
    public void isAmDisabledShouldReturnFalseIfSelectableDaysHasOneTimeInTheMorning() {
      TimePointGig[] selectableDays = {
          new TimePointGig(4), new TimePointGig(13), new TimePointGig(22)
        };
      DefaultTimePointLimiterGigGig limiter = new DefaultTimePointLimiterGigGig();

        limiter.setSelectableTimes(selectableDays);

        Assert.assertFalse(limiter.isAmDisabled());
    }

    @Test
    public void isPmDisabledShouldReturnTrueIfSelectableDaysAreInTheMorning() {
      TimePointGig[] selectableDays = {
          new TimePointGig(4), new TimePointGig(9), new TimePointGig(11)
        };
      DefaultTimePointLimiterGigGig limiter = new DefaultTimePointLimiterGigGig();

        limiter.setSelectableTimes(selectableDays);

        Assert.assertTrue(limiter.isPmDisabled());
    }

    @Test
    public void isPmDisabledShouldReturnFalseIfSelectableDaysHasOneTimeInTheAfternoon() {
      TimePointGig[] selectableDays = {
          new TimePointGig(4), new TimePointGig(22)
        };
      DefaultTimePointLimiterGigGig limiter = new DefaultTimePointLimiterGigGig();

        limiter.setSelectableTimes(selectableDays);

        Assert.assertFalse(limiter.isPmDisabled());
    }

    @Test
    public void isPmDisabledShouldReturnFalseIfSelectableDaysContainsMidday() {
      TimePointGig[] selectableDays = {
          new TimePointGig(4), new TimePointGig(9), new TimePointGig(12)
        };
      DefaultTimePointLimiterGigGig limiter = new DefaultTimePointLimiterGigGig();

        limiter.setSelectableTimes(selectableDays);

        Assert.assertFalse(limiter.isPmDisabled());
    }

    @Test
    public void isPmDisabledShouldReturnFalseWithoutConstraints() {
      DefaultTimePointLimiterGigGig limiter = new DefaultTimePointLimiterGigGig();

        Assert.assertFalse(limiter.isPmDisabled());
    }

    @Test
    public void isAmDisabledShouldReturnFalseWithoutConstraints() {
      DefaultTimePointLimiterGigGig limiter = new DefaultTimePointLimiterGigGig();

        Assert.assertFalse(limiter.isAmDisabled());
    }

    @Test
    public void setMinTimeShouldThrowExceptionWhenBiggerThanMaxTime() {
      TimePointGig maxTime = new TimePointGig(2);
      TimePointGig minTime = new TimePointGig(3);
      DefaultTimePointLimiterGigGig limiter = new DefaultTimePointLimiterGigGig();

        limiter.setMaxTime(maxTime);

        try {
            limiter.setMinTime(minTime);
            Assert.fail("setMinTime() should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            Assert.assertNotNull(e);
        }
    }

    @Test
    public void setMaxTimeShouldThrowExceptionWhenSmallerThanMinTime() {
      TimePointGig maxTime = new TimePointGig(2);
      TimePointGig minTime = new TimePointGig(3);
      DefaultTimePointLimiterGigGig limiter = new DefaultTimePointLimiterGigGig();

        limiter.setMinTime(minTime);

        try {
            limiter.setMaxTime(maxTime);
            Assert.fail("setMaxTime() should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            Assert.assertNotNull(e);
        }
    }

    @Test
    public void isOutOfRangeShouldReturnTrueIfInputSmallerThanMinTime() {
      TimePointGig minTime = new TimePointGig(10);
      TimePointGig input = new TimePointGig(2);
      DefaultTimePointLimiterGigGig limiter = new DefaultTimePointLimiterGigGig();

        limiter.setMinTime(minTime);

        Assert.assertTrue(limiter.isOutOfRange(input));
    }

    @Test
    public void isOutOfRangeShouldReturnTrueIfInputLargerThanMaxTime() {
      TimePointGig maxTime = new TimePointGig(2);
      TimePointGig input = new TimePointGig(12);
      DefaultTimePointLimiterGigGig limiter = new DefaultTimePointLimiterGigGig();

        limiter.setMaxTime(maxTime);

        Assert.assertTrue(limiter.isOutOfRange(input));
    }

    @Test
    public void isOutOfRangeShouldReturnFalseIfInputIsBetweenMinAndMaxTime() {
      TimePointGig minTime = new TimePointGig(1);
      TimePointGig maxTime = new TimePointGig(13);
      TimePointGig input = new TimePointGig(4);
      DefaultTimePointLimiterGigGig limiter = new DefaultTimePointLimiterGigGig();

        limiter.setMinTime(minTime);
        limiter.setMaxTime(maxTime);

        Assert.assertFalse(limiter.isOutOfRange(input));
    }

    @Test
    public void isOutOfRangeShouldReturnFalseWithoutRestraints() {
      TimePointGig input = new TimePointGig(14);
      DefaultTimePointLimiterGigGig limiter = new DefaultTimePointLimiterGigGig();

        Assert.assertFalse(limiter.isOutOfRange(input));
    }

    @Test
    public void isOutOfRangeShouldReturnTrueIfInputNotSelectable() {
      TimePointGig input = new TimePointGig(1);
      TimePointGig[] selectableDays = {
          new TimePointGig(13), new TimePointGig(14)
        };
      DefaultTimePointLimiterGigGig limiter = new DefaultTimePointLimiterGigGig();

        limiter.setSelectableTimes(selectableDays);

        Assert.assertTrue(limiter.isOutOfRange(input));
    }

    @Test
    public void isOutOfRangeShouldReturnFalseIfInputSelectable() {
      TimePointGig input = new TimePointGig(15);
      TimePointGig[] selectableDays = {
          new TimePointGig(4), new TimePointGig(10), new TimePointGig(15)
        };
      DefaultTimePointLimiterGigGig limiter = new DefaultTimePointLimiterGigGig();

        limiter.setSelectableTimes(selectableDays);

        Assert.assertFalse(limiter.isOutOfRange(input));
    }

    @Test
    public void isOutOfRangeWithIndexShouldHandleNull() {
      DefaultTimePointLimiterGigGig limiter = new DefaultTimePointLimiterGigGig();

      Assert.assertFalse(limiter.isOutOfRange(null, HOUR_INDEX, TimePointGig.TYPE.SECOND));
    }

    @Test
    public void isOutOfRangeMinuteShouldReturnFalseWhenMinTimeEqualsToTheMinute() {
      TimePointGig minTime = new TimePointGig(12, 13, 14);
      TimePointGig input = new TimePointGig(12, 13);
      DefaultTimePointLimiterGigGig limiter = new DefaultTimePointLimiterGigGig();

        limiter.setMinTime(minTime);

      Assert.assertFalse(limiter.isOutOfRange(input, MINUTE_INDEX, TimePointGig.TYPE.SECOND));
    }

    @Test
    public void isOutOfRangeMinuteShouldReturnFalseWhenMaxTimeEqualsToTheMinute() {
      TimePointGig maxTime = new TimePointGig(12, 13, 14);
      TimePointGig input = new TimePointGig(12, 13);
      DefaultTimePointLimiterGigGig limiter = new DefaultTimePointLimiterGigGig();

        limiter.setMaxTime(maxTime);

      Assert.assertFalse(limiter.isOutOfRange(input, MINUTE_INDEX, TimePointGig.TYPE.SECOND));
    }

    @Test
    public void isOutOfRangeMinuteShouldReturnFalseWhenTimeEqualsSelectableTimeToTheMinute() {
      TimePointGig[] selectableTimes = {
          new TimePointGig(11, 12, 13), new TimePointGig(12, 13, 14), new TimePointGig(13, 14, 15)
        };
      TimePointGig input = new TimePointGig(12, 13);
      DefaultTimePointLimiterGigGig limiter = new DefaultTimePointLimiterGigGig();

        limiter.setSelectableTimes(selectableTimes);

      Assert.assertFalse(limiter.isOutOfRange(input, MINUTE_INDEX, TimePointGig.TYPE.SECOND));
    }

    @Test
    public void isOutOfRangeMinuteShouldReturnFalseWhenTimeEqualsSelectableTimeToTheMinute2() {
      TimePointGig[] selectableTimes = {
          new TimePointGig(11, 12, 13), new TimePointGig(12, 13, 14), new TimePointGig(13, 14, 15)
        };
      TimePointGig input = new TimePointGig(13, 14, 59);
      DefaultTimePointLimiterGigGig limiter = new DefaultTimePointLimiterGigGig();

        limiter.setSelectableTimes(selectableTimes);

      Assert.assertFalse(limiter.isOutOfRange(input, MINUTE_INDEX, TimePointGig.TYPE.SECOND));
    }

    @Test
    public void isOutOfRangeMinuteShouldReturnFalseWhenTimeEqualsSelectableTimeToTheMinute3() {
      TimePointGig[] selectableTimes = {
          new TimePointGig(11, 12, 13), new TimePointGig(12, 13, 14), new TimePointGig(13, 14, 15)
        };
      TimePointGig input = new TimePointGig(11, 12, 0);
      DefaultTimePointLimiterGigGig limiter = new DefaultTimePointLimiterGigGig();

        limiter.setSelectableTimes(selectableTimes);

      Assert.assertFalse(limiter.isOutOfRange(input, MINUTE_INDEX, TimePointGig.TYPE.SECOND));
    }

    @Test
    public void isOutOfRangeMinuteShouldReturnTrueWhenTimeDoesNotEqualSelectableTimeToTheMinute() {
      TimePointGig[] selectableTimes = {
          new TimePointGig(11, 12, 13), new TimePointGig(12, 13, 14), new TimePointGig(13, 14, 15)
        };
      TimePointGig input = new TimePointGig(11, 11, 0);
      DefaultTimePointLimiterGigGig limiter = new DefaultTimePointLimiterGigGig();

        limiter.setSelectableTimes(selectableTimes);

      Assert.assertTrue(limiter.isOutOfRange(input, MINUTE_INDEX, TimePointGig.TYPE.SECOND));
    }

    @Test
    public void isOutOfRangeHourShouldReturnFalseWhenMinTimeEqualsToTheHour() {
      TimePointGig minTime = new TimePointGig(12, 13, 14);
      TimePointGig input = new TimePointGig(12);
      DefaultTimePointLimiterGigGig limiter = new DefaultTimePointLimiterGigGig();

        limiter.setMinTime(minTime);

      Assert.assertFalse(limiter.isOutOfRange(input, HOUR_INDEX, TimePointGig.TYPE.SECOND));
    }

    @Test
    public void isOutOfRangeHourShouldReturnFalseWhenMaxTimeEqualsToTheHour() {
      TimePointGig maxTime = new TimePointGig(12, 13, 14);
      TimePointGig input = new TimePointGig(12);
      DefaultTimePointLimiterGigGig limiter = new DefaultTimePointLimiterGigGig();

        limiter.setMaxTime(maxTime);

      Assert.assertFalse(limiter.isOutOfRange(input, HOUR_INDEX, TimePointGig.TYPE.SECOND));
    }

    @Test
    public void isOutOfRangeHourShouldReturnFalseWhenTimeEqualsSelectableTimeToTheHour() {
      TimePointGig[] selectableTimes = {
          new TimePointGig(11, 12, 13), new TimePointGig(12, 13, 14), new TimePointGig(13, 14, 15)
        };
      TimePointGig input = new TimePointGig(12);
      DefaultTimePointLimiterGigGig limiter = new DefaultTimePointLimiterGigGig();

        limiter.setSelectableTimes(selectableTimes);

      Assert.assertFalse(limiter.isOutOfRange(input, HOUR_INDEX, TimePointGig.TYPE.SECOND));
    }

    @Test
    public void isOutOfRangeHourShouldReturnFalseWhenTimeEqualsSelectableTimeToTheHour2() {
      TimePointGig[] selectableTimes = {
          new TimePointGig(11, 12, 13), new TimePointGig(12, 13, 14), new TimePointGig(13, 14, 15)
        };
      TimePointGig input = new TimePointGig(13, 15, 15);
      DefaultTimePointLimiterGigGig limiter = new DefaultTimePointLimiterGigGig();

        limiter.setSelectableTimes(selectableTimes);

      Assert.assertFalse(limiter.isOutOfRange(input, HOUR_INDEX, TimePointGig.TYPE.SECOND));
    }

    @Test
    public void isOutOfRangeHourShouldReturnFalseWhenTimeEqualsSelectableTimeToTheHour3() {
      TimePointGig[] selectableTimes = {
          new TimePointGig(11, 12, 13), new TimePointGig(12, 13, 14), new TimePointGig(13, 14, 15)
        };
      TimePointGig input = new TimePointGig(11);
      DefaultTimePointLimiterGigGig limiter = new DefaultTimePointLimiterGigGig();

        limiter.setSelectableTimes(selectableTimes);

      Assert.assertFalse(limiter.isOutOfRange(input, HOUR_INDEX, TimePointGig.TYPE.SECOND));
    }

    @Test
    public void isOutOfRangeShouldWorkWhenSelectableTimesContainsDuplicateEntries() {
      TimePointGig[] selectableTimes = {
          new TimePointGig(11), new TimePointGig(12), new TimePointGig(12), new TimePointGig(13)
        };
      TimePointGig input = new TimePointGig(11, 30);
      DefaultTimePointLimiterGigGig limiter = new DefaultTimePointLimiterGigGig();

        limiter.setSelectableTimes(selectableTimes);

        Assert.assertTrue(limiter.isOutOfRange(input));
    }

    @Test
    public void isOutOfRangeShouldReturnTrueWhenInputIsInDisabledTimes() {
      TimePointGig[] disabledTimes = {
          new TimePointGig(11), new TimePointGig(12), new TimePointGig(13)
        };
      TimePointGig input = disabledTimes[0];
      DefaultTimePointLimiterGigGig limiter = new DefaultTimePointLimiterGigGig();

        limiter.setDisabledTimes(disabledTimes);

        Assert.assertTrue(limiter.isOutOfRange(input));
    }

    @Test
    public void isOutOfRangeShouldUseDisabledOverSelectable() {
      TimePointGig[] disabledTimes = {
          new TimePointGig(11), new TimePointGig(12), new TimePointGig(13)
        };
      TimePointGig[] selectableTimes = {
          new TimePointGig(12), new TimePointGig(14)
        };
      TimePointGig input = new TimePointGig(12);
      DefaultTimePointLimiterGigGig limiter = new DefaultTimePointLimiterGigGig();

        limiter.setDisabledTimes(disabledTimes);
        limiter.setSelectableTimes(selectableTimes);

        Assert.assertTrue(limiter.isOutOfRange(input));
    }

    @Test
    public void isOutOfRangeHourShouldReturnFalseWhenADisabledTimeIsTestedWithSecondResolution() {
        // If there are only disabledTimes, there will still be other times that are valid
      TimePointGig[] disabledTimes = {
          new TimePointGig(12), new TimePointGig(13), new TimePointGig(14)
        };
      TimePointGig input = disabledTimes[0];
      DefaultTimePointLimiterGigGig limiter = new DefaultTimePointLimiterGigGig();

        limiter.setDisabledTimes(disabledTimes);

      Assert.assertFalse(limiter.isOutOfRange(input, HOUR_INDEX, TimePointGig.TYPE.SECOND));
    }

    @Test
    public void isOutOfRangeHourShouldReturnFalseWhenADisabledTimeIsTestedWithMinuteResolution() {
        // If there are only disabledTimes, there will still be other times that are valid
      TimePointGig[] disabledTimes = {
          new TimePointGig(12), new TimePointGig(13), new TimePointGig(14)
        };
      TimePointGig input = disabledTimes[0];
      DefaultTimePointLimiterGigGig limiter = new DefaultTimePointLimiterGigGig();

        limiter.setDisabledTimes(disabledTimes);

      Assert.assertFalse(limiter.isOutOfRange(input, HOUR_INDEX, TimePointGig.TYPE.MINUTE));
    }

    @Test
    public void isOutOfRangeHourShouldReturnTrueWhenADisabledTimeIsTestedWithHourResolution() {
        // If there are only disabledTimes, there will still be other times that are valid
      TimePointGig[] disabledTimes = {
          new TimePointGig(12), new TimePointGig(13), new TimePointGig(14)
        };
      TimePointGig input = disabledTimes[0];
      DefaultTimePointLimiterGigGig limiter = new DefaultTimePointLimiterGigGig();

        limiter.setDisabledTimes(disabledTimes);

      Assert.assertTrue(limiter.isOutOfRange(input, HOUR_INDEX, TimePointGig.TYPE.HOUR));
    }

    @Test
    public void isOutOfRangeMinuteShouldReturnFalseWhenADisabledTimeIsTestedWithSecondResolution() {
        // If there are only disabledTimes, there will still be other times that are valid
      TimePointGig[] disabledTimes = {
          new TimePointGig(12, 15), new TimePointGig(13, 16), new TimePointGig(14, 17)
        };
      TimePointGig input = disabledTimes[0];
      DefaultTimePointLimiterGigGig limiter = new DefaultTimePointLimiterGigGig();

        limiter.setDisabledTimes(disabledTimes);

      Assert.assertFalse(limiter.isOutOfRange(input, MINUTE_INDEX, TimePointGig.TYPE.SECOND));
    }

    @Test
    public void isOutOfRangeMinuteShouldReturnFalseWhenADisabledTimeIsTestedWithMinuteResolution() {
        // If there are only disabledTimes, there will still be other times that are valid
      TimePointGig[] disabledTimes = {
          new TimePointGig(12, 15), new TimePointGig(13, 16), new TimePointGig(14, 17)
        };
      TimePointGig input = disabledTimes[0];
      DefaultTimePointLimiterGigGig limiter = new DefaultTimePointLimiterGigGig();

        limiter.setDisabledTimes(disabledTimes);

      Assert.assertTrue(limiter.isOutOfRange(input, MINUTE_INDEX, TimePointGig.TYPE.MINUTE));
    }

    @Test
    public void isOutOfRangeHourShouldReturnTrueWhenADisabledTimeCancelsASelectableTime() {
      TimePointGig[] disabledTimes = {
          new TimePointGig(12), new TimePointGig(13)
        };
      TimePointGig[] selectableTimes = {
          new TimePointGig(12), new TimePointGig(14), new TimePointGig(15)
        };
      TimePointGig input = new TimePointGig(12);
      DefaultTimePointLimiterGigGig limiter = new DefaultTimePointLimiterGigGig();

        limiter.setDisabledTimes(disabledTimes);
        limiter.setSelectableTimes(selectableTimes);

      Assert.assertTrue(limiter.isOutOfRange(input, HOUR_INDEX, TimePointGig.TYPE.SECOND));
    }

    @Test
    public void roundToNearestShouldWorkWhenSelectableTimesContainsDuplicateEntries() {
      TimePointGig[] selectableTimes = {
          new TimePointGig(11), new TimePointGig(12), new TimePointGig(12), new TimePointGig(13)
        };
      TimePointGig input = new TimePointGig(12, 29);
      TimePointGig expected = new TimePointGig(12);
      DefaultTimePointLimiterGigGig limiter = new DefaultTimePointLimiterGigGig();

        limiter.setSelectableTimes(selectableTimes);

      Assert.assertEquals(limiter.roundToNearest(input, null, TimePointGig.TYPE.SECOND), expected);
    }

    @Test
    public void roundToNearestShouldReturnMaxTimeIfBiggerThanMaxTime() {
      TimePointGig maxTime = new TimePointGig(8);
      TimePointGig input = new TimePointGig(12);
      DefaultTimePointLimiterGigGig limiter = new DefaultTimePointLimiterGigGig();

        limiter.setMaxTime(maxTime);

      Assert.assertEquals(limiter.roundToNearest(input, null, TimePointGig.TYPE.SECOND), maxTime);
    }

    @Test
    public void roundToNearestShouldReturnMinTimeIfSmallerThanMinTime() {
      TimePointGig minTime = new TimePointGig(8);
      TimePointGig input = new TimePointGig(7);
      DefaultTimePointLimiterGigGig limiter = new DefaultTimePointLimiterGigGig();

        limiter.setMinTime(minTime);

      Assert.assertEquals(limiter.roundToNearest(input, null, TimePointGig.TYPE.SECOND), minTime);
    }

    @Test
    public void roundToNearestShouldReturnInputIfNotOutOfRange() {
      TimePointGig input = new TimePointGig(12, 13, 14);
      DefaultTimePointLimiterGigGig limiter = new DefaultTimePointLimiterGigGig();

      Assert.assertEquals(limiter.roundToNearest(input, null, TimePointGig.TYPE.SECOND), input);
    }

    @Test
    public void roundToNearestShouldReturnSelectableTime() {
      TimePointGig[] selectableTimes = {
          new TimePointGig(11, 12, 14), new TimePointGig(12, 13, 14), new TimePointGig(13, 14, 15)
        };
      TimePointGig input = new TimePointGig(11);
      DefaultTimePointLimiterGigGig limiter = new DefaultTimePointLimiterGigGig();

        limiter.setSelectableTimes(selectableTimes);

      Assert.assertEquals(limiter.roundToNearest(input, null, TimePointGig.TYPE.SECOND),
          selectableTimes[0]);
    }

    @Test
    public void roundToNearestShouldNotChangeTheMinutesWhenOptionIsSet() {
      TimePointGig[] selectableTimes = {
          new TimePointGig(11, 12, 13), new TimePointGig(12), new TimePointGig(12, 13, 14),
          new TimePointGig(15, 16, 17)
        };
      TimePointGig input = new TimePointGig(11, 12, 59);
      DefaultTimePointLimiterGigGig limiter = new DefaultTimePointLimiterGigGig();

        limiter.setSelectableTimes(selectableTimes);

      Assert.assertEquals(
          limiter.roundToNearest(input, TimePointGig.TYPE.MINUTE, TimePointGig.TYPE.SECOND)
              .getHour(), input.getHour());
      Assert.assertEquals(
          limiter.roundToNearest(input, TimePointGig.TYPE.MINUTE, TimePointGig.TYPE.SECOND)
              .getMinute(), input.getMinute());
    }

    @Test
    public void roundToNearestShouldNotChangeTheHourWhenOptionIsSet() {
      TimePointGig[] selectableTimes = {
          new TimePointGig(11, 12, 13), new TimePointGig(12), new TimePointGig(12, 13, 14),
          new TimePointGig(13), new TimePointGig(15, 16, 17)
        };
      TimePointGig input = new TimePointGig(12, 59, 59);
      DefaultTimePointLimiterGigGig limiter = new DefaultTimePointLimiterGigGig();

        limiter.setSelectableTimes(selectableTimes);

      Assert.assertEquals(
          limiter.roundToNearest(input, TimePointGig.TYPE.HOUR, TimePointGig.TYPE.SECOND).getHour(),
          input.getHour());
    }

    @Test
    public void roundToNearestShouldNotChangeTheHourWhenOptionIsSet2() {
      TimePointGig[] selectableTimes = {
          new TimePointGig(11, 12, 13), new TimePointGig(12), new TimePointGig(12, 13, 14),
          new TimePointGig(13), new TimePointGig(15, 16, 17)
        };
      TimePointGig input = new TimePointGig(15, 59, 59);
      DefaultTimePointLimiterGigGig limiter = new DefaultTimePointLimiterGigGig();

        limiter.setSelectableTimes(selectableTimes);

      Assert.assertEquals(
          limiter.roundToNearest(input, TimePointGig.TYPE.HOUR, TimePointGig.TYPE.SECOND).getHour(),
          input.getHour());
    }

    @Test
    public void roundToNearestShouldNotChangeAnythingWhenSecondOptionIsSet() {
      TimePointGig[] selectableTimes = {
          new TimePointGig(11, 12, 13), new TimePointGig(12), new TimePointGig(12, 13, 14),
          new TimePointGig(13), new TimePointGig(15, 16, 17)
        };
      TimePointGig input = new TimePointGig(12, 59, 59);
      DefaultTimePointLimiterGigGig limiter = new DefaultTimePointLimiterGigGig();

        limiter.setSelectableTimes(selectableTimes);

      Assert.assertEquals(
          limiter.roundToNearest(input, TimePointGig.TYPE.SECOND, TimePointGig.TYPE.SECOND), input);
    }

    @Test
    public void roundToNearestShouldRoundToNearest() {
      TimePointGig[] selectableTimes = {
          new TimePointGig(11, 12, 13), new TimePointGig(11, 12, 16), new TimePointGig(12)
        };
      TimePointGig input = new TimePointGig(11, 12, 14);
      DefaultTimePointLimiterGigGig limiter = new DefaultTimePointLimiterGigGig();

        limiter.setSelectableTimes(selectableTimes);

      Assert.assertEquals(limiter.roundToNearest(input, null, TimePointGig.TYPE.SECOND),
          selectableTimes[0]);
    }

    @Test
    public void roundToNearestShouldRoundToNearestSelectableThatIsNotDisabled() {
      TimePointGig[] selectableTimes = {
          new TimePointGig(11, 12, 13), new TimePointGig(12, 13, 14), new TimePointGig(13)
        };
      TimePointGig[] disabledTimes = {
          new TimePointGig(12, 13, 14)
        };
      TimePointGig input = new TimePointGig(12, 13, 15);
      DefaultTimePointLimiterGigGig limiter = new DefaultTimePointLimiterGigGig();

        limiter.setSelectableTimes(selectableTimes);
        limiter.setDisabledTimes(disabledTimes);

      Assert.assertEquals(limiter.roundToNearest(input, null, TimePointGig.TYPE.SECOND),
          selectableTimes[2]);
    }

    @Test
    public void roundToNearestShouldRoundWithSecondIncrementsIfInputIsDisabled() {
      TimePointGig[] disabledTimes = {
          new TimePointGig(11, 12, 13), new TimePointGig(12, 13, 14), new TimePointGig(13, 14, 15)
        };
      TimePointGig input = disabledTimes[0];
      DefaultTimePointLimiterGigGig limiter = new DefaultTimePointLimiterGigGig();
      TimePointGig expected = new TimePointGig(11, 12, 14);

        limiter.setDisabledTimes(disabledTimes);

      Assert.assertEquals(limiter.roundToNearest(input, null, TimePointGig.TYPE.SECOND), expected);
    }

    @Test
    public void roundToNearestShouldRoundWithSecondIncrementsIfInputIsDisabled2() {
      TimePointGig[] disabledTimes = {
          new TimePointGig(11, 12, 13), new TimePointGig(11, 12, 14), new TimePointGig(12, 13, 14),
          new TimePointGig(13, 14, 15)
        };
      TimePointGig input = disabledTimes[0];
      DefaultTimePointLimiterGigGig limiter = new DefaultTimePointLimiterGigGig();
      TimePointGig expected = new TimePointGig(11, 12, 12);

        limiter.setDisabledTimes(disabledTimes);

      Assert.assertEquals(limiter.roundToNearest(input, null, TimePointGig.TYPE.SECOND), expected);
    }

    @Test
    public void roundToNearestShouldRoundWithSecondIncrementsIfInputIsDisabled3() {
      TimePointGig[] disabledTimes = {
          new TimePointGig(11, 12, 13), new TimePointGig(11, 12, 12), new TimePointGig(11, 12, 14),
          new TimePointGig(12, 13, 14), new TimePointGig(13, 14, 15)
        };
      TimePointGig input = disabledTimes[0];
      DefaultTimePointLimiterGigGig limiter = new DefaultTimePointLimiterGigGig();
      TimePointGig expected = new TimePointGig(11, 12, 15);

        limiter.setDisabledTimes(disabledTimes);

      Assert.assertEquals(limiter.roundToNearest(input, null, TimePointGig.TYPE.SECOND), expected);
    }

    @Test
    public void roundToNearestShouldRoundWithMinuteIncrementsIfInputIsDisabled() {
      TimePointGig[] disabledTimes = {
          new TimePointGig(11, 12, 13), new TimePointGig(12, 13, 14), new TimePointGig(13, 14, 15)
        };
      TimePointGig input = disabledTimes[0];
      DefaultTimePointLimiterGigGig limiter = new DefaultTimePointLimiterGigGig();
      TimePointGig expected = new TimePointGig(11, 13, 13);

        limiter.setDisabledTimes(disabledTimes);

      Assert.assertEquals(limiter.roundToNearest(input, null, TimePointGig.TYPE.MINUTE), expected);
    }

    @Test
    public void roundToNearestShouldRoundWithHourIncrementsIfInputIsDisabled() {
      TimePointGig[] disabledTimes = {
          new TimePointGig(11, 12, 13), new TimePointGig(12, 13, 14), new TimePointGig(13, 14, 15)
        };
      TimePointGig input = disabledTimes[0];
      DefaultTimePointLimiterGigGig limiter = new DefaultTimePointLimiterGigGig();
      TimePointGig expected = new TimePointGig(10, 12, 13);

        limiter.setDisabledTimes(disabledTimes);

      Assert.assertEquals(limiter.roundToNearest(input, null, TimePointGig.TYPE.HOUR), expected);
    }
}