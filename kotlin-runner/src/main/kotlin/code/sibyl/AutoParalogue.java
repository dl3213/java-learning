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

    private static Robot robot;

    static {
        try {
            robot = new Robot();
        } catch (AWTException e) {
            throw new RuntimeException(e);
        }
    }

    private static int SHOOT_in = KeyEvent.VK_J;
    private static int SHOOT_out = KeyEvent.VK_K;
    private static int SHOOT_together = KeyEvent.VK_L;

    public AutoParalogue() throws AWTException {
    }

    public static void main1(String[] args) {
        while (true){
            Point point = getMousePosition();
            log.info("鼠标坐标: X={}, Y={}", point.x, point.y);
        }
    }

    public static void main(String[] args) throws AWTException {
        r.sleep(3000L);

        // X=121, Y=243
        // X=121, Y=318
        // X=121, Y=KJ
        // X=121, Y=488
        // X=121, Y=513
        final long 鼠标事件间隔 = 3000L;
        final long 鼠标期间间隔 = 3500L;
        CompletableFuture<Void> mouse = CompletableFuture.runAsync(() -> {
            boolean p = true;
            while (p) {
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

//                Point point = getMousePosition();
//                log.info("鼠标坐标: X={}, Y={}", point.x, point.y);
            }
        });

        final long 按键事件间隔 = 1000;
        CompletableFuture<Void> key = CompletableFuture.runAsync(() -> {
            boolean p = true;
            while (p) {
                r.sleep(按键事件间隔);
                keyAction(robot);
            }
        });

        mouse.join();
        key.join();

    }

    private static void keyAction(Robot robot) {
        robot.keyPress(SHOOT_together);
        robot.keyRelease(SHOOT_together);
        r.sleep(250);
        robot.keyPress(SHOOT_out);
        robot.keyRelease(SHOOT_out);
        r.sleep(250);
        robot.keyPress(SHOOT_in);
        robot.keyRelease(SHOOT_in);
//        robot.keyPress(SHOOT_out);
//        robot.keyRelease(SHOOT_out);
//        robot.keyPress(SHOOT_together);
//        robot.keyRelease(SHOOT_together);
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
