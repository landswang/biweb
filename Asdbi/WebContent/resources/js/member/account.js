//上传头像
var uploaded = true;
function upload(){
	var ext = $("#facefile").val().substring($("#facefile").val().indexOf(".")).toLowerCase();
	if(ext != ".jpg" && ext != ".jpeg" && ext != ".png" && ext != ".gif"){
		alert("请上传图片格式文件");
		return;
	}
	var filedata = $("#facefile").get(0).files[0];
	if(filedata.size > 1048576){
		alert("请上传小于1M的图片");
		return;
	}
	$("#uploading").css("display","block");
    $.ajaxFileupload({
    	url: "/member/system/file/upload?savepath=member%2Fface",
        secureuri: false,
        fileElementId: "facefile",
        dataType: "html",
        success: function(data, status){
			$("#uploading").hide();
            if (uploaded){
                if(data.indexOf("/") == 0){
                	$("#face").val(data);
                    $("#face_pre").show();
                    $("#face_pre").attr("src", data + "?temp=" + Math.round(Math.random() * 1000000));
            	}else{
            		alert("图片上传失败，仅支持Jpg、Png、Gif且小于2M的图片");
                    uploaded = false;
            	}
            }
            $("#facefile").val("");
        },
        error: function(data, status, e){
			$("#uploading").hide();
    		alert("图片上传失败，仅支持Jpg、Png、Gif且小于2M的图片");
            uploaded = false;
            $("#facefile").val("");
        }
    });
}

//表单验证
function checkform(){
	if(checknull("email", "电子邮箱")){
		preventresubmit();
		return true;
	}else{
		return false;
	}
}