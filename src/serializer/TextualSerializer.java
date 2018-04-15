package serializer;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Vector;

import ValueSerializer.ArraySerializer;
import ValueSerializer.BooleanSerializer;
import ValueSerializer.DoubleSerializer;
import ValueSerializer.FloatSerializer;
import ValueSerializer.IntegerSerializer;
import ValueSerializer.LongSerializer;
import ValueSerializer.ReferenceSerializer;
import ValueSerializer.ShortSerializer;
import ValueSerializer.StringSerializer;
import serialization.Serializer;
import util.annotations.Comp533Tags;
import util.annotations.Tags;

@Tags({Comp533Tags.LOGICAL_TEXTUAL_SERIALIZER})
public class TextualSerializer implements Serializer {

	protected TextualSerializer() {
		SerializerRegistry.registerValueSerializer(Integer.class, new IntegerSerializer());
		SerializerRegistry.registerValueSerializer(Short.class, new ShortSerializer());
		SerializerRegistry.registerValueSerializer(Long.class, new LongSerializer());
		SerializerRegistry.registerValueSerializer(Boolean.class, new BooleanSerializer());
		SerializerRegistry.registerValueSerializer(Double.class, new DoubleSerializer());
		SerializerRegistry.registerValueSerializer(Float.class, new FloatSerializer());
		SerializerRegistry.registerValueSerializer(String.class, new StringSerializer());
		SerializerRegistry.registerValueSerializer(HashSet.class, new HashSetSerializer());
		SerializerRegistry.registerValueSerializer(ArrayList.class, new ArrayListSerializer());
		SerializerRegistry.registerValueSerializer(Vector.class, new VectorSerializer());
		SerializerRegistry.registerValueSerializer(HashMap.class, new HashMapSerializer());
		SerializerRegistry.registerValueSerializer(Hashtable.class, new HashTableSerializer());
		
		SerializerRegistry.registerArraySerializer(new ArraySerializer());
		SerializerRegistry.registerBeanSerializer(new BeanSerializer());
		SerializerRegistry.registerListPatternSerializer(new ListPatternSerializer());
		SerializerRegistry.registerEnumSerializer(new EnumSerializer());
		SerializerRegistry.registerNullSerializer(new NullSerializer());
		SerializerRegistry.registerDispatchingSerializer(new ADispatchingSerializer());
		SerializerRegistry.registerReferenceSerializer(new ReferenceSerializer());
	}
	
	@Override
	public ByteBuffer outputBufferFromObject(Object object) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object objectFromInputBuffer(ByteBuffer inputBuffer) {
		// TODO Auto-generated method stub
		return null;
	}

}
