import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main extends JFrame {
    private JTextField x0Field, y0Field, x1Field, y1Field;          // 입력받을 x, y의 필드
    private JPanel canvas;                                          // 그래프 그림을 그릴 canvas
    private final int GRID_SIZE = 10;                               // 모눈종이의 각 셀 크기
    private final int GRID_COUNT = 100;                             // 그리드 셀의 개수 100 * 100

    public Main() {
        setSize(800, 800);                             // 창의 크기
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);            // 창 끄면 멈춤
        setLayout(new BorderLayout());                             // borderlayout 으로 작성

        JPanel inputPanel = new JPanel();                          // 우측에 좌표 입력 패널 추가
        inputPanel.setLayout(new GridLayout(5, 2, 10, 10)); // 입력받을 컴포넌트(panel) 5행 2열 레이아웃, 간격 설정

        inputPanel.add(new JLabel("X0:"));
        x0Field = new JTextField();
        inputPanel.add(x0Field);

        inputPanel.add(new JLabel("Y0:"));
        y0Field = new JTextField();
        inputPanel.add(y0Field);

        inputPanel.add(new JLabel("X1:"));
        x1Field = new JTextField();
        inputPanel.add(x1Field);

        inputPanel.add(new JLabel("Y1:"));
        y1Field = new JTextField();
        inputPanel.add(y1Field);

        JButton drawButton = new JButton("DDA algorithmn");
        inputPanel.add(drawButton);

        add(inputPanel, BorderLayout.EAST);                        // 입력 패널을 오른쪽에 배치

                                                                   // 캔버스에 그릴 패널 추가 (크기를 더 크게 설정)
        canvas = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawGrid(g);  // 모눈종이 그리기
            }
        };
        canvas.setPreferredSize(new Dimension(100, 100)); // 그래프 영역의 크기를 크게 설정
        add(canvas, BorderLayout.CENTER);

        // 버튼 액션 처리
        drawButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int x1 = Integer.parseInt(x0Field.getText());
                    int y1 = Integer.parseInt(y0Field.getText());
                    int x2 = Integer.parseInt(x1Field.getText());
                    int y2 = Integer.parseInt(y1Field.getText());
                    drawLine(x1, y1, x2, y2);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Invalid input. Please enter valid integers.");
                }
            }
        });
    }

    // 모눈종이 그리기 함수
    private void drawGrid(Graphics g) {
        g.setColor(Color.LIGHT_GRAY);  // 그리드 색상을 연한 회색으로 설정
        int width = canvas.getWidth();
        int height = canvas.getHeight();

        // 세로선 그리기
        for (int i = 0; i <= 100; i++) {
            int x = i * 10;
            g.drawLine(x, 0, x, height);
        }

        // 가로선 그리기
        for (int i = 0; i <= 100; i++) {
            int y = i * 10;
            g.drawLine(0, y, width, y);
        }

        // X, Y 축을 진한 색으로 표시
        g.setColor(Color.BLACK);
        g.drawLine(0, height, width, height);  // 좌측 하단으로 0,0 설정
        g.drawLine(0, 0, 0, height);
    }

    // DDA 알고리즘을 사용하여 직선을 그리는 함수 (칸을 색칠해 나가는 방식)
    private void drawLine(int x1, int y1, int x2, int y2) {
        Graphics g = canvas.getGraphics();
        g.setColor(Color.black); // 직선은 빨간색으로 설정

        // 좌표 변환: 그리드 크기에 맞게 변환하고, 왼쪽 하단 기준 좌표로 조정
        int originY = canvas.getHeight(); // 왼쪽 하단이 (0, 0)

        // 좌표를 그리드 크기에 맞춰 변환 (y축은 위로 올라갈수록 값이 커지게 설정)
        x1 = x1 * GRID_SIZE;
        y1 = originY - (y1 * GRID_SIZE); // Y 좌표는 위로 갈수록 값이 커져야 하므로 변환
        x2 = x2 * GRID_SIZE;
        y2 = originY - (y2 * GRID_SIZE); // Y 좌표는 위로 갈수록 값이 커져야 하므로 변환

        int dx = x2 - x1;
        int dy = y2 - y1;

        int steps = Math.max(Math.abs(dx), Math.abs(dy));

        float xIncrement = (float) dx / steps;
        float yIncrement = (float) dy / steps;

        float x = x1;
        float y = y1;

        // 직선이 지나는 그리드 좌표를 저장하여 중복 색칠 방지
        boolean[][] filled = new boolean[GRID_COUNT][GRID_COUNT]; // 중복 방지용 배열

        int prevGridX = -1, prevGridY = -1; // 이전 칸 위치를 저장하여 중복 색칠 방지

        for (int i = 0; i <= steps; i++) {
            // 그리드 칸을 색칠합니다 (해당 칸의 좌상단 좌표를 계산하여 그립니다)
            int gridX = (int) (Math.round(x) / GRID_SIZE);
            int gridY = (int) (Math.round(y) / GRID_SIZE);

            // 이전 그리드 좌표와 다를 때만 색칠 (불필요한 색칠 방지)
            if (!filled[gridX][gridY]) {
                g.fillRect(gridX * GRID_SIZE, gridY * GRID_SIZE, GRID_SIZE, GRID_SIZE);
                filled[gridX][gridY] = true; // 색칠된 칸으로 표시
            }

            x += xIncrement;
            y += yIncrement;
        }
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Main().setVisible(true);
            }
        });
    }
}