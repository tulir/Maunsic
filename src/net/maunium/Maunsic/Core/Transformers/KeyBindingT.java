package net.maunium.Maunsic.Core.Transformers;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.FrameNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

import net.maunium.Maunsic.Core.AbstractMauTransformer;
import net.maunium.Maunsic.Events.RawInputEvent;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.EventBus;

/**
 * Transformer to steal raw key and mouse input from the KeyBinding class.
 * 
 * @author Tulir293
 * @since 0.1
 */
public class KeyBindingT extends AbstractMauTransformer {
	public KeyBindingT() {
		super("net.minecraft.client.settings.KeyBinding", "bsr");
	}
	
	@Override
	public byte[] transform(byte[] bytes, boolean obf) {
		String methodName = obf ? "a" : "setKeyBindState";
		
		ClassNode node = new ClassNode();
		ClassReader cr = new ClassReader(bytes);
		cr.accept(node, 0);
		
		for (MethodNode m : node.methods) {
			if (m.name.equals(methodName) && m.desc.equals("(IZ)V")) {
				InsnList insn = new InsnList();
				
//				insn.add(new VarInsnNode(Opcodes.ILOAD, 0));
//				insn.add(new VarInsnNode(Opcodes.ILOAD, 1));
//				insn.add(new MethodInsnNode(Opcodes.INVOKESTATIC, InputHandler.class.getName().replace('.', '/'), "input", "(IZ)V", false));
				
				insn.add(new TypeInsnNode(Opcodes.NEW, RawInputEvent.class.getName().replace('.', '/')));
				insn.add(new InsnNode(Opcodes.DUP));
				insn.add(new VarInsnNode(Opcodes.ILOAD, 0));
				insn.add(new VarInsnNode(Opcodes.ILOAD, 1));
				insn.add(new MethodInsnNode(Opcodes.INVOKESPECIAL, RawInputEvent.class.getName().replace('.', '/'), "<init>", "(IZ)V", false));
				insn.add(new VarInsnNode(Opcodes.ASTORE, 2));
				LabelNode l1 = new LabelNode();
				insn.add(l1);
				insn.add(new FieldInsnNode(Opcodes.GETSTATIC, MinecraftForge.class.getName().replace('.', '/'), "EVENT_BUS",
						"L" + EventBus.class.getName().replace('.', '/') + ";"));
				insn.add(new VarInsnNode(Opcodes.ALOAD, 2));
				insn.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, EventBus.class.getName().replace('.', '/'), "post",
						"(L" + Event.class.getName().replace('.', '/') + ";)Z", false));
				LabelNode l2 = new LabelNode();
				insn.add(new JumpInsnNode(Opcodes.IFEQ, l2));
				insn.add(new InsnNode(Opcodes.RETURN));
				insn.add(l2);
				insn.add(new FrameNode(Opcodes.F_APPEND, 1, new Object[] { RawInputEvent.class.getName().replace('.', '/') }, 0, null));
				insn.add(new VarInsnNode(Opcodes.ALOAD, 2));
				insn.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, RawInputEvent.class.getName().replace('.', '/'), "getCode", "()I", false));
				insn.add(new VarInsnNode(Opcodes.ISTORE, 0));
				LabelNode l3 = new LabelNode();
				insn.add(l3);
				insn.add(new VarInsnNode(Opcodes.ALOAD, 2));
				insn.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, RawInputEvent.class.getName().replace('.', '/'), "isPressed", "()Z", false));
				insn.add(new VarInsnNode(Opcodes.ISTORE, 1));
				
				m.instructions.insert(insn);
				break;
			}
		}
		
		ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
		node.accept(cw);
		return cw.toByteArray();
	}
}
