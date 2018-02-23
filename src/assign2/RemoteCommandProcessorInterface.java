package assign2;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteCommandProcessorInterface extends Remote {
	public void processRemoteCommand(String command) throws RemoteException;
}
