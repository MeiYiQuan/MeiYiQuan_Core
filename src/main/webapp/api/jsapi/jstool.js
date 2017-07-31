bxsAPI.navPanelExec = function(){
	var navpanelbox = $("navPanel");
	if(!navpanelbox){
		navpanelbox = new Element("div",{"class":"navpanelbox","id":"navPanel"}).inject(document.body);
		var navh5 = new Element("h5").inject(navpanelbox);
		var navsha = new Element("span").inject(navh5).set("html","隐藏");
		navsha.onclick = function(){
			if(!bxsAPI.navShow){
				$("apicatebox").style.display = "none";
				this.set("html","显示");
				bxsAPI.navShow = 1;
			}else{
				$("apicatebox").style.display = "block";
				this.set("html"," 隐藏");
				bxsAPI.navShow = 0;
			}
			bxsAPI.reSizeNavPanel();
		}
		var apicatebox = new Element("div",{"id":"apicatebox","class":"apicatebox"}).inject(navpanelbox);
		for(var c=0;c<bxsAPI.apicates.length;c++){
			var cate = bxsAPI.apicates[c];
			var apicate = new Element("div",{"class":"apicate"}).inject(apicatebox).set("html",bxsAPI.listno(c+1)+cate.name);
			
			var apisData = bxsAPI.apis.filter(function(a){return a.cid===cate.cid});
			var navul = new Element("ul").inject(apicatebox);
			for(var i=0;i<apisData.length;i++){
				var mobj = apisData[i];
				var li = new Element("li").inject(navul).set("html",bxsAPI.listno(c+1,i+1)+mobj.name);
				li.method = mobj.method?mobj.method:mobj.name;
				li.anchor = mobj.anchor;
				li.onclick = function(){
					if(this.anchor){
						location = "#"+this.anchor;
					}else{
						location = "#"+this.method;
					}
				}
			}
		}
		bxsAPI.reSizeNavPanel(navpanelbox);
		navpanelbox.makeDraggable({
			container:document.body,
//			droppables:dropables,
			onEnter:function(el,dr){
			},
			onDrop:function(el,dr){
			}
		});
	}
}
bxsAPI.reSizeNavPanel = function(obj){
	if(!obj) obj = $("navPanel");
	if(!obj) return;
	if(!bxsAPI.navShow){
		obj.setStyle("height",($(window).getSize().y-110)+"px");
	}else{
		obj.setStyle("height","auto");
	}
}
bxsAPI.exec = function(){
	var apiboxwrapborder = $("apiboxwrapborder");
	for(var c=0;c<bxsAPI.apicates.length;c++){
		var cate = bxsAPI.apicates[c];
		var apicate = new Element("div",{"class":"apicate"}).inject(apiboxwrapborder).set("html",bxsAPI.listno(c+1)+cate.name);
		var apisData = bxsAPI.apis.filter(function(a){return a.cid===cate.cid});
		
		for(var i=0;i<apisData.length;i++){
			var mobj = apisData[i];
			mobj.idno = c+"_"+i;
			var apibox = new Element("div",{"class":"apibox"}).inject(apiboxwrapborder);
			if(mobj.anchor){
				var anchor  = new Element("a",{"id":mobj.anchor,"name":mobj.anchor}).inject(apibox)
			}else{
				var anchor  = new Element("a",{"id":mobj.method?mobj.method:mobj.name,"name":mobj.method?mobj.method:mobj.name}).inject(apibox)
			}
			
			var apih3 = new Element("div",{"class":"apih3"}).inject(apibox).set("html",bxsAPI.listno(c+1,i+1)+mobj.name);
			var apitbl = new Element("table",{"class":"apitbl","border":"0","width":"100%","cellpadding":"0","cellspacing":"0"}).inject(apibox);
			
			if(mobj.style && mobj.style==1){//处理只有key-value的情况
				for(key in mobj.datas){
					var trm = new Element("tr",{"class":"trm"}).inject(apitbl);
					var tdmn = new Element("td",{"class":"tdn"}).inject(trm).set("html",key);	
					var tdmc = new Element("td",{"class":"tdc"}).inject(trm).set("html",mobj.datas[key]);			
				}
			}else{
			
				var trm = new Element("tr",{"class":"trm"}).inject(apitbl);
				var tdmn = new Element("td",{"class":"tdn"}).inject(trm).set("html","方法");
				var tdmc = new Element("td",{"class":"tdc","colspan":2}).inject(trm).set("html",mobj.method);
				
				var trp = new Element("tr",{"class":"trm"}).inject(apitbl);
				var trpn = new Element("td",{"class":"tdn"}).inject(trp).set("html","参数");
				var pcount = 0;
				for(key in mobj.params){
					var keystr = mobj.params[key].toString().indexOf("del")>-1?"<del>"+key+"</del>":key;
					if(pcount==0){
						var tdppn = new Element("td",{"class":"tdnn"}).inject(trp).set("html",keystr);
						var tdppc = new Element("td",{"class":"tdcc"}).inject(trp).set("html",mobj.params[key]);
					}else{
						var trpp = new Element("tr",{"class":"trm"}).inject(apitbl);
						var tdppn = new Element("td",{"class":"tdnn"}).inject(trpp).set("html",keystr);
						var tdppc = new Element("td",{"class":"tdcc"}).inject(trpp).set("html",mobj.params[key]);
					}
					pcount++;
				}
				trpn.set("rowspan",pcount);
				
				if(mobj.desc){
					var trdesc = new Element("tr",{"class":"trm"}).inject(apitbl);
					var tddescn = new Element("td",{"class":"tdn"}).inject(trdesc).set("html","说明");
					var tddescc = new Element("td",{"class":"tdc","colspan":2}).inject(trdesc).set("html",mobj.desc);
				}
				
				//测试
				if(typeof mobj.istest=='undefined'){
					mobj.istest=true;//这个语句是说，如果不写istest的时候，默认是可以有测试按钮的
				}
				if(mobj.test||mobj.istest==true){
					var trtest = new Element("tr",{"class":"trm"}).inject(apitbl);
					var tdtestn = new Element("td",{"class":"tdn"}).inject(trtest).set("html","示例");
					var tdtestc = new Element("td",{"class":"tdc","colspan":2}).inject(trtest);
					var shtml="";
					if(mobj.test){
						shtml = "<textarea class=\"testcodearea\" id=\"testcode"+mobj.idno+"\">"+mobj.test+"</textarea>";
					}
					if(mobj.istest==true){
					
						shtml += "<input value=\"test\" type=\"button\" class=\"testbtn\" onclick=\"bxsAPI.testApi('"+mobj.idno+"','"+mobj.method+"',1)\"> ";
		//				shtml += "<input value=\"comment\" type=\"button\" class=\"testbtn\" onclick=\"bxsAPI.testApi('"+mobj.idno+"','"+mobj.method+"',3)\"> ";
						shtml += "<a href=\"javascript:void(0)\" onclick=\"bxsAPI.testApi('"+mobj.idno+"','"+mobj.method+"',2)\">"+"browse"+"</a>";
					}
					tdtestc.set("html",shtml);
				}
				if(mobj.testForm){
					new Element("div").inject(tdtestc).set("html",mobj.testForm);
				}
				if(mobj.testData){
					var trtest = new Element("tr",{"class":"trm"}).inject(apitbl);
					var tdtestn = new Element("td",{"class":"tdn"}).inject(trtest).set("html","示例");
					var tdtestc = new Element("td",{"class":"tdc","colspan":2}).inject(trtest);
					var testDiv = new Element("div").inject(tdtestc);
					var testHtml = "";
					for(var ii=0;ii<mobj.testData.length;ii++){
						var tdata = mobj.testData[ii];
						testHtml += "<div><label>"+tdata.ti+"</label><br/><textarea class=\"testcodearea\" id=\"testcode"+mobj.idno+""+ii+"\">"+tdata.test+"</textarea>";
						testHtml += "<input value=\"test\" type=\"button\" class=\"testbtn\" onclick=\"bxsAPI.testApi('"+mobj.idno+"','"+mobj.method+"',1,"+ii+")\"> ";
	//					testHtml += "<input value=\"comment\" type=\"button\" class=\"testbtn\" onclick=\"bxsAPI.testApi('"+mobj.idno+"','"+mobj.method+"',3,"+ii+")\"> ";
						testHtml += "<a href=\"javascript:void(0)\" onclick=\"bxsAPI.testApi('"+mobj.idno+"','"+mobj.method+"',2,"+ii+")\">"+"browse"+"</a>";
						testHtml += "</div>";
						//testHtml += "<div><label>"+tdata.ti+"</label>"+"<a target='_blank' href='"+bxsAPI.server+mobj.method+"&"+tdata.test+"'>"+tdata.test+"</a></div>";
					}
					testDiv.set("html",testHtml);
				}
				//返回说明
				if(mobj.returns){
					var trreturnp = new Element("tr",{"class":"trm"}).inject(apitbl);
					var trreturnpn = new Element("td",{"class":"tdn"}).inject(trreturnp).set("html","返回说明");
					var pcount = 0;
					for(key in mobj.returns){
						if(pcount==0){
							var trreturnppn = new Element("td",{"class":"tdnn"}).inject(trreturnp).set("html",key);
							var trreturnpc = new Element("td",{"class":"tdcc"}).inject(trreturnp).set("html",mobj.returns[key]);
						}else{
							var trreturnpp = new Element("tr",{"class":"trm"}).inject(apitbl);
							var trreturnppn = new Element("td",{"class":"tdnn"}).inject(trreturnpp).set("html",key);
							var trreturnppc = new Element("td",{"class":"tdcc"}).inject(trreturnpp).set("html",mobj.returns[key]);
						}
						pcount++;
					}
					trreturnpn.set("rowspan",pcount);
				}
				
				//返回box
				var rebox  = new Element("div",{"id":"rebox"+mobj.idno,"class":"rebox"}).inject(apibox);
				var reh5 = new Element("h5",{"id":"reh5"+mobj.idno}).inject(rebox).set("html","<span onclick=\"bxsAPI.hideApiBox('"+mobj.idno+"')\">隐藏</span>");
				var rearea = new Element("div",{"class":"Canvas","id":"rearea"+mobj.idno}).inject(rebox);
			}
		}
	}
	
	bxsAPI.layout.init();
}

//接口测试方法
bxsAPI.fjsOpts = {};
bxsAPI.testApi = function(idno,method,flag,subno){
	bxsAPI.starting = new Date().getTime();//请求开始时间
	if(idno && method){
		var para=$("testcode"+idno+(!isNaN(subno)?subno:""));
		var reqData=bxsAPI.server+method;
		if(para){
			reqData=reqData+"?"+(flag==3?"codeComment=1&":"")+encodeURI(para.value);
		}else{
			reqData=reqData+"?codingType=1";
		}
//		var reqData = bxsAPI.server+method+"?"+(flag==3?"codeComment=1&":"")+encodeURI($("testcode"+idno+(!isNaN(subno)?subno:"")).value);
//	    alert(decodeURIComponent(reqData));
		  
		
		if(flag==2){
			var parms= bxsAPI.server+"/api/http.jsp"+"?&url="+encodeURI(bxsAPI.server+method)+"&data="+encodeURI(para.value);
			window.open(parms);
		}else{
			if(bxsAPI.ziruo=='true'){
			reqData = reqData.replace("userToken","notUserToken");
			reqData = reqData +"&userToken="+bxsAPI.userToken;
			}
			var actionReq = new Request({
				method: 'post', 
				encoding:'utf-8',
				url: bxsAPI.server+method,
				data:para.value,
                contentType: "application/text; charset=utf-8", //content-type:application/json
				onSuccess:function(res){
					var rebox = $("rebox"+idno);
					var rearea = $("rearea"+idno);
					if(rebox){
						rebox.style.display = "block";
//						rearea.value = js_beautify(res, bxsAPI.fjsOpts);
						Process(res,idno);
					}
					
					bxsAPI.requestTiming(idno);
				},
				onFailure:function(req){
					alert("出错了，检查一下网络？"+req);
				}
				
			}).send();
			
			
		}
	}else{
		alert("参数为空，检查一下");
	}
}
bxsAPI.requestTiming = function(idno){
	bxsAPI.ending = new Date().getTime();//请求结束时间
	if(bxsAPI.starting && bxsAPI.ending){
		var reqtimingspan = $("reqtimingspan"+idno);
		if(reqtimingspan){
			reqtimingspan.innerHTML = (bxsAPI.ending-bxsAPI.starting)+"ms";
		}
	}
}

bxsAPI.hideApiBox = function(idno){
	var rebox = $("rebox"+idno);
	if(rebox) rebox.style.display = "none";
}

bxsAPI.execFieldDict = function(){
	var apiboxwrapborder = $("apiboxwrapborder");
		
	var apibox = new Element("div",{"class":"apibox"}).inject(apiboxwrapborder);
	var apih3 = new Element("div",{"class":"apih3"}).inject(apibox).set("html","数据字典");
	var apitbl = new Element("table",{"class":"apitbl","border":"0","width":"100%","cellpadding":"0","cellspacing":"0"}).inject(apibox);
	
//	//把apis中的params和returns都提出出来，再加上bxsAPI.fieldDict里面的一起输出
//	if(!bxsAPI.fieldDict) bxsAPI.fieldDict = [];
//	bxsAPI.apis.each(function(apiobj){
//		if(apiobj.params){
//			for(key in apiobj.params){
//				if(bxsAPI.fieldDict[key]){
//					if(bxsAPI.fieldDict[key]!=apiobj.params[key]){
//						bxsAPI.fieldDict[key] = bxsAPI.fieldDict[key] +" / "+ apiobj.params[key];
//					}
//				}else{
//					bxsAPI.fieldDict[key] = apiobj.params[key];
//				}
//			}
//		}
//	});
		
	for(key in bxsAPI.fieldDict){
		var desc = bxsAPI.fieldDict[key];
		var trm = new Element("tr",{"class":"trm"}).inject(apitbl);
		var tdmn = new Element("td",{"class":"tdn_dict"}).inject(trm).set("html",key);
		var tdmc = new Element("td",{"class":"tdc","colspan":2}).inject(trm).set("html",desc);
	}
}
bxsAPI.execUpdates = function(){
	var apiboxwrapborder = $("apiboxwrapborder");
		
	var apibox = new Element("div",{"class":"apibox"}).inject(apiboxwrapborder);
	var apih3 = new Element("div",{"class":"apih3"}).inject(apibox).set("html","<span style='color:blue'>更新记录</span>");
	var apitbl = new Element("table",{"class":"apitbl","border":"0","width":"100%","cellpadding":"0","cellspacing":"0"}).inject(apibox);
		
	for(var i=0;i<bxsAPI.updated.length;i++){
		var upobj = bxsAPI.updated[i];
		var trm = new Element("tr",{"class":"trm"}).inject(apitbl);
		var tdmn = new Element("td",{"class":"tdn"}).inject(trm).set("html",upobj.ver);
		var tdmc = new Element("td",{"class":"tdc_dt"}).inject(trm).set("html",upobj.dt);
		var tdmcon = new Element("td",{"class":"tdc"}).inject(trm);
		for(var k=0;k<upobj.con.length;k++){
			 new Element("div",{"class":"tdcondiv"}).inject(tdmcon).set("html",upobj.con[k]);
		}
	}
}
bxsAPI.execServer = function(){
	var apiboxwrapborder = $("apiboxwrapborder");
		
	var apibox = new Element("div",{"class":"apibox"}).inject(apiboxwrapborder);
	var apih3 = new Element("div",{"class":"apih3"}).inject(apibox).set("html","<span style='color:#666'>服务器信息</span>");
	var apitbl = new Element("table",{"class":"apitbl","border":"0","width":"100%","cellpadding":"0","cellspacing":"0"}).inject(apibox);
		
	var trm = new Element("tr",{"class":"trm"}).inject(apitbl);
	new Element("td",{"class":"tdn"}).inject(trm).set("html","服务器");
	new Element("td",{"class":"tdc"}).inject(trm).set("html",bxsAPI.server);
	
	trm = new Element("tr",{"class":"trm"}).inject(apitbl);
	new Element("td",{"class":"tdn"}).inject(trm).set("html","测试账号");
	new Element("td",{"class":"tdc"}).inject(trm).set("html","uid="+bxsAPI.uid+",un="+bxsAPI.un);
	
	if(bxsAPI.readme){
		trm = new Element("tr",{"class":"trm"}).inject(apitbl);
		new Element("td",{"class":"tdn"}).inject(trm).set("html","自述");
		new Element("td",{"class":"tdc"}).inject(trm).set("html",bxsAPI.readme);
	}
	
}
bxsAPI.execReturnCode = function(){
	var apiboxwrapborder = $("apiboxwrapborder");
		
	var apibox = new Element("div",{"class":"apibox"}).inject(apiboxwrapborder);
	var apih3 = new Element("div",{"class":"apih3"}).inject(apibox).set("html","返回代码");
	var apitbl = new Element("table",{"class":"apitbl","border":"0","width":"100%","cellpadding":"0","cellspacing":"0"}).inject(apibox);
		
	for(key in bxsAPI.returnCode){
		var desc = bxsAPI.returnCode[key];
		var trm = new Element("tr",{"class":"trm"}).inject(apitbl);
		var tdmn = new Element("td",{"class":"tdn_dict"}).inject(trm).set("html",key);
		var tdmc = new Element("td",{"class":"tdc","colspan":2}).inject(trm).set("html",desc);
	}
}
bxsAPI.execThirdInfo = function(){
	var apiboxwrapborder = $("apiboxwrapborder");
		
	var apibox = new Element("div",{"class":"apibox"}).inject(apiboxwrapborder);
	var apih3 = new Element("div",{"class":"apih3"}).inject(apibox).set("html","第三方信息");
	var apitbl = new Element("table",{"class":"apitbl","border":"0","width":"100%","cellpadding":"0","cellspacing":"0"}).inject(apibox);
	for(key in bxsAPI.thirdInfo){
		var thirdObj = bxsAPI.thirdInfo[key];
		var trm = new Element("tr",{"class":"trm"}).inject(apitbl);
		var tdmn = new Element("td",{"class":"tdn_dict"}).inject(trm).set("html",thirdObj.ti);
		var tdmc = new Element("td",{"class":"tdc"}).inject(trm);
		for(var i=0;i<thirdObj.items.length;i++){
			var itemObj = thirdObj.items[i];
			var dl = new Element("dl",{"class":"thirddl"}).inject(tdmc);
			var dt = new Element("dt").inject(dl).set("html",itemObj.ti);
			for(k=0;k<itemObj.keys.length;k++){
				var itemStr = itemObj.keys[k];
				var dd = new Element("dd").inject(dl).set("html",itemStr)
			}
		}
	}
}
bxsAPI.listno = function(L1,L2,L3){
	var s = "<span style='margin-right:5px;'>"+L1;
	if(L2) s += "."+L2;
	if(L3) s += "."+L3;
	return s+"</span>";
}
bxsAPI.layout = {
	init:function(){
		var layoutbox = $("layoutbox");
		if(!layoutbox){
			layoutbox = new Element("div",{"class":"layoutbox"}).inject(document.body);
			var arr = ["TOP","Default","Auto_Screen","Full"];
			for(var i=0;i<arr.length;i++){
				var aa = new Element("a",{"href":"###"}).inject(layoutbox).set("html",arr[i]);
				aa.val = arr[i];
				aa.onclick = function(){
					if(!this.val) return;
					var apiboxwrap = $("apiboxwrap");
					if(!apiboxwrap) return;
					if(this.val=="Default"){
						apiboxwrap.style.width = "830px";
						apiboxwrap.style.margin = "0 auto";
					}else if(this.val=="Auto_Screen"){
						apiboxwrap.style.width = "auto";
						apiboxwrap.style.margin = "0 230px 0 0";
					}else if(this.val=="Full"){
						apiboxwrap.style.width = "auto";
						apiboxwrap.style.margin = "0 0 0 0";
					}else if(this.val="TOP"){//回到顶部
						document.documentElement.scrollTop = document.body.scrollTop =0;
					}
				}
			}
		}
	}
}
bxsAPI.ver = function(){
	document.write("<span class='spanversion'>_"+bxsAPI.version+"</span>")
}
document.write("<script type=\"text/javascript\" src=\"jsbeautify/beautify.js\"></script>");
