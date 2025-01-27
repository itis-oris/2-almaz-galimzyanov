package ru.itis.fisd.protocol;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class Converter {

    public static byte[] encode(Protocol protocol) {
        byte[] type = ByteBuffer.allocate(4).putInt(Integer.parseInt(protocol.type().getValue())).array();
        byte[] size = ByteBuffer.allocate(4).putInt(protocol.body().length()).array();
        byte[] body = ByteBuffer.allocate(protocol.body().length()).put(protocol.body().getBytes()).array();

        byte[] combined = new byte[type.length + size.length + body.length];
        System.arraycopy(type, 0, combined, 0, type.length);
        System.arraycopy(size, 0, combined, type.length, size.length);
        System.arraycopy(body, 0, combined, type.length + size.length, body.length);
        System.out.println(Arrays.toString(type) + " " + Arrays.toString(size) + " " + Arrays.toString(body));
        System.out.println(Arrays.toString(combined));
        return combined;
    }

    public static Protocol decode(byte[] data) {
        ByteBuffer buffer = ByteBuffer.wrap(data);
        int type = buffer.getInt();
        int bodyLength = buffer.getInt();
        byte[] body = new byte[bodyLength];
        buffer.get(body);

        ProtocolType protocolType = ProtocolType.fromValue(String.valueOf(type));

        return new Protocol(protocolType, new String(body));
    }



}
