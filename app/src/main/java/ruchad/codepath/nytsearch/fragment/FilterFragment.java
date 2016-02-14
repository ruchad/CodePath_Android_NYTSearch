package ruchad.codepath.nytsearch.fragment;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import ruchad.codepath.nytsearch.R;

public class FilterFragment extends DialogFragment {

    @Bind(R.id.tvBeginDate) TextView tvBeginDate;
    @Bind(R.id.spSortOrder) Spinner spSortOrder;
    @Bind(R.id.cbArts) CheckBox cbArts;
    @Bind(R.id.cbFashion) CheckBox cbFashion;
    @Bind(R.id.cbSports) CheckBox cbSports;
    @Bind(R.id.btnSave) Button btnSave;
    @Bind(R.id.btnClear) Button btnClear;

    //Default constructor for Dialog Fragment
    public FilterFragment(){}

    private FilterFragment filterFragment;
    public static FilterFragment getInstance(String title){
        //ToDo: Set Title - Align center
        return new FilterFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_filter, container);
        ButterKnife.bind(this, view);

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getContext());
        tvBeginDate.setText(sharedPref.getString(getString(R.string.key_beginDate), ""));
        String sortOrder = sharedPref.getString(getString(R.string.key_sortOrder), "");
        if(sortOrder.equalsIgnoreCase("newest")) spSortOrder.setSelection(0);
        if(sortOrder.equalsIgnoreCase("oldest")) spSortOrder.setSelection(1);
        cbSports.setChecked(sharedPref.getBoolean(getString(R.string.key_Sports), false));
        cbFashion.setChecked(sharedPref.getBoolean(getString(R.string.key_Fashion), false));
        cbArts.setChecked(sharedPref.getBoolean(getString(R.string.key_arts), false));

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvBeginDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                final int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePicker = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
                        Calendar cal = Calendar.getInstance();
                        cal.set(year, monthOfYear, dayOfMonth);
                        String formatedDate = sdf.format(cal.getTime());
                        tvBeginDate.setText(formatedDate);
                    }
                }, year, month, day);
                datePicker.getDatePicker().setMaxDate(new Date().getTime());
                datePicker.show();
            }
        });

        //Persist filter settings in shared preferences
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getContext());
                SharedPreferences.Editor editor = sharedPref.edit();

                String sortOrder = spSortOrder.getSelectedItem().toString();
                editor.putString(getString(R.string.key_sortOrder), sortOrder);

                String BeginDate = tvBeginDate.getText().toString();
                editor.putString(getString(R.string.key_beginDate), BeginDate);


                Boolean news_desk_arts = sharedPref.getBoolean(getString(R.string.key_arts), false);
                Boolean news_desk_fashion = sharedPref.getBoolean(getString(R.string.key_Fashion), false);
                Boolean news_desk_sports = sharedPref.getBoolean(getString(R.string.key_Sports), false);

                // to provide the option to persist settings
                editor.putBoolean(getString(R.string.key_Sports), cbSports.isChecked());
                editor.putBoolean(getString(R.string.key_Fashion), cbFashion.isChecked());
                editor.putBoolean(getString(R.string.key_arts), cbArts.isChecked());

                //to avoid this processing while sending the network request
                StringBuilder sb = new StringBuilder();
                if (news_desk_arts || news_desk_fashion || news_desk_sports) {
                    sb.append("news_desk:(");
                    if (news_desk_arts)
                        sb.append("\"Arts\" ");
                    if (news_desk_fashion)
                        sb.append("\"Fashion\" ");
                    if (news_desk_sports)
                        sb.append("\"Sports\"");
                    sb.append(")");
                }
                editor.putString(getString(R.string.news_desk), sb.toString());

                editor.apply();
                dismiss();
            }
        });

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvBeginDate.setText("");
                spSortOrder.setSelection(0);
                cbArts.setChecked(false);
                cbFashion.setChecked(false);
                cbSports.setChecked(false);
            }
        });
    }
}
