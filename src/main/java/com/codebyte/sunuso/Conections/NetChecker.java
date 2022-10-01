package com.codebyte.sunuso.Conections;

import com.codebyte.sunuso.Resources.TextManagement;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class NetChecker {
    private String Message = "";

    public boolean ping(String Address, int Timeout) {
        try {
            InetAddress NetAdd = InetAddress.getByName(Address);
            return NetAdd.isReachable(Timeout);
        } catch (UnknownHostException ex) {
            try {
                this.Message = "Direccide Ping Inv(" + Address + ")";
                InetAddress NetAdd = InetAddress.getByName("127.0.0.1");
                NetAdd.isReachable(Timeout);
            } catch (UnknownHostException ex1) {
                this.Message = "Direccide Ping Inv(127.0.0.1)";
            } catch (IOException ex1) {
                this.Message = "Problema de Adaptador de red: 127.0.0.1" + TextManagement.NewLine() + "no se puede realizar ping al adaptador de red local";
            }
        } catch (IOException ex) {
            this.Message = "No se puede relaizar Ping a (" + Address + ")";
        }
        return false;
    }

    public String getMessage() {
        return this.Message;
    }
}
