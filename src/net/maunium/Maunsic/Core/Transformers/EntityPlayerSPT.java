package net.maunium.Maunsic.Core.Transformers;

import java.util.Iterator;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

import net.maunium.Maunsic.Core.AbstractMauTransformer;
import net.maunium.Maunsic.Core.ClientChatSendEvent;

/**
 * Transformer to add a caller for the ClientChatSendEvent.
 * 
 * @author Tulir293
 * @since 0.1
 * @from MauEventLib
 */
public class EntityPlayerSPT extends AbstractMauTransformer {
	public EntityPlayerSPT() {
		super("net.minecraft.client.entity.EntityPlayerSP", "cio");
	}
	
	@Override
	public byte[] transform(byte[] bytes, boolean obf) {
		String methodName1 = obf ? "e" : "sendChatMessage";// , methodName2 = obf ? "m" : "onLivingUpdate";
		
		ClassNode node = new ClassNode();
		ClassReader cr = new ClassReader(bytes);
		cr.accept(node, 0);
		
		int d = 0;
		for (MethodNode m : node.methods) {
			if (m.name.equals(methodName1) && m.desc.equals("(Ljava/lang/String;)V")) {
				AbstractInsnNode current;
				
				Iterator<AbstractInsnNode> iter = m.instructions.iterator();
				int i = 0;
				while (iter.hasNext()) {
					current = iter.next();
					if (current.getOpcode() == Opcodes.INVOKESPECIAL || current.getOpcode() == Opcodes.INVOKEVIRTUAL) {
						if (i > 0)
							m.instructions.insert(current, new MethodInsnNode(Opcodes.INVOKESTATIC, ClientChatSendEvent.class.getName().replace(".", "/"),
									"sendChatMessage", "(Ljava/lang/String;)V", false));
						m.instructions.remove(current);
						i++;
					}
				}
				d++;
				if (d == 2) break;
//			} else if (m.name.equals(methodName2) && m.desc.equals("()V")) {
//				AbstractInsnNode current;
//				
//				Iterator<AbstractInsnNode> iter = m.instructions.iterator();
//				While: while (iter.hasNext()) {
//					current = iter.next();
//					if (current.getOpcode() == Opcodes.INVOKESPECIAL) {
//						MethodInsnNode min = (MethodInsnNode) current;
//						if (min.name.equals(obf ? "" : "onLivingUpdate")) {
//							m.instructions.insert(current, new MethodInsnNode(Opcodes.INVOKESTATIC, PlayerUpdateEvent.class.getName().replace('.', '/'),
//									"call", "()V", false));
//							break While;
//						}
//					}
//				}
//				d++;
//				if (d == 2) break;
			}
		}
		
		ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
		node.accept(cw);
		return cw.toByteArray();
	}
}
