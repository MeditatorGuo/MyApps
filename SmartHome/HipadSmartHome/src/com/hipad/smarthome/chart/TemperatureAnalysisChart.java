package com.hipad.smarthome.chart;

import org.achartengine.ChartFactory;
import org.achartengine.chart.BarChart.Type;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.graphics.Typeface;
import android.view.View;

public class TemperatureAnalysisChart extends Chart {

	private double xThreshold = 9;
	private double yThreshold = 30;
	private int minUnit = 5;
	
	
	public TemperatureAnalysisChart(Context context) {
		super(context);
	}

	@Override
	public View getView(String[] xData, double[] yData, String xTitle, String yTitle, String chartTitle) {
		
		super.xDataTitles = xData;
		super.yDataValues = yData;
		checkLimitedValue();
		return getBarChart(xTitle, yTitle, chartTitle);
	}

	private View getBarChart(String xTitle, String yTitle, String chartTitle) {
		XYSeries series = new XYSeries(chartTitle);
		XYMultipleSeriesDataset Dataset = new XYMultipleSeriesDataset();
		Dataset.addSeries(series);

		XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();
		XYSeriesRenderer _renderer = new XYSeriesRenderer();
		mRenderer.addSeriesRenderer(_renderer);

		 mRenderer.setApplyBackgroundColor(true); //�趨������ɫ
	    mRenderer.setBackgroundColor(Color.LTGRAY); //�趨ͼ��Χ������ɫ
	    
	    mRenderer.setAxisTitleTextSize(24);
	    
		// ����ͼ�����߿�(��,��,��,��)
		mRenderer.setMargins(new int[] { 40, 50, 50, 20 });
		
		mRenderer.setXLabelsPadding(30);
		
		mRenderer.setMarginsColor(Color.WHITE); // �趨ͼ��Χ������ɫ
		mRenderer.setTextTypeface(null, Typeface.BOLD); // �趨����style

		mRenderer.setShowGrid(true); // �趨����
		mRenderer.setGridColor(Color.GRAY); // �趨������ɫ

		//mRenderer.setChartTitle(chartTitle); // �趨��ͷ����
		mRenderer.setXTitle(xTitle); // X������
		mRenderer.setYTitle(yTitle); // Y������
		mRenderer.setLabelsColor(Color.BLACK); // �趨��ͷ������ɫ
		mRenderer.setChartTitleTextSize(20); // �趨��ͷ���ִ�С
		mRenderer.setAxesColor(Color.BLACK); // �趨˫����ɫ
		mRenderer.setBarSpacing(0.5); // �趨bar��ľ���

		// Renderer.setXTitle(XTitle); //�趨X������
		// Renderer.setYTitle(YTitle); //�趨Y������
		mRenderer.setXLabelsColor(Color.BLACK); // �趨X��������ɫ
		mRenderer.setYLabelsColor(0, Color.BLACK); // �趨Y��������ɫ
		mRenderer.setXLabelsAlign(Align.CENTER); // �趨X����������
		//mRenderer.setYLabelsAlign(Align.CENTER); // �趨Y����������
		mRenderer.setXLabelsAngle(-25); // �趨X��������б��

		mRenderer.setZoomEnabled(false, false);
		mRenderer.setPanEnabled(false, false);
		
		mRenderer.setYLabels(12);
		
		// ������̶����ֵĴ�С
		mRenderer.setLabelsTextSize(18);
		// ����ͼ�����ִ�С
		mRenderer.setLegendTextSize(25);
		
		// �趨X�᲻��ʾ����, ���Գ�ʽ�趨����
		mRenderer.setXLabels(0);
		
		// �趨X����С,��ֵ
		mRenderer.setXAxisMin(0);
		mRenderer.setXAxisMax(xThreshold);
		
		// �趨Y����С,��ֵ
		mRenderer.setYAxisMin(0);
		mRenderer.setYAxisMax(getMaxYValue()>yThreshold?checkAxisMax(minUnit,getMaxYValue()):yThreshold);
		
		_renderer.setColor(Color.RED); // �趨Series��ɫ
		// yRenderer.setDisplayChartValues(true); //չ��Series��ֵ

		series.add(0, 0);
		mRenderer.addXTextLabel(0, "");
		for (int r = 0; r < yDataValues.length; r++) {
			mRenderer.addXTextLabel(r + 1, xDataTitles[r]);
			series.add(r + 1, yDataValues[r]);
		}
		//series.add(11, 0);
		//mRenderer.addXTextLabel(yData.length + 1, "");

		View view = ChartFactory.getBarChartView(context, Dataset, mRenderer, Type.DEFAULT);
		return view;
	}
}