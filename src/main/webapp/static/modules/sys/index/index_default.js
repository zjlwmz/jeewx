
/**
 *  经典风格
 */


$(function() {
	
		var commonCodeName=$("#common_code").val();
		var commonLoginName=$("#common_login_name").val();
		var commonLoginTime=$("#common_login_time").val();
		var langUserOnline=$("#lang_user_online").val();
	
		$('#layout_jeecg_onlineDatagrid').datagrid({
			url : 'systemController.do?datagridOnline&field=ip,logindatetime,user.userName,',
			title : '',
			iconCls : '',
			fit : true,
			fitColumns : true,
			pagination : true,
			pageSize : 10,
			pageList : [ 10 ],
			nowarp : false,
			border : false,
			idField : 'id',
			sortName : 'logindatetime',
			sortOrder : 'desc',
			frozenColumns : [ [ {
				title : commonCodeName,
				field : 'id',
				width : 150,
				hidden : true
			} ] ],
			columns : [ [ {
				title : commonLoginName,
				field : 'user.userName',
				width : 100,
				align : 'center',
				sortable : true,
				formatter : function(value, rowData, rowIndex) {
					return formatString('<span title="{0}">{1}</span>', value, value);
				}
			}, {
				title : 'IP',
				field : 'ip',
				width : 150,
				align : 'center',
				sortable : true,
				formatter : function(value, rowData, rowIndex) {
					return formatString('<span title="{0}">{1}</span>', value, value);
				}
			}, {
				title : commonLoginTime,
				field : 'logindatetime',
				width : 150,
				sortable : true,
				formatter : function(value, rowData, rowIndex) {
					return formatString('<span title="{0}">{1}</span>', value, value);
				},
				hidden : true
			} ] ],
			onClickRow : function(rowIndex, rowData) {
			},
			onLoadSuccess : function(data) {
				$('#layout_jeecg_onlinePanel').panel('setTitle', '( ' + data.total + ' )' + langUserOnline);
			},
			onLoadError : function(data) {
			}
		}).datagrid('getPager').pagination({
			showPageList : false,
			showRefresh : false,
			beforePageText : '',
			afterPageText : '/{pages}',
			displayMsg : ''
		});		
		
		$('#layout_jeecg_onlinePanel').panel({
			tools : [ {
				iconCls : 'icon-reload',
				handler : function() {
					$('#layout_jeecg_onlineDatagrid').datagrid('load', {});
				}
			} ]
		});
		
		$('#layout_east_calendar').calendar({
			fit : true,
			current : new Date(),
			border : false,
			onSelect : function(date) {
				$(this).calendar('moveTo', new Date());
			}
		});
		$(".layout-expand").click(function(){
			$('#layout_east_calendar').css("width","auto");
			$('#layout_east_calendar').parent().css("width","auto");
			$("#layout_jeecg_onlinePanel").find(".datagrid-view").css("max-height","200px");
			$("#layout_jeecg_onlinePanel .datagrid-view .datagrid-view2 .datagrid-body").css("max-height","180px").css("overflow-y","auto");
		});
	});
	var onlineInterval;
	
	function easyPanelCollapase(){
		window.clearTimeout(onlineInterval);
	}
	function easyPanelExpand(){
		onlineInterval = window.setInterval(function() {
			$('#layout_jeecg_onlineDatagrid').datagrid('load', {});
		}, 1000 * 20);
	}