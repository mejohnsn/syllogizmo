package com.sabakiengineering.syllogizmo;
// need package access
// now obsolete, since Syllogism requires many more String arguments
/*
public class SyllogismTest {
    public static void main(String[] args)  {
		CatStatement MinorPremise = new CatStatement("Some", "French", "Generals");
		CatStatement MajorPremise = new CatStatement("All", "Generals", "brave");
		CatStatement Conclusion = new CatStatement("Some", "French", "brave");
		System.out.println("b4 creating syllogism");
		System.out.println("dumpCatStatement on Minor yields: ");
		MinorPremise.dumpCatStatement();
		Syllogism FrenchBravery = new Syllogism(MinorPremise,  MajorPremise,  Conclusion);
		boolean vrai = FrenchBravery.isValid();

		System.out.println("FrenchBravery.isValid() returns: ");
		if (vrai==true)	System.out.println("valid");
		else System.out.println("INVALID");
		System.out.println("figure is: "+ FrenchBravery.figure);
		System.out.println("mood is: " + FrenchBravery.mood);

		// Try another, from Wiki HowTO:
		CatStatement MinorPremise2 = new CatStatement("All", "Cows", "Animals");
		CatStatement MajorPremise2 = new CatStatement("No", "Birds", "Cows");
		CatStatement Conclusion2 = new CatStatement("Some", "Animals", "are not", "Birds");  // this is the only one needing the other constructor
		Syllogism BirdCow = new Syllogism(MinorPremise2, MajorPremise2, Conclusion2);
		boolean BirdsAndCows = BirdCow.isValid();
		System.out.println("BirdCow.isValid returns: ");
		if (BirdsAndCows==true) System.out.println("Valid");
		else System.out.println("Invalid");
		System.out.println("figure is: "+ BirdCow.figure);
		System.out.println("mood is: " + BirdCow.mood);

		// Try yet another, also from Wiki HowTO:
		CatStatement MinorPremise3 = new CatStatement("All", "birds", "mortals");
		CatStatement MajorPremise3 = new CatStatement("All", "birds", "animals");
		CatStatement Conclusion3 = new CatStatement("Some", "mortals",  "animals");
		Syllogism Mortal = new Syllogism(MinorPremise3, MajorPremise3, Conclusion3);
		boolean BirdsAreMortal = BirdCow.isValid();
		System.out.println("Mortal.isValid returns: ");
		if (BirdsAreMortal==true) System.out.println("Valid");
		else System.out.println("Invalid");
		System.out.println("figure is: " + Mortal.figure);
		System.out.println("mood is: " + Mortal.mood);
	}
}
*/