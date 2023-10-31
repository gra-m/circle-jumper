package fun.madeby.util.utilclasses;

public class BooleanTIntegerVHolder implements GenericHolder<Boolean, Integer> {
    private  Boolean truthValue;
    private  Integer numberValue;

    public BooleanTIntegerVHolder(Boolean val, Integer numberValue) {
        this.truthValue = val;
        this.numberValue = numberValue;
    }

    @Override
    public Boolean getT() {
        return truthValue;
    }


    @Override
    public Integer getV() {
        return numberValue;
    }
}
