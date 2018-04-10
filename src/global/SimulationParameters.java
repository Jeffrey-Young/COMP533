package global;

import assignments.util.inputParameters.AnAbstractSimulationParametersBean;

public class SimulationParameters extends AnAbstractSimulationParametersBean {
	
	private static SimulationParameters simParams;
	
	
	public static SimulationParameters getSingleton() {
		if (simParams == null) {
			simParams = new SimulationParameters();
		}
		return simParams;
	}

}
