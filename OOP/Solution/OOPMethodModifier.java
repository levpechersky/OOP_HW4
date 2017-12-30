package OOP.Solution;

public enum OOPMethodModifier {
    PUBLIC(2),
    PROTECTED(1),
    PRIVATE(0);

    private int code;

    private OOPMethodModifier(int code){
        this.code = code;
    }
    public  int code() {
        return this.code;
    }
}
