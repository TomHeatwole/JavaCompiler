public abstract class ItemWithHeader extends AbstractSyntaxTree {

    protected String accessModifier;
    protected String type;
    protected boolean isAbstract;
    protected boolean isStatic;
    protected String name;

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

    public void setName(String name) {
        this.name = name;
    }

    // for debugging
    public String toString() {
        return super.toString() + accessModifier + " " + type + (isStatic ? " static " : " non-static ") +
                (isAbstract ? "abstract " : "non-abstract ") + name;
    }
}
