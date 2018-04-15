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
import util.trace.port.consensus.ProposedStateSet;
import util.trace.port.consensus.communication.CommunicationStateNames;
import util.trace.port.nio.NIOTraceUtility;
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
		ProposedStateSet.newCase(this, CommunicationStateNames.BROADCAST_MODE, -1, newValue);
		if (this.broadcastMetaState) {
			// TODO: Broadcast, need a way to surface this to whichever thing this singleton is attached to and pass message
			try {
				serverProxy.broadcastAtomic(newValue);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
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
		ipcMechanism = newValue;
	}
	
	
	public static void main(String[] args) {
		simParams = new Client(args);
		
		// RMI
				try {
					Registry rmiRegistry = LocateRegistry.getRegistry(ClientArgsProcessor.getRegistryPort(args));
					serverProxy = (RMIServerInterface) rmiRegistry.lookup(RMIServer.REGISTRY_NAME);
					rmiClient = new RMIClient(serverProxy);
					//export
					UnicastRemoteObject.exportObject(rmiClient.getCommandProcessorProxy(), 0);
					rmiRegistry.rebind(rmiClient.getName(), rmiClient.getCommandProcessorProxy());
					
					serverProxy.join(rmiClient.getName(), rmiClient.getCommandProcessorProxy());

				} catch (Exception e) {
					e.printStackTrace();
				}
				
				//NIO
				MiscAssignmentUtils.setHeadless(ClientArgsProcessor.getHeadless(args));
				SimulationParametersController aSimulationParametersController = 
						new ASimulationParametersController();
				NIOClient.launchClient(ClientArgsProcessor.getServerHost(args),
						ClientArgsProcessor.getServerPort(args),
						ClientArgsProcessor.getClientName(args), aSimulationParametersController);
	}
}
