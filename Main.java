import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.InputMismatchException;
import java.util.Locale;
import java.util.Scanner;

public class Main {
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_PURPLE = "\u001B[95m";
    public static final String ANSI_BLUE = "\u001B[36m";
    public static final String ANSI_RESET = "\u001B[0m";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        BigDecimal income = readPositiveBigDecimal(scanner, "Enter your monthly income: ");
        System.out.printf(asGreen(String.format("Done! You entered $%s as your income.%n", parseBigDecimalToString(income))));

        BigDecimal fixedExpenses = readExpenses(scanner, "fixed");
        System.out.printf(asGreen(String.format("Done! You entered $%s as your fixed expenses.%n", parseBigDecimalToString(fixedExpenses))));

        BigDecimal variableExpenses = readExpenses(scanner, "variable");
        System.out.printf(asGreen(String.format("Done! You entered $%s as your variable expenses.%n", parseBigDecimalToString(variableExpenses))));


        System.out.println(asBlue("---SUMMARY-------------------------------------------------"));
        System.out.printf(asBlue(String.format("You entered $%s as your income.%n", parseBigDecimalToString(income))));
        System.out.printf(asBlue(String.format("You entered $%s as your fixed expenses.%n", parseBigDecimalToString(fixedExpenses))));
        System.out.printf(asBlue(String.format("You entered $%s as your variable expenses.%n", parseBigDecimalToString(variableExpenses))));

        System.out.println(asBlue("---RESULT--------------------------------------------------"));

        BigDecimal remainingBudget = income
                .subtract(fixedExpenses)
                .subtract(variableExpenses);

        if (remainingBudget.compareTo(BigDecimal.ZERO) < 0) {
            BigDecimal duty = remainingBudget.abs();
            String msg = String.format("Your expenses exceed your income. By the end of the month, you will have to borrow $%s.", parseBigDecimalToString(duty));
            System.out.println(asBlue(msg));
        } else {
            String msg = String.format("After all expenses, your balance will be $%s.", parseBigDecimalToString(remainingBudget));
            System.out.println(asBlue(msg));
        }
    }

    private static BigDecimal readPositiveBigDecimal(Scanner scanner, String prompt) {
        while (true) {
            System.out.println(asPurple(prompt));
            try {
                String s = scanner.nextLine();
                BigDecimal input = parseInputToBigDecimal(s);
                if (input.compareTo(BigDecimal.ZERO) >= 0) {
                    return input.setScale(2, RoundingMode.HALF_UP);
                } else {
                    System.out.println(asRed("Income cannot be negative. Try again."));
                }
            } catch (InputMismatchException | ParseException e) {
                System.out.println(asRed("You entered invalid characters instead of a number, please try again."));
            }
        }
    }

    private static BigDecimal readExpenses(Scanner scanner, String type) {
        BigDecimal sum = BigDecimal.ZERO;
        int counter = 0;

        boolean exit = false;
        while (!exit) {
            String msg = String.format("Enter the amount of the %s expense #%d. To complete the entry, enter -1: ", type, counter + 1);
            System.out.println(asPurple(msg));

            try {
                String s = scanner.nextLine();
                BigDecimal input = parseInputToBigDecimal(s);
                if (input.compareTo(BigDecimal.valueOf(-1)) == 0) {
                    exit = true;
                } else if (input.compareTo(BigDecimal.ZERO) >= 0) {
                    sum = sum.add(input.setScale(2, RoundingMode.HALF_UP));
                    counter++;
                    System.out.println(asGreen("Done!"));
                } else {
                    System.out.println(asRed("Expenses cannot be negative. Try again."));
                }
            } catch (InputMismatchException | ParseException e) {
                System.out.println(asRed("You entered invalid characters instead of a number, please try again."));
            }
        }

        return sum;
    }

    private static String parseBigDecimalToString(BigDecimal bigDecimal) {
        if (bigDecimal == null) return "null";

        NumberFormat nf = NumberFormat.getNumberInstance(Locale.US);
        nf.setMinimumFractionDigits(2);
        nf.setMaximumFractionDigits(2);
        nf.setGroupingUsed(true);

        return nf.format(bigDecimal);
    }

    private static BigDecimal parseInputToBigDecimal(String input) throws ParseException {
        if (input == null) {
            throw new ParseException("Input is null", 0);
        }

        input = input.trim().replaceAll("[$\\s]", "");
        NumberFormat nf = NumberFormat.getInstance(Locale.US);
        Number number = nf.parse(input);

        return new BigDecimal(number.toString());
    }

    private static String asBlue(String msg){
        return ANSI_BLUE + msg + ANSI_RESET;
    }

    private static String asRed(String msg) {
        return ANSI_RED + msg + ANSI_RESET;
    }

    private static String asPurple(String msg) {
        return ANSI_PURPLE + msg + ANSI_RESET;
    }

    private static String asGreen(String msg) {
        return ANSI_GREEN + msg + ANSI_RESET;
    }
}
