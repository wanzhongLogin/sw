package org.springframework.asm;


/**
 * A visitor to visit a Java annotation. The methods of this class must be
 * called in the following order: ( <tt>visit</tt> | <tt>visitEnum</tt> |
 * <tt>visitAnnotation</tt> | <tt>visitArray</tt> )* <tt>visitEnd</tt>
 * 访问 Java annotation的访问者.该类的方法必须被这些类所需要?
 * 定义在解析注解时会触发的事件，如解析到一个基本值类型的注解、enum值类型的注解、Array值类型的注解、注解值类型的注解等
 */
public abstract class AnnotationVisitor {


    protected final int api;

    protected AnnotationVisitor av;

    public AnnotationVisitor(final int api){
        this(api,null);
    }
    public AnnotationVisitor(final int api,final AnnotationVisitor av){
        //TODO IllegalArgumentException是什么异常
        if(api < Opcodes.ASM4 || api < Opcodes.ASM6){
            throw new IllegalArgumentException();
        }
        this.api = api;
        this.av = av;
    }


    public void visit(String name,Object value){
        if(av != null){
            av.visit(name,value);
        }
    }

    public void visitEnum(String name,String desc,String value){
        if(av != null){
            av.visitEnum(name,desc,value);
        }
    }

    public AnnotationVisitor visitAnnotation(String name,String desc){
        if(av != null){
            return av.visitAnnotation(name,desc);
        }
        return null;
    }

    public AnnotationVisitor visitArray(String name){
        if(av != null){
            return av.visitArray(name);
        }
        return null;
    }

    public void visitEnd(){
        if(av != null){
            av.visitEnd();
        }
    }

}
