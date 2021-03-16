package client;

import utils.Game;
import utils.Hit;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;


public class HitGui extends javax.swing.JFrame{
    private String user;
    private int score;
    private Game game;
    private Hit hit;
    private HashMap<Integer,JRadioButton> cells;
    private Socket s;
    private DataInputStream in;
    private DataOutputStream out;


    private JPanel panel1;
    private JRadioButton rb1;
    private JRadioButton rb2;
    private JRadioButton rb3;
    private JRadioButton rb4;
    private JRadioButton rb5;
    private JRadioButton rb6;
    private JRadioButton rb7;
    private JRadioButton rb8;
    private JRadioButton rb9;
    private JRadioButton rb10;
    private JRadioButton rb11;
    private JRadioButton rb12;
    private JButton reiniciarButton;
    private JButton salirBtn;
    private JLabel scoreLbl;
    private JLabel lblS;


    public HitGui() {
        System.setProperty("java.security.policy","file:./src/cliente/client.policy");

        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }

        cells = new HashMap<Integer, JRadioButton>();
        this.user="";
        this.score=0;


        //Inicializa componentes
        //Hash
        //Inicializa interfaz
        rb1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                msj(user,""+0);
            }
        });
        rb2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                msj(user,""+1);
            }
        });
        rb3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                msj(user,""+2);
            }
        });
        rb4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                msj(user,""+3);
            }
        });
        rb5.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                msj(user,""+4);
            }
        });
        rb6.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                msj(user,""+5);
            }
        });
        rb7.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                msj(user,""+6);
            }
        });
        rb8.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                msj(user,""+7);
            }
        });
        rb9.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                msj(user,""+8);
            }
        });
        rb10.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                msj(user,""+9);
            }
        });
        rb11.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                msj(user,""+10);
            }
        });
        rb12.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                msj(user,""+11);
            }
        });
    }

    public void multicast(){
        MulticastClient mc =new MulticastClient(cells,game,reiniciarButton,user,scoreLbl,salirBtn);
        mc.start();
    }


    public void registra(){

        try {
            Registry registry;
            this.hit = null;

            registry = LocateRegistry.getRegistry("148.205.133.161");
            hit = (Hit) registry.lookup("PegaleAlMonstruo");

            this.game = connect();
            System.out.println(user+" "+game.getIpDir());

            while(game.getIpDir().equals("Error")){
                JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), "Este nombre de usuario ya está registrado.", "Error en el registro", JOptionPane.ERROR_MESSAGE);
                this.game = connect();
            }

            lblS.setText(user);
            scoreLbl.setText(""+0);

        } catch (RemoteException ex) {
            Logger.getLogger(HitGui.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NotBoundException ex) {
            Logger.getLogger(HitGui.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public Game connect(){
        Game res = new Game("Error",-1, "Error", -1);

        try {
            ImageIcon img = new ImageIcon(getClass().getClassLoader().getResource("imagenes/monstruo.png"));
            ImageIcon icono=new ImageIcon(img.getImage().getScaledInstance(145, -1,
                    java.awt.Image.SCALE_SMOOTH));
            this.user=(String) JOptionPane.showInputDialog(JOptionPane.getRootFrame(),
                    "Nombre del jugador",
                    "¡Pégale al monstruo!: Registro jugador",
                    JOptionPane.INFORMATION_MESSAGE, icono, null, ""
            );
            res = hit.gameRegister(user);
        } catch (RemoteException ex) {
            Logger.getLogger(HitGui.class.getName()).log(Level.SEVERE, null, ex);
        }

        return res;
    }

    public void iniHash(){
        cells.put(0, rb1);
        cells.put(1, rb2);
        cells.put(2, rb3);
        cells.put(3, rb4);
        cells.put(4, rb5);
        cells.put(5, rb6);
        cells.put(6, rb7);
        cells.put(7, rb8);
        cells.put(8, rb9);
        cells.put(9, rb10);
        cells.put(10, rb11);
        cells.put(11, rb12);
    }

    public void iniInterface(){
        for (JRadioButton btn : cells.values())
            image(btn, "vacio");
    }

    private void image(JRadioButton btn, String image){
        ImageIcon img = new ImageIcon(getClass().getClassLoader().getResource("imagenes/"+image+".png"));
        btn.setIcon(new ImageIcon(img.getImage().getScaledInstance((int)img.getIconWidth()/5, -1,
                java.awt.Image.SCALE_SMOOTH)));
    }

    public void msj(String user, String res){
        try {
            //Creación del tubo para la conexión. Aplano carretera
            this.s = new Socket(game.getIpDir(), game.getTcpPort());
            this.in = new DataInputStream( s.getInputStream());
            this.out = new DataOutputStream( s.getOutputStream());

            out.writeUTF(user+" "+res+" "+"fin");
            //Recibo la respuesta del servidor
            String mensaje = in.readUTF();
            String arr[] = mensaje.split(" ");
            //System.out.println("Puntuacion de "+usuario+": "+ arr[0]);
            scoreLbl.setText(arr[0]);

            //Cierra conexión
            if(s!=null)
                try {
                    s.close(); //Cerramos socket para que no quede la conexión abierta
                } catch (IOException e){
                    System.out.println("close:"+e.getMessage());
                }
        } catch (IOException ex) {
            //Logger.getLogger(ClienteRespuesta.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                //Veo si me puedo registrar
                System.setProperty("java.net.preferIPv4Stack", "true");

                HitGui client= new HitGui();
                client.registra();
                client.multicast();
                client.setVisible(true);
            }
        });
    }
}
