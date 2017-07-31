package test.push;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.support.JdbcUtils;
import cn.jpush.api.JPushClient;
import cn.jpush.api.common.ClientConfig;
import cn.jpush.api.common.resp.APIConnectionException;
import cn.jpush.api.common.resp.APIRequestException;
import cn.jpush.api.push.PushResult;

/**
 * 极光推送
 * @author Administrator
 *
 */
public class PushUtil {

	public static void main(String[] args) {
		sendToAndroid("你是在家里加班吗？", null, "140fe1da9ea415e421e");
		// sendToIOS("你是在家里加班吗？", null, "140fe1da9ea415e421e");
	}

	protected static final Logger LOG = LoggerFactory.getLogger(PushUtil.class);
	
	private static String APP_KEY;
	private static String MASTER_SECRET;

	static {
		InputStream inputStream = JdbcUtils.class.getClassLoader().getResourceAsStream("config.properties");
		Properties p = new Properties();
		try {
			p.load(inputStream);
			APP_KEY = p.getProperty("jpush.appKey");
			MASTER_SECRET = p.getProperty("jpush.masterSecret");
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * 推送给某些安卓用户
	 * 
	 * @param message
	 * @param pram
	 * @param registId
	 */
	@SuppressWarnings("deprecation")
	public static void sendToAndroid(String message, Map<String, String> pram, String... registId) {
		ClientConfig config = ClientConfig.getInstance();
		config.setPushHostName("https://api.jpush.cn");
		JPushClient jpushClient = new JPushClient(MASTER_SECRET, APP_KEY, 3, null, config);
		try {
			PushResult result = jpushClient.sendAndroidNotificationWithRegistrationID(null, message, pram, registId);
			LOG.info("Got result - " + result);
		} catch (APIConnectionException e) {
			LOG.error("Connection error. Should retry later. ", e);
		} catch (APIRequestException e) {
			LOG.error("Error response from JPush server. Should review and fix it. ", e);
			LOG.info("HTTP Status: " + e.getStatus());
			LOG.info("Error Code: " + e.getErrorCode());
			LOG.info("Error Message: " + e.getErrorMessage());
			LOG.info("Msg ID: " + e.getMsgId());
		}
	}

	/**
	 * 推送给某些ios用户
	 * 
	 * @param message
	 * @param pram
	 * @param registId
	 */
	public static void sendToIOS(String message, Map<String, String> pram, String... registId) {
		ClientConfig config = ClientConfig.getInstance();
		config.setPushHostName("https://api.jpush.cn");
		JPushClient1 jpushClient1 = new JPushClient1(MASTER_SECRET, APP_KEY, 3, null, config);
		try {
			PushResult result = jpushClient1.sendIos(message, pram, registId);
			LOG.info("Got result - " + result);
		} catch (APIConnectionException e) {
			LOG.error("Connection error. Should retry later. ", e);
		} catch (APIRequestException e) {
			LOG.error("Error response from JPush server. Should review and fix it. ", e);
			LOG.info("HTTP Status: " + e.getStatus());
			LOG.info("Error Code: " + e.getErrorCode());
			LOG.info("Error Message: " + e.getErrorMessage());
			LOG.info("Msg ID: " + e.getMsgId());
		}
	}

}
