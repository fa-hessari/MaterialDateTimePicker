package com.golrangsystem.materialdatetimepicker.time;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.Arrays;
import java.util.TreeSet;

/**
 * An implementation of TimePointLimiterGig which implements the most common ways to restrict
 * Timepoints
 * in a TimePickerDialogGig
 * Created by wdullaer on 20/06/17.
 */
class DefaultTimePointLimiterGig implements TimePointLimiterGig {
  @SuppressWarnings("WeakerAccess")
  public static final Parcelable.Creator<DefaultTimePointLimiterGig> CREATOR =
      new Parcelable.Creator<DefaultTimePointLimiterGig>() {
        public DefaultTimePointLimiterGig createFromParcel(Parcel in) {
          return new DefaultTimePointLimiterGig(in);
    }

        public DefaultTimePointLimiterGig[] newArray(int size) {
          return new DefaultTimePointLimiterGig[size];
    }
  };
  private TreeSet<TimePointGig> mSelectableTimes = new TreeSet<>();
  private TreeSet<TimePointGig> mDisabledTimes = new TreeSet<>();
  private TreeSet<TimePointGig> exclusiveSelectableTimes = new TreeSet<>();
  private TimePointGig mMinTime;
  private TimePointGig mMaxTime;

  DefaultTimePointLimiterGig() {
  }

  @SuppressWarnings("WeakerAccess") public DefaultTimePointLimiterGig(Parcel in) {
    mMinTime = in.readParcelable(TimePointGig.class.getClassLoader());
    mMaxTime = in.readParcelable(TimePointGig.class.getClassLoader());
    mSelectableTimes.addAll(Arrays.asList(in.createTypedArray(TimePointGig.CREATOR)));
    mDisabledTimes.addAll(Arrays.asList(in.createTypedArray(TimePointGig.CREATOR)));
    exclusiveSelectableTimes = getExclusiveSelectableTimes(mSelectableTimes, mDisabledTimes);
  }

  @Override public void writeToParcel(Parcel out, int flags) {
    out.writeParcelable(mMinTime, flags);
    out.writeParcelable(mMaxTime, flags);
    out.writeTypedArray(mSelectableTimes.toArray(new TimePointGig[mSelectableTimes.size()]), flags);
    out.writeTypedArray(mDisabledTimes.toArray(new TimePointGig[mDisabledTimes.size()]), flags);
  }

  @Override public int describeContents() {
    return 0;
  }

  @Nullable TimePointGig getMinTime() {
    return mMinTime;
  }

  void setMinTime(@NonNull TimePointGig minTime) {
    if (mMaxTime != null && minTime.compareTo(mMaxTime) > 0) {
      throw new IllegalArgumentException("Minimum time must be smaller than the maximum time");
    }
    mMinTime = minTime;
  }

  @Nullable TimePointGig getMaxTime() {
    return mMaxTime;
  }

  void setMaxTime(@NonNull TimePointGig maxTime) {
    if (mMinTime != null && maxTime.compareTo(mMinTime) < 0) {
      throw new IllegalArgumentException("Maximum time must be greater than the minimum time");
    }
    mMaxTime = maxTime;
  }

  @NonNull TimePointGig[] getSelectableTimes() {
    return mSelectableTimes.toArray(new TimePointGig[mSelectableTimes.size()]);
  }

  void setSelectableTimes(@NonNull TimePointGig[] selectableTimes) {
    mSelectableTimes.addAll(Arrays.asList(selectableTimes));
    exclusiveSelectableTimes = getExclusiveSelectableTimes(mSelectableTimes, mDisabledTimes);
  }

  @NonNull TimePointGig[] getDisabledTimes() {
    return mDisabledTimes.toArray(new TimePointGig[mDisabledTimes.size()]);
  }

  void setDisabledTimes(@NonNull TimePointGig[] disabledTimes) {
    mDisabledTimes.addAll(Arrays.asList(disabledTimes));
    exclusiveSelectableTimes = getExclusiveSelectableTimes(mSelectableTimes, mDisabledTimes);
  }

  @NonNull private TreeSet<TimePointGig> getExclusiveSelectableTimes(
      @NonNull TreeSet<TimePointGig> selectable, @NonNull TreeSet<TimePointGig> disabled) {
    TreeSet<TimePointGig> output = new TreeSet<>(selectable);
    output.removeAll(disabled);
    return output;
  }

  @Override public boolean isOutOfRange(@Nullable TimePointGig current, int index,
      @NonNull TimePointGig.TYPE resolution) {
    if (current == null) return false;

    if (index == TimePickerDialogGig.HOUR_INDEX) {
      if (mMinTime != null && mMinTime.getHour() > current.getHour()) return true;

      if (mMaxTime != null && mMaxTime.getHour() + 1 <= current.getHour()) return true;

      if (!exclusiveSelectableTimes.isEmpty()) {
        TimePointGig ceil = exclusiveSelectableTimes.ceiling(current);
        TimePointGig floor = exclusiveSelectableTimes.floor(current);
        return !(current.equals(ceil, TimePointGig.TYPE.HOUR) || current.equals(floor,
            TimePointGig.TYPE.HOUR));
      }

      if (!mDisabledTimes.isEmpty() && resolution == TimePointGig.TYPE.HOUR) {
        TimePointGig ceil = mDisabledTimes.ceiling(current);
        TimePointGig floor = mDisabledTimes.floor(current);
        return current.equals(ceil, TimePointGig.TYPE.HOUR) || current.equals(floor,
            TimePointGig.TYPE.HOUR);
      }

      return false;
    } else if (index == TimePickerDialogGig.MINUTE_INDEX) {
      if (mMinTime != null) {
        TimePointGig roundedMin = new TimePointGig(mMinTime.getHour(), mMinTime.getMinute());
        if (roundedMin.compareTo(current) > 0) return true;
      }

      if (mMaxTime != null) {
        TimePointGig roundedMax = new TimePointGig(mMaxTime.getHour(), mMaxTime.getMinute(), 59);
        if (roundedMax.compareTo(current) < 0) return true;
      }

      if (!exclusiveSelectableTimes.isEmpty()) {
        TimePointGig ceil = exclusiveSelectableTimes.ceiling(current);
        TimePointGig floor = exclusiveSelectableTimes.floor(current);
        return !(current.equals(ceil, TimePointGig.TYPE.MINUTE) || current.equals(floor,
            TimePointGig.TYPE.MINUTE));
      }

      if (!mDisabledTimes.isEmpty() && resolution == TimePointGig.TYPE.MINUTE) {
        TimePointGig ceil = mDisabledTimes.ceiling(current);
        TimePointGig floor = mDisabledTimes.floor(current);
        boolean ceilExclude = current.equals(ceil, TimePointGig.TYPE.MINUTE);
        boolean floorExclude = current.equals(floor, TimePointGig.TYPE.MINUTE);
        return ceilExclude || floorExclude;
      }

      return false;
    } else {
      return isOutOfRange(current);
    }
  }

  public boolean isOutOfRange(@NonNull TimePointGig current) {
    if (mMinTime != null && mMinTime.compareTo(current) > 0) return true;

    if (mMaxTime != null && mMaxTime.compareTo(current) < 0) return true;

    if (!exclusiveSelectableTimes.isEmpty()) return !exclusiveSelectableTimes.contains(current);

    return mDisabledTimes.contains(current);
  }

  @SuppressWarnings("SimplifiableIfStatement") @Override public boolean isAmDisabled() {
    TimePointGig midday = new TimePointGig(12);

    if (mMinTime != null && mMinTime.compareTo(midday) >= 0) return true;

    if (!exclusiveSelectableTimes.isEmpty()) {
      return exclusiveSelectableTimes.first().compareTo(midday) >= 0;
    }

    return false;
  }

  @SuppressWarnings("SimplifiableIfStatement") @Override public boolean isPmDisabled() {
    TimePointGig midday = new TimePointGig(12);

    if (mMaxTime != null && mMaxTime.compareTo(midday) < 0) return true;

    if (!exclusiveSelectableTimes.isEmpty()) {
      return exclusiveSelectableTimes.last().compareTo(midday) < 0;
    }

    return false;
  }

  @Override public @NonNull TimePointGig roundToNearest(@NonNull TimePointGig time,
      @Nullable TimePointGig.TYPE type, @NonNull TimePointGig.TYPE resolution) {
    if (mMinTime != null && mMinTime.compareTo(time) > 0) return mMinTime;

    if (mMaxTime != null && mMaxTime.compareTo(time) < 0) return mMaxTime;

    // type == SECOND: cannot change anything, return input
    if (type == TimePointGig.TYPE.SECOND) return time;

    if (!exclusiveSelectableTimes.isEmpty()) {
      TimePointGig floor = exclusiveSelectableTimes.floor(time);
      TimePointGig ceil = exclusiveSelectableTimes.ceiling(time);

      if (floor == null || ceil == null) {
        TimePointGig t = floor == null ? ceil : floor;
        if (type == null) return t;
        if (t.getHour() != time.getHour()) return time;
        if (type == TimePointGig.TYPE.MINUTE && t.getMinute() != time.getMinute()) return time;
        return t;
      }

      if (type == TimePointGig.TYPE.HOUR) {
        if (floor.getHour() != time.getHour() && ceil.getHour() == time.getHour()) return ceil;
        if (floor.getHour() == time.getHour() && ceil.getHour() != time.getHour()) return floor;
        if (floor.getHour() != time.getHour() && ceil.getHour() != time.getHour()) return time;
      }

      if (type == TimePointGig.TYPE.MINUTE) {
        if (floor.getHour() != time.getHour() && ceil.getHour() != time.getHour()) return time;
        if (floor.getHour() != time.getHour() && ceil.getHour() == time.getHour()) {
          return ceil.getMinute() == time.getMinute() ? ceil : time;
        }
        if (floor.getHour() == time.getHour() && ceil.getHour() != time.getHour()) {
          return floor.getMinute() == time.getMinute() ? floor : time;
        }
        if (floor.getMinute() != time.getMinute() && ceil.getMinute() == time.getMinute()) {
          return ceil;
        }
        if (floor.getMinute() == time.getMinute() && ceil.getMinute() != time.getMinute()) {
          return floor;
        }
        if (floor.getMinute() != time.getMinute() && ceil.getMinute() != time.getMinute()) {
          return time;
        }
      }

      int floorDist = Math.abs(time.compareTo(floor));
      int ceilDist = Math.abs(time.compareTo(ceil));

      return floorDist < ceilDist ? floor : ceil;
    }

    if (!mDisabledTimes.isEmpty()) {
      // if type matches resolution: cannot change anything, return input
      if (type != null && type == resolution) return time;

      if (resolution == TimePointGig.TYPE.SECOND) {
        if (!mDisabledTimes.contains(time)) return time;
        return searchValidTimePoint(time, type, resolution);
      }

      if (resolution == TimePointGig.TYPE.MINUTE) {
        TimePointGig ceil = mDisabledTimes.ceiling(time);
        TimePointGig floor = mDisabledTimes.floor(time);
        boolean ceilDisabled = time.equals(ceil, TimePointGig.TYPE.MINUTE);
        boolean floorDisabled = time.equals(floor, TimePointGig.TYPE.MINUTE);

        if (ceilDisabled || floorDisabled) return searchValidTimePoint(time, type, resolution);
        return time;
      }

      if (resolution == TimePointGig.TYPE.HOUR) {
        TimePointGig ceil = mDisabledTimes.ceiling(time);
        TimePointGig floor = mDisabledTimes.floor(time);
        boolean ceilDisabled = time.equals(ceil, TimePointGig.TYPE.HOUR);
        boolean floorDisabled = time.equals(floor, TimePointGig.TYPE.HOUR);

        if (ceilDisabled || floorDisabled) return searchValidTimePoint(time, type, resolution);
        return time;
      }
    }

    return time;
  }

  private TimePointGig searchValidTimePoint(@NonNull TimePointGig time,
      @Nullable TimePointGig.TYPE type, @NonNull TimePointGig.TYPE resolution) {
    TimePointGig forward = new TimePointGig(time);
    TimePointGig backward = new TimePointGig(time);
    int iteration = 0;
    int resolutionMultiplier = 1;
    if (resolution == TimePointGig.TYPE.MINUTE) resolutionMultiplier = 60;
    if (resolution == TimePointGig.TYPE.SECOND) resolutionMultiplier = 3600;

    while (iteration < 24 * resolutionMultiplier) {
      iteration++;
      forward.add(resolution, 1);
      backward.add(resolution, -1);

      if (type == null || forward.get(type) == time.get(type)) {
        TimePointGig forwardCeil = mDisabledTimes.ceiling(forward);
        TimePointGig forwardFloor = mDisabledTimes.floor(forward);
        if (!forward.equals(forwardCeil, resolution) && !forward.equals(forwardFloor, resolution)) {
          return forward;
        }
      }

      if (type == null || backward.get(type) == time.get(type)) {
        TimePointGig backwardCeil = mDisabledTimes.ceiling(backward);
        TimePointGig backwardFloor = mDisabledTimes.floor(backward);
        if (!backward.equals(backwardCeil, resolution) && !backward.equals(backwardFloor,
            resolution)) {
          return backward;
        }
      }

      if (type != null && backward.get(type) != time.get(type) && forward.get(type) != time.get(
          type)) {
        break;
      }
    }
    // If this step is reached, the user has disabled all timepoints
    return time;
  }
}
