package org.springframework.asm;


/**
 * 定义在读取Class字节码时会触发的事件，如类头解析完成、注解解析、字段解析、方法解析等
 */
public abstract class ClassVisitor {

    /**
     * The ASM API version implemented by this visitor. The value of this field
     * must be one of {@link Opcodes#ASM4}, {@link Opcodes#ASM5} or {@link Opcodes#ASM6}.
     */
    protected final int api;

    protected ClassVisitor cv;

    public ClassVisitor(final int api){
        this(api,null);
    }
    
    public ClassVisitor(final int api,final ClassVisitor cv){
        if(api < Opcodes.ASM4 || api > Opcodes.ASM6){
            throw new IllegalArgumentException();
        }
        this.api = api;
        this.cv = cv;
    }

    /**
     * 这样的类,肯定是下面的实现类,来调用这个接口,访问自己的,所以并不会自己调用自己
     */
    public void visit(int version,int access,String name,String signature,String superName,String[] interfaces){
        if(cv != null){
            cv.visit(version,access,name,signature,superName,interfaces);
        }
    }

    public void visitSource(String source,String debug){
        if(cv != null){
            cv.visitSource(source,debug);
        }
    }

    public ModuleVisitor visitModule(String name,int access,String verison){
        if(api < Opcodes.ASM6){
            throw new RuntimeException();
        }
        if(cv != null){
            return cv.visitModule(name,access,verison);
        }
        return null;
    }

    public void visitOuterClass(String owner,String name,String desc){
        if(cv != null){
            cv.visitOuterClass(owner,name,desc);
        }
    }

    public AnnotationVisitor visitAnnotation(String desc,boolean visible){
        if(cv != null){
            return cv.visitAnnotation(desc,visible);
        }
        return null;
    }

    public AnnotationVisitor visitTypeAnnotation(int typeRef,TypePath typePath,String desc,boolean visible){
        if(cv != null){
            return cv.visitTypeAnnotation(typeRef,typePath,desc,visible);
        }
        return null;
    }

    public void visitAttribute(Attribute attr){
        if(cv != null){
            cv.visitAttribute(attr);
        }
    }


    public void visitInnerClass(String name,String outerName,String innerName,int access){
        if(cv != null){
            cv.visitInnerClass(name,outerName,innerName,access);
        }
    }

    public FieldVisitor visitField(int access,String name,String desc,String signature,Object value){
        if(cv != null){
            return cv.visitField(access,name,desc,signature,value);
        }
        return null;
    }

    public MethodVisitor visitMethod(int access,String name,String desc,String signature,String[] exceptions){
        if(cv != null){
            return cv.visitMethod(access,name,desc,signature,exceptions);
        }
        return null;
    }

    public void visitEnd(){
        if(cv != null){
            cv.visitEnd();
        }
    }

}
