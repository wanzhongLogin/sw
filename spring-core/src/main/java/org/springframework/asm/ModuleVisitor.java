package org.springframework.asm;

public abstract class ModuleVisitor {

    protected final int api;

    protected ModuleVisitor mv;

    public ModuleVisitor(final int api){
        this(api,null);
    }
    
    public ModuleVisitor(final int api,final ModuleVisitor mv){
        if(api != Opcodes.ASM6){
            throw new IllegalArgumentException();
        }
        this.api = api;
        this.mv = mv;
    }


    public void visitMainClass(String mainClass){
        if(mv != null){
            mv.visitMainClass(mainClass);
        }
    }

    public void visitPackage(String packaze){
        if(mv != null){
            mv.visitPackage(packaze);
        }
    }

    public void visitRequire(String module,int access,String version){
        if(mv != null){
            mv.visitRequire(module,access,version);
        }
    }

    public void visitExport(String packaze,int access,String... modules){
        if(mv != null){
            mv.visitExport(packaze,access,modules);
        }
    }

    public void visitOpen(String packaze,int access,String... modules){
        if(mv != null){
            mv.visitOpen(packaze,access,modules);
        }
    }

    public void visitUse(String service){
        if(mv != null){
            mv.visitUse(service);
        }
    }

    public void visitProvide(String service,String... providers){
        if(mv != null){
            mv.visitProvide(service,providers);
        }
    }

    public void visitEnd(){
        if(mv != null){
            mv.visitEnd();
        }
    }
}
