package assign3;

import java.rmi.RemoteException;

import assign2.RemoteCommandProcessorInterface;
import assignments.util.mainArgs.RegistryArgsProcessor;
import assignments.util.mainArgs.ServerArgsProcessor;
import examples.mvc.rmi.duplex.ADistributedInheritingRMICounter;
import inputport.rpc.GIPCLocateRegistry;
import inputport.rpc.GIPCRegistry;
import port.ATracingConnectionListener;
import util.interactiveMethodInvocation.IPCMechanism;

public class GIPCRegistryAndServer implements GIPCServerInterface {
	protected static GIPCRegistry gipcRegistry;
	
	
	public GIPCRegistryAndServer(String[] args) {
		gipcRegistry = GIPCLocateRegistry.createRegistry(ServerArgsProcessor.getGIPCServerPort(args));
		//counter = new ADistributedInheritingRMICounter();			
		// gipcRegistry.rebind(COUNTER_NAME, counter);	
		gipcRegistry.getInputPort().addConnectionListener(new ATracingConnectionListener(gipcRegistry.getInputPort()));
	}
	

	@Override
	public void join(String name, RemoteCommandProcessorInterface callback) throws RemoteException {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void executeCommand(String clientName, String command) throws RemoteException {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void broadcastAtomic(Boolean newValue) throws RemoteException {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void broadcastIPC(IPCMechanism newValue) throws RemoteException {
		// TODO Auto-generated method stub
		
	}
}
