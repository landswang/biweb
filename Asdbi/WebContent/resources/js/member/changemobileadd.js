//上传图片
var uploaded = true;
function upload(){
	var ext = $("#chagemobilepicfile").val().substring($("#chagemobilepicfile").val().indexOf(".")).toLowerCase();
	if(ext != ".jpg" && ext != ".jpeg" && ext != ".png" && ext != ".gif"){
		alert("请上传图片格式文件");
		return;
	}
	var filedata = $("#chagemobilepicfile").get(0).files[0];
	if(filedata.size > 2097152){
		alert("请上传小于2M的图片");
		return;
	}
	$("#chagemobilepicfile").hide();
	$("#chagemobilepic_uploading").css("display","inline");
    $.ajaxFileupload({
    	url: "/member/system/file/upload?savepath=member%2Fchagemobilepic",
        secureuri: false,
        fileElementId: "chagemobilepicfile",
        dataType: "html",
        success: function(data, status){
			$("#chagemobilepic_uploading").hide();
            if (uploaded){
            	if(data.indexOf("/") == 0){
	            	$("#chagemobilepic").val(data);
	                $("#chagemobilepic_pre").show();
	                $("#chagemobilepic_pre").attr("src", data + "?temp=" + Math.round(Math.random() * 1000000));
            	}else{
            		alert("图片上传失败，仅支持Jpg、Png、Gif且小于2M的图片");
                    uploaded = false;
            	}
            }
            $("#chagemobilepicfile").val("");
        	$("#chagemobilepicfile").css("display","inline");
        },
        error: function(data, status, e){
			$("#chagemobilepic_uploading").hide();
    		alert("图片上传失败，仅支持Jpg、Png、Gif且小于2M的图片");
            uploaded = false;
            $("#chagemobilepicfile").val("");
        	$("#chagemobilepicfile").css("display","inline");
        }
    });
}

//表单验证
function checkform(){
    if(checknull("mobile", "新手机号码") && checknullUp("chagemobilepic", "申请拍照照片")){
    	preventresubmit();
    	return true;
    }else{
    	return false;
    }
}