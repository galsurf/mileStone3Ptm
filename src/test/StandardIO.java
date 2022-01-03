package test;

import test.Commands.DefaultIO;

import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Scanner;


public class StandardIO implements DefaultIO {

    Scanner in;
    PrintWriter out;
    public StandardIO(){
        try {
            in = new Scanner(new InputStreamReader(System.in));
            out = new PrintWriter(System.out);
        } catch (Exception e) {}
    }

    @Override
    public String readText() {
        return in.nextLine();
    }
        @Override
    public void write(String text) {
            out.print(text);
    }

    @Override
    public float readVal() {
        return in.nextFloat();
    }

    @Override
    public void write(float val) {
        out.print(val);
    }
}
