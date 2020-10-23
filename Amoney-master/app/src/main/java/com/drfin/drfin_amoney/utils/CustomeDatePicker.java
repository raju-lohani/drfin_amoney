package com.drfin.drfin_amoney.utils;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.widget.DatePicker;
import android.widget.EditText;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class CustomeDatePicker {

    public CustomeDatePicker() {
    }

    public static void showPicker(Context context, final EditText et) {
        final Calendar cldr = Calendar.getInstance();
        int day = cldr.get(Calendar.DAY_OF_MONTH);
        int month = cldr.get(Calendar.MONTH)+1;
        int year = cldr.get(Calendar.YEAR)-23;
        int maxyear = cldr.get(Calendar.YEAR)-55;
        DatePickerDialog picker = new DatePickerDialog(context, AlertDialog.THEME_HOLO_LIGHT,
                new DatePickerDialog.OnDateSetListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        view.setSpinnersShown(true);
                        et.setText(dayOfMonth + "-" + (monthOfYear +1) + "-" + year);
                    }
                }, year, month, day);
        try {
            String dateString = day+"/"+month+"/"+year;
            String maxdateString = day+"/"+month+"/"+maxyear;
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date date = sdf.parse(dateString);
            Date mindate = sdf.parse(maxdateString);
            assert date != null;
            long startDate = date.getTime();
            assert mindate != null;
            long minLongDate = mindate.getTime();
            picker.getDatePicker().setMaxDate(startDate);
            picker.getDatePicker().setMinDate(minLongDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        picker.show();
    }

    public static String getCurrentDate() {
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Date today = Calendar.getInstance().getTime();
        return dateFormat.format(today);
    }


    public static int getDaysDifference(Date fromDate, Date toDate)
    {

        if(fromDate==null||toDate==null)
            return 0;

        return (int)( (toDate.getTime() - fromDate.getTime()) / (1000 * 60 * 60 * 24));
    }
}
