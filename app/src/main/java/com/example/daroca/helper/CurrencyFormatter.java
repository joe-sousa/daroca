package com.example.daroca.helper;

import java.text.NumberFormat;

public class CurrencyFormatter {
  public static String formatCurrency(double value){
    NumberFormat format = NumberFormat.getCurrencyInstance();
    format.setMaximumFractionDigits(2);

    return format.format(value);
  }
}
