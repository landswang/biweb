//初始化
$(document)
		.ready(
				function() {
					init_banner();
					// setTimeout("refreshcurrentvcoin()", 5000);
					// $(".tradePlatBtn").css("border-bottom", "4px solid
					// #9bd7d5")
					document.getElementsByClassName("tradePlatBtn")[0].style.borderBottom = "4px solid white";
				});

// 初始化Banner
function init_banner() {
	window.mySwipe = Swipe($(".banner").get(0), {
		auto : 5000,
		callback : function(index, element) {
			$(".banner .point li").eq(index).addClass("cur").siblings()
					.removeClass("cur")
		}
	});
	$(".banner .point li").click(function() {
		mySwipe.slide($(this).index());
	});
}

var refreshindex = 0;

// 刷新虚拟币数据
function refreshcurrentvcoin(vcoinid) {
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
// 切换交易区
function change_t_p(obj) {

	$(".tradePlatBtn").css("border-bottom", "4px solid #9bd7d5")
	document.getElementsByClassName("tradePlatBtn")[obj - 1].style.borderBottom = "4px solid white";
	if (obj != 1) {
		alert('即将开放，敬请期待')
	}
	$(".tradePlatBtn").css("border-bottom", "4px solid #9bd7d5")
	document.getElementsByClassName("tradePlatBtn")[0].style.borderBottom = "4px solid white";
}

// 切换版块
function switchvirtualcoinboard(obj, index) {
	$(obj).siblings().removeClass("cur");
	$(obj).addClass("cur");
	$(obj).parent().parent().find(".tbtrcoinboards").hide();
	$(obj).parent().parent().find(".tbtrcoinboard_" + index).show();
};

// 点击虚拟币
function clickvcoin(id) {
	window.location.replace("/trade/" + id + ".html");
}
