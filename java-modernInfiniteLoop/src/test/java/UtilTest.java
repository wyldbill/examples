import lombok.extern.log4j.Log4j2;
import net.littlethunder.infloop.InfiniteLoopUtil;

import java.util.concurrent.Future;
import java.util.stream.Stream;

@Log4j2
public class UtilTest {

    public static void main(String[] args){
        Stream<Future<String>> out = InfiniteLoopUtil.streamLoop(
                () -> {
                    Thread.sleep(1500);
                    return "Yowza!";
                }
        );
        out.map(UtilTest::getIt).forEach(log::info);
    }

    private static String getIt(Future<String> f) {
        try {
            return f.get();
        } catch (Exception e){
            return "BLAM!";
        }
    }
}
