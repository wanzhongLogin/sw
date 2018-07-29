package org.springframework.asm;


/**
 * 类的field访问
 * 定义在解析字段时触发的事件，如解析到字段上的注解、解析到字段相关的属性等。
 */
public abstract class FieldVisitor {

    protected final int api;

    protected FieldVisitor fv;

    public FieldVisitor(final int api){
        this(api,null);
    }
    
    public FieldVisitor(final int api,FieldVisitor fv){
        if(api < Opcodes.ASM4 || api > Opcodes.ASM6){
            throw new IllegalArgumentException();
        }
        this.api = api;
        this.fv = fv;
    }

    public AnnotationVisitor visitAnnotation(String desc,boolean visible){
        if(fv != null){
            return fv.visitAnnotation(desc,visible);
        }
        return null;
    }

    public AnnotationVisitor visitTypeAnnotation(int typeRef,TypePath typePath,String desc,boolean visible){
        if(api < Opcodes.ASM5){
            throw new IllegalArgumentException();
        }
        if(fv != null){
            return fv.visitTypeAnnotation(typeRef,typePath,desc,visible);
        }
        return null;
    }

    public void visitAttribute(Attribute attr){
        if(fv != null){
            fv.visitAttribute(attr);
        }
    }

    public void visitEnd(){
        if(fv != null){
            fv.visitEnd();
        }
    }


}
