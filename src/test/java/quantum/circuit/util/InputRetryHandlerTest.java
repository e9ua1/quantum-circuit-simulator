package quantum.circuit.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.function.Supplier;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class InputRetryHandlerTest {

    @Test
    @DisplayName("첫 시도에서 성공하면 즉시 반환한다")
    void retrySuccessOnFirstAttempt() {
        Supplier<Integer> supplier = mock(Supplier.class);
        when(supplier.get()).thenReturn(42);

        Integer result = InputRetryHandler.retry(supplier);

        assertThat(result).isEqualTo(42);
        verify(supplier, times(1)).get();
    }

    @Test
    @DisplayName("첫 시도 실패 후 두 번째 시도에서 성공한다")
    void retrySuccessOnSecondAttempt() {
        Supplier<Integer> supplier = mock(Supplier.class);
        when(supplier.get())
                .thenThrow(new IllegalArgumentException("첫 번째 실패"))
                .thenReturn(42);

        Integer result = InputRetryHandler.retry(supplier);

        assertThat(result).isEqualTo(42);
        verify(supplier, times(2)).get();
    }

    @Test
    @DisplayName("여러 번 실패 후 성공한다")
    void retrySuccessAfterMultipleFailures() {
        Supplier<Integer> supplier = mock(Supplier.class);
        when(supplier.get())
                .thenThrow(new IllegalArgumentException("첫 번째 실패"))
                .thenThrow(new IllegalArgumentException("두 번째 실패"))
                .thenThrow(new IllegalArgumentException("세 번째 실패"))
                .thenReturn(42);

        Integer result = InputRetryHandler.retry(supplier);

        assertThat(result).isEqualTo(42);
        verify(supplier, times(4)).get();
    }

    @Test
    @DisplayName("String 타입도 처리 가능하다")
    void retryWithStringType() {
        Supplier<String> supplier = mock(Supplier.class);
        when(supplier.get()).thenReturn("success");

        String result = InputRetryHandler.retry(supplier);

        assertThat(result).isEqualTo("success");
    }

    @Test
    @DisplayName("null을 반환할 수 있다")
    void retryCanReturnNull() {
        Supplier<String> supplier = mock(Supplier.class);
        when(supplier.get()).thenReturn(null);

        String result = InputRetryHandler.retry(supplier);

        assertThat(result).isNull();
    }
}
