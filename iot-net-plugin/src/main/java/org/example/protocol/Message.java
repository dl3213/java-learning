package org.example.protocol;

public class Message {
    private int magicNumber;
    private byte version;
    private int len;
    private byte[] content;
}

