package com.example.stock.dsbridge;

import android.webkit.JavascriptInterface;

import wendu.dsbridge.CompletionHandler;

public class JsApi {

    //for synchronous invocation
    @JavascriptInterface
    public String testSyn(Object msg) {
        return msg + "［syn call］";
    }

    //for asynchronous invocation
    @JavascriptInterface
    public void testAsyn(Object msg, CompletionHandler handler) {
        handler.complete(msg + " [ asyn call]");
    }
}
