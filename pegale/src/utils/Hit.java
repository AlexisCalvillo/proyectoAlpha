package utils;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Hit extends Remote {

    public Game gameRegister(String user) throws RemoteException;

}
