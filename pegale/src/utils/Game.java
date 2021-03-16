package utils;

import java.io.Serializable;

public class Game implements Serializable {
    private String ipDir;
    private int tcpPort;
    private String mulDir;
    private int mulPort;
    private int cellNum;

    public Game (String ipDir, int tcpPort, String mulDir, int mulPort){
        this.cellNum=12;
        this.ipDir=ipDir;
        this.mulDir=mulDir;
        this.tcpPort=tcpPort;
        this.mulPort=mulPort;
    }


    public int getCellNum() {
        return cellNum;
    }

    public int getTcpPort() {
        return tcpPort;
    }

    public String getMulDir() {
        return mulDir;
    }

    public int getMulPort() {
        return mulPort;
    }
}
