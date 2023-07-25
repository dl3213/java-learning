package org.example.protocol;

import lombok.Data;

@Data
public class MsgProtocol {
    private int len;
    private byte[] content;
}
