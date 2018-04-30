package assign4.tests.part1;

import inputport.datacomm.duplex.object.explicitreceive.ReceiveReturnMessage;
import examples.gipc.counter.customization.ACustomServerConnectionListener;
import examples.gipc.counter.customization.CustomServerConnectionListener;
import examples.gipc.counter.customization.FactorySetterFactory;
import examples.gipc.counter.layers.AMultiLayerCounterServer;

public class Part1Server extends AMultiLayerCounterServer {
	public static final long POLLING_TIME = 1000;
	public static final int NUM_RECEIVE_CLIENTS = 2; // number of clients from receives done
	public static final int NUM_RECEIVES = 4;
	protected static CustomServerConnectionListener customServerConnectionListener;
	
	/*
	 * Ask the factory setter to set factories
	 */
	public static void setFactories() {
		FactorySetterFactory.setSingleton(new Part1FactorySetter());
		if (FactorySetterFactory.getSingleton() != null) {
			FactorySetterFactory.getSingleton().setFactories();
		}
	}
	
	/**
	 * Code for making explicit receive calls to receive messages
	 * It does not work in the default implementation. Your assignment
	 * should make it work.
	 * 
	 * In each iteration explicit receive from the next client is done,followed by
	 * an implicit receive from the same client.
	 */
	public static void doReceive() {
		customServerConnectionListener.waitForClients(NUM_RECEIVE_CLIENTS);
		String aClient1 = customServerConnectionListener.getClients().get(0);
		String aClient2 = customServerConnectionListener.getClients().get(1);
		boolean client1Turn = true;		
		for (int i = 0; i < NUM_RECEIVES; i++) {
			String nextClient = client1Turn?aClient1:aClient2;
			client1Turn = !client1Turn;
			System.out.println("Parameterized receive from:" + nextClient);
			ReceiveReturnMessage aReceivedMessage = duplexRPCServerInputPort.receive(nextClient);			
			System.out.println("Received message:" + aReceivedMessage );
			System.out.println("Parameterless receive from last sender:" + duplexRPCServerInputPort.getSender());
			aReceivedMessage = duplexRPCServerInputPort.receive();
			System.out.println("Received message:" + aReceivedMessage );
		}
	}
	public static void addConnectListener() {
		customServerConnectionListener = new ACustomServerConnectionListener();
		duplexRPCServerInputPort.addConnectionListener(customServerConnectionListener);
	}
	public static void launch() {
		setFactories();
		AMultiLayerCounterServer.launch();
		addConnectListener();
		doReceive();
		
	}
	
	

}
