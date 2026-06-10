import java.util.ArrayList;
import java.util.List;

class CustomerNumber {
    private int customerNumber;

    CustomerNumber () {
        customerNumber = 1;
    }

    public int nextInt() {
        customerNumber++;
        if (customerNumber > 40)
            customerNumber = 1;
        return customerNumber;
    }
}

abstract class orders {
    protected List<String> orders;

    public List<String> addOrder(String newOrder) {
        orders.add(newOrder);
        return orders;
    }

    abstract List<String> getOrders();
}

class FoodOrders extends orders {
    @Override
    public List<String> getOrders() {
        List<String> foodOrders = new ArrayList<>();
        for (String order : orders) {
            if (order.contains("!"))
                foodOrders.add(order);
        }
        return foodOrders;
    }
}

class DrinkOrders extends orders {
    @Override
    public List<String> getOrders() {
        List<String> drinkOrders = new ArrayList<>();
        for (String order : orders) {
            if (order.contains("@"))
                drinkOrders.add(order);
        }
        return drinkOrders;
    }
}

class FrozenOrders extends orders {
    @Override
    public List<String> getOrders() {
        List<String> frozenOrders = new ArrayList<>();
        for (String order : orders) {
            if (order.contains("#"))
                frozenOrders.add(order);
        }
        return frozenOrders;
    }
}

class ToppingOrders extends orders {
    @Override
    public List<String> getOrders() {
        List<String> toppingOrders = new ArrayList<>();
        for (String order : orders) {
            if (order.contains("$"))
                toppingOrders.add(order);
        }
        return toppingOrders;
    }
}

class Payments {
    private double total;

    Payments () {
        total = 0;
    }

    public double getTotal () {
        return total;
    }

    public double addTotal (double add) {
        total += add;
        return total;
    }

    public double subtractTotal(double sub) {
        total -= sub;
        return total;
    }
}

class cashOpener {

}

class print {

}