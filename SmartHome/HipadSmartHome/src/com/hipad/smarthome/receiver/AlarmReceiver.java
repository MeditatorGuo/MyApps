package com.hipad.smarthome.receiver;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import com.hipad.smart.device.CommonDevice;
import com.hipad.smart.http.HttpUtil;
import com.hipad.smart.http.HttpUtil.ResponseResultHandler;
import com.hipad.smart.json.CmdResponse;
import com.hipad.smart.json.CmdResponse.ResponseData;
import com.hipad.smart.json.QueryDeviceInfoResponse.DeviceInfo;
import com.hipad.smart.json.ErrorCode;
import com.hipad.smart.json.QueryDeviceInfoResponse;
import com.hipad.smart.json.util.GsonInstance;
import com.hipad.smart.kettle.v14.KettleStatusInfo;
import com.hipad.smart.kettle.v14.OkBoilCmd;
import com.hipad.smart.kettle.v14.SpecificBoilCmd;
import com.hipad.smarthome.AppointMentActivity;
import com.hipad.smarthome.AppointMentItem;
import com.hipad.smarthome.MyApplication;
import com.hipad.smarthome.R;
import com.hipad.smarthome.utils.AppointMentDataBase;
import com.hipad.smarthome.utils.DeviceListCache;
//import com.hipad.smarthome.KettleActivity;

public class AlarmReceiver extends BroadcastReceiver {
	private final static String TAG = "AlarmReceiver";
	private String appoint_name = null;
	private Context mContext;
	private GetHandler gHandler; 
	private boolean startAppoint = false;
	private String userId = null;
	private AppointMentItem item = new AppointMentItem();
	
	private class GetHandler implements ResponseResultHandler<QueryDeviceInfoResponse> {

		@Override
		public void handle(boolean timeout, QueryDeviceInfoResponse response) {
			// TODO Auto-generated method stub
			String str = null;
			Calendar c = Calendar.getInstance();
			int year = c.get(Calendar.YEAR);
			int month = c.get(Calendar.MONTH) + 1;
			int day = c.get(Calendar.DAY_OF_MONTH);		
			int hour = c.get(Calendar.HOUR_OF_DAY);
			int minute = c.get(Calendar.MINUTE);
			int notfiy_id = year + month + day + hour + minute;
			if (!timeout) {
				if (response != null) {
					if (response.isSuccessful()) {
						DeviceInfo data = (DeviceInfo) response.getData();
						if (data != null) {
							int errorCode = data.getErrorCode();
							if (errorCode == ErrorCode.E_SUCCESS) {
								KettleStatusInfo kettleStatusInfo = new KettleStatusInfo(data.getResponseBody());
								if (kettleStatusInfo.getState() != KettleStatusInfo.KettleState.STATE_STANDBY) {
									str = "�װ�������,ˮ������ִ����������,�޷�Ϊ������ԤԼ";
									startAppoint = false;
								} else {
									startAppoint = true;
								}
							} else if (errorCode == ErrorCode.E_OFFLINE) {
								str = "�װ�������,�豸����,ԤԼʧ��!";
								startAppoint = false;
							} else if (errorCode == ErrorCode.E_TIMEOUT) {
								str = "�װ�������,�豸����,ԤԼʧ��!";
								startAppoint = false;
							} else {
								str = "�װ�������,����ԭ��,��ʵ����ϵ��������ˮ��,û�а취��������ԤԼ.";
								startAppoint = false;
							}
						} else {
							str = "�װ�������,����ԭ��,��ʵ����ϵ��������ˮ��,û�а취��������ԤԼ.";
							startAppoint = false;
						}
					} else {
						String msgStr = response.getMsg();
						if (msgStr.contains("δ֪�쳣")) {
							str = "�װ�������,����ԭ��,��ʵ����ϵ��������ˮ��,û�а취��������ԤԼ.";
						} else {
							str = "�װ�������,ԤԼ����ʧ��,ʧ��ԭ��:" + msgStr;
						}
						startAppoint = false;
					}
				} else if(response == null) {
					str = "�װ�������,����ԭ��,��ʵ����ϵ��������ˮ��,û�а취��������ԤԼ.";
					startAppoint = false;
				}
			} else {
				str = "�װ�������,����ʱ,ԤԼʧ��!";
				startAppoint = false;
			}			
			
			if (!startAppoint) {
				showErrorNotify(notfiy_id,str);
			} else {
				if (startAppoint) {
					if (item.getAppointType() == 0) {
						boil(item);
					} else if (item.getAppointType() == 1){
						boilAndWarm(item);
					}else if (item.getAppointType() == 2) {
						sceneBoil(item);
					}
				}
			}
		}
		
	}
	
	private HttpUtil.ResponseResultHandler<CmdResponse> mHandler = new HttpUtil.ResponseResultHandler<CmdResponse>() {

		@Override
		public void handle(boolean timeout, CmdResponse response) {
			String str = null;
			Calendar c = Calendar.getInstance();
			int year = c.get(Calendar.YEAR);
			int month = c.get(Calendar.MONTH) + 1;
			int day = c.get(Calendar.DAY_OF_MONTH);		
			int hour = c.get(Calendar.HOUR_OF_DAY);
			int minute = c.get(Calendar.MINUTE);
			int notfiy_id = year + month + day + hour + minute;
			if (!timeout) {
				if (response != null) {
					if (response.isSuccessful()) {
						ResponseData data = (ResponseData) response.getData();
						if (data != null) {
							int errorCode = data.getErrorCode();
							if (errorCode == ErrorCode.E_SUCCESS) {
								str = "�װ�������," + '"' + appoint_name + '"' + "ԤԼ�Ѿ���ʼ������.";
								KettleStatusInfo kettleStatusInfo = new KettleStatusInfo(
										data.getResponseBody());
								Log.i(TAG,"ˮ������״̬:"
										+ "\n��ǰ״̬:"
										+ kettleStatusInfo.getState()
										+ "\n��������:"
										+ kettleStatusInfo.getCurrFunc()
										+ "\n����ʣ��ʱ��:"
										+ kettleStatusInfo
												.getRemainOfTimeToKeepTempC()
										+ "\n�����¶�:"
										+ kettleStatusInfo.getTempCToBeKept()
										+ "\n��ǰˮ��:"
										+ kettleStatusInfo
												.getCurrentTemperature()
										+ "\nˮ��TDSֵ����:"
										+ kettleStatusInfo.getWaterQuality()
										+ "\n��ˮ��ʱ��:"
										+ kettleStatusInfo.getWorkedTime()
										+ "\n��ˮ����:"
										+ kettleStatusInfo.getBoiledTimes());
							} else if (errorCode == ErrorCode.E_OFFLINE) {
								str = "�װ�������,�豸����,ԤԼʧ��!";
							} else if (errorCode == ErrorCode.E_TIMEOUT) {
								str = "�װ�������,�豸����,ԤԼʧ��!";
							} else {
								str = "�װ�������,����ԭ��,��ʵ����ϵ��������ˮ��,û�а취��������ԤԼ.";
							}
						} else {
							str = "�װ�������,����ԭ��,��ʵ����ϵ��������ˮ��,û�а취��������ԤԼ.";
						}
					} else {
						String msgStr = response.getMsg();
						if (msgStr.contains("δ֪�쳣")) {
							str = "�װ�������,����ԭ��,��ʵ����ϵ��������ˮ��,û�а취��������ԤԼ.";
						} else {
							str = "�װ�������,ԤԼ����ʧ��,ʧ��ԭ��:" + msgStr;
						}
					}
				}
			} else {
				str = "�װ�������,����ʱ,ԤԼʧ��!";
			}
			
			if(!timeout && null != response){
				String json = GsonInstance.get().toJson(response);
				showSuccessNotify(notfiy_id,str);
				
			} else if (response == null) {
				String msg = "�װ�������,����ԭ��,��ʵ����ϵ��������ˮ��,û�а취��������ԤԼ.";
				showErrorNotify(notfiy_id,msg);
			}
		}
	};

	private void showSuccessNotify(int notfiy_id,String msg) {
		NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
		Notification noti = new Notification(R.drawable.logo_dl, "ԤԼ��ˮ����", System.currentTimeMillis());
		noti.flags = Notification.FLAG_INSISTENT;
        RemoteViews remoteView = new RemoteViews(mContext.getPackageName(),R.layout.notify_layout);  
        remoteView.setImageViewResource(R.id.image, R.drawable.logo_dl);  
        remoteView.setTextViewText(R.id.title , "���ܼҾ�");  
        remoteView.setTextViewText(R.id.content , msg);  
        noti.contentView = remoteView;  
        Intent operation = new Intent(mContext, mContext.getClass());
        PendingIntent contentIntent = PendingIntent.getActivity(mContext, R.string.app_name, operation, PendingIntent.FLAG_UPDATE_CURRENT);  
        noti.contentIntent = contentIntent;  
		notificationManager.notify(notfiy_id, noti);
	}
	
	private void showErrorNotify(int notfiy_id,String msg) {
		NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
		Notification noti = new Notification(R.drawable.logo_dl, "ԤԼ��ˮ����", System.currentTimeMillis());
		noti.flags = Notification.FLAG_INSISTENT;
        RemoteViews remoteView = new RemoteViews(mContext.getPackageName(),R.layout.notify_layout);  
        remoteView.setImageViewResource(R.id.image, R.drawable.logo_dl);  
        remoteView.setTextViewText(R.id.title , "���ܼҾ�");  
        remoteView.setTextViewText(R.id.content , msg);  
        noti.contentView = remoteView;  
        Intent operation = new Intent(mContext, mContext.getClass());
        PendingIntent contentIntent = PendingIntent.getActivity(mContext, R.string.app_name, operation, PendingIntent.FLAG_UPDATE_CURRENT);  
        noti.contentIntent = contentIntent;  
		notificationManager.notify(notfiy_id, noti);
	}
	
	private void boil(AppointMentItem item) {
		CommonDevice device = new CommonDevice(item.getGateWayId(), item.getDeviceId());

		OkBoilCmd cmd = new OkBoilCmd();
		cmd.setFunc(KettleStatusInfo.FUNC_BOIL);	
		cmd.syncMenu(item.getMenu1(), item.getMenu2());
		device.sendCmd(cmd, mHandler);
	}
	
	private void boilAndWarm(AppointMentItem item) {
		CommonDevice device = new CommonDevice(item.getGateWayId(), item.getDeviceId());

		OkBoilCmd cmd = new OkBoilCmd();
		cmd.setTempCToBeKept(item.getWarmTmp());
		cmd.setMinutesToKeepTempC(item.getWarmTime());
		cmd.setFunc(KettleStatusInfo.FUNC_KEEP_TEMPC);	
		cmd.syncMenu(item.getMenu1(), item.getMenu2());
		
		device.sendCmd(cmd, mHandler);
	}
	
	private void sceneBoil(AppointMentItem item) {
		CommonDevice device = new CommonDevice(item.getGateWayId(), item.getDeviceId());

		SpecificBoilCmd cmd = new SpecificBoilCmd();
		cmd.boil(true);
		cmd.setTempCToBeKept(item.getWarmTmp());
		cmd.setMinutesToKeepTempC(item.getWarmTime());
		cmd.syncMenu(item.getMenu1(), item.getMenu2());
		device.sendCmd(cmd, mHandler);
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
		if (MyApplication.user != null) {
			userId = MyApplication.user.getUserId();
		} else {
			return;
		}
		mContext = context;
		final AppointMentDataBase db = AppointMentDataBase.getInstance(context);
		ContentValues values = new ContentValues();
		Calendar c = Calendar.getInstance();
		Bundle bundle = intent.getExtras();
		String appoint_date = bundle.getString("appoint_date");
		String appoint_time = bundle.getString("appoint_time");
		String deviceId = bundle.getString("device_id");
		String gatewayId = bundle.getString("gateway_id");
		item = db.getAppointMentItem(appoint_time, appoint_date,deviceId,gatewayId,userId);
		Log.d(TAG,"appoint_time:" + appoint_time + "appoint_date:" + appoint_date + "deviceId:" + deviceId + "gatewayId:" + gatewayId + " usetId:" + userId);
		if (item != null) {
			appoint_name = item.getAppointName();
			CommonDevice mDevice = new CommonDevice(item.getGateWayId(), item.getDeviceId());
			gHandler = new GetHandler();
			mDevice.getDeviceInfo(gHandler);
			
			if (item.getIsEveryDay() == 1) {
				AlarmManager mAlarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
				c.setTimeInMillis(System.currentTimeMillis());
				c.add(Calendar.HOUR_OF_DAY,24);
				int newAppoint_year = c.get(Calendar.YEAR);
				int newAppoint_month = c.get(Calendar.MONTH) + 1;
				int newAppoint_day = c.get(Calendar.DAY_OF_MONTH);
				String new_appointD,new_appointM,new_appointDate;
				if (newAppoint_month < 10) {
					new_appointM = "0" + String.valueOf(newAppoint_month);
				} else {
					new_appointM = String.valueOf(newAppoint_month);
				}
				
				if (newAppoint_day < 10) {
					new_appointD = "0" + String.valueOf(newAppoint_day);
				} else {
					new_appointD = String.valueOf(newAppoint_day);
				}
				new_appointDate = String.valueOf(newAppoint_year) + new_appointM + new_appointD;
				values.put("appoint_date", new_appointDate);
				db.updateAppointMentItem(values, appoint_time, appoint_date,deviceId,gatewayId,userId);
				Intent alamIntent = new Intent(context.getApplicationContext(),AlarmReceiver.class);
				alamIntent.putExtra("gateway_id", item.gatewayID);
				alamIntent.putExtra("device_id", item.deviceID);
				alamIntent.putExtra("warm_tmp", item.warm_tmp);
				alamIntent.putExtra("warm_time",item.warm_time);
				alamIntent.putExtra("appoint_date",new_appointDate);
				alamIntent.putExtra("appointe_time",item.appoint_time);
				PendingIntent operation = PendingIntent.getBroadcast(context,Integer.parseInt(new_appointDate) + (Integer.parseInt(item.getAppointTime())*10000), alamIntent, PendingIntent.FLAG_UPDATE_CURRENT);
				mAlarmManager.set(AlarmManager.RTC_WAKEUP,c.getTimeInMillis(), operation);
				context.sendBroadcast(new Intent("com.android.updatelistview"));
			} else {
				values.put("appoint_start", 0);
				db.updateAppointMentItem(values, appoint_time, appoint_date,deviceId,gatewayId,userId);
				context.sendBroadcast(new Intent("com.android.updatelistview"));
			}
		}
	}
}
