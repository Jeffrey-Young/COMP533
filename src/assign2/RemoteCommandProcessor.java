package assign2;

import java.rmi.Remote;
import java.rmi.RemoteException;

import assign1.Simulation;
import assign1.Simulation1;
import global.SimulationParameters;
import main.BeauAndersonFinalProject;
import stringProcessors.AHalloweenCommandProcessor;
import stringProcessors.HalloweenCommandProcessor;

public class RemoteCommandProcessor extends AHalloweenCommandProcessor implements RemoteCommandProcessorInterface {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private HalloweenCommandProcessor commandProcessor;
	public RemoteCommandProcessor(RMIClient client) {
		// super();
		commandProcessor = SimulationParameters.getCommandProcessor();
		commandProcessor.addPropertyChangeListener(client);
	}
	
	public HalloweenCommandProcessor getCommandProcessor() {
		return commandProcessor;
	}
	
	public void processRemoteCommand(String command) throws RemoteException {
		commandProcessor.processCommand(command);
	}
}
