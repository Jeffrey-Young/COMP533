package assign1;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ArrayBlockingQueue;

import assignments.util.MiscAssignmentUtils;
import global.Client;
import inputport.nio.manager.listeners.SocketChannelReadListener;
import util.interactiveMethodInvocation.IPCMechanism;

public class ClientReceiver implements SocketChannelReadListener{

	private ArrayBlockingQueue<ByteBuffer> readQueue;
	
	public ClientReceiver(ArrayBlockingQueue<ByteBuffer> readQueue) {
		this.readQueue = readQueue;

	}


	@Override
	public void socketChannelRead(SocketChannel aSocketChannel, ByteBuffer aMessage, int aLength) {
		if (Client.getSingleton().getIPCMechanism().equals(IPCMechanism.NIO)) {
			return;
		}
		String command = new String(aMessage.array(), aMessage.position(),
				aLength);
		//System.out.println("CLIENT SOCKET CHANNEL READ");
		try {
			readQueue.add(MiscAssignmentUtils.deepDuplicate(aMessage));	
		} catch (IllegalStateException e) {
			System.err.print("Error, Read queue is full!");
		}
		//System.out.println("Client receives: " + command);		
	}

}
