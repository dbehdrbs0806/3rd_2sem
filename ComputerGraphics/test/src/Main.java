import javax.swing.*;

import static java.awt.AWTEventMulticaster.add;


public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Line Drawing");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Panel 인스턴스 생성 후 프레임에 추가
        Panel panel = new Panel();
        frame.add(panel);

        // 프레임 크기 설정 및 표시
        frame.pack(); // 패널 크기에 맞춰 프레임 크기 자동 조정
        frame.setVisible(true);
    }
}