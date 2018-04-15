package assign2;

import java.rmi.Remote;
import java.rmi.RemoteException;

import util.interactiveMethodInvocation.IPCMechanism;

public interface RemoteCommandProcessorInterface extends Remote {
	public void processRemoteCommand(String command) throws RemoteException;
	public void remoteSetAtomic(boolean newValue) throws RemoteException;
	public void remoteSetIPC(IPCMechanism newValue) throws RemoteException;
}
