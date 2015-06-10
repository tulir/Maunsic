package net.maunium.Maunsic.Core;

import java.io.IOException;

import net.minecraftforge.fml.common.asm.transformers.AccessTransformer;

/**
 * Basic Forge Access Transformer.
 * 
 * @author Tulir293
 * @since 0.1
 */
public class MaunsicAT extends AccessTransformer {
	public MaunsicAT() throws IOException {
		super("maunsicat.cfg");
	}
}
