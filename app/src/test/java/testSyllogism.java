package com.sabakiengineering.syllogizmo;
import org.junit.Test;
import java.util.regex.Pattern;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Based on https://developer.android.com/training/testing/unit-testing/local-unit-tests.html
 * which is obviously very incomplete: how is testSyllogism supposed to find CatStatement
 * MinorPremise etc?
 */
@Test
public class testSyllogism {
    testSyllogismAAA() {
        SylloGizmo.CatStatement minorPremise = new CatStatement("Some", "French", "Generals");
        CatStatement MajorPremise = new CatStatement("All", "Generals", "brave");
        CatStatement Conclusion = new CatStatement("Some", "French", "brave");
        System.out.println("b4 creating syllogism");
        System.out.println("dumpCatStatement on Minor yields: ");
        MinorPremise.dumpCatStatement();
        Syllogism FrenchBravery = new Syllogism(MinorPremise, MajorPremise, Conclusion);
        boolean vrai = FrenchBravery.isValid();
    }
}