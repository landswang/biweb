//限制整数输入
function limitint(obj){
	if(event.keyCode != 8 && event.keyCode != 9 && event.keyCode != 13 && event.keyCode != 37 && event.keyCode != 39 && event.keyCode < 48 || event.keyCode > 57 && event.keyCode < 96 || event.keyCode > 105 && event.keyCode != 116 && event.keyCode != 144) event.returnValue = null;
}

//限制小数输入
function limitfloat(obj){
	if(event.keyCode != 8 && event.keyCode != 9 && event.keyCode != 13 && event.keyCode != 37 && event.keyCode != 39 && event.keyCode < 48 || event.keyCode > 57 && event.keyCode < 96 || event.keyCode > 105 && event.keyCode != 110 && event.keyCode != 116 && event.keyCode != 144 && event.keyCode != 190 || (event.keyCode == 110 || event.keyCode == 190) && ($(obj).val() == "" || $(obj).val().indexOf(".") > -1)) event.returnValue = null;
}

//文本框非空验证
function checknull(id,msg){
	if($("#" + id).val() == ""){
		alert("请输入"+msg);
		$("#" + id).focus();
		return false;
	}else{
		return true;	
	}
}

//上传框非空验证
function checknullUp(id,msg){
	if($("#" + id).val() == ""){
		alert("请上传"+msg);
		return false;
	}else{
		return true;	
	}
}

//单选框非空验证
function checknullRd(name, msg){
    var objs = $("input[name='" + name + "']");
    for (i = 0; i < objs.length; i++){
        if (objs[i].checked){
            return true;
            break;
        }
    }
    alert(msg + "必须选择一项");
    return false;
}

//复选框非空验证
function checknullCb(name, msg){
    var objs = $("input[name='" + name + "']");
    for (i = 0; i < objs.length; i++){
        if (objs[i].checked){
            return true;
            break;
        }
    }
    alert(msg + "必须至少选择一项");
    return false;
}

//文本框整数类型验证
function checkint(id,msg){
	var regex = /^[0-9]*$/;
	if(!regex.test($("#" + id).val())){
		alert(msg+"只能输入整数数字");	
		$("#" + id).focus();
		return false;
	} else {
		return true;
	}
}

//文本框浮点型类型验证
function checkfloat(id,msg){
	var regex = /^[0-9]+\.?[0-9]*$/;
	if(!regex.test($("#" + id).val())){
		alert(msg+"只能输入数字");	
		$("#" + id).focus();
		return false;
	} else {
		return true;
	}
}

//两文档框值是否相等验证
function checkequal(id1,id2,msg1,msg2){
	if($("#" + id1).val() != $("#" + id2).val()){
		alert(msg1+"和"+msg2+"值不一致");	
		$("#" + id2).focus();
		$("#" + id2).select();
		return false;
	} else {
		return true;
	}
}

//文本框字符长度验证不能短于length
function checklength(id, length, msg){
    if ($("#" + id).val().length < length){
        alert(msg + "长度不能短于" + length + "位");
		$("#" + id).focus();
        return false;
    } else {
        return true;
    }
}

//密码策略验证
function checkpassword(id){
	var regex = /^(?![a-zA-z]+$)(?!\d+$)(?![!@#$%^&*]+$)[a-zA-Z\d!@#$%^&*]+$/;
    if (regex.test($("#" + id).val())){
        return true;
    } else {
        alert("密码由6-21字母和数字组成，不能是纯数字或纯英文。");
        return false;
    }
}

//验证手机号码 
function checkmobile(id,msg){
	var regex = /^(1)\d{10}$/;
	if (regex.test($("#" + id).val())){
		return true;
	} else {
		alert(msg + "格式错误");
		$("#" + id).focus();
		return false;
	}
}

//验证身份证号码 
function checkidcard(id,msg){
	var regex = /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/;
	if (regex.test($("#" + id).val())){
		return true;
	} else {
		alert(msg + "格式错误");
		$("#" + id).focus();
		return false;
	}
}

//全选
function selectallcheckbox(obj){
	$("input[name='ids']").attr("checked", obj.checked);
}

//处理所选数据
function submitselectdata(action, msg, param){
	theParam="";
	theIds = $("input[name='ids']");
	for(var i=0;i<theIds.length;i++){
		if(theIds[i].checked) theParam += "," + theIds[i].value;
	}
	if(theParam==""){
		alert("请至少选择一项需要" + msg + "的数据");
	}else{
		if(confirm("确定" + msg + "所选数据吗？")){
			theParam = theParam.substring(1);
			window.location.replace(action + "=" + theParam + param);
		}
	}
}

//防止重复提交
function preventresubmit(){
	$("input[type=submit]").attr("disabled", true);
	$("a").removeAttr("href");
	$("*").removeAttr("onclick");
}

//数据加载中
function dataloading(){
    $("body").html("<div style='text-align:center; margin-top:200px;'><img src='/resources/images/loading.gif' width='50' height='50'></div>");
}

//获取URL参数
function geturlparam(name){ 
	var result = location.search.match(new RegExp("[\?\&]" + name+ "=([^\&]+)","i")); 
	return result == null || result.length < 1 ? null : result[1]; 
}

//读取cookie
function readCookie(name){
    var cookieValue = null;
    var search = name + "=";
    if (document.cookie.length > 0){
        offset = document.cookie.indexOf(search);
        if (offset != -1){
            offset += search.length;
            end = document.cookie.indexOf(";", offset);
            if (end == -1) end = document.cookie.length;
            cookieValue = (document.cookie.substring(offset, end));
            if (cookieValue.indexOf("\"") == 0 && cookieValue.indexOf("\\u") == 1 && cookieValue.lastIndexOf("\"") == cookieValue.length - 1){
            	cookieValue = unescape(cookieValue.substring(1, cookieValue.length - 1).replace(/\\u/gi, "%u"));
			}
        }
    }
    return cookieValue;
}

//写入cookie
function writeCookie(name, value, hours){
    var expire = "";
    if (hours != null){
        expire = new Date((new Date()).getTime() + hours * 3600000);
        expire = "; expires=" + expire.toGMTString();
    }
    document.cookie = name + "=" + escape(value) + expire;
}

//////////////////////////////////////////// 分页提交 ///////////////////////////////////////////////

//获取表单
function getform(param){
	var form = jQuery.type(param) == "object" ? $(param).parents("form") : $("form[name='" + param + "']").get(0);
	return form;
}

//设置当前页
function setpage(param, pageindex){
	$("#pageindex").val(pageindex);
    getform(param).submit();
}