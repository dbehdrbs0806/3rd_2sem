#%%
import numpy as np                                     # numpy 다차원 배열 계산 라이브러리
import matplotlib.pyplot as plt                        # 데이터 시각화 그래프 라이브러리

data = [(1.0, 1.6)]                                    # 데이터
x, y = zip(*data)                                      # x와 y 데이터를 분리

w0_init, w1_init = 1.0, 1.0                            # 초기 가중치 및 학습률
learning_rate = 0.2

def linear_regressor(x, w0, w1):                       # 선형회귀 함수
    return w1 * x + w0  

def gradient_descent(w0_init, w1_init, data, lr=0.1, max_iteration=1):      # 경사 하강법 함수
    w0, w1 = w0_init, w1_init
    N = len(data)
    x, y = data[0]

    y_pred = linear_regressor(x, w0, w1)

    for i in range(max_iteration):

        grad_w0 = 2 * (y_pred - y) / N                  # 기울기 계산
        grad_w1 = 2 * ((y_pred - y) * x) / N

        w0 = w0 - lr * grad_w0                          # 가중치 업데이트 
        w1 = w1 - lr * grad_w1

    return w0, w1  

w0_updated, w1_updated = gradient_descent(w0_init, w1_init, data, lr=learning_rate)

print(f"초기 w0: {w0_init}, 초기 w1: {w1_init}")
print(f"업데이트된 w0: {w0_updated}, 업데이트된 w1: {w1_updated}")

plt.figure(figsize=(8, 6))
plt.scatter(x, y, color='pink', label='Actual Data', s=100)
x_range = np.linspace(0, 4, 100) 
y_range_initial = linear_regressor(x_range, w0_init, w1_init)  
plt.plot(x_range, y_range_initial, color='blue', label='Initial Regression Line', linewidth=2)
y_range_updated = linear_regressor(x_range, w0_updated, w1_updated) 
plt.plot(x_range, y_range_updated, color='red', label='Updated Regression Line', linewidth=2)
plt.xlabel('x value')
plt.ylabel('y value')
plt.grid(True)
plt.legend() 
plt.show()  