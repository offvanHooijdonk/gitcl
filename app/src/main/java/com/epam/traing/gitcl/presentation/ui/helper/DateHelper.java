package com.epam.traing.gitcl.presentation.ui.helper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Yahor_Fralou on 3/7/2017 11:00 AM.
 */

public class DateHelper {
    private DateFormat dateFormatMedium = SimpleDateFormat.getDateTimeInstance(SimpleDateFormat.MEDIUM, SimpleDateFormat.SHORT, Locale.getDefault());
    private DateFormat dateFormatShort = SimpleDateFormat.getDateTimeInstance(SimpleDateFormat.SHORT, SimpleDateFormat.SHORT, Locale.getDefault());

    public String formatDateTimeShort(Date dateTime) {
        return dateFormatShort.format(dateTime);
    }

    public String formatDateTimeShort(long dateTimeMillis) {
        return formatDateTimeShort(new Date(dateTimeMillis));
    }

    public String formatDateTimeMedium(Date dateTime) {
        return dateFormatMedium.format(dateTime);
    }

    public String formatDateTimeMedium(long dateTimeMillis) {
        return formatDateTimeShort(new Date(dateTimeMillis));
    }
}
