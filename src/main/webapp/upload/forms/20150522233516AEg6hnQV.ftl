<html
xmlns:m="http://schemas.microsoft.com/office/2004/12/omml">





<script type="text/javascript" src="static/jquery/jquery-1.8.3.js"></script><script type="text/javascript" src="static/tools/dataformat.js"></script><link id="easyuiTheme" rel="stylesheet" href="static/easyui/themes/default/easyui.css" type="text/css"></link><link rel="stylesheet" href="static/easyui/themes/icon.css" type="text/css"></link><link rel="stylesheet" type="text/css" href="static/accordion/css/accordion.css"></link><script type="text/javascript" src="static/easyui/jquery.easyui.min.1.3.2.js"></script><script type="text/javascript" src="static/easyui/locale/zh-cn.js"></script><script type="text/javascript" src="static/tools/syUtil.js"></script><script type="text/javascript" src="static/My97DatePicker/WdatePicker.js"></script><script type="text/javascript" src="static/lhgDialog/lhgdialog.min.js"></script><script type="text/javascript">$(function(){$("#formobj").Validform({tiptype:4,btnSubmit:"#btn_sub",btnReset:"#btn_reset",ajaxPost:true,usePlugin:{passwordstrength:{minLen:6,maxLen:18,trigger:function(obj,error){if(error){obj.parent().next().find(".Validform_checktip").show();obj.find(".passwordStrength").hide();}else{$(".passwordStrength").show();obj.parent().next().find(".Validform_checktip").hide();}}}},callback:function(data){if(data.success==true){if(!neibuClickFlag){var win = frameElement.api.opener;frameElement.api.close();win.tip(data.msg);win.reloadTable();}else {alert(data.msg)}}else{if(data.responseText==''||data.responseText==undefined)$("#formobj").html(data.msg);else $("#formobj").html(data.responseText); return false;}if(!neibuClickFlag){var win = frameElement.api.opener;win.reloadTable();}}});});</script><script type="text/javascript" src="static/tools/curdtools_zh-cn.js"></script><script type="text/javascript" src="static/tools/easyuiextend.js"></script><link rel="stylesheet" href="static/Validform/css/style.css" type="text/css"/><link rel="stylesheet" href="static/Validform/css/tablefrom.css" type="text/css"/><script type="text/javascript" src="static/Validform/js/Validform_v5.3.1_min_zh-cn.js"></script><script type="text/javascript" src="static/Validform/js/Validform_Datatype_zh-cn.js"></script><script type="text/javascript" src="static/Validform/js/datatype_zh-cn.js"></script><script type="text/javascript" src="static/Validform/plugin/passwordStrength/passwordStrength-min.js"></script><style>body{font-size:12px;}table{border: 1px solid #000000;padding:0; margin:0 auto;border-collapse: collapse;width:100%;align:right;}td {border: 1px solid #000000;background: #fff;font-size:12px;padding: 3px 3px 3px 8px;color: #000000;word-break: keep-all;}</style>
<body  >

<div  >

<p  align=center >��ٵ�</p>

<form action="cgFormBuildController.do?saveOrUpdate" id="formobj" name="formobj" method="post">
<input type="hidden" name="tableName" value="${tableName?if_exists?html}"/>
<input type="hidden" name="id" value="${id?if_exists?html}"/>
<input type="hidden" id="btn_sub" class="btn_sub"/>
#{jform_hidden_field}<table  border=1 cellspacing=0 cellpadding=0
 >
 <tr >
  <td width=142  >
  <p  >��ٱ���</p>
  </td>
  <td width=142  >
  <p  >#{title}</p>
  </td>
  <td width=142  >
  <p  >��ٿ�ʼʱ��</p>
  </td>
  <td width=142  >
  <p  >#{begindate}</p>
  </td>
 </tr>
 <tr >
  <td width=142  >
  <p  >�����</p>
  </td>
  <td width=142  >
  <p  >#{people}</p>
  </td>
  <td width=142  >
  <p  >��ٽ���ʱ��</p>
  </td>
  <td width=142  >
  <p  >#{enddate}</p>
  </td>
 </tr>
 <tr >
  <td width=142  >
  <p  >�Ա�</p>
  </td>
  <td width=142  >
  <p  >#{sex}</p>
  </td>
  <td width=142  >
  <p  >��������</p>
  </td>
  <td width=142  >
  <p  >#{hol_dept}</p>
  </td>
 </tr>
 <tr >
  <td width=142  >
  <p  >���ԭ��</p>
  </td>
  <td width=426 colspan=3  >
  <p  >#{hol_reson}</p>
  </td>
 </tr>
 <tr >
  <td width=142  >
  <p  >����������</p>
  </td>
  <td width=426 colspan=3  >
  <p  >#{dep_leader}</p>
  </td>
 </tr>
 <tr >
  <td width=142  >
  <p  >�����������</p>
  </td>
  <td width=426 colspan=3  >
  <p  >#{content}</p>
  </td>
 </tr>
</table>
</form>

<p ></p>

</div>

</body>

</html>
