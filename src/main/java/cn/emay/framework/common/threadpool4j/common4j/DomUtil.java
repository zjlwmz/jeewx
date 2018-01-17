package cn.emay.framework.common.threadpool4j.common4j;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;

public class DomUtil
{
  private static Logger _logger = Logger.getLogger(DomUtil.class);

  public static Document createDocument(String classPathXmlFile)
  {
    DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
    Document document = null;
    try {
      DocumentBuilder dom = domFactory.newDocumentBuilder();
      document = dom.parse(DomUtil.class.getResourceAsStream(classPathXmlFile));
    } catch (Exception e) {
      _logger.error(String.format("create Document of xml file[%s] occurs error", new Object[] { classPathXmlFile }), e);
    }

    return document;
  }
}