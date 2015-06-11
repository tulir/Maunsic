package net.maunium.Maunsic.KeyMaucros.Commands;

/**
 * The base of Maucros CommandChain commands.
 * 
 * @author Tulir293
 * @since 0.1
 * @from Maucros
 */
public interface MaucroCommand {
	/**
	 * Get the name of the owner mod of this command.
	 */
	public String getOwner();
	
	/**
	 * Get the name of this command.
	 */
	public String getName();
	
	/**
	 * Execute this command.
	 * 
	 * @param label The label used to execute the command. Currently always equals {@link #getName()}, but may later be expanded if command aliases are
	 *            implemented.
	 * @param args The arguments given to the command.
	 */
	public void execute(String label, String[] args);
}
