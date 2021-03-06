package serializer;

import serialization.Serializer;
import serialization.SerializerFactory;
import util.annotations.Tags;
import util.annotations.Comp533Tags;


@Tags({Comp533Tags.LOGICAL_BINARY_SERIALIZER})
public class BinarySerializerFactory implements SerializerFactory {
	
	@Override
	public Serializer createSerializer() {
		return new BinarySerializer();
	}
}
