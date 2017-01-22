package com.example.fragment;

import java.io.UnsupportedEncodingException;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.example.config.MyConfig;
import com.example.helper.HttpClientHelper;
import com.example.helper.JSONHelper;
import com.example.teabaike.R;
import com.example.widget.MyViewPager;
import com.example.widget.XListView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.LayoutParams;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ContentFragment extends BaseListFragment implements
		OnClickListener {

	private List<Map<String, Object>> list;

	

	private String urlStr;
	/** ȫ�ֱ���֮ͼƬ����map */
	protected Map<String, Bitmap> cacheImageMap;
	/** fragment��ʶ */
	private int fragmentIndex;

	/** ����ҳ�� */
	private int page = 1;

	
	/** ���viewPager��list */
	private List<ImageView> viewList;
	
	private ContentBaseadapter mABaseadapter;
	/** ����ϵĸ��ؼ���Ϊ��ȷ����С����ȡ */
	private RelativeLayout relative_fragment_content;
	/** �ֲ�����ϵ����� */
	private TextView textView_fragment_content_titleName;

	/** �ֲ�ͼƬ�ַ���title���� */
	private String[] titleArr = new String[3];

	/** ����listview�������ص����� */
	private List<Map<String, Object>> jsonList;

	private AdvertisementAdapter adsAdapter;

	/** ��ѡ��ť�飬��ЩС�� */
	private RadioGroup radioGroup_fragment;
	
	/** ��һҳ��Ҫչʾ�Ĺ�� */
	private MyViewPager viewPager_fragment;

	public ContentFragment() {
		fragmentIndex = 1;
	}

	public ContentFragment(String urlStr, List<Map<String, Object>> list,
			Map<String, Bitmap> cacheImageMap) {

		this.urlStr = urlStr;

		this.list = list;

		this.cacheImageMap = cacheImageMap;

		if (urlStr.equals(MyConfig.JSON_LIST_DATA_0)) {

			fragmentIndex = 1;

		} else {

			fragmentIndex = 0;
		}

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		if (list == null) {

			list = new ArrayList<Map<String, Object>>();

		}
		mABaseadapter = new ContentBaseadapter(getActivity(), list);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		super.onCreateView(inflater, container, savedInstanceState);

		if (urlStr.equals("")) {
			/* ����ǵ���ҳ����ô����ʾlistView���� */
			listview.setPullLoadEnable(false);
		}
		/* ���ؼ� */
		relative_fragment_content = (RelativeLayout) view
				.findViewById(R.id.relative_fragment_content);

		textView_fragment_content_titleName = (TextView) view
				.findViewById(R.id.textView_fragment_content_titleName);

		if (fragmentIndex == 1) {
			// ��һҳ����Ҫ����С���

			/* ��ʼ�����ֲ�������� */
			new MyTask(0).execute(MyConfig.JSON_URL);

			relative_fragment_content
					.setLayoutParams(new LinearLayout.LayoutParams(
							LayoutParams.MATCH_PARENT, getButtonViewPager()));
			/* �õ�viewPager���󣬲���������ҳ�� */
			viewPager_fragment = (MyViewPager) view
					.findViewById(R.id.viewPager_fragment);
			
			radioGroup_fragment = (RadioGroup) view
					.findViewById(R.id.radioGroup_fragment);

			viewList = new ArrayList<ImageView>();
			
			for (int i = 0; i < 3; i++) {
				
				ImageView imageView = new ImageView(getActivity());
				
			    imageView.setImageResource(R.drawable.ic_launcher);
			    
				imageView.setOnClickListener(this);
				
				viewList.add(imageView);
			}
			
			adsAdapter = new AdvertisementAdapter(viewList);

			viewPager_fragment.setAdapter(adsAdapter);

			viewPager_fragment
					.setOnPageChangeListener(new OnPageChangeListener() {

						@Override
						public void onPageSelected(int arg0) {

							radioGroup_fragment.getChildAt(arg0).performClick();

						}

						@Override
						public void onPageScrolled(int arg0, float arg1,
								int arg2) {

						}

						@Override
						public void onPageScrollStateChanged(int arg0) {

						}
					});

			

			for (int i = 0; i < radioGroup_fragment.getChildCount(); i++) {

				RadioButton rButton = (RadioButton) radioGroup_fragment
						.getChildAt(i);

				rButton.setTag(i);// ����tag���±��뻬��������±걣��һ��

				rButton.setBackgroundResource(R.drawable.slide_image_dot_c2);

				rButton.setButtonDrawable(new ColorDrawable(Color.TRANSPARENT));// ȥ����ѡ��ťǰ��ĵ���ɫΪ��

			}
			// ����ѡ��ť��ѡ�еļ���
			radioGroup_fragment
					.setOnCheckedChangeListener(new OnCheckedChangeListener() {

						@Override
						public void onCheckedChanged(RadioGroup group,
								int checkedId) {

							RadioButton rButton = (RadioButton) group
									.findViewById(checkedId);

							int index = (Integer) (rButton.getTag());

							viewPager_fragment.setCurrentItem(index);

							// �ı䵱ǰ��ʾ������
							textView_fragment_content_titleName
									.setText(titleArr[index]);

						}
					});
			radioGroup_fragment.getChildAt(0).performClick();// Ĭ�����õ�һ��ҳ���ǵ���״̬

		} else {

			relative_fragment_content.setVisibility(View.GONE);

		}
		listview.setXListViewListener(this);// ���ü�������������

		listview.setAdapter(mABaseadapter);

		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				String idStr = list.get((int) id).get("id").toString();

				SkipContentActivity(idStr);

			}
		});

		return view;
	}

	/**
	 * �����ֻ��ߴ���ViewPager�߶�
	 */
	private int getButtonViewPager() {
		Display display = getActivity().getWindowManager().getDefaultDisplay();
		int lenButton = 0;
		lenButton = display.getWidth() * 1 / 2;
		return lenButton;
	}

	@Override
	public void onRefresh() {

	}

	@Override
	public void onLoadMore() {
		
		new MyTask(2).execute(urlStr + "&page=" + (++page));

	}

	class ContentBaseadapter extends BaseAdapter {

		private Context context;
		private List<Map<String, Object>> list;

		public ContentBaseadapter(Context context,
				List<Map<String, Object>> list) {
			this.context = context;
			this.list = list;
		}

		public void addList(List<Map<String, Object>> list) {
			this.list.addAll(list);
		}

		@Override
		public int getCount() {

			return list.size();
		}

		@Override
		public Object getItem(int position) {

			return list.get(position);
		}

		@Override
		public long getItemId(int position) {

			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			ViewHolder mhHolder;

			if (convertView == null) {

				mhHolder = new ViewHolder();
				convertView = LayoutInflater.from(context).inflate(
						R.layout.item_listview, null);

				mhHolder.title = (TextView) convertView
						.findViewById(R.id.title);
				mhHolder.source = (TextView) convertView
						.findViewById(R.id.source);
				mhHolder.nickname = (TextView) convertView
						.findViewById(R.id.nickname);
				mhHolder.create_time = (TextView) convertView
						.findViewById(R.id.create_time);
				mhHolder.wap_thumb = (ImageView) convertView
						.findViewById(R.id.wap_thumb);

				convertView.setTag(mhHolder);

			} else {
				
				mhHolder = (ViewHolder) convertView.getTag();
			}

			String title = (String) list.get(position).get("title");
			String source = (String) list.get(position).get("source");
			String create_time = (String) list.get(position).get("create_time");
			String nickname = (String) list.get(position).get("nickname");
			String wap_thumb = list.get(position).get("wap_thumb").toString();
			
			mhHolder.title.setText(title);
			mhHolder.source.setText(source);
			mhHolder.create_time.setText(create_time);
			mhHolder.nickname.setText(nickname);
			
			
			/* �ж�Ҫ��Ҫ��ͼƬ��λ�� */
			if (wap_thumb == null || wap_thumb.equals("")) {
				
				mhHolder.wap_thumb.setVisibility(View.GONE);
				
			} else {
				
				mhHolder.wap_thumb.setVisibility(View.INVISIBLE);
			}

			if (cacheImageMap.get(wap_thumb) == null) {
				/* �����ڿ�ʼ�������磬������ͼƬ */
				 new MyTask(mhHolder.wap_thumb, wap_thumb,
				 1).execute(wap_thumb);
			} else {
				
				mhHolder.wap_thumb.setImageBitmap(cacheImageMap.get(wap_thumb));
				
				mhHolder.wap_thumb.setVisibility(View.VISIBLE);
			}

			return convertView;
		}

		class ViewHolder {

			private TextView title;
			private TextView source;
			private TextView nickname;
			private TextView create_time;
			private ImageView wap_thumb;

		}

	}

	class MyTask extends AsyncTask<String, Integer, Object> {

		/** ��Ϊ����������ͼƬ��ַ�ַ��� */
		private String urlStr;
		/** ��ʾͼƬ�Ŀؼ� */
		private ImageView image;
		/** ������ǣ�0����listviewСͼƬ��2������ҳ���� */
		private int flag;

		public MyTask(ImageView image, String urlStr, int flag) {
			this.urlStr = urlStr;
			this.image = image;
			this.flag = flag;
		}

		public MyTask(int flag) {
			this.flag = flag;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected Object doInBackground(String... params) {

			Object obj = null;

			byte[] bitmapByte = HttpClientHelper.loadByteFromURL(params[0]);

			switch (flag) {

			case 0:
				//�ֲ�ͼƬjosn�и��ֵ�ַ����
				jsonToList(bitmapByte,
						new String[] { "image_s", "id", "title" }, "data", flag);
				break;
			case 1:
				obj = bitmapByte;
				break;

			case 2:

				String jsonString = HttpClientHelper.loadTextFromURL(params[0],
						"UTF-8");// ������ʵõ�����
				List<Map<String, Object>> list = JSONHelper.jsonStringToList(
						jsonString, new String[] { "title", "source",
								"nickname", "create_time", "wap_thumb", "id" },
						"data");
				obj = list;

				break;

			default:

				obj = bitmapByte;
				break;
			}

			return obj;
		}

		@Override
		protected void onPostExecute(Object object) {
			super.onPostExecute(object);

			if (object == null) {

				return;

			}

			switch (flag) {

			case 1:

				byte[] result = (byte[]) object;

				Bitmap bitmap = BitmapFactory.decodeByteArray(result, 0,
						result.length);

				image.setImageBitmap(bitmap);

				image.setVisibility(View.VISIBLE);

				cacheImageMap.put(urlStr, bitmap);

				break;

			case 2:
				
				List<Map<String, Object>> list = (List<Map<String, Object>>) object;
				
				mABaseadapter.addList(list);
				
				mABaseadapter.notifyDataSetChanged();
				
				if (list.size() == 0) {
					/* ȷ����������û�������ˣ���ôlistView���������������� */
					listview.setPullLoadEnable(false);
				}

				break;
			case 3:
				getBitmap(urlStr, (byte[]) object, 0);
				break;
			case 4:
				getBitmap(urlStr, (byte[]) object, 1);
				break;
			case 5:
				getBitmap(urlStr, (byte[]) object, 2);
				break;
			default:
				break;
			}

		}

	}

	public Bitmap getBitmap(String urlStr, byte[] result, int i) {

		Bitmap bitmap = BitmapFactory.decodeByteArray(result, 0, result.length);

		ImageView imageView = (ImageView) viewList.get(i);

		imageView.setImageBitmap(bitmap);

		cacheImageMap.put(urlStr, bitmap);

		adsAdapter.notifyDataSetChanged();

		radioGroup_fragment.getChildAt(0).performClick();
		// �ı䵱ǰ��ʾ������ҲΪ��һҳ
		textView_fragment_content_titleName.setText(titleArr[0]);

		return bitmap;
	}



	class AdvertisementAdapter extends PagerAdapter {

		private List<ImageView> list = null;

		public AdvertisementAdapter(List<ImageView> list) {

			this.list = list;
		}

		@Override
		public int getCount() {

			if (list != null) {

				return list.size();

			}
			return 0;
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {

			container.removeView(list.get(position));
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {

			container.addView(list.get(position));// ÿһ��itemʵ��������

			return list.get(position);
		}

	}
/**
 * JSON��������
 * @param result
 * @param jsonStrings
 * @param string
 * @param flag
 */
	public void jsonToList(byte[] result, String[] jsonStrings, String string,
			int flag) {

		String str = "";
		
		String urlStr = null;
		
		try {
			
			str = new String(result, "UTF-8");
			
		} catch (UnsupportedEncodingException e) {
			
			e.printStackTrace();
		}

		jsonList = JSONHelper.jsonStringToList(str, jsonStrings, string);

		for (int i = 0; i < jsonList.size(); i++) {

			urlStr = jsonList.get(i).get("image_s").toString();

			String id = jsonList.get(i).get("id").toString();

			String title = jsonList.get(i).get("title").toString();

			titleArr[i] = title;

			ImageView imageView = (ImageView) viewList.get(i);

			imageView.setTag(id);

			if (cacheImageMap.get(urlStr) == null) {

				new MyTask(3 + i).execute(urlStr);// ����ÿһ��ͼƬ

			} else {

				imageView.setImageBitmap(cacheImageMap.get(urlStr));

				adsAdapter.notifyDataSetChanged();
			}

		}

	}
	
	
	@Override
	public void onClick(View v) {

		SkipContentActivity(v.getTag().toString());

	}

}
