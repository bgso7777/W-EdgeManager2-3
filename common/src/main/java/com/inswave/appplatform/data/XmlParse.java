package com.inswave.appplatform.data;

import java.io.StringReader;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class XmlParse {

    public String parseShinhanbankLoginXml(String xml, String key) {
        String value = "";
        try {
            Document document = convertStringToXMLDocument(xml);
            Element element = document.getDocumentElement();
            NodeList nodeList = element.getChildNodes();
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    if(key.equals(node.getNodeName())) {
                        Element iElement = (Element) node;
                        if(iElement!=null && iElement.getFirstChild()!=null && iElement.getFirstChild().getNodeValue()!=null ) {
                            value = iElement.getFirstChild().getNodeValue();
                            break;
                        }
                    }
                }
            }
        } catch(Exception e) {
          e.printStackTrace();
        }
        return value;
    }

    private static Document convertStringToXMLDocument(String xmlString) {

        //Parser that produces DOM object trees from XML content
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        //API to obtain DOM Document instance
        DocumentBuilder builder = null;
        try {
            //Create DocumentBuilder with default configuration
            builder = factory.newDocumentBuilder();

            //Parse the content to Document object
            Document doc = builder.parse(new InputSource(new StringReader(xmlString)));
            return doc;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String argv[]) {

        StringBuffer xml = new StringBuffer("");
        xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        xml.append("<sso>");
        xml.append("    <result><![CDATA[0]]></result>");
        xml.append("    <errMSG><![CDATA[abce]]></errMSG>");
        xml.append("    <errCNT><![CDATA[0]]></errCNT>");
        xml.append("</sso>");

        XmlParse xmlParse = new XmlParse();
        System.out.println("result-->>"+xmlParse.parseShinhanbankLoginXml(xml.toString(), "result"));
        System.out.println("errMSG-->>"+xmlParse.parseShinhanbankLoginXml(xml.toString(), "errMSG"));
        System.out.println("errCNT-->>"+xmlParse.parseShinhanbankLoginXml(xml.toString(), "errCNT"));
    }
}