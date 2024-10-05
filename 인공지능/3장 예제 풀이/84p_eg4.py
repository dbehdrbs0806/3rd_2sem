#%%
import numpy as np                                     # numpy 다차원 배열 계산 라이브러리
import matplotlib.pyplot as plt                        # 데이터 시각화 그래프 라이브러리

data = [(1, 1), (2, 2), (3, 3), (4, 4)]                # 데이터

x = np.array([d[0] for d in data])
y = np.array([d[1] for d in data])

initial_weights = [(np.random.randint(1, 11), np.random.randint(1, 11)),
                   (np.random.randint(1, 11), np.random.randint(1, 11)),
                   (np.random.randint(1, 11), np.random.randint(1, 11))]


def linear_regressor(x, w0, w1):                       # 선형회귀 함수
    return w1 * x + w0


def mean_squared_error_loss(x, y, w0, w1):             # 손실 함수
    return np.mean((linear_regressor(x, w0, w1) - y) ** 2)


def gradient_descent(x, y, w0_init, w1_init, lr=0.1, tolerance=0.0001, max_iteration=1000):    # 경사 하강법 함수
    w0, w1 = w0_init, w1_init
    N = len(data)
    iterations = 0
    loss_history = []

    while iterations < max_iteration:
        y_pred = linear_regressor(x, w0, w1)
        loss = mean_squared_error_loss(x, y, w0, w1)
        loss_history.append(loss)

        if loss < tolerance:                          # 손실이 임계값보다 작으면 반복 중지
            break

        grad_w0 = np.mean(2 * (y_pred - y))           # 기울기 계산
        grad_w1 = np.mean(2 * (y_pred - y) * x)

        w0 = w0 - lr * grad_w0                        # 가중치 업데이트
        w1 = w1 - lr * grad_w1

        iterations += 1                               # 학습 횟수

    return w0, w1, iterations, loss_history


for w0_init, w1_init in initial_weights:
    w0_updated, w1_updated, iterations, loss_history = gradient_descent(x, y, w0_init, w1_init)

    print(f"초기 w0: {w0_init}, 초기 w1: {w1_init}")
    print(f"학습 반복 횟수: {iterations}, 마지막 손실값: {loss_history[-1]}")

    plt.figure(figsize=(8, 6))
    plt.scatter(x, y, color='pink', label='Actual Data', s=100)
    x_range = np.linspace(0, 5, 100)  
    y_range_initial = linear_regressor(x_range, w0_init, w1_init)
    plt.plot(x_range, y_range_initial, color='blue', label='Initial Regression Line', linewidth=2)
    y_range_updated = linear_regressor(x_range, w0_updated, w1_updated)
    plt.plot(x_range, y_range_updated, color='red', label='Updated Regression Line', linewidth=2)
    plt.xlabel('x value')
    plt.ylabel('y value')
    plt.grid(True)
    plt.legend()
    plt.show()