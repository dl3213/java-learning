// import { from } from "_array-flatten@2.1.2@array-flatten"


const FROM = []
const TO = []
const URL_XZQ = ""
const URL_TOWN = ""
const URL_VILLAGE = ""
const URL_2016 = ""
const URL_2017 = ""
// const URL_SDYS = ""
const URL_SDYS = ""

var arcgis = new ArcGISJS()

var dlNum = ["建设用地", "耕地", "其它农用地", "未利用地"]
var XZQList = []

// var ret = arcgis.querySync(URL_TOWN, false, null, "1=1")

// arcgis.queryAsync(URL_2017, false, null, "DLMC = '水田'", function (res) {
// 	if (res) {
// 	}
// }, function (err) {
// })

{//异步
	arcgis.queryAsync(URL_TOWN, false, null, "1=1", function (res) {
		var xzqList = []
		for (var i = 0; i < res.features.length; i++) {
			var obj = {}
			obj.XZQDM = res.features[i].attributes.XZQDM
			obj.XZQMC = res.features[i].attributes.XZQMC
			xzqList.push(obj)
		}
		var counter = 0
		var xList = unique(xzqList)

		$("#xzqList").html("")
		for (var i = 0; i < xList.length; i++) {
			$("#xzqList").append("<li><button index='" + xList[i].XZQDM + "' name='" + xList[i].XZQMC + "'>" + xList[i].XZQDM + "---" + xList[i].XZQMC + "</button></li>")
		}

		$("#xzqList").on("click", "button", function () {
			var xzqmc = $(this).attr("name")
			var xzqdm = $(this).attr("index")
			var xzq = {}
			arcgis.queryAsync(URL_TOWN, true, null, "XZQDM = '" + xzqdm + "'", function (res) {
				var gg = []
				for (var i = 0; i < res.features.length; i++) {
					gg.push((res.features[i].geometry))
				}
				var bGeo = arcgis.union(gg)
				var bArea = arcgis.planarArea(bGeo)
				xzq = {
					XZQDM: xzqdm,
					XZQMC: xzqmc,
					XZQArea: bArea * 0.0015
					// geometry: bGeo
				}
				// item.area = bArea
				// XZQList.push(obj)
				// if (XZQList.length === 29) {
				// 	// saveJSON(XZQList, "xzqList.json")
				// }
				// var _sd
				// var _2017
				// var _2016
				arcgis.queryAsync4dl(URL_SDYS, true, bGeo, "1=1", function (sdSet) {
					if (sdSet) {
						var groupInsd = groupByDLBM(bGeo, sdSet.features, arcgis)
						var get4group = return4Group(groupInsd, arcgis)


						var sdJson = getGroupJson(groupInsd)
						xzq.DL_sd = sdJson

						arcgis.queryAsync4dl(URL_2017, true, bGeo, "1=1", function (_2017Set) {
							if (_2017Set) {
								var groupIn2017 = groupByDLBM(bGeo, _2017Set.features, arcgis)
								var get4groupIn2017 = return4Group(groupIn2017, arcgis)


								var json2017 = getGroupJson(groupIn2017)
								xzq.DL_2017 = json2017

								arcgis.queryAsync4dl(URL_2016, true, bGeo, "1=1", function (_2016Set) {
									if (_2016Set) {
										var groupIn2016 = groupByDLBM(bGeo, _2016Set.features, arcgis)
										var get4groupIn2016 = return4Group(groupIn2016, arcgis)
		
		
										var json2016 = getGroupJson(groupIn2016)
										xzq.DL_2016 = json2016
		
										var from2016tosd = from2sd4xzq(get4group, get4groupIn2016, arcgis)
										xzq.from2016 = from2016tosd
		
										var from2017tosd = from2sd4xzq(get4group, get4groupIn2017, arcgis)
										xzq.from2017 = from2017tosd
		
										saveJSON(xzq, xzq.XZQDM + ".json")
									}
								}, function (err) {
								})

							}
						}, function (err) {
						})

					}
				}, function (err) {
				})

				// setInterval(function () {

				// }, 60000)
				// if (counter === xList.length) {
				// 	clearInterval()
				// }
			}, function (err) {
			})
		})
		// reCallback(xList, counter)
		// xList.forEach((item, index) => {
		// 	arcgis.queryAsync(URL_TOWN, true, null, "XZQDM = '" + item.XZQDM + "'", function (res) {
		// 		var gg = []
		// 		for (var i = 0; i < res.features.length; i++) {
		// 			gg.push((res.features[i].geometry))
		// 		}
		// 		var bGeo = arcgis.union(gg)
		// 		var bArea = arcgis.planarArea(bGeo)
		// 		var obj = {
		// 			XZQDM: item.XZQDM,
		// 			XZQMC: item.XZQMC,
		// 			area: bArea * 0.0015,
		// 			geometry: bGeo
		// 		}
		// 		// item.area = bArea
		// 		XZQList.push(obj)
		// 		// if (XZQList.length === 29) {
		// 		// 	// saveJSON(XZQList, "xzqList.json")
		// 		// }
		// 		// arcgis.queryAsync(URL_SDYS, true, obj.geometry, "1=1", function (sdSet) {
		// 		// 	if (sdSet) {
		// 		// 		// var groupInsd = groupByDLBM(obj.geometry, sdSet.features, arcgis)
		// 		// 		// var get4group = return4Group(groupInsd[1], groupInsd[0], arcgis)



		// 		// 		// arcgis.queryAsync(URL_2016, true, obj.geometry, "1=1", function (pastSet) {
		// 		// 		// 	if (pastSet) {
		// 		// 		// 		var groupIn20 = groupByDLBM(obj.geometry, pastSet.features, arcgis)
		// 		// 		// 		var get4groupIn20 = return4Group(groupIn20[1], groupIn20[0], arcgis)

		// 		// 		// 		counter++
		// 		// 		// 		if (counter === xList.length) {
		// 		// 		// 			return
		// 		// 		// 		}

		// 		// 		// 	}
		// 		// 		// }, function (err) {
		// 		// 		// })
		// 		// 	}
		// 		// }, function (err) {
		// 		// })

		// 		// setInterval(function () {

		// 		// }, 60000)
		// 		// if (counter === xList.length) {
		// 		// 	clearInterval()
		// 		// }
		// 	}, function (err) {
		// 	})
		// })


	}, function (err) {
	})
}

// { //同步
// 	var tRet = query2URL(URL_TOWN, false, null, "1=1")
// 	var xzqList = []
// 	for (var i = 0; i < tRet.features.length; i++) {
// 		var obj = {}
// 		obj.XZQDM = tRet.features[i].attributes.XZQDM
// 		obj.XZQMC = tRet.features[i].attributes.XZQMC
// 		xzqList.push(obj)
// 	}

// 	var xList = unique(xzqList)

// 	xList.forEach((item, index) => {
// 		var res = query2URL(URL_TOWN, true, null, "XZQDM = '" + item.XZQDM + "'")
// 		// res.spatialReference._geVersion = {
// 		// 	Uh:null,
// 		// 	bv:null,
// 		// 	jq:"",
// 		// 	lj:-1,
// 		// 	tp:-1,
// 		// 	yg:4526
// 		// }

// 		var gg = []
// 		for (var i = 0; i < res.features.length; i++) {
// 			// res.features[i].geometry.spatialReference.wkid = 4526
// 			gg.push((res.features[i].geometry))
// 		}
// 		var bGeo = arcgis.union(gg)
// 		// geometryService.union(gg,function(r){
// 		// })
// 		var bArea = arcgis.planarArea(bGeo)
// 		var obj = {
// 			XZQDM: item.XZQDM,
// 			XZQMC: item.XZQMC,
// 			area: bArea * 0.0015,
// 			geometry: bGeo
// 		}
// 		// item.area = bArea
// 		XZQList.push(obj)
// 		// if (XZQList.length === 29) {
// 		//     // saveJSON(XZQList, "xzqList.json")
// 		// }

// 	})
// }

function reCallback(xList, counter) {
	if (counter >= xList.length) {
		return
	} else {
		// arcgis.queryAsync(URL_SDYS, true, xList[counter].geometry, "1=1", function (sdSet){
		// 	if(sdSet){
		// 	}
		// })

		counter++
		return reCallback(xList, counter)
	}

}
function getGroupJson(groupInsd) {
	var sd = {}
	var sdJson = []
	for (var i = 0; i < groupInsd[2].length; i++) {
		var objSD = {}
		objSD.DLBM = groupInsd[0][i][0].attributes.DLBM
		objSD.DLMC = groupInsd[0][i][0].attributes.DLMC
		objSD.DLCount = groupInsd[1][i].length
		objSD.DLArea = groupInsd[2][i]
		// objSD.Area = groupInsd[2][i]
		sdJson.push(objSD)
	}
	sd.arr = sdJson
	sd.area = groupInsd[3]
	// sdJson.push(groupInsd[3])
	return sd
}
function query2URL(url, boolean, geometry, where) {
	var url = url + "/query"
	var xhr = new XMLHttpRequest();
	xhr.open('post', url, false);
	// xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
	var formdata = new FormData()
	formdata.set("f", "pjson")
	formdata.set("where", where)
	formdata.set("returnGeometry", boolean)
	formdata.set("inSR", 4526)
	formdata.set("outSR", 4526)
	formdata.set("geometryType", "esriGeometryPolygon")
	formdata.set("spatialRel", "esriSpatialRelIntersects")
	formdata.set("outFields", "*")
	if (geometry) {
		formdata.set("geometry", JSON.stringify(geometry))
	}

	// if (groupBy) {
	//     formdata.set("groupByFieldsForStatistics", groupBy)
	// }

	// var formdata = {
	//     f:"pjson",
	//     where:"DLMC = '水田'",
	//     outFields:"*",
	//     returnGeometry:false
	// }
	xhr.send(formdata)
	if (xhr.status === 200) {
		return JSON.parse(xhr.response)
	} else {
	}
}
function saveJSON(data, filename) {
	if (!data) {
		alert('保存的数据为空');
		return;
	}
	if (!filename)
		filename = 'json.json'
	if (typeof data === 'object') {
		data = JSON.stringify(data, undefined, 4)
	}
	var blob = new Blob([data], { type: 'text/json' }),
		e = document.createEvent('MouseEvents'),
		a = document.createElement('a')
	a.download = filename
	a.href = window.URL.createObjectURL(blob)
	a.dataset.downloadurl = ['text/json', a.download, a.href].join(':')
	e.initMouseEvent('click', true, false, window, 0, 0, 0, 0, 0, false, false, false, false, 0, null)
	a.dispatchEvent(e)
}
function unique(list) {
	var obj = {}
	list = list.reduce(function (item, next) {
		obj[next.XZQDM] ? '' : obj[next.XZQDM] = true && item.push(next)
		return item
	}, [])
	return list
}
function ArcGISJS() {
	this.queryAsync4dl = function (url, boolean, geometry, where, callback, errback) {
		require(["esri/tasks/FeatureSet", "esri/SpatialReference", "esri/geometry/Geometry", "esri/tasks/query", "esri/tasks/QueryTask"],
			function (FeatureSet, SpatialReference, Geometry, Query, QueryTask) {
				var queryTask = new QueryTask(url)
				var para = new Query()
				para.returnGeometry = boolean
				para.outFields = ["TBBH", "DLBM", "DLMC"]
				para.where = where
				para.geometry = geometry

				// para.outStatistics = 
				// para.groupByFieldsForStatistics = 

				queryTask.execute(para, function (res) {
					if (res) {
						callback(res)
					} else {
					}
				}, function (err) {
					if (err) {
						errback(err)
					}
				})
			})
	};
	this.queryAsync = function (url, boolean, geometry, where, callback, errback) {
		require(["esri/tasks/FeatureSet", "esri/SpatialReference", "esri/geometry/Geometry", "esri/tasks/query", "esri/tasks/QueryTask"],
			function (FeatureSet, SpatialReference, Geometry, Query, QueryTask) {
				var queryTask = new QueryTask(url)
				var para = new Query()
				para.returnGeometry = boolean
				para.outFields = ["*"]
				para.where = where
				para.geometry = geometry

				// para.outStatistics = 
				// para.groupByFieldsForStatistics = 

				queryTask.execute(para, function (res) {
					if (res) {
						callback(res)
					} else {
					}
				}, function (err) {
					if (err) {
						errback(err)
					}
				})
			})
	};
	this.querySync = function (url, boolean, geometry, where) {
		require(["esri/tasks/FeatureSet", "esri/SpatialReference", "esri/geometry/Geometry", "esri/tasks/query", "esri/tasks/QueryTask"],
			function (FeatureSet, SpatialReference, Geometry, Query, QueryTask) {
				var featureSet = new FeatureSet()
				var queryTask = new QueryTask(url)
				var para = new Query()
				para.returnGeometry = boolean
				para.outFields = ["*"]
				para.where = where
				para.geometry = geometry
				// para.outSpatialReference = new SpatialReference({
				// 	wkid: 4526
				// })
				featureSet = queryTask.execute(para)
				return featureSet
			})
	};
	this.p2geometry = function (pArr) {
		var geometry
		require([
			"esri/geometry/Geometry", "esri/geometry/Extent", "esri/geometry/Polygon", "esri/SpatialReference"
		], function (
			Geometry, Extent, Polygon, SpatialReference
		) {
			if (pArr) {
				switch (pArr.length) {
					case 2:
						geometry = new Extent(pArr[0][0], pArr[0][1], pArr[1][0], pArr[1][1], new SpatialReference({
							wkid: 4326
						}));
						break;
					case 1:
						geometry = new Polygon(new SpatialReference({
							wkid: 4326
						}));
						geometry.addRing(pArr[0]);
				}
			}
		})
		return geometry;
	};
	this.planarArea = function (geometry) {
		var area
		require([
			"esri/SpatialReference", "esri/geometry/geometryEngine"
		], function (
			SpatialReference, geometryEngine
		) {
			area = geometryEngine.planarArea(geometry, "square-meters")
		})
		return Math.abs(area);
	};
	this.geodesicArea = function (geometry) {
		var area
		require([
			"esri/SpatialReference", "esri/geometry/geometryEngine"
		], function (
			SpatialReference, geometryEngine
		) {
			area = geometryEngine.geodesicArea(geometry, "square-meters")
		})
		return Math.abs(area);
	};
	this.intersect = function (geo1, geo2) {
		var getIntersectGeos
		require([
			"esri/SpatialReference", "esri/geometry/geometryEngine"
		], function (
			SpatialReference, geometryEngine
		) {
			getIntersectGeos = geometryEngine.intersect(geo1, geo2)
		})
		return getIntersectGeos;
	};
	this.union = function (geoArr) {
		var getUnionGeos
		require([
			"esri/SpatialReference", "esri/geometry/geometryEngine"
		], function (
			SpatialReference, geometryEngine
		) {
			getUnionGeos = geometryEngine.union(geoArr)
		})
		return getUnionGeos;
	};
	this.simplify = function (geo) {
		var simplifyGeo
		require([
			"esri/SpatialReference", "esri/geometry/geometryEngine"
		], function (
			SpatialReference, geometryEngine
		) {
			simplifyGeo = geometryEngine.simplify(geo)
		})
		return simplifyGeo;
	};
}
function return4Group(groups, arcgis) {

	var landType0 = [],
		landType1 = [],
		landType2 = [],
		landType3 = []
	var interType0 = [],
		interType1 = [],
		interType2 = [],
		interType3 = []
	for (var i = 0; i < groups[0].length; i++) {
		switch (groups[0][i][0].attributes.DLMC) {
			case '村庄':
				landType0.push(groups[0][i]);
				interType0.push(groups[1][i]);
				break
			case '公路用地':
				landType0.push(groups[0][i]);
				interType0.push(groups[1][i]);
				break
			case '建制镇':
				landType0.push(groups[0][i]);
				interType0.push(groups[1][i]);
				break
			case '风景名胜及特殊用地':
				landType0.push(groups[0][i]);
				interType0.push(groups[1][i]);
				break
			case '水工建筑用地':
				landType0.push(groups[0][i]);
				interType0.push(groups[1][i]);
				break
			case '采矿用地':
				landType0.push(groups[0][i]);
				interType0.push(groups[1][i]);
				break
			case '铁路用地':
				landType0.push(groups[0][i]);
				interType0.push(groups[1][i]);
				break
			case '农村宅基地':
				landType0.push(groups[0][i]);
				interType0.push(groups[1][i]);
				break
			case '特殊用地':
				landType0.push(groups[0][i]);
				interType0.push(groups[1][i]);
				break
			case '物流仓储用地':
				landType0.push(groups[0][i]);
				interType0.push(groups[1][i]);
				break
			case '商业服务业设施用地':
				landType0.push(groups[0][i]);
				interType0.push(groups[1][i]);
				break
			case '工业用地':
				landType0.push(groups[0][i]);
				interType0.push(groups[1][i]);
				break
			case '城镇住宅用地':
				landType0.push(groups[0][i]);
				interType0.push(groups[1][i]);
				break
			case '城镇村道路用地':
				landType0.push(groups[0][i]);
				interType0.push(groups[1][i]);
				break
			case '公用设施用地':
				landType0.push(groups[0][i]);
				interType0.push(groups[1][i]);
				break
			case '公园与绿地':
				landType0.push(groups[0][i]);
				interType0.push(groups[1][i]);
				break
			case '广场用地':
				landType0.push(groups[0][i]);
				interType0.push(groups[1][i]);
				break
			case '机关团体新闻出版用地':
				landType0.push(groups[0][i]);
				interType0.push(groups[1][i]);
				break
			case '科教文卫用地':
				landType0.push(groups[0][i]);
				interType0.push(groups[1][i]);
				break
			case '高教用地':
				landType0.push(groups[0][i]);
				interType0.push(groups[1][i]);
				break
			case '交通服务场站用地':
				landType0.push(groups[0][i]);
				interType0.push(groups[1][i]);
				break
			case '空闲地':
				landType0.push(groups[0][i]);
				interType0.push(groups[1][i]);
				break
			case '管道运输用地':
				landType0.push(groups[0][i]);
				interType0.push(groups[1][i]);
				break
			case '港口码头用地':
				landType0.push(groups[0][i]);
				interType0.push(groups[1][i]);
				break
			case '机场用地':
				landType0.push(groups[0][i]);
				interType0.push(groups[1][i]);
				break

			case '水田':
				landType1.push(groups[0][i]);
				interType1.push(groups[1][i]);
				break
			case '水浇地':
				landType1.push(groups[0][i]);
				interType1.push(groups[1][i]);
				break
			case '旱地':
				landType1.push(groups[0][i]);
				interType1.push(groups[1][i]);
				break

			case '果园':
				landType2.push(groups[0][i]);
				interType2.push(groups[1][i]);
				break
			case '茶园':
				landType2.push(groups[0][i]);
				interType2.push(groups[1][i]);
				break
			case '其他园地':
				landType2.push(groups[0][i]);
				interType2.push(groups[1][i]);
				break
			case '可调整果园':
				landType2.push(groups[0][i]);
				interType2.push(groups[1][i]);
				break
			case '橡胶园':
				landType2.push(groups[0][i]);
				interType2.push(groups[1][i]);
				break
			case '可调整其他园地':
				landType2.push(groups[0][i]);
				interType2.push(groups[1][i]);
				break

			case '有林地':
				landType2.push(groups[0][i]);
				interType2.push(groups[1][i]);
				break
			case '灌木林地':
				landType2.push(groups[0][i]);
				interType2.push(groups[1][i]);
				break
			case '其他林地':
				landType2.push(groups[0][i]);
				interType2.push(groups[1][i]);
				break
			case '乔木林地':
				landType2.push(groups[0][i]);
				interType2.push(groups[1][i]);
				break
			case '竹林地':
				landType2.push(groups[0][i]);
				interType2.push(groups[1][i]);
				break
			case '可调整乔木林地':
				landType2.push(groups[0][i]);
				interType2.push(groups[1][i]);
				break

			case '坑塘水面':
				landType2.push(groups[0][i]);
				interType2.push(groups[1][i]);
				break
			case '农村道路':
				landType2.push(groups[0][i]);
				interType2.push(groups[1][i]);
				break
			case '沟渠':
				landType2.push(groups[0][i]);
				interType2.push(groups[1][i]);
				break
			case '设施农用地':
				landType2.push(groups[0][i]);
				interType2.push(groups[1][i]);
				break
			case '水库水面':
				landType2.push(groups[0][i]);
				interType2.push(groups[1][i]);
				break
			case '坑塘水面':
				landType2.push(groups[0][i]);
				interType2.push(groups[1][i]);
				break
			case '人工牧草地':
				landType2.push(groups[0][i]);
				interType2.push(groups[1][i]);
				break
			case '养殖坑塘':
				landType2.push(groups[0][i]);
				interType2.push(groups[1][i]);
				break
			case '可调整养殖坑塘':
				landType2.push(groups[0][i]);
				interType2.push(groups[1][i]);
				break
			case '干渠':
				landType2.push(groups[0][i]);
				interType2.push(groups[1][i]);
				break

			case '其他草地':
				landType3.push(groups[0][i]);
				interType3.push(groups[1][i]);
				break
			case '内陆滩涂':
				landType3.push(groups[0][i]);
				interType3.push(groups[1][i]);
				break
			case '河流水面':
				landType3.push(groups[0][i]);
				interType3.push(groups[1][i]);
				break
			case '裸地':
				landType3.push(groups[0][i]);
				interType3.push(groups[1][i]);
				break
			case '裸土地':
				landType3.push(groups[0][i]);
				interType3.push(groups[1][i]);
				break
			case '裸岩石砾地':
				landType3.push(groups[0][i]);
				interType3.push(groups[1][i]);
				break
			case '湖泊水面':
				landType3.push(groups[0][i]);
				interType3.push(groups[1][i]);
				break
			default:
		}
	}

	var group = [],
		interGroup = []
	group.push(landType0)
	group.push(landType1)
	group.push(landType2)
	group.push(landType3)

	interGroup.push(interType0)
	interGroup.push(interType1)
	interGroup.push(interType2)
	interGroup.push(interType3)


	var area = [0, 0, 0, 0]
	for (var i = 0; i < interGroup.length; i++) {
		for (var j = 0; j < interGroup[i].length; j++) {
			for (var k = 0; k < interGroup[i][j].length; k++) {
				if (interGroup[i][j][k]) {
					area[i] += (arcgis.planarArea(interGroup[i][j][k], "square-meters")) * 0.0015
				} else {
					area[i] += 0
				}
			}
		}
	}
	return [group, interGroup, area]
}
function groupByDLBM(geometry, fSet, arcgis) {
	var list = fSet,
		flag = 0,
		groups = []
	for (var i = 0; i < list.length; i++) {
		var az = ''
		for (var j = 0; j < groups.length; j++) {
			if (groups[j][0].attributes.DLBM == list[i].attributes.DLBM) {
				flag = 1
				az = j
				break
			}
		}
		if (flag == 1) {
			groups[az].push(list[i])
			flag = 0
		} else if (flag == 0) {
			arr2 = new Array()
			arr2.push(list[i])
			groups.push(arr2)
		}
	}

	var interGeos = []
	// var interGeo = []
	for (var i = 0; i < groups.length; i++) {
		var yGeos = []
		for (var j = 0; j < groups[i].length; j++) {
			yGeos.push(groups[i][j].geometry)
		}
		interGeos.push(yGeos)
		// interGeo.push(arcgis.union(yGeos))
	}

	var interArr = []
	for (var i = 0; i < interGeos.length; i++) {
		var Geos = arcgis.intersect(interGeos[i], geometry)
		interArr.push(Geos)
	}

	var interA = []
	for (var i = 0; i < interArr.length; i++) {
		var interB = []
		for (var j = 0; j < interArr[i].length; j++) {
			if (interArr[i][j]) {
				interB.push(interArr[i][j])
			} else {
				interB.push(null)
			}
		}
		interA.push(interB)
	}

	var areaInGroup = []
	for (var i = 0; i < interA.length; i++) {
		var areaIn = 0
		for (var j = 0; j < interA[i].length; j++) {
			if (interA[i][j]) {
				areaIn += (arcgis.planarArea(interA[i][j])) * 0.0015
			} else {
				var m2 = 0
				areaIn += m2
			}
		}
		areaInGroup.push(areaIn)
	}
	// var geoInGroup =[]
	// var areaInGroup = []
	// for (var i = 0; i < interArr.length; i++) {
	// 	var areaIn = arcgis.union(interArr[i])
	// 	geoInGroup.push(areaIn)
	// 	areaInGroup.push(arcgis.planarArea(areaIn))
	// }

	var allArea = 0
	for (var i = 0; i < areaInGroup.length; i++) {
		allArea += areaInGroup[i]
	}

	return [groups, interA, areaInGroup, allArea];
}
function getFrom2Byxzq(union_20, _sdData, arcgis) {
	var inf = []
	for (var a = 0; a < union_20.length; a++) {
		var z1 = []
		for (var b = 0; b < _sdData[0].length; b++) {
			var z2 = []
			for (var i = 0; i < _sdData[0][b].length; i++) {
				if (_sdData[0][b][i]) {
					var g1 = arcgis.intersect(_sdData[0][b][i], union_20[a])
					if (g1) {
						var zz = arcgis.planarArea(g1, "square-meters") * 0.0015
						var obj = {}
						obj.TBBH = _sdData[1][b][i].attributes.TBBH
						obj.DLBM = _sdData[1][b][i].attributes.DLBM
						obj.DLMC = _sdData[1][b][i].attributes.DLMC
						obj.area = zz
						z2.push(obj)
					}
				}
			}
			z1.push(z2)
		}
		inf.push(z1)
	}

	var finInf = []
	for (var a = 0; a < inf.length; a++) {
		var inInf = []
		for (var b = 0; b < inf[a].length; b++) {
			var infa = []
			var flag = 0
			for (var c = 0; c < inf[a][b].length; c++) {
				var a1 = ''
				for (var d = 0; d < infa.length; d++) {
					if (infa[d][0].DLBM === inf[a][b][c].DLBM) {
						flag = 1
						a1 = d
						break
					}
				}
				if (flag == 1) {
					infa[a1].push(inf[a][b][c])
					flag = 0
				} else if (flag == 0) {
					arr = new Array()
					arr.push(inf[a][b][c])
					infa.push(arr)
				}
			}
			inInf.push(infa)
		}
		finInf.push(inInf)
	}

	return finInf
}
function getUnionInEachGroup(data, arcgis) {
	var unions = []
	for (var i = 0; i < data.length; i++) {
		var union
		if (data[i].length > 0) {
			union = arcgis.union(data[i])
		} else {
			union = null
		}
		unions.push(union)
	}
	return unions
}
function from2sd4xzq(sd, _2016, arcgis) {

	var from2 = []

	for (var i = 0; i < _2016[1].length; i++) {
		var z1 = []
		if (_2016[1][i]) {
			for (var i2 = 0; i2 < sd[1].length; i2++) {
				var z2 = []
				if (sd[1][i2]) {

					for (var j = 0; j < _2016[1][i].length; j++) {
						if (_2016[1][i][j]) {
							for (var j2 = 0; j2 < sd[1][i2].length; j2++) {
								if (sd[1][i2][j2]) {
									var z3 = 0
									for (var k = 0; k < _2016[1][i][j].length; k++) {
										if (_2016[1][i][j][k]) {
											for (var k2 = 0; k2 < sd[1][i2][j2].length; k2++) {
												if (sd[1][i2][j2][k2]) {
													var g1 = arcgis.intersect(sd[1][i2][j2][k2], _2016[1][i][j][k])
													if (g1) {
														z3 += arcgis.planarArea(g1, "square-meters") * 0.0015
													} else {
														z3 += 0
													}
												}
											}
										}
									}
									var obj = {}
									// obj.TBBH = sd[0][m][n][0].attributes.TBBH
									obj.DLBM = sd[0][i2][j2][0].attributes.DLBM
									obj.DLMC = sd[0][i2][j2][0].attributes.DLMC
									obj.area = z3
									z2.push(obj)
								}
							}
						}
					}
				}
				var fruitTotal = [];

				var obj4U = {};
				z2.forEach(item => {
					obj4U[item.DLBM] = obj4U[item.DLBM] || [];
					obj4U[item.DLBM].push(item);
				});

				var arr2 = Object.keys(obj4U);
				arr2.forEach(nameItem => {
					var count = 0
					obj4U[nameItem].forEach(item => {
						count += item.area
					});
					fruitTotal.push({ 'DLBM': nameItem, 'DLMC': obj4U[nameItem][0].DLMC, 'area': count })
				});

				z1.push(fruitTotal)
			}
			from2.push(z1)
		}

	}
	return from2
}