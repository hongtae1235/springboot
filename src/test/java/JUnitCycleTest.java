import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JUnitCycleTest {
    @BeforeEach
    public void beforeEach() {
        System.out.println("각 테스트 전 준비 실행");
    }

    @AfterEach
    public void afterEach() {
        System.out.println("각 테스트 후 설겆이 실행");
    }


    @Test
    public void test1() {
        System.out.println("test1");

    }

    @Test
    public void test2() {
        System.out.println("test2");
    }


    public void test3() {
        System.out.println("test3");
    }

    @AfterAll
    static void cleanTotal() {
        System.out.println("모든 테스트 실행 후 마지막 설겆이");
    }
}

