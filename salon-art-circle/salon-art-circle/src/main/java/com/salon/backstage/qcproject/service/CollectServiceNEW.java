package com.salon.backstage.qcproject.service;

import com.salon.backstage.qcproject.util.Code;

/**
 * 作者：齐潮
 * 创建日期：2016年12月26日
 * 类说明：处理有关收藏的业务逻辑
 */
public interface CollectServiceNEW {

	/**
	 * 通过传入的userId和type去分页获取相应的收藏列表
	 * @param userId
	 * @param page
	 * @param size
	 * @param type
	 * @return
	 */
	public Code getCollectsByUserId(String userId,int page,int size,int type);
	
	
	/**
	 * 收藏与取消收藏，在方法里直接判断是那种收藏并且执行
	 * @param userId
	 * 		用户id
	 * @param type
	 * 		收藏目标类型，见静态量
	 * @param typeId
	 * 		收藏目标的具体id
	 * @return
	 * @throws Exception 
	 */
	public Code doing(String userId,int type,String typeId) throws Exception;
	
	/**
	 * 批量删除收藏信息
	 * @param userId
	 * 		用户id
	 * @param type
	 * 		收藏目标类型，见静态量
	 * @param collectIds
	 * 		收藏目标的具体id
	 * @return
	 */
	public Code delete(String userId,int type,String[] collectIds);
	
}
