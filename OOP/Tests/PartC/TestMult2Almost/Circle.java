package OOP.Tests.PartC.TestMult2Almost;

public class Circle extends Shape {
    private Integer radius;
    public Circle(Integer rad) {
        super();
        this.radius = rad;
        this.descr = "Circle";
    }

    public Integer getRadius() {
        return this.radius;
    }
}
