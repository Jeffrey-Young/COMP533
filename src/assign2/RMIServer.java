package assign2;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;

import assignments.util.inputParameters.SimulationParametersListener;
import assignments.util.mainArgs.ServerArgsProcessor;
import inputport.nio.manager.NIOManagerFactory;
import util.interactiveMethodInvocation.ConsensusAlgorithm;
import util.interactiveMethodInvocation.IPCMechanism;
import util.trace.bean.BeanTraceUtility;
import util.trace.factories.FactoryTraceUtility;
import util.trace.port.nio.NIOTraceUtility;

import java.nio.channels.SocketChannel;
import java.rmi.NotBoundException;
import java.rmi.Remote;

public class RMIServer implements SimulationParametersListener, RMIServerInterface {
	public static final String REGISTRY_NAME = "RMI_SERVER";

	private boolean atomic;
	private boolean localProcessing;
	private Map<String, RemoteCommandProcessorInterface> clients;
	private Registry rmiRegistry;

	public RMIServer(Registry rmiRegistry) {
		this.rmiRegistry = rmiRegistry;
		clients = new HashMap<String, RemoteCommandProcessorInterface>();
		// Dynamic Invocation Params
		atomic = false;
		localProcessing = false;
	}

	public static void main(String[] args) {
		FactoryTraceUtility.setTracing();
		BeanTraceUtility.setTracing();
		NIOTraceUtility.setTracing();
		
		try {
			Registry rmiRegistry = LocateRegistry.getRegistry(ServerArgsProcessor.getRegistryHost(args));
			RMIServer server = new RMIServer(rmiRegistry);
			UnicastRemoteObject.exportObject(server, 0);
			rmiRegistry.rebind(REGISTRY_NAME, server);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void join(String name, RemoteCommandProcessorInterface callback) throws RemoteException {
		System.out.println(name + " has connected!");
		clients.put(name, callback);
	}

	@Override
	public void executeCommand(String invokerName, String command) throws RemoteException {
		System.out.println("Command: " + command + " by " + invokerName + " successfully sent to server");
		for (String proxyName : clients.keySet()) {
			if (!atomic && invokerName.equals(proxyName)) {
				continue;
			}
			// Invoke command on all remote command processors
			System.out.println("Attempting to invoke " + command + " on " + proxyName);
			try {
				RemoteCommandProcessorInterface clientProxy = (RemoteCommandProcessorInterface) rmiRegistry.lookup(proxyName);
				clientProxy.processRemoteCommand(command);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

	// Simulation Interface Methods

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
