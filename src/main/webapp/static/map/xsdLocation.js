var map = new OpenLayers.Map(
		{
			div : "allmap",
			projection : "EPSG:900913",
			displayProjection : new OpenLayers.Projection("EPSG:4326"),
			controls : [ new OpenLayers.Control.PanZoom(),
					new OpenLayers.Control.Navigation({
						dragPanOptions : {
							enableKinetic : true
						}
					}), new OpenLayers.Control.LayerSwitcher(),
					new OpenLayers.Control.MousePosition(),
					new OpenLayers.Control.TouchNavigation({
						dragPanOptions : {
							enableKinetic : true
						}
					}) ],
			layers : [
					new OpenLayers.Layer.XYZ(
							"arcgis矢量地图",
							ioc.arcgisMapUrl,
							{
								isBaseLayer : true,
								transitionEffect : "resize"
							}),
					new OpenLayers.Layer.XYZ(
							"google影像地图",
							ioc.googleMapImageUrl,
							{
								isBaseLayer : true,
								transitionEffect : "resize",
								maxResolution : 156543.03390625,
								numZoomLevels : 20
							}),
					new OpenLayers.Layer.XYZ(
							"google矢量地图",
							ioc.googleMapBaseUrl,
							{
								isBaseLayer : true,
								transitionEffect : "resize",
								maxResolution : 156543.03390625,
								numZoomLevels : 20
							}) 
					],
			center : [ 0, 0 ],
			zoom : 1
		});

map.addControl(new OpenLayers.Control.LayerSwitcher());

var tiled = new OpenLayers.Layer.WMS("新时代微信位置20160326",
		"http://localhost:8787/geoserver/zhangjialu/wms", {
			LAYERS : 'zhangjialu:wx_location_20160326',
			STYLES : '',
			format : 'image/png',
			TRANSPARENT : true,
			tiled : true,
			sphericalMercator : true,
			tilesOrigin : map.maxExtent.left + ',' + map.maxExtent.bottom
		}, {
			buffer : 0,
			displayOutsideMaxExtent : true,
			isBaseLayer : false
		});

// map.addLayer(tiled);
var tiled1 = new OpenLayers.Layer.WMS("宁波轨迹201210_bd",
		"http://localhost:8787/geoserver/zhangjialu/wms", {
			LAYERS : 'zhangjialu:user_track_201210',
			STYLES : '',
			format : 'image/png',
			TRANSPARENT : true,
			tiled : true,
			sphericalMercator : true,
			tilesOrigin : map.maxExtent.left + ',' + map.maxExtent.bottom
		}, {
			buffer : 0,
			displayOutsideMaxExtent : true,
			isBaseLayer : false
		});
// map.addLayer(tiled1);
