package com.alone.common;

import com.google.gson.Gson;

public class GsonStatis {
    private static final Gson gson = new Gson();
    public static Gson instance() {return gson;}
}
