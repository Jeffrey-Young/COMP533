package serializer;


// ugh can't sublcass StringBuffer because it is final why??
public class PositionedStringBuffer {
	
	private int pos;
	private StringBuffer buf;
	
	public PositionedStringBuffer() {
		pos = 0;
		buf = new StringBuffer();
	}
	
	
	
	public void putInteger(Integer val) {
		buf.append(val);
		pos += val.toString().length();
	}
	
	public void putShort(Short val) {
		buf.append(val);
		pos += val.toString().length();
	}
	
	public void putLong(Long val) {
		buf.append(val);
		pos += val.toString().length();
	}
	
	public void putDouble(Double val) {
		buf.append(val);
		pos += val.toString().length();
	}
	
	public void putFloat(Float val) {
		buf.append(val);
		pos += val.toString().length();
	}
	
	public void putString(String val) {
		buf.append(val);
		pos += val.toString().length();
	}
	
	public void putDelimiter(Character delim) {
		buf.append(delim);
		pos++;
	}
	
	public void putLength(int len) {
		// TODO: we assume the length is 1-9 is this ok??
		buf.append(len);
		pos++;
	}
	
	

}
