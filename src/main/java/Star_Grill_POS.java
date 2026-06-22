import java.awt.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.*;
import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import java.time.LocalTime;
import java.util.List;
import javax.print.*;

class CustomerNumber {
    public static int customerNumber = 1;

    public static int nextInt() {
        customerNumber++;
        if (customerNumber > 40)
            customerNumber = 1;
        return customerNumber;
    }
}

class Orders {
    public static List<String> orders = new ArrayList<>();

    public static List<String> addOrder(String newOrder) {
        orders.add(newOrder);
        return orders;
    }

    public static void removeOrder (String order) {
        orders.remove(order);
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
    private static final StarGrillTextFileEditor editor = new StarGrillTextFileEditor(
            "Star_Grill_info/Order_History");

    public static void recordAllOrders(String paymentType) {
        List<String> allOrders = new ArrayList<>();

        for (String order: Orders.orders) {
            allOrders.add(order.substring(0, order.length() - 1) + ", " + Time.getHourMinute() + ", " + Time.getDate() + ", " + paymentType);
        }

        editor.addLastLines(allOrders);
    }
}

class Time {
    private Time() {
        /* This utility class should not be instantiated */
    }

    public static String getHourMinute() {
        LocalTime localTime = LocalTime.now();

        int hour = localTime.getHour();
        int minute = localTime.getMinute();
        String minutes = String.valueOf(minute);

        if (minute < 10)
            minutes = "0" + minute;

        return hour + ":" + minutes;
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
    private static List<String> extraDetails = new ArrayList<>();

    public static void addExtraDetails(String extraDetail) {
        extraDetails.add(extraDetail);
    }

    public static List<String> getExtraDetails() {
        return extraDetails;
    }

    public static void toGo() {
        if (!extraDetails.contains("TOGO"))
            extraDetails.add("TOGO");
        else
            extraDetails.remove("TOGO");
    }

    public static void removeExtraDetails() {
        extraDetails = new ArrayList<>();
    }

    public static void removeExtraDetail(String detail) {
        extraDetails.remove(detail);
    }
}

class Payments {
    private static double total = 0;

    public static String getTotal () {
        String totalStr = String.valueOf(total);
        totalStr = totalStr.substring(0,
                totalStr.indexOf(".") + 3 <= totalStr.length() ? totalStr.indexOf(".") + 3 : totalStr.length());
        return totalStr;
    }

    public static double addTotal (double add) {
        total += add;
        return total;
    }

    public static double subtractTotal(double sub) {
        total -= sub;
        return total;
    }

    public static void resetTotal() {
        total = 0;
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
                System.out.println("No default printer found.");
                return;
            }
            Path path = Path.of(fileName);
            if (!Files.exists(path)) {
                System.out.println("File does not exist: " + path.toAbsolutePath());
                return;
            }
            byte[] resetPrinter = new byte[]{0x1B, 0x40};
            ByteArrayOutputStream endingCommands = new ByteArrayOutputStream();
            // Add some space before cutting
            endingCommands.write("\n\n".getBytes(StandardCharsets.US_ASCII));
            // Feed paper
            endingCommands.write(new byte[]{0x1B, 0x64, 0x08});
            // Cut paper
            endingCommands.write(new byte[]{0x1D, 0x56, 0x00});
            InputStream resetStream = new ByteArrayInputStream(resetPrinter);
            InputStream fileStream = Files.newInputStream(path);
            InputStream endingStream = new ByteArrayInputStream(endingCommands.toByteArray());
            Enumeration<InputStream> streams = Collections.enumeration(
                    Arrays.asList(resetStream, fileStream, endingStream)
            );
            try (InputStream finalStream = new SequenceInputStream(streams)) {
                DocPrintJob job = printer.createPrintJob();
                Doc doc = new SimpleDoc(
                        finalStream,
                        DocFlavor.INPUT_STREAM.AUTOSENSE,
                        null
                );
                job.print(doc, null);
            }
            System.out.println("Print job sent to: " + printer.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

class CustomerReceiptPrinter extends ReceiptPrint {
    private static final String customerFileName = "Star_Grill_info/Customer_Receipt";
    private static final StarGrillTextFileEditor editor = new StarGrillTextFileEditor(customerFileName);

    public static void CustomerReceiptPrint() throws IOException {
        editor.removeAllLines();

        editor.addLastLine("      Star Grill");
        editor.addLastLine("Order Number: " + CustomerNumber.customerNumber);
        editor.addLastLine("We would appreciate your review.");

        printTextFile("Star_Grill_info/Customer_Receipt");
    }
}

class OrdersReceiptPrinter extends ReceiptPrint {
    private static final String customerFileNama = "Star_Grill_info/Orders";
    private static StarGrillTextFileEditor editor = new StarGrillTextFileEditor(customerFileNama);

    public static void OrdersReceiptPrint() throws IOException {
        String curTime = Time.getHourMinute();

        editor.removeAllLines();

        editor.addLastLine("Order Number: " + CustomerNumber.customerNumber);

        FoodOrders foodOrders = new FoodOrders();
        DrinkOrders drinkOrders = new DrinkOrders();

        Map<String, Integer> allOrders = new HashMap<>();

        for (String order : foodOrders.getOrders()) {
            if (!allOrders.containsKey(order)) {
                allOrders.put(order, 0);
                for (String orde : foodOrders.getOrders()) {
                    if (orde.equals(order))
                        allOrders.put(order, allOrders.get(order) + 1);
                }
            }
        }

        for (String order : drinkOrders.getOrders()) {
            if (!allOrders.containsKey(order)) {
                allOrders.put(order, 0);
                for (String orde : drinkOrders.getOrders()) {
                    if (orde.equals(order))
                        allOrders.put(order, allOrders.get(order) + 1);
                }
            }
        }

        for (String order : allOrders.keySet())
            editor.addLastLine(order + " X " + allOrders.get(order));


        editor.addLastLines(ExtraDetails.getExtraDetails());
        editor.addLastLine(curTime);

        printTextFile("Star_Grill_info/Orders");
    }
}

class NextReset {
    public static void nextReset(boolean ifTrue) {
        Orders nextResetOrders = new Orders();
        nextResetOrders.clearOrders();

        if (ifTrue)
            CustomerNumber.customerNumber = 1;
        else
            CustomerNumber.nextInt();

        ExtraDetails.removeExtraDetails();
        Payments.resetTotal();
    }
}
