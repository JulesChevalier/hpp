package fr.tse.fi2.hpp.labs.queries;

import java.util.concurrent.BlockingQueue;

public abstract class AbstractLineWriter extends Thread {

    protected BlockingQueue<String> lines;

    public abstract void addLine(String line);

    public abstract void finish();

}
