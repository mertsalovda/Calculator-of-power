package ru.mertsalovda.core_impl.calculator.translator;


import java.util.ArrayList;

import ru.mertsalovda.core_impl.calculator.translator.expressions.Bracket;
import ru.mertsalovda.core_impl.calculator.translator.expressions.Constant;
import ru.mertsalovda.core_impl.calculator.translator.expressions.Expression;
import ru.mertsalovda.core_impl.calculator.translator.expressions.ExpressionsSet;
import ru.mertsalovda.core_impl.calculator.translator.expressions.Function;
import ru.mertsalovda.core_impl.calculator.translator.expressions.Operand;
import ru.mertsalovda.core_impl.calculator.translator.expressions.Operation;

/**
 * Отвечает за разбиение входной строки на токены
 */
class Tokenizer {
    private enum State {NONE, UNDEFINED, NUMBER, WORD, BRACKET}

    private ArrayList<Expression> tokens = new ArrayList<>();
    private StringBuilder tokenName = new StringBuilder();

    private boolean wasThereRadixPoint = false; // Был ли уже десятичный разделитель
    private boolean isParametr = false; // Является ли данное выражение параметром функции
    private State state = State.NONE;

    private String inputExpression;

    Tokenizer(String expr) {
        inputExpression = expr;
    }

    /**
     * Разбивает входную строку на токены(операнды, операции, функции, открывающиеся и закрывающиеся скобки)
     *
     * @return списочный массив токенов данного выражения
     */
    ArrayList<Expression> getTokens() {
        for (int i = 0; i < inputExpression.length(); ++i) {
            Character c = inputExpression.charAt(i);

            if (switchState(c))
                tokenName.append(c);
        }

        addToken(); // Добавление последнего слова

        return tokens;
    }

    /**
     * Меняет состояние в зависимости от переданного символа
     *
     * @param c символ входной строки
     * @return true, если данный символ нужно добавить к
     * токену. false, если нет.
     */
    private boolean switchState(Character c) {
        if (Character.isDigit(c)) {
            processDigit();
            return true;
        } else if (c.equals('.')) {
            if (state == State.NUMBER && !wasThereRadixPoint) {
                wasThereRadixPoint = true;
                return true;
            } else {
                System.err.println("Лишняя точка");
                return false;
            }
        } else if (Character.isLetter(c)) {
            if (state != State.WORD) {
                addToken();
                state = State.WORD;
            }
            return true;
        } else if (c.equals(',')) {
            if (isParametr) {
                addToken();
            } else System.err.println("Лишняя запятая");

            return false;
        } else if (c.equals('(') || c.equals(')')) {
            processBracket(c);
            return true;
        } else if (c.equals(' ')) {
            addToken();
            state = State.NONE;
            return false;
        } else {
            if (state != State.UNDEFINED) {
                addToken();
                state = State.UNDEFINED;
            }
            return true;

        }
    }

    /**
     * Обрабатывает изменение состояния, если
     * встречено число
     */
    private void processDigit() {
        if (state != State.NUMBER) {
            addToken();
            state = State.NUMBER;
        }
    }

    /**
     * Обрабатывает изменение состояния, если
     * встречена скобка
     *
     * @param c литерал скобки
     */
    private void processBracket(Character c) {
        Bracket.BracketType bracketType = ExpressionsSet.getBracketTypeByLiteral(c);

        if (bracketType == Bracket.BracketType.OPEN && state == State.WORD) {
            String token = tokenName.toString();
            if (ExpressionsSet.getExpressionType(token) == ExpressionsSet.ExpressionType.FUNCTION) {
                addToken();
                isParametr = true;
            } else {
                System.err.println("Скобка после константы");
            }
        } else {
            addToken();

            if (isParametr && bracketType == Bracket.BracketType.CLOSE)
                isParametr = false;
        }

        state = State.BRACKET;
    }

    /**
     * Добавляет токен в итоговое выражение
     */
    private void addToken() {
        if (tokenName.toString().isEmpty())
            return;

        String value = tokenName.toString();
        tokenName = new StringBuilder();
        ExpressionsSet.ExpressionType type = ExpressionsSet.getExpressionType(value);

        switch (state) {
            case NUMBER:
                tokens.add(new Operand(value));
                break;
            case WORD:
                if (type == ExpressionsSet.ExpressionType.CONSTANT)
                    tokens.add(new Constant(value));
                else if (type == ExpressionsSet.ExpressionType.FUNCTION)
                    tokens.add(new Function(value));
                else System.err.println("Неизвестное слово");
                break;
            case BRACKET:
                tokens.add(new Bracket(value));
                break;
            case UNDEFINED:
                if (type == ExpressionsSet.ExpressionType.OPERATION)
                    tokens.add(new Operation(value));
                else
                    System.err.println("Неизвестное выражение");
                break;
        }
    }
}
