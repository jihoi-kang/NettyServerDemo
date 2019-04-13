package server;

public class ProtocolHeader {
	
//  해더 길이
	public static final int HEADER_LENGTH       = 8;

//  스타트 확인 비트 - 임의 지정
	public static final short START             = (short) 0xabcd;

	/** sign 0x01 ~ 0x03 */
	public static final byte REQUEST            = 0x01; // client --> server
	public static final byte RESPONSE           = 0x02; // server --> client
	public static final byte NOTICE             = 0x03; // server --> client

    /** type 0x11 ~ */
	public static final byte CONN				= 0x11;

	
}
