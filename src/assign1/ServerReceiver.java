package assign1;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import inputport.nio.manager.listeners.SocketChannelReadListener;

public class ServerReceiver implements SocketChannelReadListener {

	@Override
	public void socketChannelRead(SocketChannel aSocketChannel,
			ByteBuffer aMessage, int aLength) {
		String command = new String(aMessage.array(), aMessage.position(),
				aLength);
		System.out.println("Server receives: " + command);
	}

}
