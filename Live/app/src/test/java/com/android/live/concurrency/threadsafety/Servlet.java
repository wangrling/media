package com.android.live.concurrency.threadsafety;

public abstract class Servlet {

   abstract void service(Object req, Object resp);

   public void extractFromRequest(Object req) {

   }

   public void encodeIntoResponse(Object resp) {

   }
}
