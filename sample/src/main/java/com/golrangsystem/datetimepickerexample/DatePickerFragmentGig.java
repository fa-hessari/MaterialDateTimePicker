package com.golrangsystem.datetimepickerexample;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.golrangsystem.materialdatetimepicker.date.DatePickerDialogGig;
import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 */
public class DatePickerFragmentGig extends Fragment
    implements DatePickerDialogGig.OnDateSetListenerGig {

    private TextView dateTextView;
    private CheckBox modeDarkDate;
    private CheckBox modeCustomAccentDate;
    private CheckBox vibrateDate;
    private CheckBox dismissDate;
    private CheckBox titleDate;
    private CheckBox showYearFirst;
    private CheckBox showVersion2;
    private CheckBox switchOrientation;
    private CheckBox limitSelectableDays;
    private CheckBox highlightDays;
  private DatePickerDialogGig dpd;

  public DatePickerFragmentGig() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.datepicker_layout, container, false);

        // Find our View instances
        dateTextView = view.findViewById(R.id.date_textview);
        Button dateButton = view.findViewById(R.id.date_button);
        modeDarkDate = view.findViewById(R.id.mode_dark_date);
        modeCustomAccentDate = view.findViewById(R.id.mode_custom_accent_date);
        vibrateDate = view.findViewById(R.id.vibrate_date);
        dismissDate = view.findViewById(R.id.dismiss_date);
        titleDate = view.findViewById(R.id.title_date);
        showYearFirst = view.findViewById(R.id.show_year_first);
        showVersion2 = view.findViewById(R.id.show_version_2);
        switchOrientation = view.findViewById(R.id.switch_orientation);
        limitSelectableDays = view.findViewById(R.id.limit_dates);
        highlightDays = view.findViewById(R.id.highlight_dates);

        view.findViewById(R.id.original_button).setOnClickListener(v -> {
            Calendar now = Calendar.getInstance();
            new android.app.DatePickerDialog(
                    requireActivity(),
                    (view1, year, month, dayOfMonth) -> Log.d("Orignal", "Got clicked"),
                    now.get(Calendar.YEAR),
                    now.get(Calendar.MONTH),
                    now.get(Calendar.DAY_OF_MONTH)
            ).show();
        });

        // Show a datepicker when the dateButton is clicked
        dateButton.setOnClickListener(v -> {
            Calendar now = Calendar.getInstance();
            /*
            It is recommended to always create a new instance whenever you need to show a Dialog.
            The sample app is reusing them because it is useful when looking for regressions
            during testing
             */
            if (dpd == null) {
              dpd = DatePickerDialogGig.newInstance(DatePickerFragmentGig.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
            } else {
                dpd.initialize(DatePickerFragmentGig.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
            }
            dpd.setThemeDark(modeDarkDate.isChecked());
            dpd.vibrate(vibrateDate.isChecked());
            dpd.dismissOnPause(dismissDate.isChecked());
            dpd.showYearPickerFirst(showYearFirst.isChecked());
          dpd.setVersion(showVersion2.isChecked() ? DatePickerDialogGig.Version.VERSION_2
              : DatePickerDialogGig.Version.VERSION_1);
            if (modeCustomAccentDate.isChecked()) {
                dpd.setAccentColor(Color.parseColor("#9C27B0"));
            }
            if (titleDate.isChecked()) {
                dpd.setTitle("DatePicker Title");
            }
            if (highlightDays.isChecked()) {
                Calendar date1 = Calendar.getInstance();
                Calendar date2 = Calendar.getInstance();
                date2.add(Calendar.WEEK_OF_MONTH, -1);
                Calendar date3 = Calendar.getInstance();
                date3.add(Calendar.WEEK_OF_MONTH, 1);
                Calendar[] days = {date1, date2, date3};
                dpd.setHighlightedDays(days);
            }
            if (limitSelectableDays.isChecked()) {
                Calendar[] days = new Calendar[13];
                for (int i = -6; i < 7; i++) {
                    Calendar day = Calendar.getInstance();
                    day.add(Calendar.DAY_OF_MONTH, i * 2);
                    days[i + 6] = day;
                }
                dpd.setSelectableDays(days);
            }
            if (switchOrientation.isChecked()) {
              if (dpd.getVersion() == DatePickerDialogGig.Version.VERSION_1) {
                dpd.setScrollOrientation(DatePickerDialogGig.ScrollOrientation.HORIZONTAL);
                } else {
                dpd.setScrollOrientation(DatePickerDialogGig.ScrollOrientation.VERTICAL);
                }
            }
          dpd.setOnCancelListener(dialog -> Log.d("DatePickerDialogGig", "Dialog was cancelled"));
            dpd.show(requireFragmentManager(), "Datepickerdialog");
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
      DatePickerDialogGig dpd =
          (DatePickerDialogGig) requireFragmentManager().findFragmentByTag("Datepickerdialog");
        if(dpd != null) dpd.setOnDateSetListener(this);
    }

    @Override
    public void onDateSetGig(DatePickerDialogGig view, int year, int monthOfYear, int dayOfMonth) {
        String date = "You picked the following date: "+dayOfMonth+"/"+(++monthOfYear)+"/"+year;
        dateTextView.setText(date);
    }
}
