//查询交易ID
function querytxid(brower,txid){
	if(brower == "") {
		alert("暂时无法查看");
	}else{
		window.open(brower+txid);
	}
}