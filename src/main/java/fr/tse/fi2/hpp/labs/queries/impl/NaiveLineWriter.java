package fr.tse.fi2.hpp.labs.queries.impl;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.tse.fi2.hpp.labs.queries.AbstractLineWriter;

public class NaiveLineWriter extends AbstractLineWriter {

    final static Logger logger = LoggerFactory.getLogger(AbstractLineWriter.class);

    private final BufferedWriter outputWriter;
    private final int id;

    public NaiveLineWriter(final int id, final BufferedWriter outputWriter) {
        super.lines = new LinkedBlockingQueue<String>();
        this.outputWriter = outputWriter;
        this.id = id;
        this.setName("W" + id);
    }

    @Override
    public void run() {

        String line;

        while (true) {
            try {
                line = this.lines.take();
                if (line == "") {
                    break;
                }
                this.writeLine(line);
            } catch (final InterruptedException e) {
                break;
            }
        }
        this.finish();

    }

    @Override
    public void addLine(final String line) {
        this.lines.offer(line);
    }

    public void writeLine(final String line) {
        try {
            this.outputWriter.write(line);
            this.outputWriter.newLine();
        } catch (final IOException e) {
            logger.error("Could not write new line for query processor " + this.id + ", line content " + line, e);
        }

    }

    @Override
    public void finish() {
        try {
            this.outputWriter.flush();
            this.outputWriter.close();
        } catch (final IOException e) {
            logger.error("Cannot property close the output file for query " + this.id, e);
        }
        logger.info("Closing query lineWriter " + this.id);
    }
}
