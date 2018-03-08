//表单验证
function checkform(){
	var txtgradecontentses = $(".tablelist .comment textarea");
	for(var i=0; i<txtgradecontentses.length; i++){
		if ($(txtgradecontentses[i]).val() == ""){
			alert("请输入评价内容");
			$(txtgradecontentses[i]).get(0).focus();
			return false;
		}
	}
	preventresubmit();
	return true;
}