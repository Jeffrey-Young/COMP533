package assign4;

import inputport.datacomm.AReceiveRegistrarAndNotifier;
import util.trace.port.objects.ReceivedMessageQueueCreated;
import util.trace.port.objects.ReceivedMessageQueued;

public class ACustomDuplexReceiveNotifier extends AReceiveRegistrarAndNotifier {

	@Override
	public void notifyPortReceive(String aSource, Object aMessage) {
		super.notifyPortReceive(aSource, aMessage);
		CountingBlockingQueue<Object> port = ACustomDuplexObjectServerInputPort.getPorts().get(aSource);
		if (port == null) {
			port = new CountingBlockingQueue<Object>(4096);
			ReceivedMessageQueueCreated.newCase(this, port, "Created port for: " + aSource);
			
			// put on both queues
			ACustomDuplexObjectServerInputPort.getPorts().put(aSource, port);
			ACustomDuplexObjectServerInputPort.getAnonymousPort().offer(aMessage);
			
		} else if (port.getBlocked() <= 0) {
			ACustomDuplexObjectServerInputPort.getAnonymousPort().offer(aMessage);
			ReceivedMessageQueued.newCase(this, ACustomDuplexObjectServerInputPort.getAnonymousPort(), aMessage);
			
		} else {
			port.offer(aMessage);
			ReceivedMessageQueued.newCase(this, port, aMessage);
			ACustomDuplexObjectServerInputPort.getPorts().put(aSource, port);
		}
	}
}
