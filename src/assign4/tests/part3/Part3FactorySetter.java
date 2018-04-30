package assign4.tests.part3;

import assign4.ACustomDuplexObjectInputPortFactory;
import assign4.ACustomDuplexReceivedCallInvokerFactory;
import assign4.ACustomDuplexSentCallCompleterFactory;
import examples.gipc.counter.customization.FactorySetter;
import inputport.datacomm.duplex.object.DuplexObjectInputPortSelector;
import inputport.rpc.duplex.DuplexReceivedCallInvokerSelector;
import inputport.rpc.duplex.DuplexSentCallCompleterSelector;
import serialization.SerializerSelector;

public class Part3FactorySetter implements FactorySetter {

	@Override
	public void setFactories() {
		/*
		 * Determines the ports for sending and receiving objects. Needed for
		 * sync receive.
		 */
		DuplexObjectInputPortSelector.setDuplexInputPortFactory(new ACustomDuplexObjectInputPortFactory());

		/*
		 * Two alternatives for received call invoker factory, with one
		 * commented out. This factory determines the object that actually calls
		 * a method of a remote object in response to a received message
		 */
		DuplexReceivedCallInvokerSelector.setReceivedCallInvokerFactory(new ACustomDuplexReceivedCallInvokerFactory());
		// DuplexReceivedCallInvokerSelector.setReceivedCallInvokerFactory(
		// new AnAsynchronousCustomDuplexReceivedCallInvokerFactory());

		/*
		 * Determines the object that processes return value, if any, of a
		 * remote call
		 */
		DuplexSentCallCompleterSelector.setDuplexSentCallCompleterFactory(new ACustomDuplexSentCallCompleterFactory());

		/*
		 * This is for the serializer assignment, determines the serializer
		 */
		// SerializerSelector.setSerializerFactory(new
		// ACustomSerializerFactory());
	}

}
