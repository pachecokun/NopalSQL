function varargout = NN(varargin)
% NN MATLAB code for NN.fig
%      NN, by itself, creates a new NN or raises the existing
%      singleton*.
%
%      H = NN returns the handle to a new NN or the handle to
%      the existing singleton*.
%
%      NN('CALLBACK',hObject,eventData,handles,...) calls the local
%      function named CALLBACK in NN.M with the given input arguments.
%
%      NN('Property','Value',...) creates a new NN or raises the
%      existing singleton*.  Starting from the left, property value pairs are
%      applied to the GUI before NN_OpeningFcn gets called.  An
%      unrecognized property name or invalid value makes property application
%      stop.  All inputs are passed to NN_OpeningFcn via varargin.
%
%      *See GUI Options on GUIDE's Tools menu.  Choose "GUI allows only one
%      instance to run (singleton)".
%
% See also: GUIDE, GUIDATA, GUIHANDLES

% Edit the above text to modify the response to help NN

% Last Modified by GUIDE v2.5 19-Jun-2017 21:58:29

% Begin initialization code - DO NOT EDIT
gui_Singleton = 1;
gui_State = struct('gui_Name',       mfilename, ...
                   'gui_Singleton',  gui_Singleton, ...
                   'gui_OpeningFcn', @NN_OpeningFcn, ...
                   'gui_OutputFcn',  @NN_OutputFcn, ...
                   'gui_LayoutFcn',  [] , ...
                   'gui_Callback',   []);
if nargin && ischar(varargin{1})
    gui_State.gui_Callback = str2func(varargin{1});
end

if nargout
    [varargout{1:nargout}] = gui_mainfcn(gui_State, varargin{:});
else
    gui_mainfcn(gui_State, varargin{:});
end
% End initialization code - DO NOT EDIT


% --- Executes just before NN is made visible.
function NN_OpeningFcn(hObject, eventdata, handles, varargin)
% This function has no output args, see OutputFcn.
% hObject    handle to figure
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)
% varargin   command line arguments to NN (see VARARGIN)

% Choose default command line output for NN
handles.output = hObject;

% Update handles structure
guidata(hObject, handles);

% UIWAIT makes NN wait for user response (see UIRESUME)
% uiwait(handles.figure1);


% --- Outputs from this function are returned to the command line.
function varargout = NN_OutputFcn(hObject, eventdata, handles) 
% varargout  cell array for returning output args (see VARARGOUT);
% hObject    handle to figure
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Get default command line output from handles structure
varargout{1} = handles.output;





function txtInputTargets_Callback(hObject, eventdata, handles)
% hObject    handle to txtInputTargets (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hints: get(hObject,'String') returns contents of txtInputTargets as text
%        str2double(get(hObject,'String')) returns contents of txtInputTargets as a double


% --- Executes during object creation, after setting all properties.
function txtInputTargets_CreateFcn(hObject, eventdata, handles)
% hObject    handle to txtInputTargets (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    empty - handles not created until after all CreateFcns called

% Hint: edit controls usually have a white background on Windows.
%       See ISPC and COMPUTER.
if ispc && isequal(get(hObject,'BackgroundColor'), get(0,'defaultUicontrolBackgroundColor'))
    set(hObject,'BackgroundColor','white');
end



function txtArchVector_Callback(hObject, eventdata, handles)
% hObject    handle to txtArchVector (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hints: get(hObject,'String') returns contents of txtArchVector as text
%        str2double(get(hObject,'String')) returns contents of txtArchVector as a double


% --- Executes during object creation, after setting all properties.
function txtArchVector_CreateFcn(hObject, eventdata, handles)
% hObject    handle to txtArchVector (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    empty - handles not created until after all CreateFcns called

% Hint: edit controls usually have a white background on Windows.
%       See ISPC and COMPUTER.
if ispc && isequal(get(hObject,'BackgroundColor'), get(0,'defaultUicontrolBackgroundColor'))
    set(hObject,'BackgroundColor','white');
end



function txtFunctionVector_Callback(hObject, eventdata, handles)
% hObject    handle to txtFunctionVector (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hints: get(hObject,'String') returns contents of txtFunctionVector as text
%        str2double(get(hObject,'String')) returns contents of txtFunctionVector as a double


% --- Executes during object creation, after setting all properties.
function txtFunctionVector_CreateFcn(hObject, eventdata, handles)
% hObject    handle to txtFunctionVector (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    empty - handles not created until after all CreateFcns called

% Hint: edit controls usually have a white background on Windows.
%       See ISPC and COMPUTER.
if ispc && isequal(get(hObject,'BackgroundColor'), get(0,'defaultUicontrolBackgroundColor'))
    set(hObject,'BackgroundColor','white');
end



function txtLearningFactor_Callback(hObject, eventdata, handles)
% hObject    handle to txtLearningFactor (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hints: get(hObject,'String') returns contents of txtLearningFactor as text
%        str2double(get(hObject,'String')) returns contents of txtLearningFactor as a double


% --- Executes during object creation, after setting all properties.
function txtLearningFactor_CreateFcn(hObject, eventdata, handles)
% hObject    handle to txtLearningFactor (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    empty - handles not created until after all CreateFcns called

% Hint: edit controls usually have a white background on Windows.
%       See ISPC and COMPUTER.
if ispc && isequal(get(hObject,'BackgroundColor'), get(0,'defaultUicontrolBackgroundColor'))
    set(hObject,'BackgroundColor','white');
end



function txtitMax_Callback(hObject, eventdata, handles)
% hObject    handle to txtitMax (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hints: get(hObject,'String') returns contents of txtitMax as text
%        str2double(get(hObject,'String')) returns contents of txtitMax as a double


% --- Executes during object creation, after setting all properties.
function txtitMax_CreateFcn(hObject, eventdata, handles)
% hObject    handle to txtitMax (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    empty - handles not created until after all CreateFcns called

% Hint: edit controls usually have a white background on Windows.
%       See ISPC and COMPUTER.
if ispc && isequal(get(hObject,'BackgroundColor'), get(0,'defaultUicontrolBackgroundColor'))
    set(hObject,'BackgroundColor','white');
end



function edit9_Callback(hObject, eventdata, handles)
% hObject    handle to edit9 (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hints: get(hObject,'String') returns contents of edit9 as text
%        str2double(get(hObject,'String')) returns contents of edit9 as a double


% --- Executes during object creation, after setting all properties.
function edit9_CreateFcn(hObject, eventdata, handles)
% hObject    handle to edit9 (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    empty - handles not created until after all CreateFcns called

% Hint: edit controls usually have a white background on Windows.
%       See ISPC and COMPUTER.
if ispc && isequal(get(hObject,'BackgroundColor'), get(0,'defaultUicontrolBackgroundColor'))
    set(hObject,'BackgroundColor','white');
end



function txtLearningRate_Callback(hObject, eventdata, handles)
% hObject    handle to txtLearningRate (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hints: get(hObject,'String') returns contents of txtLearningRate as text
%        str2double(get(hObject,'String')) returns contents of txtLearningRate as a double


% --- Executes during object creation, after setting all properties.
function txtLearningRate_CreateFcn(hObject, eventdata, handles)
% hObject    handle to txtLearningRate (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    empty - handles not created until after all CreateFcns called

% Hint: edit controls usually have a white background on Windows.
%       See ISPC and COMPUTER.
if ispc && isequal(get(hObject,'BackgroundColor'), get(0,'defaultUicontrolBackgroundColor'))
    set(hObject,'BackgroundColor','white');
end



function txteEval_Callback(hObject, eventdata, handles)
% hObject    handle to txteEval (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hints: get(hObject,'String') returns contents of txteEval as text
%        str2double(get(hObject,'String')) returns contents of txteEval as a double


% --- Executes during object creation, after setting all properties.
function txteEval_CreateFcn(hObject, eventdata, handles)
% hObject    handle to txteEval (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    empty - handles not created until after all CreateFcns called

% Hint: edit controls usually have a white background on Windows.
%       See ISPC and COMPUTER.
if ispc && isequal(get(hObject,'BackgroundColor'), get(0,'defaultUicontrolBackgroundColor'))
    set(hObject,'BackgroundColor','white');
end



function txtitVal_Callback(hObject, eventdata, handles)
% hObject    handle to txtitVal (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hints: get(hObject,'String') returns contents of txtitVal as text
%        str2double(get(hObject,'String')) returns contents of txtitVal as a double


% --- Executes during object creation, after setting all properties.
function txtitVal_CreateFcn(hObject, eventdata, handles)
% hObject    handle to txtitVal (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    empty - handles not created until after all CreateFcns called

% Hint: edit controls usually have a white background on Windows.
%       See ISPC and COMPUTER.
if ispc && isequal(get(hObject,'BackgroundColor'), get(0,'defaultUicontrolBackgroundColor'))
    set(hObject,'BackgroundColor','white');
end

function txtNumVal_Callback(hObject, eventdata, handles)
% hObject    handle to txtNumVal (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hints: get(hObject,'String') returns contents of txtNumVal as text
%        str2double(get(hObject,'String')) returns contents of txtNumVal as a double


% --- Executes during object creation, after setting all properties.
function txtNumVal_CreateFcn(hObject, eventdata, handles)
% hObject    handle to txtNumVal (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    empty - handles not created until after all CreateFcns called

% Hint: edit controls usually have a white background on Windows.
%       See ISPC and COMPUTER.
if ispc && isequal(get(hObject,'BackgroundColor'), get(0,'defaultUicontrolBackgroundColor'))
    set(hObject,'BackgroundColor','white');
end



function txtInputValues_Callback(hObject, eventdata, handles)
% hObject    handle to txtInputValues (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hints: get(hObject,'String') returns contents of txtInputValues as text
%        str2double(get(hObject,'String')) returns contents of txtInputValues as a double


% --- Executes during object creation, after setting all properties.
function txtInputValues_CreateFcn(hObject, eventdata, handles)
% hObject    handle to txtInputValues (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    empty - handles not created until after all CreateFcns called

% Hint: edit controls usually have a white background on Windows.
%       See ISPC and COMPUTER.
if ispc && isequal(get(hObject,'BackgroundColor'), get(0,'defaultUicontrolBackgroundColor'))
    set(hObject,'BackgroundColor','white');
end


% --- Executes on button press in btnComenzar.
function btnComenzar_Callback(hObject, eventdata, handles)
% hObject    handle to btnComenzar (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)
nameTestValues = strcat(get(handles.txtInputValues, 'String'),'.txt');
testValues = (load(nameTestValues))';
nameTargetValues = strcat(get(handles.txtInputTargets, 'String'), '.txt');
targetValues = (load(nameTargetValues))';
archVector = str2num(get(handles.txtArchVector, 'String'));
functionVector = str2num(get(handles.txtFunctionVector, 'String'));
learningRate = str2num(get(handles.txtLearningRate, 'String'));
itMax =  str2num(get(handles.txtitMax, 'String'));
eEval =  str2num(get(handles.txteEval, 'String'));
itVal =  str2num(get(handles.txtitVal, 'String'));
numVal = str2num(get(handles.txtNumVal, 'String'));
mlp(archVector, functionVector, testValues, targetValues, learningRate, itMax,eEval,itVal,numVal);
cells = matfile('data.mat');
cellWeights = cells.c_weights;
numCapas = size(cellWeights,2);
for i=1:numCapas
    figure;
    graphWeight(cellWeights(:,i),int2str(i));
end






%------------plots test values------------------------    
test = load('test.txt');

p = test(:,1);
t = test(:,2);
a = test(:,3);
figure();
hold on;
scatter(p,t,'red','filled');
scatter(p,a,'blue','filled');
legend('target','a');


%-----------------plot errors-----------------
eent = load('eent.txt');
eval = load('eval.txt');

figure();
hold on;
scatter(eent(:,1),eent(:,2),'red','filled');
scatter(eval(:,1),eval(:,2),'blue','filled');
legend('eent','eval');
