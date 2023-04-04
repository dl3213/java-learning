console.log("noding.......")
var fs = require('fs')
var esriLoader = require('esri-loader')
console.log(esriLoader)
// var arcgis = new ArcGISJS();
const URL_XZQ = ""
const URL_TOWN = ""
const URL_VILLAGE = ""
const URL_2016 = ""
const URL_2017 = ""
// const URL_SDYS = ""
const URL_SDYS = ""

var dlNum = ["建设用地", "耕地", "其它农用地", "未利用地"]
var xzqList = []

esriLoader.setDefaultOptions({ version: '3.30' })

esriLoader.loadModules(["esri/SpatialReference",
	"esri/geometry/Geometry",
	"esri/tasks/query", "esri/tasks/QueryTask"])
	.then(([SpatialReference,
		Geometry,
		Query, QueryTask]) => {
		
			var queryTask = new QueryTask(URL_TOWN)
			var para = new Query()
			para.returnGeometry = false
			para.outFields = ["*"]
			para.where = "1=1"
			para.geometry = null
			queryTask.execute(para, function (res) {
				if (res) {
					// callback(res)
					console.log(res)
				} else {
					console.log("no res back")
				}
			})
	})
	.catch(err => {
		// handle any errors
		console.error(err);
	});

// query.queryFeatures({
// 	url: URL_TOWN,
// 	where: "1=1"
// },function(res){
// 	console.log(res)
// })

// arcgis.query(URL_TOWN, false, null, "1=1", function (res) {
// 	console.log(res)
// 	for (var i = 0; i < res.features.length; i++) {
// 		var obj = {}
// 		obj.XZQDM = res.features[i].attributes.XZQDM
// 		obj.XZQMC = res.features[i].attributes.XZQMC
// 		obj.JSMJ = res.features[i].attributes.JSMJ
// 		xzqList.push(obj)
// 	}
// 	console.log(xzqList)
// 	if (xzqList) {
// 		fs.writeFile('./4xzq/data/xzqList.json', JSON.stringify(xzqList), function (err) {
// 			if (err) {
// 				console.log(err)
// 			} else {
// 				console.log("ok")
// 			}
// 		})
// 	}
// })
function ArcGISJS() {
	this.query = function (url, boolean, geometry, where, callback) {
		require([
			"esri/SpatialReference",
			"esri/geometry/Geometry",
			"esri/tasks/query", "esri/tasks/QueryTask"
		], function (
			SpatialReference,
			Geometry,
			Query, QueryTask
		) {
			var queryTask = new QueryTask(url)
			var para = new Query()
			para.returnGeometry = boolean
			para.outFields = ["*"]
			para.where = where
			para.geometry = geometry
			queryTask.execute(para, function (res) {
				if (res) {
					callback(res)
				} else {
					console.log("no res back")
				}
			})
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
	// this.simplify = function(geo) {
	// 	var simplifyGeo
	// 	require([
	// 		"esri/SpatialReference", "esri/geometry/geometryEngine"
	// 	], function(
	// 		SpatialReference, geometryEngine
	// 	) {
	// 		simplifyGeo = geometryEngine.simplify(geo)
	// 	})
	// 	// console.log(area)
	// 	return simplifyGeo;
	// };
}

function writeFile() {

}