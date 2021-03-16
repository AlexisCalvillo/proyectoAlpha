package server;

import utils.Game;
import utils.Hit;
import utils.Match;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Server implements Hit {

    private static Game game;
    private static int rmiPort;
    private static Match match;
    private static String name;


    public Server(String ipDir, int tcpPort, String mulDir, int mulPort){
        this.game=new Game(ipDir,tcpPort,mulDir,mulPort);
        this.name="Pégale al coso";
        this.rmiPort=rmiPort;
        this.match = new Match();
    }

    @Override
    public Game gameRegister(String user) throws RemoteException {
        if(!match.addUser(user)){
            System.out.println("Servidor: Reingreso de "+user);
        }
        else{
            System.out.println("Servidor: Registro exitosos de "+user);
        }

        return this.game;
    }

    public static void main(String[] args) throws IOException {
        System.setProperty("java.rmi.server.hostname", " 172.26.144.1");
        System.setProperty("java.net.preferIPv4Stack", "true");

        int mulPort          = 6868;
        int tcpPort          = 6869;
        String ipDir    = "148.205.133.161";
        String mulDir    = "225.228.225.228";

        Server engine = new Server(ipDir, tcpPort, mulDir,mulPort);

        System.setProperty("java.security.policy","file:./src/servidor/server.policy");

        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }

        try {
            LocateRegistry.createRegistry(rmiPort);
            Hit stub =(Hit) UnicastRemoteObject.exportObject(engine, 0);
            Registry registry = LocateRegistry.getRegistry();
            registry.rebind(name, stub);
            System.out.println("Servidor: Se ha desplegado correctamente el servicio JavaRMI");
        } catch (Exception e) {
            System.err.println("Error en servidor");
            e.printStackTrace();
        }

        MulticastServer mulS = new MulticastServer(match, engine.game);
        TCPServer       servidorT = new TCPServer(match, engine.game);

        mulS.start();
        servidorT.start();

    }

}
