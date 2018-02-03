package assign1;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import inputport.nio.manager.listeners.SocketChannelWriteListener;

public class ServerSender implements SocketChannelWriteListener {

	@Override
	public void written(SocketChannel socketChannel, ByteBuffer theWriteBuffer, int sendId) {
		// TODO Auto-generated method stub
		
	}
	
}
