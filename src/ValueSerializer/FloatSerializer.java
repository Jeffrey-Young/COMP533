package ValueSerializer;

import java.io.NotSerializableException;
import java.io.StreamCorruptedException;
import java.nio.ByteBuffer;
import java.util.List;

import serializer.PositionedStringBuffer;
import util.annotations.Comp533Tags;
import util.annotations.Tags;
import util.trace.port.serialization.extensible.ExtensibleValueSerializationFinished;
import util.trace.port.serialization.extensible.ExtensibleValueSerializationInitiated;

@Tags({ Comp533Tags.FLOAT_SERIALIZER })
public class FloatSerializer implements ValueSerializer {

	@Override
	public void objectToBuffer(Object anOutputBuffer, Object anObject, List<Object> visitedObjects)
			throws NotSerializableException {
		ExtensibleValueSerializationInitiated.newCase(this, anObject, anOutputBuffer);

		Float toSerialize = (Float) anObject;

		if (anOutputBuffer instanceof ByteBuffer) {

		} else { // Textual Serialization
			PositionedStringBuffer buf = (PositionedStringBuffer) anOutputBuffer;
			buf.putDelimiter('F');
			buf.putLength(toSerialize.toString().length());
			buf.putFloat(toSerialize);
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
