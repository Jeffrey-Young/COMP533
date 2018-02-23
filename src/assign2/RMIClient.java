package assign2;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.nio.ByteBuffer;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import assignments.util.inputParameters.SimulationParametersListener;
import assignments.util.mainArgs.ClientArgsProcessor;
import examples.mvc.rmi.duplex.DistributedRMICounter;
import inputport.nio.manager.NIOManagerFactory;
import util.interactiveMethodInvocation.ConsensusAlgorithm;
import util.interactiveMethodInvocation.IPCMechanism;
import util.trace.bean.BeanTraceUtility;
import util.trace.factories.FactoryTraceUtility;
import util.trace.port.nio.NIOTraceUtility;

public class RMIClient implements SimulationParametersListener, PropertyChangeListener {

	private boolean atomic;
	private boolean localProcessing;
	private RemoteCommandProcessorInterface commandProcessorProxy;
	private String name;
	
	// not the actual server object but a proxy for it via the registry
	private RMIServerInterface serverProxy;

	public RMIClient(RMIServerInterface serverProxy) {
		this.serverProxy = serverProxy;
		commandProcessorProxy = new RemoteCommandProcessor(this);
		// Dynamic Invocation Params
		atomic = false;
		localProcessing = false;
		name = Math.random() + "";
	}

	@Override
	public void propertyChange(PropertyChangeEvent anEvent) {
		if (!anEvent.getPropertyName().equals("InputString"))
			return;
		String newCommand = (String) anEvent.getNewValue();
		System.out.println("Client has command:" + newCommand);
		if (!localProcessing) {
			if (!atomic) {
				try {
					commandProcessorProxy.processRemoteCommand(newCommand);
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			// send to server
			try {
				serverProxy.executeCommand(this.name, newCommand);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		} else {
			try {
				commandProcessorProxy.processRemoteCommand(newCommand);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		FactoryTraceUtility.setTracing();
		BeanTraceUtility.setTracing();
		NIOTraceUtility.setTracing();
		
		try {
			Registry rmiRegistry = LocateRegistry.getRegistry(ClientArgsProcessor.getRegistryHost(args));
			RMIServerInterface serverProxy = (RMIServerInterface) rmiRegistry.lookup(RMIServer.REGISTRY_NAME);
			RMIClient client = new RMIClient(serverProxy);
			//export
			UnicastRemoteObject.exportObject(client.getCommandProcessorProxy(), 0);
			rmiRegistry.rebind(client.getName(), client.getCommandProcessorProxy());
			
			serverProxy.join(client.getName(), client.getCommandProcessorProxy());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getName() {
		return name;
	}
	
	public RemoteCommandProcessorInterface getCommandProcessorProxy() {
		return commandProcessorProxy;
	}
	
	// INTERFACE METHODS
	@Override
	public void atomicBroadcast(boolean newValue) {
		atomic = newValue;
	}

	@Override
	public void experimentInput() {
		// TODO Auto-generated method stub

	}

	@Override
	public void localProcessingOnly(boolean newValue) {
		localProcessing = newValue;

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
