package hearthstone.models;

public class Packet {
    private String functionName;
    private Object[] args;

    public String getFunctionName() {
        return functionName;
    }
    public void setFunctionName(String functionName) {
        this.functionName = functionName;
    }

    public Object[] getArgs() {
        return args;
    }
    public void setArgs(Object[] args) {
        this.args = args;
    }

    public Packet(){

    }

    public Packet(String functionName, Object[] args){
        this.functionName = functionName;
        this.args = args;
    }
}
