package me.sibyl.util.object;

import lombok.SneakyThrows;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;

public class CloneTest {

    @SneakyThrows
    public static void main1(String[] args) {
        Object o = new Object();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(o);

    }
}
