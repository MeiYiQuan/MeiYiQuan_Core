package com.salon.backstage.qcproject.service;

import com.salon.backstage.qcproject.util.Code;
import com.salon.backstage.qcproject.util.Enums.ShareDistrict;
import com.salon.backstage.qcproject.util.Enums.ShareType;

/**
 * 作者：齐潮
 * 创建日期：2017年2月14日
 * 类说明：处理有关分享的业务逻辑
 */
public interface ShareServiceNEW {

	/**
	 * 分享成功后的回调
	 * @param userId
	 * @param sharedId
	 * @param type
	 * @param district
	 * @return
	 */
	public Code sharedHook(String userId,String sharedId,ShareType type,ShareDistrict district);
	
}
