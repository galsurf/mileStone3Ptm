package test;

import java.io.PrintWriter;
import java.util.Scanner;

public class SocketIO implements Commands.DefaultIO {
    Scanner in;
    PrintWriter out;
    public SocketIO(){
        //in = new Scanner

    }

    @Override
    public String readText()  {
        return null;
    }

    @Override
    public void write(String text) {

    }

    @Override
    public float readVal() {
        return 0;
    }

    @Override
    public void write(float val) {

    }
}
