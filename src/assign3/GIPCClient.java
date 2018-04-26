package assign3;

import java.beans.PropertyChangeEvent;
import util.trace.port.consensus.*;
import util.tags.DistributedTags;
import java.beans.PropertyChangeListener;
import java.nio.ByteBuffer;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import assign2.RemoteCommandProcessor;
import assign2.RemoteCommandProcessorInterface;
import assignments.util.inputParameters.SimulationParametersListener;
import assignments.util.mainArgs.ClientArgsProcessor;
import examples.mvc.rmi.duplex.DistributedRMICounter;
import global.Client;
import inputport.nio.manager.NIOManagerFactory;
import inputport.rpc.GIPCLocateRegistry;
import inputport.rpc.GIPCRegistry;
import util.annotations.Tags;
import util.interactiveMethodInvocation.ConsensusAlgorithm;
import util.interactiveMethodInvocation.IPCMechanism;
import util.misc.ThreadSupport;
import util.trace.bean.BeanTraceUtility;
import util.trace.bean.NotifiedPropertyChangeEvent;
import util.trace.factories.FactoryTraceUtility;
import util.trace.misc.ThreadDelayed;
import util.trace.port.consensus.ConsensusTraceUtility;
import util.trace.port.consensus.communication.CommunicationStateNames;
import util.trace.port.nio.NIOTraceUtility;
import util.trace.port.rpc.rmi.RMIObjectRegistered;
import util.trace.port.rpc.rmi.RMIRegistryLocated;
import util.trace.port.rpc.rmi.RMITraceUtility;

public class GIPCClient implements PropertyChangeListener {

	private RemoteCommandProcessorInterface commandProcessorProxy;
	private String name;

	// not the actual server object but a proxy for it via the registry
	private GIPCServerInterface serverProxy;

	public GIPCClient(GIPCServerInterface serverProxy, String name) {
		this.serverProxy = serverProxy;
		commandProcessorProxy = new RemoteCommandProcessor(this);
		// Dynamic Invocation Params
		// Client.getSingleton().setAtomicBroadcast(false);
		// Client.getSingleton().localProcessingOnly(false);
		this.name = name;
	}

	@Override
	public void propertyChange(PropertyChangeEvent anEvent) {
		if (!Client.getSingleton().getIPCMechanism().equals(IPCMechanism.GIPC)) {
			return;
		}
		if (!anEvent.getPropertyName().equals("InputString"))
			return;
		NotifiedPropertyChangeEvent.newCase(this, anEvent, new PropertyChangeListener[] {});
		String newCommand = (String) anEvent.getNewValue();
		System.out.println("GIPC Client has command:" + newCommand);
		if (!Client.getSingleton().isLocalProcessingOnly()) {
			if (!Client.getSingleton().isAtomicBroadcast()) {
				try {
					commandProcessorProxy.processRemoteCommand(newCommand);
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			// send to server
			try {
				ThreadSupport.sleep(Client.getSingleton().getDelay());
				if (Client.getSingleton().isAtomicBroadcast()) {
					ProposalMade.newCase(this, CommunicationStateNames.COMMAND, -1, newCommand);

				}
				RemoteProposeRequestSent.newCase(this, CommunicationStateNames.COMMAND, -1, newCommand);
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
		// NIOTraceUtility.setTracing();
		RMITraceUtility.setTracing();
		ConsensusTraceUtility.setTracing();
		ThreadDelayed.enablePrint();

		try {
			String name = Math.random() + "";
			GIPCRegistry gipcRegistry= GIPCLocateRegistry.getRegistry(ClientArgsProcessor.getRegistryHost(args), ClientArgsProcessor.getGIPCPort(args), name);
			GIPCServerInterface serverProxy = (GIPCServerInterface) gipcRegistry.lookup(GIPCServerInterface.class, GIPCServer.GIPC_SERVER_NAME);
			GIPCClient client = new GIPCClient(serverProxy, name);
			// export
			gipcRegistry.rebind(client.getName(), client.getCommandProcessorProxy());

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

	// @Override
	public void experimentInput() {
		// TODO Auto-generated method stub

	}

	// @Override
	public void quit(int aCode) {
		System.exit(aCode);

	}

	// @Override
	public void simulationCommand(String aCommand) {
		// TODO Auto-generated method stub

	}

}
