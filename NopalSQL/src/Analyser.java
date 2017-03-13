import java.util.ArrayList;
public class Analyser{
	private String q;
	private int ptr, i;
	private int END_OF_LINE, WORDS_LIMIT;
	private String[] words;
	private String message;
	
	private final static String
	CREATE = "create",
	DATABASE = "database",
	USE = "use",
	DROP = "drop",
	TABLE = "table",
	INSERT = "insert",
	INTO = "into",
	VALUES = "values",
	SELECT = "select",
	FROM = "from",
	WHERE = "where",
	DELETE = "delete",
	UPDATE = "update",
	SET = "set",
	SHOW = "show",
	DATABASES = "databases",
	TABLES = "tables";

	private final static String[]
	DATA_TYPES = {
		"int", 
		"float", 
		"double", 
		"char", 
		"byte", 
		"long", 
		"boolean"}
	,RESERVED_WORDS = {
		CREATE,
		DATABASE,
		USE,
		DROP,
		TABLE,
		INSERT,
		INTO,
		VALUES,
		SELECT,
		FROM,
		WHERE,
		DELETE,
		UPDATE,
		SET,
		SHOW,
		DATABASES,
		TABLES,
		","
	};
        
        ArrayList<Database> databases = new ArrayList<>();
        int sel = -1;
        
	public String[] getWords(){
		return words;
	}
	public String getMessage(){
		return message;
	}
	
	private boolean error_sintaxis(){
		message = "ERROR: Revisar sintaxis cerca de \'"+words[i-1]+" "+words[i]+"\'";
		return false;
	}
        private boolean noDB_error(){
            message = "Ninguna base de datos seleccionada.";
            return false;
        }
        private boolean error(String s){
            message = s;
            return false;
        }
	private boolean wordIs(String s){
		return words[i].equalsIgnoreCase(s);
	}
	private boolean isReserved(char c){
		char[] reserved = {'(', ')', ',', ';', '=', '['};
		for(char i : reserved){
			if(c == i)
				return true;
		}
		return false;
	}
	private boolean wordIsReserved(){
		for(String s : RESERVED_WORDS){
			if(wordIs(s))
				return true;
		}
		return false;
	}

	public String nextWord(){
		String word = "";
		if(ptr < END_OF_LINE){
			while(q.charAt(ptr)==' '){
				ptr++;
				if(ptr == END_OF_LINE)
					break;
			}
			if(ptr < END_OF_LINE){
				char first = q.charAt(ptr);
				if(first == '\"' || first == '\''){
					word += first;
					ptr++;
					while(q.charAt(ptr)!=first){
						word += q.charAt(ptr);
						ptr++;
						if(ptr== END_OF_LINE)
							break;
					}
					if(ptr < END_OF_LINE)
						word += q.charAt(ptr++);
				}
				else if(first == '['){
					word += first;
					ptr++;
					while(q.charAt(ptr) != ']'){
						word += q.charAt(ptr);
						ptr++;
						if(ptr == END_OF_LINE)
							break;
					}
					if(ptr < END_OF_LINE)
						word += q.charAt(ptr++);
					word = word.replace(" ", "");
				}
				else if(isReserved(first)){
					word += first;
					ptr++;
				}
				else{
					while(q.charAt(ptr)!=' '){
						if(isReserved(q.charAt(ptr)))
							break;
						word += q.charAt(ptr);
						ptr++;
						if(ptr== END_OF_LINE)
							break;
					}
				}
			}
		}
		return word;
	}	
	private void setWords(){
		ArrayList<String> w = new ArrayList<>();
		while(ptr != END_OF_LINE){
			w.add(nextWord());
		}
		w.remove(w.size()-1);
		words = new String[w.size()];
		words = w.toArray(words);
	}
	private void initialize(String q){
		this.q = q;
		END_OF_LINE = q.length();
		ptr = 0;
		i = 0;
		setWords();
		WORDS_LIMIT = words.length;
	}
	public boolean execute(String s){
		initialize(s);
		try{
			String next = words[i];
			i++;
			if(next.equalsIgnoreCase(CREATE))
				return validar_create();
			else if(next.equalsIgnoreCase(USE))
				return validar_use();
			else if(next.equalsIgnoreCase(DROP))
				return validar_drop();
			else if(next.equalsIgnoreCase(INSERT))
				return validar_insert();
			else if(next.equalsIgnoreCase(SELECT))
				return validar_select();
			else if(next.equalsIgnoreCase(DELETE))
				return validar_delete();
			else if(next.equalsIgnoreCase(UPDATE))
				return validar_update();
			else if(next.equalsIgnoreCase(SHOW))
				return validar_show();
			else{
				message = "Instruccion \'"+next+"\' no encontrada";
				return false;
			}
		}catch(ArrayIndexOutOfBoundsException ex){
			message = "Fin de linea alcanzada. Instruccion incompleta";
			return false;
		}
	}

	
	private boolean nombre_valido(){
		String s = words[i];
		if(wordIsReserved())
			return false;
		if(s.charAt(0) == '\'' || s.charAt(0) == '\"'){
			s = s.substring(1, (s.length()-1));
		}
		words[i] = s;
		return !(s.indexOf(' ') > 0);
	}
	private boolean valor_valido(){
		String s = words[i];
		if(s.charAt(0) == '\'' || s.charAt(0) == '\"'){
			s = s.substring(1, (s.length()-1));
		}
		words[i] = s;
		return true;
	}
	private boolean dato_valido(){
		for(String s : DATA_TYPES){
			if(wordIs(s))
				return true;
		}
		return false;
	}


	private boolean validar_create(){
		String next = words[i];
		i++;
		if(next.equalsIgnoreCase(DATABASE))
			return validar_create_database();
		else if(next.equalsIgnoreCase(TABLE))
			return validar_create_table();
		else	return error_sintaxis();
	}
	private boolean validar_drop(){
		String next = words[i];
		i++;
		if(next.equalsIgnoreCase(DATABASE))
			return validar_drop_database();
		else if(next.equalsIgnoreCase(TABLE))
			return validar_drop_table();
		else	return error_sintaxis();
	}
	private boolean validar_show(){
		String next = words[i];
		i++;
		if(next.equalsIgnoreCase(DATABASES) && i == WORDS_LIMIT){
                        if(databases.size() == 0){
                            message = "\tVacio";
                        }
                        else{
                            message = "-------- BASES DE DATOS --------";
                            for(int j = 0; j < databases.size(); j++){
                                message += "\n"+databases.get(j).getName();
                            }
                        }
			return true;
		}
		else if(next.equalsIgnoreCase(TABLES) && i == WORDS_LIMIT){
                    if(sel < 0)
                        return noDB_error();
                    Database db = databases.get(sel);
                    String [] tables = db.showTables();
                    if(tables.length == 0){
                        message = "\t Vacio";
                    }
                    else{
                        message = "---- Tablas en "+db.getName()+" ----";
                        for (String table : tables) {
                            message += "\n" + table;
                        }
                    }
                    return true;
		}
		return error_sintaxis();
	}
	private boolean validar_create_database(){
		if(nombre_valido()){
                    Database db = new Database(words[i]);
                    i++;
                    if(i==WORDS_LIMIT){
                        databases.add(db);
                        message = "Hecho";
                        return true;
                    }
		}
		else{
			message = "Nombre \'"+words[i]+"\' no valido";
			return false;
		}
		return	error_sintaxis();
	}
	private boolean validar_drop_database(){
		if(nombre_valido()){
			String name = words[i];
			i++;
			if(i==WORDS_LIMIT){
                            for(int j=0; j<databases.size(); j++){
                                if(databases.get(j).getName().equals(name)){
                                    databases.remove(j);
                                    message = "Hecho";
                                    return true;
                                }
                            }
                            message = "Base de datos '"+name+"' desconocida";
                            return false;
                        }
		}
		else{
			message = "Nombre \'"+words[i]+"\' no valido";
			return false;
		}
		return	error_sintaxis();
	}
	private boolean validar_use(){
		if(nombre_valido()){
			String name = words[i];
			i++;
			if(i==WORDS_LIMIT){
                            for(int j=0; j<databases.size(); j++){
                                if(databases.get(j).getName().equals(name)){
                                    sel = j;
                                    message = "Base de datos seleccionada";
                                    return true;
                                }
                            }
                            message = "Base de datos '"+name+"' desconocida";
                            return false;
                        }
		}
		else{
			message = "Nombre \'"+words[i]+"\' no valido";
			return false;
		}
		return error_sintaxis();
	}
	private boolean validar_drop_table(){
            if(sel < 0)
                return noDB_error();
            Database db = databases.get(sel);
		if(nombre_valido()){
			String name = words[i];
			i++;
			if(i==WORDS_LIMIT){
                            db.dropTable(name);
                            databases.set(sel, db);
                            message = "Hecho";
                            return true;
                        }
		}
		else{
			message = "Nombre \'"+words[i]+"\' no valido";
			return false;
		}
		return error_sintaxis();
	}
        
        //FALTA
	private boolean validar_create_table(){
		if(nombre_valido() && words[i+1].equalsIgnoreCase("(")){
			message = "Se creara la tabla con los valores:";
			i+=2;
			if(nombre_valido()){
				message += "\n \'"+words[i]+"\'";
				i++;
				if(dato_valido()){
					message += "\t"+words[i];
					i++;
					if(wordIs(")")){
						i++;
						if(i == WORDS_LIMIT)
							return true;
					}
					else{
						while(!wordIs(")")){
							if(wordIs(",")){
								message += words[i];
								i++;
								if(nombre_valido()){
									message += "\n \'"+words[i]+"\'";
									i++;
									if(dato_valido()){
										message += "\t"+words[i];
										i++;
									}
								}
								else break;
							}
							else break;
						}
						if(wordIs(")")){
							i++;
							if(i == WORDS_LIMIT)
								return true;
						}
					}
				}
			}
		}
		return error_sintaxis();
	}
	private boolean validar_insert(){
		if(wordIs(INTO)){//insert into
			i++;
			if(nombre_valido()){//insert into ´table_name´
				message = "Se ingresara en la tabla \""+words[i]+"\":";
				i++;
				if(wordIs(VALUES)){//insert into ´table_name´ values
					i++;
					if(wordIs("(")){
						i++;
						if(valor_valido()){
							message += "\n"+words[i];
							i++;
							while(!wordIs(")")){
								if(wordIs(",")){
									i++;
									if(valor_valido()){
										message +=",\n"+words[i];
										i++;
									}
									else break;
								}
								else break;
							}
							if(wordIs(")")){
								i++;
								if(i == WORDS_LIMIT)
									return true;
							}
						}
					}
				}
			}
		}
		return error_sintaxis();
	}
	private boolean validar_select(){
		message = "Se mostraran los campos:";
		if(nombre_valido() && !wordIs(",")){
			if(wordIs("*")){
				message +="\nTODOS";
				i++;
				if(!wordIs(FROM))	return error_sintaxis();
			}
			else{
				message +="\n"+words[i];
				i++;
				while(!wordIs(FROM)){
					if(wordIs(",")){
						i++;
						if(nombre_valido()){
							message += ",\n"+words[i];
							i++;
						}
						else break;
					}
					else break;
				}
				if(!wordIs(FROM))	return error_sintaxis();
			}
			i++;
			if(nombre_valido()){
				message +="\nDe la tabla \'"+words[i]+"\'";
				i++;
				if(i == WORDS_LIMIT)
					return true;
				else if(wordIs(WHERE)){
					message += "\nCondicion: ";
					i++;
					if(nombre_valido()){
						message += words[i];
						i++;
						if(wordIs("=")){
							message += " = ";
							i++;
							if(valor_valido()){
								message += words[i];
								i++;
								if(i == WORDS_LIMIT)
									return true;
							}
						}
					}
				}
			}
		}
		return error_sintaxis();
	}
	private boolean validar_delete(){
		if(wordIs(FROM)){
			i++;
			if(nombre_valido()){
				message = "Borrar de tabla\'"+words[i]+"\'";
				i++;
				if(i == WORDS_LIMIT){
					message += "\nTODO";
					return true;
				}
				else if(wordIs(WHERE)){
					message +="\nCondicion: ";
					i++;
					if(nombre_valido()){
						message += words[i];
						i++;
						if(wordIs("=")){
							message += " = ";
							i++;
							if(valor_valido()){
								message += words[i];
								i++;
								if(i == WORDS_LIMIT)
									return true;
							}
						}
					}
				}
			}
		}
		return error_sintaxis();
	}
	private boolean validar_update(){
		if(nombre_valido()){
			message = "Actualizar datos de la tabla \'"+words[i]+"\':";
			i++;
			if(wordIs(SET)){
				i++;
				if(nombre_valido()){
					message +="\n"+words[i];
					i++;
					if(wordIs("=")){
						message += " = ";
						i++;
						if(valor_valido()){
							message += words[i];
							i++;
							while(true){
								if(i == WORDS_LIMIT)
									break;
								else if(wordIs(WHERE))
									break;

								if(wordIs(",")){
									message += words[i];
									i++;
									if(nombre_valido()){
										message +="\n"+words[i];
										i++;
										if(wordIs("=")){
											message += " = ";
											i++;
											if(valor_valido()){
												message += words[i];
												i++;
											}
											else return error_sintaxis();
										}
										else return error_sintaxis();
									}
									else return error_sintaxis();
								}
								else return error_sintaxis();
							}
							if(i == WORDS_LIMIT){
								message +="\nCondicion: TODOS";
								return true;
							}
							else if(wordIs(WHERE)){
								message += "\nCondicion: ";
								i++;
								if(nombre_valido()){
									message += words[i];
									i++;
									if(wordIs("=")){
										message += " = ";
										i++;
										if(valor_valido()){
											message += words[i];
											i++;
											if(i == WORDS_LIMIT)
												return true;
										}
									}
								}
							}
						}
					}
				}
			}
		}
		return error_sintaxis();
	}
}