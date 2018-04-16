package assign3;

import assignments.util.mainArgs.RegistryArgsProcessor;
import examples.mvc.rmi.duplex.ADistributedInheritingRMICounter;
import inputport.rpc.GIPCLocateRegistry;
import inputport.rpc.GIPCRegistry;
import port.ATracingConnectionListener;

public class GIPCRegistryandServer {
	protected static GIPCRegistry gipcRegistry;
	protected static void main(String[] args) {
		gipcRegistry = GIPCLocateRegistry.createRegistry(RegistryArgsProcessor.getRegistryPort(args));
		counter = new ADistributedInheritingRMICounter();			
		gipcRegistry.rebind(COUNTER_NAME, counter);	
		gipcRegistry.getInputPort().addConnectionListener(new ATracingConnectionListener(gipcRegistry.getInputPort()));
	}
}
