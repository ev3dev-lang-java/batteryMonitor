package ev3dev.mocks.metatest;

public class UpperClass {

    final InsideClass insideClass;

    public UpperClass(final InsideClass insideClass){
        this.insideClass = insideClass;
    }

    public int methodA(){
        return this.insideClass.methodA() * 2;
    }

}
