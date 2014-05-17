/*-------------------------------------------
	Demo graphs for Flot Chart page (charts_flot.html)
---------------------------------------------*/
//
// Graph1 created in element with id = box-one-content
//
function FlotGraph1(){
	// We use an inline data source in the example, usually data would
	// be fetched from a server
	var data = [],
	totalPoints = 300;
	function getRandomData() {
		if (data.length > 0)
			data = data.slice(1);
		// Do a random walk
		while (data.length < totalPoints) {
			var prev = data.length > 0 ? data[data.length - 1] : 50,
			y = prev + Math.random() * 10 - 5;
			if (y < 0) {
				y = 0;
			} else if (y > 100) {
				y = 100;
			}
			data.push(y);
		}
		// Zip the generated y values with the x values
		var res = [];
		for (var i = 0; i < data.length; ++i) {
			res.push([i, data[i]])
		}
		return res;
	}
	var updateInterval = 30;
	var plot = $.plot("#box-one-content", [ getRandomData() ], {
		series: {
			shadowSize: 0	// Drawing is faster without shadows
		},
		yaxis: {min: 0,	max: 100},
		xaxis: {show: false	}
	});
	function update() {
		plot.setData([getRandomData()]);
		// Since the axes don't change, we don't need to call plot.setupGrid()
		plot.draw();
		setTimeout(update, updateInterval);
	}
	update();
}
//
// Graph2 created in element with id = box-two-content
//
function FlotGraph2(){
	var sin = [];
	var cos = [];
	var tan = [];
	for (var i = 0; i < 14; i += 0.1) {
		sin.push([i, Math.sin(i)]);
		cos.push([i, Math.cos(i)]);
		tan.push([i, Math.tan(i)/4]);
	}
	var plot = $.plot("#box-two-content", [
		{ data: sin, label: "sin(x) = -0.00"},
		{ data: cos, label: "cos(x) = -0.00" },
		{ data: tan, label: "tan(x)/4 = -0.00" }
	], {
		series: {
			lines: {
				show: true
			}
		},
		crosshair: {
			mode: "x"
		},
		grid: {
			hoverable: true,
			autoHighlight: false
		},
		yaxis: {
			min: -5.2,
			max: 5.2
		}
	});
	var legends = $("#box-two-content .legendLabel");
	legends.each(function () {
		// fix the widths so they don't jump around
		$(this).css('width', $(this).width());
	});
	var updateLegendTimeout = null;
	var latestPosition = null;
	function updateLegend() {
		updateLegendTimeout = null;
		var pos = latestPosition;
		var axes = plot.getAxes();
		if (pos.x < axes.xaxis.min || pos.x > axes.xaxis.max ||
			pos.y < axes.yaxis.min || pos.y > axes.yaxis.max) {
			return;
		}
		var i, j, dataset = plot.getData();
		for (i = 0; i < dataset.length; ++i) {
			var series = dataset[i];
			// Find the nearest points, x-wise
			for (j = 0; j < series.data.length; ++j) {
				if (series.data[j][0] > pos.x) {
					break;
				}
			}
		// Now Interpolate
		var y, p1 = series.data[j - 1],	p2 = series.data[j];
			if (p1 == null) {
				y = p2[1];
			} else if (p2 == null) {
				y = p1[1];
			} else {
				y = p1[1] + (p2[1] - p1[1]) * (pos.x - p1[0]) / (p2[0] - p1[0]);
			}
			legends.eq(i).text(series.label.replace(/=.*/, "= " + y.toFixed(2)));
		}
	}
	$("#box-two-content").bind("plothover",  function (event, pos, item) {
		latestPosition = pos;
		if (!updateLegendTimeout) {
			updateLegendTimeout = setTimeout(updateLegend, 50);
		}
	});
}
//
// Graph3 created in element with id = box-three-content
//
function FlotGraph3(){
	var d1 = [];
	for (var i = 0; i <= 60; i += 1) {
		d1.push([i, parseInt(Math.random() * 30 - 10)]);
	}
	function plotWithOptions(t) {
		$.plot("#box-three-content", [{
			data: d1,
			color: "rgb(30, 180, 20)",
			threshold: {
				below: t,
				color: "rgb(200, 20, 30)"
			},
			lines: {
				steps: true
			}
		}]);
	}
	plotWithOptions(0);
}
//
// Graph4 created in element with id = box-four-content
//
function FlotGraph4(){
	var d1 = [];
	for (var i = 0; i < 14; i += 0.5) {
		d1.push([i, Math.sin(i)]);
	}
	var d2 = [[0, 3], [4, 8], [8, 5], [9, 13]];
	var d3 = [];
	for (var i = 0; i < 14; i += 0.5) {
		d3.push([i, Math.cos(i)]);
	}
	var d4 = [];
	for (var i = 0; i < 14; i += 0.1) {
		d4.push([i, Math.sqrt(i * 10)]);
	}
	var d5 = [];
	for (var i = 0; i < 14; i += 0.5) {
		d5.push([i, Math.sqrt(i)]);
	}
	var d6 = [];
	for (var i = 0; i < 14; i += 0.5 + Math.random()) {
		d6.push([i, Math.sqrt(2*i + Math.sin(i) + 5)]);
	}
	$.plot("#box-four-content", [{
		data: d1,
			lines: { show: true, fill: true }
		}, {
			data: d2,
			bars: { show: true }
		}, {
			data: d3,
			points: { show: true }
		}, {
			data: d4,
			lines: { show: true }
		}, {
			data: d5,
			lines: { show: true },
			points: { show: true }
		}, {
			data: d6,
			lines: { show: true, steps: true }
		}]);
}
