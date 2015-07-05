package net.maunium.Maunsic.Core.Transformers;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import net.maunium.Maunsic.Actions.ActionXray;
import net.maunium.Maunsic.Core.AbstractMauTransformer;

public class BlockT extends AbstractMauTransformer {
	
	public BlockT() {
		super("net.minecraft.block.Block", "atr");
	}
	
	@Override
	public byte[] transform(byte[] bytes, boolean obf) {
		String methodName = obf ? "a" : "shouldSideBeRendered";
		String bC = obf ? "atr" : "net/minecraft/block/Block";
		String bpC = obf ? "dt" : "net/minecraft/util/BlockPos";
		String bsC = obf ? "bec" : "net/minecraft/block/state/IBlockState";
		String baC = obf ? "ard" : "net/minecraft/world/IBlockAccess";
		String efC = obf ? "ej" : "net/minecraft/util/EnumFacing";
		String desc = "(L" + baC + ";L" + bpC + ";L" + efC + ";)Z";
		
		ClassNode node = new ClassNode();
		ClassReader cr = new ClassReader(bytes);
		cr.accept(node, 0);
		
		for (MethodNode m : node.methods) {
			if (m.name.equals(methodName) && m.desc.equals(desc)) {
				InsnList inject = new InsnList();
				
				inject.add(new MethodInsnNode(Opcodes.GETSTATIC, ActionXray.class.getName().replace('.', '/'), "enabled", "Z", false));
				LabelNode l = new LabelNode();
				inject.add(new JumpInsnNode(Opcodes.IFEQ, l));
				inject.add(new VarInsnNode(Opcodes.ALOAD, 1));
				inject.add(new VarInsnNode(Opcodes.ALOAD, 2));
				inject.add(new MethodInsnNode(Opcodes.INVOKEINTERFACE, baC, obf ? "p" : "getBlockState", "(L" + bpC + ";)L" + bsC + ";", true));
				inject.add(new MethodInsnNode(Opcodes.INVOKEINTERFACE, bsC, obf ? "c" : "getBlock", "()L" + bC + ";", true));
				inject.add(new MethodInsnNode(Opcodes.INVOKESTATIC, ActionXray.class.getName().replace(".", "/"), "isDisabled", "(L" + bC + ";)Z", false));
				inject.add(new JumpInsnNode(Opcodes.IFEQ, l));
				inject.add(new InsnNode(Opcodes.ICONST_1));
				inject.add(new InsnNode(Opcodes.IRETURN));
				inject.add(l);
				m.instructions.insert(inject);
			}
		}
		
		ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
		node.accept(cw);
		return cw.toByteArray();
	}
}
