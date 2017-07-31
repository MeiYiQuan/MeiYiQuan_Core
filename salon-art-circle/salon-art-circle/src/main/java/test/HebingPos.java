package test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 作者：齐潮
 * 创建日期：2017年3月2日
 * 类说明：用于校验实体类
 */
public class HebingPos {

	
	/**
	 * 校验实体类的属性名以及个数是否一致
	 * @param args
	 * @throws ClassNotFoundException 
	 */
	public static void main(String[] args) throws ClassNotFoundException {
		
		boolean f = true;
		
		for(String name:names){
			String app = appUrl + name;
			String cms = cmsUrl + name;
			Class appClass = Class.forName(app);
			Class cmsClass = Class.forName(cms);
			Field[] appFileds = appClass.getDeclaredFields();
			Field[] cmsFileds = cmsClass.getDeclaredFields();
//			if(appFileds.length!=cmsFileds.length){
//				System.out.println(name + "类中的属性个数不一致！");
//				System.out.println("接口：" + appFileds.length);
//				System.out.println("cms：" + cmsFileds.length);
//				f = false;
//				break;
//			}
			Set<String> appSet = getSet(appFileds);
			Set<String> cmsSet = getSet(cmsFileds);
			if(appSet.size()!=cmsSet.size()){
				System.out.println(name + "类中的属性去重后个数不一致！");
				System.out.println("接口：" + appSet.size());
				System.out.println("cms：" + cmsSet.size());
//				f = false;
//				break;
			}
			boolean b = false;
			for(String str:cmsSet){
				if(!appSet.contains(str)){
					System.out.println("管理系统中" + name + "类中的" + str + "属性没有在接口项目中找到！");
					b = true;
					break;
				}
			}
			for(String str:appSet){
				if(!cmsSet.contains(str)){
					System.out.println("接口系统中" + name + "类中的" + str + "属性没有在cms项目中找到！");
					b = true;
					break;
				}
			}
			
			if(b){
				f = false;
				break;
			}
		}
		
		if(f){
			System.out.println("两套实体类的属性完全一致！");
		}else{
			System.err.println("两套实体类的属性有冲突！");
		}
	}
	
	private static Set<String> getSet(Field[] fileds){
		Set<String> set = new HashSet<String>();
		for(Field f:fileds){
			set.add(f.getName());
		}
		return set;
	}
	
	
	static String preFix = "";
	static String appUrl = "com.salon.backstage.pub.bsc.dao.po.";
	static String cmsUrl = "test.cmspos.";
	static String[] names = {
				"Activity" + preFix,
				"ActivityStatus" + preFix,
				"ActivityUser" + preFix,
				"Admin" + preFix,
				"Banner" + preFix,
				"Channel" + preFix,
				"ChannelCourse" + preFix,
				"CmsUrls" + preFix,
				"Collect" + preFix,
				"Comment" + preFix,
				"Coupon" + preFix,
				"CouponUser" + preFix,
				"CouponWillSendUser" + preFix,
				"Course" + preFix,
				"CourseRecommend" + preFix,
				"District" + preFix,
				"Follow" + preFix,
				"GuidePic" + preFix,
				"HomePage" + preFix,
				"IosCoin" + preFix,
				"Job" + preFix,
				"Like" + preFix,
				"Menu" + preFix,
				"MenuToUrls" + preFix,
				"Order" + preFix,
				"Playrecord" + preFix,
				"Point" + preFix,
				"PointDay" + preFix,
				"PointMost" + preFix,
				"PubicParameter" + preFix,
				"Push" + preFix,
				"Role" + preFix,
				"RoleToMenu" + preFix,
				"Shared" + preFix,
				"Statistics" + preFix,
				"Subject" + preFix,
				"Suggestion" + preFix,
				"SuggestionType" + preFix,
				"Sys" + preFix,
				"SystemInfo" + preFix,
				"Teacher" + preFix,
				"TeacherOrder" + preFix,
				"TeacherSend" + preFix,
				"User" + preFix,
				"UserSurvey" + preFix,
				"UserToBuyVideos" + preFix,
				"UserToIosCoins" + preFix,
				"UserToRequest" + preFix,
				"UserVideoRequest" + preFix,
				"Video" + preFix,
				"VideoStandard" + preFix
				};
	
	
}
