package com.ensim.crakm.monbudget.Activites;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ensim.crakm.monbudget.ChartUtil.EuroValueFormatter;
import com.ensim.crakm.monbudget.Model.Categorie;
import com.ensim.crakm.monbudget.Model.Transaction;
import com.ensim.crakm.monbudget.R;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

public class PieChartActivity extends android.support.v4.app.Fragment {

    PieChart chart;
    PieData data;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setData();
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view,savedInstanceState);
        chart = (PieChart) view.findViewById(R.id.chart);
        chart.setData(data);
        chart.setHoleColor(getResources().getColor(R.color.colorPrimaryLight)/*Color.parseColor("#C8E6C9")*/);
        chart.highlightValues(null);
        chart.setUsePercentValues(false);
        chart.getLegend().setEnabled(false);
        chart.invalidate();
        

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_pie_chart, parent, false);
    }

    private void setData()
    {
        List<String> listCategories= new ArrayList<>(Categorie.categories.keySet());
        List<Entry> entries = new ArrayList<Entry>();
        List<Transaction> transactions = Transaction.getTransactionsNeg();
        List<String> listCategoriesUsed = new ArrayList<String>();

        for (int i = 0; i< listCategories.size();i++)
        {
            for(Transaction t : transactions)
            {
                if (t.getCategorie().getNomCategorie()==listCategories.get(i) && !listCategoriesUsed.contains(listCategories.get(i)))
                    listCategoriesUsed.add(listCategories.get(i));

            }

        }
        int[] listMontantParCategorie = new int[listCategoriesUsed.size()];
        float[] listPourcentages = new float[listCategoriesUsed.size()];
        for (Transaction t : Transaction.getTransactionsNeg())
        {
            int index = listCategoriesUsed.indexOf(t.getCategorie().getNomCategorie());
            listMontantParCategorie[index]+= t.getMontant();

        }
        for (int i = 0; i<listMontantParCategorie.length;i++)
        {
            Log.d("chart",listMontantParCategorie[i]+"");

            listPourcentages[i]=(float)listMontantParCategorie[i]/Transaction.getSommeDepense();
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
        data = new PieData(listCategoriesUsed, dataSet);
        data.setValueFormatter(new EuroValueFormatter());
        data.setValueTextSize(15f);
        data.setValueTextColor(Color.BLACK);
    }
}
