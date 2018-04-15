package assign2;

import java.rmi.Remote;
import java.rmi.RemoteException;

import assign1.Simulation;
import assign1.Simulation1;
import global.Client;
import main.BeauAndersonFinalProject;
import stringProcessors.AHalloweenCommandProcessor;
import stringProcessors.HalloweenCommandProcessor;
import util.interactiveMethodInvocation.IPCMechanism;

public class RemoteCommandProcessor extends AHalloweenCommandProcessor implements RemoteCommandProcessorInterface {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private HalloweenCommandProcessor commandProcessor;
	public RemoteCommandProcessor(RMIClient client) {
		// super();
		commandProcessor = Client.getCommandProcessor();
		commandProcessor.addPropertyChangeListener(client);
	}
	
	public HalloweenCommandProcessor getCommandProcessor() {
		return commandProcessor;
	}
	
	public void processRemoteCommand(String command) throws RemoteException {
		commandProcessor.processCommand(command);
	}

	@Override
	public void remoteSetAtomic(boolean newValue) throws RemoteException {
		Client.getSingleton().atomicBroadcast(newValue);
		
	}

	@Override
	public void remoteSetIPC(IPCMechanism newValue) throws RemoteException {
		Client.getSingleton().setIPCMechanism(newValue);
		
	}
}
