package net.maunium.Maunsic.Core.Transformers;

import java.util.Iterator;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import net.maunium.Maunsic.Maunsic;
import net.maunium.Maunsic.Core.AbstractMauTransformer;
import net.maunium.Maunsic.Logging.ChatLogger;

public class GuiNewChatT extends AbstractMauTransformer {
	public GuiNewChatT() {
		super("net.minecraft.client.gui.GuiNewChat", "buh");
	}
	
	@Override
	public byte[] transform(byte[] bytes, boolean obf) {
		String methodName = obf ? "a" : "printChatMessageWithOptionalDeletion";
		String icc = obf ? "ho" : "net/minecraft/util/IChatComponent";
		
		ClassNode node = new ClassNode();
		ClassReader cr = new ClassReader(bytes);
		cr.accept(node, 0);
		
		for (MethodNode m : node.methods) {
			if (m.name.equals(methodName) && m.desc.equals("(L" + icc + ";I)V")) {
				AbstractInsnNode current;
				
				Iterator<AbstractInsnNode> iter = m.instructions.iterator();
				while (iter.hasNext()) {
					current = iter.next();
					if (current.getOpcode() == Opcodes.GETSTATIC) {
						for (int i = 0; i < 10; i++)
							m.instructions.remove(iter.next());
						
						InsnList insn = new InsnList();
						insn.add(new MethodInsnNode(Opcodes.INVOKESTATIC, Maunsic.class.getName().replace('.', '/'), "getChatLogger",
								"()Lnet/maunium/Maunsic/Logging/ChatLogger;", false));
						insn.add(new VarInsnNode(Opcodes.ALOAD, 1));
						insn.add(new MethodInsnNode(Opcodes.INVOKEINTERFACE, icc, obf ? "c" : "getUnformattedText", "()Ljava/lang/String;", true));
						insn.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, ChatLogger.class.getName().replace('.', '/'), "in", "(Ljava/lang/Object;)V", false));
						m.instructions.insert(current, insn);
						m.instructions.remove(current);
						
						break;
					}
				}
				break;
			}
		}
		
		ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
		node.accept(cw);
		return cw.toByteArray();
	}
}
