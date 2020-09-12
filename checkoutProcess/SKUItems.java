package checkoutProcess;

/**
 * Created by praveen on 11/9/20.
 */
public enum SKUItems {
    A(0,"50","3","130"),
    B(1,"30","2","45"),
    C(2,"20","1","20"),
    D(3,"15","1","15");
    private int i;
    private String displayLabel;
    private String minValue;
    private String promotionValue;
    SKUItems(int i,String displayLabel,String minValue,String promotionValue) {
        this.i = i;
        this.displayLabel=displayLabel;
        this.minValue= minValue;
        this.promotionValue=promotionValue;
    }

   /* private int i;
    private String displayLabel;

    CauseCategoryGui(int i, String displayLabel) {
        this.i = i;
        this.displayLabel=displayLabel;
    }*/

    public  String getDisplayLabel()
    {
        return displayLabel;
    }

    public int getI() {
        return i;
    }

    public void setI(int i) {
        this.i = i;
    }

    public void setDisplayLabel(String displayLabel) {
        this.displayLabel = displayLabel;
    }

    public String getMinValue() {
        return minValue;
    }

    public void setMinValue(String minValue) {
        this.minValue = minValue;
    }

    public String getPromotionValue() {
        return promotionValue;
    }

    public void setPromotionValue(String promotionValue) {
        this.promotionValue = promotionValue;
    }
}
