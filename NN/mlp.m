function [weights] = mlp(layers, functions, input, target, alpha, itmax,eent,itval,numval)

    function e = propagate(p,t)
        %prop. hacia adelante
        a{1} = p;
        for m = 1:M
            func = functions(m);
            W = weights{m};
            b = bias{m};
            if func==1
                a{m+1} = purelin(W*a{m}+b);
            elseif func == 2
                a{m+1} = logsig(W*a{m}+b);
            elseif func == 3
                a{m+1} = tansig(W*a{m}+b);
            else
                disp('Funcion desconocida')
            end
        end
        
        e = t - a{M+1};
    end

    function write_matrix(f,matrix)
        fprintf(f,'[');
        for row = matrix
            fprintf(f,'%f,',row);
            fprintf(f,';');
        end
        fprintf(f,']');
    end
 
    %abrimos archivos de datos
    
    fclose('all');
    f_eent = fopen('eent.txt','w+');
    f_eval = fopen('eval.txt','w+');
    f_etest = fopen('etest.txt','w+');
    f_weights = fopen('weights.txt','w+');
    f_bias = fopen('bias.txt','w+');
    f_test = fopen('test.txt','w+');
    
    
    
    %inicializacion de variables

    
    M = size(layers,2)-1;
    n_data = size(input,2);
    max_eent = eent;
    max_numval = numval;
    numval = 0;
    finished = false;
    prop = 1;
    
    %obtenemos conjuntos de datos
    
    training = [];
    validation = [];
    test = [];
    t_training = [];
    t_validation = [];
    t_test = [];
    
    for i = 1:n_data
       aux = rem(i,20);
       if aux<14
          training(end+1) = input(i); 
          t_training(end+1) = target(i); 
       elseif aux<17
          validation(end+1) = input(i);
          t_validation(end+1) = target(i); 
       else
          test(end+1) = input(i); 
          t_test(end+1) = target(i); 
       end
    end
    
    n_training = size(training,2);
    n_validation = size(validation,2);
    n_test = size(test,2);
    
    c_weights = cell(1,1);
    c_bias = cell(1,1);
    
    weights = {};
    bias ={};
    a = {};
    s = {};
    F = {};
    
    %inicializamos pesos y bias
    
    for m = 1:M
        r = layers(m);
        S = layers(m+1);
        weights{m} = 2*rand(S,r)-1;
        bias{m} = 2*rand(S,1)-1;
    end
         
    
    %iteraciones
    
    for it = 1:itmax
        
        
        if finished
           break
        end
        
        fprintf('Iteración %d: ',it);
        
        if rem(it,itval)~=0 %entrenamiento
            fprintf('Entrenamiento\n');
            
            eent = 0;
            for n = 1:n_training
                t = t_training(n);
                p = training(n);
                e = propagate(p,t);
                               
                eent = eent + e.^2;

                %sensibilidades

                for m = 1:M
                    S = layers(m+1);
                    func = functions(m);

                    F{m} = zeros(S);

                    if func==1
                        for i = 1:S
                            F{m}(i,i) = 1;
                        end
                    elseif func == 2
                        for i = 1:S
                            F{m}(i,i) = a{m+1}(i)*(1-a{m+1}(i));
                        end
                    elseif func == 3
                        for i = 1:S
                            F{m}(i,i) = (1-a{m+1}(i).^2);
                        end
                    end
                    
                end

                %prop hacia atras

                s{M} = -2*F{m}*e;

                for m = M-1:-1:1
                    s{m} = F{m}*weights{m+1}'*s{m+1};
                end

                %actualizacion pesos y bias

                for m = 1:M
                   weights{m} = weights{m} -alpha*s{m}*a{m}';
                   bias{m} = bias{m} -alpha*s{m}; 
                   
                   c_weights{prop,m} = weights{m};
                   c_bias{prop,m} = bias{m};
                   
                   write_matrix(f_weights,weights{m});
                   write_matrix(f_bias,bias{m});
                   prop = prop + 1;
                end
                fprintf(f_weights,'\n');
                fprintf(f_bias,'\n');
                
            end
            fprintf(f_eent,'%f',eent);
            fprintf(f_eent,'\n');
            
            fprintf('eent: %f\n',eent);
            
            if eent<max_eent
               disp('Aprendizaje terminado por eent')
               finished = true;
            end
            
        else %validacion
            fprintf('Validación\n');
            if exist('e_val')~=0
                evalant = e_val;
            end
            e_val = 0;
            for n = 1:n_validation
                p = validation(n);
                t = t_validation(n);
                e = propagate(p,t);
                
                e_val = e_val +e.^2;
            end
            fprintf(f_eval,'%f',eent);
            fprintf(f_eval,'\n');
            
            
            if exist('evalant')~=0 && (evalant < e_val || isnan(e_val))
                numval = numval+1;
            else
                numval = 0;
            end
            fprintf('eval: %f, numval = %d\n',e_val,numval);
           
            if numval == max_numval
               disp('Aprendizaje terminado por numval') 
               finished = true;
            end
        end
    end
    
    if ~finished
       disp('Aprendizaje finalizado por itmax') 
    end
    
    %Se calcula error de prueba
    ep = 0;
    for n = 1:n_test
        p = test(n);
        t = t_test(n);
        e = propagate(p,t);
        
        aux = t-e;
        
        fprintf(f_test,'%f ',[p t aux]);
        fprintf(f_test,'\n');
        

        ep = ep +e.^2;
        
    end
    fprintf(f_etest,'%f',eent);
    fprintf(f_etest,'\n');
    
    fprintf('ep: %f\n',ep);
    
    %cerramos archivos    
    fclose('all');
    save('data.mat','c_weights','c_bias');
end 

