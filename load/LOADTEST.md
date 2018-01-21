# 3 этап - тестирование с Нагрузкой
Нагрузочное тестирование было проведено с помощью wrk в режимах 1/2/4 потока/соединения. Длительность 1 минута. 
## До оптимизации

### PUT без перезаписи
##### 1 поток, 1 соединение
```
sudo wrk --latency -t1 -c1 -d1m -s put.lua http://localhost:8080
Running 1m test @ http://localhost:8080
  1 threads and 1 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     1.70ms    2.07ms  43.54ms   99.20%
    Req/Sec    62.09      5.97    70.00     84.14%
  Latency Distribution
     50%    1.47ms
     75%    1.62ms
     90%    1.86ms
     99%    2.94ms
  3733 requests in 1.00m, 29.46MB read
Requests/sec:     62.21
Transfer/sec:    502.82KB
```
##### 2 потока, 2 соединения
```
sudo wrk --latency -t2 -c2 -d1m -s put.lua http://localhost:8080
Running 1m test @ http://localhost:8080
  2 threads and 2 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     1.90ms    1.70ms  51.98ms   98.98%
    Req/Sec    44.71      5.17    60.00     68.37%
  Latency Distribution
     50%    1.73ms
     75%    1.92ms
     90%    2.15ms
     99%    3.67ms
  5386 requests in 1.00m, 42.51MB read
Requests/sec:     89.66
Transfer/sec:    724.62KB
```
##### 4 потока, 4 соединения
```
sudo wrk --latency -t4 -c4 -d1m -s put.lua http://localhost:8080
Running 1m test @ http://localhost:8080
  4 threads and 4 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     4.02ms    3.96ms 100.75ms   92.03%
    Req/Sec    29.17      5.04    40.00     76.51%
  Latency Distribution
     50%    2.60ms
     75%    4.13ms
     90%    7.05ms
     99%   19.96ms
  7051 requests in 1.00m, 55.65MB read
Requests/sec:    117.31
Transfer/sec:      0.93MB
```
### GET без повторов
##### 1 поток, 1 соединение
```
sudo wrk --latency -t1 -c1 -d1m -s get.lua http://localhost:8080
Running 1m test @ http://localhost:8080
  1 threads and 1 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency   301.00us  490.33us  27.19ms   99.47%
    Req/Sec     3.35k     1.86k    7.92k    84.50%
  Latency Distribution
     50%  310.00us
     75%  394.00us
     90%  446.00us
     99%  595.00us
  200183 requests in 1.00m, 47.44MB read
  Non-2xx or 3xx responses: 196449
Requests/sec:   3335.95
Transfer/sec:    809.51KB
```
##### 2 потока, 2 соединения
```
sudo wrk --latency -t2 -c2 -d1m -s get.lua http://localhost:8080
Running 1m test @ http://localhost:8080
  2 threads and 2 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency   368.27us  218.66us  10.75ms   85.09%
    Req/Sec     2.65k     1.27k    4.81k    67.47%
  Latency Distribution
     50%  284.00us
     75%  519.00us
     90%  614.00us
     99%    0.92ms
  317306 requests in 1.00m, 71.35MB read
  Non-2xx or 3xx responses: 311881
Requests/sec:   5279.64
Transfer/sec:      1.19MB
```
##### 4 потока, 4 соединения
```
Running 1m test @ http://localhost:8080
  4 threads and 4 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency   460.57us  272.53us  11.04ms   84.20%
    Req/Sec     2.18k   593.46     2.94k    69.96%
  Latency Distribution
     50%  408.00us
     75%  540.00us
     90%  737.00us
     99%    1.34ms
  520504 requests in 1.00m, 102.84MB read
  Non-2xx or 3xx responses: 513425
Requests/sec:   8673.81
Transfer/sec:      1.71MB
```
### PUT c перезаписью
##### 1 поток, 1 соединение
```
sudo wrk --latency -t1 -c1 -d1m -s put_rewriting.lua http://localhost:8080
Running 1m test @ http://localhost:8080
  1 threads and 1 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     1.68ms    1.56ms  51.65ms   98.71%
    Req/Sec    59.33      9.34    70.00     79.18%
  Latency Distribution
     50%    1.47ms
     75%    1.74ms
     90%    2.05ms
     99%    3.51ms
  3561 requests in 1.00m, 28.11MB read
Requests/sec:     59.32
Transfer/sec:    479.44KB
```
##### 2 потока, 2 соединения
```
sudo wrk --latency -t2 -c2 -d1m -s put_rewriting.lua http://localhost:8080
Running 1m test @ http://localhost:8080
  2 threads and 2 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     1.82ms    1.76ms  50.38ms   99.11%
    Req/Sec    45.71      5.30    60.00     68.89%
  Latency Distribution
     50%    1.62ms
     75%    1.84ms
     90%    2.05ms
     99%    3.40ms
  5507 requests in 1.00m, 43.46MB read
Requests/sec:     91.66
Transfer/sec:    740.79KB
```
##### 4 потока, 4 соединения
```
sudo wrk --latency -t4 -c4 -d1m -s put_rewriting.lua http://localhost:8080
Running 1m test @ http://localhost:8080
  4 threads and 4 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     5.69ms    6.00ms  93.10ms   90.21%
    Req/Sec    26.19      6.53    40.00     67.10%
  Latency Distribution
     50%    3.45ms
     75%    6.38ms
     90%   11.47ms
     99%   31.72ms
  6290 requests in 1.00m, 49.64MB read
Requests/sec:    104.77
Transfer/sec:    846.72KB
```
### GET с повторами
##### 1 поток, 1 соединение
```
sudo wrk --latency -t1 -c1 -d1m -s get_repeat.lua http://localhost:8080
Running 1m test @ http://localhost:8080
  1 threads and 1 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     3.34ms   20.95ms 370.96ms   97.20%
    Req/Sec     3.14k     1.06k    4.40k    74.66%
  Latency Distribution
     50%  253.00us
     75%  337.00us
     90%  547.00us
     99%   96.24ms
  183664 requests in 1.00m, 1.41GB read
  Non-2xx or 3xx responses: 268
Requests/sec:   3057.15
Transfer/sec:     24.08MB
```
##### 2 потока, 2 соединения
```
sudo wrk --latency -t2 -c2 -d1m -s get_repeat.lua http://localhost:8080
Running 1m test @ http://localhost:8080
  2 threads and 2 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     1.65ms    8.82ms 184.94ms   97.75%
    Req/Sec     1.82k   580.84     2.52k    76.13%
  Latency Distribution
     50%  435.00us
     75%  585.00us
     90%    0.94ms
     99%   35.43ms
  216177 requests in 1.00m, 1.67GB read
Requests/sec:   3599.78
Transfer/sec:     28.39MB
```
##### 4 потока, 4 соединения
```
sudo wrk --latency -t4 -c4 -d1m -s get_repeat.lua http://localhost:8080
Running 1m test @ http://localhost:8080
  4 threads and 4 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency   801.96us  333.93us  12.30ms   81.63%
    Req/Sec     1.24k   127.21     1.43k    91.75%
  Latency Distribution
     50%  749.00us
     75%    0.94ms
     90%    1.12ms
     99%    1.99ms
  296267 requests in 1.00m, 2.28GB read
Requests/sec:   4936.45
Transfer/sec:     38.94MB
```
## После оптимизации

Оптимизация была выполнена путем добавления кэша в хранилище

### PUT без перезаписи
##### 1 поток, 1 соединение
```
sudo wrk --latency -t1 -c1 -d1m -s put.lua http://localhost:8080
Running 1m test @ http://localhost:8080
  1 threads and 1 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     1.60ms    1.54ms  47.37ms   99.35%
    Req/Sec    63.69      4.12    70.00     89.66%
  Latency Distribution
     50%    1.45ms
     75%    1.56ms
     90%    1.72ms
     99%    2.55ms
  3832 requests in 1.00m, 30.24MB read
Requests/sec:     63.81
Transfer/sec:    515.68KB
```
##### 2 потока, 2 соединения
```
sudo wrk --latency -t2 -c2 -d1m -s put.lua http://localhost:8080
Running 1m test @ http://localhost:8080
  2 threads and 2 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     1.97ms    2.85ms  76.45ms   99.22%
    Req/Sec    44.85      5.52    70.00     77.83%
  Latency Distribution
     50%    1.72ms
     75%    1.91ms
     90%    2.10ms
     99%    3.89ms
  5399 requests in 1.00m, 42.61MB read
Requests/sec:     89.94
Transfer/sec:    726.91KB
```
##### 4 потока, 4 соединения
```
sudo wrk --latency -t4 -c4 -d1m -s put.lua http://localhost:8080
Running 1m test @ http://localhost:8080
  4 threads and 4 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     4.47ms    3.63ms  58.25ms   88.70%
    Req/Sec    28.39      5.00    40.00     78.08%
  Latency Distribution
     50%    2.83ms
     75%    4.98ms
     90%    8.70ms
     99%   18.07ms
  6869 requests in 1.00m, 54.21MB read
Requests/sec:    114.27
Transfer/sec:      0.90MB
```
### GET без повторов
##### 1 поток, 1 соединение
```
sudo wrk --latency -t1 -c1 -d1m -s get.lua http://localhost:8080
Running 1m test @ http://localhost:8080
  1 threads and 1 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency   398.11us    1.42ms  70.18ms   98.85%
    Req/Sec     2.98k     1.84k    7.86k    80.50%
  Latency Distribution
     50%  340.00us
     75%  434.00us
     90%  545.00us
     99%    2.06ms
  178014 requests in 1.00m, 46.18MB read
  Non-2xx or 3xx responses: 174181
Requests/sec:   2964.74
Transfer/sec:    787.58KB
```
##### 2 потока, 2 соединения
```
sudo wrk --latency -t2 -c2 -d1m -s get.lua http://localhost:8080
Running 1m test @ http://localhost:8080
  2 threads and 2 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency   334.01us  533.12us  41.50ms   98.33%
    Req/Sec     2.94k     1.19k    5.18k    67.26%
  Latency Distribution
     50%  296.00us
     75%  403.00us
     90%  506.00us
     99%    1.16ms
  180647 requests in 1.00m, 58.99MB read
  Socket errors: connect 0, read 76129, write 0, timeout 0
  Non-2xx or 3xx responses: 175202
Requests/sec:   3009.58
Transfer/sec:      0.98MB
```
##### 4 потока, 4 соединения
```
sudo wrk --latency -t4 -c4 -d1m -s get.lua http://localhost:8080
Running 1m test @ http://localhost:8080
  4 threads and 4 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency   350.64us  420.13us  32.20ms   95.04%
    Req/Sec     2.71k     1.46k    6.35k    72.12%
  Latency Distribution
     50%  262.00us
     75%  436.00us
     90%  605.00us
     99%    1.62ms
  173382 requests in 1.00m, 69.75MB read
  Socket errors: connect 0, read 111672, write 0, timeout 0
  Non-2xx or 3xx responses: 166471
Requests/sec:   2884.96
Transfer/sec:      1.16MB
```
### PUT c перезаписью
##### 1 поток, 1 соединение
```
sudo wrk --latency -t1 -c1 -d1m -s put_rewriting.lua http://localhost:8080
Running 1m test @ http://localhost:8080
  1 threads and 1 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     1.66ms    2.19ms  73.36ms   99.42%
    Req/Sec    60.46      6.49    70.00     66.01%
  Latency Distribution
     50%    1.50ms
     75%    1.63ms
     90%    1.80ms
     99%    2.66ms
  3636 requests in 1.00m, 28.70MB read
Requests/sec:     60.50
Transfer/sec:    489.00KB
```
##### 2 потока, 2 соединения
```
 sudo wrk --latency -t2 -c2 -d1m -s put_rewriting.lua http://localhost:8080
Running 1m test @ http://localhost:8080
  2 threads and 2 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     2.16ms    2.70ms  84.55ms   97.93%
    Req/Sec    41.11      7.84    60.00     70.82%
  Latency Distribution
     50%    1.82ms
     75%    2.05ms
     90%    2.48ms
     99%    8.50ms
  4920 requests in 1.00m, 38.83MB read
Requests/sec:     81.90
Transfer/sec:    661.90KB
```
##### 4 потока, 4 соединения
```
sudo wrk --latency -t4 -c4 -d1m -s put_rewriting.lua http://localhost:8080
Running 1m test @ http://localhost:8080
  4 threads and 4 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     4.74ms    3.97ms  59.30ms   88.44%
    Req/Sec    28.26      5.18    40.00     76.16%
  Latency Distribution
     50%    2.99ms
     75%    5.35ms
     90%    9.47ms
     99%   20.52ms
  6828 requests in 1.00m, 53.89MB read
Requests/sec:    113.69
Transfer/sec:      0.90MB
```
### GET с повторами
##### 1 поток, 1 соединение
```
sudo wrk --latency -t1 -c1 -d1m -s get_repeat.lua http://localhost:8080
Running 1m test @ http://localhost:8080
  1 threads and 1 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency   107.25us  105.25us   9.46ms   98.31%
    Req/Sec     8.50k   780.73     9.26k    91.85%
  Latency Distribution
     50%   93.00us
     75%  108.00us
     90%  120.00us
     99%  269.00us
  508492 requests in 1.00m, 3.92GB read
  Socket errors: connect 0, read 743, write 0, timeout 0
  Non-2xx or 3xx responses: 1
Requests/sec:   8460.83
Transfer/sec:     66.74MB
```
##### 2 потока, 2 соединения
```
sudo wrk --latency -t2 -c2 -d1m -s get_repeat.lua http://localhost:8080
Running 1m test @ http://localhost:8080
  2 threads and 2 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency   199.59us  121.98us   8.11ms   97.77%
    Req/Sec     4.77k   509.17     5.49k    73.38%
  Latency Distribution
     50%  181.00us
     75%  213.00us
     90%  245.00us
     99%  552.00us
  570592 requests in 1.00m, 4.40GB read
Requests/sec:   9494.50
Transfer/sec:     74.89MB
```
##### 4 потока, 4 соединения
```
sudo wrk --latency -t4 -c4 -d1m -s get_repeat.lua http://localhost:8080
Running 1m test @ http://localhost:8080
  4 threads and 4 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency   360.07us  185.46us  11.14ms   86.28%
    Req/Sec     2.68k   232.37     3.28k    60.55%
  Latency Distribution
     50%  341.00us
     75%  430.00us
     90%  522.00us
     99%  841.00us
  640251 requests in 1.00m, 4.93GB read
Requests/sec:  10653.53
Transfer/sec:     84.03MB
```

Из результатов оптимизации видно, что мы получили прирост в производительности и в количестве обрабатываемых запросов. Также уменьшена задержка.
