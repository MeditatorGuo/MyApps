package com.example.config;

public class MyConfig {

	/** �����Ƿ��ǵ�һ�����г���ı�� */
	public final static String IS_FIRST_RUN = "isFirstRun";
	/** ���ǵ�һ�����б�ʶ */
	public final static int NOT_FIRST = 1;
	/** �ǵ�һ�����б�ʶ */
	public final static int IS_FIRST = -1;
	
	/** �����������������url */
	public final static String JSON_URL = "http://sns.maimaicha.com/api?apikey=b4f4ee31a8b9acc866ef2afb754c33e6&format=json&method=news.getSlideshow";
	
	/** listview���ݽ���url */
	public final static String JSON_LIST_DATA_0 = "http://sns.maimaicha.com/api?apikey=b4f4ee31a8b9acc866ef2afb754c33e6&format=json&method=news.getHeadlines";
	/** listview���ݽ���url */
	public final static String JSON_LIST_DATA_1 = "http://sns.maimaicha.com/api?apikey=b4f4ee31a8b9acc866ef2afb754c33e6&format=json&method=news.getListByType&type=16";
	/** listview���ݽ���url */
	public final static String JSON_LIST_DATA_2 = "http://sns.maimaicha.com/api?apikey=b4f4ee31a8b9acc866ef2afb754c33e6&format=json&method=news.getListByType&type=52";
	/** listview���ݽ���url */
	public final static String JSON_LIST_DATA_3 = "http://sns.maimaicha.com/api?apikey=b4f4ee31a8b9acc866ef2afb754c33e6&format=json&method=news.getListByType&type=53";
	/** listview���ݽ���url */
	public final static String JSON_LIST_DATA_4 = "http://sns.maimaicha.com/api?apikey=b4f4ee31a8b9acc866ef2afb754c33e6&format=json&method=news.getListByType&type=54";
	/** ���������ַ */
	public static final String SEARCH = "http://sns.maimaicha.com/api?apikey=b4f4ee31a8b9acc866ef2afb754c33e6&format=json&method=news.searcListByTitle";
	/** ������ҳ�������ַ �����&id= */
	public static final String CONTENT = "http://sns.maimaicha.com/api?apikey=b4f4ee31a8b9acc866ef2afb754c33e6&format=json&method=news.getNewsContent";

}
