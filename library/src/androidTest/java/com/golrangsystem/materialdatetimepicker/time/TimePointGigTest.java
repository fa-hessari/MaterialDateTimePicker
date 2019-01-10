package com.golrangsystem.materialdatetimepicker.time;

import android.os.Parcel;
import androidx.test.runner.AndroidJUnit4;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

/**
 * Test for TimePointGig which need to run on an actual device
 * Created by wdullaer on 1/11/17.
 */
@RunWith(AndroidJUnit4.class) public class TimePointGigTest {
    @Test
    public void shouldCorrectlySaveAndRestoreAParcel() {
      TimePointGig input = new TimePointGig(1, 2, 3);
        Parcel timepointParcel = Parcel.obtain();
        input.writeToParcel(timepointParcel, 0);
        timepointParcel.setDataPosition(0);

      TimePointGig output = TimePointGig.CREATOR.createFromParcel(timepointParcel);
        assertEquals(input.getHour(), output.getHour());
        assertEquals(input.getMinute(), output.getMinute());
        assertEquals(input.getSecond(), output.getSecond());
    }
}