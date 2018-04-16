package ValueSerializer;

import java.io.NotSerializableException;
import java.io.StreamCorruptedException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import serializer.PositionedStringBuffer;
import serializer.SerializerRegistry;
import util.annotations.Comp533Tags;
import util.annotations.Tags;
import util.trace.port.serialization.extensible.ExtensibleValueSerializationFinished;
import util.trace.port.serialization.extensible.ExtensibleValueSerializationInitiated;

@Tags({ Comp533Tags.MAP_SERIALIZER })
public class HashMapSerializer implements ValueSerializer {

	@Override
	public void objectToBuffer(Object anOutputBuffer, Object anObject, List<Object> visitedObjects)
			throws NotSerializableException {
		ExtensibleValueSerializationInitiated.newCase(this, anObject, anOutputBuffer);
		
		visitedObjects.add((HashMap<Object, Object>) anObject);

		if (anOutputBuffer instanceof ByteBuffer) {

		} else { // Textual Serialization
			PositionedStringBuffer buf = (PositionedStringBuffer) anOutputBuffer;
			buf.putDelimiter('M');
			// put name of class and its length
			buf.putInteger(anObject.getClass().getComponentType().getName().length());
			buf.putString(anObject.getClass().getComponentType().getName());
			for (Object key : ((HashMap<Object, Object>) anObject).keySet()) {
				Object item = ((HashMap<Object, Object>) anObject).get(key);
				if (visitedObjects.contains(key)) {
					SerializerRegistry.getReferenceSerializer().objectToBuffer(anOutputBuffer, visitedObjects.indexOf(item), visitedObjects);
				}else {
					visitedObjects.add(item);
					SerializerRegistry.getDispatchingSerializer().objectToBuffer(anOutputBuffer, item, visitedObjects);
				}
			}
		}

		ExtensibleValueSerializationFinished.newCase(this, anObject, anOutputBuffer, visitedObjects);
	}

	@Override
	public Object objectFromBuffer(Object anInputBuffer, Class aClass, List<Object> retrievedObjects)
			throws StreamCorruptedException, NotSerializableException {
		// TODO Auto-generated method stub
		return null;
	}

}
