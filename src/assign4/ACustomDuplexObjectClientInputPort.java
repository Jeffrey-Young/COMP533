package assign4;

import java.nio.ByteBuffer;
import java.util.concurrent.ArrayBlockingQueue;

import inputport.datacomm.duplex.DuplexClientInputPort;
import inputport.datacomm.duplex.object.ADuplexObjectClientInputPort;
import inputport.datacomm.duplex.object.DuplexObjectInputPortSelector;
import inputport.datacomm.duplex.object.explicitreceive.AReceiveReturnMessage;
import inputport.datacomm.duplex.object.explicitreceive.ReceiveReturnMessage;
import util.trace.port.rpc.ReceivedCallDequeued;
/**
 * Overrides interesting methods of the default object client input port
 */
public class ACustomDuplexObjectClientInputPort extends ADuplexObjectClientInputPort {
	
	private static ArrayBlockingQueue<Object> port;

	public ACustomDuplexObjectClientInputPort(
			DuplexClientInputPort<ByteBuffer> aBBClientInputPort) {
		super(aBBClientInputPort);
		port = new ArrayBlockingQueue<Object>(4096);
	}
	/**
	 * Simply traces the sends dispatched to the sueprclass, does nothing more
	 */
	@Override
	public void send(String aDestination, Object aMessage) {
		System.out.println (aDestination + "<-" + aMessage);
		super.send(aDestination, aMessage);	
	}
	/**
	 * Overrides the unimplemented paramaterized receive method
	 */
	@Override
	public ReceiveReturnMessage<Object> receive(String aSource) {
		System.err.println("Receive started");
		Object retVal = null;
		try {
			retVal = port.take();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ReceivedCallDequeued.newCase(this, port, retVal);
		System.out.println (aSource + "<-" + retVal);
		return new AReceiveReturnMessage<Object>(aSource, retVal);
	}
	
	// parameterless receive
	public ReceiveReturnMessage<Object> receive() {
		return receive(super.getSender());
	}
	
	public static ArrayBlockingQueue<Object> getPort() {
		return port;
	}
}
