$("#queryCategoryLevel1").change(function(){
	var queryCategoryLevel1 = $("#queryCategoryLevel1").val();
	if(queryCategoryLevel1 != '' && queryCategoryLevel1 != null){
		$.ajax({
			type:"GET",//请求类型
			url:"/categorylevellist",//请求的url
			data:{id:queryCategoryLevel1},//请求参数
			success:function(data){//data：返回数据（json对象）
				$("#queryCategoryLevel2").html("");
				var options = "<option value=\"\">--请选择--</option>";
				for(var i = 0; i < data.length; i++){
					options += "<option value=\""+data[i].id+"\">"+data[i].categoryname+"</option>";
				}
				$("#queryCategoryLevel2").html(options);
			},
			error:function(data){//当访问时候，404，500 等非200的错误状态码
				alert("加载二级分类失败！");
			}
		});
	}else{
		$("#queryCategoryLevel2").html("");
		var options = "<option value=\"\">--请选择--</option>";
		$("#queryCategoryLevel2").html(options);
	}
	$("#queryCategoryLevel3").html("");
	var options = "<option value=\"\">--请选择--</option>";
	$("#queryCategoryLevel3").html(options);
});

$("#queryCategoryLevel2").change(function(){
	var queryCategoryLevel2 = $("#queryCategoryLevel2").val();
	if(queryCategoryLevel2 != '' && queryCategoryLevel2 != null){
		$.ajax({
			type:"GET",//请求类型
			url:"/categorylevellist",//请求的url
			data:{id:queryCategoryLevel2},//请求参数
			success:function(data){//data：返回数据（json对象）
				$("#queryCategoryLevel3").html("");
				var options = "<option value=\"\">--请选择--</option>";
				for(var i = 0; i < data.length; i++){
					//alert(data[i].id);
					//alert(data[i].categoryName);
					options += "<option value=\""+data[i].id+"\">"+data[i].categoryname+"</option>";
				}
				$("#queryCategoryLevel3").html(options);
			},
			error:function(data){//当访问时候，404，500 等非200的错误状态码
				alert("加载三级分类失败！");
			}
		});
	}else{
		$("#queryCategoryLevel3").html("");
		var options = "<option value=\"\">--请选择--</option>";
		$("#queryCategoryLevel3").html(options);
	}
});


$(".addVersion").on("click",function(){
	var obj = $(this);
	window.location.href="/appversionadd?id="+obj.attr("appinfoid");
	// window.location.href="/jsp/developer/appversionadd.jsp";
});
$(".modifyVersion").on("click",function(){
	var obj = $(this);
	var status = obj.attr("status");
	var versionid = obj.attr("versionid");
	var appinfoid = obj.attr("appinfoid");
	if(status == "1" || status == "3"){//待审核、审核未通过状态下才可以进行修改操作
		if(versionid == null || versionid == ""){
			alert("该APP应用无版本信息，请先增加版本信息！");
		}else{
			window.location.href="appversionmodify?vid="+ versionid + "&aid="+ appinfoid;
		}
	}else{
		alert("该APP应用的状态为：【"+obj.attr("statusname")+"】,不能修改其版本信息，只可进行【新增版本】操作！");
	}
});
$(".modifyAppInfo").on("click",function(){
	var obj = $(this);
	var status = obj.attr("status");
	if(status == 1 || status == 3){//待审核、审核未通过状态下才可以进行修改操作
		window.location.href="appinfomodify?id="+ obj.attr("appinfoid");
		//window.location.href="/jsp/developer/appinfomodify.jsp";
	}else{
		alert("该APP应用的状态为：【"+obj.attr("statusname")+"】,不能修改！");
	}
});

$(document).on("click",".saleSwichOpen,.saleSwichClose",function(){
	var obj = $(this);
	var appinfoid = obj.attr("appinfoid");
	var saleSwitch = obj.attr("saleSwitch");
	if("open" === saleSwitch){
		saleSwitchAjax(appinfoid,obj);
	}else if("close" === saleSwitch){
		if(confirm("你确定要下架您的APP应用【"+obj.attr("appsoftwarename")+"】吗？")){
			saleSwitchAjax(appinfoid,obj);
		}
	}
});

var saleSwitchAjax = function(appId,obj){
	$.ajax({
		type:"PUT",
		url:appId+"/sale.json",
		dataType:"json",
		success:function(data){
			/*
			 * resultMsg:success/failed
			 * errorCode:exception000001
			 * appId:appId
			 * errorCode:param000001
			 */
			if(data.errorCode === '0'){
				if(data.resultMsg === "success"){//操作成功
					if("open" === obj.attr("saleSwitch")){
						//alert("恭喜您，【"+obj.attr("appsoftwarename")+"】的【上架】操作成功");
						$("#appInfoStatus" + obj.attr("appinfoid")).html("已上架");
						obj.className="saleSwichClose";
						obj.html("下架");
						obj.attr("saleSwitch","close");
						$("#appInfoStatus" + obj.attr("appinfoid")).css({
							'background':'green',
							'color':'#fff',
							'padding':'3px',
							'border-radius':'3px'
						});
						$("#appInfoStatus" + obj.attr("appinfoid")).hide();
						$("#appInfoStatus" + obj.attr("appinfoid")).slideDown(300);
					}else if("close" === obj.attr("saleSwitch")){
						//alert("恭喜您，【"+obj.attr("appsoftwarename")+"】的【下架】操作成功");
						$("#appInfoStatus" + obj.attr("appinfoid")).html("已下架");
						obj.className="saleSwichOpem";
						obj.html("上架");
						obj.attr("saleSwitch","open");
						$("#appInfoStatus" + obj.attr("appinfoid")).css({
							'background':'red',
							'color':'#fff',
							'padding':'3px',
							'border-radius':'3px'
						});
						$("#appInfoStatus" + obj.attr("appinfoid")).hide();
						$("#appInfoStatus" + obj.attr("appinfoid")).slideDown(300);
					}
				}else if(data.resultMsg === "failed"){//删除失败
					if("open" === obj.attr("saleSwitch")){
						alert("恭喜您，【"+obj.attr("appsoftwarename")+"】的【上架】操作失败");
					}else if("close" === obj.attr("saleSwitch")){
						alert("恭喜您，【"+obj.attr("appsoftwarename")+"】的【下架】操作失败");
					}
				}
			}else{
				if(data.errorCode === 'exception000001'){
					alert("对不起，系统出现异常，请联系IT管理员");
				}else if(data.errorCode === 'param000001'){
					alert("对不起，参数出现错误，您可能在进行非法操作");
				}
			}
		},
		error:function(data){
			if("open" === obj.attr("saleSwitch")){
				alert("恭喜您，【"+obj.attr("appsoftwarename")+"】的【上架】操作成功");
			}else if("close" === obj.attr("saleSwitch")){
				alert("恭喜您，【"+obj.attr("appsoftwarename")+"】的【下架】操作成功");
			}
		}
	});
};



$(".viewApp").on("click",function(){
	var obj = $(this);
	window.location.href="appview/"+ obj.attr("appinfoid");
	/*window.location.href="/jsp/developer/appinfoview.jsp";*/
});

$(".deleteApp").on("click",function(){
	var obj = $(this);
	if(confirm("你确定要删除APP应用【"+obj.attr("appsoftwarename")+"】及其所有的版本吗？")){
		$.ajax({
			type:"GET",
			url:"/dev/app/delapp",
			data:{id:obj.attr("appinfoid")},
			success:function(data){
				if(data.success){//删除成功：移除删除行
					alert("删除成功");
					obj.parents("tr").remove();
					//删除之后进行刷新页面  不然总条数需要手动刷新页面才能变更
					location.reload();
				}else {//删除失败
					alert("对不起，删除AAP应用【"+obj.attr("appsoftwarename")+"】失败");
				}
			},
			error:function(data){
				alert("对不起，删除失败");
			}
		});
	}
});

	
