package assign2;

import java.rmi.Remote;
import java.rmi.RemoteException;

import util.interactiveMethodInvocation.IPCMechanism;
import util.trace.port.consensus.communication.CommunicationStateNames;

public interface RemoteCommandProcessorInterface extends Remote {
	public void processRemoteCommand(String command) throws RemoteException;
	public void remoteSetAtomic(boolean newValue) throws RemoteException;
	public void remoteSetIPC(IPCMechanism newValue) throws RemoteException;
	public boolean receiveProposal(String communicationState, Object value) throws RemoteException;
}
