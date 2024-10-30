package josecarlos.lenguajes.proyecto2.sintactico;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import josecarlos.lenguajes.proyecto2.ListSyntaxError;
import josecarlos.lenguajes.proyecto2.ddl.Declaracion;
import josecarlos.lenguajes.proyecto2.ddl.Llave;
import josecarlos.lenguajes.proyecto2.ddl.Alter;
import josecarlos.lenguajes.proyecto2.ddl.Tabla;
import josecarlos.lenguajes.proyecto2.enums.DataType;
import josecarlos.lenguajes.proyecto2.enums.PalabraReservada;
import josecarlos.lenguajes.proyecto2.tokens.Token;
import josecarlos.lenguajes.proyecto2.tokens.TokenType;

/**
 *
 * @author emahch
 */
public class AnalizadorDDL {

    private static final String[] ACEPTED = {PalabraReservada.DATABASE.name(), PalabraReservada.TABLE.name()};

    private List<String> DBCreadas;
    private List<Tabla> tablasCreadas;
    private List<Alter> tablasModificadas;
    private List<Tabla> tablasBorradas;
    private ListSyntaxError tokensError;
    private AnalizadorHelper helper;

    public AnalizadorDDL(ListSyntaxError tokensError) {
        this.DBCreadas = new ArrayList<>();
        this.tablasCreadas = new ArrayList<>();
        this.tablasModificadas = new ArrayList<>();
        this.tablasBorradas = new ArrayList<>();
        this.tokensError = tokensError;
        this.helper = new AnalizadorHelper();
    }

    public void analizarCreate(Pila pila) {
        pila.popFirst();
        Token token = pila.popFirst();

        if (token.getTipo().equals(TokenType.PALABRA_RESERVADA) && token.getValor().equals(PalabraReservada.DATABASE.name())) {
            analizarCreacionDB(pila);
        } else if (token.getTipo().equals(TokenType.PALABRA_RESERVADA) && token.getValor().equals(PalabraReservada.TABLE.name())) {
            analizarCreacionTabla(pila);
        } else {
            this.tokensError.addError(token, ACEPTED);
        }
    }

    public void analizarAlter(Pila pila) {
        pila.popFirst();
        Token token = pila.popFirst();
        if (!helper.analizarValor(token, PalabraReservada.TABLE.name())) {
            this.tokensError.addError(token, PalabraReservada.TABLE.name());
            return;
        }
        token = pila.popFirst();
        if (helper.analizarTipo(token, TokenType.IDENTIFICADOR)) {
            
        } else {
            this.tokensError.addError(token, TokenType.IDENTIFICADOR);
            return;
        }
    }

    public void analizarDrop(Pila pila) {
        pila.popFirst();
        Token token = pila.popFirst();

        if (token.getTipo().equals(TokenType.PALABRA_RESERVADA) && token.getValor().equals(PalabraReservada.DATABASE.name())) {
            analizarCreacionDB(pila);
        } else if (token.getTipo().equals(TokenType.PALABRA_RESERVADA) && token.getValor().equals(PalabraReservada.TABLE.name())) {
            analizarCreacionTabla(pila);
        } else {
            this.tokensError.addError(token, ACEPTED);
        }
    }

    // CREATE DATABASE <identificador> ";"
    private void analizarCreacionDB(Pila pila) {
        String nameDB;
        Token token = pila.popFirst();
        if (helper.analizarTipo(token, TokenType.IDENTIFICADOR)) {
            nameDB = token.getValor();
        } else {
            this.tokensError.addError(token, TokenType.IDENTIFICADOR);
            return;
        }

        token = pila.popFirst();
        if (helper.analizarFinBloque(token)) { // Detecta si es un ; o el fin del bloque
            this.DBCreadas.add(nameDB);
            System.out.println("db creada: " + nameDB);
        } else {
            this.tokensError.addError(token, ";");
        }
    }

    // CREATE TABLE <identificador> "(" [Estructura_declaracion] "," [Estructura_llaves]? ")"";"
    private void analizarCreacionTabla(Pila pila) {
        Tabla tabla = new Tabla();

        Token token = pila.popFirst();
        if (helper.analizarTipo(token, TokenType.IDENTIFICADOR)) {
            tabla.setNombre(token.getValor());
        } else {
            this.tokensError.addError(token, TokenType.IDENTIFICADOR);
            return;
        }

        token = pila.popFirst();
        if (!helper.analizarValor(token, "(")) {
            this.tokensError.addError(token, "(");
            return;
        }

        boolean enDeclaraciones = true;
        do {
            Optional<Declaracion> posibleDeclaracion = analizarDeclaracion(pila);
            if (posibleDeclaracion.isPresent()) {
                tabla.addDeclaracion(posibleDeclaracion.get());
            }
            token = pila.popFirst();
            if (!helper.analizarValor(token, ",")) {
                if (helper.analizarValor(token, ")")) {
                    token = pila.popFirst();
                    if (helper.analizarFinBloque(token)) {
                        this.tablasCreadas.add(tabla);
                    } else {
                        this.tokensError.addError(token, ";");
                    }
                } else {
                    this.tokensError.addError(token, ")");
                }
                return;
            }
            Token peekToken = pila.getFirst();
            if (helper.analizarValor(peekToken, PalabraReservada.CONSTRAINT.name())) {
                enDeclaraciones = false;
            }
        } while (enDeclaraciones);

        Optional<Llave> posibleLlave = analizarLlave(pila);
        if (posibleLlave.isPresent()) {
            tabla.setLlave(posibleLlave.get());
        } else {
            return;
        }

        token = pila.popFirst();
        if (helper.analizarValor(token, ")")) {
            token = pila.popFirst();
            if (helper.analizarFinBloque(token)) {
                this.tablasCreadas.add(tabla);
            } else {
                this.tokensError.addError(token, ";");
            }
        } else {
            this.tokensError.addError(token, ")");
        }
    }

    private Optional<Llave> analizarLlave(Pila pila) {
        Llave llave = new Llave();
        Token token = pila.popFirst();
        if (!helper.analizarValor(token, PalabraReservada.CONSTRAINT.name())) {
            this.tokensError.addError(token, PalabraReservada.CONSTRAINT.name());
            return Optional.empty();
        }

        token = pila.popFirst();
        if (helper.analizarTipo(token, TokenType.IDENTIFICADOR)) {
            llave.setIdentificador(token.getValor());
        } else {
            this.tokensError.addError(token, TokenType.IDENTIFICADOR);
            return Optional.empty();
        }

        token = pila.popFirst();
        if (!helper.analizarValor(token, PalabraReservada.FOREIGN.name())) {
            this.tokensError.addError(token, PalabraReservada.FOREIGN.name());
            return Optional.empty();
        }

        token = pila.popFirst();
        if (!helper.analizarValor(token, PalabraReservada.KEY.name())) {
            this.tokensError.addError(token, PalabraReservada.KEY.name());
            return Optional.empty();
        }

        token = pila.popFirst();
        if (!helper.analizarValor(token, "(")) {
            this.tokensError.addError(token, "(");
            return Optional.empty();
        }

        token = pila.popFirst();
        if (helper.analizarTipo(token, TokenType.IDENTIFICADOR)) {
            llave.setForeignKey(token.getValor());
        } else {
            this.tokensError.addError(token, TokenType.IDENTIFICADOR);
            return Optional.empty();
        }

        token = pila.popFirst();
        if (!helper.analizarValor(token, ")")) {
            this.tokensError.addError(token, ")");
            return Optional.empty();
        }

        token = pila.popFirst();
        if (!helper.analizarValor(token, PalabraReservada.REFERENCES.name())) {
            this.tokensError.addError(token, PalabraReservada.REFERENCES.name());
            return Optional.empty();
        }

        token = pila.popFirst();
        if (helper.analizarTipo(token, TokenType.IDENTIFICADOR)) {
            llave.setReference(token.getValor());
        } else {
            this.tokensError.addError(token, TokenType.IDENTIFICADOR);
            return Optional.empty();
        }

        token = pila.popFirst();
        if (!helper.analizarValor(token, "(")) {
            this.tokensError.addError(token, "(");
            return Optional.empty();
        }

        token = pila.popFirst();
        if (helper.analizarTipo(token, TokenType.IDENTIFICADOR)) {
            llave.setIdReference(token.getValor());
        } else {
            this.tokensError.addError(token, TokenType.IDENTIFICADOR);
            return Optional.empty();
        }

        token = pila.popFirst();
        if (!helper.analizarValor(token, ")")) {
            this.tokensError.addError(token, ")");
            return Optional.empty();
        }

        return Optional.of(llave);
    }

    // <identificador> [Tipo_de_dato] [Condiciones]*
    private Optional<Declaracion> analizarDeclaracion(Pila pila) {
        Declaracion declaracion = new Declaracion();

        Token token = pila.popFirst();
        if (helper.analizarTipo(token, TokenType.IDENTIFICADOR)) {
            declaracion.setIdentificador(token.getValor());
        } else {
            this.tokensError.addError(token, TokenType.IDENTIFICADOR);
            return Optional.empty();
        }

        Optional<String> posibleDato = analizarDato(pila);
        if (posibleDato.isEmpty()) {
            return Optional.empty();
        }
        declaracion.setTipoDato(posibleDato.get());

        StringBuilder condicion = new StringBuilder();
        token = pila.getFirst();
        if (helper.analizarValor(token, Declaracion.ACEPTED)) {
            token = pila.popFirst();
            condicion.append(token.getValor());
            switch (token.getValor()) {
                case "PRIMARY" -> {
                    condicion.append(" ");
                    token = pila.popFirst();
                    if (helper.analizarValor(token, "KEY")) {
                        condicion.append(token.getValor());
                        declaracion.setLlavePrimaria(true);
                    } else {
                        this.tokensError.addError(token, "KEY");
                        return Optional.empty();
                    }
                }
                case "NOT" -> {
                    condicion.append(" ");
                    token = pila.popFirst();
                    if (helper.analizarValor(token, "NULL")) {
                        condicion.append(token.getValor());
                    } else {
                        this.tokensError.addError(token, "NULL");
                        return Optional.empty();
                    }
                }
                default -> {
                }
            }
            declaracion.setCondicion(condicion.toString());

        }
        return Optional.of(declaracion);
    }

    private Optional<String> analizarDato(Pila pila) {
        Token token = pila.popFirst();
        Optional<DataType> posibleTipoDato = helper.analizarDato(token);
        if (posibleTipoDato.isEmpty()) {
            this.tokensError.addError(token, TokenType.TIPO_DATO);
            return Optional.empty();
        }
        String dato = null;
        DataType tipoDato = posibleTipoDato.get();
        if (helper.isDatoEspecial(tipoDato)) {
            Optional<String> posibleDato = analizarDatoEspecial(pila, tipoDato);
            if (posibleDato.isPresent()) {
                dato = posibleDato.get();
            }
        } else {
            dato = tipoDato.name();
        }
        if (dato == null) {
            return Optional.empty();
        }
        return Optional.of(dato);
    }

    private Optional<String> analizarDatoEspecial(Pila pila, DataType tipoDato) {
        StringBuilder sb = new StringBuilder();
        sb.append(tipoDato.name());
        Token token = pila.popFirst();
        // VARCHAR(<entero>)
        if (tipoDato.equals(DataType.VARCHAR)) {
            if (helper.analizarValor(token, "(")) {
                sb.append(token.getValor());
            } else {
                this.tokensError.addError(token, "(");
                return Optional.empty();
            }

            token = pila.popFirst();
            if (helper.analizarTipo(token, TokenType.ENTERO)) {
                sb.append(token.getValor());
            } else {
                this.tokensError.addError(token, TokenType.ENTERO);
                return Optional.empty();
            }

            token = pila.popFirst();
            if (helper.analizarValor(token, ")")) {
                sb.append(token.getValor());
            } else {
                this.tokensError.addError(token, ")");
                return Optional.empty();
            }

            return Optional.of(sb.toString());
        }

        // DECIMAL(<entero>, <entero>) | NUMERIC(<entero>, <entero>)
        if (tipoDato.equals(DataType.DECIMAL) || tipoDato.equals(DataType.NUMERIC)) {
            if (helper.analizarValor(token, "(")) {
                sb.append(token.getValor());
            } else {
                this.tokensError.addError(token, "(");
                return Optional.empty();
            }

            token = pila.popFirst();
            if (helper.analizarTipo(token, TokenType.ENTERO)) {
                sb.append(token.getValor());
            } else {
                this.tokensError.addError(token, TokenType.ENTERO);
                return Optional.empty();
            }

            token = pila.popFirst();
            if (helper.analizarValor(token, ",")) {
                sb.append(token.getValor());
            } else {
                this.tokensError.addError(token, ",");
                return Optional.empty();
            }

            token = pila.popFirst();
            if (helper.analizarTipo(token, TokenType.ENTERO)) {
                sb.append(token.getValor());
            } else {
                this.tokensError.addError(token, TokenType.ENTERO);
                return Optional.empty();
            }

            token = pila.popFirst();
            if (helper.analizarValor(token, ")")) {
                sb.append(token.getValor());
            } else {
                this.tokensError.addError(token, ")");
                return Optional.empty();
            }

            return Optional.of(sb.toString());
        }
        this.tokensError.addError(token, TokenType.TIPO_DATO);
        return Optional.empty();
    }

    public void printDB() {
        if (DBCreadas.isEmpty()) {
            return;
        }
        for (String DBCreada : DBCreadas) {
            System.out.println("DB creada: " + DBCreada);
        }
    }

    public void printTables() {
        if (tablasCreadas.isEmpty()) {
            return;
        }
        for (Tabla tabla : tablasCreadas) {
            System.out.println(tabla.print());
        }
    }
}
