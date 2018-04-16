package serializer;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Vector;

import ValueSerializer.ArrayListSerializer;
import ValueSerializer.ArraySerializer;
import ValueSerializer.BooleanSerializer;
import ValueSerializer.DoubleSerializer;
import ValueSerializer.FloatSerializer;
import ValueSerializer.HashMapSerializer;
import ValueSerializer.HashSetSerializer;
import ValueSerializer.HashTableSerializer;
import ValueSerializer.IntegerSerializer;
import ValueSerializer.LongSerializer;
import ValueSerializer.ReferenceSerializer;
import ValueSerializer.ShortSerializer;
import ValueSerializer.StringSerializer;
import ValueSerializer.VectorSerializer;
import serialization.Serializer;
import util.annotations.Comp533Tags;
import util.annotations.Tags;

@Tags({Comp533Tags.LOGICAL_TEXTUAL_SERIALIZER})
public class TextualSerializer implements Serializer {

	protected TextualSerializer() {
		SerializerRegistry.registerValueSerializer(Integer.class, new IntegerSerializer()); // I
		SerializerRegistry.registerValueSerializer(Short.class, new ShortSerializer()); // S
		SerializerRegistry.registerValueSerializer(Long.class, new LongSerializer()); // L
		SerializerRegistry.registerValueSerializer(Boolean.class, new BooleanSerializer()); // B
		SerializerRegistry.registerValueSerializer(Double.class, new DoubleSerializer()); // D
		SerializerRegistry.registerValueSerializer(Float.class, new FloatSerializer()); // F
		SerializerRegistry.registerValueSerializer(String.class, new StringSerializer()); // S
		SerializerRegistry.registerValueSerializer(HashSet.class, new HashSetSerializer()); // H
		SerializerRegistry.registerValueSerializer(ArrayList.class, new ArrayListSerializer()); // Y
		SerializerRegistry.registerValueSerializer(Vector.class, new VectorSerializer()); // V
		SerializerRegistry.registerValueSerializer(HashMap.class, new HashMapSerializer()); // M
		SerializerRegistry.registerValueSerializer(Hashtable.class, new HashTableSerializer()); // T
		
		SerializerRegistry.registerArraySerializer(new ArraySerializer()); // A
		SerializerRegistry.registerBeanSerializer(new BeanSerializer()); // E
//		SerializerRegistry.registerListPatternSerializer(new ListPatternSerializer());
//		SerializerRegistry.registerEnumSerializer(new EnumSerializer());
//		SerializerRegistry.registerNullSerializer(new NullSerializer());
		SerializerRegistry.registerDispatchingSerializer(new ADispatchingSerializer());
		SerializerRegistry.registerReferenceSerializer(new ReferenceSerializer()); // R
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
