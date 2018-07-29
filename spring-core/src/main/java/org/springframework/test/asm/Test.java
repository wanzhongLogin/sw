package org.springframework.test.asm;

import com.sun.xml.internal.ws.org.objectweb.asm.*;
import org.springframework.asm.Opcodes;

public class Test {

    class AddSecurityCheckClassAdapter extends ClassAdapter {
        public AddSecurityCheckClassAdapter(ClassVisitor cv) {
            //Responsechain 的下一个 ClassVisitor，这里我们将传入 ClassWriter，
            // 负责改写后代码的输出
            super(cv);
        }
        // 重写 visitMethod，访问到 "operation" 方法时，
        // 给出自定义 MethodVisitor，实际改写方法内容
        public MethodVisitor visitMethod(final int access, final String name, final String desc, final String signature, final String[] exceptions) {
            MethodVisitor mv = cv.visitMethod(access, name, desc, signature,exceptions);
            MethodVisitor wrappedMv = mv;
            if (mv != null) {
                // 对于 "operation" 方法
                if (name.equals("operation")) {
                    // 使用自定义 MethodVisitor，实际改写方法内容
                    wrappedMv = new AddSecurityCheckMethodAdapter(mv);
                }
            }
            return wrappedMv;
        }
    }

    class AddSecurityCheckMethodAdapter extends MethodAdapter {
        public AddSecurityCheckMethodAdapter(MethodVisitor mv) {
            super(mv);
        }
        public void visitCode() {
            visitMethodInsn(Opcodes.INVOKESTATIC, "SecurityChecker", "checkSecurity", "()V");
        }
    }

    public static void main(String args[]) throws Exception {
//            ClassReader cr = new ClassReader("org/springframework/test/Account");
//            ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
//
//            ClassAdapter classAdapter = new Test().new AddSecurityCheckClassAdapter(cw);
//            cr.accept(classAdapter, ClassReader.SKIP_DEBUG);
//            byte[] data = cw.toByteArray();
////            File file = new File("/Users/wanzhong/Documents/ideaworkspace/springWork/spring-core/src/main/java/org/springframework/test/Account.class");
//            File file = new File("Account.class");
//            FileOutputStream fout = new FileOutputStream(file);
//            fout.write(data);
//            fout.close();
//        SecurityChecker.checkSecurity();
        Account a = new Account();
        a.operation();
    }
    
}
