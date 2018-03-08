//表单验证
function checkform(){
	if(checknull("vcoinname", "币种名称") && checknull("teamname", "开发团队") && checknull("telphone", "联系电话")){
		preventresubmit();
		return true;
	}else{
		return false;
	}
}
