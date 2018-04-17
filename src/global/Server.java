package global;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import assign1.NIOServer;
import assign1.Simulation;
import assign1.Simulation1;
import assign2.RMIServer;
import assign3.GIPCRegistryAndServer;
import assignments.util.inputParameters.ASimulationParametersController;
import assignments.util.inputParameters.AnAbstractSimulationParametersBean;
import assignments.util.mainArgs.ServerArgsProcessor;
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
import util.trace.port.rpc.gipc.GIPCRPCTraceUtility;
import util.trace.port.rpc.rmi.RMITraceUtility;

@Tags({DistributedTags.SERVER, DistributedTags.RMI, DistributedTags.NIO})
public class Server extends AnAbstractSimulationParametersBean {

	private static Server simParams;
	private static HalloweenCommandProcessor commandProcessor;
	
	private static RMIServer rmiServer;
	private static GIPCRegistryAndServer gipcRegistryAndServer;
	
	
	public static void main(String[] args) {
		args = ServerArgsProcessor.removeEmpty(args);
		FactoryTraceUtility.setTracing();
		NIOTraceUtility.setTracing();
		BeanTraceUtility.setTracing();// not really needed, but does not hurt
		RMITraceUtility.setTracing();
		ConsensusTraceUtility.setTracing();
		ThreadDelayed.enablePrint();
		GIPCRPCTraceUtility.setTracing();
		
		// GIPC
		gipcRegistryAndServer = new GIPCRegistryAndServer(args);

		// RMI
		try {
			Registry rmiRegistry = LocateRegistry.getRegistry(ServerArgsProcessor.getRegistryPort(args));
			rmiServer = new RMIServer(rmiRegistry);
			UnicastRemoteObject.exportObject(rmiServer, 0);
			rmiRegistry.rebind(RMIServer.REGISTRY_NAME, rmiServer);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// NIO
		NIOServer aServer = new NIOServer();
		SimulationParametersController aSimulationParametersController = new ASimulationParametersController();
		aSimulationParametersController.addSimulationParameterListener(Server.getSingleton());
		aServer.initialize(ServerArgsProcessor.getServerPort(args));
		aSimulationParametersController.processCommands();
	}
	
	public static Server getSingleton() {
		if (simParams == null) {
			simParams = new Server();
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
			try {
				rmiServer.broadcastAtomic(newValue);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		atomicBroadcast = newValue;
	}
	
	@Override
	public synchronized void setIPCMechanism(IPCMechanism newValue) {
		if (this.broadcastMetaState) {
			try {
				rmiServer.broadcastIPC(newValue);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		ipcMechanism = newValue;
	}
}
