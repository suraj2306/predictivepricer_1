/- creates a multi variable linear regression model to approximate the TOTAL
/- method will use the least squares normal equations

/-taking historucal_rates to a temp w table, easy to plan with data without disturbing original
w:historical_rates

/-define an empty list of size as predictor variable matrix. 
x0: 1
terms:"j"$(terms-1)
terms:(count w)%6+0j
do[terms; x0:x0,1]

/define the predictor variables from 

x1: select rate from w where alias=`5Y
x2: select rate from w where alias=`10Y
x3: select rate from w where alias=`30Y


/-select from x0
/take y values
y: select rate from w where alias=`7Y

/create matrix X^t
X_t: ("f"$x0; "f"$x1.rate; "f"$x2.rate; "f"$x3.rate)

/- create matrix X
X: flip X_t

/- solve normal equations X^T X beta = X^T y by solving transposed system
/- y^T X = beta^T X^T  X  for beta
/- done this way to make use of lsq solver

f:y
y: value y
meta y
f:  "f"$y.rate

f: f mmu X      /- f now equals y^T X 
X: X_t mmu X    /- X now equals X^T X


/- convert f to matrix anc cast as float so lsq works 
k: 0* til count f
f: "f"$(f;k;k;k;k;k)

/use built-in solver  f lsq X solves  f= A X, where A is unknown matrix
sol:f lsq X
sol: sol[0]

weights:([]alias:`7YR;c:sol[0];w0:sol[1];w1:sol[2];w2:sol[3])


