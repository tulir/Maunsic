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
import org.objectweb.asm.tree.VarInsnNode;

import net.maunium.Maunsic.Actions.ActionBlink;
import net.maunium.Maunsic.Core.AbstractMauTransformer;

/**
 * Transformer to allow blinking.
 * 
 * @author Tulir293
 * @since 0.1
 * @from Maucros
 */
public class NetworkManagerT extends AbstractMauTransformer {
	public NetworkManagerT() {
		super("net.minecraft.network.NetworkManager", "gr");
	}
	
	@Override
	public byte[] transform(byte[] bytes, boolean obf) {
		String methodName = obf ? "a" : "dispatchPacket";
		String packet = obf ? "id" : "net/minecraft/network/Packet";
		String desc = "(L" + packet + ";[Lio/netty/util/concurrent/GenericFutureListener;)V";
		
		ClassNode node = new ClassNode();
		ClassReader cr = new ClassReader(bytes);
		cr.accept(node, 0);
		
		for (MethodNode m : node.methods) {
			if (m.name.equals(methodName) && m.desc.equals(desc)) {
				InsnList inject = new InsnList();
				inject.add(new FrameNode(Opcodes.F_SAME, 0, null, 0, null));
				inject.add(new FieldInsnNode(Opcodes.GETSTATIC, ActionBlink.class.getName().replace('.', '/'), "active", "Z"));
				LabelNode ln = new LabelNode();
				inject.add(new JumpInsnNode(Opcodes.IFEQ, ln));
				inject.add(new VarInsnNode(Opcodes.ALOAD, 1));
				inject.add(new MethodInsnNode(Opcodes.INVOKESTATIC, ActionBlink.class.getName().replace(".", "/"), "blink", "(L" + packet + ";)V", false));
				inject.add(new InsnNode(Opcodes.RETURN));
				inject.add(ln);
				m.instructions.insert(inject);
			}
		}
		
		ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
		node.accept(cw);
		return cw.toByteArray();
	}
}
