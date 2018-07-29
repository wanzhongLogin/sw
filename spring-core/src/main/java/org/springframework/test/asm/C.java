package org.springframework.test.asm;

import java.io.*;

public class C extends ClassLoader {
    public Class<?> findClass(String name) {
        byte[] data = loadClassData(name);
        return defineClass(name, data, 0, data.length);// 将一个 byte 数组转换为 Class// 类的实例
    }

    public byte[] loadClassData(String name) {
        FileInputStream fis = null;
        InputStream in = ClassLoader.getSystemResourceAsStream("org/springframework/test/Account.class");
        byte[] data = null;
        try {
//            fis = new FileInputStream(name);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int ch = 0;
            while ((ch = in.read()) != -1) {
                baos.write(ch);
            }
            data = baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }
}
