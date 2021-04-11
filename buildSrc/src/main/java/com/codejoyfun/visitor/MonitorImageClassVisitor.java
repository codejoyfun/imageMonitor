package com.codejoyfun.visitor;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class MonitorImageClassVisitor extends ClassVisitor {

    private static final String MONITOR_SUPER_CLASS_NAME = "com/codejoyfun/imagemonitor/MonitorImageView";
    private static final String IMAGE_VIEW_CLASS_NAME = "android/widget/ImageView";

    public MonitorImageClassVisitor(ClassVisitor classVisitor) {
        super(Opcodes.ASM5, classVisitor);
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        if(superName.equals(IMAGE_VIEW_CLASS_NAME)
                && !name.equals(MONITOR_SUPER_CLASS_NAME)){
            superName = MONITOR_SUPER_CLASS_NAME;
            System.out.println("name ->" + name + ", superName-> " + superName);
        }
        super.visit(version, access, name, signature, superName, interfaces);
    }
}
