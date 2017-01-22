package com.hipad.tracker.update;

import java.io.File;

import com.hipad.tracker.MyApplication;
import com.hipad.tracker.R;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.NotificationCompat;


public class AppNotificationControl {
	private NotificationManager mNotificationManager;
    private NotificationCompat.Builder mBuilder;
    private String urlPath;
    
    
    int progress;
    final int NOTIFYCATIONID = 1001;

    public AppNotificationControl(String urlPath) {
        initNotifycation();
        this.urlPath = urlPath;
    }

    /**
     * 
     * ��������: <br>
     * ��������ϸ������ ��ʼ��һ��builder Author: 14052012 zyn Date: 2014��11��6�� ����7:13:14
     * 
     * @see [�����/����](��ѡ)
     * @since [��Ʒ/ģ��汾](��ѡ)
     */
    private void initNotifycation() {
    	
    	
        mNotificationManager = (NotificationManager) MyApplication.getContextObject()
                .getSystemService(Context.NOTIFICATION_SERVICE);
        mBuilder = new NotificationCompat.Builder(
        		MyApplication.getContextObject());
        mBuilder.setWhen(System.currentTimeMillis()).setSmallIcon(
                R.drawable.ic_launcher);
    }

    /**
     * 
     * ��������: <br>
     * ��������ϸ������ �״�չʾ֪ͨ�� Author: 14052012 zyn Date: 2014��11��6�� ����7:13:39
     * 
     * @see [�����/����](��ѡ)
     * @since [��Ʒ/ģ��汾](��ѡ)
     */
    public void showProgressNotify() {
        mBuilder.setContentTitle("Waiting").setContentText("Progress:")
                .setTicker("Start Download");// ֪ͨ�״γ�����֪ͨ��������������Ч����
        Notification mNotification = mBuilder.build();
        mNotification.flags = Notification.FLAG_ONGOING_EVENT;//
        // ȷ�����ȵ�
        mBuilder.setProgress(100, progress, false); // �����������ʾ������ ����Ϊtrue���ǲ�ȷ�������ֽ�����Ч��
        mNotificationManager.notify(NOTIFYCATIONID, mNotification);
    }

    /** �������ؽ��� */
    public void updateNotification(int progress) {
        Notification mNotification = mBuilder.build();
        mNotification.flags = Notification.FLAG_ONGOING_EVENT;//
        mBuilder.setProgress(100, progress, false); // �����������ʾ������
        mBuilder.setContentText("loading...").setContentTitle("");
        if (progress >= 100) {
            mBuilder.setContentText("").setContentTitle("complete");
            new Thread(new Runnable() {
                public void run() {
                    Message msg = handler.obtainMessage();
                    msg.sendToTarget();
                }
            }).start();

        }
        mNotificationManager.notify(NOTIFYCATIONID, mNotification);
    }

    /**
     * �첽��װapk
     */
    Handler handler = new Handler() {

        public void handleMessage(android.os.Message msg) {
            Intent apkIntent = new Intent();
            apkIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            apkIntent.setAction(android.content.Intent.ACTION_VIEW);
            File apkFile = new File(urlPath);
            Uri uri = Uri.fromFile(apkFile);
            apkIntent.setDataAndType(uri,
                    "application/vnd.android.package-archive");
            MyApplication.getContextObject().startActivity(apkIntent);
            mNotificationManager.cancel(NOTIFYCATIONID);// ɾ��һ���ض���֪ͨID��Ӧ��֪ͨ
        };
    };
}
