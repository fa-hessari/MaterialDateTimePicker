package com.golrangsystem.materialdatetimepicker.time;

import java.util.HashSet;
import org.junit.Assert;
import org.junit.Test;

public class TimePointGigTest {

    @Test
    public void timepointsWithSameFieldsShouldHaveSameHashCode() {
      TimePointGig first = new TimePointGig(12, 0, 0);
      TimePointGig second = new TimePointGig(12, 0, 0);
        Assert.assertEquals(first.hashCode(), second.hashCode());
    }

    @Test
    public void timepointsWithSameFieldsShouldBeEquals() {
      TimePointGig first = new TimePointGig(12, 0, 0);
      TimePointGig second = new TimePointGig(12, 0, 0);
        Assert.assertEquals(first, second);
    }

    @Test
    public void timepointsWithSameFieldsShouldBeDistinctInHashSet() {
      HashSet<TimePointGig> timePointGigs = new HashSet<>(4);
      timePointGigs.add(new TimePointGig(12, 0, 0));
      timePointGigs.add(new TimePointGig(12, 0, 0));
      timePointGigs.add(new TimePointGig(12, 0, 0));
      timePointGigs.add(new TimePointGig(12, 0, 0));
      Assert.assertEquals(timePointGigs.size(), 1);
    }

    @Test
    public void timepointsWithDifferentFieldsShouldNotBeDistinctInHashSet() {
      HashSet<TimePointGig> timePointGigs = new HashSet<>(4);
      timePointGigs.add(new TimePointGig(12, 1, 0));
      timePointGigs.add(new TimePointGig(12, 2, 0));
      timePointGigs.add(new TimePointGig(12, 3, 0));
      timePointGigs.add(new TimePointGig(12, 4, 0));
      Assert.assertEquals(timePointGigs.size(), 4);
    }

    @Test
    public void compareToShouldReturnNegativeIfArgumentIsBigger() {
      TimePointGig orig = new TimePointGig(12, 11, 10);
      TimePointGig arg = new TimePointGig(13, 14, 15);
        Assert.assertTrue(orig.compareTo(arg) < 0);
    }

    @Test
    public void compareToShouldReturnPositiveIfArgumentIsSmaller() {
      TimePointGig orig = new TimePointGig(12, 11, 10);
      TimePointGig arg = new TimePointGig(10, 14, 15);
        Assert.assertTrue(orig.compareTo(arg) > 0);
    }

    @Test
    public void compareToShouldReturnZeroIfArgumentIsEqual() {
      TimePointGig orig = new TimePointGig(12, 11, 10);
      TimePointGig arg = new TimePointGig(12, 11, 10);
        Assert.assertTrue(orig.compareTo(arg) == 0);
    }

    @Test
    public void isAMShouldReturnTrueIfTimepointIsBeforeMidday() {
      TimePointGig timePointGig = new TimePointGig(11);
      Assert.assertTrue(timePointGig.isAM());
    }

    @Test
    public void isAMShouldReturnFalseIfTimepointIsMidday() {
      TimePointGig timePointGig = new TimePointGig(12);
      Assert.assertFalse(timePointGig.isAM());
    }

    @Test
    public void isAMShouldReturnFalseIfTimepointIsAfterMidday() {
      TimePointGig timePointGig = new TimePointGig(13);
      Assert.assertFalse(timePointGig.isAM());
    }

    @Test
    public void isAMShouldReturnTrueIfTimepointIsMidnight() {
      TimePointGig timePointGig = new TimePointGig(0);
      Assert.assertTrue(timePointGig.isAM());
    }

    @Test
    public void isPMShouldReturnFalseIfTimepointIsBeforeMidday() {
      TimePointGig timePointGig = new TimePointGig(11);
      Assert.assertFalse(timePointGig.isPM());
    }

    @Test
    public void isPMShouldReturnTrueIfTimepointIsMidday() {
      TimePointGig timePointGig = new TimePointGig(12);
      Assert.assertTrue(timePointGig.isPM());
    }

    @Test
    public void isPMShouldReturnTrueIfTimepointIsAfterMidday() {
      TimePointGig timePointGig = new TimePointGig(13);
      Assert.assertTrue(timePointGig.isPM());
    }

    @Test
    public void isPMShouldReturnFalseIfTimepointIsMidnight() {
      TimePointGig timePointGig = new TimePointGig(0);
      Assert.assertFalse(timePointGig.isPM());
    }

    @Test
    public void setAMShouldDoNothingIfTimepointIsBeforeMidday() {
      TimePointGig timePointGig = new TimePointGig(11);
      timePointGig.setAM();
      Assert.assertEquals(timePointGig.getHour(), 11);
    }

    @Test
    public void setAMShouldSetToMidnightIfTimepointIsMidday() {
      TimePointGig timePointGig = new TimePointGig(12);
      timePointGig.setAM();
      Assert.assertEquals(timePointGig.getHour(), 0);
    }

    @Test
    public void setAMShouldSetBeforeMiddayIfTimepointIsAfterMidday() {
      TimePointGig timePointGig = new TimePointGig(13);
      timePointGig.setAM();
      Assert.assertEquals(timePointGig.getHour(), 1);
    }

    @Test
    public void setAMShouldDoNothingIfTimepointIsMidnight() {
      TimePointGig timePointGig = new TimePointGig(0);
      timePointGig.setAM();
      Assert.assertEquals(timePointGig.getHour(), 0);
    }

    @Test
    public void setAMShouldNotChangeMinutesOrSeconds() {
      TimePointGig timePointGig = new TimePointGig(13, 14, 15);
      timePointGig.setAM();
      Assert.assertEquals(timePointGig.getMinute(), 14);
      Assert.assertEquals(timePointGig.getSecond(), 15);
    }

    @Test
    public void setPMShouldDoNothingIfTimepointIsAfterMidday() {
      TimePointGig timePointGig = new TimePointGig(13);
      timePointGig.setPM();
      Assert.assertEquals(timePointGig.getHour(), 13);
    }

    @Test
    public void setPMShouldSetToMiddayIfTimepointIsMidnight() {
      TimePointGig timePointGig = new TimePointGig(0);
      timePointGig.setPM();
      Assert.assertEquals(timePointGig.getHour(), 12);
    }

    @Test
    public void setPMShouldSetAfterMiddayIfTimepointIsBeforeMidday() {
      TimePointGig timePointGig = new TimePointGig(5);
      timePointGig.setPM();
      Assert.assertEquals(timePointGig.getHour(), 17);
    }

    @Test
    public void setPMShouldDoNothingIfTimepointIsMidday() {
      TimePointGig timePointGig = new TimePointGig(12);
      timePointGig.setPM();
      Assert.assertEquals(timePointGig.getHour(), 12);
    }

    @Test
    public void setPMShouldNotChangeMinutesOrSeconds() {
      TimePointGig timePointGig = new TimePointGig(1, 14, 15);
      timePointGig.setPM();
      Assert.assertEquals(timePointGig.getMinute(), 14);
      Assert.assertEquals(timePointGig.getSecond(), 15);
    }

    @Test
    public void equalsShouldReturnTrueWhenInputsAreEqualWithSecondsResolution() {
      TimePointGig timePointGig1 = new TimePointGig(1, 14, 15);
      TimePointGig timePointGig2 = new TimePointGig(1, 14, 15);
      Assert.assertTrue(timePointGig1.equals(timePointGig2, TimePointGig.TYPE.SECOND));
    }

    @Test
    public void equalsShouldReturnFalseWhenInputsDifferWithSecondsResolution() {
      TimePointGig timePointGig1 = new TimePointGig(1, 14, 15);
      TimePointGig timePointGig2 = new TimePointGig(1, 14, 16);
      Assert.assertFalse(timePointGig1.equals(timePointGig2, TimePointGig.TYPE.SECOND));
    }

    @Test
    public void equalsShouldIgnoreSecondsWithMinuteResolution() {
      TimePointGig timePointGig1 = new TimePointGig(1, 14, 15);
      TimePointGig timePointGig2 = new TimePointGig(1, 14, 16);
      Assert.assertTrue(timePointGig1.equals(timePointGig2, TimePointGig.TYPE.MINUTE));
    }

    @Test
    public void equalsShouldReturnFalseWhenInputsDifferWithMinuteResolution() {
      TimePointGig timePointGig1 = new TimePointGig(1, 14, 15);
      TimePointGig timePointGig2 = new TimePointGig(1, 15, 15);
      Assert.assertFalse(timePointGig1.equals(timePointGig2, TimePointGig.TYPE.MINUTE));
    }

    @Test
    public void equalsShouldIgnoreSecondsWithHourResolution() {
      TimePointGig timePointGig1 = new TimePointGig(1, 14, 15);
      TimePointGig timePointGig2 = new TimePointGig(1, 14, 16);
      Assert.assertTrue(timePointGig1.equals(timePointGig2, TimePointGig.TYPE.HOUR));
    }

    @Test
    public void equalsShouldIgnoreMinutesWithHourResolution() {
      TimePointGig timePointGig1 = new TimePointGig(1, 14, 15);
      TimePointGig timePointGig2 = new TimePointGig(1, 15, 16);
      Assert.assertTrue(timePointGig1.equals(timePointGig2, TimePointGig.TYPE.HOUR));
    }

    @Test
    public void equalsShouldReturnFalseWhenInputsDifferWithHourResolution() {
      TimePointGig timePointGig1 = new TimePointGig(1, 14, 15);
      TimePointGig timePointGig2 = new TimePointGig(2, 14, 15);
      Assert.assertFalse(timePointGig1.equals(timePointGig2, TimePointGig.TYPE.HOUR));
    }
}
