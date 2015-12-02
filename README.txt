This is a simple predictive pricer
1) linear regression to find out the Price of give treasury based on 5YR, 10YR, 30YR current treasury prices
2) y0 = c+w0*log(Price_5YR) + w0*log(Price_10YR) + w0*log(Price_30YR)
3) more practical to take differnece of price, and to use a moving weighted averate to track that days behaviour 
 - these funcitonality yet to implement
4) KDB - need to download from Kx Systems to run this - 32 bit version is free
5) run q.exe -p 2306  ( Configurable port - I am using port 2306.)
6) Treasury yilds are downloaded from 
 https://www.treasury.gov/resource-center/data-chart-center/interest-rates/Pages/TextView.aspx?data=yield
7) c.java - this is API provided by KxSystems. not my code. 
