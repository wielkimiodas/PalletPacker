function new_w = reglog(x, y, w, lambda)
n = size(x, 1);
B = 1./(1.+exp(y.*(x*w)));
B = diag(B);
H = lambda * eye(size(x,2)) + x'*B*(eye(n)-B)*x;
L = lambda * w - x'*B*y;
delta = inv(H) * L;
new_w = w - delta;