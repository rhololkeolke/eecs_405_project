package com.devinschwab.eecs405;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Created by Devin on 4/14/15.
 */
public class NagVectorGenerator {

    public GramDictionary gramDict;

    /**
     * Construct a Number of Affected Grams (NAG) Generator class using the specified Gram Dictionary.
     *
     * @param gramDict Dictionary to use when generating a NAG vector
     */
    public NagVectorGenerator(GramDictionary gramDict) {
        this.gramDict = gramDict;
    }

    /**
     * For a given string, use this generators Dictionary to generate the VGRAMS. Then calculate the NAG vector,
     * NAG(s, k), values for k in range [1, numVectors].
     *
     * If numVectors is < 1 then IllegalArgumentException will be thrown.
     *
     * @param s String to generate NAG for
     * @param numVectors Max size of NAG vector
     * @return The list of NAG values for the NAG vector
     */
    public List<Integer> generate(String s, int numVectors) {
        return null;
    }

    /**
     * For a given string and its associated vgrams determine the number of potentially affected grams if the given
     * character had an edit operation applied to it (i.e. deletion, substitution, insertion).
     *
     * When isInsertion is false, charIndex must be in range [0, |s|). When true, charIndex must be in range [0, |s|].
     * Otherwise IllegalArgumentException will be thrown.
     *
     * @param s String to calculate number of potentially affected grams for
     * @param vgrams The set of vgrams for the given string
     * @param charIndex The character index to apply an edit operation to
     * @param isInsertion Specifies the edit operation is a substitution, deletion or insertion
     * @return The number of potentially affected grams
     */
    public int numPotentiallyAffectedGrams(String s, List<QGram> vgrams, int charIndex, boolean isInsertion) {
        Set<QGram> potentiallyAffectedGrams = new HashSet<>(vgrams);

        // check category 1
        for (int category = 1; category <= 4; category++) {
            Iterator<QGram> gramIter = potentiallyAffectedGrams.iterator();
            while (gramIter.hasNext()) {
                if (!checkCategory(category, s, gramIter.next(), charIndex, isInsertion)) {
                    gramIter.remove();
                }
            }

            // stop if all grams have beem eliminated
            if (potentiallyAffectedGrams.size() == 0) {
                return 0;
            }
        }
        return potentiallyAffectedGrams.size();
    }

    protected boolean checkCategory(int category, String s, QGram gram, int charIndex, boolean isInsertion) {
        switch (category) {
            case 1:
                return checkCategory1(s, gram, charIndex, isInsertion);
            case 2:
                return checkCategory2(s, gram, charIndex, isInsertion);
            case 3:
                return checkCategory3(s, gram, charIndex, isInsertion);
            case 4:
                return checkCategory4(s, gram, charIndex, isInsertion);
            default:
                System.err.println("Category " + category + " does not exist");
        }
        return false;
    }

    private boolean checkCategory1(String s, QGram gram, int charIndex, boolean isInsertion) {

        // if start of gram is less than lower bound of potential effects
        // this gram cannot be affected
        int a = Math.max(0, charIndex - gramDict.getQMax() + 1);
        if (gram.position < a) {
            return false;
        }

        // if the end of the gram is greater than the upper bound of potential effects
        // this gram cannot be affected
        int b;
        if (isInsertion) {
            b = Math.min(s.length(), charIndex + gramDict.getQMax() - 1);
        } else {
            b = Math.min(s.length(), charIndex + gramDict.getQMax() - 2);
        }
        if (gram.position + gram.size() - 1 > b) {
            return false;
        }

        return true;
    }

    private boolean checkCategory2(String s, QGram gram, int charIndex, boolean isInsertion) {
        return false;
    }

    private boolean checkCategory3(String s, QGram gram, int charIndex, boolean isInsertion) {
        return false;
    }

    private boolean checkCategory4(String s, QGram gram, int charIndex, boolean isInsertion) {
        return false;
    }
}
