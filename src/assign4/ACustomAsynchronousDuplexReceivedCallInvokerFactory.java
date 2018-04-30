package assign4;

import examples.gipc.counter.customization.ACustomDuplexReceivedCallInvokerFactory;
import inputport.datacomm.duplex.DuplexInputPort;
import inputport.rpc.RPCRegistry;
import inputport.rpc.duplex.AnAsynchronousSingleThreadDuplexReceivedCallInvoker;
import inputport.rpc.duplex.DuplexReceivedCallInvoker;
import inputport.rpc.duplex.LocalRemoteReferenceTranslator;

public class ACustomAsynchronousDuplexReceivedCallInvokerFactory extends ACustomDuplexReceivedCallInvokerFactory {

	@Override
	public DuplexReceivedCallInvoker createDuplexReceivedCallInvoker(LocalRemoteReferenceTranslator aRemoteHandler,
			DuplexInputPort<Object> aReplier, RPCRegistry anRPCRegistry) {
		return new AnAsynchronousSingleThreadDuplexReceivedCallInvoker(
				super.createDuplexReceivedCallInvoker(aRemoteHandler, aReplier, anRPCRegistry));
	}
}
