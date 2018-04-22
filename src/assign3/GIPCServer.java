package assign3;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;

import assign2.RemoteCommandProcessorInterface;
import assignments.util.mainArgs.RegistryArgsProcessor;
import assignments.util.mainArgs.ServerArgsProcessor;
import consensus.ProposalFeedbackKind;
import examples.mvc.rmi.duplex.ADistributedInheritingRMICounter;
import global.Server;
import inputport.rpc.GIPCLocateRegistry;
import inputport.rpc.GIPCRegistry;
import port.ATracingConnectionListener;
import util.interactiveMethodInvocation.ConsensusAlgorithm;
import util.interactiveMethodInvocation.IPCMechanism;
import util.misc.ThreadSupport;
import util.trace.port.consensus.ProposalAcceptRequestSent;
import util.trace.port.consensus.ProposalAcceptedNotificationReceived;
import util.trace.port.consensus.ProposalLearnedNotificationSent;
import util.trace.port.consensus.RemoteProposeRequestReceived;
import util.trace.port.consensus.communication.CommunicationStateNames;
import util.trace.port.rpc.rmi.RMIObjectLookedUp;

public class GIPCServer implements GIPCServerInterface {
	public static final String GIPC_SERVER_NAME = "GIPC_SERVER";
	private GIPCRegistry gipcRegistry;
	private Map<String, RemoteCommandProcessorInterface> clients;

	public GIPCServer(GIPCRegistry gipcRegistry) {
		this.gipcRegistry = gipcRegistry;
		// counter = new ADistributedInheritingRMICounter();
		// gipcRegistry.rebind(COUNTER_NAME, counter);
		clients = new HashMap<String, RemoteCommandProcessorInterface>();
	}

	public GIPCRegistry getGIPCRegistry() {
		return this.gipcRegistry;
	}
	@Override
	public void join(String name, RemoteCommandProcessorInterface callback) throws RemoteException {
		System.out.println(name + " has connected via GIPC!");
		clients.put(name, callback);
	}

	@Override
	public void executeCommand(String invokerName, String command) throws RemoteException {
		if (Server.getSingleton().getConsensusAlgorithm() == ConsensusAlgorithm.CENTRALIZED_SYNCHRONOUS) {
			RemoteProposeRequestReceived.newCase(this, CommunicationStateNames.COMMAND, -1, command);
			boolean accept = true;
			for (String proxyName : clients.keySet()) {
				RemoteCommandProcessorInterface clientProxy = null;
				clientProxy = (RemoteCommandProcessorInterface) gipcRegistry
						.lookup(RemoteCommandProcessorInterface.class, proxyName);

				ProposalAcceptRequestSent.newCase(this, CommunicationStateNames.COMMAND, -1, command);
				boolean clientAccept = clientProxy.receiveProposal(CommunicationStateNames.COMMAND, command);
				ProposalAcceptedNotificationReceived.newCase(this, CommunicationStateNames.COMMAND, -1, command,
						ProposalFeedbackKind.SUCCESS);
				if (!clientAccept) {
					accept = false;
				}
			}
			if (!accept) {
				// send blank if someone rejects
				command = "";
			}
		}
		System.out.println("Command: " + command + " by " + invokerName + " successfully sent to server");
		for (String proxyName : clients.keySet()) {
			if (!Server.getSingleton().isAtomicBroadcast() && invokerName.equals(proxyName)) {
				continue;
			}
			// Invoke command on all remote command processors
			System.out.println("Attempting to invoke " + command + " on " + proxyName);
			try {
				ThreadSupport.sleep(Server.getSingleton().getDelay());
				RemoteCommandProcessorInterface clientProxy = (RemoteCommandProcessorInterface) gipcRegistry
						.lookup(RemoteCommandProcessorInterface.class, proxyName);
				ProposalLearnedNotificationSent.newCase(this, CommunicationStateNames.COMMAND, -1, command);
				// TODO check IPC MECH and send via GIPC or RMI depending on
				// value
				clientProxy.processRemoteCommand(command);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

	@Override
	public void broadcastAtomic(Boolean newValue) throws RemoteException {
		Server.rmiServer.broadcastAtomic(newValue);
	}

	@Override
	public void broadcastIPC(IPCMechanism newValue) throws RemoteException {
		Server.rmiServer.broadcastIPC(newValue);

	}

	public static void main(String[] args) {
		GIPCRegistry gipcRegistry = GIPCLocateRegistry.createRegistry(ServerArgsProcessor.getGIPCServerPort(args));
		GIPCServer gipcServer = new GIPCServer(gipcRegistry);
		gipcRegistry.rebind(GIPC_SERVER_NAME, gipcServer);
		gipcRegistry.getInputPort().addConnectionListener(new ATracingConnectionListener(gipcRegistry.getInputPort()));

	}
}
