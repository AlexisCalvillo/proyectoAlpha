package utils;

import java.util.HashMap;

public class Match
{

    private static HashMap<String,Integer> board;
    private final static int max = 7; //Máximo de puntos para ganar
    private static int acCell;
    private static String winner;

    public Match(){
        this.board = new HashMap<String, Integer>();
        acCell = -1;
        this.winner="";
    }


    public static String getWinner() {
        return winner;
    }

    synchronized public static void setWinner(String winner) {
        Match.winner = winner;
    }

    public boolean addUser(String user){
        boolean res =false;
        if(!exist(user)) {
            board.put(user, 0);
            res = true;
        }
        return res;
    }

    public static void setAcCell(int acCell) {
        Match.acCell = acCell;
    }

    public void addPoint(String user){
        int acScore=board.containsKey(user) ?board.get(user):-1;//Regresa puntuación si existe o -1 si no existe;
        if(acScore!=-1)
            board.put(user,acScore+1);
        if(acScore == max)
            setWinner(user);
    }

    public int userScore(String user){
        return board.containsKey(user)?board.get(user):-1;
    }

    public void reset(){
        this.board =new HashMap<String, Integer>();
        this.acCell= -1;
        this.winner = "";
    }

    public boolean exist(String usuario){
        return board.containsKey(usuario);
    }

    public void scoreTable(){
        System.out.println("---------------------------");
        System.out.println("Usuario: \t Puntuacion");
        System.out.println("---------------------------");
        for (String usuario : board.keySet())
            System.out.println(usuario+": \t "+board.get(usuario));
    }

    public static int getAcCell() {
        return acCell;
    }
}
