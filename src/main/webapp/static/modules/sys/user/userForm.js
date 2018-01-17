function openDepartmentSelect() {
	$.dialog.setting.zIndex = 9999;

	var orgIds = $("#orgIds").val();
	var confirmName=$("#common_confirm").val();
	var cancelName=$("#common_cancel").val();
	$.dialog({
		content : 'url:departController.do?departSelect&orgIds=' + orgIds,
		zIndex : 2100,
		title : '组织机构列表',
		lock : true,
		width : '400px',
		height : '350px',
		opacity : 0.4,
		button : [ {
			name : confirmName,
			callback : callbackDepartmentSelect,
			focus : true
		}, {
			name : cancelName,
			callback : function() {
			}
		} ]
	}).zindex();
}

function callbackDepartmentSelect() {
	var iframe = this.iframe.contentWindow;
	var treeObj = iframe.$.fn.zTree.getZTreeObj("departSelect");
	var nodes = treeObj.getCheckedNodes(true);
	if (nodes.length > 0) {
		var ids = '', names = '';
		for (i = 0; i < nodes.length; i++) {
			var node = nodes[i];
			ids += node.id + ',';
			names += node.name + ',';
		}
		$('#departname').val(names);
		$('#departname').blur();
		$('#orgIds').val(ids);
	}
}

function callbackClean() {
	$('#departname').val('');
	$('#orgIds').val('');
}

function setOrgIds() {
}
$(function() {
	$("#departname").prev().hide();
});





$(document).ready(function(){
	
	var uploadButton = $('<button/>')
	.addClass('btn btn-primary')
	.prop('disabled', true)
	.text('上传中...')
	.on('click', function () {
	    var $this = $(this), data = $this.data();
	    $this.off('click').text('正在上传...').on('click', function () {
	            $this.remove();
	            data.abort();
	    });
	    data.submit().always(function () {
	        $this.remove();
	    });
	});
	//文件上传
	$('#fileupload')
			.fileupload(
					{
						url : "imageController.do?upload",
						dataType : 'json',
						autoUpload : false,
						acceptFileTypes : /(\.|\/)(gif|jpe?g|png)$/i,
						maxFileSize : 2000000, // 2 MB
						disableImageResize : /Android(?!.*Chrome)|Opera/
								.test(window.navigator.userAgent),
						previewMaxWidth : 290,
						previewMaxHeight : 160,
						previewCrop : true
					})
			.on(
					'fileuploadadd',
					function(e, data) {
						$("#files").text("");
						data.context = $('<div/>').appendTo('#files');
						$.each(data.files, function(index, file) {
							// var node =
							// $('<p/>').append($('<span/>').text(file.name));
							// fileupload
							var node = $('<p/>');
							if (!index) {
								node.append('<br>').append(
										uploadButton.clone(true).data(data));
							}
							node.appendTo(data.context);
						});
					})
			.on(
					'fileuploadprocessalways',
					function(e, data) {
						var index = data.index, file = data.files[index], node = $(data.context
								.children()[index]);
						if (file.preview) {
							node.prepend('<br>').prepend(file.preview);
						}
						if (file.error) {
							node.append('<br>').append(
									$('<span class="text-danger"/>').text(
											file.error));
						}
						if (index + 1 === data.files.length) {
							data.context.find('button').text('上传').prop('disabled',
									!!data.files.error);
						}
					}).on('fileuploadprogressall', function(e, data) {
				var progress = parseInt(data.loaded / data.total * 100, 10);
				$('#progress .progress-bar').css('width', progress + '%');
			}).on(
					'fileuploaddone',
					function(e, data) {
						console.info(data);
						var file = data.files[0];
						// var delUrl = "<a class=\"js_removeCover\"
						// onclick=\"return false;\"
						// href=\"javascript:void(0);\">删除</a>";
						$("#imgName").text("").append(file.name);
						$("#progress").hide();
						var d = data.result;
						if (d.success) {
							var link = $('<a>').attr('target', '_blank').prop(
									'href', d.attributes.viewhref);
							$(data.context.children()[0]).wrap(link);
							console.info(d.attributes.viewhref);
							$("#imagePath").val(d.attributes.url);
						} else {
							var error = $('<span class="text-danger"/>')
									.text(d.msg);
							$(data.context.children()[0]).append('<br>').append(
									error);
						}
					}).on(
					'fileuploadfail',
					function(e, data) {
						$.each(data.files, function(index, file) {
							var error = $('<span class="text-danger"/>').text(
									'File upload failed.');
							$(data.context.children()[index]).append('<br>')
									.append(error);
						});
					}).prop('disabled', !$.support.fileInput).parent().addClass(
					$.support.fileInput ? undefined : 'disabled');
});

