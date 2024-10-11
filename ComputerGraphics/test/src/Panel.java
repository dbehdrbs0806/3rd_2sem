import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Panel extends JPanel {                 // JPanel 패널 구성 class
    private final int pix = 10;                     // 픽셀 1개 가로세로 크기
    private final int numX = 30;                    // 제1사분면 x 좌표 범위
    private final int numY = 30;                    // 제1사분면 y 좌표 범위
    private final int sizeX = numX * pix;           // 모눈종이 x축 크기
    private final int sizeY = numY * pix;           // 모눈종이 y축 크기
    private ArrayList<xypos> arr;                   // 직선을 그릴 좌표 리스트
    private panel_graph gpanel;                     // 내부 클래스 객체로 그림 그리는 패널
    float start_x, start_y, end_x, end_y;           // 직선을 그릴 두 점의 좌표 저장

    Panel() {                                       // Panel 클래스의 생성자
        setLayout(new FlowLayout());                // Layout을 FlowLayout으로 설정
        gpanel = new panel_graph();                 // Panel_graph() 객체 gpanel을 생성 후 추가
        add(gpanel);
        add(new panel_control());
        arr = new ArrayList<>();
    }

    // 좌표 저장 클래스
    class xypos {
        int x, y;
        public xypos(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    // 그래프 패널 클래스
    class panel_graph extends JPanel {
        panel_graph() {
            setPreferredSize(new Dimension((sizeX + 1) * 2, (sizeY + 1) * 2));
        }

        // 화면을 그리는 메서드
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.translate(sizeX, sizeY);              // 원점을 화면 중앙으로 이동
            g2.setColor(Color.BLACK);
            g2.setStroke(new BasicStroke(1));

            // 모눈종이 그리기
            for (int i = -sizeY; i <= sizeY; i += pix) {
                g2.drawLine(-sizeX, i, sizeX, i);
            }
            for (int i = -sizeX; i <= sizeX; i += pix) {
                g2.drawLine(i, -sizeY, i, sizeY);
            }

            // x, y 축 그리기
            g2.setStroke(new BasicStroke(2));
            g2.drawLine(-sizeX, 0, sizeX, 0);
            g2.drawLine(0, -sizeY, 0, sizeY);

            // 저장된 좌표 그리기
            for (xypos p : arr) {
                g2.fillRect(p.x * pix, -(p.y + 1) * pix, pix, pix);   // 좌표로 그리기
            }
        }
    }

    // DDA 알고리즘 함수
    public void drawDDA(float startX, float startY, float endX, float endY) {
        arr.clear();  // 이전 좌표 초기화
        float m = (endY - startY) / (endX - startX);
        if (Math.abs(m) <= 1) {  // 기울기 1 이하일 때
            float y = startY + m;
            for (int x = (int) startX + 1; x <= (int) endX; x++) {
                arr.add(new xypos(x, Math.round(y)));
                y += m;
            }
        } else {  // 기울기 1 초과일 때
            float invM = 1 / m;
            float x = startX + invM;
            for (int y = (int) startY + 1; y <= (int) endY; y++) {
                arr.add(new xypos(Math.round(x), y));
                x += invM;
            }
        }
        gpanel.repaint();  // 다시 그리기
    }

    // Bresenham 직선 알고리즘 함수
    public void drawBresenham(int startX, int startY, int endX, int endY) {
        arr.clear();  // 이전 좌표 초기화
        int dx = Math.abs(endX - startX);
        int dy = Math.abs(endY - startY);
        int sx = startX < endX ? 1 : -1;
        int sy = startY < endY ? 1 : -1;
        int err = dx - dy;

        while (true) {
            arr.add(new xypos(startX, startY));  // 현재 좌표 저장
            if (startX == endX && startY == endY) break;
            int e2 = err * 2;
            if (e2 > -dy) {
                err -= dy;
                startX += sx;
            }
            if (e2 < dx) {
                err += dx;
                startY += sy;
            }
        }
        gpanel.repaint();  // 다시 그리기
    }

    // Bresenham 원 알고리즘 함수
    public void drawBresenhamCircle(int centerX, int centerY, int radius) {
        arr.clear();  // 이전 좌표 초기화
        int x = 0;
        int y = radius; // y의 초기값을 radius로 설정
        int d = 1 - radius;  // 초기 결정 변수

        // 원의 점을 그리기
        while (x <= y) {  // x가 y보다 작거나 같을 때까지
            // 픽셀을 계산하고 추가 (대칭성을 고려하여 모든 점 추가)
            arr.add(new xypos(centerX + x, centerY + y));
            arr.add(new xypos(centerX - x, centerY + y));
            arr.add(new xypos(centerX + x, centerY - y));
            arr.add(new xypos(centerX - x, centerY - y));
            arr.add(new xypos(centerX + y, centerY + x));
            arr.add(new xypos(centerX - y, centerY + x));
            arr.add(new xypos(centerX + y, centerY - x));
            arr.add(new xypos(centerX - y, centerY - x));

            x++; // x를 증가시킴

            // 결정 변수를 업데이트
            if (d < 0) {
                d = d + 2 * x + 1;  // 픽셀이 아래쪽으로 이동
            } else {
                y--;  // y를 줄이면서 대칭성의 경우를 그리기
                d = d + 2 * (x - y) + 1;  // 결정 변수 업데이트
            }
        }

        gpanel.repaint();  // 다시 그리기
    }

    // 컨트롤 패널 클래스
    class panel_control extends JPanel {
        JTextField x1, y1, x2, y2, radius;
        JButton btnDDA, btnBresenham, btnBresenhamCircle, btnClear;

        panel_control() {
            setPreferredSize(new Dimension(600, 130));
            setLayout(new GridLayout(5, 3, 2, 2));

            // 텍스트 필드와 버튼 생성
            x1 = new JTextField();
            y1 = new JTextField();
            x2 = new JTextField();
            y2 = new JTextField();
            radius = new JTextField();
            btnDDA = new JButton("DDA");
            btnBresenham = new JButton("Bresenham");
            btnBresenhamCircle = new JButton("Bresenham Circle");
            btnClear = new JButton("Clear");

            // 텍스트 필드 배치
            add(new JLabel("X1"));
            add(x1);
            add(btnDDA);
            add(new JLabel("Y1"));
            add(y1);
            add(btnBresenham);
            add(new JLabel("X2"));
            add(x2);
            add(btnBresenhamCircle);
            add(new JLabel("Y2"));
            add(y2);
            add(new JLabel("")); // 빈 공간
            add(new JLabel("Radius"));
            add(radius);
            add(btnClear);

            // DDA 버튼 액션 리스너
            btnDDA.addActionListener(e -> {
                start_x = Float.parseFloat(x1.getText());
                start_y = Float.parseFloat(y1.getText());
                end_x = Float.parseFloat(x2.getText());
                end_y = Float.parseFloat(y2.getText());
                drawDDA(start_x, start_y, end_x, end_y);
            });

            // Bresenham 버튼 액션 리스너
            btnBresenham.addActionListener(e -> {
                start_x = Integer.parseInt(x1.getText());
                start_y = Integer.parseInt(y1.getText());
                end_x = Integer.parseInt(x2.getText());
                end_y = Integer.parseInt(y2.getText());
                drawBresenham((int) start_x, (int) start_y, (int) end_x, (int) end_y);
            });

            // Bresenham Circle 버튼 액션 리스너
            btnBresenhamCircle.addActionListener(e -> {
                start_x = Integer.parseInt(x1.getText());
                start_y = Integer.parseInt(y1.getText());
                int radiusValue = Integer.parseInt(radius.getText());
                drawBresenhamCircle((int) start_x, (int) start_y, radiusValue);
            });

            // Clear 버튼 액션 리스너
            btnClear.addActionListener(e -> {
                arr.clear();
                gpanel.repaint();
            });
        }
    }
}