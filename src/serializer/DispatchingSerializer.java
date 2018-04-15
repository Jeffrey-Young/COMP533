package serializer;

import java.io.NotSerializableException;
import java.io.StreamCorruptedException;
import java.util.List;

import util.annotations.Comp533Tags;
import util.annotations.Tags;

@Tags({ Comp533Tags.DISPATCHING_SERIALIZER })
public interface DispatchingSerializer {
	void objectToBuffer(Object anOutputBuffer, Object anObject, List<Object> visitedObjects)
			throws NotSerializableException;

	Object objectFromBuffer(Object anInputBuffer, List<Object> retrievedObjects)
			throws StreamCorruptedException, NotSerializableException;
}
