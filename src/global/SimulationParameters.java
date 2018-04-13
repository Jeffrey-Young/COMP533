package global;

import assign1.Simulation;
import assign1.Simulation1;
import assign2.RemoteCommandProcessor;
import assignments.util.inputParameters.AnAbstractSimulationParametersBean;
import main.BeauAndersonFinalProject;
import stringProcessors.HalloweenCommandProcessor;
import util.trace.port.consensus.ProposedStateSet;
import util.trace.port.consensus.communication.CommunicationStateNames;

public class SimulationParameters extends AnAbstractSimulationParametersBean {
	
	private static SimulationParameters simParams;
	private static HalloweenCommandProcessor commandProcessor;
	
	
	public static SimulationParameters getSingleton() {
		if (simParams == null) {
			simParams = new SimulationParameters();
		}
		return simParams;
	}
	
	public static HalloweenCommandProcessor getCommandProcessor() {
		if (commandProcessor == null) {
			commandProcessor = BeauAndersonFinalProject.createSimulation(
					Simulation1.SIMULATION1_PREFIX,
					Simulation1.SIMULATION1_X_OFFSET, 
					Simulation.SIMULATION_Y_OFFSET, 
					Simulation.SIMULATION_WIDTH, 
					Simulation.SIMULATION_HEIGHT, 
					Simulation1.SIMULATION1_X_OFFSET, 
					Simulation.SIMULATION_Y_OFFSET);
		}
		return commandProcessor;
	}
	
	@Override
	public synchronized void setAtomicBroadcast(Boolean newValue) {
		ProposedStateSet.newCase(this, CommunicationStateNames.BROADCAST_MODE, -1, newValue);
		if (this.broadcastMetaState) {
			// TODO: Broadcast, need a way to surface this to whichever thing this singleton is attached to and pass message
		}
		atomicBroadcast = newValue;
	}

}
