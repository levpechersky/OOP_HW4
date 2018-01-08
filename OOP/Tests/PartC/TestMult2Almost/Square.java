package OOP.Tests.PartC.TestMult2Almost;

public class Square extends Shape {
    private Integer side_len;

    public Square(Integer side) {
        super();
        this.side_len = side;
        this.descr = "Square";
    }

    public Integer getSide() {
        return this.side_len;
    }
}
