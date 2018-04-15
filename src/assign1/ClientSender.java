package assign1;

import inputport.nio.manager.NIOManagerFactory;
import util.interactiveMethodInvocation.IPCMechanism;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import global.Client;

/**
 * Listens to model changes and sends them to the connected server through the
 * NIO manager.
 * 
 * @author Dewan
 *
 */
public class ClientSender implements PropertyChangeListener {
	// ObservableNIOManager nioManager;
	SocketChannel socketChannel;
	String clientName;
	NIOClient client;

	public ClientSender(SocketChannel aSocketChannel, String aClientName, NIOClient client) {
		// nioManager = anNIOManager;
		socketChannel = aSocketChannel;
		clientName = aClientName;
		this.client = client;
	}

	@Override
	public void propertyChange(PropertyChangeEvent anEvent) {
		// TODO change to send messages to server
		if (!Client.getSingleton().getIPCMechanism().equals(IPCMechanism.NIO)) {
			return;
		}
		if (!anEvent.getPropertyName().equals("InputString"))
			return;
		String newCommand = (String) anEvent.getNewValue();
		// System.out.println("Client has command:" + newCommand);
		if (!Client.getSingleton().isLocalProcessingOnly()) {
			if (!Client.getSingleton().isAtomicBroadcast()) {
				client.commandProcessor.processCommand(newCommand);
			} 
			ByteBuffer aMeaningByteBuffer = ByteBuffer.wrap((newCommand.getBytes()));
			NIOManagerFactory.getSingleton().write(socketChannel, aMeaningByteBuffer);
		} else {
			client.commandProcessor.processCommand(newCommand);
		}
	}

}
