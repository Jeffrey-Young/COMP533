package global;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import assign1.NIOClient;
import assign1.Simulation;
import assign1.Simulation1;
import assign2.RMIClient;
import assign2.RMIServer;
import assign2.RMIServerInterface;
import assignments.util.MiscAssignmentUtils;
import assignments.util.inputParameters.ASimulationParametersController;
import assignments.util.inputParameters.AnAbstractSimulationParametersBean;
import assignments.util.mainArgs.ClientArgsProcessor;
import main.BeauAndersonFinalProject;
import stringProcessors.HalloweenCommandProcessor;
import util.annotations.Tags;
import util.interactiveMethodInvocation.IPCMechanism;
import util.interactiveMethodInvocation.SimulationParametersController;
import util.tags.DistributedTags;
import util.trace.bean.BeanTraceUtility;
import util.trace.factories.FactoryTraceUtility;
import util.trace.misc.ThreadDelayed;
import util.trace.port.consensus.ConsensusTraceUtility;
import util.trace.port.consensus.ProposalMade;
import util.trace.port.consensus.ProposedStateSet;
import util.trace.port.consensus.RemoteProposeRequestSent;
import util.trace.port.consensus.communication.CommunicationStateNames;
import util.trace.port.nio.NIOTraceUtility;
import util.trace.port.rpc.gipc.GIPCRPCTraceUtility;
import util.trace.port.rpc.rmi.RMIObjectLookedUp;
import util.trace.port.rpc.rmi.RMIRegistryLocated;
import util.trace.port.rpc.rmi.RMITraceUtility;

@Tags({DistributedTags.CLIENT, DistributedTags.RMI, DistributedTags.NIO})
public class Client  extends AnAbstractSimulationParametersBean {
	

	private static Client simParams;
	private static HalloweenCommandProcessor commandProcessor;
	
	
	private static RMIClient rmiClient;
	private static RMIServerInterface serverProxy;

	public Client(String[] args) {
		FactoryTraceUtility.setTracing();
		BeanTraceUtility.setTracing();
		NIOTraceUtility.setTracing();
		RMITraceUtility.setTracing();
		ConsensusTraceUtility.setTracing();
		ThreadDelayed.enablePrint();
		GIPCRPCTraceUtility.setTracing();

		args = ClientArgsProcessor.removeEmpty(args);
		
		
	}	
	
	public static Client getSingleton() {
//		if (simParams == null) {
//			simParams = new Client();
//		}
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
		if (this.broadcastMetaState) {
			ProposalMade.newCase(this, CommunicationStateNames.BROADCAST_MODE, -1, newValue);
			try {
				serverProxy.broadcastAtomic(newValue);
				RemoteProposeRequestSent.newCase(this, CommunicationStateNames.BROADCAST_MODE, -1, newValue);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
		ProposedStateSet.newCase(this, CommunicationStateNames.BROADCAST_MODE, -1, newValue);
		atomicBroadcast = newValue;
	}
	
	@Override
	public synchronized void setIPCMechanism(IPCMechanism newValue) {
		if (this.broadcastMetaState) {
			try {
				serverProxy.broadcastIPC(newValue);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		ProposedStateSet.newCase(this, CommunicationStateNames.IPC_MECHANISM, -1, newValue);
		ipcMechanism = newValue;
	}
	
	public synchronized void setAtomicBroadcastAfterConsensus(Boolean newValue) {
		ProposedStateSet.newCase(this, CommunicationStateNames.BROADCAST_MODE, -1, newValue);
		atomicBroadcast = newValue == null ? atomicBroadcast : newValue;
	}
	
	public synchronized void setIPCMechanismAfterConsensus(IPCMechanism newValue) {
		ProposedStateSet.newCase(this, CommunicationStateNames.IPC_MECHANISM, -1, newValue);
		ipcMechanism = newValue == null ? ipcMechanism : newValue;
	}
	
	
	public static void main(String[] args) {
		MiscAssignmentUtils.setHeadless(ClientArgsProcessor.getHeadless(args));
		simParams = new Client(args);
		
		// RMI
				try {
					Registry rmiRegistry = LocateRegistry.getRegistry(ClientArgsProcessor.getRegistryPort(args));
					RMIRegistryLocated.newCase(Client.getSingleton(), ClientArgsProcessor.getRegistryHost(args), ClientArgsProcessor.getRegistryPort(args), rmiRegistry);
					serverProxy = (RMIServerInterface) rmiRegistry.lookup(RMIServer.REGISTRY_NAME);
					RMIObjectLookedUp.newCase(Client.getSingleton(), serverProxy, serverProxy.toString(), rmiRegistry);
					rmiClient = new RMIClient(serverProxy);
					//export
					UnicastRemoteObject.exportObject(rmiClient.getCommandProcessorProxy(), 0);
					rmiRegistry.rebind(rmiClient.getName(), rmiClient.getCommandProcessorProxy());
					
					serverProxy.join(rmiClient.getName(), rmiClient.getCommandProcessorProxy());

				} catch (Exception e) {
					e.printStackTrace();
				}
				
				//NIO
				SimulationParametersController aSimulationParametersController = 
						new ASimulationParametersController();
				NIOClient.launchClient(ClientArgsProcessor.getServerHost(args),
						ClientArgsProcessor.getServerPort(args),
						ClientArgsProcessor.getClientName(args), aSimulationParametersController);
	}
}
