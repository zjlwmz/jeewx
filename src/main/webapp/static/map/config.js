var ioc={
	arcgisMapUrl:["http://www.arcgisonline.cn/ArcGIS/rest/services/ChinaOnlineCommunity/MapServer/tile/${z}/${y}/${x}"],
	googleMapImageUrl:["http://localhost:8080/TileService/ImageryService/getTile.nut?x=${x}&y=${y}&z=${z}"],
	googleMapBaseUrl:["http://localhost:8080/TileService/MapService/getTile.nut?x=${x}&y=${y}&z=${z}"],

	baseWms:"http://localhost:8787/geoserver/zhangjialu/wms"
}