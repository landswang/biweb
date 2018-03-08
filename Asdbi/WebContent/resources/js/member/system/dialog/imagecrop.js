var imagecrop_rateX = 1;
var imagecrop_rateY = 1;

//初始化
function imagecrop_init(){
    imagecrop_rateX = $("#rateX").val();
    try{
    	$("#dialog_imagecrop #photo").imgAreaSelect({ aspectRatio: imagecrop_rateX + ":" + imagecrop_rateY, handles: true, onSelectChange: imagecrop_preview });
        prettyPrint();
    } catch(ex){}
}

//裁切图片切换
function imagecrop_preview(img, selection){
    if (selection.width && selection.height){
        var scaleX = 100 / selection.width;
        var scaleY = 100 / selection.height;
        $('#dialog_imagecrop #preview img').css({
            width: Math.round(scaleX * $("#photo").width() * imagecrop_rateX),
            height: Math.round(scaleY * $("#photo").height() * imagecrop_rateY),
            marginLeft: -Math.round(scaleX * selection.x1 * imagecrop_rateX),
            marginTop: -Math.round(scaleY * selection.y1 * imagecrop_rateY)
        });
        $('#dialog_imagecrop #x1').text(selection.x1);
        $('#dialog_imagecrop #y1').text(selection.y1);
        $('#dialog_imagecrop #x2').text(selection.x2);
        $('#dialog_imagecrop #y2').text(selection.y2);
        $('#dialog_imagecrop #w').text(selection.width);
        $('#dialog_imagecrop #h').text(selection.height);
        $('#dialog_imagecrop #startX').val(selection.x1);
        $('#dialog_imagecrop #startY').val(selection.y1);
        $('#dialog_imagecrop #iWidth').val(selection.width);
    }
}

//隐藏选择框
function hideimgareaselectborder(){
	$(".imgareaselect-border1").hide();
	$(".imgareaselect-border2").hide();
	$(".imgareaselect-border3").hide();
	$(".imgareaselect-border4").hide();
	$(".imgareaselect-handle").hide();
	$(".imgareaselect-outer").hide();
	$(".imgareaselect-selection").parent().hide();
}

//确定裁切
function imagecrop_cut(){
    if ($("#dialog_imagecrop #iWidth").val() == ""){
        alert("请选择裁切区域");
    } else {
		$.ajax({  
			type: "post",      
			url: "/member/system/dialog/imagecropcut",  
			data: {
				filepath:$("#dialog_imagecrop #filepath").val(),
				pnlWidth:$("#dialog_imagecrop #pnlWidth").val(),
				pnlHeight:$("#dialog_imagecrop #pnlHeight").val(),
				preWidth:$("#dialog_imagecrop #photo").width(),
				startX:$("#dialog_imagecrop #startX").val(),
				startY:$("#dialog_imagecrop #startY").val(),
				iWidth:$("#dialog_imagecrop #iWidth").val(),
				iHeight:$("#dialog_imagecrop #iHeight").val()
			},
			async: false,
			cache: false,
			success: function(data){
				hideimgareaselectborder();
				dialogsubmit();
	        },
			error: function(){
				alert("裁切失败");  
			} 
		}); 
    }
}

//使用原图
function imagecrop_donot(){
	hideimgareaselectborder();
	dialogsubmit();
}

//使用原图
function imagecrop_cropcannel(){
	hideimgareaselectborder();
	dialogclose();
}

//修改尺寸事件
function switchsize(x, y){
    imagecrop_rateX = x / y;
    $("#dialog_imagecrop #photo").imgAreaSelect({ aspectRatio: imagecrop_rateX + ":" + imagecrop_rateY, handles: true, onSelectChange: imagecrop_preview });
    $("#dialog_imagecrop #leftframe").width(x * (100 / y));
    $("#dialog_imagecrop #pnlWidth").val(x);
    $("#dialog_imagecrop #pnlHeight").val(y);
}