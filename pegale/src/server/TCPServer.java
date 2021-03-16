package server;

import utils.Game;
import utils.Match;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer extends Thread {
    private static Match match;
    private static int port;
    private static ServerSocket socketEntrada;

    public TCPServer (Match match, Game game){
        this.match =match;
        this.port=game.getTcpPort();
        try {
            socketEntrada = new ServerSocket(port);
        } catch (IOException ex) {
            System.out.println("ServidorT: Problemas con el puerto del servidor.");
            //Logger.getLogger(ServidorTCP.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    @Override
    public void run(){
        while(true){
            try {
                Socket socketCliente = socketEntrada.accept();
                Connection c = new Connection(socketCliente, match);
                c.start();
                System.out.println("ServidorT: Esperando puntos de los usuarios...");
            } catch (IOException ex) {
                System.out.println("ServidorT: Problemas para establecer conexión con el usuario.");
                //Logger.getLogger(ServidorTCP.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}

class Connection extends Thread {
    Match match;
    DataInputStream in;
    DataOutputStream out;
    Socket socketCliente;

    public Connection (Socket socketCliente, Match match) {
        try {
            this.socketCliente = socketCliente;
            in = new DataInputStream(socketCliente.getInputStream());
            out =new DataOutputStream(socketCliente.getOutputStream());
            this.match=match;
        } catch(IOException e)  {
            System.out.println("Connection:"+e.getMessage());
        }
    }

    @Override
    public void run(){
        try {

            String mensaje = in.readUTF();
            String arr[] = mensaje.split(" ");
            String usuario = arr[0];
            int celda = Integer.parseInt(arr[1]);


            if(celda == match.getAcCell()){
                match.addPoint(usuario);
                System.out.println("ServidorT: Se le agrego punto a "+usuario+". Ahora cuenta con: "+match.userScore(usuario));
            }
            else{
                System.out.println("Casiii, pero no");
            }
            out.writeUTF(""+match.userScore(usuario)+" "+"fin");


        } catch (IOException ex) {
            System.out.println("ServidorT: Problemas en las recepción/salida de mensaje en TCP");
            //Logger.getLogger(Connection.class.getName()).log(Level.SEVERE, null, ex);
        }finally {
            if(socketCliente!=null){
                try {
                    socketCliente.close();
                    //System.out.println("ServidorT: Se cerró conexión");
                } catch (IOException e){
                    System.out.println("close:"+e.getMessage());
                }
            }
        }
    }
}
