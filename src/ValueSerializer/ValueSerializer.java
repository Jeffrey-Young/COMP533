package ValueSerializer;

import util.annotations.Tags;

import java.io.NotSerializableException;
import java.io.StreamCorruptedException;
import java.util.List;

import util.annotations.Comp533Tags;

@Tags({ Comp533Tags.VALUE_SERIALIZER })
public interface ValueSerializer {
	void objectToBuffer (Object anOutputBuffer,  Object anObject, List<Object> visitedObjects) throws NotSerializableException;
	Object objectFromBuffer(Object anInputBuffer, Class aClass, List<Object> retrievedObjects) throws StreamCorruptedException, NotSerializableException;
}
