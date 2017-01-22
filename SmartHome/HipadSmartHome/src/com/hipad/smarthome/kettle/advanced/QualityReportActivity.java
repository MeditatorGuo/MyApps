package com.hipad.smarthome.kettle.advanced;

import java.io.File;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.BasicStroke;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hipad.smart.device.CommonDevice;
import com.hipad.smart.local.device.Device;
import com.hipad.smart.local.service.DeviceController;
import com.hipad.smarthome.BaseActivity;
import com.hipad.smarthome.MyApplication;
import com.hipad.smarthome.R;
import com.hipad.smarthome.kettle.dao.WaterLevelDao;
import com.hipad.smarthome.kettle.statistics.entity.WaterLevel;

/**
 * ��ˮ��������
 * 
 * @author guowei
 *
 */

public class QualityReportActivity extends BaseActivity implements IFunction,
		OnClickListener {
	public static final String MYACTION = "QualityReportActivity_action";
	private LinearLayout chartLayout;
	private ImageButton backBtn;
	private TextView title;
	private TextView notifyTxt;
	private Device localDevice = null;

	private CommonDevice cloudDevice = null;
	private DeviceController controller;
	private Context mContext;

	List<WaterLevel> datas = null;

	String deviceId = null;

	WaterLevelDao levelDao = null;
	double avg = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.quality_reporter_layout);
		mContext = this;
		levelDao = new WaterLevelDao(mContext);
		controller = new DeviceController();
		initViews();

	}

	private void initViews() {

		if (getIntent().getParcelableExtra("device") instanceof Device) {
			localDevice = getIntent().getParcelableExtra("device");

			deviceId = localDevice.getId();
			controller.init(localDevice);
		} else {

			cloudDevice = getIntent().getParcelableExtra("device");
			deviceId = cloudDevice.getDeviceId();
		}

		String titleStr = getIntent().getStringExtra("title");
		backBtn = (ImageButton) findViewById(R.id.leftBtn);
		backBtn.setOnClickListener(this);

		notifyTxt = (TextView) findViewById(R.id.notify_text);
		title = (TextView) findViewById(R.id.titleTxt);
		title.setText(titleStr);
		chartLayout = (LinearLayout) findViewById(R.id.chart_layout);

		chartLayout.addView(getView(mContext));

		showTips(avg);
	}

	private void showTips(double avg) {

		if (0 <= avg && avg <= 200) {

			notifyTxt.setText(R.string.waterlevel_excellent);

		} else if (avg < 500) {
			notifyTxt.setText(R.string.waterlevel_good);
		} else if (avg < 1000) {
			notifyTxt.setText(R.string.waterlevel_preferably);
		} else {
			notifyTxt.setText(R.string.waterlevel_bad);
		}

	}

	private GraphicalView getView(Context context) {

		// query data from database
		double[] xData = new double[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12,
				13, 14, 15, 16, 17, 18, 19, 20 };

		datas = levelDao.obtainWaterLevelList(MyApplication.user.getUserId(),
				deviceId);
		double[] yData = new double[20];
		double sum = 0, max = 0;
		for (int i = 0; i < datas.size(); i++) {

			yData[i] = datas.get(i).getLevel();
			if (max < yData[i]) {
				max = yData[i];
			}
			sum = sum + yData[i];
		}
		if (datas.size() == 0) {
			avg = 0;
		} else {
			avg = sum / datas.size();// ��ȡƽ��ֵ
		}

		double[] _yData = new double[20];
		for (int i = 0; i < _yData.length; i++) {
			_yData[i] = avg;
		}

		List<double[]> xValues = new ArrayList<double[]>();
		xValues.add(xData);
		xValues.add(xData);
		List<double[]> yValues = new ArrayList<double[]>();
		yValues.add(yData);
		yValues.add(_yData);

		// ���嵥λ����
		String[] unitName = new String[] { "ˮ��", "ƽ��ֵ" };
		String title = "��ˮˮ�ʷ���";
		String xTilte = "��ˮ����";
		String yTitle = "ˮ��TDSֵ";

		int[] colors = new int[] { Color.BLUE, setLineColor(avg) };
		PointStyle[] styles = new PointStyle[] { PointStyle.DIAMOND,
				PointStyle.POINT };
		// X,Y����ֵ����
		XYMultipleSeriesDataset dataset = buildDataset(unitName, xValues,
				yValues);
		XYMultipleSeriesRenderer renderer = buildRenderer(colors, styles);
		setChartSettings(renderer, title, xTilte, yTitle, 0, 20, 0,
				max > 0 ? max + 50 : 100, Color.BLACK);

		return ChartFactory.getLineChartView(context, dataset, renderer);
	}

	protected void setChartSettings(XYMultipleSeriesRenderer mRenderer,
			String title, String xTitle, String yTitle, double xMin,
			double xMax, double yMin, double yMax, int axesColor) {

		mRenderer.setXTitle(xTitle); // X������
		mRenderer.setYTitle(yTitle); // Y������
		mRenderer.setXAxisMin(xMin); // X����ʾ��Сֵ
		mRenderer.setXAxisMax(xMax); // X����ʾ���ֵ
		mRenderer.setYAxisMin(yMin); // Y����ʾ��Сֵ
		mRenderer.setYAxisMax(yMax); // Y����ʾ���ֵ

		mRenderer.setAntialiasing(true);
		// mRenderer.setChartTitle(title);// ����ͼ����
		mRenderer.setPanEnabled(false, false);// �Ƿ������϶�X��Y��
		// �Ƿ���ʾ����
		mRenderer.setShowGridX(true);
		mRenderer.setShowGridY(false);
		mRenderer.setShowLegend(true);
		mRenderer.setXLabels(20);
		mRenderer.setYLabels(15);// ����y����ʾ10����,����setChartSettings�����ֵ����Сֵ�Զ������ļ��
		mRenderer.setZoomButtonsVisible(false);// �Ƿ���ʾ�Ŵ���С��ť
		mRenderer.setZoomEnabled(false, false);
		/**
		 * Layout define
		 */
		// mRenderer.setMarginsColor(Color.rgb(0xDE, 0xFF, 0xF9));// �趨������ɫ
		mRenderer.setMarginsColor(Color.WHITE);

		// ����ͼ�����߿�(��,��,��,��)
		mRenderer.setMargins(new int[] { 40, 50, 50, 20 });

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
		mRenderer.setAxisTitleTextSize(24);

		// �Ƿ�ɸı䱳����ɫ
		mRenderer.setApplyBackgroundColor(true);

		// X�������ɫ
		mRenderer.setXLabelsColor(Color.BLACK);

		// Y������ɫ
		mRenderer.setYLabelsColor(0, Color.BLACK);

		// ������̶����ֵĴ�С
		mRenderer.setLabelsTextSize(18);

		// ����ͼ�����ִ�С(����ѹ ��)
		mRenderer.setLegendTextSize(25);

		// �̶�����̶ȱ�ע֮������λ�ù�ϵ
		mRenderer.setXLabelsAlign(Align.CENTER);

		// �̶�����̶ȱ�ע֮������λ�ù�ϵ
		mRenderer.setYLabelsAlign(Align.RIGHT);

		// X��Text��б�Ƕ�
		mRenderer.setXLabelsAngle(0);

	}

	/**
	 * ����ͼ(���ݼ�) : ��������ͼͼ�����ݼ�
	 * 
	 * @param ����ı���
	 * @param xValues
	 *            x�������
	 * @param yValues
	 *            y�������
	 * @return XY�����ݼ�
	 */

	protected XYMultipleSeriesDataset buildDataset(String[] titles,
			List<double[]> xValues, List<double[]> yValues) {

		// ���ݼ�
		XYMultipleSeriesDataset mDataset = new XYMultipleSeriesDataset();
		addXYSeries(mDataset, titles, xValues, yValues, 0);
		return mDataset;

	}

	/**
	 * ����ͼ(�����÷���) : ��� XY ���������� �� XYMultipleSeriesDataset ���ݼ���
	 * 
	 * @param dataset
	 *            ���� XY ���ݼ����, �൱�뷵��ֵ�ڲ�����
	 * @param titles
	 *            Ҫ����ı���
	 * @param xValues
	 *            x�����ݼ���
	 * @param yValues
	 *            y�����ݼ���
	 * @param scale
	 *            ����
	 * 
	 *            titles ������� �� xValues, yValues ������ͬ tittle �� һ��ͼ������ж�������,
	 *            ÿ�����߶���һ������ XYSeries ������ͼ�е� һ������, ���з�װ�� ��������, X���Y������
	 */
	public void addXYSeries(XYMultipleSeriesDataset dataset, String[] titles,
			List<double[]> xValues, List<double[]> yValues, int scale) {

		int length = titles.length; /* ��ȡ������� */
		for (int i = 0; i < length; i++) {
			XYSeries series = new XYSeries(titles[i], scale); /* ������������ */
			double[] xV = xValues.get(i); /* ��ȡ�������ߵ�x���������� */
			double[] yV = yValues.get(i); /* ��ȡ�������ߵ�y���������� */
			int seriesLength = xV.length;
			for (int k = 0; k < seriesLength; k++) {
				series.add(xV[k], yV[k]); /* ���������ߵ� x,y �������ŵ� �������������� */
			}
			dataset.addSeries(series); /* �������������ݴ�ŵ� ͼ�����ݼ��� */

		}

	}

	/**
	 * ����ͼ(��Ⱦ��) : ��������ͼͼ����Ⱦ��
	 * 
	 * @param ÿ������Ҫ��Ⱦ����ɫ
	 *            , ����Щ��ɫ��������
	 * @param ÿ�����߻��Ƶ�ķ��
	 * @return ������Ⱦ������
	 */

	protected XYMultipleSeriesRenderer buildRenderer(int[] colors,
			PointStyle[] styles) {

		XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();/* ��������ͼͼ����Ⱦ�� */

		setRenderer(renderer, colors, styles);
		return renderer;

	}

	/**
	 * ����ͼ(��Ⱦ�� - �����÷���) : ������������Ⱦ��
	 * 
	 * @param renderer
	 *            ���õ���Ⱦ������, ��������൱�뷵��ֵ, ������Ⱦ��������������������
	 * @param colors
	 *            Ҫ��Ⱦ����ɫ����
	 * @param styles
	 *            Ҫ��Ⱦ����ʽ����
	 */
	protected void setRenderer(XYMultipleSeriesRenderer renderer, int[] colors,
			PointStyle[] styles) {

		renderer.setAxisTitleTextSize(16); /* ����XY����������С */
		renderer.setChartTitleTextSize(20); /* ���ñ��������ִ�С */
		renderer.setLabelsTextSize(15); /* ���ñ�ǩ���ִ�С */
		renderer.setLegendTextSize(15); /* ����˵�����ִ�С */
		renderer.setPointSize(5f); /* ���õ�Ĵ�С */
		renderer.setMargins(new int[] { 20, 30, 15, 20 }); /* ���� margin, ��λ���� */
		int length = colors.length; /* ��ȡ��Ⱦ���ĸ���, ���ж��������� */
		for (int i = 0; i < length; i++) {
			XYSeriesRenderer r = new XYSeriesRenderer(); /* �������ߵ���Ⱦ�� */
			r.setColor(colors[i]); /* Ϊ����������Ⱦ������������ɫ */
			r.setPointStyle(styles[i]); /* Ϊ����������Ⱦ���������߷�� */

			r.setLineWidth(5);
			r.setStroke(BasicStroke.SOLID);
			r.setGradientEnabled(true);
			// �Ƿ�ʵ��
			r.setFillPoints(true);
			renderer.addSeriesRenderer(r); /* ������������Ⱦ�����õ���Ⱦ�������� */
		}
	}

	@Override
	public String getName() {
		return "��ˮ��������";
	}

	@Override
	public Intent execute(Context context) {
		Intent intent = new Intent();
		intent.setAction(MYACTION);
		return intent;
	}

	@Override
	public boolean isForResult() {
		return false;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.leftBtn:
			finish();
			break;
		default:
			break;
		}

	}

	private int setLineColor(double tds) {
		int color = Color.rgb(111, 203, 125);
		if (tds * 10 <= 200) {
			color = Color.rgb(111, 203, 125);
		} else if (tds * 10 <= 500) {
			color = Color.rgb(37, 183, 188);
		} else if (tds * 10 <= 1000) {
			color = Color.rgb(255, 188, 0);
		} else {
			color = Color.rgb(229, 0, 17);
		}
		return color;
	}

}
