package com.devinschwab.eecs405;

/**
 * Created by Devin on 4/17/15.
 */
public class EditDistance {

    public static int calculate(String r, String s) {
        int[][] edMatrix = new int[r.length() + 1][s.length() + 1];

        // first column is simple a count
        for (int i = 0; i < edMatrix.length; i++) {
            edMatrix[i][0] = i;
        }
        // first row is simply a count
        for (int j = 0; j < edMatrix[0].length; j++) {
            edMatrix[0][j] = j;
        }

        for (int i = 1; i < edMatrix.length; i++) {
            for (int j = 1; j < edMatrix[i].length; j++) {
                int cost = Math.min(edMatrix[i-1][j] + 1,
                        edMatrix[i][j-1] + 1);
                if (r.charAt(i-1) == s.charAt(j-1)) {
                    cost = Math.min(cost, edMatrix[i-1][j-1]);
                } else {
                    cost = Math.min(cost, edMatrix[i-1][j-1] + 1);
                }

                edMatrix[i][j] = cost;
            }
        }

        return edMatrix[r.length()][s.length()];
    }
}
