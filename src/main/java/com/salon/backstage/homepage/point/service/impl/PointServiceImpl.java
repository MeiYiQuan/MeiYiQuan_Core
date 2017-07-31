package com.salon.backstage.homepage.point.service.impl;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salon.backstage.common.util.ExtraSpringHibernateTemplate;
import com.salon.backstage.common.util.MobileMessageCondition;
import com.salon.backstage.common.util.afterCreate.DateAddUtil;
import com.salon.backstage.common.util.afterCreate.DateToNumUtil;
import com.salon.backstage.common.util.afterCreate.NumEditUtil;
import com.salon.backstage.homepage.point.service.IPointService;
import com.salon.backstage.homepage.statistics.service.IStatisticsService;
import com.salon.backstage.pub.bsc.dao.po.Point;
import com.salon.backstage.pub.bsc.dao.po.PointDay;
import com.salon.backstage.pub.bsc.dao.po.PointMost;
import com.salon.backstage.pub.bsc.dao.po.User;
import com.salon.backstage.pub.bsc.dao.vo.MobileMessage;
import com.salon.backstage.pub.bsc.domain.Constant;
import com.salon.backstage.pub.bsc.domain.Constant.PointEachType;
import com.salon.backstage.pub.bsc.domain.Constant.PointType;
import com.salon.backstage.qcproject.util.Statics;

@Service
public class PointServiceImpl implements IPointService {

	@Autowired
	private ExtraSpringHibernateTemplate extraSpringHibernateTemplate;
	
	@Autowired
	private IStatisticsService is;
	
	private final SimpleDateFormat day = new SimpleDateFormat("yyyy-MM-dd");
	
	private final SimpleDateFormat nowDay = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	/**
	 * 登陆
	 */
	@Override
	public void login(User user) {
		// 判断是不是昨天登陆过
		int today = DateToNumUtil.DateToInteger(new Date());
		int yesday = DateToNumUtil.DateToInteger(DateAddUtil.descDay(new Date(), 1));
		int lastLogin = NumEditUtil.LongSub(user.getLatest_login_time());
		if(lastLogin < yesday){//最后一次登陆时间(日)小于昨天:昨天没有登陆(连续登陆天数重置为1)
			user.setContinue_day(1);
		}else if(lastLogin == yesday){//最后一次登陆时间(日)等于昨天:昨天登陆过(连续登陆天数+1)
			user.setContinue_day(user.getContinue_day()+1);
		}else{//最后一次登陆时间(日)大于昨天:说明今天已经登陆过(连续登陆天数不变)
		}
		int lastSign = NumEditUtil.LongSub(user.getLatest_sign_time());
		
		//赠送积分:判断是否为当天第一次登录(如果最后一次签到时间不是今天,赠送积分)
		if(lastSign != today){
			user.setLatest_sign_time(DateToNumUtil.DateToLong(new Date()));
			addPoint(user);
		}    
		user.setLatest_login_time(DateToNumUtil.DateToLong(new Date()));
		extraSpringHibernateTemplate.getHibernateTemplate().update(user);
	}
	
	/**
	 * 登录送积分
	 */
	private void addPoint(User user) {
		Point point = new Point();
		point.setGet_time(DateToNumUtil.DateToLong(new Date()));
		point.setWarn_time(DateToNumUtil.DateToLong(DateAddUtil.descMonth(DateAddUtil.addYear(new Date(), 1), 3)));
		point.setExpire_time(DateToNumUtil.DateToLong(DateAddUtil.addYear(new Date(), 1)));
		point.setPoint_quantum(getDayPoint(user.getContinue_day()%30)); //连续登陆30天后刷新 
		point.setPoint_way_id("1"); //point_way_id=1 登录签到
		point.setUser_id((String)user.getId());
		extraSpringHibernateTemplate.getHibernateTemplate().save(point);
	}
	
	/**
	 * tb_point_day(连续签到天数对应当日获得积分表)查询连续签到天数该赠送的积分数量
	 */
	private int getDayPoint(int day) {
		String sql = "select day_point dayPoint from tb_point_day where day = "+day;
		Map map = extraSpringHibernateTemplate.createSQLQueryFindFirstOne(sql);
		return (int) map.get("dayPoint");
	}
	
	/**
	 * 注册时获得的积分
	 */
	@Override
	public void pointRegister(String userId) {
		Point point = new Point();
		point.setUser_id(userId);
		point.setGet_time(DateToNumUtil.DateToLong(new Date()));
		point.setWarn_time(DateToNumUtil.DateToLong(DateAddUtil.descMonth(DateAddUtil.addYear(new Date(), 1), 3)));
		point.setExpire_time(DateToNumUtil.DateToLong(DateAddUtil.addYear(new Date(), 1)));
		point.setPoint_way_id("2"); //point_way_id=2 途径:注册
		point.setPoint_quantum(500); 
		extraSpringHibernateTemplate.getHibernateTemplate().save(point);
	}
	
	/**
	 * 收藏时获得的积分
	 */
	@Override
	public void pointCollect(String userId) {
		Point point = new Point();
		point.setUser_id(userId);
		point.setGet_time(DateToNumUtil.DateToLong(new Date()));
		point.setWarn_time(DateToNumUtil.DateToLong(DateAddUtil.descMonth(DateAddUtil.addYear(new Date(), 1), 3)));
		point.setExpire_time(DateToNumUtil.DateToLong(DateAddUtil.addYear(new Date(), 1)));
		point.setPoint_way_id("9"); //point_way_id=2 途径:收藏
		point.setPoint_quantum(10); 
		extraSpringHibernateTemplate.getHibernateTemplate().save(point);
		
	}
	
	/**
	 * 投票时获得的积分
	 */
	@Override
	public void pointVote(String userId) {
		 String nowDate = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()).toString().substring(0, 8);//格式化
		 long now = Long.parseLong(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()).toString().substring(0, 8));
		 Date date=new Date();//取时间
		 Calendar calendar = new GregorianCalendar();
		 calendar.setTime(date);
		 calendar.add(calendar.DATE,1);//把日期往后增加一天.整数往后推,负数往前移动
		 date=calendar.getTime();
		 long tomorrow = Long.parseLong(new SimpleDateFormat("yyyyMMddHHmmss").format(date).toString().substring(0, 8));//明天
		 String sql="select get_time,point_quantum from tb_point where tb_point.point_way_id=10 and tb_point.user_id=?";
		 String[] values={userId};
		 List<Map> points = extraSpringHibernateTemplate.createSQLQueryFindAll(sql,values);
		 Float pointQuantum=0.0f;
		 for (Map point : points){
			long getTime = Long.parseLong(point.get("get_time").toString().substring(0, 8));
			if(getTime>=now && getTime<=tomorrow){
				pointQuantum+=Float.valueOf(point.get("point_quantum").toString());
			}
		}
		 if(pointQuantum<300){
			 pointVoteAdd(userId);
		 }
	}
	
	/**
	 * 投票添加积分
	 * @param userId
	 */
	private void pointVoteAdd(String userId) {
		Point point = new Point();
		point.setUser_id(userId);
		point.setGet_time(DateToNumUtil.DateToLong(new Date()));
		point.setWarn_time(DateToNumUtil.DateToLong(DateAddUtil.descMonth(DateAddUtil.addYear(new Date(), 1), 3)));
		point.setExpire_time(DateToNumUtil.DateToLong(DateAddUtil.addYear(new Date(), 1)));
		point.setPoint_way_id("10"); //point_way_id=10 途径:投票
		point.setPoint_quantum(10); 
		extraSpringHibernateTemplate.getHibernateTemplate().save(point);
	}
	
	/**
	 * 评论时获得的积分
	 */
	@Override
	public void pointComment(String userId) {
		 String nowDate = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()).toString().substring(0, 8);//格式化
		 long now = Long.parseLong(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()).toString().substring(0, 8));
		 Date date=new Date();//取时间
		 Calendar calendar = new GregorianCalendar();
		 calendar.setTime(date);
		 calendar.add(calendar.DATE,1);//把日期往后增加一天.整数往后推,负数往前移动
		 date=calendar.getTime();
		 long tomorrow = Long.parseLong(new SimpleDateFormat("yyyyMMddHHmmss").format(date).toString().substring(0, 8));//明天
		 String sql="select get_time,point_quantum from tb_point where tb_point.point_way_id=7 and tb_point.user_id=?";
		 String[] values={userId};
		 List<Map> points = extraSpringHibernateTemplate.createSQLQueryFindAll(sql,values);
		 Float pointQuantum=0.0f;
		 for (Map point : points){
			long getTime = Long.parseLong(point.get("get_time").toString().substring(0, 8));
			if(getTime>=now && getTime<=tomorrow){
				pointQuantum+=Float.valueOf(point.get("point_quantum").toString());
			}
		}
		 if(pointQuantum<50){
			 pointCommentAdd(userId);
		 }
	}

	/**
	 * 添加评论时获得的积分
	 * @param userId
	 */
	private void pointCommentAdd(String userId) {
		Point point = new Point();
		point.setUser_id(userId);
		point.setGet_time(DateToNumUtil.DateToLong(new Date()));
		point.setWarn_time(DateToNumUtil.DateToLong(DateAddUtil.descMonth(DateAddUtil.addYear(new Date(), 1), 3)));
		point.setExpire_time(DateToNumUtil.DateToLong(DateAddUtil.addYear(new Date(), 1)));
		point.setPoint_way_id("7"); //point_way_id=7途径:评论课程
		point.setPoint_quantum(5); 
		extraSpringHibernateTemplate.getHibernateTemplate().save(point);
		
	}
	
	/**
	 * 修改积分时获得的积分
	 */
	@Override
	public void pointEditPersion(String userId) {
		Point point = new Point();
		point.setUser_id(userId);
		point.setGet_time(DateToNumUtil.DateToLong(new Date()));
		point.setWarn_time(DateToNumUtil.DateToLong(DateAddUtil.descMonth(DateAddUtil.addYear(new Date(), 1), 3)));
		point.setExpire_time(DateToNumUtil.DateToLong(DateAddUtil.addYear(new Date(), 1)));
		point.setPoint_way_id("4"); //point_way_id=4途径:完善资料
		point.setPoint_quantum(80); 
		extraSpringHibernateTemplate.getHibernateTemplate().save(point);
		
	}
	
	/**
	 * 修改个人信息时获得积分的次数
	 */
	@Override
	public long pointPersion(String userId,int point_way_id) {
		String sql="select id from tb_point where tb_point.point_way_id=? and tb_point.user_id=?";
		String[] values={String.valueOf(point_way_id),userId};
		return extraSpringHibernateTemplate.createSQLQueryCount(sql, values);
	}
	
	/**
	 * 替换头像时获得的积分
	 */
	@Override
	public void pointReplaceHead(String userId) {
		Point point = new Point();
		point.setUser_id(userId);
		point.setGet_time(DateToNumUtil.DateToLong(new Date()));
		point.setWarn_time(DateToNumUtil.DateToLong(DateAddUtil.descMonth(DateAddUtil.addYear(new Date(), 1), 3)));
		point.setExpire_time(DateToNumUtil.DateToLong(DateAddUtil.addYear(new Date(), 1)));
		point.setPoint_way_id("3"); //point_way_id=3途径:上传头像
		point.setPoint_quantum(20); 
		extraSpringHibernateTemplate.getHibernateTemplate().save(point);
		
	}
	
	/**
	 * 手机验证获得的积分
	 */
	@Override
	public void pointPhone(String userId) {
		Point point = new Point();
		point.setUser_id(userId);
		point.setGet_time(DateToNumUtil.DateToLong(new Date()));
		point.setWarn_time(DateToNumUtil.DateToLong(DateAddUtil.descMonth(DateAddUtil.addYear(new Date(), 1), 3)));
		point.setExpire_time(DateToNumUtil.DateToLong(DateAddUtil.addYear(new Date(), 1)));
		point.setPoint_way_id("5"); //point_way_id=5途径:手机验证
		point.setPoint_quantum(100); 
		extraSpringHibernateTemplate.getHibernateTemplate().save(point);
	}

	
	@Override
	public MobileMessage addPoint(User user, PointEachType type, double orderPrice) throws Exception {
		if(type==null)
			return MobileMessageCondition.addCondition(false, -1, "参数为空！",null);
		PointType pointType = type.getPointType();
		PointMost most = extraSpringHibernateTemplate.findFirstOneByPropEq(PointMost.class, new String[]{"way_type"}, new Object[]{type.getType()});
		if(most==null)
			return MobileMessageCondition.addCondition(false, -2, "无数据！",null);
		MobileMessage result = null;
		switch(pointType){
			case MANY_OF_DAY:
				result = manyOfDay(user, most);
				break;
			case NO_RESTRICT:
				result = noRestrict(user, most);
				break;
			case SIGN_IN_DAY:
				result = signInDay(user, most);
				break;
			case ONLY_ONE:
				result = onlyOne(user, most);
				break;
			case PAY_SUCCESS:
				result = paySuccess(user, most, orderPrice);
				break;
		}
		return result;
	}
	
	/**
	 * 支付成功
	 * @param user
	 * @param most
	 * @param orderPrice
	 * @return
	 * @throws Exception 
	 */
	private MobileMessage paySuccess(User user,PointMost most, double orderPrice) throws Exception{
		long now = System.currentTimeMillis();
		int point = (int) (most.getWay_single() * orderPrice);
		String id = savePoint(now, user.getId().toString(), most.getId().toString(),point);
		if(id==null)
			return MobileMessageCondition.addCondition(false, -4, "数据库错误！",null);
		return MobileMessageCondition.addCondition(true, 0, "恭喜您获得了" + point + "个积分！",point);
	}
	
	/**
	 * 仅获取一次
	 * @param user
	 * @param most
	 * @return
	 * @throws Exception 
	 */
	private MobileMessage onlyOne(User user,PointMost most) throws Exception{
		Point point = extraSpringHibernateTemplate.findFirstOneByPropEq(Point.class, new String[]{"user_id","point_way_id"}, new Object[]{user.getId().toString(),most.getId().toString()});
		if(point==null){
			long now = System.currentTimeMillis();
			String id = savePoint(now, user.getId().toString(),most.getId().toString(), most.getWay_most());
			if(id==null)
				return MobileMessageCondition.addCondition(false, -4, "数据库错误！",null);
			return MobileMessageCondition.addCondition(true, 0, "恭喜您获得了" + most.getWay_most() + "个积分！",most.getWay_most());
		}
		return MobileMessageCondition.addCondition(false, -1, "您已经获取过该项积分！",null);
	}
	
	/**
	 * 获得积分到期时间
	 * @param now
	 * @return
	 */
	private long getExpireTime(Long now){
		long result;
		if(now==null){
			result = DateToNumUtil.DateToLong(DateAddUtil.addYear(new Date(), 1));
		}else{
			result = DateToNumUtil.DateToLong(DateAddUtil.addYear(new Date(now), 1));
		}
		return result;
	}
	
	/**
	 * 获取提醒积分到期的时间
	 * @param now
	 * @return
	 */
	private long getWarnTime(Long now){
		long result;
		if(now==null){
			result = DateToNumUtil.DateToLong(DateAddUtil.descMonth(DateAddUtil.addYear(new Date(), 1), 3));
		}else{
			result = DateToNumUtil.DateToLong(DateAddUtil.descMonth(DateAddUtil.addYear(new Date(now), 1), 3));
		}
		return result;
	}
	
	/**
	 * 获取当天 00:00:00 的long值
	 * @param now
	 * @return
	 * @throws ParseException 
	 */
	private long getNowDay(long now){
		String day = this.day.format(now);
		long result = 0;
		try {
			result = this.nowDay.parse(day + " 00:00:00").getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 获得某天 00:00:00 的long值，ex=1表示明天00:00:00，ex=-1表示昨天00：00：00
	 * @param now
	 * @return
	 */
	private long getDay(long now,int ex){
		String day = this.day.format(now);
		long result = 0;
		try {
			long nowDay = this.nowDay.parse(day + " 00:00:00").getTime();
			result = DateAddUtil.addDay(new Date(nowDay), ex).getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 保存一个Point
	 * @param now
	 * @param userId
	 * @param mostId
	 * @param point
	 * @return
	 * @throws Exception 
	 */
	private String savePoint(long now,String userId,String mostId,int point) throws Exception{
		if(now<100000)
			throw new Exception("出现了非常小的时间搓！");
		Point po = new Point();
		po.setExpire_time(getExpireTime(now));
		po.setGet_time(now);
		po.setPoint_used(0);
		po.setPoint_way_id(mostId);
		po.setStatus(Constant.YES_INT);
		po.setUse_time((long) 0);
		po.setUser_id(userId);
		po.setWarn_time(getWarnTime(now));
		po.setPoint_quantum(point);
		po.setPoint_unused(point);
		Serializable serId = extraSpringHibernateTemplate.getHibernateTemplate().save(po);
		if(serId==null)
			return null;
		return serId.toString();
	}
	
	/**
	 * 每日没有次数限制，但是有每日的积分上限
	 * @param user
	 * @param most
	 * @return
	 * @throws Exception 
	 * @throws ParseException 
	 */
	@SuppressWarnings("rawtypes")
	private MobileMessage manyOfDay(User user,PointMost most) throws Exception{
		long now = System.currentTimeMillis();
		long nowDay = getNowDay(now);
		String sql = "select ifnull(sum(`point`.`point_quantum`),0) as `sum` "
						+ "from `tb_point` as `point` "
						+ "where `point`.`user_id` = ? and `point`.`point_way_id` = ? "
						+ "		and `point`.`get_time` >= " + nowDay + " and `point`.`get_time` <= " + now + " and `point`.`expire_time` > " + now ;
		Map p = extraSpringHibernateTemplate.createSQLQueryFindFirstOne(sql, new Object[]{user.getId(),most.getId()});
		int sum = p==null?0:Integer.parseInt(p.get("sum").toString());
		if(sum < most.getWay_most()){
			int nowSum = sum + most.getWay_single();
			int canAdd = 0;
			String message = "";
			if(nowSum < most.getWay_most()){
				canAdd = most.getWay_single();
			}else{
				canAdd = most.getWay_most() - sum;
				message = "当天该项积分已达上限！";
			}
			String id = savePoint(now, user.getId().toString(), most.getId().toString(), canAdd);
			if(id==null)
				return MobileMessageCondition.addCondition(false, -4, "数据库错误！",null);
			return MobileMessageCondition.addCondition(true, 0, "恭喜您获得了" + canAdd + "个积分！" + message,canAdd);
		}
		return MobileMessageCondition.addCondition(false, -5, "当天获取该项积分已达上限！",null);
	}
	
	/**
	 * 无上限，每天无数次，没有任何限制
	 * @param user
	 * @param most
	 * @param point
	 * @return
	 * @throws Exception 
	 */
	private MobileMessage noRestrict(User user,PointMost most) throws Exception{
		long now = System.currentTimeMillis();
		String id = savePoint(now, user.getId().toString(), most.getId().toString(), most.getWay_single());
		if(id==null)
			return MobileMessageCondition.addCondition(false, -4, "数据库错误！",null);
		return MobileMessageCondition.addCondition(true, 0, "恭喜您获得了" + most.getWay_single() + "个积分！",most.getWay_single());
	}
	
	/**
	 * 签到获得积分(注册时,最后签到时间应该为0)
	 * @param user
	 * @return
	 * @throws Exception 
	 */
	private MobileMessage signInDay(User user,PointMost most) throws Exception{
		long now = System.currentTimeMillis();
		long lastSign = user.getLatest_sign_time();
		long nowDay = getNowDay(now);
		long yesterDay = getDay(nowDay, -1);
		MobileMessage result = null;
		if(lastSign>=nowDay){
			// 今日已经签到
			return MobileMessageCondition.addCondition(false, 100, "今日您已签到！",null);
		}else if(lastSign>=yesterDay){
			// 昨天已经签到，要注意今天是几号，如果今天是1号，则要无条件回归1。如果不是1号，则可以在原有的天数上加1，加完1之后判断是否大于30
			String today = day.format(new Date(now));
			int nowD = Integer.parseInt(today.substring(today.length()-2));
			if(nowD==1){
				// 今天是1号
				result = signInDaySupport(now, user, most, 1,1);
			}else{
				// 今天不是1号
				int signed = user.getContinue_day();
				int dayCount = signed>=30?1:(signed + 1);
				result = signInDaySupport(now, user, most, dayCount,signed + 1);
			}
		}else{
			// 已经断签
			result = signInDaySupport(now, user, most, 1,1);
		}
		return result;
	}
	
	/**
	 * 用于处理签到获得积分的逻辑，continueDay是已经包括了今天的值，continueDay用于去获取该连续天数下可以获得的积分数量，userUpDay用于去更新该user连续签到的天数。
	 * 原则上这两个是一样的，但是在31天的时候会有不同，因为数据库中没有31天对应的积分数量，此时user里还必须要显示成签到31天才对
	 * @param now
	 * @param user
	 * @param most
	 * @param continueDay
	 * @return
	 * @throws Exception 
	 */
	private MobileMessage signInDaySupport(long now,User user,PointMost most,int continueDay,int userUpDay) throws Exception{
		PointDay pointDay = extraSpringHibernateTemplate.findFirstOneByPropEq(PointDay.class, new String[]{"day"}, new Object[]{continueDay});
		int point = pointDay.getDay_point();
		String id = savePoint(now, user.getId().toString(), most.getId().toString(), point);
		if(id==null)
			return MobileMessageCondition.addCondition(false, -4, "数据库错误！",null);
		user.setLatest_sign_time(now);
		user.setContinue_day(userUpDay);
		extraSpringHibernateTemplate.getHibernateTemplate().update(user);
		
		return MobileMessageCondition.addCondition(true, 0, "恭喜您签到获得了" + point + "个积分！",point);
	}
	
	

}
