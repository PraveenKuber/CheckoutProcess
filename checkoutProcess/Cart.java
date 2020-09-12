package checkoutProcess;

/**
 * Created by praveen on 11/9/20.
 */
public class Cart {
    private SKUItems skuItems;
    private String name;
    private int finalCartValue;
    private String finalDisplay;
    private int itemCount;

    public SKUItems getSkuItems() {
        return skuItems;
    }

    public void setSkuItems(SKUItems skuItems) {
        this.skuItems = skuItems;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getFinalCartValue() {
        return finalCartValue;
    }

    public void setFinalCartValue(int finalCartValue) {
        this.finalCartValue = finalCartValue;
    }

    public String getFinalDisplay() {
        return finalDisplay;
    }

    public void setFinalDisplay(String finalDisplay) {
        this.finalDisplay = finalDisplay;
    }

    public int getItemCount() {
        return itemCount;
    }

    public void setItemCount(int itemCount) {
        this.itemCount = itemCount;
    }

    public String toString()
    {
        return name + " " + name + " " + finalCartValue + " " + finalDisplay +" "+itemCount;
    }

}