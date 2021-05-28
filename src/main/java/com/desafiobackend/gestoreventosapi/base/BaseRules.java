package com.desafiobackend.gestoreventosapi.base;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseRules {

    protected static final List<Boolean> rules = new ArrayList<>();


    public boolean fullValidate() {
        boolean result = !rules.contains(false);
        rules.clear();
        return result;
    }

    public boolean validateAtLeastOne() {
        boolean result =  rules.contains(true);
        rules.clear();
        return result;
    }
}
