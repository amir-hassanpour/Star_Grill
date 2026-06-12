import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;

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

class CashDrawerOpener {
    public static void cashDrawerOpener() {
        PrintService printer = PrintServiceLookup.lookupDefaultPrintService();

        if (printer == null) {
            return;
        }

        byte[] openDrawerCommand = new byte[] {
                0x1B, 0x40,
                0x1B, 0x70, 0x00, 0x64, (byte) 0xFF
        };

        try {
            DocPrintJob job = printer.createPrintJob();

            Doc doc = new SimpleDoc(openDrawerCommand, DocFlavor.BYTE_ARRAY.AUTOSENSE, null);
            job.print(doc, null);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}

class Print {
    public static void print(String fileName) {
        try {
            PrintService printer = PrintServiceLookup.lookupDefaultPrintService();

            if (printer == null) {
                return;
            }

            byte[] fileBytes = Files.readAllBytes(Path.of(fileName));
            DocPrintJob job = printer.createPrintJob();

            Doc doc = new SimpleDoc(fileBytes, DocFlavor.BYTE_ARRAY.AUTOSENSE, null);
            job.print(doc, null);
        }
        catch (Exception e) {
            e.printStackTrace();

        }
    }
}