package com.peterliu.peterrabbit.utils;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;

/**
 * Created by bavatinolab on 17/1/29.
 */
public abstract class CharsetUtils {

    private static final CharsetEncoder charsetEncoder = Charset.forName("utf-8").newEncoder();
    private static final CharsetDecoder charsetDecoder = Charset.forName("utf-8").newDecoder();

    /**
     * 编码
     *
     * @param charBuffer
     * @return
     * @throws CharacterCodingException
     */
    public static ByteBuffer enCode(CharBuffer charBuffer) throws CharacterCodingException {
        return charsetEncoder.encode(charBuffer);
    }

    /**
     * 解码
     *
     * @param byteBuffer
     * @return
     * @throws CharacterCodingException
     */
    public static CharBuffer deCode(ByteBuffer byteBuffer) throws CharacterCodingException {
        return charsetDecoder.decode(byteBuffer);
    }
}
