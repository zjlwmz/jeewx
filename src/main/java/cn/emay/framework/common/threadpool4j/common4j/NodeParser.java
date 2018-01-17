package cn.emay.framework.common.threadpool4j.common4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class NodeParser
{
  private Node _node;
  private Map<String, String> _attrMap;
  private List<Node> _childNodes;

  public NodeParser(Node node)
  {
    this._node = node;
  }

  public String getAttributeValue(String attrName)
  {
    initAttrMap();
    return (String)this._attrMap.get(attrName);
  }

  public int getAttributeCount()
  {
    initAttrMap();
    return this._attrMap.size();
  }

  private void initAttrMap() {
    if (null != this._attrMap) {
      return;
    }

    this._attrMap = new HashMap<String,String>();
    NamedNodeMap nodeMap = this._node.getAttributes();
    if (null == nodeMap) {
      return;
    }
    for (int i = 0; i < nodeMap.getLength(); ++i) {
      Node attr = nodeMap.item(i);
      this._attrMap.put(attr.getNodeName(), attr.getNodeValue());
    }
  }

  public List<Node> getChildNodes()
  {
    initChildNodeList();
    return this._childNodes;
  }

  public int getChildNodeCount()
  {
    initChildNodeList();
    return this._childNodes.size();
  }

  public Node getChildNode(String nodeName)
  {
    if (null == nodeName) {
      return null;
    }

    initChildNodeList();
    for (Node node : this._childNodes) {
      if (nodeName.equals(node.getNodeName())) {
        return node;
      }
    }

    return null;
  }

  public String getChildNodeValue(String nodeName)
  {
    Node node = getChildNode(nodeName);
    if (null == node) {
      return null;
    }

    return node.getTextContent();
  }

  private void initChildNodeList() {
    if (null != this._childNodes) {
      return;
    }

    this._childNodes = new ArrayList<Node>();
    NodeList nodeList = this._node.getChildNodes();
    for (int i = 0; i < nodeList.getLength(); ++i) {
      Node node = nodeList.item(i);
      if (1 != node.getNodeType()) {
        continue;
      }
      this._childNodes.add(node);
    }
  }
}