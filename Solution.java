import org.junit.*;
import org.junit.runner.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Solution {

    public static void main(String[] args) {
        JUnitCore.main("Solution");
    }

    @Test
    public void testVendingMachine() {

        VendingMachine machine = new VendingMachine();
        Item cookie = new Item(100);
        Item candyBar = new Item(150);
        machine.addItem("1A", cookie);
        machine.addItem("1A", cookie);
        machine.addItem("2B", candyBar);
        machine.addChange(200);

        Assert.assertEquals(2, machine.getItemCount("1A"));
        Assert.assertEquals(100, machine.getItemCost("1A"));

        VendingMachine.VendedItem vended = machine.vend("1A", 125);
        Assert.assertEquals(cookie, vended.item);
        Assert.assertEquals(25, vended.change);

        // show that the item stock is decremented
        Assert.assertEquals(1, machine.getItemCount("1A"));
        // machine started with 200, the user added 125, and the machine vended 25, so we should have 200 + 125 - 25 = 300
        Assert.assertEquals(300, machine.getChange());

        Assert.assertEquals(0, machine.getItemCount("3C"));
        try {
            machine.getItem("3C");
            Assert.fail("Item is out of stock. Machine should throw exception, but it didn't");
        } catch (IllegalArgumentException ex) {}

        try {
            machine.getItemCost("3C");
            Assert.fail("Item is out of stock. Machine should throw exception, but it didn't");
        } catch (IllegalArgumentException ex) {}

        try {
            machine.vend("2B", 100);
            Assert.fail("Insufficient provided change to purchase item. Machine should throw exception, but it didn't");
        } catch (IllegalArgumentException ex) {}

        try {
            machine.vend("2B", 1000);
            Assert.fail("Machine has insufficient change to vend item. Machine should throw exception, but it didn't");
        } catch (IllegalArgumentException ex) {}

    }

    public static class VendingMachine {
        int totalMoney = 0;
        Item cookie = new Item(100);
        Item candyBar = new Item(250);
        //hashmap to keep track of items and their code
        Map<String, List<Item>> inventory = new HashMap<>();

        /**
         * Vend the provided item based on its code. Once the item is vended, it should be removed
         * from the machine's stock and the customer's change added to the machine
         * @param code the item code
         * @param providedChange the change the user provides to purchase the item
         * @return the vended item
         * @throws IllegalArgumentException if the item is out of stock
         * @throws IllegalArgumentException if providedChange is less than the cost of the item
         * @throws IllegalArgumentException if the vending machine has insufficient change to vend the item
         */
        public VendedItem vend(String code, int providedChange) {
            VendedItem customerItem;
            if(inventory.get(code).size() > 0){
               int price = inventory.get(code).get(0).cost;
               if(price > providedChange){
                   throw new IllegalArgumentException("Price greater than provided change");
               } else {
                   int changeForCustomer = (providedChange - price);
                   int difference = providedChange - changeForCustomer;
                   if(totalMoney - changeForCustomer >= 0 ){
                       totalMoney += difference;
                       Item lastItem = inventory.get(code).remove(inventory.get(code).size() - 1);
                       customerItem = new VendedItem(lastItem, changeForCustomer);
                       return customerItem;
                   }
               }
            } else {
                throw new IllegalArgumentException("Item out of stock");
            }
            return null;
        }

        /**
         * Get an item by its code. Throw an exception if not found
         * @return the item
         * @throws IllegalArgumentException if the item is not found or is out of stock
         */
        public Item getItem(String code) {
            throw new IllegalStateException("Implement this!");
        }

        /**
         * Get the number of items with this code. Return 0 if the item is not available or is out of stock
         * @param code the item code
         * @return the number of items
         */
        public int getItemCount(String code) {
            //check if code in machine and check if code has item. If any true return 0. Else return item count
            //associated with code
            if(!inventory.containsKey(code) || inventory.get(code).size() == 0){
                return 0;
            }
            return inventory.get(code).size();
        }

        public int getItemCost(String code) {
            if(!inventory.containsKey(code)){
                return 0;
            }
             int price = inventory.get(code).get(0).cost;
             return price;
        }

        /**
         * Add an item to the vending machine
         * @param code the item code
         * @param item the item
         */
        public void addItem(String code, Item item) {
            //check if code in map, if code exist add item, if not add code and item
            if(inventory.containsKey(code)) {
                inventory.get(code).add(item);
            } else {
                List<Item> newlist = new ArrayList<>();
                newlist.add(item);
                inventory.put(code, newlist);
            }
        }

        /**
         * Remove an item from the vending machine
         * @param code the item code
         * @param item the item
         */
        public void removeItem(String code, Item item) {
            throw new IllegalStateException("Implement this!");
        }

        /**
         * Get the change in the vending machine
         * @return the change in the vending machine
         */
        public int getChange() {
            return totalMoney;
        }

        /**
         * Add change to the vending machine
         * @param change the change to add
         */
        public void addChange(int change) {
            totalMoney +=  change;
        }

        /**
         * Remove change from the vending machine
         * @param change the change to remove
         */
        public void removeChange(int change) {
            throw new IllegalStateException("Implement this!");
        }

        public static class VendedItem {
            Item item;
            int change;
            public VendedItem(Item item, int change) {
                this.item = item;
                this.change = change;
            }
        }

    }

    public static class Item {
        int cost;
        public Item(int cost) {
            this.cost = cost;
        }
    }

}
