package ru.mertsalovda.core_impl.calculator.translator.expressions;

import java.util.HashMap;

/**
 * Класс, описывающий множество всех выражений, поддерживаемых программой
 */
public class ExpressionsSet {
    public enum ExpressionType {NONE, CONSTANT, OPERATION, FUNCTION, BRACKET}

    private static final HashMap<String, Constant.ConstantType> constants = new HashMap<String, Constant.ConstantType>() {{
        put("e", Constant.ConstantType.E);
        put("pi", Constant.ConstantType.PI);
    }};

    private static final HashMap<String, Operation.OperationType> operations = new HashMap<String, Operation.OperationType>() {{
        put("+", Operation.OperationType.PLUS);
        put("-", Operation.OperationType.MINUS);
        put("*", Operation.OperationType.MULTIPLICATION);
        put("/", Operation.OperationType.DIVISION);
        put("^", Operation.OperationType.POWER);
    }};

    private static final HashMap<String, Function.FunctionType> functions = new HashMap<String, Function.FunctionType>() {{
        put("sin", Function.FunctionType.SIN);
        put("cos", Function.FunctionType.COS);
        put("tan", Function.FunctionType.TAN);
        put("tg", Function.FunctionType.TAN);
        put("cotan", Function.FunctionType.COTAN);
        put("cot", Function.FunctionType.COTAN);
        put("ctg", Function.FunctionType.COTAN);
        put("pow", Function.FunctionType.POW);
        put("sqrt", Function.FunctionType.SQRT);
    }};

    private static final HashMap<Character, Bracket.BracketType> brackets = new HashMap<Character, Bracket.BracketType>() {{
        put('(', Bracket.BracketType.OPEN);
        put(')', Bracket.BracketType.CLOSE);
    }};

    /**
     * Возвращает тип операции за счет ее литерала
     *
     * @param literal литерал
     * @return тип операции
     */
    static public Operation.OperationType getOperationTypeByLiteral(String literal) {
        if (operations.containsKey(literal))
            return operations.get(literal);
        else
            System.err.println("Переданная операция не подерживается " + literal);

        return Operation.OperationType.NONE;
    }

    /**
     * Возвращает тип функция за счет ее литерала
     *
     * @param literal литерал
     * @return тип функции
     */
    static public Function.FunctionType getFunctionTypeByLiteral(String literal) {
        if (functions.containsKey(literal))
            return functions.get(literal);
        else
            System.err.println("Переданная функция не подерживается " + literal);

        return Function.FunctionType.NONE;
    }

    /**
     * Возвращает тип константы за счет ее литерала
     *
     * @param literal литерал
     * @return тип константы
     */
    static public Constant.ConstantType getConstantTypeByLiteral(String literal) {
        if (constants.containsKey(literal))
            return constants.get(literal);
        else
            System.err.println("Переданная константа не подерживается " + literal);

        return Constant.ConstantType.NONE;
    }

    /**
     * Возвращает тип скобки за счет ее литерала
     *
     * @param literal скобка
     * @return тип скобки
     */
    static public Bracket.BracketType getBracketTypeByLiteral(Character literal) {
        if (brackets.containsKey(literal))
            return brackets.get(literal);
        else
            System.err.println("Переданная скобка не подерживается " + literal);

        return Bracket.BracketType.NONE;
    }

    /**
     * Возвращает тип выражения по литералу literal
     *
     * @param literal литерал, проверяемого выражения
     * @return если выражение поддерживается, то возвращает его тип.
     * В противном случае возвращает NONE;
     */
    static public ExpressionType getExpressionType(String literal) {
        if (operations.containsKey(literal))
            return ExpressionType.OPERATION;
        else if (functions.containsKey(literal))
            return ExpressionType.FUNCTION;
        else if (constants.containsKey(literal))
            return ExpressionType.CONSTANT;
        else if (brackets.containsKey(literal))
            return ExpressionType.BRACKET;

        return ExpressionType.NONE;
    }
}
