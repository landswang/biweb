//初始化
$(document).ready(function(){
	$("#mobilevcode").val("");
});

//上传图片
var uploaded = true;
function upload(name){
	var ext = $("#" + name + "file").val().substring($("#" + name + "file").val().indexOf(".")).toLowerCase();
	if(ext != ".jpg" && ext != ".jpeg" && ext != ".png" && ext != ".gif"){
		alert("请上传图片格式文件");
		return;
	}
	var filedata = $("#" + name + "file").get(0).files[0];
	if(filedata.size > 2097152){
		alert("请上传小于2M的图片");
		return;
	}
	$("#" + name + "file").hide();
	$("#" + name + "_uploading").css("display","inline");
    $.ajaxFileupload({
    	url: "/member/system/file/upload?savepath=member%2F" + name,
        secureuri: false,
        fileElementId: name + "file",
        dataType: "html",
        success: function(data, status){
			$("#" + name + "_uploading").hide();
            if (uploaded){
            	if(data.indexOf("/") == 0){
                	$("#" + name).val(data);
                    $("#" + name + "_pre").show();
                    $("#" + name + "_pre").attr("src", data + "?temp=" + Math.round(Math.random() * 1000000));
            	}else{
            		alert("图片上传失败，仅支持Jpg、Png、Gif且小于2M的图片");
                    uploaded = false;
            	}
            }
            $("#" + name + "file").val("");
        	$("#" + name + "file").css("display","inline");
        },
        error: function(data, status, e){
			$("#" + name + "_uploading").hide();
    		alert("图片上传失败，仅支持Jpg、Png、Gif且小于2M的图片");
            uploaded = false;
            $("#" + name + "file").val("");
        	$("#" + name + "file").css("display","inline");
        }
    });
}

//表单验证
function checkform(){
    if(checknullUp("idcardpicfront", "身份证正面照片") && checknullUp("idcardpicback", "身份证背面照片") && checknullUp("idcardpiconhand", "手持身份证照片")){
    	preventresubmit();
    	return true;
    }else{
    	return false;
    }
}