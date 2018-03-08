//初始化
$(document).ready(function() {
	// setTimeout("refreshcurrentvcoin()", 5000);
	$('.tradeDiv').pwstabs({
		effect : 'scale',
		defaultTab : 1,
		containerWidth : '1120px',
		tabsPosition : 'vertical',
		responsive : 'false',
		verticalPosition : 'left'
	});
});

var refreshindex = 0;

// 刷新虚拟币数据表格
function refreshcurrentvcoin() {
	$
			.ajax({
				url : "/interfaces/trade/homevcoin",
				cache : false,
				dataType : "json",
				success : function(virtualcoins) {
					var tbvcointrs = $("#tbvcoin tr");
					for (var i = 1; i < tbvcointrs.length; i++) {
						var vcoinid = $(tbvcointrs[i]).attr("id").substring(9);
						for (var j = 0; j < virtualcoins.length; j++) {
							var virtualcoin = virtualcoins[j];
							if (vcoinid == virtualcoin.id) {
								var tbvcointrtds = $("#tbtrcoin_" + vcoinid)
										.children();
								$(tbvcointrtds[1]).text("" + virtualcoin.price);
								if (virtualcoin.increaserate < 0)
									$(tbvcointrtds[2]).css("color", "red");
								else if (virtualcoin.increaserate > 0)
									$(tbvcointrtds[2]).css("color", "#02CD1A");
								if (virtualcoin.increaserate != 0)
									$(tbvcointrtds[2]).text(
											virtualcoin.increaserate + "%");
								else
									$(tbvcointrtds[2]).text("--");
								// $(tbvcointrtds[3]).text("" +
								// virtualcoin.bottomprice24hour + " / " +
								// virtualcoin.topprice24hour);
								$(tbvcointrtds[3]).text(
										virtualcoin.tradecount24hour);
								$(tbvcointrtds[4])
										.text(
												""
														+ parseFloat(parseFloat(
																virtualcoin.trademoney24hour)
																.toFixed(2)));
							}
						}
					}
					if (refreshindex++ < 60)
						setTimeout("refreshcurrentvcoin()", 5000);
				},
				error : function() {
				}
			});
}

// 切换版块
function switchvirtualcoinboard(obj, index) {
	$(obj).siblings().removeClass("cur");
	$(obj).addClass("cur");
	$(obj).parent().parent().find(".tbtrcoinboards").hide();
	$(obj).parent().parent().find(".tbtrcoinboard_" + index).show();
};
