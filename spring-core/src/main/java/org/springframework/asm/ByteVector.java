package org.springframework.asm;

/**
 * 字节码二进制存储的容器
 */
public class ByteVector {

    byte[] data;

    int length;

    public ByteVector(){
        data = new byte[64];
    }
    public ByteVector(final int initialSize){
        data = new byte[initialSize];
    }

    public ByteVector putByte(final int b) {
        int length = this.length;
        if(length + 1 > data.length){
            enlarge(1);
        }
        data[length++] = (byte) b;
        this.length = length;
        return this;
    }

    public ByteVector put11(final int b1,final int b2) {
        int length = this.length;
        if(length + 2 > data.length){
            enlarge(2);
        }
        byte[] data = this.data;
        data[length++] = (byte) b1;
        data[length++] = (byte) b2;
        this.length = length;
        return this;
    }

    public ByteVector putShort(final int s){
        int length = this.length;
        if(length + 2 > data.length){
            enlarge(2);
        }
        byte[] data = this.data;
        data[length++] = (byte) (s >>> 8);
        data[length++] = (byte) s;
        this.length = length;
        return this;
    }

    ByteVector put12(final int b,final int s){
        int length = this.length;
        if(length + 3 > data.length){
            enlarge(3);
        }
        byte[] data = this.data;
        data[length++] = (byte) b;
        data[length++] = (byte) (s >>> 8);
        data[length++] = (byte) s;
        this.length = length;
        return this;
    }

    public ByteVector putInt(final int i){
        int length = this.length;
        if(length + 4 > data.length){
            enlarge(4);
        }
        byte[] data = this.data;
        data[length++] = (byte) (i >>> 24);
        data[length++] = (byte) (i >>> 16);
        data[length++] = (byte) (i >>> 8);
        data[length++] = (byte) i;
        this.length = length;
        return this;
    }


    public ByteVector putLong(final long l){
        int length = this.length;
        if(length + 8 > data.length){
            enlarge(8);
        }
        byte[] data = this.data;

        int i = (int) (l >>> 32);

        data[length++] = (byte)(i >>> 24);
        data[length++] = (byte) (i >>> 16);
        data[length++] = (byte)(i >>> 8);
        data[length++] = (byte) i;
        this.length = length;
        return this;
    }

    public ByteVector putUTF8(final String s) {
        int charLength = s.length();
        if (charLength > 65535) {
            throw new IllegalArgumentException();
        }
        int len = length;
        if (len + 2 + charLength > data.length) {
            enlarge(2 + charLength);
        }
        byte[] data = this.data;
        data[len++] = (byte) (charLength >>> 8);
        data[len++] = (byte) charLength;
        for (int i = 0; i < charLength; ++i) {
            char c = s.charAt(i);
            if (c >= '\001' && c <= '\177') {
                data[len++] = (byte) c;
            } else {
                length = len;
                return encodeUTF8(s, i, 65535);
            }
        }
        length = len;
        return this;
    }

    ByteVector encodeUTF8(final String s, int i, int maxByteLength) {
        int charLength = s.length();
        int byteLength = i;
        char c;
        for (int j = i; j < charLength; ++j) {
            c = s.charAt(j);
            if (c >= '\001' && c <= '\177') {
                byteLength++;
            } else if (c > '\u07FF') {
                byteLength += 3;
            } else {
                byteLength += 2;
            }
        }
        if (byteLength > maxByteLength) {
            throw new IllegalArgumentException();
        }
        int start = length - i - 2;
        if (start >= 0) {
            data[start] = (byte) (byteLength >>> 8);
            data[start + 1] = (byte) byteLength;
        }
        if (length + byteLength - i > data.length) {
            enlarge(byteLength - i);
        }
        int len = length;
        for (int j = i; j < charLength; ++j) {
            c = s.charAt(j);
            if (c >= '\001' && c <= '\177') {
                data[len++] = (byte) c;
            } else if (c > '\u07FF') {
                data[len++] = (byte) (0xE0 | c >> 12 & 0xF);
                data[len++] = (byte) (0x80 | c >> 6 & 0x3F);
                data[len++] = (byte) (0x80 | c & 0x3F);
            } else {
                data[len++] = (byte) (0xC0 | c >> 6 & 0x1F);
                data[len++] = (byte) (0x80 | c & 0x3F);
            }
        }
        length = len;
        return this;
    }

    public ByteVector putByteArray(final byte[] b,final int off,final int len){
        if(length + len > data.length){
            enlarge(len);
        }
        if(b != null){
            System.arraycopy(b,off,data,length,len);
        }
        length += len;
        return this;
    }

    private void enlarge(final int size){
        int length1 = 2 * data.length;
        int length2 = length + size;
        byte[] newData = new byte[length1 > length2 ? length1 : length2];
        data = newData;
    }

}
