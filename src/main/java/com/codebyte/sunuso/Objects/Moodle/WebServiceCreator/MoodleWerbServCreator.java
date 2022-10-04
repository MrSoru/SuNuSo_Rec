/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codebyte.sunuso.Objects.Moodle.WebServiceCreator;

import com.codebyte.sunuso.Objects.Moodle.Moodle_Curso;
import com.codebyte.sunuso.Objects.Moodle.Resources.Courses;
import com.codebyte.sunuso.Objects.Moodle.Resources.ExceptionMoodle;
import com.codebyte.sunuso.Objects.Moodle.Resources.Response;
import com.codebyte.sunuso.Objects.Moodle.Resources.UsuarioGenerico;
import com.codebyte.sunuso.Objects.Moodle.WebServiceConsumer.Consumer;
import com.codebyte.sunuso.Objects.Surver.Surver_Curso;
import com.codebyte.sunuso.Objects.Surver.UsuarioSurver;
import com.codebyte.sunuso.Resources.Information;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author Arturo Meza Garcia
 */
public class MoodleWerbServCreator {

    /**
     * @deprecated String Send
     */
    @Deprecated
    private synchronized static String Send(URLBuilder URL) {
        try {
            Document doc = Consumer.getResponse(URL.buildUrlConsumtion());
            StringWriter sw = new StringWriter();
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

            transformer.transform(new DOMSource(doc), new StreamResult(sw));
            return sw.toString();
        } catch (TransformerConfigurationException ex) {
            Logger.getLogger(MoodleWerbServCreator.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (TransformerException ex) {
            Logger.getLogger(MoodleWerbServCreator.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    /**
     * @deprecated
     *
     */
    @Deprecated
    private synchronized static Integer send2(URLBuilder URL) {
//        JSONObject JSONObj = new JSONObject(Consumer.getResponseJson(URL.buildUrlConsumtion()));
////        System.out.println(JSONObj.toString(4));
//        Iterator<String> it = JSONObj.keySet().iterator();
//        Iterator<String> itv = JSONObj.keys();
////        System.out.println("----------------------------\n" + JSONObj.getInt("idnumber") + "\n--------------------------------------------------------------");
//        while (it.hasNext()) {
//            String Key = it.next();
//            String V = itv.next();
////            System.out.println(Key + " V " + V);
//        }

//        JSONArray jSONArray = new JSONArray(Consumer.getResponseJson(URL.buildUrlConsumtion()));
//        jSONArray.get(0);
        return null;
    }

    private synchronized static Document Send_XML(URLBuilder URL) {
        Document doc = Consumer.getResponse(URL.buildUrlConsumtion());
        return doc;
    }

    private synchronized static String Send_String(URLBuilder URL) {
        return Consumer.getResponseString(URL.buildUrlConsumtion());
    }

    public synchronized static boolean ModificarUsuario(final String ServPath, final String Token, final String ID, final String userName, final String firstName, final String lastName, final String email, final String RFC, final String Empresa, final String Segmento) {

        URLBuilder URL = new URLBuilder(ServPath);
        URL.addParam("wstoken", Token);
        URL.addParam("wsfunction", "core_user_update_users");
        URL.addParam("users[0][id]", ID);//
        URL.addParam("users[0][username]", userName);
        URL.addParam("users[0][firstname]", firstName);//
        URL.addParam("users[0][lastname]", lastName);//
        URL.addParam("users[0][email]", email);//
        URL.addParam("users[0][timezone]", "99");
        URL.addParam("users[0][idnumber]", RFC);//
        URL.addParam("users[0][country]", "MX");
        URL.addParam("users[0][institution]", Empresa);
        URL.addParam("users[0][department]", Segmento);

        Document document = MoodleWerbServCreator.Send_XML(URL);
        String Response = "";
        NodeList ExceptionList = document.getElementsByTagName("EXCEPTION");
        for (int i = 0; i < ExceptionList.getLength(); i++) {
            Node ExceptionNode = ExceptionList.item(i);
            if (ExceptionNode.getNodeType() == Node.ELEMENT_NODE) {
                Element ElementException = (Element) ExceptionNode;
                NodeList ChildsEx = ElementException.getChildNodes();
                for (int j = 0; j < ChildsEx.getLength(); j++) {
                    Node ChildsExNode = ChildsEx.item(i);
                    if (ChildsExNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element ChildsExElement = (Element) ChildsExNode;
                        Response += ChildsExElement.getNodeName() + ": " + ChildsExElement.getTextContent() + "\n";
                    }
                }
            }
        }
        return Response.isBlank();
    }

    public synchronized static boolean ModificarUsuario(final String ServPath, final String Token, final String ID_moodle, final UsuarioSurver UsuarioSurver) {

        URLBuilder URL = new URLBuilder(ServPath);
        URL.addParam("wstoken", Token);
        URL.addParam("wsfunction", "core_user_update_users");
        URL.addParam("users[0][id]", ID_moodle);//
        URL.addParam("users[0][username]", UsuarioSurver.getUsuario());
        URL.addParam("users[0][firstname]", UsuarioSurver.getNombre());//
        URL.addParam("users[0][lastname]", UsuarioSurver.getPaterno() + " " + UsuarioSurver.getMaterno());//
        URL.addParam("users[0][email]", UsuarioSurver.getCorreo());//
        URL.addParam("users[0][timezone]", "99");
        URL.addParam("users[0][idnumber]", UsuarioSurver.getRFC() == null ? "" : UsuarioSurver.getRFC());//
        URL.addParam("users[0][country]", "MX");
        URL.addParam("users[0][institution]", UsuarioSurver.getEmpresa());
        URL.addParam("users[0][department]", UsuarioSurver.getSegmento());
        URL.addParam("users[0][address]", UsuarioSurver.getPuesto());

        Document document = MoodleWerbServCreator.Send_XML(URL);
        String Response = "";
        NodeList ExceptionList = document.getElementsByTagName("EXCEPTION");
        for (int i = 0; i < ExceptionList.getLength(); i++) {
            Node ExceptionNode = ExceptionList.item(i);
            if (ExceptionNode.getNodeType() == Node.ELEMENT_NODE) {
                Element ElementException = (Element) ExceptionNode;
                NodeList ChildsEx = ElementException.getChildNodes();
                for (int j = 0; j < ChildsEx.getLength(); j++) {
                    Node ChildsExNode = ChildsEx.item(i);
                    if (ChildsExNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element ChildsExElement = (Element) ChildsExNode;
                        Response += ChildsExElement.getNodeName() + ": " + ChildsExElement.getTextContent() + "\n";
                    }
                }
            }
        }
        return Response.isBlank();
    }

    public synchronized static boolean SuspenderUsuario(final String ServPath, final String Token, final String ID) {

        URLBuilder URL = new URLBuilder(ServPath);
        URL.addParam("wstoken", Token);
        URL.addParam("wsfunction", "core_user_update_users");
        URL.addParam("users[0][id]", ID);// 
        URL.addParam("users[0][suspended]", "1");

        Document document = MoodleWerbServCreator.Send_XML(URL);
        String Response = "";
        NodeList ExceptionList = document.getElementsByTagName("EXCEPTION");
        for (int i = 0; i < ExceptionList.getLength(); i++) {
            Node ExceptionNode = ExceptionList.item(i);
            if (ExceptionNode.getNodeType() == Node.ELEMENT_NODE) {
                Element ElementException = (Element) ExceptionNode;
                NodeList ChildsEx = ElementException.getChildNodes();
                for (int j = 0; j < ChildsEx.getLength(); j++) {
                    Node ChildsExNode = ChildsEx.item(i);
                    if (ChildsExNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element ChildsExElement = (Element) ChildsExNode;
                        Response += ChildsExElement.getNodeName() + ": " + ChildsExElement.getTextContent() + "\n";
                    }
                }
            }
        }
        return Response.isBlank();
    }

    public synchronized static boolean ActivarUsuario(final String ServPath, final String Token, final String ID) {

        URLBuilder URL = new URLBuilder(ServPath);
        URL.addParam("wstoken", Token);
        URL.addParam("wsfunction", "core_user_update_users");
        URL.addParam("users[0][id]", ID);// 
        URL.addParam("users[0][suspended]", "0");

        Document document = MoodleWerbServCreator.Send_XML(URL);
        String Response = "";
        NodeList ExceptionList = document.getElementsByTagName("EXCEPTION");
        for (int i = 0; i < ExceptionList.getLength(); i++) {
            Node ExceptionNode = ExceptionList.item(i);
            if (ExceptionNode.getNodeType() == Node.ELEMENT_NODE) {
                Element ElementException = (Element) ExceptionNode;
                NodeList ChildsEx = ElementException.getChildNodes();
                for (int j = 0; j < ChildsEx.getLength(); j++) {
                    Node ChildsExNode = ChildsEx.item(i);
                    if (ChildsExNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element ChildsExElement = (Element) ChildsExNode;
                        Response += ChildsExElement.getNodeName() + ": " + ChildsExElement.getTextContent() + "\n";
                    }
                }
            }
        }
        return Response.isBlank();
    }

    public synchronized static boolean cambiarContrasena(final String ServPath, final String Token, final String ID, final String Password) {

        URLBuilder URL = new URLBuilder(ServPath);
        URL.addParam("wstoken", Token);
        URL.addParam("wsfunction", "core_user_update_users");
        URL.addParam("users[0][id]", ID);
        URL.addParam("users[0][password]", Password);
        URL.addParam("users[0][preferences][0][type]", "auth_forcepasswordchange");
        URL.addParam("users[0][preferences][0][value]", "1");

        Document document = MoodleWerbServCreator.Send_XML(URL);
        String Response = "";
        NodeList ExceptionList = document.getElementsByTagName("EXCEPTION");
        for (int i = 0; i < ExceptionList.getLength(); i++) {
            Node ExceptionNode = ExceptionList.item(i);
            if (ExceptionNode.getNodeType() == Node.ELEMENT_NODE) {
                Element ElementException = (Element) ExceptionNode;
                NodeList ChildsEx = ElementException.getChildNodes();
                for (int j = 0; j < ChildsEx.getLength(); j++) {
                    Node ChildsExNode = ChildsEx.item(i);
                    if (ChildsExNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element ChildsExElement = (Element) ChildsExNode;
                        Response += ChildsExElement.getNodeName() + ": " + ChildsExElement.getTextContent() + "\n";
                    }
                }
            }
        }
        return Response.isBlank();

    }

    public synchronized static String bajaUsuarioMoodlePlatform(final String ServPath, final String Token, final String ID) {
        URLBuilder URL = new URLBuilder(ServPath);
        URL.addParam("wstoken", Token);
        URL.addParam("wsfunction", "core_user_delete_users");
        URL.addParam("userids[0]", ID);
        ArrayList<ExceptionMoodle> Exs = new ArrayList<>();
        ExceptionMoodle Ex;

        Document doc = MoodleWerbServCreator.Send_XML(URL);

        NodeList ExceptionList = doc.getElementsByTagName("EXCEPTION");
        for (int i = 0; i < ExceptionList.getLength(); i++) {
            Node ExceptionNode = ExceptionList.item(i);
            if (ExceptionNode.getNodeType() == Node.ELEMENT_NODE) {
                Ex = new ExceptionMoodle();
                Element ExceptionElement = (Element) ExceptionNode;
                NodeList ChidExList = ExceptionElement.getChildNodes();
                for (int j = 0; j < ChidExList.getLength(); j++) {
                    Node ChidNode = ChidExList.item(j);
                    if (ChidNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element ChidElement = (Element) ChidNode;
                        String TagName = ChidElement.getTagName();
                        switch (TagName) {
                            case "MESSAGE":
                                Ex.setMESSAGE(ChidElement.getTextContent());
                                break;
                            case "DEBUGINFO":
                                Ex.setDEBUGINFO(ChidElement.getTextContent());
                        }
                    }
                }
                if (!Ex.isNullData()) {
                    Exs.add(Ex);
                }
            }
        }
        if (Exs.isEmpty()) {
            return "";
        } else {
            return Exs.get(0).toString();
        }

    }

    public synchronized static boolean asignarUsuarioCurso(final String ServPath, final String Token, final String ID, final String CourseID, final String Puesto) {
        URLBuilder URL = new URLBuilder(ServPath);
        URL.addParam("wstoken", Token);
        URL.addParam("wsfunction", "enrol_manual_enrol_users");
        URL.addParam("enrolments[0][userid]", ID);
        URL.addParam("enrolments[0][courseid]", CourseID);
        URL.addParam("enrolments[0][roleid]", Puesto);
        ArrayList<ExceptionMoodle> Exs = new ArrayList<>();
        ExceptionMoodle Ex;

        Document doc = MoodleWerbServCreator.Send_XML(URL);

        NodeList ExceptionList = doc.getElementsByTagName("EXCEPTION");
        for (int i = 0; i < ExceptionList.getLength(); i++) {
            Node ExceptionNode = ExceptionList.item(i);
            if (ExceptionNode.getNodeType() == Node.ELEMENT_NODE) {
                Ex = new ExceptionMoodle();
                Element ExceptionElement = (Element) ExceptionNode;
                NodeList ChidExList = ExceptionElement.getChildNodes();
                for (int j = 0; j < 10; j++) {
                    Node ChidNode = ChidExList.item(j);
                    if (ChidNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element ChidElement = (Element) ChidNode;
                        String TagName = ChidElement.getTagName();
                        switch (TagName) {
                            case "MESSAGE":
                                Ex.setMESSAGE(ChidElement.getTextContent());
                                break;
                            case "DEBUGINFO":
                                Ex.setDEBUGINFO(ChidElement.getTextContent());
                        }
                    }
                }
                if (!Ex.isNullData()) {
                    Exs.add(Ex);
                }
            }
        }
        return Exs.isEmpty();
    }

    public synchronized static boolean bajaCursoUsuario(final String ServPath, final String Token, final String ID, final String CourseID) {
        URLBuilder URL = new URLBuilder(ServPath);
        URL.addParam("wstoken", Token);
        URL.addParam("wsfunction", "enrol_manual_unenrol_users");
        URL.addParam("enrolments[0][userid]", ID);
        URL.addParam("enrolments[0][courseid]", CourseID);

        ArrayList<ExceptionMoodle> Exs = new ArrayList<>();
        ExceptionMoodle Ex;

        Document doc = MoodleWerbServCreator.Send_XML(URL);

        NodeList ExceptionList = doc.getElementsByTagName("EXCEPTION");
        for (int i = 0; i < ExceptionList.getLength(); i++) {
            Node ExceptionNode = ExceptionList.item(i);
            if (ExceptionNode.getNodeType() == Node.ELEMENT_NODE) {
                Ex = new ExceptionMoodle();
                Element ExceptionElement = (Element) ExceptionNode;
                NodeList ChidExList = ExceptionElement.getChildNodes();
                for (int j = 0; j < 10; j++) {
                    Node ChidNode = ChidExList.item(j);
                    if (ChidNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element ChidElement = (Element) ChidNode;
                        String TagName = ChidElement.getTagName();
                        switch (TagName) {
                            case "MESSAGE":
                                Ex.setMESSAGE(ChidElement.getTextContent());
                                break;
                            case "DEBUGINFO":
                                Ex.setDEBUGINFO(ChidElement.getTextContent());
                        }
                    }
                }
                if (!Ex.isNullData()) {
                    Exs.add(Ex);
                }
            }
        }
        return Exs.isEmpty();

    }

    public synchronized static UsuarioGenerico buscarUsuario_RFC(final String ServPath, final String Token, final String RFC) {
        URLBuilder URL = new URLBuilder(ServPath);
        URL.addParam("wstoken", Token);
        URL.addParam("wsfunction", "core_user_get_users");
        URL.addParam("criteria[0][key]", "idnumber");
        URL.addParam("criteria[0][value]", RFC);

        ArrayList<UsuarioGenerico> Users = new ArrayList<>();//lista de usuarios
        UsuarioGenerico User;//usuario

        Document doc = MoodleWerbServCreator.Send_XML(URL);
        NodeList Root = doc.getElementsByTagName("SINGLE");
        for (int i = 0; i < Root.getLength(); i++) {
            Node RootNode = Root.item(i);
            if (RootNode.getNodeType() == Node.ELEMENT_NODE) {
                User = new UsuarioGenerico();
                Element RootElement = (Element) RootNode;
                NodeList KeyList = RootElement.getChildNodes();
                for (int j = 0; j < KeyList.getLength(); j++) {
                    Node KeyNode = KeyList.item(j);
                    if (KeyNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element KeyElement = (Element) KeyNode;
                        NodeList ValueList = KeyElement.getChildNodes();
                        for (int k = 0; k < ValueList.getLength(); k++) {
                            Node NodeValue = ValueList.item(k);
                            if (NodeValue.getNodeType() == Node.ELEMENT_NODE) {
                                Element ValueElement = (Element) NodeValue;
                                //System.out.println(KeyElement.getAttribute("name") + "  ddddd  " + ValueElement.getTextContent());
                                String KeyAttText = KeyElement.getAttribute("name");
                                switch (KeyAttText) {
                                    case "idnumber":
                                        User.setIdNumber(ValueElement.getTextContent());
                                        break;
                                    case "id":
                                        User.setID(ValueElement.getTextContent());
                                        break;
                                    case "firstname":
                                        User.setFirstName(ValueElement.getTextContent());
                                        break;
                                    case "lastname":
                                        User.setLastName(ValueElement.getTextContent());
                                        break;
                                    case "fullname":
                                        User.setFullName(ValueElement.getTextContent());
                                        break;
                                    case "email":
                                        User.setEmail(ValueElement.getTextContent());
                                        break;
                                    case "username":
                                        User.setUserName(ValueElement.getTextContent());
                                        break;
                                    case "department":
                                        User.setDepartament(ValueElement.getTextContent());
                                        break;
                                    case "institution":
                                        User.setInstitution(ValueElement.getTextContent());
                                        break;
                                }
                            }
                        }
                    }
                }
                if (!User.isNullData()) {
                    Users.add(User);
                }
            }
        }
        //for result test only
//        for (UsuarioGenerico User1 : Users) {
//            System.out.println(User1.toString());
//        }
        if (Users.isEmpty()) {
            return null;
        }
//        return ExportXML.getIDUserFromXMLMoodle(MoodleWerbServCreator.Send_XML(URL));
        return Users.get(0);
    }

    public synchronized static UsuarioGenerico buscarUsuario_Username(final String ServPath, final String Token, final String Username) {
        URLBuilder URL = new URLBuilder(ServPath);
        URL.addParam("wstoken", Token);
        URL.addParam("wsfunction", "core_user_get_users");
        URL.addParam("criteria[0][key]", "username");
        URL.addParam("criteria[0][value]", Username);

        ArrayList<UsuarioGenerico> Users = new ArrayList<>();//lista de usuarios
        UsuarioGenerico User;//usuario

        Document doc = MoodleWerbServCreator.Send_XML(URL);
        NodeList Root = doc.getElementsByTagName("SINGLE");
        for (int i = 0; i < Root.getLength(); i++) {
            Node RootNode = Root.item(i);
            if (RootNode.getNodeType() == Node.ELEMENT_NODE) {
                User = new UsuarioGenerico();
                Element RootElement = (Element) RootNode;
                NodeList KeyList = RootElement.getChildNodes();
                for (int j = 0; j < KeyList.getLength(); j++) {
                    Node KeyNode = KeyList.item(j);
                    if (KeyNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element KeyElement = (Element) KeyNode;
                        NodeList ValueList = KeyElement.getChildNodes();
                        for (int k = 0; k < ValueList.getLength(); k++) {
                            Node NodeValue = ValueList.item(k);
                            if (NodeValue.getNodeType() == Node.ELEMENT_NODE) {
                                Element ValueElement = (Element) NodeValue;
                                //System.out.println(KeyElement.getAttribute("name") + "  ddddd  " + ValueElement.getTextContent());
                                String KeyAttText = KeyElement.getAttribute("name");
                                switch (KeyAttText) {
                                    case "idnumber":
                                        User.setIdNumber(ValueElement.getTextContent());
                                        break;
                                    case "id":
                                        User.setID(ValueElement.getTextContent());
                                        break;
                                    case "firstname":
                                        User.setFirstName(ValueElement.getTextContent());
                                        break;
                                    case "lastname":
                                        User.setLastName(ValueElement.getTextContent());
                                        break;
                                    case "fullname":
                                        User.setFullName(ValueElement.getTextContent());
                                        break;
                                    case "email":
                                        User.setEmail(ValueElement.getTextContent());
                                        break;
                                    case "username":
                                        User.setUserName(ValueElement.getTextContent());
                                        break;
                                    case "department":
                                        User.setDepartament(ValueElement.getTextContent());
                                        break;
                                    case "institution":
                                        User.setInstitution(ValueElement.getTextContent());
                                        break;
                                }
                            }
                        }
                    }
                }
                if (!User.isNullData()) {
                    Users.add(User);
                }
            }
        }
        //for result test only
//        for (UsuarioGenerico User1 : Users) {
//            System.out.println(User1.toString());
//        }
        if (Users.isEmpty()) {
            return new UsuarioGenerico();
        }
//        return ExportXML.getIDUserFromXMLMoodle(MoodleWerbServCreator.Send_XML(URL));
        return Users.get(0);
    }

    public synchronized static ArrayList<UsuarioGenerico> AllUsers(final String ServPath, final String Token) {
        URLBuilder URL = new URLBuilder(ServPath);
        URL.addParam("wstoken", Token);
        URL.addParam("wsfunction", "core_user_get_users");
        URL.addParam("criteria[0][key]", "firstname");
        URL.addParam("criteria[0][value]", "%");

        ArrayList<UsuarioGenerico> Users = new ArrayList<>();//lista de usuarios
        UsuarioGenerico User;//usuario

        Document doc = MoodleWerbServCreator.Send_XML(URL);

        NodeList Root = doc.getElementsByTagName("SINGLE");
        for (int i = 0; i < Root.getLength(); i++) {
            Node RootNode = Root.item(i);
            if (RootNode.getNodeType() == Node.ELEMENT_NODE) {
                User = new UsuarioGenerico();
                Element RootElement = (Element) RootNode;
                NodeList KeyList = RootElement.getChildNodes();
                for (int j = 0; j < KeyList.getLength(); j++) {
                    Node KeyNode = KeyList.item(j);
                    if (KeyNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element KeyElement = (Element) KeyNode;
                        NodeList ValueList = KeyElement.getChildNodes();
                        for (int k = 0; k < ValueList.getLength(); k++) {
                            Node NodeValue = ValueList.item(k);
                            if (NodeValue.getNodeType() == Node.ELEMENT_NODE) {
                                Element ValueElement = (Element) NodeValue;
                                //System.out.println(KeyElement.getAttribute("name") + "  ddddd  " + ValueElement.getTextContent());
                                String KeyAttText = KeyElement.getAttribute("name");
                                switch (KeyAttText) {
                                    case "idnumber":
                                        User.setIdNumber(ValueElement.getTextContent());
                                        break;
                                    case "id":
                                        User.setID(ValueElement.getTextContent());
                                        break;
                                    case "firstname":
                                        User.setFirstName(ValueElement.getTextContent());
                                        break;
                                    case "lastname":
                                        User.setLastName(ValueElement.getTextContent());
                                        break;
                                    case "fullname":
                                        User.setFullName(ValueElement.getTextContent());
                                        break;
                                    case "email":
                                        User.setEmail(ValueElement.getTextContent());
                                        break;
                                    case "username":
                                        User.setUserName(ValueElement.getTextContent());
                                        break;
                                    case "department":
                                        User.setDepartament(ValueElement.getTextContent());
                                        break;
                                    case "institution":
                                        User.setInstitution(ValueElement.getTextContent());
                                        break;
                                    case "suspended": 
                                        User.setSuspended(ValueElement.getTextContent());
                                        break;
                                }
                            }
                        }
                    }
                }
                if (!User.isNullData()) {
                    Users.add(User);
                }
            }
        }
        //for result test only
//        for (UsuarioGenerico User1 : Users) {
//            System.out.println(User1.toString());
//        }
//        return ExportXML.getIDUserFromXMLMoodle(MoodleWerbServCreator.Send_XML(URL));
        if (Users.isEmpty()) {
            return null;
        }
        return Users;
    }

    public synchronized static ArrayList<Courses> getAllCourses(final String ServPath, final String Token) {
        URLBuilder URL = new URLBuilder(ServPath);
        URL.addParam("wstoken", Token);
        URL.addParam("wsfunction", "core_course_get_courses");
        //URL.addParam("moodlewsrestformat", "json");
        ArrayList<Courses> Cursos = new ArrayList<>();
        Courses Curso;
        Document doc = MoodleWerbServCreator.Send_XML(URL);
        NodeList Root = doc.getElementsByTagName("SINGLE");
        for (int i = 0; i < Root.getLength(); i++) {
            Node Nod = Root.item(i);
            if (Nod.getNodeType() == Node.ELEMENT_NODE) {//verifica si el nodo SINGLE es un Nodo Validp para su lectura
                Element Single = (Element) Nod;
                NodeList child = Single.getChildNodes();
                //System.out.println(Single.getNodeName());
                Curso = new Courses();
                for (int j = 0; j < child.getLength(); j++) {
                    Node Key = child.item(j);
                    if (Key.getNodeType() == Node.ELEMENT_NODE) {//verifica si los nodos consecuentes son nodos validos
                        Element keyelement = (Element) Key;

                        //System.out.println("<" + keyelement.getNodeName() + " name= " + keyelement.getAttribute("name"));
                        //flitros para nodo key
                        //if (keyelement.getAttribute("name").equals("id") || keyelement.getAttribute("name").equals("shortname") || keyelement.getAttribute("name").equals("fullname") 
                        //|| keyelement.getAttribute("name").equals("displayname") || keyelement.getAttribute("name").equals("idnumber") || keyelement.getAttribute("name").equals("lang")) {
                        NodeList valNodeList = keyelement.getChildNodes();
                        for (int k = 0; k < valNodeList.getLength(); k++) {
                            Node Value = valNodeList.item(k);
                            if (Value.getNodeType() == Node.ELEMENT_NODE) {
                                Element c = (Element) Value;
                                //System.out.println(keyelement.getAttribute("name") + " Value: " + c.getTextContent());
                                String AttribName = keyelement.getAttribute("name");
                                switch (AttribName) {
                                    case "id":
                                        Curso.setID(c.getTextContent());
                                        break;
                                    case "shortname":
                                        Curso.setNombreCorto(c.getTextContent());
                                        break;
                                    case "fullname":
                                        Curso.setNombreCompleto(c.getTextContent());
                                        break;
                                    case "displayname":
                                        Curso.setNombreMostrado(c.getTextContent());
                                        break;
                                    case "idnumber":
                                        Curso.setIDNumber(c.getTextContent());
                                        break;
                                    case "lang":
                                        Curso.setIdioma(c.getTextContent());
                                        break;
                                    case "summary":
                                        Curso.setDescripcion(c.getTextContent());
                                        break;
                                }
                            }
                        }
                        //}

                    }
                }
                if (!Curso.isNulldata()) {
                    Cursos.add(Curso);
                }

            }

        }
//        for (Courses Curso1 : Cursos) {
//            System.out.println(Curso1.toString());
//        }
        return Cursos;
    }

    public synchronized static Courses getCourse(final String ServPath, final String Token, final String ID) {
        URLBuilder URL = new URLBuilder(ServPath);
        URL.addParam("wstoken", Token);
        URL.addParam("wsfunction", "core_course_get_courses");
        URL.addParam("options[ids][0]", ID);
        ArrayList<Courses> Cursos = new ArrayList<>();
        Courses Curso;
        Document doc = MoodleWerbServCreator.Send_XML(URL);
        NodeList Root = doc.getElementsByTagName("SINGLE");
        for (int i = 0; i < Root.getLength(); i++) {
            Node Nod = Root.item(i);
            if (Nod.getNodeType() == Node.ELEMENT_NODE) {//verifica si el nodo SINGLE es un Nodo Validp para su lectura
                Element Single = (Element) Nod;
                NodeList child = Single.getChildNodes();
                //System.out.println(Single.getNodeName());
                Curso = new Courses();
                for (int j = 0; j < child.getLength(); j++) {
                    Node Key = child.item(j);
                    if (Key.getNodeType() == Node.ELEMENT_NODE) {//verifica si los nodos consecuentes son nodos validos
                        Element keyelement = (Element) Key;

                        //System.out.println("<" + keyelement.getNodeName() + " name= " + keyelement.getAttribute("name"));
                        //flitros para nodo key
                        //if (keyelement.getAttribute("name").equals("id") || keyelement.getAttribute("name").equals("shortname") || keyelement.getAttribute("name").equals("fullname") 
                        //|| keyelement.getAttribute("name").equals("displayname") || keyelement.getAttribute("name").equals("idnumber") || keyelement.getAttribute("name").equals("lang")) {
                        NodeList valNodeList = keyelement.getChildNodes();
                        for (int k = 0; k < valNodeList.getLength(); k++) {
                            Node Value = valNodeList.item(k);
                            if (Value.getNodeType() == Node.ELEMENT_NODE) {
                                Element c = (Element) Value;
                                //System.out.println(keyelement.getAttribute("name") + " Value: " + c.getTextContent());
                                String AttribName = keyelement.getAttribute("name");
                                switch (AttribName) {
                                    case "id":
                                        Curso.setID(c.getTextContent());
                                        break;
                                    case "shortname":
                                        Curso.setNombreCorto(c.getTextContent());
                                        break;
                                    case "fullname":
                                        Curso.setNombreCompleto(c.getTextContent());
                                        break;
                                    case "displayname":
                                        Curso.setNombreMostrado(c.getTextContent());
                                        break;
                                    case "idnumber":
                                        Curso.setIDNumber(c.getTextContent());
                                        break;
                                    case "lang":
                                        Curso.setIdioma(c.getTextContent());
                                        break;
                                    case "summary":
                                        Curso.setDescripcion(c.getTextContent());
                                        break;
                                }
                            }
                        }
                        //}

                    }
                }
                if (!Curso.isNulldata()) {
                    Cursos.add(Curso);
                }

            }

        }
//        for (Courses Curso1 : Cursos) {
//            System.out.println(Curso1.toString());
//        }
        if (Cursos.isEmpty()) {
            return null;
        }

        return Cursos.get(0);
    }

    public synchronized static Courses searchCourseByIdnumber(final String ServPath, final String Token, final String IDNumber) {
        URLBuilder URL = new URLBuilder(ServPath);
        URL.addParam("wstoken", Token);
        URL.addParam("wsfunction", "core_course_get_courses_by_field");
        URL.addParam("field", "idnumber");
        URL.addParam("value", IDNumber);
        ArrayList<Courses> Cursos = new ArrayList<>();
        Courses Curso = new Courses();

        String KeyElementNameAttrib = "";

        Document doc = MoodleWerbServCreator.Send_XML(URL);
        NodeList MultipleList = doc.getElementsByTagName("MULTIPLE");
        for (int i = 0; i < MultipleList.getLength(); i++) {
            Node MultipleNode = MultipleList.item(i);
            if (MultipleNode.getNodeType() == Node.ELEMENT_NODE) {
                Element MultipleElement = (Element) MultipleNode;
                NodeList SingleList = MultipleElement.getChildNodes();
                for (int j = 0; j < SingleList.getLength(); j++) {
                    Node SingleNode = SingleList.item(j);
                    if (SingleNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element SingleElement = (Element) SingleNode;
                        NodeList KeyList = SingleElement.getChildNodes();
                        Curso = new Courses();
                        for (int k = 0; k < KeyList.getLength(); k++) {
                            Node KeyNode = KeyList.item(k);
                            if (KeyNode.getNodeType() == Node.ELEMENT_NODE) {
                                Element KeyElement = (Element) KeyNode;
                                KeyElementNameAttrib = KeyElement.getAttribute("name");
                                NodeList ValueList = KeyElement.getChildNodes();
                                for (int l = 0; l < ValueList.getLength(); l++) {
                                    Node ValueNode = ValueList.item(l);
                                    if (ValueNode.getNodeType() == Node.ELEMENT_NODE) {
                                        Element ValueElement = (Element) ValueNode;
                                        switch (KeyElementNameAttrib) {
                                            case "id":
                                                Curso.setID(ValueElement.getTextContent());
                                                break;
                                            case "shortname":
                                                Curso.setNombreCorto(ValueElement.getTextContent());
                                                break;
                                            case "fullname":
                                                Curso.setNombreCompleto(ValueElement.getTextContent());
                                                break;
                                            case "displayname":
                                                Curso.setNombreMostrado(ValueElement.getTextContent());
                                                break;
                                            case "idnumber":
                                                Curso.setIDNumber(ValueElement.getTextContent());
                                                break;
                                            case "lang":
                                                Curso.setIdioma(ValueElement.getTextContent());
                                                break;
                                            case "summary":
                                                Curso.setDescripcion(ValueElement.getTextContent());
                                                break;
                                        }
                                    }
                                }
                            }
                        }
                        if (!Curso.isNulldata()) {
                            Cursos.add(Curso);
                        }
                    }
                }
            }
        }
        if (Cursos.isEmpty()) {
            return null;
        } else {
            return Cursos.get(0);
        }
    }

    public synchronized static Courses searchCourseShortName(final String ServPath, final String Token, final String ShortName) {
        URLBuilder URL = new URLBuilder(ServPath);
        URL.addParam("wstoken", Token);
        URL.addParam("wsfunction", "core_course_get_courses_by_field");
        URL.addParam("field", "shortname");
        URL.addParam("value", ShortName);
        ArrayList<Courses> Cursos = new ArrayList<>();
        Courses Curso = new Courses();

        String KeyElementNameAttrib = "";

        Document doc = MoodleWerbServCreator.Send_XML(URL);
        NodeList MultipleList = doc.getElementsByTagName("MULTIPLE");
        for (int i = 0; i < MultipleList.getLength(); i++) {
            Node MultipleNode = MultipleList.item(i);
            if (MultipleNode.getNodeType() == Node.ELEMENT_NODE) {
                Element MultipleElement = (Element) MultipleNode;
                NodeList SingleList = MultipleElement.getChildNodes();
                for (int j = 0; j < SingleList.getLength(); j++) {
                    Node SingleNode = SingleList.item(j);
                    if (SingleNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element SingleElement = (Element) SingleNode;
                        NodeList KeyList = SingleElement.getChildNodes();
                        Curso = new Courses();
                        for (int k = 0; k < KeyList.getLength(); k++) {
                            Node KeyNode = KeyList.item(k);
                            if (KeyNode.getNodeType() == Node.ELEMENT_NODE) {
                                Element KeyElement = (Element) KeyNode;
                                KeyElementNameAttrib = KeyElement.getAttribute("name");
                                NodeList ValueList = KeyElement.getChildNodes();
                                for (int l = 0; l < ValueList.getLength(); l++) {
                                    Node ValueNode = ValueList.item(l);
                                    if (ValueNode.getNodeType() == Node.ELEMENT_NODE) {
                                        Element ValueElement = (Element) ValueNode;
                                        switch (KeyElementNameAttrib) {
                                            case "id":
                                                Curso.setID(ValueElement.getTextContent());
                                                break;
                                            case "shortname":
                                                Curso.setNombreCorto(ValueElement.getTextContent());
                                                break;
                                            case "fullname":
                                                Curso.setNombreCompleto(ValueElement.getTextContent());
                                                break;
                                            case "displayname":
                                                Curso.setNombreMostrado(ValueElement.getTextContent());
                                                break;
                                            case "idnumber":
                                                Curso.setIDNumber(ValueElement.getTextContent());
                                                break;
                                            case "lang":
                                                Curso.setIdioma(ValueElement.getTextContent());
                                                break;
                                            case "summary":
                                                Curso.setDescripcion(ValueElement.getTextContent());
                                                break;
                                        }
                                    }
                                }
                            }
                        }
                        if (!Curso.isNulldata()) {
                            Cursos.add(Curso);
                        }
                    }
                }
            }
        }
        if (Cursos.isEmpty()) {
            return null;
        } else {
            return Cursos.get(0);
        }
    }

    public synchronized static ArrayList<UsuarioGenerico> getUsersFromCourse(final String ServPath, final String Token, final String IDNumber) {
        URLBuilder URL = new URLBuilder(ServPath);
        URL.addParam("wstoken", Token);
        URL.addParam("wsfunction", "core_enrol_get_enrolled_users");
        URL.addParam("courseid", IDNumber);

        String KeyElementNameAttrib = "";

        ArrayList<UsuarioGenerico> Users = new ArrayList<>();//lista de usuarios
        UsuarioGenerico User;//usuario

        Document doc = MoodleWerbServCreator.Send_XML(URL);
        NodeList MultipleList = doc.getElementsByTagName("MULTIPLE");
        for (int i = 0; i < MultipleList.getLength(); i++) {
            Node MultipleNode = MultipleList.item(i);
            if (MultipleNode.getNodeType() == Node.ELEMENT_NODE) {
                Element MultipleEle = (Element) MultipleNode;
                NodeList SingleList = MultipleEle.getChildNodes();
                for (int j = 0; j < SingleList.getLength(); j++) {
                    Node SingleNode = SingleList.item(j);
                    if (SingleNode.getNodeType() == Node.ELEMENT_NODE) {
                        User = new UsuarioGenerico();
                        Element SingleElement = (Element) SingleNode;
                        NodeList KeyList = SingleElement.getChildNodes();
                        for (int k = 0; k < KeyList.getLength(); k++) {
                            Node KeyNode = KeyList.item(k);
                            if (KeyNode.getNodeType() == Node.ELEMENT_NODE) {
                                Element KeyElement = (Element) KeyNode;
                                NodeList ValueList = KeyElement.getChildNodes();
                                for (int l = 0; l < ValueList.getLength(); l++) {
                                    Node ValueNode = ValueList.item(l);
                                    if (ValueNode.getNodeType() == Node.ELEMENT_NODE) {
                                        Element ValueElement = (Element) ValueNode;
                                        String KeyAttText = KeyElement.getAttribute("name");
                                        switch (KeyAttText) {
                                            case "idnumber":
                                                User.setIdNumber(ValueElement.getTextContent());
                                                break;
                                            case "id":
                                                User.setID(ValueElement.getTextContent());
                                                break;
                                            case "firstname":
                                                User.setFirstName(ValueElement.getTextContent());
                                                break;
                                            case "lastname":
                                                User.setLastName(ValueElement.getTextContent());
                                                break;
                                            case "fullname":
                                                User.setFullName(ValueElement.getTextContent());
                                                break;
                                            case "email":
                                                User.setEmail(ValueElement.getTextContent());
                                                break;
                                            case "username":
                                                User.setUserName(ValueElement.getTextContent());
                                                break;
                                            case "department":
                                                User.setDepartament(ValueElement.getTextContent());
                                                break;
                                            case "institution":
                                                User.setInstitution(ValueElement.getTextContent());
                                                break;
                                        }
                                    }
                                }
                            }
                        }
                        if (!User.isNullData() && User.getUserName() != null) {
                            Users.add(User);
                        }
                    }
                }
            }
        }
        if (Users.isEmpty()) {
            return null;
        } else {
            return Users;
        }
    }

    /**
     * <p>
     * Crea un usuario nuevo en Moodle</p>
     *
     * @param ServPath Direccion de webservice
     * @param Token Token de entrada de webservices
     * @param userName Nombre de usuario (lowercase)
     * @param Password Contraseña [Mayus][a-zA-Z0-9][espCharacter]
     * @param firstName Primer nombre
     * @param lastName Apellidos
     * @param email [az-A-Z0-9][@][a-zA-Z0-9].com
     * @param Segmento Departamento
     * @param RFC
     * @param Empresa
     *
     * @return Response Class
     */
    public synchronized static Response CreateUser1(
            final String ServPath,
            final String Token,
            final String userName,
            final String Password,
            final String firstName,
            final String lastName,
            final String email,
            final String RFC,
            final String Empresa,
            final String Segmento,
            final String Puesto) {

        URLBuilder URL = new URLBuilder(ServPath);
        URL.addParam("wstoken", Token);
        URL.addParam("wsfunction", "core_user_create_users");
        URL.addParam("users[0][username]", userName);
        URL.addParam("users[0][auth]", "manual");
        URL.addParam("users[0][password]", Password);
        URL.addParam("users[0][firstname]", firstName);
        URL.addParam("users[0][lastname]", lastName);
        URL.addParam("users[0][email]", email);
        URL.addParam("users[0][maildisplay]", "0");
        URL.addParam("users[0][timezone]", "99");
        URL.addParam("users[0][idnumber]", RFC);
        URL.addParam("users[0][country]", "MX");
        URL.addParam("users[0][institution]", Empresa);
        URL.addParam("users[0][department]", Segmento);
        URL.addParam("users[0][preferences][0][type]", "auth_forcepasswordchange");
        URL.addParam("users[0][preferences][0][value]", "1");
        //comprueba si el usuario existe
//        System.out.println(URL.buildUrlConsumtion());

        ArrayList<Response> responses = new ArrayList<>();
        Response response;
        Document doc = MoodleWerbServCreator.Send_XML(URL);
        NodeList Root = doc.getElementsByTagName("SINGLE");

        for (int i = 0; i < Root.getLength(); i++) {
            Node SingleNode = Root.item(i);
            if (SingleNode.getNodeType() == Node.ELEMENT_NODE) {
                response = new Response();
                Element SingleElement = (Element) SingleNode;
                NodeList KeyList = SingleElement.getChildNodes();
                for (int j = 0; j < KeyList.getLength(); j++) {
                    Node KeyNode = KeyList.item(j);
                    if (KeyNode.getNodeType() == Node.ELEMENT_NODE) {

                        Element KeyElement = (Element) KeyNode;
                        NodeList ValueList = KeyElement.getChildNodes();
                        for (int k = 0; k < ValueList.getLength(); k++) {
                            Node ValueNode = ValueList.item(k);
                            if (ValueNode.getNodeType() == Node.ELEMENT_NODE) {
                                Element ValueElement = (Element) ValueNode;
                                //                                    System.out.println(ValueElement.getTextContent());
                                String KeyAttText = KeyElement.getAttribute("name");
                                switch (KeyAttText) {
                                    case "id":
                                        response.setID(ValueElement.getTextContent());
                                        break;
                                    case "username":
                                        response.setID(ValueElement.getTextContent());
                                        break;
                                }
                            }
                        }
                    }
                }
                if (!response.isNullData()) {
                    responses.add(response);
                }
            }
        }

//        System.out.println("======\n Intento For");
//        for (Response response1 : responses) {
//            System.out.println(response1.toString());
//        }
        if (responses.isEmpty()) {
            return null;
        }
        return responses.get(0);
    }

    public synchronized static Response CreateGenericCourse(String Token, String ServPath, Surver_Curso Sur_Cur) {
        URLBuilder URL = new URLBuilder(ServPath);

        if (Sur_Cur.getDescripcion_curso() == null) {
            Sur_Cur.setDescripcion_curso("[Sin descripción " + Information.Fuente + "] -> Curso Generado automáticamente por interfaz " + Information.ProgramName + Information.Version + " " + Information.Acronimo);
        } else if (Sur_Cur.getDescripcion_curso().trim().isBlank()) {
            Sur_Cur.setDescripcion_curso("[Sin descripción " + Information.Fuente + "] -> Curso Generado automáticamente por interfaz " + Information.ProgramName + Information.Version + " " + Information.Acronimo);
        }

        URL.addParam("wstoken", Token);
        URL.addParam("wsfunction", "core_course_create_courses");
        URL.addParam("courses[0][fullname]", Sur_Cur.getNombre_Curso());
        URL.addParam("courses[0][shortname]", Sur_Cur.getNombre_Corto());
        URL.addParam("courses[0][categoryid]", "1");
        URL.addParam("courses[0][idnumber]", Sur_Cur.getMoodle_curso_id() + "");
        URL.addParam("courses[0][summary]", Sur_Cur.getDescripcion_curso());
        URL.addParam("courses[0][summaryformat]", "2");
        URL.addParam("courses[0][format]", "topics");
        URL.addParam("courses[0][showgrades]", "1");
        URL.addParam("courses[0][newsitems]", "5");
        URL.addParam("courses[0][numsections]", "4");
        URL.addParam("courses[0][maxbytes]", "0");
        URL.addParam("courses[0][showreports]", "0");
        URL.addParam("courses[0][visible]", "1");
        URL.addParam("courses[0][groupmode]", "0");
        URL.addParam("courses[0][groupmodeforce]", "0");
        URL.addParam("courses[0][defaultgroupingid]", "0");
        URL.addParam("courses[0][enablecompletion]", "1");
        URL.addParam("courses[0][completionnotify]", "1");

        ArrayList<Response> responses = new ArrayList<>();
        Response response;
        Document doc = MoodleWerbServCreator.Send_XML(URL);
        NodeList Root = doc.getElementsByTagName("SINGLE");

        for (int i = 0; i < Root.getLength(); i++) {
            Node SingleNode = Root.item(i);
            if (SingleNode.getNodeType() == Node.ELEMENT_NODE) {
                response = new Response();
                Element SingleElement = (Element) SingleNode;
                NodeList KeyList = SingleElement.getChildNodes();
                for (int j = 0; j < KeyList.getLength(); j++) {
                    Node KeyNode = KeyList.item(j);
                    if (KeyNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element KeyElement = (Element) KeyNode;
                        NodeList ValueList = KeyElement.getChildNodes();
                        for (int k = 0; k < ValueList.getLength(); k++) {
                            Node ValueNode = ValueList.item(k);
                            if (ValueNode.getNodeType() == Node.ELEMENT_NODE) {
                                Element ValueElement = (Element) ValueNode;
                                //                                    System.out.println(ValueElement.getTextContent());
                                String KeyAttText = KeyElement.getAttribute("name");
                                switch (KeyAttText) {
                                    case "id":
                                        response.setID(ValueElement.getTextContent());
                                        break;
                                    case "shortname":
                                        response.setID(ValueElement.getTextContent());
                                        break;
                                }
                            }
                        }
                    }
                }
                if (!response.isNullData()) {
                    responses.add(response);
                }
            }
        }

        if (responses.isEmpty()) {
            return null;
        }
        return responses.get(0);
    }

    public synchronized static boolean UpdateCourse(String Token, String ServPath, Moodle_Curso Curso, Surver_Curso Sur_Cur) {
        URLBuilder URL = new URLBuilder(ServPath);

        URL.addParam("wstoken", Token);
        URL.addParam("wsfunction", "core_course_update_courses");
        URL.addParam("courses[0][id]", Curso.getId() + "");
        URL.addParam("courses[0][fullname]", Sur_Cur.getNombre_Curso());
        URL.addParam("courses[0][shortname]", Sur_Cur.getNombre_Corto());
        URL.addParam("courses[0][idnumber]", Sur_Cur.getMoodle_curso_id() + "");
        URL.addParam("courses[0][summary]", Sur_Cur.getDescripcion_curso());
        URL.addParam("courses[0][summaryformat]", "2");

        ArrayList<Response> responses = new ArrayList<>();
        Response response;
        Document doc = MoodleWerbServCreator.Send_XML(URL);
        NodeList Root = doc.getElementsByTagName("SINGLE");
        for (int i = 0; i < Root.getLength(); i++) {
            Node SingleNode = Root.item(i);
            if (SingleNode.getNodeType() == Node.ELEMENT_NODE) {
                response = new Response();
                Element SingleElement = (Element) SingleNode;
                NodeList KeyList = SingleElement.getChildNodes();
                for (int j = 0; j < KeyList.getLength(); j++) {
                    Node KeyNode = KeyList.item(j);
                    if (KeyNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element KeyElement = (Element) KeyNode;
                        NodeList ValueList = KeyElement.getChildNodes();
                        for (int k = 0; k < ValueList.getLength(); k++) {
                            Node ValueNode = ValueList.item(k);
                            if (ValueNode.getNodeType() == Node.ELEMENT_NODE) {
                                Element ValueElement = (Element) ValueNode;
                                //                                    System.out.println(ValueElement.getTextContent());
                                String KeyAttText = KeyElement.getAttribute("name");
                                switch (KeyAttText) {
                                    case "item":
                                        response.setID(ValueElement.getTextContent());
                                        break;
                                    case "itemid":
                                        response.setID(ValueElement.getTextContent());
                                        break;
                                }
                            }
                        }
                    }
                }
                if (!response.isNullData()) {
                    responses.add(response);
                }
            }
        }
        return !responses.isEmpty();
    }

    public static ArrayList<Courses> GetEnrolledCourses(String ServPath, String Token, UsuarioGenerico UsuarioGen) {
        URLBuilder URL = new URLBuilder(ServPath);

        URL.addParam("wstoken", Token);
        URL.addParam("wsfunction", "core_enrol_get_users_courses");
        URL.addParam("userid", UsuarioGen.getID() + "");
        ArrayList<Courses> Cursos = new ArrayList<>();
        Courses Curso;
        Document doc = MoodleWerbServCreator.Send_XML(URL);
        NodeList Root = doc.getElementsByTagName("SINGLE");
        for (int i = 0; i < Root.getLength(); i++) {
            Node Nod = Root.item(i);
            if (Nod.getNodeType() == Node.ELEMENT_NODE) {//verifica si el nodo SINGLE es un Nodo Validp para su lectura
                Element Single = (Element) Nod;
                NodeList child = Single.getChildNodes();
                //System.out.println(Single.getNodeName());
                Curso = new Courses();
                for (int j = 0; j < child.getLength(); j++) {
                    Node Key = child.item(j);
                    if (Key.getNodeType() == Node.ELEMENT_NODE) {//verifica si los nodos consecuentes son nodos validos
                        Element keyelement = (Element) Key;

                        //System.out.println("<" + keyelement.getNodeName() + " name= " + keyelement.getAttribute("name"));
                        //flitros para nodo key
                        //if (keyelement.getAttribute("name").equals("id") || keyelement.getAttribute("name").equals("shortname") || keyelement.getAttribute("name").equals("fullname") 
                        //|| keyelement.getAttribute("name").equals("displayname") || keyelement.getAttribute("name").equals("idnumber") || keyelement.getAttribute("name").equals("lang")) {
                        NodeList valNodeList = keyelement.getChildNodes();
                        for (int k = 0; k < valNodeList.getLength(); k++) {
                            Node Value = valNodeList.item(k);
                            if (Value.getNodeType() == Node.ELEMENT_NODE) {
                                Element c = (Element) Value;
                                //System.out.println(keyelement.getAttribute("name") + " Value: " + c.getTextContent());
                                String AttribName = keyelement.getAttribute("name");
                                switch (AttribName) {
                                    case "id":
                                        Curso.setID(c.getTextContent());
                                        break;
                                    case "shortname":
                                        Curso.setNombreCorto(c.getTextContent());
                                        break;
                                    case "fullname":
                                        Curso.setNombreCompleto(c.getTextContent());
                                        break;
                                    case "displayname":
                                        Curso.setNombreMostrado(c.getTextContent());
                                        break;
                                    case "idnumber":
                                        Curso.setIDNumber(c.getTextContent());
                                        break;
                                    case "lang":
                                        Curso.setIdioma(c.getTextContent());
                                        break;
                                    case "summary":
                                        Curso.setDescripcion(c.getTextContent());
                                        break;
                                }
                            }
                        }
                        //}

                    }
                }
                if (!Curso.isNulldata()) {
                    Cursos.add(Curso);
                }

            }

        }
        if (Cursos.isEmpty()) {
            return null;
        }

        return Cursos;
    }

    public synchronized static String runtasks(final String CLI_CRON_PATH, final String CLI_CRON_PWR) {
        URLBuilder URL = new URLBuilder(CLI_CRON_PATH);
        URL.addParam("password", CLI_CRON_PWR);
        return MoodleWerbServCreator.Send_String(URL);
    }

}