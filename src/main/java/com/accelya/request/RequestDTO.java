package com.accelya.request;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class RequestDTO<T> {
    private T lo;
}
