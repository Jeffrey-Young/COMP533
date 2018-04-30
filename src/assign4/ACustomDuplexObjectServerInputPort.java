package assign4;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import inputport.datacomm.AReceiveRegistrarAndNotifier;
import inputport.datacomm.ReceiveRegistrarAndNotifier;
import inputport.datacomm.duplex.DuplexServerInputPort;
import inputport.datacomm.duplex.object.ADuplexObjectServerInputPort;
import inputport.datacomm.duplex.object.explicitreceive.AReceiveReturnMessage;
import inputport.datacomm.duplex.object.explicitreceive.ReceiveReturnMessage;
import util.trace.port.objects.ReceivedMessageQueueCreated;
import util.trace.port.rpc.ReceivedCallDequeued;

public class ACustomDuplexObjectServerInputPort extends ADuplexObjectServerInputPort {

	private static Map<String, CountingBlockingQueue<Object>> ports;
	public static ArrayBlockingQueue<Object> anonPort;

	public ACustomDuplexObjectServerInputPort(DuplexServerInputPort<ByteBuffer> aBBDuplexServerInputPort) {
		super(aBBDuplexServerInputPort);
		ports = new HashMap<String, CountingBlockingQueue<Object>>();
		anonPort = new ArrayBlockingQueue<Object>(4096);
	}

	/**
	 * Changes the notifier that invokes receive listeners
	 */
	@Override
	protected ReceiveRegistrarAndNotifier<Object> createReceiveRegistrarAndNotifier() {
		// TODO I think this should be my notifier
		return new ACustomDuplexReceiveNotifier();
	}

	/**
	 * Overrides the unimplemented paramaterized receive method
	 */
	@Override
	public ReceiveReturnMessage<Object> receive(String aSource) {
		System.err.println("Receive started");
		
		BlockingQueue<Object> portToReceiveOn = null;
		
		if (aSource == null || aSource.equals("*")) {
			portToReceiveOn = anonPort;
		} else {
			CountingBlockingQueue<Object> clientPort = ports.get(aSource);
			if (clientPort == null) {
				clientPort = new CountingBlockingQueue<Object>(4096);
				// ReceivedMessageQueueCreated.newCase(this, clientPort);
				ports.put(aSource, clientPort);
			}
			portToReceiveOn = clientPort;
		}
		
		Object retVal = null;
		try {
			retVal = portToReceiveOn.take();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ReceivedCallDequeued.newCase(this, portToReceiveOn, retVal);
		System.out.println(aSource + "<-" + retVal);
		return new AReceiveReturnMessage<Object>(aSource, retVal);
	}

	public ReceiveReturnMessage<Object> receive() {
		String previous = getSender();
		if (previous == null) {
			previous = "*";
		}
		return receive(previous);

	}
	
	public static Map<String, CountingBlockingQueue<Object>> getPorts() {
		return ports;
	}
	
	public static BlockingQueue<Object> getAnonymousPort() {
		return anonPort;
	}

}
