package com.salon.backstage.homepage.push.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.salon.backstage.common.util.ExtraSpringHibernateTemplate;
import com.salon.backstage.common.util.PushUtil;
import com.salon.backstage.homepage.push.service.PushService;
import com.salon.backstage.pub.bsc.dao.po.Push;
import com.salon.backstage.pub.bsc.dao.po.Sys;
import com.salon.backstage.pub.bsc.dao.po.SystemInfo;
import com.salon.backstage.pub.bsc.dao.po.User;
import com.salon.backstage.pub.bsc.domain.Constant;
import com.salon.backstage.qcproject.dao.ObjectDao;

/**
 * 作者：齐潮
 * 创建日期：2016年12月21日
 * 类说明：
 */
@Service
public class PushServiceImpl implements PushService {
	
	private final static Logger logger = LoggerFactory.getLogger(PushServiceImpl.class);

	@Autowired
	private ExtraSpringHibernateTemplate template;
	
	@Autowired
	private ObjectDao od;
	
	/**
	 * 通过id和类型获取push信息
	 * @param pushId
	 * @param type
	 * @return
	 */
	private Push getPush(String pushId,int type){
		Push push = template.findFirstOneByPropEq(Push.class, new String[]{"id","type","status"}, new Object[]{pushId,type,Constant.YES_INT});
		return push;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void pushEveryOne(String pushId,int type, String... userIds) {
		Push push = getPush(pushId, type);
		if(push==null)
			return;
		Map<String,String> params = (push.getMapjson()==null||push.getMapjson().trim().equals(""))?null:(JSONObject.parseObject(push.getMapjson(), Map.class));
		pushEveryOne(pushId, push.getTitle(), type, push.getContent(), params, userIds);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void pushAllIos(String pushId,int type) {
		Push push = getPush(pushId, type);
		if(push==null)
			return;
		Map<String,Object> conditions = new HashMap<String,Object>();
		conditions.put("equi_type", Constant.USER_PHONETYPE_IOS);
		conditions.put("user_state", Constant.YES_INT);
		List<Map<String, Object>> list = od.getPosForMap(User.class, conditions, null, null);
		Map<String,String> params = (push.getMapjson()==null||push.getMapjson().trim().equals(""))?null:(JSONObject.parseObject(push.getMapjson(), Map.class));
		if(list!=null&&list.size()>0){
			List<String> userIds = new ArrayList<String>();
			List<String> registIds = new ArrayList<String>();
			for(Map<String, Object> user:list){
				if(user.get("push_token")!=null&&!user.get("push_token").equals("")){
					userIds.add(user.get("id").toString());
					registIds.add(user.get("push_token").toString());
				}
			}
			String[] regists = registIds.toArray(new String[0]);
			PushUtil.sendToIOS(push.getContent(), params, regists);
			int n = saveSystemInfo(userIds, type, push);
			if(n!=userIds.size())
				logger.warn("执行推送给所有ios用户的操作保存系统信息时保存的信息条数与用户id个数不一致！用户id个数：" + userIds.size() + ",保存成功信息条数：" + n);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void pushAllAndroid(String pushId,int type) {
		Push push = getPush(pushId, type);
		if(push==null)
			return;
		Map<String,String> params = (push.getMapjson()==null||push.getMapjson().trim().equals(""))?null:(JSONObject.parseObject(push.getMapjson(), Map.class));
		
//		jpush.pushToAllAndroid(push.getContent(), params);
		
		Map<String,Object> conditions = new HashMap<String,Object>();
		conditions.put("equi_type", Constant.USER_PHONETYPE_ANDROID);
		conditions.put("user_state", Constant.YES_INT);
		List<Map<String, Object>> list = od.getPosForMap(User.class, conditions, null, null);
		if(list!=null&&list.size()>0){
			List<String> userIds = new ArrayList<String>();
			List<String> registIds = new ArrayList<String>();
			for(Map<String, Object> user:list){
				if(user.get("push_token")!=null&&!user.get("push_token").equals("")){
					userIds.add(user.get("id").toString());
					registIds.add(user.get("push_token").toString());
				}
			}
			String[] regists = registIds.toArray(new String[0]);
			PushUtil.sendToAndroid(push.getContent(), params, regists);
			int n = saveSystemInfo(userIds, type, push);
			if(n!=userIds.size())
				logger.warn("执行推送给所有android用户的操作保存系统信息时保存的信息条数与用户id个数不一致！用户id个数：" + userIds.size() + ",保存成功信息条数：" + n);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void pushAll(String pushId,int type) {
		Push push = getPush(pushId, type);
		if(push==null)
			return;
		Map<String,String> params = (push.getMapjson()==null||push.getMapjson().trim().equals(""))?null:(JSONObject.parseObject(push.getMapjson(), Map.class));
		
//		jpush.pushToAllAndroid(push.getContent(), params);
//		jpush.pushToAllIOS(push.getContent(), params);
		
		Map<String,Object> conditions = new HashMap<String,Object>();
		conditions.put("user_state", Constant.YES_INT);
		List<Map<String, Object>> list = od.getPosForMap(User.class, conditions, null, null);
		if(list!=null&&list.size()>0){
			List<String> userIds = new ArrayList<String>();
			List<String> iosRegistIds = new ArrayList<String>();
			List<String> androidRegistIds = new ArrayList<String>();
			for(Map<String, Object> user:list){
				if(user.get("push_token")!=null&&!user.get("push_token").equals("")){
					userIds.add(user.get("id").toString());
					if(Integer.parseInt(user.get("equi_type").toString())==Constant.USER_PHONETYPE_IOS){
						iosRegistIds.add(user.get("push_token").toString());
					}else{
						androidRegistIds.add(user.get("push_token").toString());
					}
				}
			}
			String[] iosregists = iosRegistIds.toArray(new String[0]);
			String[] androidregists = androidRegistIds.toArray(new String[0]);
			PushUtil.sendToAndroid(push.getContent(), params, androidregists);
			PushUtil.sendToIOS(push.getContent(), params, iosregists);
			int n = saveSystemInfo(userIds, type, push);
			if(n!=userIds.size())
				logger.warn("执行推送给所有用户的操作保存系统信息时保存的信息条数与用户id个数不一致！用户id个数：" + userIds.size() + ",保存成功信息条数：" + n);
		}
		
	}

	/**
	 * 用于推送成功后生成系统消息，使用时要保证push不为空
	 * @param userIds
	 * @param type
	 * @param push
	 * @return
	 */
	private int saveSystemInfo(List<String> userIds,int type,Push push){
		return saveSystemInfo(push.getId().toString(),push.getTitle(),push.getContent(), push.getMapjson(), type, userIds);
	}
	
	/**
	 * 用于推送成功后生成系统消息
	 * @param pushId
	 * @param title
	 * @param message
	 * @param mapjson
	 * @param type
	 * @param userIds
	 * @return
	 */
	private int saveSystemInfo(String pushId,String title,String message,String mapjson,int type,List<String> userIds){
		if(userIds!=null&&userIds.size()>0){
			long now = System.currentTimeMillis();
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("sys_key", Constant.SYSTEM_KEY_PUSH_SYSPHOTO);
			params.put("type", Constant.SYSTEM_TYPE_PHOTO);
			params.put("status", Constant.YES_INT);
			Sys sys = od.getObjByParams(Sys.class, params);
			List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
			for(String userId:userIds){
				Map<String,Object> userPush = new HashMap<String,Object>();
				userPush.put("user_id", userId);
				userPush.put("system_pic_url", sys.getSys_value());
				userPush.put("title", title);
				userPush.put("get_info_time", now);
				userPush.put("content", message);
				userPush.put("status", Constant.NO_INT);
				userPush.put("mapjson", mapjson);
				userPush.put("type", type);
				userPush.put("pushId", pushId);
				list.add(userPush);
			}
			int result = od.saveObjects(SystemInfo.class, list);
			return result;
		}
		return 0;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void pushEveryOne(String pushId,String title,int type,String message, Map<String, String> params, String... userIds) {
		if(userIds==null||userIds.length<1)
			return;
		StringBuffer inStr = new StringBuffer();
		List<String> list = new ArrayList<String>();
		for(int i=0;i<userIds.length;i++){
			if(userIds[i]!=null&&!userIds[i].trim().equals("")){
				inStr.append("?,");
				list.add(userIds[i]);
			}
		}
		if(list.size()<1)
			return;
		Object[] values = list.toArray();
		String sql = "select `id`,`push_token`,`equi_type` from `tb_user` where `id` in (" + inStr.substring(0, inStr.length()-1) + ") and `user_state` = " + Constant.YES_INT;
		List<Map<String,Object>> re = template.createSQLQueryFindAll(sql, values);
		if(re==null||re.size()<1)
			return;
		List<String> registIdsForAndroid = new ArrayList<String>();
		List<String> registIdsForIos = new ArrayList<String>();
		List<String> userHavedIds = new ArrayList<String>();
		for(int i=0;i<re.size();i++){
			Map<String,Object> map = re.get(i);
			if(map!=null&&map.size()>0){
				userHavedIds.add(map.get("id").toString());
				Object tokenObj = map.get("push_token");
				Object typeObj = map.get("equi_type");
				if(tokenObj!=null&&!tokenObj.equals("")&&typeObj!=null&&!typeObj.equals("")){
					int aiType = Integer.parseInt(typeObj.toString());
					if(aiType==Constant.USER_PHONETYPE_ANDROID){
						registIdsForAndroid.add(tokenObj.toString());
					}else if(aiType==Constant.USER_PHONETYPE_IOS){
						registIdsForIos.add(tokenObj.toString());
					}
				}
			}
		}
		if(registIdsForAndroid.size()>0){
			String[] android = registIdsForAndroid.toArray(new String[0]);
			PushUtil.sendToAndroid(message, params, android);
		}
		if(registIdsForIos.size()>0){
			String[] ios = registIdsForIos.toArray(new String[0]);
			PushUtil.sendToIOS(message, params, ios);
		}
		String mapjson = (params==null||params.size()<1)?null:(JSONObject.toJSONString(params));
		int n = saveSystemInfo(pushId, title, message, mapjson, type, userHavedIds);
		if(n!=userHavedIds.size())
			logger.warn("执行推送给任何用户的操作保存系统信息时保存的信息条数与用户id个数不一致！用户id个数：" + userHavedIds.size() + ",保存成功信息条数：" + n);
	}
	
}
