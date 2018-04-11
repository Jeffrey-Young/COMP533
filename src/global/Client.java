package global;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import assign1.NIOClient;
import assign2.RMIClient;
import assign2.RMIServer;
import assign2.RMIServerInterface;
import assignments.util.MiscAssignmentUtils;
import assignments.util.inputParameters.ASimulationParametersController;
import assignments.util.mainArgs.ClientArgsProcessor;
import util.annotations.Tags;
import util.interactiveMethodInvocation.SimulationParametersController;
import util.tags.DistributedTags;
import util.trace.bean.BeanTraceUtility;
import util.trace.factories.FactoryTraceUtility;
import util.trace.misc.ThreadDelayed;
import util.trace.port.consensus.ConsensusTraceUtility;
import util.trace.port.nio.NIOTraceUtility;
import util.trace.port.rpc.rmi.RMITraceUtility;

@Tags({DistributedTags.CLIENT, DistributedTags.RMI, DistributedTags.NIO})
public class Client {

	public Client(String[] args) {
		FactoryTraceUtility.setTracing();
		BeanTraceUtility.setTracing();
		NIOTraceUtility.setTracing();
		RMITraceUtility.setTracing();
		ConsensusTraceUtility.setTracing();
		ThreadDelayed.enablePrint();

		args = ClientArgsProcessor.removeEmpty(args);
		
		// RMI
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
		
		//NIO
		MiscAssignmentUtils.setHeadless(ClientArgsProcessor.getHeadless(args));
		SimulationParametersController aSimulationParametersController = 
				new ASimulationParametersController();
		NIOClient.launchClient(ClientArgsProcessor.getServerHost(args),
				ClientArgsProcessor.getServerPort(args),
				ClientArgsProcessor.getClientName(args), aSimulationParametersController);
	}	
	
	public static void main(String[] args) {
		Client aClient = new Client(args);
	}
}
