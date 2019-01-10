package com.golrangsystem.materialdatetimepicker.time;

import org.junit.Assert;
import org.junit.Test;

public class TimePickerDialogTest {
    @Test
    public void getPickerResolutionShouldReturnSecondIfSecondsAreEnabled() {
      TimePickerDialogGIg tpd = TimePickerDialogGIg.newInstance(null, false);
        tpd.enableSeconds(true);
      Assert.assertEquals(tpd.getPickerResolution(), TimePointGig.TYPE.SECOND);
    }

    @Test
    public void getPickerResolutionShouldReturnMinuteIfMinutesAreEnabled() {
      TimePickerDialogGIg tpd = TimePickerDialogGIg.newInstance(null, false);
        tpd.enableSeconds(false);
        tpd.enableMinutes(true);
      Assert.assertEquals(tpd.getPickerResolution(), TimePointGig.TYPE.MINUTE);
    }

    @Test
    public void getPickerResolutionShouldReturnHourIfMinutesAndSecondsAreDisabled() {
      TimePickerDialogGIg tpd = TimePickerDialogGIg.newInstance(null, false);
        tpd.enableMinutes(false);
      Assert.assertEquals(tpd.getPickerResolution(), TimePointGig.TYPE.HOUR);
    }
}
