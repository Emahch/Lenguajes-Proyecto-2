package josecarlos.lenguajes.proyecto2.sintactico;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import josecarlos.lenguajes.proyecto2.Operaciones;
import josecarlos.lenguajes.proyecto2.dml.Insert;
import josecarlos.lenguajes.proyecto2.enums.Booleano;
import josecarlos.lenguajes.proyecto2.enums.Logico;
import josecarlos.lenguajes.proyecto2.enums.PalabraReservada;
import josecarlos.lenguajes.proyecto2.tokens.ListSyntaxError;
import josecarlos.lenguajes.proyecto2.tokens.Token;
import josecarlos.lenguajes.proyecto2.tokens.TokenType;

/**
 *
 * @author emahch
 */
public class AnalizadorDML {

    private Operaciones operaciones;
    private ListSyntaxError tokensError;
    private AnalizadorHelper helper;
    private List<Insert> inserts;

    public AnalizadorDML(ListSyntaxError tokensError, Operaciones operaciones) {
        this.tokensError = tokensError;
        this.helper = new AnalizadorHelper();
        this.inserts = new ArrayList<>();
        this.operaciones = operaciones;
    }

    // --------------------------- INSERT ------------------------------------
    public void analizarInsert(Pila pila) {
        Insert insert = new Insert();
        pila.popFirst();
        Token token = pila.popFirst();
        if (!helper.analizarValor(token, PalabraReservada.INTO.name())) {
            this.tokensError.addError(token, PalabraReservada.INTO.name());
            return;
        }

        token = pila.popFirst();
        if (helper.analizarTipo(token, TokenType.IDENTIFICADOR)) {
            insert.setIdTabla(token.getValor());
        } else {
            this.tokensError.addError(token, TokenType.IDENTIFICADOR);
            return;
        }

        Optional<List<String>> posiblesIdentificadores = getIdentificadores(pila);
        if (posiblesIdentificadores.isEmpty()) {
            return;
        }

        insert.setColumnas(posiblesIdentificadores.get());

        token = pila.popFirst();
        if (!helper.analizarValor(token, PalabraReservada.VALUES.name())) {
            this.tokensError.addError(token, PalabraReservada.VALUES.name());
            return;
        }

        boolean inApartadoDatos = true;
        while (inApartadoDatos) {
            boolean inConjuntoDatos = true;
            while (inConjuntoDatos) {

                token = pila.popFirst();
                if (!helper.analizarValor(token, "(")) {
                    this.tokensError.addError(token, "(");
                    return;
                }

                boolean inDatos = true;
                List<String> listaDatos = new ArrayList<>();
                while (inDatos) {
                    Optional<String> posibleDato = getDato(pila);
                    if (posibleDato.isEmpty()) {
                        this.tokensError.addError(token, "[DATO]");
                        return;
                    }
                    listaDatos.add(posibleDato.get());

                    token = pila.popFirst();
                    if (!helper.analizarValor(token, ",")) {
                        inDatos = false;
                    }
                }

                if (!helper.analizarValor(token, ")")) {
                    this.tokensError.addError(token, new String[]{",", ")"});
                    return;
                } else {
                    inConjuntoDatos = false;
                }
                insert.addConjuntoDatos(listaDatos);
            }

            token = pila.popFirst();
            if (!helper.analizarValor(token, ",")) {
                inApartadoDatos = false;
            }
        }

        if (helper.analizarFinBloque(token)) {
            this.inserts.add(insert);
        } else {
            this.tokensError.addError(token, new String[]{",", ";"});
        }
    }

    private Optional<List<String>> getIdentificadores(Pila pila) {
        List<String> identificadores = new ArrayList<>();
        boolean enParentesis;
        Token token = pila.popFirst();
        if (helper.analizarValor(token, "(")) {
            enParentesis = true;
        } else {
            this.tokensError.addError(token, "(");
            return Optional.empty();
        }

        while (enParentesis) {
            token = pila.popFirst();
            if (helper.analizarTipo(token, TokenType.IDENTIFICADOR)) {
                identificadores.add(token.getValor());
            } else {
                this.tokensError.addError(token, TokenType.IDENTIFICADOR);
                return Optional.empty();
            }

            token = pila.getFirst();
            if (!helper.analizarValor(token, ",")) {
                enParentesis = false;
            } else {
                pila.popFirst();
            }
        }
        if (helper.analizarValor(token, ")")) {
            pila.popFirst();
            return Optional.of(identificadores);
        } else {
            this.tokensError.addError(token, new String[]{",", ")"});
            return Optional.empty();
        }
    }

    private Optional<String> getDato(Pila pila) {
        Token token = pila.popFirst();
        if (helper.analizarTipo(token, TokenType.ENTERO) || helper.analizarTipo(token, TokenType.DECIMAL)
                || helper.analizarTipo(token, TokenType.FECHA) || helper.analizarTipo(token, TokenType.CADENA)
                || helper.analizarValor(token, Booleano.TRUE.name()) || helper.analizarValor(token, Booleano.FALSE.name())) {
            Optional<String> posibleOperador = getOperador(pila);
            if (posibleOperador.isEmpty()) {
                return Optional.of(token.getValor());
            }
            String datoCompleto = token.getValor() + " " + posibleOperador.get();
            pila.popFirst();
            Optional<String> posibleDato = getDato(pila);
            if (posibleDato.isEmpty()) {
                return Optional.empty();
            }
            datoCompleto = datoCompleto + " " + posibleDato.get();
            return Optional.of(datoCompleto);
        } else if (helper.analizarValor(token, "(")) {
            Optional<String> posibleDato = getDato(pila);
            if (posibleDato.isEmpty()) {
                this.tokensError.addError(token, "[DATO]");
                return Optional.empty();
            }
            token = pila.popFirst();
            if (!helper.analizarValor(token, ")")) {
                this.tokensError.addError(token, ")");
                return Optional.empty();
            }
            String datoCompleto = "( " + posibleDato.get() + " )";
            return Optional.of(datoCompleto);
        }
        return Optional.empty();
    }

    private boolean isDato(Token token) {
        return helper.analizarTipo(token, TokenType.ENTERO) || helper.analizarTipo(token, TokenType.DECIMAL)
                || helper.analizarTipo(token, TokenType.FECHA) || helper.analizarTipo(token, TokenType.CADENA);
    }

    private Optional<String> getOperador(Pila pila) {
        String[] operadoresAceptados = {"+", "-", "*", "/", "<", ">", "<=", ">=", "AND", "OR", "NOT"};

        Token token = pila.getFirst();
        if (helper.analizarValor(token, operadoresAceptados)) {
            return Optional.of(token.getValor());
        } else {
            return Optional.empty();
        }
    }

    // ---------------------------- SELECT -------------------------------------
    public void analizarSelect(Pila pila) {
        pila.popFirst();
        Token token = pila.getFirst();
        if (!analizarSeleccionColumna(pila)) {
            this.tokensError.addError(token, "[SELECTOR_COLUMNA]");
            return;
        }

        token = pila.popFirst();
        if (!helper.analizarValor(token, PalabraReservada.FROM.name())) {
            this.tokensError.addError(token, PalabraReservada.FROM.name());
            return;
        }

        token = pila.popFirst();
        if (!helper.analizarTipo(token, TokenType.IDENTIFICADOR)) {
            this.tokensError.addError(token, TokenType.IDENTIFICADOR);
            return;
        }
        token = pila.getFirst();
        if (helper.analizarTipo(token, TokenType.IDENTIFICADOR)) {
            pila.popFirst();
        }

        analizarSentencia(pila);

        token = pila.popFirst();
        if (!helper.analizarFinBloque(token)) {
            this.tokensError.addError(token, ";");
            return;
        }
        this.operaciones.sumSelect();
    }

    private boolean analizarSeleccionColumna(Pila pila) {
        String[] agregacion = {"SUM", "AVG", "COUNT", "MIN", "MAX"};
        Token token = pila.popFirst();

        if (helper.analizarValor(token, "*")) {
            return true;
        }
        boolean enColumnas = true;
        while (enColumnas) {
            if (helper.analizarTipo(token, TokenType.IDENTIFICADOR)) {
                token = pila.getFirst();
                if (helper.analizarValor(token, ".")) {
                    pila.popFirst();
                    token = pila.popFirst();
                    if (!helper.analizarTipo(token, TokenType.IDENTIFICADOR)) {
                        this.tokensError.addError(token, TokenType.IDENTIFICADOR);
                        return false;
                    }
                }
            } else if (helper.analizarValor(token, agregacion)) {
                token = pila.popFirst();
                if (!helper.analizarValor(token, "(")) {
                    this.tokensError.addError(token, "(");
                    return false;
                }
                token = pila.popFirst();
                if (!helper.analizarTipo(token, TokenType.IDENTIFICADOR)) {
                    this.tokensError.addError(token, TokenType.IDENTIFICADOR);
                    return false;
                }
                token = pila.popFirst();
                if (!helper.analizarValor(token, ")")) {
                    this.tokensError.addError(token, ")");
                    return false;
                }
            } else {
                this.tokensError.addError(token, new String[]{"*", "<identificador>", "[FUNCION_AGREGACION]"});
                return false;
            }

            token = pila.getFirst();
            if (helper.analizarValor(token, PalabraReservada.AS.name())) {
                pila.popFirst();
                token = pila.getFirst();
                if (!helper.analizarTipo(token, TokenType.IDENTIFICADOR)) {
                    this.tokensError.addError(token, TokenType.IDENTIFICADOR);
                    return false;
                } else {
                    pila.popFirst();
                    token = pila.getFirst();
                }
            }
            if (!helper.analizarValor(token, ",")) {
                enColumnas = false;
            } else {
                pila.popFirst();
                token = pila.popFirst();
            }
        }
        return true;
    }

    private boolean analizarSentencia(Pila pila) {
        Token token = pila.getFirst();
        if (helper.analizarValor(token, PalabraReservada.JOIN.name())) {
            pila.popFirst();
            return analizarJoin(pila);
        } else if (helper.analizarValor(token, PalabraReservada.WHERE.name())) {
            pila.popFirst();
            return analizarWhere(pila);
        } else if (helper.analizarValor(token, PalabraReservada.GROUP.name())) {
            pila.popFirst();

        } else if (helper.analizarValor(token, PalabraReservada.ORDER.name())) {
            pila.popFirst();

        } else if (helper.analizarValor(token, PalabraReservada.LIMIT.name())) {
            pila.popFirst();

        } else {
            return false;
        }
        return true;
    }

    private boolean analizarJoin(Pila pila) {
        Token token = pila.popFirst();
        if (!helper.analizarTipo(token, TokenType.IDENTIFICADOR)) {
            this.tokensError.addError(token, TokenType.IDENTIFICADOR);
            return false;
        }
        token = pila.popFirst();
        if (!helper.analizarTipo(token, TokenType.IDENTIFICADOR)) {
            this.tokensError.addError(token, TokenType.IDENTIFICADOR);
            return false;
        }
        token = pila.popFirst();
        if (!helper.analizarValor(token, PalabraReservada.ON.name())) {
            this.tokensError.addError(token, PalabraReservada.ON.name());
            return false;
        }
        token = pila.popFirst();
        if (!helper.analizarTipo(token, TokenType.IDENTIFICADOR)) {
            this.tokensError.addError(token, TokenType.IDENTIFICADOR);
            return false;
        }
        token = pila.getFirst();
        if (helper.analizarValor(token, ".")) {
            pila.popFirst();
            token = pila.popFirst();
            if (!helper.analizarTipo(token, TokenType.IDENTIFICADOR)) {
                this.tokensError.addError(token, TokenType.IDENTIFICADOR);
                return false;
            }
        }
        token = pila.popFirst();
        if (!helper.analizarValor(token, "=")) {
            this.tokensError.addError(token, "=");
            return false;
        }
        token = pila.popFirst();
        if (!helper.analizarTipo(token, TokenType.IDENTIFICADOR)) {
            this.tokensError.addError(token, TokenType.IDENTIFICADOR);
            return false;
        }
        token = pila.popFirst();
        if (!helper.analizarValor(token, ".")) {
            this.tokensError.addError(token, ".");
            return false;
        }
        token = pila.popFirst();
        if (!helper.analizarTipo(token, TokenType.IDENTIFICADOR)) {
            this.tokensError.addError(token, TokenType.IDENTIFICADOR);
            return false;
        }
        return true;
    }

    private boolean analizarWhere(Pila pila) {
        Token token = pila.popFirst();
        if (helper.analizarTipo(token, TokenType.IDENTIFICADOR)) {
            token = pila.getFirst();
            if (helper.analizarValor(token, ".")) {
                pila.popFirst();
                token = pila.popFirst();
                if (!helper.analizarTipo(token, TokenType.IDENTIFICADOR)) {
                    this.tokensError.addError(token, TokenType.IDENTIFICADOR);
                    return false;
                }
            }
        } else if (isDato(token)) {
        } else {
            this.tokensError.addError(token, "<identificador> o [DATO]");
            return false;
        }

        token = pila.popFirst();
        if (!helper.analizarValor(token, new String[]{">", "<", "<=", ">=", "="})) {
            this.tokensError.addError(token, new String[]{">", "<", "<=", ">=", "="});
            return false;
        }
        token = pila.popFirst();
        if (helper.analizarTipo(token, TokenType.IDENTIFICADOR)) {
            token = pila.getFirst();
            if (helper.analizarValor(token, ".")) {
                pila.popFirst();
                token = pila.popFirst();
                if (!helper.analizarTipo(token, TokenType.IDENTIFICADOR)) {
                    this.tokensError.addError(token, TokenType.IDENTIFICADOR);
                    return false;
                }
            }
        } else if (isDato(token)) {
        } else {
            this.tokensError.addError(token, "<identificador> o [DATO]");
            return false;
        }

        token = pila.getFirst();
        if (helper.analizarValor(token, Logico.AND.name())) {
            pila.popFirst();
            return analizarWhere(pila);
        } else {
            return true;
        }
    }

    public void printInserts() {
        if (inserts.isEmpty()) {
            return;
        }
        for (Insert insert : inserts) {
            System.out.println(insert.toString());
        }
    }
}
