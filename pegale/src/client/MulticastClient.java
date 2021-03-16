package client;

import utils.Game;

import javax.swing.*;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.util.HashMap;

public class MulticastClient extends Thread{
    private HashMap<Integer,JRadioButton> cells;
    private Game game;
    private int acCell;
    private JButton btnReset,btnExit;
    private String user;
    private JLabel score;
    private MulticastSocket s;
    private boolean isWinner;

    public MulticastClient(HashMap<Integer, JRadioButton> cells, Game game,
                            JButton btnReset, String user, JLabel puntuacion, JButton btnExit) {
        this.cells = cells;
        this.game = game;
        this.acCell =0;
        this.btnReset=btnReset;
        this.user =user;
        this.score =puntuacion;
        this.btnExit=btnExit;
        this.s = null;
        this.isWinner =false;
    }

    public void run(){


        InetAddress grupo;
        try {
            grupo=InetAddress.getByName(game.getMulDir());
            System.out.println(game.getMulDir());
            s = new MulticastSocket(game.getMulPort());
            byte[] buffer = new byte[1000];
            DatagramPacket messageIn;

            while (!isWinner) {
                s.joinGroup(grupo);
                //System.out.println("Esperando monstruos");
                messageIn= new DatagramPacket(buffer, buffer.length);
                s.receive(messageIn);
                String mensaje =(new String(messageIn.getData())).trim();
                String arr[] = mensaje.split(" ");
                //System.out.println("mensaje: "+mensaje);
                if(arr[0].equals("Ganador")){
                    isWinner =true;
                    if(arr[1].equals(user)){
                        enviaMensaje("exito", "    ¡Felicidades has ganado el juego! \n "
                                + "\n ¿Quieres unirte a la siguiente partida?");

                    }else{
                        enviaMensaje("fracaso", "      ¡Te ha ganado "+arr[1]+"! \n "
                                + "\n ¿Quieres unirte a la siguiente partida?");

                    }
                }
                else{
                    //Mando llamar método TCP para que consteste
                    cambiaCelda(Integer.parseInt(arr[1]));
                    //respuestaTCP.enviaMensaje(usuario, arr[1]);
                }

                //Se manda llamar función de TCP para
                s.leaveGroup(grupo);
            }

        } catch (SocketException e) {
            System.out.println("Socket: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO: " + e.getMessage());
        }finally{
            if (s != null)
                s.close();
        }
    }

    public void salJuego(){
        btnExit.doClick();
    }

    public void reiniciaJuego(){
        isWinner =false;
        score.setText("0");
        btnReset.doClick();
    }

    public void cambiaCelda(int cell){
        cambiaImagen(cells.get(cell),"Vacío");
        cambiaImagen(cells.get(cell),"Monstruo");
        acCell =cell;
    }

    private void cambiaImagen(JRadioButton btn, String imagen){
        ImageIcon img = new ImageIcon(getClass().getClassLoader().getResource("imagenes/"+imagen+".png"));
        btn.setIcon(new ImageIcon(img.getImage().getScaledInstance((int)img.getIconWidth()/5, -1,
                java.awt.Image.SCALE_SMOOTH)));
    }

    public void enviaMensaje(String imagen, String mensaje ){

        ImageIcon img = new ImageIcon(getClass().getClassLoader().getResource("imagenes/"+imagen+".png"));
        ImageIcon icono=new ImageIcon(img.getImage().getScaledInstance(145, -1,
                java.awt.Image.SCALE_SMOOTH));

        if (JOptionPane.showConfirmDialog(null,mensaje, "¡El juego ha terminado!",
                JOptionPane.YES_NO_OPTION,0, icono) == JOptionPane.YES_OPTION) {
            isWinner =false;
            score.setText("-");
        } else {
            if (s != null)
                s.close();
            System.exit(0);
        }



    }
}
