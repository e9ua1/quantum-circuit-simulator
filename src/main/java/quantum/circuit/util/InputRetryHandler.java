package quantum.circuit.util;

import java.util.function.Supplier;

import quantum.circuit.view.OutputView;

public class InputRetryHandler {

    private InputRetryHandler() {
    }

    public static <T> T retry(Supplier<T> supplier) {
        while (true) {
            try {
                return supplier.get();
            } catch (IllegalArgumentException e) {
                OutputView.printErrorMessage(e.getMessage());
            }
        }
    }
}
