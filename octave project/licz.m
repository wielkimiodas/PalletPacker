function licz()
clear reglog;
clear stochgrad

chdir D:\Downloads

[names, X, Y] = readdata('file2_train.txt', ' ');
X = [ones(size(X,1),1), X];

[namesT, XT, YT] = readdata('file2_test.txt', ' ');
XT = [ones(size(XT,1),1), XT];

% regresja logistyczna
tic
lambda=0;
w=zeros(size(X,2),1);

for it=1:10,
	w=reglog(X, Y, w, lambda);
	blad01 = 0;
	bladLog = 0;
	for i=1:size(XT, 1),
		realY = XT(i, :) * w;
		bladLog = bladLog + log(1 + exp(-YT(i) * (w' * XT(i, :)')));
		if (realY >= 0)
			if (YT(i) < 0)
				blad01 = blad01 + 1;
			endif
		else
			if (YT(i) >= 0)
				blad01 = blad01 + 1;
			endif
		endif
	end
		
	bladLog = bladLog / size(XT, 1);

	disp([it bladLog blad01])
end
toc

% gradient
tic

w=zeros(size(X,2),1);
c = 1 * 10 ^ -3;
for it=0:2000,
	alpha = c / sqrt(it + 1);
	w = stochgrad(X, Y, w, alpha);
	
	if (mod(it, 200) == 0),
		blad01 = 0;
		bladLog = 0;
		for i=1:size(XT, 1),
			realY = XT(i, :) * w;
			bladLog = bladLog + log(1 + exp(-YT(i) * (w' * XT(i, :)')));
			if (realY >= 0)
				if (YT(i) < 0)
					blad01 = blad01 + 1;
				endif
			else
				if (YT(i) >= 0)
					blad01 = blad01 + 1;
				endif
			endif
		end
		
		bladLog = bladLog / size(XT, 1);

		disp([it bladLog blad01])
	endif
end
toc