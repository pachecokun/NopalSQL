function graphWeight(cells, numCapa)    
    hold on;
    grid on;
    numMatrices = size(cells,1);
    [S R] = size(cell2mat(cells(1)));
    j = 1;   
    totalW = S*R;    
    legendsW = cell(1,totalW);
    for c = 1: totalW        
        legendsW{j} = strcat('W',int2str(c)); 
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
    title(strcat('Evolucion de pesos de capa ',numCapa));
    xlabel('k');
    ylabel('W');    
    legend(legendsW);    
end