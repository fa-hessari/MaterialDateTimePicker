package com.golrangsystem.materialdatetimepicker.time;

import android.os.Parcelable;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

@SuppressWarnings("WeakerAccess") public interface TimePointLimiterGig extends Parcelable {
  boolean isOutOfRange(@Nullable TimePointGig point, int index,
      @NonNull TimePointGig.TYPE resolution);

  boolean isAmDisabled();

  boolean isPmDisabled();

  @NonNull TimePointGig roundToNearest(@NonNull TimePointGig time, @Nullable TimePointGig.TYPE type,
      @NonNull TimePointGig.TYPE resolution);
}