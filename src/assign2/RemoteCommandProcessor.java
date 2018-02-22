package assign2;

import java.rmi.Remote;

import assign1.Simulation;
import assign1.Simulation1;
import main.BeauAndersonFinalProject;
import stringProcessors.AHalloweenCommandProcessor;
import stringProcessors.HalloweenCommandProcessor;

public class RemoteCommandProcessor extends AHalloweenCommandProcessor implements Remote {
	private HalloweenCommandProcessor commandProcessor;
	public RemoteCommandProcessor() {
		super();
		commandProcessor = BeauAndersonFinalProject.createSimulation(
				Simulation1.SIMULATION1_PREFIX,
				Simulation1.SIMULATION1_X_OFFSET, 
				Simulation.SIMULATION_Y_OFFSET, 
				Simulation.SIMULATION_WIDTH, 
				Simulation.SIMULATION_HEIGHT, 
				Simulation1.SIMULATION1_X_OFFSET, 
				Simulation.SIMULATION_Y_OFFSET);

	}
	
	public HalloweenCommandProcessor getCommandProcessor() {
		return commandProcessor;
	}
}
