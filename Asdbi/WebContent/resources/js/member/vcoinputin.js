//打开/关闭币菜单
function openothercoins(event){
	$(".othercoins").slideToggle(300);
	event.stopPropagation ? event.stopPropagation() : event.cancelBubble = true;
}