package serializer;

import java.nio.ByteBuffer;

public interface Serializer {
	public ByteBuffer outputBufferFromObject(Object object);

	public Object objectFromInputBuffer(ByteBuffer inputBuffer);
}
