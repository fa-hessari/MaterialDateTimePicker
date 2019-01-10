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
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.golrangsystem.materialdatetimepicker.GravitySnapHelperGig;
import com.golrangsystem.materialdatetimepicker.UtilsGig;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * This displays a list of months in a calendar format with selectable days.
 */
public abstract class DayPickerViewGig extends RecyclerView
    implements DatePickerDialogGig.OnDateChangedListener {
  private static final String TAG = "MonthFragment";
  protected Context mContext;
  // highlighted time
  protected MonthAdapterGig.CalendarDay mSelectedDay;
  protected MonthAdapterGig mAdapter;
  protected MonthAdapterGig.CalendarDay mTempDay;
  // which month should be displayed/highlighted [0-11]
  protected int mCurrentMonthDisplayed;
  // used for tracking what state listview is in
  protected int mPreviousScrollState = RecyclerView.SCROLL_STATE_IDLE;
  private OnPageListener pageListener;
  private DatePickerControllerGig mController;

  public DayPickerViewGig(Context context, AttributeSet attrs) {
    super(context, attrs);
    DatePickerDialogGig.ScrollOrientation scrollOrientation =
        Build.VERSION.SDK_INT < Build.VERSION_CODES.M
            ? DatePickerDialogGig.ScrollOrientation.VERTICAL
            : DatePickerDialogGig.ScrollOrientation.HORIZONTAL;
    init(context, scrollOrientation);
  }

  public DayPickerViewGig(Context context, DatePickerControllerGig controller) {
    super(context);
    init(context, controller.getScrollOrientation());
    setController(controller);
  }

  private static String getMonthAndYearString(int month, int year, Locale locale) {
    Calendar calendar = Calendar.getInstance();
    calendar.set(Calendar.MONTH, month);
    calendar.set(Calendar.YEAR, year);
    return new SimpleDateFormat("MMMM yyyy", locale).format(calendar.getTime());
  }

  public void setController(DatePickerControllerGig controller) {
    mController = controller;
    mController.registerOnDateChangedListener(this);
    mSelectedDay = new MonthAdapterGig.CalendarDay(mController.getTimeZone());
    mTempDay = new MonthAdapterGig.CalendarDay(mController.getTimeZone());
    refreshAdapter();
    onDateChanged();
  }

  public void init(Context context, DatePickerDialogGig.ScrollOrientation scrollOrientation) {
    @RecyclerView.Orientation int layoutOrientation =
        scrollOrientation == DatePickerDialogGig.ScrollOrientation.VERTICAL ? RecyclerView.VERTICAL
            : RecyclerView.HORIZONTAL;
    LinearLayoutManager linearLayoutManager =
        new LinearLayoutManager(context, layoutOrientation, false);
    setLayoutManager(linearLayoutManager);
    setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
    setVerticalScrollBarEnabled(false);
    setHorizontalScrollBarEnabled(false);
    setClipChildren(false);

    mContext = context;
    setUpRecyclerView(scrollOrientation);
  }

  /**
   * Sets all the required fields for the list view. Override this method to
   * set a different list view behavior.
   */
  protected void setUpRecyclerView(DatePickerDialogGig.ScrollOrientation scrollOrientation) {
    setVerticalScrollBarEnabled(false);
    setFadingEdgeLength(0);
    int gravity = scrollOrientation == DatePickerDialogGig.ScrollOrientation.VERTICAL ? Gravity.TOP
        : Gravity.START;
    GravitySnapHelperGig helper = new GravitySnapHelperGig(gravity, position -> {
      // Leverage the fact that the SnapHelper figures out which position is shown and
      // pass this on to our PageListener after the snap has happened
      if (pageListener != null) pageListener.onPageChanged(position);
    });
    helper.attachToRecyclerView(this);
  }

  public void onChange() {
    refreshAdapter();
  }

  @Override protected void onLayout(boolean changed, int l, int t, int r, int b) {
    super.onLayout(changed, l, t, r, b);
    final MonthAdapterGig.CalendarDay focusedDay = findAccessibilityFocus();
    restoreAccessibilityFocus(focusedDay);
  }

  /**
   * Creates a new adapter if necessary and sets up its parameters. Override
   * this method to provide a custom adapter.
   */
  protected void refreshAdapter() {
    if (mAdapter == null) {
      mAdapter = createMonthAdapter(mController);
    } else {
      mAdapter.setSelectedDay(mSelectedDay);
      if (pageListener != null) pageListener.onPageChanged(getMostVisiblePosition());
    }
    // refresh the view with the new parameters
    setAdapter(mAdapter);
  }

  public abstract MonthAdapterGig createMonthAdapter(DatePickerControllerGig controller);

  @Nullable @SuppressWarnings("unused") public OnPageListener getOnPageListener() {
    return pageListener;
  }

  public void setOnPageListener(@Nullable OnPageListener pageListener) {
    this.pageListener = pageListener;
  }

  /**
   * This moves to the specified time in the view. If the time is not already
   * in range it will move the list so that the first of the month containing
   * the time is at the top of the view. If the new time is already in view
   * the list will not be scrolled unless forceScroll is true. This time may
   * optionally be highlighted as selected as well.
   *
   * @param day The day to move to
   * @param animate Whether to scroll to the given time or just redraw at the
   * new location
   * @param setSelected Whether to set the given time as selected
   * @param forceScroll Whether to recenter even if the time is already
   * visible
   * @return Whether or not the view animated to the new location
   */
  public boolean goTo(MonthAdapterGig.CalendarDay day, boolean animate, boolean setSelected,
      boolean forceScroll) {

    // Set the selected day
    if (setSelected) {
      mSelectedDay.set(day);
    }

    mTempDay.set(day);
    int minMonth = mController.getStartDate().get(Calendar.MONTH);
    final int position =
        (day.year - mController.getMinYear()) * MonthAdapterGig.MONTHS_IN_YEAR + day.month
            - minMonth;

    View child;
    int i = 0;
    int top = 0;
    // Find a child that's completely in the view
    do {
      child = getChildAt(i++);
      if (child == null) {
        break;
      }
      top = child.getTop();
      if (Log.isLoggable(TAG, Log.DEBUG)) {
        Log.d(TAG, "child at " + (i - 1) + " has top " + top);
      }
    } while (top < 0);

    // Compute the first and last position visible
    int selectedPosition = child != null ? getChildAdapterPosition(child) : 0;

    if (setSelected) {
      mAdapter.setSelectedDay(mSelectedDay);
    }

    if (Log.isLoggable(TAG, Log.DEBUG)) {
      Log.d(TAG, "GoTo position " + position);
    }
    // Check if the selected day is now outside of our visible range
    // and if so scroll to the month that contains it
    if (position != selectedPosition || forceScroll) {
      setMonthDisplayed(mTempDay);
      mPreviousScrollState = RecyclerView.SCROLL_STATE_DRAGGING;
      if (animate) {
        smoothScrollToPosition(position);
        if (pageListener != null) pageListener.onPageChanged(position);
        return true;
      } else {
        postSetSelection(position);
      }
    } else if (setSelected) {
      setMonthDisplayed(mSelectedDay);
    }
    return false;
  }

  public void postSetSelection(final int position) {
    clearFocus();
    post(() -> {
      ((LinearLayoutManager) getLayoutManager()).scrollToPositionWithOffset(position, 0);

      // Set initial accessibility focus to selected day
      restoreAccessibilityFocus(mSelectedDay);

      if (pageListener != null) pageListener.onPageChanged(position);
    });
  }

  /**
   * Sets the month displayed at the top of this view based on time. Override
   * to add custom events when the title is changed.
   */
  protected void setMonthDisplayed(MonthAdapterGig.CalendarDay date) {
    mCurrentMonthDisplayed = date.month;
  }

  /**
   * Gets the position of the view that is most prominently displayed within the list.
   */
  public int getMostVisiblePosition() {
    return getChildAdapterPosition(getMostVisibleMonth());
  }

  public MonthViewGig getMostVisibleMonth() {
    boolean verticalScroll =
        mController.getScrollOrientation() == DatePickerDialogGig.ScrollOrientation.VERTICAL;
    final int maxSize = verticalScroll ? getHeight() : getWidth();
    int maxDisplayedSize = 0;
    int i = 0;
    int size = 0;
    MonthViewGig mostVisibleMonth = null;

    while (size < maxSize) {
      View child = getChildAt(i);
      if (child == null) {
        break;
      }
      size = verticalScroll ? child.getBottom() : child.getRight();
      int endPosition = verticalScroll ? child.getTop() : child.getLeft();
      int displayedSize = Math.min(size, maxSize) - Math.max(0, endPosition);
      if (displayedSize > maxDisplayedSize) {
        mostVisibleMonth = (MonthViewGig) child;
        maxDisplayedSize = displayedSize;
      }
      i++;
    }
    return mostVisibleMonth;
  }

  public int getCount() {
    return mAdapter.getItemCount();
  }

  @Override public void onDateChanged() {
    goTo(mController.getSelectedDay(), false, true, true);
  }

  /**
   * Attempts to return the date that has accessibility focus.
   *
   * @return The date that has accessibility focus, or {@code null} if no date
   * has focus.
   */
  private MonthAdapterGig.CalendarDay findAccessibilityFocus() {
    final int childCount = getChildCount();
    for (int i = 0; i < childCount; i++) {
      final View child = getChildAt(i);
      if (child instanceof MonthViewGig) {
        final MonthAdapterGig.CalendarDay focus = ((MonthViewGig) child).getAccessibilityFocus();
        if (focus != null) {
          if (Build.VERSION.SDK_INT == Build.VERSION_CODES.JELLY_BEAN_MR1) {
            // Clear focus to avoid ListView bug in Jelly Bean MR1.
            ((MonthViewGig) child).clearAccessibilityFocus();
          }
          return focus;
        }
      }
    }

    return null;
  }

  /**
   * Attempts to restore accessibility focus to a given date. No-op if
   * {@code day} is {@code null}.
   *
   * @param day The date that should receive accessibility focus
   * @return {@code true} if focus was restored
   */
  private boolean restoreAccessibilityFocus(MonthAdapterGig.CalendarDay day) {
    if (day == null) {
      return false;
    }

    final int childCount = getChildCount();
    for (int i = 0; i < childCount; i++) {
      final View child = getChildAt(i);
      if (child instanceof MonthViewGig) {
        if (((MonthViewGig) child).restoreAccessibilityFocus(day)) {
          return true;
        }
      }
    }

    return false;
  }

  @Override public void onInitializeAccessibilityEvent(@NonNull AccessibilityEvent event) {
    super.onInitializeAccessibilityEvent(event);
    event.setItemCount(-1);
  }

  void accessibilityAnnouncePageChanged() {
    MonthViewGig mv = getMostVisibleMonth();
    String monthYear = getMonthAndYearString(mv.mMonth, mv.mYear, mController.getLocale());
    UtilsGig.tryAccessibilityAnnounce(this, monthYear);
  }

  public interface OnPageListener {
    /**
     * Called when the visible page of the DayPickerViewGig has changed
     *
     * @param position the new position visible in the DayPickerViewGig
     */
    void onPageChanged(int position);
  }
}
