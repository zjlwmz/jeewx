package cn.emay.framework.common.utils;

import java.util.List;
import java.util.Map;

import cn.emay.modules.sys.entity.Function;
import cn.emay.modules.sys.service.MutiLangService;


/**
 * 动态菜单栏生成 工具类
 * @author lenovo
 *
 */
public class MenuUtils {

	private static MutiLangService mutiLangService;
	
	/**
	 * 拼装webos头部菜单
	 * @param pFunctions
	 * @param functions
	 * @return
	 */
	public static String getWebosMenu(Map<Integer, List<Function>> map) {
		StringBuffer menuString = new StringBuffer();
		StringBuffer DeskpanelString = new StringBuffer();
		StringBuffer dataString = new StringBuffer();
		String menu = "";
		String desk = "";
		String data = "";
		
		//menu的全部json，这里包括对菜单的展示及每个二级菜单的点击出详情
//		menuString.append("[");
		menuString.append("{");
		//绘制data.js数组，用于替换data.js中的app:{//桌面1 'dtbd':{ appid:'2534',,······
		dataString.append("{app:{");
		//绘制Deskpanel数组，用于替换webos-core.js中的Icon1:['dtbd','sosomap','jinshan'],······
		DeskpanelString.append("{");
		
		List<Function> pFunctions = (List<Function>) map.get(0);
		if(pFunctions==null || pFunctions.size()==0){
			return "";
		}
		int n = 1;
		for (Function pFunction : pFunctions) {
			//是否有子菜单
			boolean hasSub = pFunction.hasSubFunction(map);
			//绘制一级菜单
//			menuString.append("{ ");
			menuString.append("\""+ pFunction.getId() + "\":");
			menuString.append("{\"id\":\""+pFunction.getId()+"\",\"name\":\""+pFunction.getFunctionName()
					+"\",\"path\":\""+ResourceUtil.allTSIcons.get(pFunction.getIcon().getId()).getIconPath()+"\",\"level\":\""+pFunction.getFunctionLevel()+"\",");
			menuString.append("\"child\":{");

			//绘制Deskpanel数组
			DeskpanelString.append("Icon"+n+":[");
			
			//绘制二级菜单
			if(hasSub){
//				menuString.append(getWebosChild(pFunction, 1, map));
				DeskpanelString.append(getWebosDeskpanelChild(pFunction, 1, map));
				dataString.append(getWebosDataChild(pFunction, 1, map));
			}
			DeskpanelString.append("],");
			menuString.append("}},");
			n++;
		}

		menu = menuString.substring(0, menuString.toString().length()-1);
//		menu += "]";
		menu += "}";
		
		data = dataString.substring(0, dataString.toString().length()-1);
		data += "}}";
		
		desk = DeskpanelString.substring(0, DeskpanelString.toString().length()-1);
		desk += "}";
		
		//初始化为1，需减少一个个数。
		n = n-1;
		
//		System.out.println("-------------------");
//		System.out.println(menu+"$$"+desk+"$$"+data+"$$"+"{\"total\":"+n+"}");
		return menu+"$$"+desk+"$$"+data+"$$"+n;
	}
	
	
	private static String getWebosDeskpanelChild(Function parent,int level,Map<Integer, List<Function>> map){
		StringBuffer DeskpanelString = new StringBuffer();
		String desk = "";
		List<Function> list = map.get(level);
		if(list==null || list.size()==0){
			return "";
		}
		for (Function function : list) {
			
			if (function.getParentFunction().getId().equals(parent.getId())){
				DeskpanelString.append("'"+function.getId()+"',");
			}
		}
		desk = DeskpanelString.substring(0, DeskpanelString.toString().length()-1);
		return desk;
	}
	
	
	private static String getWebosDataChild(Function parent,int level,Map<Integer, List<Function>> map){
		StringBuffer dataString = new StringBuffer();
		String data = "";
		List<Function> list = map.get(level);
		if(list==null || list.size()==0){
			return "";
		}
		for (Function function : list) {
			if (function.getParentFunction().getId().equals(parent.getId())){
				dataString.append("'"+function.getId()+"':{ ");
				dataString.append("appid:'"+function.getId()+"',");
				dataString.append("url:'"+function.getFunctionUrl()+"',");

//				dataString.append(getIconandName(function.getFunctionName()));
				dataString.append(getIconAndNameForDesk(function));

				dataString.append("asc :"+function.getFunctionOrder());
				dataString.append(" },");
			}
		}
//		data = dataString.substring(0, dataString.toString().length()-1);
		data = dataString.toString();
		return data;
	}
	
	
	private static String getIconAndNameForDesk(Function function) {
        StringBuffer dataString = new StringBuffer();

        String colName = function.getIconDesk() == null ? null : function.getIconDesk().getIconPath();
        colName = (colName == null || colName.equals("")) ? "static/sliding/icon/default.png" : colName;
        String functionName = getMutiLang(function.getFunctionName());

        dataString.append("icon:'" + colName + "',");
        dataString.append("name:'"+functionName+"',");

        return dataString.toString();
    }
	
	
	/**
	*  @Title: getMutiLang
	*  @Description: 转换菜单多语言
	*  @param functionName
	* @return String    
	* @throws
	 */
	private static String getMutiLang(String functionName){
		//add by Rocky, 处理多语言
		if(mutiLangService == null)
		{
			mutiLangService = SpringContextHolder.getApplicationContext().getBean(MutiLangService.class);	
		}
		
		String lang_context = mutiLangService.getLang(functionName);
		return lang_context;
	}
	
	
	/**
	 * 拼装easyui菜单JSON方式
	 * 
	 * @param set
	 * @param set1
	 * @return
	 */
	public static String getMenu(List<Function> set, List<Function> set1) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("{\"menus\":[");
		for (Function node : set) {
			String iconClas = "default";// 权限图标样式
			if (node.getIcon() != null) {
				iconClas = ResourceUtil.allTSIcons.get(node.getIcon().getId()).getIconClas();
			}
			buffer.append("{\"menuid\":\"" + node.getId() + "\",\"icon\":\""
					+ iconClas + "\"," + "\"menuname\":\""
			+ getMutiLang(node.getFunctionName()) + "\",\"menus\":[");
			iterGet(set1, node.getId(), buffer);
			buffer.append("]},");
		}
		buffer.append("]}");

		// 将,\n]替换成\n]

		String tmp = buffer.toString();

		tmp = tmp.replaceAll(",\n]", "\n]");
		tmp = tmp.replaceAll(",]}", "]}");
		return tmp;

	}
	
	static int count = 0;
	
	/**
	 * @param args
	 */

	static void iterGet(List<Function> set1, String pid, StringBuffer buffer) {

		for (Function node : set1) {

			// 查找所有父节点为pid的所有对象，然后拼接为json格式的数据
			count++;
			if (node.getParentFunction().getId().equals(pid))

			{
				buffer.append("{\"menuid\":\"" + node.getId()
						+ " \",\"icon\":\"" + ResourceUtil.allTSIcons.get(node.getIcon().getId()).getIconClas()
						+ "\"," + "\"menuname\":\"" + getMutiLang(node.getFunctionName())
						+ "\",\"url\":\"" + node.getFunctionUrl() + "\"");
				if (count == set1.size()) {
					buffer.append("}\n");
				}
				buffer.append("},\n");

			}
		}

	}
}
