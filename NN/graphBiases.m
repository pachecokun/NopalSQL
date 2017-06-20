function graphBiases(cells, numCapa)    
    hold on;
    grid on;
    numMatrices = size(cells,1);
    [S R] = size(cell2mat(cells(1)));
    j = 1;   
    totalW = S*R;    
    disp(totalW);
    legendsW = cell(1,totalW);
    for c = 1: totalW        
        legendsW{j} = strcat('b',int2str(c)); 
        j = j+1;    
    end          
    for c = 1:totalW
        vector = zeros(1,numMatrices);
        for i = 1:numMatrices            
            matriz = cell2mat(cells(i)); 
            vector(1,i)=matriz(c);                                  
        end 
        plot(vector);       
    end   
    hold off;
    title(strcat('Evolucion de bias de capa ',numCapa));
    xlabel('k');
    ylabel('b');    
    legend(legendsW);    
end