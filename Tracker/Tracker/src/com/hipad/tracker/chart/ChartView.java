package com.hipad.tracker.chart;

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
/**
 * 
 * @author guowei
 *
 */
public class ChartView extends Chart{

	private double yThreshold = 10;
	private double minUnit = 15;
	
	public ChartView(Context context) {
		super(context);
	}

	@Override
	public View getView(String[] xData, double[] yData, String xTitle, String yTitle, String chartTitle, int MaxLimite) {
		
		super.xDataTitles = xData;
		super.yDataValues = yData;
		checkLimitedValue();

		// X,Y����ֵ����
		XYMultipleSeriesDataset dataset = buildDataset(chartTitle);
		XYMultipleSeriesRenderer renderer = buildRenderer();
		yThreshold = getMaxYValue()>MaxLimite?checkAxisMax(minUnit,getMaxYValue())+500:MaxLimite;
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
		mRenderer.setShowGridX(false);
		mRenderer.setShowGridY(false);

		mRenderer.setShowLegend(true);
		//mRenderer.setXLabels(20);
		mRenderer.setYLabels(2);// ����y����ʾ12����,����setChartSettings�����ֵ����Сֵ�Զ������ļ��
		mRenderer.setZoomButtonsVisible(false);// �Ƿ���ʾ�Ŵ���С��ť

		mRenderer.setZoomEnabled(false, false);
		/**
		 * Layout define
		 */
		//mRenderer.setMarginsColor(Color.rgb(0xDE, 0xFF, 0xF9));// �趨������ɫ
		mRenderer.setMarginsColor(Color.rgb(0x00, 0xB8, 0xCC));

		mRenderer.setAxisTitleTextSize(24);
		
		// ����ͼ�����߿�(��,��,��,��)
		mRenderer.setMargins(new int[] { 0, -10, 0, 10 });
		mRenderer.setXLabelsPadding(30);
		mRenderer.setXLabels(0);
		
		// ��������ͼ��������ֵĴ�С
		mRenderer.setChartTitleTextSize(15);

		// XY���ڵ���ɫ
		mRenderer.setBackgroundColor(Color.rgb(0x00, 0xB8, 0xCC));

		// ������ɫ
		mRenderer.setGridColor(Color.WHITE);

		// �趨��`��ɫ
		mRenderer.setLabelsColor(Color.BLACK);

		// �趨��������ɫ
		mRenderer.setAxesColor(Color.WHITE);
		// mRenderer.setAxesColor(axesColor);

		// ������������ֵĴ�С
		//mRenderer.setAxisTitleTextSize(24);
		
		// �趨����style
		mRenderer.setTextTypeface(null, Typeface.BOLD);

		// �Ƿ�ɸı䱳����ɫ
		mRenderer.setApplyBackgroundColor(true);

		// X�������ɫ
		mRenderer.setXLabelsColor(Color.WHITE);

		// Y������ɫ
		mRenderer.setYLabelsColor(0, Color.rgb(0x00, 0xB8, 0xCC));

		// ������̶����ֵĴ�С
		mRenderer.setLabelsTextSize(15);

		// ����ͼ�����ִ�С
		mRenderer.setLegendTextSize(25);

		// �̶�����̶ȱ�ע֮������λ�ù�ϵ
		mRenderer.setXLabelsAlign(Align.CENTER);

		// �̶�����̶ȱ�ע֮������λ�ù�ϵ
		mRenderer.setYLabelsAlign(Align.RIGHT);

		// X��Text��б�Ƕ�
		mRenderer.setXLabelsAngle(0);
	}

	// ��������ͼ�ĸ�ʽ
	private XYMultipleSeriesRenderer buildRenderer() {
		XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
		XYSeriesRenderer seriesRenderer = new XYSeriesRenderer();

		seriesRenderer.setLineWidth(2);
		seriesRenderer.setStroke(BasicStroke.SOLID);
		seriesRenderer.setGradientEnabled(true);
		

		//X��Text
		for(int i=1;i<xDataTitles.length;i++)
			renderer.addXTextLabel(i, xDataTitles[i]);
		
		seriesRenderer.setDisplayChartValues(true);
		seriesRenderer.setDisplayChartValuesDistance(30);
		// ���ߵ���ɫ
		seriesRenderer.setColor(Color.WHITE);
		// seriesRenderer.setColor(Color.parseColor("#EEB422"));

		// ���ߵ����״
		seriesRenderer.setPointStyle(PointStyle.DIAMOND);

		// �Ƿ�ʵ��
		seriesRenderer.setFillPoints(false);

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