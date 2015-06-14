package net.maunium.Maunsic.Core.Transformers;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import net.maunium.Maunsic.Core.AbstractMauTransformer;
import net.maunium.Maunsic.Listeners.InputHandler;

public class KeyBindingT extends AbstractMauTransformer {
	public KeyBindingT() {
		super("net.minecraft.client.settings.KeyBinding", "");
	}
	
	@Override
	public byte[] transform(byte[] bytes, boolean obf) {
		String methodName = obf ? "" : "setKeyBindState";
		
		ClassNode node = new ClassNode();
		ClassReader cr = new ClassReader(bytes);
		cr.accept(node, 0);
		
		for (MethodNode m : node.methods) {
			if (m.name.equals(methodName) && m.desc.equals("(IZ)V")) {
				InsnList insn = new InsnList();
				
				insn.add(new VarInsnNode(Opcodes.ILOAD, 0));
				insn.add(new VarInsnNode(Opcodes.ILOAD, 1));
				insn.add(new MethodInsnNode(Opcodes.INVOKESTATIC, InputHandler.class.getName().replace('.', '/'), "input", "(IZ)V", false));
				
				m.instructions.insert(insn);
				break;
			}
		}
		
		ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
		node.accept(cw);
		return cw.toByteArray();
	}
}
