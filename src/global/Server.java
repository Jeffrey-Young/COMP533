package global;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import assign1.NIOServer;
import assign2.RMIServer;
import assignments.util.inputParameters.ASimulationParametersController;
import assignments.util.mainArgs.ServerArgsProcessor;
import util.annotations.Tags;
import util.interactiveMethodInvocation.SimulationParametersController;
import util.tags.DistributedTags;
import util.trace.bean.BeanTraceUtility;
import util.trace.factories.FactoryTraceUtility;
import util.trace.misc.ThreadDelayed;
import util.trace.port.consensus.ConsensusTraceUtility;
import util.trace.port.nio.NIOTraceUtility;
import util.trace.port.rpc.rmi.RMITraceUtility;

@Tags({DistributedTags.SERVER, DistributedTags.RMI, DistributedTags.NIO})
public class Server {

	
	public static void main(String[] args) {
		args = ServerArgsProcessor.removeEmpty(args);
		FactoryTraceUtility.setTracing();
		NIOTraceUtility.setTracing();
		BeanTraceUtility.setTracing();// not really needed, but does not hurt
		RMITraceUtility.setTracing();
		ConsensusTraceUtility.setTracing();
		ThreadDelayed.enablePrint();

		// RMI
		try {
			Registry rmiRegistry = LocateRegistry.getRegistry(ServerArgsProcessor.getRegistryHost(args));
			RMIServer server = new RMIServer(rmiRegistry);
			UnicastRemoteObject.exportObject(server, 0);
			rmiRegistry.rebind(RMIServer.REGISTRY_NAME, server);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// NIO
		NIOServer aServer = new NIOServer();
		SimulationParametersController aSimulationParametersController = new ASimulationParametersController();
		aSimulationParametersController.addSimulationParameterListener(SimulationParameters.getSingleton());
		aServer.initialize(ServerArgsProcessor.getServerPort(args));
		aSimulationParametersController.processCommands();
	}
}
