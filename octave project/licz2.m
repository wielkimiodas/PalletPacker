function licz()
clear reglog;
clear stochgrad

chdir D:\Downloads

[names, X, Y] = readdata('file2_train.txt', ' ');
X = [ones(size(X,1),1), X];

[namesT, XT, YT] = readdata('file2_test.txt', ' ');
XT = [ones(size(XT,1),1), XT];

% regresja logistyczna
for lambda=0:100,
	w=zeros(size(X,2),1);

	for it=1:10,
		w=reglog(X, Y, w, lambda);
	end
	blad01Test = 0;
	bladLogTest = 0;
	for i=1:size(XT, 1),
		realY = XT(i, :) * w;
		bladLogTest = bladLogTest + log(1 + exp(-YT(i) * (w' * XT(i, :)')));
		if (realY >= 0)
			if (YT(i) < 0)
				blad01Test = blad01Test + 1;
			endif
		else
			if (YT(i) >= 0)
				blad01Test = blad01Test + 1;
			endif
		endif
	end
	bladLogTest = bladLogTest / size(XT, 1);
	blad01Test = blad01Test / size(XT, 1);
	
	blad01Train = 0;
	bladLogTrain = 0;
	for i=1:size(X, 1),
		realY = X(i, :) * w;
		bladLogTrain = bladLogTrain + log(1 + exp(-Y(i) * (w' * X(i, :)')));
		if (realY >= 0)
			if (Y(i) < 0)
				blad01Train = blad01Train + 1;
			endif
		else
			if (Y(i) >= 0)
				blad01Train = blad01Train + 1;
			endif
		endif
	end
	bladLogTrain = bladLogTrain / size(X, 1);
	blad01Train = blad01Train / size(X, 1);

	disp([lambda bladLogTrain blad01Train bladLogTest blad01Test])
end
