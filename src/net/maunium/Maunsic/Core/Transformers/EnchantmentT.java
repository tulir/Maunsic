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

public class EnchantmentT extends AbstractMauTransformer {
	
	public EnchantmentT() {
		super("net.minecraft.enchantment.Enchantment", "apf");
	}
	
	@Override
	public byte[] transform(byte[] bytes, boolean obf) {
		String methodName = obf ? "d" : "getTranslatedName";
		String desc = "(I)Ljava/lang/String;";
		
		ClassNode node = new ClassNode();
		ClassReader cr = new ClassReader(bytes);
		cr.accept(node, 0);
		
		for (MethodNode m : node.methods) {
			if (m.name.equals(methodName) && m.desc.equals(desc)) {
				AbstractInsnNode current = null;
				
				Iterator<AbstractInsnNode> iter = m.instructions.iterator();
				boolean b = false;
				while (iter.hasNext()) {
					current = iter.next();
					if (b && current.getOpcode() == Opcodes.NEW) break;
					else if (current.getOpcode() == Opcodes.NEW) b = true;
				}
				if (current == null) throw new RuntimeException("Could not find correct position!");
				m.instructions.remove(current);
				m.instructions.remove(iter.next());
				m.instructions.remove(iter.next());
				m.instructions.remove(iter.next());
				m.instructions.remove(iter.next());
				AbstractInsnNode insertat = iter.next();
				m.instructions.remove(iter.next());
				m.instructions.remove(iter.next());
				m.instructions.remove(iter.next());
				m.instructions.insert(insertat, new MethodInsnNode(Opcodes.INVOKESTATIC, EnchantmentT.class.getName().replace(".", "/"), "toRoman",
						"(I)Ljava/lang/String;", false));
				break;
			}
		}
		
		ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
		node.accept(cw);
		return cw.toByteArray();
	}
	
	public static String toRoman(int input) {
		if (input == Short.MAX_VALUE) return "Maximum";
		else if (input == Short.MIN_VALUE) return "Minimum";
		else if (input < 1 || input > 3999) return "" + input;
		StringBuffer sb = new StringBuffer();
		while (input >= 1000) {
			sb.append("M");
			input -= 1000;
		}
		while (input >= 900) {
			sb.append("CM");
			input -= 900;
		}
		while (input >= 500) {
			sb.append("D");
			input -= 500;
		}
		while (input >= 400) {
			sb.append("CD");
			input -= 400;
		}
		while (input >= 100) {
			sb.append("C");
			input -= 100;
		}
		while (input >= 90) {
			sb.append("XC");
			input -= 90;
		}
		while (input >= 50) {
			sb.append("L");
			input -= 50;
		}
		while (input >= 40) {
			sb.append("XL");
			input -= 40;
		}
		while (input >= 10) {
			sb.append("X");
			input -= 10;
		}
		while (input >= 9) {
			sb.append("IX");
			input -= 9;
		}
		while (input >= 5) {
			sb.append("V");
			input -= 5;
		}
		while (input >= 4) {
			sb.append("IV");
			input -= 4;
		}
		while (input >= 1) {
			sb.append("I");
			input -= 1;
		}
		return sb.toString();
	}
}
