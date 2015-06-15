package net.maunium.Maunsic.Util;

import java.util.regex.Pattern;

import com.github.darius.expr.Parser;
import com.github.darius.expr.SyntaxException;
import com.github.darius.expr.Variable;

import net.maunium.Maunsic.Maunsic;

import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;

/**
 * A simplified interface for Expr by Darius.
 * 
 * @author Tulir293
 * @since 0.1
 * @from Maucros
 */
public class Calculator {
	private static Variable ans = Variable.make("ans");
	private static Variable answer = Variable.make("answer");
	
	/**
	 * Process the given expression.
	 * 
	 * @param s The expression to process.
	 * @return The result, "varname" if the expression was a variable setting one and contained an invalid variable name or "syntax" if the syntax was
	 *         incorrect. If the return is "syntax" the error has already been printed to the chat.
	 */
	public static String processCalculation(String s) {
		if (s.contains("=")) {
			String[] ss = s.split(Pattern.quote("="));
			ss[0] = ss[0].trim();
			if (ss[0].contains(" ")) { return "varname"; }
			ss[1] = ss[1].trim();
			double value;
			try {
				value = calculate(ss[1]);
			} catch (SyntaxException e) {
				catchSyntax(e);
				return "syntax";
			}
			Variable.make(ss[0]).setValue(value);
			return I18n.translate("message.calculator.variable.setvalue", ss[0], value);
		} else {
			double value;
			try {
				value = calculate(s.trim());
			} catch (SyntaxException e) {
				catchSyntax(e);
				return "syntax";
			}
			ans.setValue(value);
			answer.setValue(value);
			return I18n.translate("message.calculator.result", s, value);
		}
	}
	
	@SuppressWarnings("deprecation")
	public static void catchSyntax(SyntaxException e) {
		Maunsic.printChatError_static("A wild Syntax Exception appears!");
		Maunsic.getLogger().catching(e);
		for (String ss : e.explain().split("\n"))
			Maunsic.printChat_static(ss, new ChatStyle().setColor(EnumChatFormatting.RED));
	}
	
	private static double calculate(String s) throws SyntaxException {
		return Parser.parse(s).value();
	}
}
