package com.hipad.smarthome.chart;

import org.achartengine.ChartFactory;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.BasicStroke;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.Paint.Align;
import android.view.View;

public class UsageAnalysisChart extends Chart{

	private double yThreshold = 30;
	private int minUnit = 5;
	
	public UsageAnalysisChart(Context context) {
		super(context);
	}

	@Override
	public View getView(String[] xData, double[] yData, String xTitle, String yTitle, String chartTitle) {
		
		super.xDataTitles = xData;
		super.yDataValues = yData;
		checkLimitedValue();

		// X,Y����ֵ����
		XYMultipleSeriesDataset dataset = buildDataset(chartTitle);
		XYMultipleSeriesRenderer renderer = buildRenderer();
		yThreshold = getMaxYValue()>yThreshold?checkAxisMax(minUnit,getMaxYValue()):yThreshold;
		setChartSettings(renderer, xTitle, yTitle, 0, 0, 0, yThreshold, Color.BLACK);
		return ChartFactory.getLineChartView(context, dataset, renderer);
	}

	// ��������ͼ
	protected void setChartSettings(XYMultipleSeriesRenderer mRenderer,
			String xTitle, String yTitle, double xMin,
			double xMax, double yMin, double yMax, int axesColor) {

		/**
		 * Chart define
		 */
		mRenderer.setXTitle(xTitle); // X������
		mRenderer.setYTitle(yTitle); // Y������
		mRenderer.setXAxisMin(xMin); // X����ʾ��Сֵ
		//mRenderer.setXAxisMax(xMax); // X����ʾ���ֵ
		mRenderer.setYAxisMin(yMin); // Y����ʾ��Сֵ
		mRenderer.setYAxisMax(yMax); // Y����ʾ���ֵ

		mRenderer.setAntialiasing(true);
		// mRenderer.setChartTitle(title);// ����ͼ����
		mRenderer.setPanEnabled(false, false);// �Ƿ������϶�X��Y��

		// �Ƿ���ʾ����
		mRenderer.setShowGridX(true);
		mRenderer.setShowGridY(false);

		mRenderer.setShowLegend(true);
		//mRenderer.setXLabels(20);
		mRenderer.setYLabels(12);// ����y����ʾ12����,����setChartSettings�����ֵ����Сֵ�Զ������ļ��
		mRenderer.setZoomButtonsVisible(false);// �Ƿ���ʾ�Ŵ���С��ť

		mRenderer.setZoomEnabled(false, false);
		/**
		 * Layout define
		 */
		//mRenderer.setMarginsColor(Color.rgb(0xDE, 0xFF, 0xF9));// �趨������ɫ
		mRenderer.setMarginsColor(Color.WHITE);

		mRenderer.setAxisTitleTextSize(24);
		
		// ����ͼ�����߿�(��,��,��,��)
		mRenderer.setMargins(new int[] { 40, 50, 50, 20 });
		mRenderer.setXLabelsPadding(30);
		mRenderer.setXLabels(0);
		
		// ��������ͼ��������ֵĴ�С
		mRenderer.setChartTitleTextSize(40);

		// XY���ڵ���ɫ
		mRenderer.setBackgroundColor(Color.rgb(0xEF, 0xFF, 0xFC));

		// ������ɫ
		mRenderer.setGridColor(Color.GRAY);

		// �趨��`��ɫ
		mRenderer.setLabelsColor(Color.BLACK);

		// �趨��������ɫ
		mRenderer.setAxesColor(Color.BLACK);
		// mRenderer.setAxesColor(axesColor);

		// ������������ֵĴ�С
		//mRenderer.setAxisTitleTextSize(24);
		
		// �趨����style
		mRenderer.setTextTypeface(null, Typeface.BOLD);

		// �Ƿ�ɸı䱳����ɫ
		mRenderer.setApplyBackgroundColor(true);

		// X�������ɫ
		mRenderer.setXLabelsColor(Color.BLACK);

		// Y������ɫ
		mRenderer.setYLabelsColor(0, Color.BLACK);

		// ������̶����ֵĴ�С
		mRenderer.setLabelsTextSize(18);

		// ����ͼ�����ִ�С
		mRenderer.setLegendTextSize(25);

		// �̶�����̶ȱ�ע֮������λ�ù�ϵ
		mRenderer.setXLabelsAlign(Align.CENTER);

		// �̶�����̶ȱ�ע֮������λ�ù�ϵ
		mRenderer.setYLabelsAlign(Align.RIGHT);

		// X��Text��б�Ƕ�
		mRenderer.setXLabelsAngle(-30);
	}

	// ��������ͼ�ĸ�ʽ
	private XYMultipleSeriesRenderer buildRenderer() {
		XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
		XYSeriesRenderer seriesRenderer = new XYSeriesRenderer();

		seriesRenderer.setLineWidth(5);
		seriesRenderer.setStroke(BasicStroke.SOLID);
		seriesRenderer.setGradientEnabled(true);

		//X��Text
		for(int i=1;i<xDataTitles.length;i++)
			renderer.addXTextLabel(i, xDataTitles[i]);
		
		// ���ߵ���ɫ
		seriesRenderer.setColor(Color.BLUE);
		// seriesRenderer.setColor(Color.parseColor("#EEB422"));

		// ���ߵ����״
		seriesRenderer.setPointStyle(PointStyle.DIAMOND);

		// �Ƿ�ʵ��
		seriesRenderer.setFillPoints(true);

		// ���������߼���ͼ����ʾ
		renderer.addSeriesRenderer(seriesRenderer);

		return renderer;
	}

	private XYMultipleSeriesDataset buildDataset(String title) {
		XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
		XYSeries series = new XYSeries(title); // ����ÿ���ߵ���������

		for (int k = 1; k < xDataTitles.length; k++)
			series.add(k, yDataValues[k]);
		dataset.addSeries(series);

		return dataset;
	}
}