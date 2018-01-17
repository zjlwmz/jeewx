package cn.emay.framework.core.common.dao;


import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import cn.emay.framework.core.common.model.common.UploadFile;
import cn.emay.framework.core.common.model.json.ComboTree;
import cn.emay.framework.core.common.model.json.ImportFile;
import cn.emay.framework.core.common.model.json.TreeGrid;
import cn.emay.framework.core.extend.template.Template;
import cn.emay.framework.tag.vo.easyui.ComboTreeModel;
import cn.emay.framework.tag.vo.easyui.TreeGridModel;
import cn.emay.modules.sys.entity.Depart;
import cn.emay.modules.sys.entity.User;

@SuppressWarnings("rawtypes")
public interface ICommonDao extends IGenericBaseCommonDao{
	
	
	/**
	 * admin账户密码初始化
	 * @param user
	 */
	public void pwdInit(User user,String newPwd);
	/**
	 * 检查用户是否存在
	 * */
	public User getUserByUserIdAndUserNameExits(User user);
	public String getUserRole(User user);
	/**
	 * 文件上传
	 * @param request
	 */
	public <T> T  uploadFile(UploadFile uploadFile);
	/**
	 * 文件上传或预览
	 * @param uploadFile
	 * @return
	 */
	public HttpServletResponse viewOrDownloadFile(UploadFile uploadFile);

	public Map<Object,Object> getDataSourceMap(Template template);
	/**
	 * 生成XML文件
	 * @param fileName XML全路径
	 */
	public HttpServletResponse createXml(ImportFile importFile);
	/**
	 * 解析XML文件
	 * @param fileName XML全路径
	 */
	public void parserXml(String fileName);
	public List<ComboTree> comTree(List<Depart> all,ComboTree comboTree);

	/**
     * 根据模型生成ComboTree JSON
     *
     * @param all 全部对象
     * @param comboTreeModel 模型
     * @param in 已拥有的对象
     * @param recursive 是否递归加载所有子节点
     * @return List<ComboTree>
     */
	public  List<ComboTree> ComboTree(List all, ComboTreeModel comboTreeModel, List in, boolean recursive);

	public  List<TreeGrid> treegrid(List all,TreeGridModel treeGridModel);
}

