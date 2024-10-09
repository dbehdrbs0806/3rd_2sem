#%%
import numpy as np                                     # numpy 다차원 배열 계산 라이브러리
import matplotlib.pyplot as plt                        # 데이터 시각화 그래프 라이브러리

data = [(1.0, 1.6), (3.0, 3.4)]                        # 데이터

x = np.array([d[0] for d in data])                     # x 값: 1.0, 3.0
y = np.array([d[1] for d in data])                     # y 값: 1.6, 3.4 (실제 값)

def linear_regressor(x):                               # 선형회귀 함수
    return 0.5 + 0.5 * x

def mean_squared_error_loss(x, y):                     # 손실 함수
    return np.mean((linear_regressor(x) - y) ** 2)

mse = mean_squared_error_loss(x, y)
print(f"평균 제곱 오차: {mse:.2f}")

plt.figure(figsize=(8, 6))
plt.scatter(x, y, color='pink', label='Actual Data', s=100)

x_range = np.linspace(0, 4, 100)  
y_range = linear_regressor(x_range)
plt.plot(x_range, y_range, color='darkblue', label='Regression Line', linewidth=2)
plt.xlabel('x value')
plt.ylabel('y value')
plt.grid(True)
plt.legend()
plt.show()