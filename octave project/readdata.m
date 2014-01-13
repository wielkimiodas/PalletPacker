% najperw zmień katalog na ten, w którym zapisane są pliki funkcją:
% chdir folder

function [names, X, Y] = readdata(filename, delimiter)
% Funkcja odczytuje zbiór danych z pliku.
% Pierwsza linijka powinna być nagłówkowa z nazwami zmiennych.
% Każda kolejna linijka to wektor x i wartość y.
% Wszystkie elementy oddzielone za pomocą delimiter (np spacja).
% Funkcja zwraca komórkę z nazwami, macierz X (każda obserwacja to
% wiersz tej macierzy) oraz wektor kolumnowy Y.
    fileID = fopen(filename, 'r');
    header = fgetl(fileID);
    names = strsplit(header, delimiter);
    i = 1;
    while true
        line = fgetl(fileID);
        if line == -1
            break;
        endif

        elements = strsplit(line, delimiter);
        X(i,:) = str2double(elements);
        i = i + 1;        
    end
    fclose(fileID);
    
    Y = X(:,end);
    X = X(:,1:end-1);
end
