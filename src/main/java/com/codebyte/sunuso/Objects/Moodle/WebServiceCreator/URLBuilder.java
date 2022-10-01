/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codebyte.sunuso.Objects.Moodle.WebServiceCreator;

import com.codebyte.sunuso.Objects.Moodle.Resources.ParamItem;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Arturo Meza Garcia
 */
public class URLBuilder {

    private String RootService;
    private ArrayList<ParamItem> Params;

    public URLBuilder() {
        this.Params = new ArrayList<>();
    }

    public URLBuilder(String RootService) {
        this.RootService = RootService;
        this.Params = new ArrayList<>();
    }

    /**
     * <p>
     * Agrega un parametro al link raiz para ser transfromado en un link
     * completo para consumo de una api</p>
     *
     * @param Name Nombre del atributo
     * @param Value Valor del atributo
     *
     * @return boolean
     * <p>
     * true si los parametros no estan vacios false si hay un parametro o dos
     * vacios</p>
     */
    public boolean addParam(String Name, String Value) {
        if (Name.length() == 0 || Value.length() == 0) {
            return false;
        }
        return Params.add(new ParamItem(Name, Value));
    }

    /**
     * <p>
     * Elimina un parametro en una posicion especifica</p>
     *
     * @param index parametro en posicion [0,1,.....]
     * @return boolean true eliminacion existosa
     */
    public boolean deleteParam(int index) {
        if (index < 0 || Params.isEmpty()) {
            return false;
        } else {
            Params.remove(index);
            return true;
        }
    }

    /**
     * <p>
     * Elimina el ultimo parametro agregado</p>
     *
     * @return boolean true
     */
    public boolean deleteLastParam() {
        if (Params.isEmpty()) {
            return false;
        } else {
            Params.remove(Params.size() - 1);
            return true;
        }
    }

    /**
     * <p>
     * Este método construye la URL a ser consumido por un webservice </p>
     *
     * @return String <p>Construye el URL para crear el consumo web</p>
     * <p>Example: http://www.example/service.php<strong>?</strong>valuename1=value<strong>&</strong>valuename2=value<p>
     */
    public String buildUrlConsumtion() {
        if (getRootService().isEmpty()) {
            return null;
        }
        String direction = "";
        
        if (getRootService().endsWith("?")) {
            direction += getRootService();
        } else {
            direction += getRootService() + "?";
        }
        int Counter = Params.size() - 1;
        for (ParamItem Param : Params) {
            direction += Param.getName() + "=" + URLEncoder.encode(Param.getValue(), StandardCharsets.UTF_8);;
            if (Counter > 0) {
                direction += "&";
                Counter--;
            }
        }
        
        return direction;
    }

    public String getRootService() {
        return this.RootService;
    }

    public void setRootService(String RootService) {
        this.RootService = RootService;
    }

    public List getParams() {
        return Params;
    }

    public void setParams(ArrayList<ParamItem> Params) {
        this.Params = Params;
    }

}