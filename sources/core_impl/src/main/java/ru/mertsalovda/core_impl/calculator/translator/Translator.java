package ru.mertsalovda.core_impl.calculator.translator;


import java.util.ArrayList;
import java.util.Stack;

import ru.mertsalovda.core_impl.calculator.translator.expressions.Bracket;
import ru.mertsalovda.core_impl.calculator.translator.expressions.Computational;
import ru.mertsalovda.core_impl.calculator.translator.expressions.Expression;
import ru.mertsalovda.core_impl.calculator.translator.expressions.Operand;
import ru.mertsalovda.core_impl.calculator.translator.expressions.Operation;

/**
 * Отвечает за перевод выражения
 * из инфиксной формы записи в постфиксную
 */
public class Translator {
    private String inputExpression = "";
    private Stack<Expression> operators = new Stack<>();
    private ArrayList<Expression> outputExpression = new ArrayList<>();

    /**
     * @param expression Исходное выражение в инфиксной форме записи
     */
    public Translator(String expression) {
        inputExpression = expression;
    }

    /**
     * Переводит выражение из инфиксной формы записи в постфиксную
     *
     * @return выражение в постфиксной форме записи
     */
    public ArrayList<Expression> translateToPostfixNotation() {
        outputExpression = new ArrayList<>();

        Tokenizer tokenizer = new Tokenizer(inputExpression);
        ArrayList<Expression> tokens = tokenizer.getTokens();

        for (Expression token : tokens) processToken(token);

        while (!operators.empty())
            outputExpression.add(operators.pop());

        return outputExpression;
    }

    /**
     * В зависимости от значения токена,
     * добавляет его на стек или в выходное выражение
     *
     * @param token Обрабатываемый токен
     */
    private void processToken(Expression token) {
        Expression.Type tokenType = token.getType();

        if (tokenType == Expression.Type.OPERAND) {
            outputExpression.add(token);
        } else if (tokenType == Expression.Type.CONSTANT) {
            Double value = token.getValue();

            outputExpression.add(new Operand(value));
        } else if (tokenType == Expression.Type.OPERATION) {
            if (token instanceof Operation) {
                Operation oToken = (Operation) token;
                if (!operators.empty()) {
                    Computational topOperation;
                    Expression expression = operators.peek();
                    if (expression instanceof Computational) {
                        topOperation = (Computational) expression;
                        if (oToken.getPriority() < topOperation.getPriority())
                            outputExpression.add(operators.pop());
                    }
                }
            }
            operators.push(token);
        } else if (tokenType == Expression.Type.FUNCTION) {
            operators.push(token);
        } else if (tokenType == Expression.Type.BRACKET) {
            Bracket bracket = (Bracket) token;
            if (bracket.getBracketType() == Bracket.BracketType.OPEN)
                operators.push(token);
            else if (bracket.getBracketType() == Bracket.BracketType.CLOSE)
                processCloseBracketToken();
        }
    }

    /**
     * Обрабатывает токен закрывающейся скобки
     */
    private void processCloseBracketToken() {
        Expression top = operators.pop();
        while (!isOpenBracketToken(top)) {
            if (operators.empty()) {
                System.err.println("Входное выражение " + inputExpression + " некорректно");
                System.exit(1);
            }

            outputExpression.add(top);
            top = operators.pop();
        }
    }

    /**
     * Проверяет является ли переданный
     * токен открывающейся скобкой
     *
     * @param token проверяемый токен
     * @return true, если токен является скобкой.
     * В противном случае возвращает false;
     */
    private boolean isOpenBracketToken(Expression token) {
        if (token instanceof Bracket) {
            Bracket bracket = (Bracket) token;

            return bracket.getBracketType() == Bracket.BracketType.OPEN;
        }

        return false;
    }
}
