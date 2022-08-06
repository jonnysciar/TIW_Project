package it.jonnysciar.html_pure;

public class Main {
    public static void main (String[] args) {
        Test test = new Test(1);
        System.out.println("valore 1: " + test.getVal1());
        System.out.println("valore 2: " + test.getVal2());
    }
}

class Test {
    private int val1;
    private int val2;

    public Test(int val1) {
        this.val1 = val1;
    }

    public int getVal1() {
        return val1;
    }

    public int getVal2() {
        return val2;
    }

    public void setVal2(int val2) {
        this.val2 = val2;
    }
}