//初始化
$(document).ready(function(){
	var clipboard = new Clipboard(document.getElementById("btncopyinviteaddress"));
    clipboard.on("success", function(e){
        alert("已成功复制到剪贴板");
    });
    clipboard.on("error", function(e){
    	alert("复制失败，请手动复制");
    });
});