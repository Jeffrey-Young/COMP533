package assign2;

import java.rmi.Remote;
import java.rmi.RemoteException;

import util.interactiveMethodInvocation.IPCMechanism;

public interface RMIServerInterface extends Remote {
	public void join(String name, RemoteCommandProcessorInterface callback) throws RemoteException;
	public void executeCommand(String clientName, String command) throws RemoteException;
	public void broadcastAtomic(Boolean newValue) throws RemoteException;
	public void broadcastIPC(IPCMechanism newValue) throws RemoteException;
}
