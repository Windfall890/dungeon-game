package com.jsaop.dungeonGame.gui;

import com.jsaop.dungeonGame.dungeon.Console;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class DialogConsole implements Console {

    private PrintStream consoleOut;
    private ByteArrayOutputStream baos;


    public DialogConsole() {
        baos = new ByteArrayOutputStream();
        consoleOut = new PrintStream(new BufferedOutputStream(baos));
    }

    @Override
    public void write(String s) {
        consoleOut.println(s);
    }

    @Override
    public String readAndFlush() {
        consoleOut.flush();
        String text = baos.toString();
        baos.reset();
        return text;
    }

}
