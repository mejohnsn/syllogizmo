package com.sabakiengineering.syllogizmo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Class representing single Categorical Statement:
 * can be used even when we don't know which is major, which
 * minor term.
 *
 * Status: compiles, runs, tested. See SyllogismTest.java for early tests.
 *
 * More status: I finished checking that logic for determining mood
 * letter of MajorPremise matches that of Conclusion. Minor is known
 * to be different, all expect that double negatives ("No French are
 * not brave") are disallowed, but I do not check for this. Candidate for
 * exception?

 * TODO: http://www.friesian.com/syllog.htm, http://www.friesian.com/aristotl.htm contain great
 * material both confirming and expanding on Quine. It suggests the following enhancements:
 * 1) include the letter formulations 'darii', 'celarent' etc. DONE
 * 2) show square of opposition: but what would this really mean doing?
 * 3) apply rules to suggest corrections for inevitable user input
 * mistakes some of these can be applied early on in input process,
 * others cannot: e.g. I can test for existence of middle term b4
 * conclusion is entered. Same for rules 4,5,8
 * 4) build on 1 to show equivalent 1st figure syllogism
 * 5) Button to swap major/minor premises if Syllogism invalid,
 * subtract points if that doesn't make it valid;)
 * 6) fix layout by adding margin to LinearLayout in portrait mode
 *
 * http://www.wikihow.com/Understand-Syllogisms also looks good.
 *
 * How WAS I planning on figure out what figure it is? Do I
 * really need separate logic for determining mood-letter of each
 * CatStatement? A: answer 2 2nd MAY BE yes. Although choice between
 * AI can be done on reading just the quantifier, and btw AIEO inside
 * the whole CatStatement, I need to know which term is the middle
 * term and whether it is subject or predicate to compute figure.
 *
 * old to do item: figure out why Quine includes as "valid with
 * strengthening premise" forms not included on Wikipedia's list. IS
 * there a difference after all between "existential fallacy" and
 * "valid with strengthening"? A: No, since Quine gives an exhaustive
 * list of the strengthening premises and they are all
 * existential. And I have now added the complete list using
 * 'logic.txt', copied from Encyclopedia Britannica
 *
 * Computing the figure: suppose I had a table:
 *
 *     0 - subject position of middle term G
 *     1 - predicate position of mid. term  G
 *
 * Major Premise/Minor Premise  0           1
-------------------------------------------------------------
 *     0                            3           1
 *     1                            4           2
 *
 * Then all I need is the position of the middle term, I don't need
 * the complete breakdown of FGH.
 * 
 * NB: above table logic not complete test for validity: if too many terms
 * entered, it will not catch it, as long as the middle term can be found
 * in both premises. How many rules (below) need to true to supplement the
 * table? I can test that there are exactly three terms using Set (HashSet)
 * to find list of all Terms, make sure there are 3. Methods add(), addAll()
 * size() will do the trick.
 */

class CatStatement {
	protected String X, Y;       // these Strings are terms: no special type for terms. (See if I remember about 'protected' and package right...
	private String Quantifier;    // which must go on X following Quine's pattern
	private String Verb;          // which must be 'are' or 'are not'
	private String[] quantArray = {"All", "Some", "No"};
	private String[] verbArray = {"are", "are not"};

	// Why couldn't I access these from Syllogism? They are in the same package. A: make 'protected' for package access
	CatStatement(String quantifier, String Term1, String Term2) {
		Quantifier = quantifier;      // I can't believe I forgot this!
		X = Term1;
		Y = Term2;
		Verb = "are";
	}
	CatStatement(String quantifier, String Term1, String Verbum, String Term2) {
		Quantifier = quantifier;      // I can't believe I forgot this!
		X = Term1;                  // Now I can't remember where these names
		Y = Term2;                  //  came from:( Quine?
		Verb = Verbum;              // must be 'are' or 'are not', but we do not validate this yet.
	}
	/**
	 * Using index into "all/some/none" and "are/are not".
	 * This is intended to simplify internationalization;
	 * but what I really need is to rewrite setQuantifer()
	 * and setVerb() to work off of index.
	 */
	CatStatement(int qIndex, String Term1, int verbIndex, String Term2) {
		Quantifier = quantArray[qIndex];
		Verb = verbArray[verbIndex];
		X = Term1;
		Y = Term2;
	}

	// Copy Constructor:
	CatStatement(CatStatement propositio ) {
		Quantifier = propositio.Quantifier;
		X = propositio.X;
		Y = propositio.Y;
		Verb = propositio.Verb;

	}

	String getQuantifier() { return Quantifier; }
	int getQuantifierIndex() {
		for (int i=0;i<quantArray.length;i++) {
			if (Quantifier.equals(quantArray[i])) return i;
		}
		return 0; // here for stupid compiler
	}
	int getVerbIndex() {
		for (int i=0;i<verbArray.length;i++) {
			if (Verb.equals(verbArray[i])) return i;
		}
		return 0; // ibidem
	}
    String getVerb() { return Verb; }
	String getSubject() { return X; }
	String getPredicate() { return Y; }

	void setSubject(String sbj) { X = sbj; }
	void setPredicate(String pred) { Y = pred; }
	void setQuantifier(String q) { Quantifier = q; }
	void setQuantifier(int idx) { Quantifier = quantArray[idx]; }
	void setVerb(String vb) { Verb = vb; }
	void setVerb(int idx) { Verb = verbArray[idx]; }
}


/**
 * 
 * @author mejohnsn Class representing a syllogism
 */
public class Syllogism {
	protected String mood;     // AEI or O, three of them.
	protected int figure;      // I was thinking of making all this into one string
	protected String F, G, H;  // minor, middle, major terms. At first, I thought these would be redundant...
	boolean badTerms;          // means something wrong with Terms, i.e. too many

	/**
	 * Not sure if I really want a null constructor, but here goes...
	 */
	Syllogism() {
		mood = null;
		figure = 0; // an illegitimate value: must be 1 - 4.
		System.out.println("WE SHOULD NEVER GET HERE!!!");
	}

	/**
	 * 
	 * This was going to be the constructor I REALLY want: given quantifier,
	 * subject predicate major term and minor term (from conclusion)
	 * from the Syllogism. But I decided it is easier to start when I already
	 * know which is major, which is minor.
	 * 
	 * This constructor assumes categorical statements all of form
	 *  "All|Some|No F are [not] G" etc.
     *
	 * NB: is there any reason the logic should really be different on major and minor premise?
	 * Except, perhaps, for finding figure.
	 */
	Syllogism(CatStatement MinorPremise, CatStatement MajorPremise, CatStatement Conclusion) {
		StringBuilder sb = new StringBuilder();
		// First letter of mood:
		if (MajorPremise.getQuantifier().equals("All"))
			sb.append("A");            // "All" makes it Affirmo
		if (MajorPremise.getQuantifier().equals("Some")) {
			if (MajorPremise.getVerb().equals( "are")) {
				sb.append("I");        // "Some" makes it affIrmo ONLY if verb is 'are'
			}
			else sb.append("O");        // otherwise "negO"
		}
		if (MajorPremise.getQuantifier().equals("No"))
			sb.append("E");         // "No" makes it "nEgo"
		// Now do the same for the Minor Premise to get the second letter
		if (MinorPremise.getQuantifier().equals("All")) sb.append("A");          // "All" makes it Affirmo
		else {
			if (MinorPremise.getQuantifier().equals( "Some")) {
				if (MinorPremise.getVerb().equals("are"))
					sb.append("I");        // "Some" makes it affIrmo ONLY if verb is 'are'
				else sb.append("O");        // otherwise "negO"
			}
		}
		if (MinorPremise.getQuantifier().equals("No"))
			sb.append("E");                                          // "No" makes it nEgo: disregard "no French are not brave"
		// And finally for the Conclusion to get the third and final letter:
		if (Conclusion.getQuantifier().equals("All")) sb.append("A");   // "All" makes it Affirmo
		if (Conclusion.getQuantifier().equals("Some")) {
			if (Conclusion.getVerb().equals("are")) {
				sb.append("I");                                      // "Some" makes it affIrmo ONLY if verb is 'are'
			} else sb.append("O");                                  // otherwise "negO" bcuz "are not"
		}
		if (Conclusion.getQuantifier().equals("No"))
			sb.append("E");                                          // "No" makes it nEgo
		mood = sb.toString();
		// Now set the letters FGH, starting with F and H: minor & major terms resp.
		F = Conclusion.X;
		H = Conclusion.Y;
		if (MinorPremise.X.equals(F)) G = MinorPremise.Y;
		else G = MinorPremise.X;      // since Minor Premise either FG or GF
		// Now test that G also in MajorPremise, badTerms if not, check later
		if (MajorPremise.X.equals(G) || MajorPremise.Y.equals(G)) {
			badTerms = false;     // middle term must occur in both
		}
		else
			badTerms = true; // this test now redundant?
		// And now the figure:
		// Test for first figure first
		if (MajorPremise.X.equals(G) && MinorPremise.Y.equals(G)) {
			if (MajorPremise.Y.equals(H) && MinorPremise.X.equals(F))
				figure = 1;
			else
				badTerms = true;
		}
		if (MajorPremise.Y.equals(G) && MinorPremise.Y.equals(G)) {
			if (MajorPremise.X.equals(H) && MinorPremise.X.equals(F))
				figure = 2;
			else
				badTerms = true;
		}
		if (MajorPremise.X.equals(G) && MinorPremise.X.equals(G)) {
			if (MajorPremise.Y.equals(H) && MinorPremise.Y.equals(F))
			figure = 3;
			else badTerms = true;
			}
		if (MajorPremise.Y.equals(G) && MinorPremise.X.equals(G)) {
			if (MajorPremise.X.equals(H) && MinorPremise.Y.equals(F))
			figure = 4;
			else badTerms = true;
			}
	}
	// NB: these work bcuz mood/figure/mnemonic computed by Constructor: so
	// client programmer can never get at them until they are valid.
	String getMood() { return mood; }
	int getFigure() { return figure; }
	/**
	 * Ret. medieval mnemonic e.g. 'Barbara', "Darii"...
	 * How will HashMap help me here? Key = Mood, value = (figure, mnemonic)?
	 * Or would it be easier the other way around? Lookup on figure 1st, ret. array of strings listing valid mnemonics for this figure
	 * I could build on extracting vowels from mnemonics, compare in code to mood, or even an equals() op that looks at vowels only,
	 * but what is the point? I don't think this buys me much.
	 */
	String getMedMnemonic() {

	// array for fig 1st looks like:
	//   fig=1: ("AAA", "Barbara"), ("EAE", "Celarent"), ("AII", "Darii")
	//   etc.
	//
	// fig 1st xlates into code as:
	Map<String, String>m1 = new HashMap<String, String>();
	m1.put("AAA", "Barbara");m1.put("EAE", "Celarent"); m1.put("AII", "Darii");m1.put("EIO", "Ferio");  m1.put("AAI", "*Barbari"); m1.put("EAO", "*Celaront");
	Map<String, String>m2 = new HashMap<String, String>();
    m2.put("EAE", "Cesare");m2.put("AEE", "Camestres");m2.put("EIO","Festino");m2.put("AOO","Baroco"); m2.put("EAO", "*Cesaro"); m2.put("AEO", "*Camestrop");
	Map<String, String>m3 = new HashMap<String, String>();
    m3.put("AAI","Darapti");m3.put("IAI","Disamis");m3.put("AII","Datisi");m3.put("EAO","Felapton");m3.put("OAO","Bocardo");m3.put("EIO","Ferison");
	Map<String, String>m4 = new HashMap<String, String>();
	m4.put("AAI","Bramantip");m4.put("AEE","Camenes");m4.put("IAI","Dimaris");m4.put("EAO","Fesapo");m4.put("EIO","Fresison"); m4.put("AEO", "*Camenop");
	//m[Map<String, String>] = { m1, m2, m3, m4}; // this is the syntax I want, but no. It is:
	ArrayList<Map<String, String>> m = new ArrayList();
	m.add(m1);m.add(m2);m.add(m3);m.add(m4);

	// So to get the mnemonic given mood & figure:
	if ((figure<0) || (figure>5)) return ("");          // But first, check for outside values:
	if (badTerms) return ("");
	Map<String, String> thisFiguresMap = m.get(figure-1);  // Now we know figure OK
	if (thisFiguresMap.containsKey(mood))
		return(thisFiguresMap.get(mood));
		else return("");
	}

	/**
	 * test if given syllogism is valid
	 * 
	 * @return boolean 'true' if this syllogism is valid. Otherwise false
	 *
	 * The table to test against is:
	 * 1 AAA EAE AII EIO         barbara  celarent   darii    ferio
	 * 2 EAE AEE EIO AOO        cesare   camestres festino  baroco
	 * 3 IAI AII OAO EIO          disamis  datisi     bocardo ferison
	 * 4 AEE IAI EIO               camenes dimaris   fresison
	 * This is all 15 valid syllogisms: but then why does Wikipedia list
	 * 19? Adding the list valid "with added premise" yields 23, not 19.
	 *
	 * OTOH, if this test fails, there is no hint to the user what went wrong.
	 * If instead I use the rules from http://www.friesian.com/aristotl.htm,
	 * the user will know where his error is. These are:
	 *1) There are only three terms in a syllogism (by definition).
	 *2) The middle term is not in the conclusion (by definition).
	 *3) The quantity of a term cannot become greater in the conclusion.
	 *4) The middle term must be universally quantified in at least one premise.
	 *5) At least one premise must be affirmative.
	 *6) If one premise is negative, the conclusion is negative.
	 *7) If both premises are affirmative, the conclusion is affirmative.
	 *8) At least one premise must be universal.
	 *9) If one premise is particular, the conclusion is particular.
	 *10) In extensional logic, if both premises are universal, the conclusion
	 *    is universal.  (See DARAPTI, etc., and "In Defense of Bramantip")
	 *
	 */
	boolean isValid() {
		if (badTerms) { return false; }
		switch (figure) {
		case 1:
			if (mood.equals("AAA") || mood.equals("EAE") || mood.equals("AII") || mood.equals("EIO")) return true;
			break;
		case 2:
			if (mood.equals("EAE") || mood.equals("AEE") || mood.equals("EIO") || mood.equals("AOO")) return true;
			break;
		case 3:
			if (mood.equals("IAI") || mood.equals("AII") || mood.equals("OAO") || mood.equals("EIO")) return true;
			break;
		case 4:
			if (mood.equals("IAI") || mood.equals("AEE") || mood.equals("EIO")) return true;
			break;
		default: // this is an error: there are only 4 figures!
			 return false;
		}
		return false;
	}
	/**
	 * Tests if valid with added premise "There are F" etc. Ret. false for
	 * all others (including those passing isValid test)
	 * @return
	 */
	boolean isValidWithAddedPremise() {
		switch (figure) {
		case 1:
			if (mood.equals("AAI") || mood.equals("EAO")) return true;      // not in Wikipedia's list: Barbari & Celaront
			break;
		case 2:
			if (mood.equals("EAO") || mood.equals("AEO")) return true;     // not in Wikipedia's list either:  Cesaro & Camestrop
			break;
		case 3:
			if (mood.equals("AAI") || mood.equals("EAO")) return true;      // darapti or felapton
			break;
		case 4:                                                                  // bramantip fesapo; aeo not in Wikipedia's list: Camenop
			if (mood.equals("AAI") || mood.equals("EAO") || mood.equals("AEO")) return true;
			break;
			default:       // this is an error: there are only 4 figures!
				return false;
		}
		return false;
	}
}