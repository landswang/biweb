function splitData(rawData){
    var categoryData = [];
    var values = [];
    var volumes = [];
    for (var i = 0; i < rawData.length; i++){
        categoryData.push(rawData[i].splice(0, 1)[0]);
        values.push(rawData[i]);
        volumes.push([i, rawData[i][4], rawData[i][0] > rawData[i][1] ? 1 : -1]);
    }

    return {
        categoryData: categoryData,
        values: values,
        volumes: volumes
    };
}

function calculateMA(dayCount, data){
    var result = [];
    for (var i = 0, len = data.values.length; i < len; i++){
        if (i < dayCount){
            result.push('-');
            continue;
        }
        var sum = 0;
        for (var j = 0; j < dayCount; j++){
            sum += data.values[i - j][1];
        }
        result.push(+(sum / dayCount).toFixed(3));
    }
    return result;
}

var upColor = '#00da3c';
var downColor = '#ec0000';

//绘制K线图
function drawkline(virtualcoinid, timetype){
	$.get("/file/kline/" + virtualcoinid + "/" + timetype + ".json", null, function(rawData){
	    var data = splitData(rawData);
	    myChart.setOption(option = {
	        backgroundColor: '#fff',
	        animation: true,
	        legend: {
	            bottom: 10,
	            left: 'center',
	            data: ['Dow-Jones index', '5均钱', '10均钱', '20均钱', '30均钱']
	        },
	        tooltip: {
	            trigger: 'axis',
	            axisPointer: {
	                type: 'cross'
	            },
	            backgroundColor: 'rgba(245, 245, 245, 0.9)',
	            borderWidth: 1,
	            borderColor: '#ccc',
	            padding: 10,
	            textStyle: {
	                color: '#000'
	            },
	            position: function (pos, params, el, elRect, size){
	                var obj = {top: 40, left: (pos[0] < size.viewSize[0] - 300 ? pos[0] + 20 : pos[0] - 200)};
	                return obj;
	            },
	            extraCssText: 'width:170px; font-size:12px;'
	        },
	        axisPointer: {
	            link: {xAxisIndex: 'all'},
	            label: {
	                backgroundColor: '#777'
	            }
	        },
	        visualMap: {
	            show: false,
	            seriesIndex: 5,
	            dimension: 2,
	            pieces: [{
	                value: 1,
	                color: downColor
	            }, {
	                value: -1,
	                color: upColor
	            }]
	        },
	        grid: [
	            {
	                left: '10%',
	                right: '8%',
	                height: '50%'
	            },
	            {
	                left: '10%',
	                right: '8%',
	                top: '63%',
	                height: '16%'
	            }
	        ],
	        xAxis: [
	            {
	                type: 'category',
	                data: data.categoryData,
	                scale: true,
	                boundaryGap : false,
	                axisLine: {onZero: false},
	                splitLine: {show: false},
	                splitNumber: 20,
	                min: 'dataMin',
	                max: 'dataMax',
	                axisPointer: {
	                    z: 100
	                }
	            },
	            {
	                type: 'category',
	                gridIndex: 1,
	                data: data.categoryData,
	                scale: true,
	                boundaryGap : false,
	                axisLine: {onZero: false},
	                axisTick: {show: false},
	                splitLine: {show: false},
	                axisLabel: {show: false},
	                splitNumber: 20,
	                min: 'dataMin',
	                max: 'dataMax',
	                axisPointer: {
	                    label: {
	                        formatter: function (params){
	                            var seriesValue = (params.seriesData[0] || {}).value;
	                            return params.value
	                            + (seriesValue != null
	                                ? '\n' + echarts.format.addCommas(seriesValue)
	                                : ''
	                            );
	                        }
	                    }
	                }

	            }
	        ],
	        yAxis: [
	            {
	                scale: true,
	                splitArea: {
	                    show: true
	                }
	            },
	            {
	                scale: true,
	                gridIndex: 1,
	                splitNumber: 2,
	                axisLabel: {show: false},
	                axisLine: {show: false},
	                axisTick: {show: false},
	                splitLine: {show: false}
	            }
	        ],
	        dataZoom: [
	            {
	                type: 'inside',
	                xAxisIndex: [0, 1],
	                start: 50,
	                end: 100
	            },
	            {
	                show: true,
	                xAxisIndex: [0, 1],
	                type: 'slider',
	                top: '82%',
	                start: 50,
	                end: 100
	            }
	        ],
	        series: [
	            {
	                name: '价格明细',
	                type: 'candlestick',
	                data: data.values,
	                itemStyle: {
	                    normal: {
	                        color: upColor,
	                        color0: downColor,
	                        borderColor: null,
	                        borderColor0: null
	                    }
	                }
	            },
	            {
	                name: '5均钱',
	                type: 'line',
	                data: calculateMA(5, data),
	                smooth: true,
	                lineStyle: {
	                    normal: {opacity: 0.5}
	                }
	            },
	            {
	                name: '10均钱',
	                type: 'line',
	                data: calculateMA(10, data),
	                smooth: true,
	                lineStyle: {
	                    normal: {opacity: 0.5}
	                }
	            },
	            {
	                name: '20均钱',
	                type: 'line',
	                data: calculateMA(20, data),
	                smooth: true,
	                lineStyle: {
	                    normal: {opacity: 0.5}
	                }
	            },
	            {
	                name: '30均钱',
	                type: 'line',
	                data: calculateMA(30, data),
	                smooth: true,
	                lineStyle: {
	                    normal: {opacity: 0.5}
	                }
	            },
	            {
	                name: '成交量',
	                type: 'bar',
	                xAxisIndex: 1,
	                yAxisIndex: 1,
	                data: data.volumes
	            }
	        ]
	    }, true);
	    $("#klineloading").hide();
	});
}