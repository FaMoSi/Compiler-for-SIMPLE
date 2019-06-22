package util;

import models.SimpleParameter;

import java.util.List;

public class Params {

    List<SimpleParameter> simpleParameters;

    public Params(List<SimpleParameter> simpleParameters){
        this.simpleParameters = simpleParameters;
    }

    public List<SimpleParameter> getSimpleParameters() { return simpleParameters; }
}
