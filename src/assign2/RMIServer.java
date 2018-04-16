package assign2;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;

import assignments.util.inputParameters.SimulationParametersListener;
import assignments.util.mainArgs.ServerArgsProcessor;
import global.Server;
import inputport.nio.manager.NIOManagerFactory;
import util.annotations.Tags;
import util.interactiveMethodInvocation.ConsensusAlgorithm;
import util.interactiveMethodInvocation.IPCMechanism;
import util.misc.ThreadSupport;
import util.trace.bean.BeanTraceUtility;
import util.trace.factories.FactoryTraceUtility;
import util.trace.misc.ThreadDelayed;
import util.trace.port.consensus.ConsensusTraceUtility;
import util.trace.port.consensus.ProposalAcceptRequestSent;
import util.trace.port.consensus.RemoteProposeRequestReceived;
import util.trace.port.consensus.communication.CommunicationStateNames;
import util.trace.port.nio.NIOTraceUtility;
import util.trace.port.rpc.rmi.RMITraceUtility;

import java.nio.channels.SocketChannel;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import util.tags.DistributedTags;

public class RMIServer implements  RMIServerInterface {
	public static final String REGISTRY_NAME = "RMI_SERVER";

	private Map<String, RemoteCommandProcessorInterface> clients;
	private Registry rmiRegistry;

	public RMIServer(Registry rmiRegistry) {
		this.rmiRegistry = rmiRegistry;
		clients = new HashMap<String, RemoteCommandProcessorInterface>();
		// Dynamic Invocation Params
		Server.getSingleton().setAtomicBroadcast(false);
		Server.getSingleton().localProcessingOnly(false);
	}

	public static void main(String[] args) {
		FactoryTraceUtility.setTracing();
		BeanTraceUtility.setTracing();
		NIOTraceUtility.setTracing();
		RMITraceUtility.setTracing();
		ConsensusTraceUtility.setTracing();
		ThreadDelayed.enablePrint();

		
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
		RemoteProposeRequestReceived.newCase(this, CommunicationStateNames.COMMAND, -1, command);
		System.out.println("Command: " + command + " by " + invokerName + " successfully sent to server");
		for (String proxyName : clients.keySet()) {
			if (!Server.getSingleton().isAtomicBroadcast() && invokerName.equals(proxyName)) {
				continue;
			}
			// Invoke command on all remote command processors
			System.out.println("Attempting to invoke " + command + " on " + proxyName);
			try {
				ThreadSupport.sleep(Server.getSingleton().getDelay());
				RemoteCommandProcessorInterface clientProxy = (RemoteCommandProcessorInterface) rmiRegistry.lookup(proxyName);
				boolean clientAccept = clientProxy.accept();
				ProposalAcceptRequestSent.newCase(this, CommunicationStateNames.COMMAND, -1, command);
				// TODO: RESTART HERE
				//TODO check IPC MECH and send via GIPC or RMI depending on value
				clientProxy.processRemoteCommand(command);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

	public void quit(int aCode) {
		System.exit(aCode);
	}

	@Override
	public void broadcastAtomic(Boolean newValue) throws RemoteException {
		for (String proxyName : clients.keySet()) {
			RemoteCommandProcessorInterface clientProxy = null;
			try {
				clientProxy = (RemoteCommandProcessorInterface) rmiRegistry.lookup(proxyName);
			} catch (NotBoundException e) {
				e.printStackTrace();
			}
			clientProxy.remoteSetAtomic(newValue); //WHAT IS THE STRING I SHOULD SEND
		}
	}

	@Override
	public void broadcastIPC(IPCMechanism newValue) throws RemoteException {
		for (String proxyName : clients.keySet()) {
			RemoteCommandProcessorInterface clientProxy = null;
			try {
				clientProxy = (RemoteCommandProcessorInterface) rmiRegistry.lookup(proxyName);
			} catch (NotBoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			clientProxy.remoteSetIPC(newValue); //WHAT IS THE STRING I SHOULD SEND
		}		
	}
}
