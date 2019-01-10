package com.golrangsystem.materialdatetimepicker.date;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import com.golrangsystem.materialdatetimepicker.UtilsGig;

public class DayPickerGroupGig extends ViewGroup
    implements View.OnClickListener, DayPickerViewGig.OnPageListener {
  private ImageButton prevButton;
  private ImageButton nextButton;
  private DayPickerViewGig dayPickerViewGig;
  private DatePickerControllerGig controller;

  public DayPickerGroupGig(Context context) {
    super(context);
    init();
  }

  public DayPickerGroupGig(Context context, @NonNull DatePickerControllerGig controller) {
    super(context);
    this.controller = controller;
    init();
  }

  public DayPickerGroupGig(Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    init();
  }

  public DayPickerGroupGig(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init();
  }

  private void init() {
    dayPickerViewGig = new SimpleDayPickerViewGig(getContext(), controller);
    addView(dayPickerViewGig);

    final LayoutInflater inflater = LayoutInflater.from(getContext());
    final ViewGroup content = (ViewGroup) inflater.inflate(
        com.golrangsystem.materialdatetimepicker.R.layout.fh_gig_mdtp_daypicker_group, this, false);

    // Transfer all children from the content to this
    while (content.getChildCount() > 0) {
      final View view = content.getChildAt(0);
      content.removeViewAt(0);
      addView(view);
    }

    prevButton = findViewById(
        com.golrangsystem.materialdatetimepicker.R.id.fh_gig_mdtp_previous_month_arrow);
    nextButton =
        findViewById(com.golrangsystem.materialdatetimepicker.R.id.fh_gig_mdtp_next_month_arrow);

    if (controller.getVersion() == DatePickerDialogGig.Version.VERSION_1) {
      int size = UtilsGig.dpToPx(16f, getResources());
      prevButton.setMinimumHeight(size);
      prevButton.setMinimumWidth(size);
      nextButton.setMinimumHeight(size);
      nextButton.setMinimumWidth(size);
    }

    if (controller.isThemeDark()) {
      int color = ContextCompat.getColor(getContext(),
          com.golrangsystem.materialdatetimepicker.R.color.fh_gig_mdtp_date_picker_text_normal_dark_theme);
      prevButton.setColorFilter(color);
      nextButton.setColorFilter(color);
    }

    prevButton.setOnClickListener(this);
    nextButton.setOnClickListener(this);

    dayPickerViewGig.setOnPageListener(this);
  }

  private void updateButtonVisibility(int position) {
    final boolean isHorizontal =
        controller.getScrollOrientation() == DatePickerDialogGig.ScrollOrientation.HORIZONTAL;
    final boolean hasPrev = position > 0;
    final boolean hasNext = position < (dayPickerViewGig.getCount() - 1);
    prevButton.setVisibility(isHorizontal && hasPrev ? View.VISIBLE : View.INVISIBLE);
    nextButton.setVisibility(isHorizontal && hasNext ? View.VISIBLE : View.INVISIBLE);
  }

  public void onChange() {
    dayPickerViewGig.onChange();
  }

  public void onDateChanged() {
    dayPickerViewGig.onDateChanged();
  }

  public void postSetSelection(int position) {
    dayPickerViewGig.postSetSelection(position);
  }

  public int getMostVisiblePosition() {
    return dayPickerViewGig.getMostVisiblePosition();
  }

  @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    measureChild(dayPickerViewGig, widthMeasureSpec, heightMeasureSpec);

    final int measuredWidthAndState = dayPickerViewGig.getMeasuredWidthAndState();
    final int measuredHeightAndState = dayPickerViewGig.getMeasuredHeightAndState();
    setMeasuredDimension(measuredWidthAndState, measuredHeightAndState);

    final int pagerWidth = dayPickerViewGig.getMeasuredWidth();
    final int pagerHeight = dayPickerViewGig.getMeasuredHeight();
    final int buttonWidthSpec = MeasureSpec.makeMeasureSpec(pagerWidth, MeasureSpec.AT_MOST);
    final int buttonHeightSpec = MeasureSpec.makeMeasureSpec(pagerHeight, MeasureSpec.AT_MOST);
    prevButton.measure(buttonWidthSpec, buttonHeightSpec);
    nextButton.measure(buttonWidthSpec, buttonHeightSpec);
  }

  @Override protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
    final ImageButton leftButton;
    final ImageButton rightButton;
    if (ViewCompat.getLayoutDirection(this) == ViewCompat.LAYOUT_DIRECTION_RTL) {
      leftButton = nextButton;
      rightButton = prevButton;
    } else {
      leftButton = prevButton;
      rightButton = nextButton;
    }

    final int topMargin = controller.getVersion() == DatePickerDialogGig.Version.VERSION_1 ? 0
        : getContext().getResources()
            .getDimensionPixelSize(
                com.golrangsystem.materialdatetimepicker.R.dimen.fh_gig_mdtp_date_picker_view_animator_padding_v2);
    final int width = right - left;
    final int height = bottom - top;
    dayPickerViewGig.layout(0, topMargin, width, height);

    final SimpleMonthViewGig monthView = (SimpleMonthViewGig) dayPickerViewGig.getChildAt(0);
    final int monthHeight = monthView.getMonthHeight();
    final int cellWidth = monthView.getCellWidth();
    final int edgePadding = monthView.getEdgePadding();

    // Vertically center the previous/next buttons within the month
    // header, horizontally center within the day cell.
    final int leftDW = leftButton.getMeasuredWidth();
    final int leftDH = leftButton.getMeasuredHeight();
    final int leftIconTop = topMargin + monthView.getPaddingTop() + (monthHeight - leftDH) / 2;
    final int leftIconLeft = edgePadding + (cellWidth - leftDW) / 2;
    leftButton.layout(leftIconLeft, leftIconTop, leftIconLeft + leftDW, leftIconTop + leftDH);

    final int rightDW = rightButton.getMeasuredWidth();
    final int rightDH = rightButton.getMeasuredHeight();
    final int rightIconTop = topMargin + monthView.getPaddingTop() + (monthHeight - rightDH) / 2;
    final int rightIconRight = width - edgePadding - (cellWidth - rightDW) / 2 - 2;
    rightButton.layout(rightIconRight - rightDW, rightIconTop, rightIconRight,
        rightIconTop + rightDH);
  }

  @Override public void onPageChanged(int position) {
    updateButtonVisibility(position);
    dayPickerViewGig.accessibilityAnnouncePageChanged();
  }

  @Override public void onClick(@NonNull View v) {
    int offset;
    if (nextButton == v) {
      offset = 1;
    } else if (prevButton == v) {
      offset = -1;
    } else {
      return;
    }
    int position = dayPickerViewGig.getMostVisiblePosition() + offset;

    // updateButtonVisibility only triggers when a scroll is completed. So a user might
    // click the button when the animation is still ongoing potentially pushing the target
    // position outside of the bounds of the dayPickerViewGig
    if (position >= 0 && position < dayPickerViewGig.getCount()) {
      dayPickerViewGig.smoothScrollToPosition(position);
      updateButtonVisibility(position);
    }
  }
}
