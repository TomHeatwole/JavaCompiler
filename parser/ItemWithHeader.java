public abstract class ItemWithHeader extends AbstractSyntaxTree {

    private String accessModifier;
    private String type; // abstract/interface/class
    private boolean isAbstract;
    private boolean isStatic;
    private String name;

    public ItemWithHeader(AbstractSyntaxTree parent){
        super(parent);
    }

    public String getPrivacy() {
        return this.accessModifier;
    }

    public String getType() {
        return this.type;
    }

    public boolean isAbstract() {
        return this.isAbstract;
    }

    public boolean isStatic() {
        return this.isStatic;
    }

    public String getName() {
        return this.name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setAccessModifier(String accessModifier) {
        this.accessModifier = accessModifier;
    }

    public void setAbstract(boolean isAbstract) {
        this.isAbstract = isAbstract;
    }

    public void setStatic(boolean isStatic) {
        this.isStatic = isStatic;
    }
}
