package net.maunium.Maunsic.KeyMaucros.Commands;

import net.maunium.Maunsic.Maunsic;

/**
 * The command to print local messages in the chat.
 * 
 * @author Tulir293
 * @since 0.1
 * @from Maucros
 */
public class CommandLocal implements MaucroCommand {
	@Override
	public String getOwner() {
		return "Maucros";
	}
	
	@Override
	public String getName() {
		return "Local";
	}
	
	@Override
	public void execute(String label, String[] args) {
		if (args.length > 0) {
			StringBuffer sb = new StringBuffer();
			for (String s : args)
				sb.append(s + " ");
			// TODO: Use a proper chat formatter.
			Maunsic.printChat("keymaucros.output.local", sb.toString().replace("&", "§"));
		}
	}
}
