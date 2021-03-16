package server;

import utils.Game;
import utils.Match;
import utils.Monster;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.util.Random;

public class MulticastServer extends Thread {
    private Match match;
    private int cells;
    private double seconds;
    private int port;
    private MulticastSocket socketMulticast;
    private InetAddress address;

    public MulticastServer (Match match, Game game) {
        try {
            this.match=match;
            this.cells=game.getCellNum();
            this.seconds=1.5;
            this.port=game.getMulPort();
            this.socketMulticast = new MulticastSocket(port);
            this.address=InetAddress.getByName(game.getMulDir());

            socketMulticast.joinGroup(address);
        } catch (IOException ex) {
            System.out.println("ServidorM: Problemas para unirse al grupo.");
        }
    }

    public void run(){
        try {
            while(true){
                while (match.getWinner().equals("")) {

                    Random rand = new Random();
                    int celda=rand.nextInt(cells);
                    Monster monstruo = new Monster(celda);

                    match.setAcCell(celda);

                    String mensaje = monstruo.toString()+" "+"fin";
                    byte[] m = mensaje.getBytes();
                    DatagramPacket messageOut = new DatagramPacket(m, m.length, address, port);
                    socketMulticast.send(messageOut);

                    try {
                        Thread.sleep((int)seconds*1000); //Sólo para que no me lo esté mandando a cada rato
                    } catch (InterruptedException ex) {
                        System.out.println("ServidorM: Problemas para dormir el hilo.");
                        //Logger.getLogger(ServidorMulticast.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

                //Avisa del ganador
                //Enviar monstruo con el tiempo que esperará
                String mensaje = "Ganador "+match.getWinner()+" fin";
                byte[] m = mensaje.getBytes();
                DatagramPacket messageOut = new DatagramPacket(m, m.length, address, port);
                socketMulticast.send(messageOut);
                System.out.println("ServidorM: "+mensaje);
                match.scoreTable();

                //Espera tantito y reinicia el juego
                try {
                    Thread.sleep(2*1000); //Sólo para que no me lo esté mandando a cada rato
                    System.out.println("ServidorM: Se ha reiniciado");
                } catch (InterruptedException ex) {
                    System.out.println("ServidorM: Problemas para dormir el hilo.");
                    //Logger.getLogger(ServidorMulticast.class.getName()).log(Level.SEVERE, null, ex);
                }
                match.reset();

            }

        } catch (SocketException e) {
            System.out.println("Socket: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO: " + e.getMessage());
        } finally {
            if (socketMulticast != null)
                socketMulticast.close();
        }

    }
}
