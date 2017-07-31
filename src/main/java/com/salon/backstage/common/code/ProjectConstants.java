package com.salon.backstage.common.code;

import java.util.ArrayList;
import java.util.List;

public class ProjectConstants {
    /**
     * 验证码用途：注册使用(作为Redis中查找的key)
     */
    public static final String SMS_AUTH_CODE_REGIST = "sms_auth_code_regist";

    /**
     * 验证码用途：找回密码(作为Redis中查找的key)
     */
    public static final String SMS_AUTH_CODE_FORGET_PASSWD = "sms_auth_code_forget_passwd";

    /**
     * 短信类型：注册使用
     */
    public static final int SMS_TYPE_REGIST = 10;

    /**
     * 短信类型：找回密码
     */
    public static final int SMS_TYPE_FORGET_PASSWD = 20;

    /**
     * 短信相关:上次发送短信时间
     */
    public static final String SMS_LAST_SEND_TIME = "sms_last_send_time";

    /**
     * 根据数字类型的短信用途，获取字符串类型的验证码类型
     *
     * @param code
     * @return
     */
    public static String getSMSAuthCodeType(int code) {
        String smsType = "";
        switch (code) {
            case SMS_TYPE_REGIST:
                smsType = ProjectConstants.SMS_AUTH_CODE_REGIST;
                break;
            case SMS_TYPE_FORGET_PASSWD:
                smsType = ProjectConstants.SMS_AUTH_CODE_FORGET_PASSWD;
                break;
            default:
                break;
        }
        return smsType;
    }

    /**
     * 用于对加密信息进行约定(位数是8的倍数)
     */
    public final static String TRANSFER_KEY = "jlkqeriu13623456";

    /**
     * json信息前缀(约定，没有位数要求,暂时作废)
     */
    public final static String BEFORE = "content";

    /**
     * 加密解密时的字符编码
     */
    public final static String ENCODE = "ISO-8859-1";

    /**
     * 返回体code
     */
    public final static String RESPONSE_JSON_CODE = "code";

    /**
     * 返回体message
     */
    public final static String RESPONSE_JSON_MESSAGE = "message";

    /**
     * 返回体内容
     */
    public final static String RESPONSE_JSON_DATA = "data";

    /**
     * 是
     */
    public final static int YES_INT = 1;

    /**
     * 否
     */
    public final static int NO_INT = 2;

    /**
     * 性别 男
     */
    public final static int SEX_MAN = 0;

    /**
     * 性别 女
     */
    public final static int SEX_WOMAN = 1;

    /**
     * 性别 未知
     */
    public final static int SEX_UNKNOW = 2;

    /**
     * 用于表示ip的字段
     */
    public final static String IP = "ip";


    public final static String DEVICE_ID = "deviceId";
    /**
     *
     */
    public final static String TOKEN = "token";

    public final static String USER_ID = "authUserId";

    /**
     * 订单状态码，未拍摄
     */
    public final static int INDENT_STATE_NOPHOTO = 10;

    /**
     * 订单状态码，拍摄完成，未确认
     */
    public final static int INDENT_STATE_NOAFFIRM = 20;

    /**
     * 订单状态码，确认已拍摄，未评价
     */
    public final static int INDENT_STATE_NOAPPRAISE = 30;

    /**
     * 订单状态码，已经评价
     */
    public final static int INDENT_STATE_APPRAISE = 40;

    /**
     * 刚刚申请退款，但是平台还没有做出回应
     */
    public final static int INDENT_STATE_WAITINGREFUND = 50;

    /**
     * 同意退款，正在退款中
     */
//	public final static int INDENT_STATE_REFUNDING = 60;	--- 作废

    /**
     * 过期未拍摄(过了拍摄日期)
     */
    public final static int INDENT_STATE_NOREFUNDING = 60;

    /**
     * 已退款(退款成功)
     */
    public final static int INDENT_STATE_REFUNDED = 70;

    /**
     * 保存订单状态。因为以上8个状态还要归总为3个大状态：未拍摄，已拍摄，已退款
     */
    public static enum IndentType {
        /**
         * 未拍摄
         */
        NOSHOOT("10", new int[]{INDENT_STATE_NOPHOTO, INDENT_STATE_NOREFUNDING}),
        /**
         * 已拍摄
         */
        SHOOTED("20", new int[]{INDENT_STATE_NOAFFIRM, INDENT_STATE_NOAPPRAISE, INDENT_STATE_APPRAISE}),
        /**
         * 已退款
         */
        REFUNDED("30", new int[]{INDENT_STATE_WAITINGREFUND, INDENT_STATE_REFUNDED});


        private String type;

        private int[] ns;


        private IndentType(String type, int[] ns) {
            this.type = type;
            this.ns = ns;
        }

        /**
         * 必须得提供一个前台传过来一个关于type的标识，然后这里可以直接返回这个标识下的数组。如果没有，则返回null
         */
        public static int[] getTypes(String type) {
            if (type == null) {
                return null;
            } else if (type.equals(NOSHOOT.type)) {
                return NOSHOOT.ns;
            } else if (type.equals(SHOOTED.type)) {
                return SHOOTED.ns;
            } else if (type.equals(REFUNDED.type)) {
                return REFUNDED.ns;
            } else {
                return null;
            }
        }
        
        /**
         * 获得一个type字符串
         * @return
         */
        public String getType(){
        	return this.type;
        }

        /**
         * 提供一个前台传过来一个关于type的标识，然后这里可以直接返回这个标识下的List。如果没有，则返回null
         */
        public static List<Integer> getTypesList(String type) {
            int[] n = getTypes(type);
            if (n == null || n.length < 1) {
                return null;
            } else {
                List<Integer> list = new ArrayList<Integer>();
                for (int i = 0; i < n.length; i++) {
                    list.add(n[i]);
                }
                return list;
            }
        }
    }


    /**
     * 图片用途：轮播图
     */
    public final static int WED_PICTURE_USE_CAROUSEL = 1;

    /**
     * 图片用户：店铺实景
     */
    public final static int MERCHANT_PICTURE_USE_SHIJING = 1;

    /**
     * 时间戳有效期，毫秒为单位
     */
    public final static long TIMESTEMP = 1000 * 300;

    /**
     * 商家类型：影楼
     */
    public final static int MERCHANT_TYPE_YINGLOU = 1;

    /**
     * 商家类型：工作室
     */
    public final static int MERCHANT_TYPE_GONGZUOSHI = 2;

    /**
     * 商家类型：个人摄影师
     */
    public final static int MERCHANT_TYPE_GERENSHEYINGSHI = 3;

    /**
     * 商家类型：自营摄影师
     */
    public final static int MERCHANT_TYPE_ZIYINGSHEYINGSHI = 4;

    /**
     * 附加服务类型：优质服务
     */
    public final static int ATTRIBUTE_TYPE_YOUZHIFUWU = 1;

    /**
     * 附加服务类型：商家特色
     */
    public final static int ATTRIBUTE_TYPE_SHANGJIATESE = 2;

    /**
     * 附加服务类型：全球旅拍
     */
    public final static int ATTRIBUTE_TYPE_QUANQIULVPAI = 3;

    /**
     * 附加服务类型：赠送
     */
    public final static int ATTRIBUTE_TYPE_ZENGSONG = 4;
    
    /**
     * 附加服务类型：商家营业面积
     */
    public final static int ATTRIBUTE_TYPE_ACREAGE = 5;
    
    /**
     * 商家状态state：刚刚申请，正在审核中
     */
    public final static int MERCHANT_STATE_CHECKING = 10;
    
    /**
     * 商家状态state：审核通过
     */
    public final static int MERCHANT_STATE_APPROVED = 20;
    
    /**
     * 商家状态state：审核未通过
     */
    public final static int MERCHANT_STATE_NOAPPROVED = 30;
    
    /**
     * 商家状态state：没有创建商家(附加，根本不存在这个Merchant对象)
     */
    public final static int MERCHANT_NOMERCHANT = -1;
    
    /**
     * 对于int，double不可为负数的列值的默认值
     */
    public final static int NOHAVE_DEFAULT = -1;
}
