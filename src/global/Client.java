package global;

import java.beans.PropertyChangeEvent;
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
import assign3.GIPCClient;
import assign3.GIPCServer;
import assign3.GIPCServerInterface;
import assignments.util.MiscAssignmentUtils;
import assignments.util.inputParameters.ASimulationParametersController;
import assignments.util.inputParameters.AnAbstractSimulationParametersBean;
import assignments.util.mainArgs.ClientArgsProcessor;
import inputport.rpc.GIPCLocateRegistry;
import inputport.rpc.GIPCRegistry;
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
import util.trace.port.rpc.rmi.RMIObjectRegistered;
import util.trace.port.rpc.rmi.RMIRegistryLocated;
import util.trace.port.rpc.rmi.RMITraceUtility;

@Tags({DistributedTags.CLIENT, DistributedTags.RMI, DistributedTags.NIO, DistributedTags.GIPC})
public class Client  extends AnAbstractSimulationParametersBean {
	

	private static Client simParams;
	private static HalloweenCommandProcessor commandProcessor;
	
	
	private static RMIClient rmiClient;
	private static NIOClient nioClient;
	private static GIPCClient gipcClient;
	private static RMIServerInterface serverProxy;

	public Client() {
		FactoryTraceUtility.setTracing();
		BeanTraceUtility.setTracing();
		NIOTraceUtility.setTracing();
		RMITraceUtility.setTracing();
		ConsensusTraceUtility.setTracing();
		ThreadDelayed.enablePrint();
		GIPCRPCTraceUtility.setTracing();		
		
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
	
	@Override
	public void simulationCommand(String aCommand) {
		ProposalMade.newCase(this, CommunicationStateNames.COMMAND, -1, aCommand);
		// TODO: this method gets called in headless mode, need to send this command to RMI, NIO, and GIPC clients
		nioClient.getNIOSender().propertyChange(new PropertyChangeEvent(this, "InputString", null, aCommand));
		rmiClient.propertyChange(new PropertyChangeEvent(this, "InputString", null, aCommand));
		gipcClient.propertyChange(new PropertyChangeEvent(this, "InputString", null, aCommand));
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
		FactoryTraceUtility.setTracing();
		BeanTraceUtility.setTracing();
		NIOTraceUtility.setTracing();
		RMITraceUtility.setTracing();
		ConsensusTraceUtility.setTracing();
		ThreadDelayed.enablePrint();
		GIPCRPCTraceUtility.setTracing();
		
		args = ClientArgsProcessor.removeEmpty(args);
		MiscAssignmentUtils.setHeadless(ClientArgsProcessor.getHeadless(args));
		//MiscAssignmentUtils.setHeadless(true);
		simParams = new Client();

		// GIPC

		try {
			String name = ClientArgsProcessor.getClientName(args);
			GIPCRegistry gipcRegistry= GIPCLocateRegistry.getRegistry(ClientArgsProcessor.getRegistryHost(args), ClientArgsProcessor.getGIPCPort(args), name);
			GIPCServerInterface serverProxy = (GIPCServerInterface) gipcRegistry.lookup(GIPCServerInterface.class, GIPCServer.GIPC_SERVER_NAME);
			gipcClient = new GIPCClient(serverProxy, name);
			// export
			gipcRegistry.rebind(gipcClient.getName(), gipcClient.getCommandProcessorProxy());

			serverProxy.join(gipcClient.getName(), gipcClient.getCommandProcessorProxy());

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		// RMI
				try {
					Registry rmiRegistry = LocateRegistry.getRegistry(ClientArgsProcessor.getRegistryPort(args));
					RMIRegistryLocated.newCase(Client.getSingleton(), ClientArgsProcessor.getRegistryHost(args), ClientArgsProcessor.getRegistryPort(args), rmiRegistry);
					serverProxy = (RMIServerInterface) rmiRegistry.lookup(RMIServer.RMI_SERVER_NAME);
					RMIObjectLookedUp.newCase(Client.getSingleton(), serverProxy, serverProxy.toString(), rmiRegistry);
					rmiClient = new RMIClient(serverProxy);
					//export
					UnicastRemoteObject.exportObject(rmiClient.getCommandProcessorProxy(), 0);
					RMIObjectRegistered.newCase(RMIClient.class, rmiClient.getName(), rmiClient, rmiRegistry);
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

	public static void setNIOClient(NIOClient aClient) {
		nioClient = aClient;
	}
}
