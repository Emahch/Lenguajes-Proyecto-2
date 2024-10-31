package josecarlos.lenguajes.proyecto2.sintactico;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import josecarlos.lenguajes.proyecto2.Operaciones;
import josecarlos.lenguajes.proyecto2.tokens.ListSyntaxError;
import josecarlos.lenguajes.proyecto2.ddl.Declaracion;
import josecarlos.lenguajes.proyecto2.ddl.Llave;
import josecarlos.lenguajes.proyecto2.ddl.Alter;
import josecarlos.lenguajes.proyecto2.ddl.AlterType;
import josecarlos.lenguajes.proyecto2.ddl.Drop;
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
    private List<Drop> tablasBorradas;
    private ListSyntaxError tokensError;
    private AnalizadorHelper helper;
    private Operaciones operaciones;

    public AnalizadorDDL(ListSyntaxError tokensError, Operaciones operaciones) {
        this.DBCreadas = new ArrayList<>();
        this.tablasCreadas = new ArrayList<>();
        this.tablasModificadas = new ArrayList<>();
        this.tablasBorradas = new ArrayList<>();
        this.tokensError = tokensError;
        this.helper = new AnalizadorHelper();
        this.operaciones = operaciones;
    }

    /* -------------------------------------- CREATE --------------------------------*/
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
            this.operaciones.sumCreate();
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
                        this.operaciones.sumCreate();
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

        Llave llave = new Llave();
        token = pila.popFirst();
        if (!helper.analizarValor(token, PalabraReservada.CONSTRAINT.name())) {
            this.tokensError.addError(token, PalabraReservada.CONSTRAINT.name());
            return;
        }

        token = pila.popFirst();
        if (helper.analizarTipo(token, TokenType.IDENTIFICADOR)) {
            llave.setIdentificador(token.getValor());
        } else {
            this.tokensError.addError(token, TokenType.IDENTIFICADOR);
            return;
        }

        token = pila.popFirst();
        if (!helper.analizarValor(token, PalabraReservada.FOREIGN.name())) {
            this.tokensError.addError(token, PalabraReservada.FOREIGN.name());
            return;
        }
        Optional<Llave> posibleLlave = analizarLlave(pila, llave);
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
                this.operaciones.sumCreate();
            } else {
                this.tokensError.addError(token, ";");
            }
        } else {
            this.tokensError.addError(token, ")");
        }
    }

    private Optional<Llave> analizarLlave(Pila pila, Llave llave) {
        Token token = pila.popFirst();
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

    /* -------------------------------------- ALTER --------------------------------*/
    public void analizarAlter(Pila pila) {
        Alter alter = new Alter();
        pila.popFirst();
        Token token = pila.popFirst();
        if (!helper.analizarValor(token, PalabraReservada.TABLE.name())) {
            this.tokensError.addError(token, PalabraReservada.TABLE.name());
            return;
        }
        token = pila.popFirst();
        if (helper.analizarTipo(token, TokenType.IDENTIFICADOR)) {
            alter.setIdTabla(token.getValor());
        } else {
            this.tokensError.addError(token, TokenType.IDENTIFICADOR);
            return;
        }

        Optional<Alter> posibleAlter;
        token = pila.popFirst();
        if (helper.analizarValor(token, AlterType.array)) {
            switch (token.getValor()) {
                case "ADD" -> {
                    alter.setTipo(AlterType.ADD);
                    posibleAlter = analizarAlterAdd(pila, alter);
                }
                case "ALTER" -> {
                    alter.setTipo(AlterType.ALTER);
                    posibleAlter = analizarAlterAlter(pila, alter);
                }
                case "DROP" -> {
                    alter.setTipo(AlterType.DROP);
                    posibleAlter = analizarAlterDrop(pila, alter);
                }
                default -> {
                    return;
                }
            }
            if (posibleAlter.isPresent()) {
                alter = posibleAlter.get();
            } else {
                return;
            }
        } else {
            this.tokensError.addError(token, AlterType.array);
            return;
        }

        token = pila.popFirst();
        if (helper.analizarFinBloque(token)) {
            this.tablasModificadas.add(alter);
            this.operaciones.sumAlter();
        } else {
            this.tokensError.addError(token, ";");
        }
    }

    private Optional<Alter> analizarAlterAdd(Pila pila, Alter alter) {
        String[] valoresPosibles = {PalabraReservada.COLUMN.name(), PalabraReservada.CONSTRAINT.name()};

        Optional<Alter> posibleAlter;
        Token token = pila.popFirst();
        if (helper.analizarValor(token, valoresPosibles)) {
            alter.setObjetivo(token.getValor());
            if (helper.analizarValor(token, PalabraReservada.COLUMN.name())) {
                posibleAlter = analizarAddColumn(pila, alter);
            } else if (helper.analizarValor(token, PalabraReservada.CONSTRAINT.name())) {
                posibleAlter = analizarAddConstraint(pila, alter);
            } else {
                return Optional.empty();
            }
        } else {
            this.tokensError.addError(token, valoresPosibles);
            return Optional.empty();
        }
        return posibleAlter;
    }

    private Optional<Alter> analizarAddColumn(Pila pila, Alter alter) {
        Token token = pila.popFirst();
        if (helper.analizarTipo(token, TokenType.IDENTIFICADOR)) {
            alter.setIdentificador(token.getValor());
        } else {
            this.tokensError.addError(token, TokenType.IDENTIFICADOR);
            return Optional.empty();
        }

        Optional<String> posibleDato = analizarDato(pila);
        if (posibleDato.isEmpty()) {
            return Optional.empty();
        }
        alter.setTipoDato(posibleDato.get());
        return Optional.of(alter);
    }

    private Optional<Alter> analizarAddConstraint(Pila pila, Alter alter) {
        Llave llave = new Llave();
        Token token = pila.popFirst();

        if (helper.analizarTipo(token, TokenType.IDENTIFICADOR)) {
            llave.setIdentificador(token.getValor());
            alter.setIdentificadorObjetivo(token.getValor());
        } else {
            this.tokensError.addError(token, TokenType.IDENTIFICADOR);
            return Optional.empty();
        }

        String[] valoresPosibles = {PalabraReservada.UNIQUE.name(), PalabraReservada.FOREIGN.name()};
        token = pila.popFirst();
        if (helper.analizarValor(token, valoresPosibles)) {
            if (helper.analizarValor(token, PalabraReservada.UNIQUE.name())) {
                Optional<Alter> posibleAlter = analizarConstraintUnique(pila, alter);
                if (posibleAlter.isPresent()) {
                    alter = posibleAlter.get();
                } else {
                    return Optional.empty();
                }
            } else {
                Optional<Llave> posibleLlave = analizarLlave(pila, llave);
                if (posibleLlave.isPresent()) {
                    alter.setIdentificadorObjetivo(null);
                    alter.setLlave(posibleLlave.get());
                } else {
                    return Optional.empty();
                }
            }
        } else {
            Optional<String> posibleDato = analizarDato(pila);
            if (posibleDato.isEmpty()) {
                return Optional.empty();
            }
            alter.setTipoDato(posibleDato.get());
        }
        return Optional.of(alter);

    }

    private Optional<Alter> analizarConstraintUnique(Pila pila, Alter alter) {
        Token token = pila.popFirst();
        if (!helper.analizarValor(token, "(")) {
            this.tokensError.addError(token, "(");
            return Optional.empty();
        }
        token = pila.popFirst();
        if (helper.analizarTipo(token, TokenType.IDENTIFICADOR)) {
            alter.setIdentificadorObjetivo(token.getValor());
        } else {
            this.tokensError.addError(token, TokenType.IDENTIFICADOR);
            return Optional.empty();
        }
        token = pila.popFirst();
        if (!helper.analizarValor(token, ")")) {
            this.tokensError.addError(token, ")");
            return Optional.empty();
        }
        return Optional.of(alter);
    }

    private Optional<Alter> analizarAlterAlter(Pila pila, Alter alter) {
        Token token = pila.popFirst();
        if (helper.analizarValor(token, PalabraReservada.COLUMN.name())) {
            alter.setObjetivo(token.getValor());
        } else {
            this.tokensError.addError(token, PalabraReservada.COLUMN.name());
            return Optional.empty();
        }
        token = pila.popFirst();
        if (helper.analizarTipo(token, TokenType.IDENTIFICADOR)) {
            alter.setIdentificador(token.getValor());
        } else {
            this.tokensError.addError(token, TokenType.IDENTIFICADOR);
            return Optional.empty();
        }

        token = pila.popFirst();
        if (!helper.analizarValor(token, PalabraReservada.TYPE.name())) {
            this.tokensError.addError(token, PalabraReservada.TYPE.name());
            return Optional.empty();
        }

        Optional<String> posibleDato = analizarDato(pila);
        if (posibleDato.isEmpty()) {
            return Optional.empty();
        }
        alter.setTipoDato(posibleDato.get());
        return Optional.of(alter);
    }

    private Optional<Alter> analizarAlterDrop(Pila pila, Alter alter) {
        Token token = pila.popFirst();
        if (helper.analizarValor(token, PalabraReservada.COLUMN.name())) {
            alter.setObjetivo(token.getValor());
        } else {
            this.tokensError.addError(token, PalabraReservada.COLUMN.name());
            return Optional.empty();
        }
        token = pila.popFirst();
        if (helper.analizarTipo(token, TokenType.IDENTIFICADOR)) {
            alter.setIdentificador(token.getValor());
        } else {
            this.tokensError.addError(token, TokenType.IDENTIFICADOR);
            return Optional.empty();
        }

        return Optional.of(alter);
    }

    /* -------------------------------------- DROP --------------------------------*/
    public void analizarDrop(Pila pila) {
        Drop drop = new Drop();
        pila.popFirst();
        Token token = pila.popFirst();
        if (!helper.analizarValor(token, PalabraReservada.TABLE.name())) {
            this.tokensError.addError(token, PalabraReservada.TABLE.name());
            return;
        }
        token = pila.popFirst();
        if (!helper.analizarValor(token, PalabraReservada.IF.name())) {
            this.tokensError.addError(token, PalabraReservada.IF.name());
            return;
        }
        token = pila.popFirst();
        if (!helper.analizarValor(token, PalabraReservada.EXISTS.name())) {
            this.tokensError.addError(token, PalabraReservada.EXISTS.name());
            return;
        }
        token = pila.popFirst();
        if (helper.analizarTipo(token, TokenType.IDENTIFICADOR)) {
            drop.setIdTabla(token.getValor());
        } else {
            this.tokensError.addError(token, TokenType.IDENTIFICADOR);
            return;
        }
        token = pila.popFirst();
        if (!helper.analizarValor(token, PalabraReservada.CASCADE.name())) {
            this.tokensError.addError(token, PalabraReservada.CASCADE.name());
            return;
        }
        token = pila.popFirst();
        if (helper.analizarFinBloque(token)) {
            this.tablasBorradas.add(drop);
        } else {
            this.tokensError.addError(token, ";");
        }
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
