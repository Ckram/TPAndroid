package com.ensim.crakm.monbudget.ChartUtil;

import com.ensim.crakm.monbudget.Model.Transaction;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.formatter.YAxisValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DecimalFormat;

/**
 * Created by Moi on 26/05/2016.
 */
public class EuroValueFormatter implements ValueFormatter, YAxisValueFormatter{

    protected DecimalFormat mFormat;

    public EuroValueFormatter() {
        mFormat = new DecimalFormat("###,###,###.#");
    }

    /**
     * Allow a custom decimalformat
     *
     * @param format
     */
    public EuroValueFormatter(DecimalFormat format) {
        this.mFormat = format;
    }

    // ValueFormatter
    @Override
    public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
        if (value==0)
            return "";
        return mFormat.format(value* Transaction.getSommeDepense()) + " €";
    }

    // YAxisValueFormatter
    @Override
    public String getFormattedValue(float value, YAxis yAxis) {
        if (value==0)
            return "";
        return mFormat.format(value* Transaction.getSommeDepense()) + " €";
    }
}
