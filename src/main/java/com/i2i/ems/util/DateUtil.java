package com.i2i.ems.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.Date;

public class DateUtil {
  public static int getYearsBetween(Date date1, Date date2) {
    if(date1 == null || date2 == null) {
      return -1;
    }
    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    LocalDate date1InLocalDate = LocalDate.parse(dateFormat.format(date1));
    LocalDate date2InLocalDate = LocalDate.parse(dateFormat.format(date2));
    return Period.between(date1InLocalDate, date2InLocalDate).getYears();
  }
}
