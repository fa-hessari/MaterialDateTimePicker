/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.golrangsystem.materialdatetimepicker.date;

import android.content.Context;
import android.util.AttributeSet;

/**
 * A DayPickerViewGig customized for {@link SimpleMonthAdapterGig}
 */
public class SimpleDayPickerViewGig extends DayPickerViewGig {
  public SimpleDayPickerViewGig(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public SimpleDayPickerViewGig(Context context, DatePickerControllerGig controller) {
    super(context, controller);
  }

  @Override public MonthAdapterGig createMonthAdapter(DatePickerControllerGig controller) {
    return new SimpleMonthAdapterGig(controller);
  }
}
