package code.sibyl;

import code.sibyl.common.r;
import io.netty.util.concurrent.CompleteFuture;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.FutureTask;

@Slf4j
public class AutoParalogue {

    public static void main(String[] args) throws AWTException {
        Robot robot = new Robot();
        r.sleep(5000L);

        // X=121, Y=243
        // X=121, Y=318  ljljljlj
        // X=121, Y=393
        // X=121, Y=488
        // X=121, Y=513
        final long 鼠标事件间隔 = 3000L;
        final long 鼠标期间间隔 = 3000L;
        CompletableFuture<Void> mouse = CompletableFuture.runAsync(() -> {
            while (true) {
//                r.sleep(鼠标事件间隔);

                r.sleep(鼠标期间间隔);
                robot.mouseMove(121, 241);
                mouseLeftClick(robot);

                r.sleep(鼠标期间间隔);
                robot.mouseMove(121, 318);
                mouseLeftClick(robot);

                r.sleep(鼠标期间间隔);
                robot.mouseMove(121, 392);
                mouseLeftClick(robot);

                r.sleep(鼠标期间间隔);
                robot.mouseMove(121, 413);
                mouseLeftClick(robot);

                r.sleep(鼠标期间间隔);
                robot.mouseMove(121, 488);
                mouseLeftClick(robot);

                r.sleep(鼠标期间间隔);
                robot.mouseMove(121, 513);
                mouseLeftClick(robot);

                log.info("mouse left click end ");
//                Point point = getMousePosition();
//                log.info("鼠标坐标: X={}, Y={}", point.x, point.y);
            }
        });

        final long 按键事件间隔 = 1000L;
        CompletableFuture<Void> key = CompletableFuture.runAsync(() -> {
            while (true) {
                robot.keyPress(KeyEvent.VK_L);
                robot.keyRelease(KeyEvent.VK_L);
                robot.keyPress(KeyEvent.VK_J);
                robot.keyRelease(KeyEvent.VK_J);
                r.sleep(按键事件间隔);
            }
        });


        mouse.join();
        key.join();

    }

    private static void mouseLeftClick(Robot robot) {
        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK); // 按下左键:cite[1]:cite[7]
        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK); // 释放左键:cite[1]:cite[7]
    }


    public static Point getMousePosition() {
        PointerInfo pointerInfo = MouseInfo.getPointerInfo();
        return pointerInfo.getLocation();
    }
}
