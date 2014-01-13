function result = stochgrad(X, Y, w, alpha)
for i=1:size(X,1),
	w = w + 2 * alpha * (Y(i) - X(i,:)*w) * X(i,:)';
end
result = w;
