package org.springframework.asm;

/**
 * 类的方法的访问
 * 定义在解析方法时触发的事件，如方法上的注解、属性、代码等。
 */
public abstract class MethodVisitor {

    protected final int api;

    protected MethodVisitor mv;
    
    public MethodVisitor(final int api){
        this(api,null);
    }
    
    public MethodVisitor(final int api,final MethodVisitor mv){
        if(api < Opcodes.ASM4 || api > Opcodes.ASM6){
            throw new IllegalArgumentException();
        }
        this.api = api;
        this.mv = mv;
    }

    public void visitParameter(String name,int access){
        if(mv != null){
            mv.visitParameter(name,access);
        }
    }

    public AnnotationVisitor visitAnnotationDefault(){
        if(mv != null){
            return mv.visitAnnotationDefault();
        }
        return null;
    }

    public AnnotationVisitor visitAnnotation(String desc,boolean visible){
        if(mv != null){
            return mv.visitAnnotation(desc,visible);
        }
        return null;
    }

    public AnnotationVisitor visitTypeAnnotation(int typeRef,TypePath typePath,String desc,boolean visible){
        if(mv != null){
            mv.visitTypeAnnotation(typeRef,typePath,desc,visible);
        }
        return null;
    }

    public AnnotationVisitor visitParameterAnnotation(int parameter,String desc,boolean visible){
        if(mv != null){
            return mv.visitParameterAnnotation(parameter,desc,visible);
        }
        return null;
    }

    public void visitAttribute(Attribute attr){
        if(mv != null){
            mv.visitAttribute(attr);
        }
    }

    public void visitCode(){
        if(mv != null){
            mv.visitCode();
        }
    }

    public void visitFrame(int type,int nLocal,Object[] local,int nStack,Object[] statck){
        if(mv != null){
            mv.visitFrame(type,nLocal,local,nStack,statck);
        }
    }

    public void visitInsn(int opcode){
        if(mv != null){
            mv.visitInsn(opcode);
        }
    }

    public void visitIntInsn(int opcode,int operand){
        if(mv != null){
            mv.visitIntInsn(opcode,operand);
        }
    }
    public void visitVarInsn(int opcode,int var){
        if(mv != null){
            mv.visitVarInsn(opcode,var);
        }
    }

    public void visitTypeInsn(int opcode,String type){
        if(mv != null){
            mv.visitTypeInsn(opcode,type);
        }
    }

    public void visitFieldInsn(int opcode,String owner,String name,String desc){
        if(mv != null){
            mv.visitFieldInsn(opcode,owner,name,desc);
        }
    }

    public void visitMethodInsn(int opcode, String owner, String name,
                                String desc, boolean itf) {
        if (api < Opcodes.ASM5) {
            if (itf != (opcode == Opcodes.INVOKEINTERFACE)) {
                throw new IllegalArgumentException(
                        "INVOKESPECIAL/STATIC on interfaces require ASM 5");
            }
            visitMethodInsn(opcode, owner, name, desc);
            return;
        }
        if (mv != null) {
            mv.visitMethodInsn(opcode, owner, name, desc, itf);
        }
    }
    @Deprecated
    public void visitMethodInsn(int opcode, String owner, String name,
                                String desc) {
        if (api >= Opcodes.ASM5) {
            boolean itf = opcode == Opcodes.INVOKEINTERFACE;
            visitMethodInsn(opcode, owner, name, desc, itf);
            return;
        }
        if (mv != null) {
            mv.visitMethodInsn(opcode, owner, name, desc);
        }
    }

    public void visitInvokeDynamicInsn(String name,String desc,Handle bsm,Object... bsmArgs){
        if(mv != null){
            mv.visitInvokeDynamicInsn(name,desc,bsm,bsmArgs);
        }
    }

    public void visitJumpInsn(int opcode,Label label){
        if(mv != null){
            mv.visitJumpInsn(opcode,label);
        }
    }


    public void visitLabel(Label label){
        if(mv != null){
            mv.visitLabel(label);
        }
    }


    public void visitLdcInsn(Object cst){
        if(mv != null){
            mv.visitLdcInsn(cst);
        }
    }

    public void visitIincInsn(int var,int increment){
        if(mv != null){
            mv.visitIincInsn(var,increment);
        }
    }

    public void visitTableSwitchInsn(int min,int max,Label dflt,Label... labels){
        if(mv != null){
            mv.visitTableSwitchInsn(min,max,dflt,labels);
        }
    }

    public void visitLookupSwitchInsn(Label dflt,int[] keys,Label[] labels){
        if(mv != null){
            mv.visitLookupSwitchInsn(dflt,keys,labels);
        }
    }

    public void visitMultiANewArrayInsn(String desc,int dims){
        if(mv != null){
            mv.visitMultiANewArrayInsn(desc,dims);
        }
    }

    public AnnotationVisitor visitInsnAnnotation(int typeRef,TypePath typePath,String desc,boolean visible){
        if(mv != null){
            return mv.visitInsnAnnotation(typeRef,typePath,desc,visible);
        }
        return null;
    }

    public void visitTryCatchBlock(Label start,Label end,Label handler,String type){
        if(mv != null){
            mv.visitTryCatchBlock(start,end,handler,type);
        }
    }

    public AnnotationVisitor visitTryCatchAnnotation(int typeRef,TypePath typePath,String desc,boolean visible){
        if(mv != null){
            return mv.visitTryCatchAnnotation(typeRef,typePath,desc,visible);
        }
        return null;
    }

    public void visitLocalVariable(String name,String desc,String signature,Label start,Label end,int index){
        if(mv != null){
            mv.visitLocalVariable(name,desc,signature,start,end,index);
        }
    }

    public AnnotationVisitor visitLocalVariableAnnotation(int typeRef,TypePath typePath,Label[] start,Label[] end,int[] index,String desc,boolean visible){
        if(mv != null){
            mv.visitLocalVariableAnnotation(typeRef,typePath,start,end,index,desc,visible);
        }
        return null;
    }

    public void visitLineNumber(int line,Label start){
        if(mv != null){
            mv.visitLineNumber(line,start);
        }
    }

    public void visitMaxs(int maxStack,int maxLocals){
        if(mv != null){
            mv.visitMaxs(maxStack,maxLocals);
        }
    }

    public void visitEnd(){
        if(mv != null){
            mv.visitEnd();
        }
    }
}
