package fr.iutvalence.projet.battleArenaGame.spell;

import java.io.Serializable;

/**
 * This class represents a Spell page it is composed of 3 spells.
 * A spell page has a name.
 * each pawn have one spell page.
 * @author durantho
 */

public class SpellPage implements Serializable{
	
	/**
	 * serialVersionUID is an hash code, which allow the JVM to check if attributes, names and type are the same for the object
	 */
	private static final long serialVersionUID = 474283235313170067L;

	/**
	 * name of the page.
	 */
	private String pageName;
	
	/**
	 * first spell of the page.
	 */
	private Spell spell1;
	
	/**
	 * second spell of the page.
	 */
	private Spell spell2;
	
	/**
	 * third spell of the page.
	 */
	private Spell spell3;

	/**
	 * Constructor of a spell page
	 * @param pName The name you give to this SpellPage.
	 */
	public SpellPage(String pName)
	{
		this.pageName = pName;
		
		this.spell1 = new Spell();
		this.spell2 = new Spell();
		this.spell3 = new Spell();
	}
	
	/**
	 * @return the name off the page.
	 */
	public String getPageName()
	{
		return this.pageName;
	}
	/**
	 * return the fist spell.
	 * @return spell1 : the spell
	 */
	public Spell getSpell1()
	{
		return this.spell1;
	}
	
	/**
	 * return the second spell.
	 * @return spell2 : the spell
	 */
	public Spell getSpell2()
	{
		return this.spell2;
	}
	
	/**
	 * return the third spell.
	 * @return spell2 : the spell
	 */
	public Spell getSpell3()
	{
		return this.spell3;
	}
	

	/**
	 * Return the current state of the page,
	 * name
	 * spell1
	 * spell2
	 * spell3
	 */
	public String toString() {
		return "SpellPage [pageName=" + pageName + ", spell1=" + spell1 + ", spell2=" + spell2 + ", spell3=" + spell3 + "]";
	}
	
}
