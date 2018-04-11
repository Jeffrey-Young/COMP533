package global;

import assign1.Simulation;
import assign1.Simulation1;
import assign2.RemoteCommandProcessor;
import assignments.util.inputParameters.AnAbstractSimulationParametersBean;
import main.BeauAndersonFinalProject;
import stringProcessors.HalloweenCommandProcessor;

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

}
