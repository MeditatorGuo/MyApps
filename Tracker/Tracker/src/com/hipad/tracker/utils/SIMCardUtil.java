package com.hipad.tracker.utils;

import android.content.Context;
import android.telephony.TelephonyManager;

public class SIMCardUtil {
	 /**
     * TelephonyManager�ṩ�豸�ϻ�ȡͨѶ������Ϣ����ڡ� Ӧ�ó������ʹ������෽��ȷ���ĵ��ŷ����̺͹��� �Լ�ĳЩ���͵��û�������Ϣ��
     * Ӧ�ó���Ҳ����ע��һ�����������绰��״̬�ı仯������Ҫֱ��ʵ���������
     * ʹ��Context.getSystemService(Context.TELEPHONY_SERVICE)����ȡ������ʵ����
     */
    private TelephonyManager telephonyManager;
    /**
     * �����ƶ��û�ʶ����
     */
    private String IMSI;
 
    public SIMCardUtil(Context context) {
        telephonyManager = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
    }
 
    /**
     * Role:��ȡ��ǰ���õĵ绰����
     */
    public String getNativePhoneNumber() {
        String NativePhoneNumber=null;
        NativePhoneNumber=telephonyManager.getLine1Number();
        return NativePhoneNumber;
    }
 
    /**
     * Role:Telecom service providers��ȡ�ֻ���������Ϣ <BR>
     * ��Ҫ����Ȩ��<uses-permission
     * android:name="android.permission.READ_PHONE_STATE"/> <BR>
     *
     */
    public String getProvidersName() {
        // ����Ψһ���û�ID;�������ſ��ı�������
        IMSI = telephonyManager.getSubscriberId();
        
        return IMSI;
    }
}
