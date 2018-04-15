package serializer;

import java.io.NotSerializableException;
import java.io.StreamCorruptedException;
import java.util.List;

import util.annotations.Comp533Tags;
import util.annotations.Tags;

@Tags({ Comp533Tags.DISPATCHING_SERIALIZER })
public class ADispatchingSerializer implements DispatchingSerializer {

	@Override
	public void objectToBuffer(Object anOutputBuffer, Object anObject, List<Object> visitedObjects)
			throws NotSerializableException {
		// TODO Auto-generated method stub

	}

	@Override
	public Object objectFromBuffer(Object anInputBuffer, List<Object> retrievedObjects)
			throws StreamCorruptedException, NotSerializableException {
		// TODO Auto-generated method stub
		return null;
	}

}
