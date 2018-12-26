package com.android.mm.jcon;

import java.math.BigInteger;

// 假的Servlet接口
public abstract class BaseServlet {

    public abstract void service();

    protected BigInteger[] factor(BigInteger i) {
        return new BigInteger[0];
    }

    protected void encodeIntoResponse(BigInteger[] bigIntegers) {

    }

    protected BigInteger extractFromRequest() {
        return null;
    }
}
