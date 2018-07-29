package org.springframework.test.asm;

import com.sun.xml.internal.ws.org.objectweb.asm.*;

import java.io.IOException;

public class DelLogin extends ClassAdapter {
    public DelLogin(ClassVisitor cv) {
        super(cv);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        if(name.equals("login")){
            return null;
        }
        return cv.visitMethod(access, name, desc, signature, exceptions);
    }


    public class AccessClassAdapter extends ClassAdapter{
        public AccessClassAdapter(ClassVisitor cv) {
            super(cv);
        }
        @Override
        public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
            int privateAccess = Opcodes.ACC_PRIVATE;
            return cv.visitField(privateAccess,name,desc,signature,value);
        }
    }

    public static void main(String args[]) throws IOException {
        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        ClassAdapter delAdapter = new DelLogin(classWriter);
        DelLogin.AccessClassAdapter acc = new DelLogin(classWriter).new AccessClassAdapter(classWriter);

        ClassReader classReader = new ClassReader("");
        classReader.accept(delAdapter,ClassReader.SKIP_DEBUG);

    }

}
