package assign2;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import assignments.util.inputParameters.SimulationParametersListener;
import assignments.util.mainArgs.ClientArgsProcessor;
import util.interactiveMethodInvocation.ConsensusAlgorithm;
import util.interactiveMethodInvocation.IPCMechanism;

public class RMIClient implements SimulationParametersListener {

	
	public RMIClient() {
		RemoteCommandProcessor commandProcessor = new RemoteCommandProcessor();
	}
	
	public static void main (String[] args) {	
		try {
			Registry rmiRegistry = LocateRegistry.getRegistry(ClientArgsProcessor.getRegistryHost(args));
			RMIClient client = new RMIClient();

		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	
	
	// INTERFACE METHODS
	@Override
	public void atomicBroadcast(boolean newValue) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void experimentInput() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void localProcessingOnly(boolean newValue) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void ipcMechanism(IPCMechanism newValue) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void broadcastBroadcastMode(boolean newValue) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void waitForBroadcastConsensus(boolean newValue) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void broadcastIPCMechanism(boolean newValue) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void waitForIPCMechanismConsensus(boolean newValue) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void consensusAlgorithm(ConsensusAlgorithm newValue) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void quit(int aCode) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void simulationCommand(String aCommand) {
		// TODO Auto-generated method stub
		
	}
	
}
