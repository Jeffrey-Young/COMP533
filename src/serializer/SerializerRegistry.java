package serializer;

import java.util.HashMap;
import java.util.Map;

import ValueSerializer.ReferenceSerializer;
import ValueSerializer.ValueSerializer;
import util.annotations.Comp533Tags;
import util.annotations.Tags;

@Tags({ Comp533Tags.SERIALIZER_REGISTRY })
public class SerializerRegistry {

	private static Map<Class, ValueSerializer> registry = new HashMap<Class, ValueSerializer>();
	
	private static DispatchingSerializer dispatchingSerializer;
	private static ValueSerializer arraySerializer;
	private static ValueSerializer beanSerializer;
	private static ValueSerializer listPatternSerializer;
	private static ValueSerializer enumSerializer;
	private static ValueSerializer nullSerializer;
	private static ValueSerializer referenceSerializer;

	public static void registerValueSerializer(Class aClass, ValueSerializer anExternalSerializer) {
		registry.put(aClass, anExternalSerializer);
	}

	public static ValueSerializer getValueSerializer(Class aClass) {
		return registry.get(aClass);
	}

	// other methods

	public static DispatchingSerializer getDispatchingSerializer() {
		return dispatchingSerializer;
	}
	
	public static void  registerDispatchingSerializer(DispatchingSerializer  newVal) {
		dispatchingSerializer = newVal;
	}
	
	public static void registerArraySerializer(ValueSerializer anExternalSerializer) {
		arraySerializer = anExternalSerializer;
	}

	public static ValueSerializer getArraySerializer() {
		return arraySerializer;
	}
	
	public static void registerBeanSerializer(ValueSerializer anExternalSerializer) {
		beanSerializer = anExternalSerializer;
	}

	public static ValueSerializer getBeanSerializer() {
		return beanSerializer;
	}

	public static void registerListPatternSerializer(ValueSerializer anExternalSerializer) {
		listPatternSerializer = anExternalSerializer;
	}

	public static ValueSerializer getListPatternSerializer() {
		return listPatternSerializer;
	}
	
	public static void registerEnumSerializer(ValueSerializer anExternalSerializer) {
		enumSerializer = anExternalSerializer;
	}

	public static ValueSerializer getEnumSerializer() {
		return enumSerializer;
	}
	
	public static void registerNullSerializer(ValueSerializer anExternalSerializer) {
		nullSerializer = anExternalSerializer;
	}

	public static ValueSerializer getNullSerializer() {
		return nullSerializer;
	}
	
	public static void registerReferenceSerializer(ReferenceSerializer anExternalSerializer) {
		referenceSerializer = anExternalSerializer;
	}
	
	public static ValueSerializer getReferenceSerializer() {
		return referenceSerializer;
	}
}
