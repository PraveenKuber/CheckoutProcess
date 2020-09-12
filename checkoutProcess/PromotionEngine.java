package checkoutProcess;

import java.util.*;
import java.util.logging.Logger;

/**
 * Created by praveen on 11/9/20.
 */
public class PromotionEngine {
    private Logger logger = null;
    private Map<String, Integer> itemsMap = null;
    private final int maxPromotionToApply = 2;
    private int comboPromotion = 30;


    public PromotionEngine() {
        logger = Logger.getLogger(PromotionEngine.class.getSimpleName());
        itemsMap = new HashMap<>();
    }

    private void displaySKUItems() {
        System.out.println("** Please find the below SKU items ***");
        for (SKUItems skuItem : SKUItems.values()) {
            System.out.println("Item : " + skuItem + " , Price : " + skuItem.getDisplayLabel());
        }
    }

    private boolean isItemAvailable(String name) {
        boolean isAvailable = false;
        for (SKUItems skuItem : SKUItems.values()) {
            String value = skuItem.name();
            if (value.equals(name)) {
                isAvailable = true;
                break;
            }
        }
        return isAvailable;
    }


    private Cart getCartObject(int value, boolean isPromotionIsApplicable, SKUItems skuItems) {
        int promotionValue;
        int finalCartValue;
        String finalDisplayValue;
        String tempDisp;
        int mod;
        int per;
        if (isPromotionIsApplicable) {
            mod = value / Integer.parseInt(skuItems.getMinValue());
            per = value % Integer.parseInt(skuItems.getMinValue());
            if (mod == 0) {
                promotionValue = Integer.parseInt(skuItems.getDisplayLabel()) * value;
                tempDisp = getPromotionDisplay(value,Integer.parseInt(skuItems.getDisplayLabel()));
                finalDisplayValue = "" + tempDisp;
                finalCartValue = promotionValue;
            } else {
                promotionValue = mod * Integer.parseInt(skuItems.getPromotionValue());
                tempDisp = getPromotionDisplay(mod,Integer.parseInt(skuItems.getPromotionValue()));
                if (per == 0) {

                    finalDisplayValue = "" + tempDisp;
                    finalCartValue = promotionValue;
                } else {
                    finalDisplayValue = tempDisp + " + " + (per + "*" + "" + skuItems.getDisplayLabel());
                    finalCartValue = promotionValue + (per * Integer.parseInt(skuItems.getDisplayLabel()));
                }
            }
        } else {
            finalCartValue = value * Integer.parseInt(skuItems.getDisplayLabel());
            finalDisplayValue = value + "*" + skuItems.getDisplayLabel();
        }
        finalDisplayValue = skuItems.name() + "*" + value + " == " + finalDisplayValue;
        Cart result = new Cart();
        result.setFinalDisplay(finalDisplayValue);
        result.setName(skuItems.name());
        result.setSkuItems(skuItems);
        result.setFinalCartValue(finalCartValue);
        result.setItemCount(value);
        return result;
    }


    private boolean isComboObjectAdded() {
        if (itemsMap.containsKey(SKUItems.C.name()) && itemsMap.containsKey(SKUItems.D.name())) {
            return true;
        }
        return false;
    }

    private void checkOut() {
        int totalCartCount = 0;
        String itemName;
        int itemValue;
        SKUItems skuItems;
        int promotionOnItems = 0;
        Cart cart;
        boolean isComboObjectAdded = isComboObjectAdded();
        boolean allowCombo = true;
        Set<Cart> finalResults = new HashSet<>();
        if(itemsMap.size()!=0){
            for (Map.Entry<String, Integer> map : itemsMap.entrySet()) {
                itemName = map.getKey();
                itemValue = map.getValue();
                skuItems = getSKUItemByName(itemName);
                if (SKUItems.A.name().equals(itemName) || SKUItems.B.name().equals(itemName)) {
                    skuItems = getSKUItemByName(itemName);
                    getSKUItemByName(itemName);
                    if (itemValue >= Integer.parseInt(skuItems.getMinValue())) {
                        if (promotionOnItems < maxPromotionToApply) {
                            cart = getCartObject(itemValue, true, skuItems);
                            finalResults.add(cart);
                            promotionOnItems++;
                        } else {
                            cart = getCartObject(itemValue, false, skuItems);
                            finalResults.add(cart);
                        }
                    } else {
                        cart = getCartObject(itemValue, false, skuItems);
                        finalResults.add(cart);
                    }
                }

            /*Item C and D*/
                if (!isComboObjectAdded && (SKUItems.C.name().equals(itemName) || SKUItems.D.name().equals(itemName) )) {
                    cart = getCartObject(itemValue, false, skuItems);
                    finalResults.add(cart);
                    //totalCartCount = totalCartCount + cart.getFinalCartValue();
                }else if(isComboObjectAdded && allowCombo){
                    if(promotionOnItems < maxPromotionToApply){
                        List<Cart> comboCarts = getComboCartObject();
                        for(Cart cartData:comboCarts){
                            finalResults.add(cartData);
                        }
                        allowCombo = false;
                        promotionOnItems++;
                    }
                }
            }


            for(Cart checkoutCart:finalResults){
                    System.out.println(checkoutCart.getFinalDisplay());
                    totalCartCount = totalCartCount + checkoutCart.getFinalCartValue();
            }


            System.out.println("======================");
            System.out.println("TotalCartCount::::::::" + totalCartCount);
            System.out.println("Total promotions added -> "+promotionOnItems);
        }else{
            System.out.println("Oops ! Your cart is empty");
        }

    }


    private String getPromotionDisplay(int repeat, int promotionValue){
        String tempDisp="";
        for(int i=0;i<repeat;i++){
            tempDisp = tempDisp+"+"+1*promotionValue;
        }
        tempDisp = tempDisp.substring(1,tempDisp.length());
        return tempDisp;
    }


    private SKUItems getSKUItemByName(String name) {
        for (SKUItems skuItem : SKUItems.values()) {
            if (name.equals(skuItem.name())) {
                return skuItem;
            }
        }
        return null;
    }


    private void addItems() {
        System.out.println("*** Please add the items ***");
        System.out.println("** Item names --->");
        EnumSet.allOf(SKUItems.class)
                .forEach(items -> System.out.println(items));
        System.out.println("Enter the item name");
        Scanner name = new Scanner(System.in);
        String itemName = name.next();
        if (isItemAvailable(itemName)) {
            System.out.println("Please enter the number of items for the selected item ( " + itemName + " )");
            Scanner count = new Scanner(System.in);
            int totalCount = count.nextInt();
            if (totalCount > 0) {
                System.out.println("Count::::::" + totalCount);
                if (!itemsMap.containsKey(itemName)) {
                    itemsMap.put(itemName, totalCount);
                    System.out.println(itemName + " name added successfully to cart with count of " + totalCount);
                } else {
                    int total = itemsMap.get(itemName) + totalCount;
                    itemsMap.remove(itemName);
                    itemsMap.put(itemName, total);
                    System.out.println("Item " + itemName + " added with updated count --->" + total);
                }
            } else {
                logger.info("Please add 1 or more items into cart!");
            }
        } else {
            logger.info("The entered item is not available! , Please check the items once");
        }
    }

    private List<Cart> getComboCartObject() {
        Cart comboC = new Cart();
        Cart comboD = new Cart();
        int comboCValue = 0;
        int comboDValue = 0;
        int finalCartValue;
        String tempDisp;
        String finalCartDisplayValue;
        List<Cart> comboCarts = new ArrayList<>();
        int temp;
        int sameUnit;
        for (Map.Entry<String, Integer> map : itemsMap.entrySet()) {
            if (map.getKey().equals(SKUItems.C.name())) {
                comboCValue = map.getValue();
            } else if (map.getKey().equals(SKUItems.D.name())) {
                comboDValue = map.getValue();
            }
        }
        if (comboCValue == comboDValue) {
            finalCartValue = comboPromotion * comboCValue;
            tempDisp = getPromotionDisplay(comboCValue,comboPromotion);
            finalCartDisplayValue = "C and D is "+comboPromotion+"*"+comboCValue+" == "+tempDisp;
            //System.out.println("Same:::"+comboPromotion+"*"+comboCValue+" = "+finalCartValue);
            comboC.setName("C");
            comboC.setSkuItems(SKUItems.C);
            comboC.setFinalDisplay(finalCartDisplayValue);
            comboC.setFinalCartValue(finalCartValue);
            comboC.setItemCount(comboCValue);
            comboCarts.add(comboC);
        } else if (comboCValue > comboDValue) {
            temp = comboCValue - comboDValue;
            sameUnit = comboCValue - temp;
            int discountValue =  sameUnit * comboPromotion ;
            finalCartValue = discountValue + temp * Integer.parseInt(SKUItems.C.getDisplayLabel());
            tempDisp = getPromotionDisplay(sameUnit,comboPromotion);
            finalCartDisplayValue = "C and D "+tempDisp + " + " + temp +" * "+ Integer.parseInt(SKUItems.C.getDisplayLabel());
            /*AddingC*/
            comboC.setFinalCartValue(finalCartValue);
            comboC.setFinalDisplay(finalCartDisplayValue);
            comboC.setSkuItems(SKUItems.C);
            comboC.setName(SKUItems.C.name());
            comboC.setItemCount(comboCValue);
            /*AddingC*/
            comboD.setFinalCartValue(0);
            comboD.setFinalDisplay("-");
            comboD.setSkuItems(SKUItems.D);
            comboD.setFinalDisplay(SKUItems.D.name()+" * "+comboDValue+" ==  -- ");
            comboD.setName(SKUItems.D.name());
            comboD.setItemCount(comboDValue);

            comboCarts.add(comboC);
            comboCarts.add(comboD);
            //System.out.println("Differ 1 finalCartValue:::" + discountValue + "::::: +" + temp * Integer.parseInt(SKUItems.C.getDisplayLabel()));
        } else {
            temp = comboDValue - comboCValue;
            sameUnit = comboDValue - temp;
            int discountValue =  sameUnit * comboPromotion ;
            finalCartValue = discountValue + temp * Integer.parseInt(SKUItems.D.getDisplayLabel());

            tempDisp = getPromotionDisplay(sameUnit,comboPromotion);
            finalCartDisplayValue = "C and D == "+tempDisp + " + " + temp +" * "+Integer.parseInt(SKUItems.D.getDisplayLabel());
            //System.out.println("Differ 2 finalCartValue:::" + discountValue + "::::: +" + temp * Integer.parseInt(SKUItems.D.getDisplayLabel()));
            /*AddingC*/
            comboD.setFinalCartValue(finalCartValue);
            comboD.setFinalDisplay(finalCartDisplayValue);
            comboD.setSkuItems(SKUItems.D);
            comboD.setName(SKUItems.D.name());
            comboD.setItemCount(comboDValue);
            /*AddingC*/
            comboC.setFinalCartValue(0);
            comboC.setFinalDisplay(SKUItems.C.name()+" * "+comboCValue+" ==  -- ");
            comboC.setSkuItems(SKUItems.C);
            comboC.setName(SKUItems.C.name());
            comboC.setItemCount(comboCValue);
            comboCarts.add(comboC);
            comboCarts.add(comboD);
        }
        return comboCarts;
        //return cart;
    }



    public void performOperations() {
        System.out.println("*** Hey ! Good to see you here ***");

        System.out.println("** Promotions\n3 A's for 130\n2 B's for 45\nC and D for 30 **");
        System.out.println("\n\n Explore now!");
        Scanner sc1 = new Scanner(System.in);
        int input;
        do {
            System.out.println("1.Display items and prices \n2.Add the items to cart\n3.Checkout\n4.Remove items in cart\n5.Clear cart\n6.Exit\n");
            try {
                int choice = Integer.parseInt(sc1.next());
                switch (choice) {
                    case 1:
                        displaySKUItems();
                        break;
                    case 2:
                        addItems();
                        break;
                    case 3:
                        checkOut();
                        break;
                    case 4:
                        removeItems();
                        break;
                    case 5:
                        clearCart();
                        break;
                    case 6:
                        System.exit(0);
                        break;
                }
            }catch (Exception e){
                System.out.println("Selection is not correct!");
            }
            System.out.println("Do you want to continue if yes press 1 else 0");
            input = sc1.nextInt();
        } while (input == 1);
    }


    private void removeItems(){
        if(itemsMap.size()!=0){
            System.out.println("*Your cart items*");
            for (Map.Entry<String, Integer> map : itemsMap.entrySet()) {
                System.out.println("Item name -> "+map.getKey()+" , No of items ->"+map.getValue());
            }
            System.out.println("Enter the item name you want to delete");
            Scanner input = new Scanner(System.in);
            String itemName = input.next();
            if(itemsMap.containsKey(itemName)){
                itemsMap.remove(itemName);
                System.out.println("Your items successfully deleted from cart!"+itemName);
            }else{
                System.out.println("Oops! The given item name is not added in the car "+itemName);
            }
        }else{
            System.out.println("Your cart is empty!");
        }
    }

    private void clearCart(){
        if(itemsMap.size()!=0){
            itemsMap = new HashMap<>();
            System.out.println("Cart cleared successfully and Your cart is empty");
        }else{
            System.out.println("No items added , Your cart is empty");
        }
    }


    public static void main(String[] args) {
        PromotionEngine promotionEngine = new PromotionEngine();
        promotionEngine.performOperations();
    }

}
