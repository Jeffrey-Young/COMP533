package assign1;

import java.nio.ByteBuffer;
import assignments.util.MiscAssignmentUtils;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ArrayBlockingQueue;

import inputport.nio.manager.listeners.SocketChannelReadListener;

public class ServerReceiver implements SocketChannelReadListener {
	
	private ArrayBlockingQueue<ByteBuffer> readQueue;
	
	public ServerReceiver(ArrayBlockingQueue<ByteBuffer> readQueue) {
		this.readQueue = readQueue;

	}

	@Override
	public void socketChannelRead(SocketChannel aSocketChannel,
			ByteBuffer aMessage, int aLength) {
		String command = new String(aMessage.array(), aMessage.position(),
				aLength);
		try {
			readQueue.add(MiscAssignmentUtils.deepDuplicate(aMessage));	
		} catch (IllegalStateException e) {
			System.err.print("Error, Read queue is full!");
		}
		System.out.println("Server receives: " + command);
	}

}
