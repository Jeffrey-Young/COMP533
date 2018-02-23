package assign2;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMIServerInterface extends Remote {
	public void join(String name, RemoteCommandProcessorInterface callback) throws RemoteException;
	public void executeCommand(String clientName, String command) throws RemoteException;
}
