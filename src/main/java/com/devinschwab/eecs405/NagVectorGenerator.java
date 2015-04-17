package com.devinschwab.eecs405;

import java.util.*;

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
        gramDict.generateInverseTrie();
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
        List<QGram> grams = gramDict.generateVGrams(s);

        List<Integer> affectedGramCounts = new ArrayList<>(2 * s.length() + 1);

        // get the affected grams for deletions/substitutions
        for (int i = 0; i < s.length(); i++) {
            affectedGramCounts.add(numPotentiallyAffectedGrams(s, grams, i, false));
        }

        // get the affected grams for insertions
        for (int i = 0; i <= s.length(); i++) {
            affectedGramCounts.add(numPotentiallyAffectedGrams(s, grams, i, true));
        }

        // sort the list
        Collections.sort(affectedGramCounts, Collections.reverseOrder());

        List<Integer> nagVector = new ArrayList<>(numVectors);
        int lastNagValue = 0;
        for (int i = 0; i < Math.min(numVectors, affectedGramCounts.size()); i++) {
            lastNagValue += affectedGramCounts.get(i);
            // Number of affected grams cannot be greater than the total number of grams for a string
            lastNagValue = Math.min(lastNagValue, grams.size());
            nagVector.add(lastNagValue);
        }

        return nagVector;
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

        // filter category 1
        Iterator<QGram> gramIter = potentiallyAffectedGrams.iterator();
        while (gramIter.hasNext()) {
            if (checkCategory1(s, gramIter.next(), charIndex, isInsertion)) {
                gramIter.remove();
            }
        }

        if (potentiallyAffectedGrams.size() == 0) {
            return 0;
        }

        // check category 2, 3, and 4
        int numAffected = 0;
        for (int category = 2; category <= 4; category++) {
            gramIter = potentiallyAffectedGrams.iterator();
            while (gramIter.hasNext()) {
                if (checkCategory(category, s, gramIter.next(), charIndex, isInsertion)) {
                    numAffected++;
                    gramIter.remove();
                }
            }

            // stop if all grams have beem eliminated
            if (potentiallyAffectedGrams.size() == 0) {
                return numAffected;
            }
        }
        return numAffected;
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
            return true;
        }

        // if the end of the gram is greater than the upper bound of potential effects
        // this gram cannot be affected
        int b;
        if (!isInsertion) {
            b = Math.min(s.length(), charIndex + gramDict.getQMax() - 1);
        } else {
            b = Math.min(s.length(), charIndex + gramDict.getQMax() - 2);
        }
        if (gram.position + gram.size() - 1 > b) {
            return true;
        }

        return false;
    }

    private boolean checkCategory2(String s, QGram gram, int charIndex, boolean isInsertion) {

        // if gram starts after character index
        if ((!isInsertion && gram.position > charIndex)
                || (isInsertion && gram.position >= charIndex)){
            return false;
        }

        // if gram ends before character index
        if (gram.position + gram.size() - 1 < charIndex) {
            return false;
        }

        return true;
    }

    private boolean checkCategory3(String s, QGram gram, int charIndex, boolean isInsertion) {

        int a = Math.max(0, charIndex - gramDict.getQMax() + 1);
        if (a > gram.position) {
            return false;
        }

        if (gram.position + gram.size() - 1 > charIndex - 1) {
            return false;
        }

        for (int j = a; j <= charIndex - gramDict.getQMin() + 1; j++) {
            if (j > gram.position) {
                return false;
            }
            if (gramDict.dictionaryTrie.getExtendedQGrams(s.substring(j, charIndex), false).size() > 0) {
                return true;
            }
        }

        return false;
    }

    private boolean checkCategory4(String s, QGram gram, int charIndex, boolean isInsertion) {
        int rightOfCharIndex;
        if (!isInsertion) {
            rightOfCharIndex = charIndex + 1;
        } else {
            rightOfCharIndex = charIndex;
        }
        if (gram.position < rightOfCharIndex) {
            return false;
        }

        int b;
        if (!isInsertion) {
            b = Math.min(s.length(), charIndex + gramDict.getQMax() - 1);
        } else {
            b = Math.min(s.length(), charIndex + gramDict.getQMax() - 2);
        }
        if (gram.position + gram.size() - 1 > b) {
            return false;
        }

        for (int j = b; j >= rightOfCharIndex; j--) {
            if (j < gram.position + gram.size() - 1) {
                return false;
            }
            String suffix = new StringBuilder(s.substring(rightOfCharIndex, j+1)).reverse().toString();
            if (gramDict.inverseTrie.getExtendedQGrams(suffix, false).size() > 0) {
                return true;
            }
        }
        return false;
    }
}
