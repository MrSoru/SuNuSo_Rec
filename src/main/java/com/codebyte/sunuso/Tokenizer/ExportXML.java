package com.codebyte.sunuso.Tokenizer;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class ExportXML {
    public static boolean FillXML(File Archivo, ArrayList<ObjectAttrib> Data1, ArrayList<ObjectAttrib> Data2) {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.newDocument();
            doc.setXmlVersion("1.1");
            Element DB = doc.createElement("DataBase");
            doc.appendChild(DB);
            Element elem = doc.createElement(((ObjectAttrib)Data1.get(1)).getName());
            DB.appendChild(elem);
            for (ObjectAttrib objAtt : Data1) {
                Element single = doc.createElement(objAtt.getTittle());
                Attr atrib = doc.createAttribute("Value");
                atrib.setValue(objAtt.getValue());
                single.setAttributeNode(atrib);
                elem.appendChild(single);
            }
            Element elem2 = doc.createElement(((ObjectAttrib)Data2.get(1)).getName());
            DB.appendChild(elem2);
            for (ObjectAttrib objAtt : Data2) {
                Element single = doc.createElement(objAtt.getTittle());
                Attr atrib = doc.createAttribute("Value");
                atrib.setValue(objAtt.getValue());
                single.setAttributeNode(atrib);
                elem2.appendChild(single);
            }
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty("version", "1.1");
            transformer.setOutputProperty("encoding", "UTF-8");
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(Archivo);
            transformer.transform(source, result);
            return true;
        } catch (ParserConfigurationException|javax.xml.transform.TransformerException ex) {
            Logger.getLogger(ExportXML.class.getName()).log(Level.SEVERE, (String)null, ex);
            return false;
        }
    }

    public static ArrayList<ArrayList> ReadXMLfromText(String XMLText) {
        try {
            ArrayList<ArrayList> Arr = new ArrayList<>();
            ArrayList<ObjectAttrib> AD = new ArrayList<>();
            ArrayList<ObjectAttrib> mdl = new ArrayList<>();
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            InputSource is = new InputSource();
            is.setCharacterStream(new StringReader(XMLText));
            is.setEncoding("UTF-8");
            Document doc = db.parse(is);
            doc.getDocumentElement().normalize();
            doc.setXmlVersion("1.1");
            doc.getDocumentElement().normalize();
            NodeList NodesAD = doc.getElementsByTagName("ADempiere");
            Node nodo = NodesAD.item(0);
            if (nodo.getNodeType() == 1) {
                Element element = (Element)nodo;
                Element element1 = (Element)element.getElementsByTagName("DatabaseName").item(0);
                Element element2 = (Element)element.getElementsByTagName("User").item(0);
                Element element3 = (Element)element.getElementsByTagName("Password").item(0);
                Element element4 = (Element)element.getElementsByTagName("Location").item(0);
                Element element5 = (Element)element.getElementsByTagName("Port").item(0);
                Element element6 = (Element)element.getElementsByTagName("Schema").item(0);
                AD.add(new ObjectAttrib(nodo.getNodeName(), element1.getNodeName(), element1.getAttribute("Value")));
                AD.add(new ObjectAttrib(nodo.getNodeName(), element2.getNodeName(), element2.getAttribute("Value")));
                AD.add(new ObjectAttrib(nodo.getNodeName(), element3.getNodeName(), element3.getAttribute("Value")));
                AD.add(new ObjectAttrib(nodo.getNodeName(), element4.getNodeName(), element4.getAttribute("Value")));
                AD.add(new ObjectAttrib(nodo.getNodeName(), element5.getNodeName(), element5.getAttribute("Value")));
                AD.add(new ObjectAttrib(nodo.getNodeName(), element6.getNodeName(), element6.getAttribute("Value")));
            }
            NodeList NodesMDL = doc.getElementsByTagName("Moodle");
            Node nodo1 = NodesMDL.item(0);
            if (nodo.getNodeType() == 1) {
                Element element = (Element)nodo1;
                Element element11 = (Element)element.getElementsByTagName("DatabaseName").item(0);
                Element element21 = (Element)element.getElementsByTagName("User").item(0);
                Element element31 = (Element)element.getElementsByTagName("Password").item(0);
                Element element41 = (Element)element.getElementsByTagName("Location").item(0);
                Element element51 = (Element)element.getElementsByTagName("Port").item(0);
                Element element61 = (Element)element.getElementsByTagName("Schema").item(0);
                mdl.add(new ObjectAttrib(nodo1.getNodeName(), element11.getNodeName(), element11.getAttribute("Value")));
                mdl.add(new ObjectAttrib(nodo1.getNodeName(), element21.getNodeName(), element21.getAttribute("Value")));
                mdl.add(new ObjectAttrib(nodo1.getNodeName(), element31.getNodeName(), element31.getAttribute("Value")));
                mdl.add(new ObjectAttrib(nodo1.getNodeName(), element41.getNodeName(), element41.getAttribute("Value")));
                mdl.add(new ObjectAttrib(nodo1.getNodeName(), element51.getNodeName(), element51.getAttribute("Value")));
                mdl.add(new ObjectAttrib(nodo1.getNodeName(), element61.getNodeName(), element61.getAttribute("Value")));
            }
            Arr.add(AD);
            Arr.add(mdl);
            return Arr;
        } catch (ParserConfigurationException|SAXException|IOException ex) {
            Logger.getLogger(ExportXML.class.getName()).log(Level.SEVERE, (String)null, ex);
            return null;
        }
    }

    public static ArrayList<ArrayList> ReadXMLFile(File file) {
        try {
            ArrayList<ArrayList> Arr = new ArrayList<>();
            ArrayList<ObjectAttrib> AD = new ArrayList<>();
            ArrayList<ObjectAttrib> mdl = new ArrayList<>();
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = dbf.newDocumentBuilder();
            Document doc = documentBuilder.parse(file);
            doc.setXmlVersion("1.1");
            doc.getDocumentElement().normalize();
            NodeList NodesAD = doc.getElementsByTagName("ADempiere");
            Node nodo = NodesAD.item(0);
            if (nodo.getNodeType() == 1) {
                Element element = (Element)nodo;
                Element element1 = (Element)element.getElementsByTagName("DatabaseName").item(0);
                Element element2 = (Element)element.getElementsByTagName("User").item(0);
                Element element3 = (Element)element.getElementsByTagName("Password").item(0);
                Element element4 = (Element)element.getElementsByTagName("Location").item(0);
                Element element5 = (Element)element.getElementsByTagName("Port").item(0);
                Element element6 = (Element)element.getElementsByTagName("Schema").item(0);
                AD.add(new ObjectAttrib(nodo.getNodeName(), element1.getNodeName(), element1.getAttribute("Value")));
                AD.add(new ObjectAttrib(nodo.getNodeName(), element2.getNodeName(), element2.getAttribute("Value")));
                AD.add(new ObjectAttrib(nodo.getNodeName(), element3.getNodeName(), element3.getAttribute("Value")));
                AD.add(new ObjectAttrib(nodo.getNodeName(), element4.getNodeName(), element4.getAttribute("Value")));
                AD.add(new ObjectAttrib(nodo.getNodeName(), element5.getNodeName(), element5.getAttribute("Value")));
                AD.add(new ObjectAttrib(nodo.getNodeName(), element6.getNodeName(), element6.getAttribute("Value")));
            }
            NodeList NodesMDL = doc.getElementsByTagName("Moodle");
            Node nodo1 = NodesMDL.item(0);
            if (nodo.getNodeType() == 1) {
                Element element = (Element)nodo1;
                Element element11 = (Element)element.getElementsByTagName("DatabaseName").item(0);
                Element element21 = (Element)element.getElementsByTagName("User").item(0);
                Element element31 = (Element)element.getElementsByTagName("Password").item(0);
                Element element41 = (Element)element.getElementsByTagName("Location").item(0);
                Element element51 = (Element)element.getElementsByTagName("Port").item(0);
                Element element61 = (Element)element.getElementsByTagName("Schema").item(0);
                mdl.add(new ObjectAttrib(nodo1.getNodeName(), element11.getNodeName(), element11.getAttribute("Value")));
                mdl.add(new ObjectAttrib(nodo1.getNodeName(), element21.getNodeName(), element21.getAttribute("Value")));
                mdl.add(new ObjectAttrib(nodo1.getNodeName(), element31.getNodeName(), element31.getAttribute("Value")));
                mdl.add(new ObjectAttrib(nodo1.getNodeName(), element41.getNodeName(), element41.getAttribute("Value")));
                mdl.add(new ObjectAttrib(nodo1.getNodeName(), element51.getNodeName(), element51.getAttribute("Value")));
                mdl.add(new ObjectAttrib(nodo1.getNodeName(), element61.getNodeName(), element61.getAttribute("Value")));
            }
            Arr.add(AD);
            Arr.add(mdl);
            return Arr;
        } catch (ParserConfigurationException|SAXException|IOException ex) {
            Logger.getLogger(ExportXML.class.getName()).log(Level.SEVERE, (String)null, ex);
            return null;
        }
    }

    public static String getIDUserFromXMLMoodle(String XMLResponse) {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            InputSource is = new InputSource();
            is.setCharacterStream(new StringReader(XMLResponse));
            is.setEncoding("UTF-8");
            Document document = db.parse(is);
        } catch (SAXException ex) {
            Logger.getLogger(ExportXML.class.getName()).log(Level.SEVERE, (String)null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ExportXML.class.getName()).log(Level.SEVERE, (String)null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(ExportXML.class.getName()).log(Level.SEVERE, (String)null, ex);
        }
        return null;
    }
}
