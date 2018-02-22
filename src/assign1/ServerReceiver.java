package assign1;

import java.nio.ByteBuffer;
import assignments.util.MiscAssignmentUtils;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;

import inputport.nio.manager.listeners.SocketChannelReadListener;

public class ServerReceiver implements SocketChannelReadListener {
	
	private ArrayBlockingQueue<Map<SocketChannel, ByteBuffer>> readQueue;
	
	public ServerReceiver(ArrayBlockingQueue<Map<SocketChannel, ByteBuffer>> readQueue) {
		this.readQueue = readQueue;

	}

	@Override
	public void socketChannelRead(SocketChannel aSocketChannel,
			ByteBuffer aMessage, int aLength) {
		String command = new String(aMessage.array(), aMessage.position(),
				aLength);
		try {
			Map<SocketChannel, ByteBuffer> aMap = new HashMap<SocketChannel, ByteBuffer>();
			aMap.put(aSocketChannel, MiscAssignmentUtils.deepDuplicate(aMessage));
			readQueue.add(aMap);	
		} catch (IllegalStateException e) {
			System.err.print("Error, Read queue is full!");
		}
		// System.out.println("Server receives: " + command);
	}

}
