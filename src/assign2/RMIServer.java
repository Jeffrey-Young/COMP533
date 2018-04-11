package assign2;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;

import assignments.util.inputParameters.SimulationParametersListener;
import assignments.util.mainArgs.ServerArgsProcessor;
import global.SimulationParameters;
import inputport.nio.manager.NIOManagerFactory;
import util.annotations.Tags;
import util.interactiveMethodInvocation.ConsensusAlgorithm;
import util.interactiveMethodInvocation.IPCMechanism;
import util.misc.ThreadSupport;
import util.trace.bean.BeanTraceUtility;
import util.trace.factories.FactoryTraceUtility;
import util.trace.misc.ThreadDelayed;
import util.trace.port.consensus.ConsensusTraceUtility;
import util.trace.port.nio.NIOTraceUtility;
import util.trace.port.rpc.rmi.RMITraceUtility;

import java.nio.channels.SocketChannel;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import util.tags.DistributedTags;

@Tags({DistributedTags.SERVER, DistributedTags.RMI, DistributedTags.NIO})
public class RMIServer implements  RMIServerInterface {
	public static final String REGISTRY_NAME = "RMI_SERVER";

	private Map<String, RemoteCommandProcessorInterface> clients;
	private Registry rmiRegistry;

	public RMIServer(Registry rmiRegistry) {
		this.rmiRegistry = rmiRegistry;
		clients = new HashMap<String, RemoteCommandProcessorInterface>();
		// Dynamic Invocation Params
		SimulationParameters.getSingleton().setAtomicBroadcast(false);
		SimulationParameters.getSingleton().localProcessingOnly(false);
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
		if (!SimulationParameters.getSingleton().getIPCMechanism().equals(IPCMechanism.RMI)) {
			return;
		}
		System.out.println("Command: " + command + " by " + invokerName + " successfully sent to server");
		for (String proxyName : clients.keySet()) {
			if (!SimulationParameters.getSingleton().isAtomicBroadcast() && invokerName.equals(proxyName)) {
				continue;
			}
			// Invoke command on all remote command processors
			System.out.println("Attempting to invoke " + command + " on " + proxyName);
			try {
				ThreadSupport.sleep(SimulationParameters.getSingleton().getDelay());
				RemoteCommandProcessorInterface clientProxy = (RemoteCommandProcessorInterface) rmiRegistry.lookup(proxyName);
				clientProxy.processRemoteCommand(command);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

	public void quit(int aCode) {
		System.exit(aCode);
	}
}
