import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import java.time.LocalTime;

class CustomerNumber {
    public static int customerNumber;

    public static void customerNumberInitializer() {
        customerNumber = 1;
    }

    public static int nextInt() {
        customerNumber++;
        if (customerNumber > 40)
            customerNumber = 1;
        return customerNumber;
    }
}

class Orders {
    protected static List<String> orders;

    public List<String> addOrder(String newOrder) {
        orders.add(newOrder);
        return orders;
    }

    public void clearOrders() {
        orders = new ArrayList<>();
    }

    public List<String> getOrders() {
        return orders;
    }
}

class FoodOrders extends Orders {
    @Override
    public List<String> getOrders() {
        List<String> foodOrders = new ArrayList<>();
        for (String order : orders) {
            if (order.contains("!"))
                foodOrders.add(order.substring(0, order.length() - 1));

        }
        return foodOrders;
    }
}

class DrinkOrders extends Orders {
    @Override
    public List<String> getOrders() {
        List<String> drinkOrders = new ArrayList<>();
        for (String order : orders) {
            if (order.contains("@"))
                drinkOrders.add(order.substring(0, order.length() - 1));
        }
        return drinkOrders;
    }
}

class FrozenOrders extends Orders {
    @Override
    public List<String> getOrders() {
        List<String> frozenOrders = new ArrayList<>();
        for (String order : orders) {
            if (order.contains("#"))
                frozenOrders.add(order.substring(0, order.length() - 1));
        }
        return frozenOrders;
    }
}

class ToppingOrders extends Orders {
    @Override
    public List<String> getOrders() {
        List<String> toppingOrders = new ArrayList<>();
        for (String order : orders) {
            if (order.contains("$"))
                toppingOrders.add(order.substring(0, order.length() - 1));
        }
        return toppingOrders;
    }
}

class RecordAllOrders {
    private Star_Grill_Text_File_Editor editor = new Star_Grill_Text_File_Editor("Star_Grill_info/Order_History");

    public void recordAllOrders(String paymentType) {
        List<String> allOrders = new ArrayList<>();

        for (String order: Orders.orders) {
            allOrders.add(order + " " + Time.getHourMinute() + " " + Time.getDate() + " " + paymentType);
        }

        editor.addLastLines(allOrders);
    }
}

class Time {
    public static String getHourMinute() {
        LocalTime localTime = LocalTime.now();

        int hour = localTime.getHour();
        int minute = localTime.getMinute();

        return hour + ":" + minute;
    }

    public static String getDate() {
        LocalDate today = LocalDate.now();

        int year = today.getYear();
        int month = today.getMonthValue();
        int day = today.getDayOfMonth();

        return year + "-" + month + "-" + day;
    }
}

class ExtraDetails {
    private static List<String> extraDetails;

    public static void addExtraDetails(String extraDetail) {
        extraDetails.add(extraDetail);
    }

    public static List<String> getExtraDetails() {
        return extraDetails;
    }

    public static void toGo() {
        extraDetails.add("TOGO");
    }

    public static void removeExtraDetails() {
        extraDetails = new ArrayList<>();
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

class ReceiptPrint {
    public static void printTextFile(String fileName) {
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

class CustomerReceiptPrinter extends ReceiptPrint {
    private static final String customerFileNama = "Star_Grill_info/CustomerReceiptPrinter.txt";
    private static Star_Grill_Text_File_Editor editor = new Star_Grill_Text_File_Editor(customerFileNama);

    public void CustomerReceiptPrint() throws IOException {
        editor.removeAllLines();

        editor.addLastLine("Order Number: " + CustomerNumber.customerNumber);
        editor.addLastLine("We’d appreciate your review!");

        super.printTextFile("Star_Grill_info/CustomerReceiptPrinter.txt");
    }
}

class OrdersReceiptPrinter extends ReceiptPrint {
    private static final String customerFileNama = "Star_Grill_info/Orders.txt";
    private static Star_Grill_Text_File_Editor editor = new Star_Grill_Text_File_Editor(customerFileNama);

    public void OrdersReceiptPrint() throws IOException {
        editor.removeAllLines();

        editor.addLastLine("Order Number: " + CustomerNumber.customerNumber);

        FoodOrders foodOrders = new FoodOrders();
        DrinkOrders drinkOrders = new DrinkOrders();

        editor.addLastLines(foodOrders.getOrders());
        editor.addLastLines(drinkOrders.getOrders());
        editor.addLastLines(ExtraDetails.getExtraDetails());
        editor.addLastLine(Time.getHourMinute());

        super.printTextFile("Star_Grill_info/OrdersReceiptPrinter.txt");
    }
}
