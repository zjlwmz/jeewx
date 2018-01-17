<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/context/mytags.jsp"%>
<!DOCTYPE html>
<html>
  <head>
   <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta name="viewport" content="initial-scale=1.0, user-scalable=no" />
    <title>新时代地图</title>
    <style type="text/css">
        body, html,#allmap {width: 100%;height: 100%;overflow: hidden;margin:0;font-family:"微软雅黑";}
    </style>
</head>
<body>
    <div id="allmap" class="smallmap"></div>
    <script type="text/javascript" src="${ webRoot}/static/map/config.js"></script>
    <script type="text/javascript" src="${ webRoot}/static/OpenLayers-2.12/OpenLayers.js"></script>
   	<script type="text/javascript" src="${ webRoot}/static/map/xsdLocation.js"></script>
</body>