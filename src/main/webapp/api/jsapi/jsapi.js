/**
 * API接口定义 服务器，接口方法，接口参数及说明，测试数据，
 */
var bxsAPI = {};
bxsAPI.local = location.toString().indexOf("http://192.168.1.112:8081/salon-art-circle") > -1 ? 0 : 1;
bxsAPI.apis = [];
bxsAPI.server = bxsAPI.local?"http://192.168.1.112:8081/salon-art-circle":"http://192.168.1.112:8081/salon-art-circle";
bxsAPI.uid = bxsAPI.local?"22497":"22497";
bxsAPI.un = "13552997366";
bxsAPI.userToken = "6d7a2e40-4949-4c89-b00b-58a38626ae91";
bxsAPI.ziruo = "false";
bxsAPI.readme = "此apps中应用了密码加密，在apps端用户输入的密码发送至服务器时全部用<redlight>MD5</redlight>加密，<br/>"
		+ "服务器端返回的密码是加密的，所以服务器给的密码不用处理，可直接使用<br/><redlight>MD5取值范围：0123456789ABCDEF</redlight>"
		+ "<br/>EG:<redlight> 111111</redlight>>>><redlight>96E79218965EB72C92A549DD5A330112</redlight><br/>EG:<redlight> 123456</redlight>>>><redlight>E10ADC3949BA59ABBE56E057F20F883E</redlight>";

// API最新版本号，更新需要调整版本号，并更新下面的记录
bxsAPI.version = "v1.0";
bxsAPI.updated = [ {
	ver : "v1.0",
	dt : "2016-09-07",
	con : [ "全部接口", ]
} ];

/**
 * 接口模块定义 主要用于分类接口，看起来更清楚
 */

bxsAPI.apicates = [// api模块
{
	cid :0,
	name:"公共接口"
	},
{	
	cid :1,
	name:"用户操作接口"
	},
];

bxsAPI.apis.push({
    "desc": "发送验证码",
    "name": "发送验证码",
    "test": '{"phone":"111"}',
    "method": "/user/getIdenCode.do",
    "anchor": "/user/getIdenCode.do",
    "returns": {
        "codeExplain": "状态值0正常8缺少参数",
        "response": '返回的数据',
        "result": false, 
        "code": 12, 
        "errorMessage": "服务器没有合适的方法发送验证码", 
        "response": ""
    },
    "params": {
        "phone": "<require>[必选]</require>电话号",
        "doType": "<require>[必选]</require>验证码类型1注册2忘记密码",
    },
    "cid": 1,
    
});
