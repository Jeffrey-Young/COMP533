package assign2;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import assignments.util.inputParameters.SimulationParametersListener;
import assignments.util.mainArgs.ServerArgsProcessor;
import util.interactiveMethodInvocation.ConsensusAlgorithm;
import util.interactiveMethodInvocation.IPCMechanism;
import java.rmi.Remote;

public class RMIServer implements SimulationParametersListener, Remote {

	private boolean atomic;
	private boolean localProcessing;
	
	
	
	

	
	public static void main(String[] args) {
		try {
			Registry rmiRegistry = LocateRegistry.getRegistry(ServerArgsProcessor.getRegistryHost(args));
			RMIServer server = new RMIServer();
			UnicastRemoteObject.exportObject(server, 0);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	// Interface Methods
	
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
