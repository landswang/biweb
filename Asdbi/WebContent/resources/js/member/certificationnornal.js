//表单验证
function checkform(){
    if(checknull("name", "姓名") && checknull("idcard", "身份证号码") && checkidcard("idcard", "身份证号码") && checkmobilevocde() && checknull("mobilevcode", "短信验证码")){
    	preventresubmit();
    	return true;
    }else{
    	return false;
    }
}