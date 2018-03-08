var dialog = null;
var dialogXrate = -1;
var dialogYrate = -1;
var dialogShadecreated = false;
var dialogCallbackhandler = null;
var dialogAbledmove = false;

//打开对话框
function opendialog(name, dialogurl, params, ihandler, chandler){
	dialog = $("#" + name);
	$.ajax({
        type: "post",
        url: dialogurl,
        data: params,
		async: false,
        cache: false,
        success: function(data){
			dialog.html(data);
			var dialogLeft = document.body.scrollLeft;
			if(document.body.clientWidth > parseInt(dialog.width())) dialogLeft += (document.body.clientWidth - parseInt(dialog.width())) / 2;
			var dialogTop = document.body.scrollTop;
			if(document.body.clientHeight > parseInt(dialog.height())) dialogTop += (document.body.clientHeight - parseInt(dialog.height())) / 2;
			dialog.css("left", dialogLeft + "px");
			dialog.css("top", dialogTop + "px");
			dialog.show();
			if(!dialogShadecreated){
				dialog.after("<div class='dialogshade'></div>");
				$(".dialogshade").width($(document).width());
				$(".dialogshade").height($(document).height());
				dialogShadecreated = true;
			}
			$(".dialogshade").show();
			if(chandler != null) dialogCallbackhandler = chandler;
			if(ihandler != null) ihandler();
        },
		error: function(){   
			alert("Dialog回调异常！");  
     	} 
    });
}

//对话框头鼠标按下事件
function dialogheadclickdown(obj){
	dialogXrate = event.pageX - parseInt($(obj).parent().css("left"));
	dialogYrate = event.pageY - parseInt($(obj).parent().css("top"));
	dialogAbledmove = true;
	$(obj).bind("mousemove",dialogmove);
}

//对话框头鼠标弹出事件
function dialogheadclickup(obj){
	dialogAbledmove = false;
	$(obj).unbind("mousemove",dialogmove);
}

//对话框移动事件
function dialogmove(){
	if(dialogAbledmove){
		$("#x").text(event.pageX);
		$("#y").text(event.pageY);
		$(dialog).css("left", event.pageX - dialogXrate);
		$(dialog).css("top", event.pageY - dialogYrate);
	}
}

//对话框提交事件
function dialogsubmit(){
	if(dialogCallbackhandler != null) dialogCallbackhandler();
	dialogclose();
}

//关闭对话框
function dialogclose(){
	$(dialog).hide();
	dialog = null;
	dialogCallbackhandler = null;
	$(".dialogshade").hide();
}