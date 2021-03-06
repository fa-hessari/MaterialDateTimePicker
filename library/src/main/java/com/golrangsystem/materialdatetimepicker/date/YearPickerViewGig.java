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
import android.content.res.Resources;
import android.graphics.drawable.StateListDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.golrangsystem.materialdatetimepicker.date.DatePickerDialogGig.OnDateChangedListener;

/**
 * Displays a selectable list of years.
 */
public class YearPickerViewGig extends ListView
    implements OnItemClickListener, OnDateChangedListener {
  private final DatePickerControllerGig mController;
  private YearAdapter mAdapter;
  private int mViewSize;
  private int mChildSize;
  private TextViewWithCircularIndicatorGig mSelectedView;

  public YearPickerViewGig(Context context, DatePickerControllerGig controller) {
    super(context);
    mController = controller;
    mController.registerOnDateChangedListener(this);
    ViewGroup.LayoutParams frame =
        new ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
    setLayoutParams(frame);
    Resources res = context.getResources();
    mViewSize = mController.getVersion() == DatePickerDialogGig.Version.VERSION_1
        ? res.getDimensionPixelOffset(
        com.golrangsystem.materialdatetimepicker.R.dimen.fh_gig_mdtp_date_picker_view_animator_height)
        : res.getDimensionPixelOffset(
            com.golrangsystem.materialdatetimepicker.R.dimen.fh_gig_mdtp_date_picker_view_animator_height_v2);
    mChildSize = res.getDimensionPixelOffset(
        com.golrangsystem.materialdatetimepicker.R.dimen.fh_gig_mdtp_year_label_height);
    setVerticalFadingEdgeEnabled(true);
    setFadingEdgeLength(mChildSize / 3);
    init();
    setOnItemClickListener(this);
    setSelector(new StateListDrawable());
    setDividerHeight(0);
    onDateChanged();
  }

  private static int getYearFromTextView(TextView view) {
    return Integer.valueOf(view.getText().toString());
  }

  private void init() {
    mAdapter = new YearAdapter(mController.getMinYear(), mController.getMaxYear());
    setAdapter(mAdapter);
  }

  @Override public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    mController.tryVibrate();
    TextViewWithCircularIndicatorGig clickedView = (TextViewWithCircularIndicatorGig) view;
    if (clickedView != null) {
      if (clickedView != mSelectedView) {
        if (mSelectedView != null) {
          mSelectedView.drawIndicator(false);
          mSelectedView.requestLayout();
        }
        clickedView.drawIndicator(true);
        clickedView.requestLayout();
        mSelectedView = clickedView;
      }
      mController.onYearSelected(getYearFromTextView(clickedView));
      mAdapter.notifyDataSetChanged();
    }
  }

  public void postSetSelectionCentered(final int position) {
    postSetSelectionFromTop(position, mViewSize / 2 - mChildSize / 2);
  }

  public void postSetSelectionFromTop(final int position, final int offset) {
    post(() -> {
      setSelectionFromTop(position, offset);
      requestLayout();
    });
  }

  public int getFirstPositionOffset() {
    final View firstChild = getChildAt(0);
    if (firstChild == null) {
      return 0;
    }
    return firstChild.getTop();
  }

  @Override public void onDateChanged() {
    mAdapter.notifyDataSetChanged();
    postSetSelectionCentered(mController.getSelectedDay().year - mController.getMinYear());
  }

  @Override public void onInitializeAccessibilityEvent(AccessibilityEvent event) {
    super.onInitializeAccessibilityEvent(event);
    if (event.getEventType() == AccessibilityEvent.TYPE_VIEW_SCROLLED) {
      event.setFromIndex(0);
      event.setToIndex(0);
    }
  }

  private final class YearAdapter extends BaseAdapter {
    private final int mMinYear;
    private final int mMaxYear;

    YearAdapter(int minYear, int maxYear) {
      if (minYear > maxYear) {
        throw new IllegalArgumentException("minYear > maxYear");
      }
      mMinYear = minYear;
      mMaxYear = maxYear;
    }

    @Override public int getCount() {
      return mMaxYear - mMinYear + 1;
    }

    @Override public Object getItem(int position) {
      return mMinYear + position;
    }

    @Override public long getItemId(int position) {
      return position;
    }

    @Override public View getView(int position, View convertView, ViewGroup parent) {
      TextViewWithCircularIndicatorGig v;
      if (convertView != null) {
        v = (TextViewWithCircularIndicatorGig) convertView;
      } else {
        v = (TextViewWithCircularIndicatorGig) LayoutInflater.from(parent.getContext())
            .inflate(
                com.golrangsystem.materialdatetimepicker.R.layout.fh_gig_mdtp_year_label_text_view,
                parent, false);
        v.setAccentColor(mController.getAccentColor(), mController.isThemeDark());
      }
      int year = mMinYear + position;
      boolean selected = mController.getSelectedDay().year == year;
      v.setText(String.format(mController.getLocale(), "%d", year));
      v.drawIndicator(selected);
      v.requestLayout();
      if (selected) {
        mSelectedView = v;
      }
      return v;
    }
  }
}
