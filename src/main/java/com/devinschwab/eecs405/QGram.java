package com.devinschwab.eecs405;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Devin on 4/8/15.
 */
public class QGram {

    public final int position;
    public final String gram;

    public QGram(int position, String gram) {
        if(gram == null) {
            throw new IllegalArgumentException("gram cannot be null");
        }
        if(gram.equals("")) {
            throw new IllegalArgumentException("gram cannot be empty");
        }
        if(position < 0) {
            throw new IllegalArgumentException("position cannot be negative");
        }

        this.position = position;
        this.gram = gram;
    }

    public int size() {
        return gram.length();
    }

    public boolean subsumes(QGram qgram) {
        if(qgram.position < position)
            return false;
        if(qgram.position+qgram.size() > position + size())
            return false;
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        QGram qGram = (QGram) o;

        if (position != qGram.position) return false;
        return gram.equals(qGram.gram);

    }

    @Override
    public int hashCode() {
        int result = position;
        result = 31 * result + gram.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "(" + position + ", '" + gram + "')";
    }

    public static List<QGram> generateQGrams(int gramSize, String s) {
        if(gramSize <= 0) {
            throw new IllegalArgumentException("gram size must be > 0. " + gramSize);
        }
        List<QGram> grams = new LinkedList<>();
        if(s.length() < gramSize) {
            return grams;
        }
        for (int i=0; i<=s.length()-gramSize; i++) {
            grams.add(new QGram(i, s.substring(i, i+gramSize)));
        }
        return grams;
    }
}
