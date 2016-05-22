package com.ensim.crakm.monbudget.Activites;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.ensim.crakm.monbudget.Model.Categorie;
import com.ensim.crakm.monbudget.Model.Transaction;
import com.ensim.crakm.monbudget.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

public class PieChartActivity extends AppCompatActivity {

    PieChart chart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pie_chart);

        chart = (PieChart) findViewById(R.id.chart);
        List<String> listCategories= new ArrayList<>(Categorie.categories.keySet());
        Log.d("chart",listCategories.toString() + "patate");
        int[] listNbTransacParCategorie = new int[listCategories.size()];
        float[] listPourcentages = new float[listCategories.size()];
        List<Entry> entries = new ArrayList<Entry>();
        for (int i = 0; i< listCategories.size();i++)
        {
            listNbTransacParCategorie[i]=0;
        }
        Log.d("chart",listNbTransacParCategorie.toString() + "carotte");
        for (Transaction t : Transaction.transactions)
        {
            int index = listCategories.indexOf(t.getCategorie().getNomCategorie());
            listNbTransacParCategorie[index]++;

        }
        for (int i = 0; i<listNbTransacParCategorie.length;i++)
        {
            Log.d("chart",listNbTransacParCategorie[i]+"");
            Log.d("chart",Transaction.transactions.size()+"size");
            listPourcentages[i]=(float)listNbTransacParCategorie[i]/Transaction.transactions.size()*100;
            entries.add(new Entry(listPourcentages[i],i));
            Log.d("chart",""+listPourcentages[i]);
        }
        PieDataSet dataSet = new PieDataSet(entries, "Election Results");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);
        ArrayList<Integer> colors = new ArrayList<Integer>();

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());

        dataSet.setColors(colors);
        PieData data = new PieData(listCategories, dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);


        chart.setData(data);
        chart.highlightValues(null);

        chart.invalidate();

    }
}
