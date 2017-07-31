package test.push;

import cn.jpush.api.common.ClientConfig;
import cn.jpush.api.common.connection.HttpProxy;
import cn.jpush.api.common.resp.APIConnectionException;
import cn.jpush.api.common.resp.APIRequestException;
import cn.jpush.api.device.DeviceClient;
import cn.jpush.api.push.PushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;
import cn.jpush.api.report.ReportClient;
import cn.jpush.api.schedule.ScheduleClient;
import java.util.Map;

public class JPushClient1{

    private final PushClient _pushClient;
    private final ReportClient _reportClient;
    private final DeviceClient _deviceClient;
    private final ScheduleClient _scheduleClient;
	
	
    public JPushClient1(String masterSecret, String appKey){
        _pushClient = new PushClient(masterSecret, appKey);
        _reportClient = new ReportClient(masterSecret, appKey);
        _deviceClient = new DeviceClient(masterSecret, appKey);
        _scheduleClient = new ScheduleClient(masterSecret, appKey);
    }

    public JPushClient1(String masterSecret, String appKey, HttpProxy proxy, ClientConfig conf){
        _pushClient = new PushClient(masterSecret, appKey, proxy, conf);
        _reportClient = new ReportClient(masterSecret, appKey, proxy, conf);
        _deviceClient = new DeviceClient(masterSecret, appKey, proxy, conf);
        _scheduleClient = new ScheduleClient(masterSecret, appKey, proxy, conf);
    }


    
    public JPushClient1(String masterSecret, String appKey, int maxRetryTimes)
    {
        _pushClient = new PushClient(masterSecret, appKey, maxRetryTimes);
        _reportClient = new ReportClient(masterSecret, appKey, maxRetryTimes);
        _deviceClient = new DeviceClient(masterSecret, appKey, maxRetryTimes);
        _scheduleClient = new ScheduleClient(masterSecret, appKey, maxRetryTimes);
    }

    
    public JPushClient1(String masterSecret, String appKey, int maxRetryTimes, HttpProxy proxy)
    {
        _pushClient = new PushClient(masterSecret, appKey, maxRetryTimes, proxy);
        _reportClient = new ReportClient(masterSecret, appKey, maxRetryTimes, proxy);
        _deviceClient = new DeviceClient(masterSecret, appKey, maxRetryTimes, proxy);
        _scheduleClient = new ScheduleClient(masterSecret, appKey, maxRetryTimes, proxy);
    }

    
    public JPushClient1(String masterSecret, String appKey, int maxRetryTimes, HttpProxy proxy, ClientConfig conf)
    {
        conf.setMaxRetryTimes(maxRetryTimes);
        _pushClient = new PushClient(masterSecret, appKey, proxy, conf);
        _reportClient = new ReportClient(masterSecret, appKey, proxy, conf);
        _deviceClient = new DeviceClient(masterSecret, appKey, proxy, conf);
        _scheduleClient = new ScheduleClient(masterSecret, appKey, proxy, conf);
    }

    
    public JPushClient1(String masterSecret, String appKey, int maxRetryTimes, HttpProxy proxy, ClientConfig conf, boolean apnsProduction, long timeToLive)
    {
        conf.setMaxRetryTimes(maxRetryTimes);
        conf.setApnsProduction(apnsProduction);
        conf.setTimeToLive(timeToLive);
        _pushClient = new PushClient(masterSecret, appKey, proxy, conf);
        _reportClient = new ReportClient(masterSecret, appKey, proxy, conf);
        _deviceClient = new DeviceClient(masterSecret, appKey, proxy, conf);
        _scheduleClient = new ScheduleClient(masterSecret, appKey, proxy, conf);
    }

    
    public JPushClient1(String masterSecret, String appKey, boolean apnsProduction, long timeToLive)
    {
        ClientConfig conf = ClientConfig.getInstance();
        conf.setApnsProduction(apnsProduction);
        conf.setTimeToLive(timeToLive);
        _pushClient = new PushClient(masterSecret, appKey);
        _reportClient = new ReportClient(masterSecret, appKey);
        _deviceClient = new DeviceClient(masterSecret, appKey);
        _scheduleClient = new ScheduleClient(masterSecret, appKey);
    }

    /**
     * 发送给指定ios用户
     * @param alert
     * @param extras
     * @param registId
     * @return
     * @throws APIConnectionException
     * @throws APIRequestException
     */
	public PushResult sendIos(String alert, Map<String,String> extras, String... registId) throws APIConnectionException, APIRequestException {
		PushPayload payload =  PushPayload.newBuilder()
	            .setPlatform(Platform.ios())
	            .setAudience(Audience.registrationId(registId))
	            .setNotification(Notification.newBuilder()
	                    .addPlatformNotification(IosNotification.newBuilder()
	                            .setAlert(alert)
	                            .setBadge(+1)
	                            .setSound("default")
	                            .addExtras(extras)
	                            .build())
	                    .build())
	             .setOptions(Options.newBuilder()
	                     .setApnsProduction(false)
	                     .build())
	             .build();
	    return _pushClient.sendPush(payload);
	}
}