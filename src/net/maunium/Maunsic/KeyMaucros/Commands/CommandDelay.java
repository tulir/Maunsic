package net.maunium.Maunsic.KeyMaucros.Commands;

import net.maunium.Maunsic.Maunsic;

/**
 * The command to create delays in command chain key maucros.
 * 
 * @author Tulir293
 * @since 0.1
 * @from Maucros
 */
public class CommandDelay implements MaucroCommand {
	@Override
	public String getOwner() {
		return "Maunsic";
	}
	
	@Override
	public String getName() {
		return "Delay";
	}
	
	@Override
	public void execute(String label, String[] args) {
		if (args.length > 0) try {
			Thread.sleep(Integer.parseInt(args[0]));
		} catch (NumberFormatException e) {
			Maunsic.printChatError_static("The MaucroCommand Delay takes one integer.");
		} catch (InterruptedException e) {}
		else Maunsic.printChatError_static("The MaucroCommand Delay takes one integer.");
	}
	
}
