package com.hipad.smarthome.adapter;

/**
 * @author EthanChung
 */

import java.util.TimerTask;

import com.hipad.smart.device.CommonDevice;
import com.hipad.smart.http.HttpUtil.ResponseResultHandler;
import com.hipad.smart.json.ErrorCode;
import com.hipad.smart.json.QueryDeviceInfoResponse;
import com.hipad.smart.json.QueryDeviceInfoResponse.DeviceInfo;
import com.hipad.smart.kettle.v14.KettleStatusInfo;
import android.content.Context;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class KettleHolder extends TimerTask implements ResponseResultHandler<QueryDeviceInfoResponse>{
	private String TAG = "KettleHolder";
	public ImageView imgIcon, status_network, temperature_unit, edit,delete;
	public TextView name, status_operation, temperature;
	public Context context;
	public CommonDevice device;
	private KettleStatusInfo kettleStatusInfo;

	private void updateView(){
		
		//TODO WangBaoMin need to update API of <class>KettleStatusInfo</class>
		//byte sta = kettleStatusInfo.getState();
		//String _sta = sta==KettleStatusInfo.FUNC_STANDBY?"���C":sta==KettleStatusInfo.
		//status_operation.setText("");
		temperature.setText(String.valueOf(kettleStatusInfo.getTempCToBeKept()) + "��C");
	}
	
	@Override
	public void run() {
		device.getDeviceInfo(this);
	}
	
	@Override
	public void handle(boolean timeout, QueryDeviceInfoResponse response) {
		boolean successTag = false;
		if (!timeout) {
			if (response != null) {
				if (response.isSuccessful()) {
					DeviceInfo data = (DeviceInfo) response.getData();
					if (data != null) {
						int errorCode = data.getErrorCode();
						Log.d(TAG,"errorcode = " + data.getErrorCode());
						if (errorCode == ErrorCode.E_SUCCESS) {
							kettleStatusInfo = new KettleStatusInfo(data.getResponseBody());
							Log.d(TAG,"ˮ������״̬:"
									+ "\n��ǰ״̬:"
									+ kettleStatusInfo.getState()
									+ "\n��������:"
									+ kettleStatusInfo.getCurrFunc()
									+ "\n����ʣ��ʱ��:"
									+ kettleStatusInfo.getRemainOfTimeToKeepTempC()
									+ "\n�����¶�:"
									+ kettleStatusInfo.getTempCToBeKept()
									+ "\n��ǰˮ��:"
									+ kettleStatusInfo.getCurrentTemperature()
									+ "\nˮ��TDSֵ����:"
									+ kettleStatusInfo.getWaterQuality()
									+ "\n��ˮ��ʱ��:"
									+ kettleStatusInfo.getWorkedTime()
									+ "\n��ˮ����:"
									+ kettleStatusInfo.getBoiledTimes());
							updateView();
							successTag = true;
						} else if (errorCode == ErrorCode.E_OFFLINE) {
							Log.e(TAG,"��ȡ����ʧ��,�豸������!");
						} else if (errorCode == ErrorCode.E_TIMEOUT) {
							Log.e(TAG,"��ȡ����ʧ��,����ʱ!");
							//Toast.makeText(context,context.getString(R.string.timeout_hint),Toast.LENGTH_SHORT).show();
						} else {
							Log.e(TAG,"��ȡ����ʧ��,������룺" + errorCode);
						}
					} else {
						Log.e(TAG,"��ȡ������Ϊ��!");
					}
				} else {
					String msgStr = response.getMsg();
					Log.e(TAG,"msgStr = " + msgStr);
					//Toast.makeText(context,"��ȡ����ʧ��," + msgStr,Toast.LENGTH_SHORT).show();
				}
			} else {
				//Toast.makeText(context,context.getString(R.string.neterror_hint),Toast.LENGTH_SHORT).show();
			}
		} else {
			//Toast.makeText(context,context.getString(R.string.timeout_hint),Toast.LENGTH_SHORT).show();
		}
		if (!successTag) {
			kettleStatusInfo = null;
		}
	}
}