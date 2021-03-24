package ru.mertsalovda.core_impl.calculator.translator.expressions;

/**
 * Класс, описывающий операцию математического выражения
 */
public class Operation implements Computational {
    enum OperationType {
        NONE, PLUS, MINUS, MULTIPLICATION, DIVISION, POWER
    }

    private OperationType type = OperationType.NONE;
    private Double[] parametres = null;

    /**
     * @param literal литерал данной операции
     */
    public Operation(String literal) {
        type = ExpressionsSet.getOperationTypeByLiteral(literal);
    }

    @Override
    public Type getType() {
        return Type.OPERATION;
    }

    @Override
    public Double getValue() {
        if (parametres != null) {
            return calculateResult();
        } else {
            System.err.println("Параметры для операции не были установлены до расчета");
            return 0.0;
        }
    }

    @Override
    public void setParametres(Double[] parametres) {
        if (isCorrectParametresCount(parametres.length)) {
            this.parametres = parametres;
        }
    }

    @Override
    public int getParametresCount() {
        return 2;
    }

    @Override
    public String toString() {
        return type.toString();
    }

    @Override
    public int getPriority() {
        if (type == OperationType.PLUS || type == OperationType.MINUS) {
            return 1;
        } else if (type == OperationType.DIVISION || type == OperationType.MULTIPLICATION || type == OperationType.POWER) {
            return 2;
        } else return 0;
    }

    /**
     * Проверяет корректное ли количество параметров count
     * передано для данной операции
     *
     * @return true, если количество параметров коректно.
     * В противном случае возвращает false
     */
    private boolean isCorrectParametresCount(int count) {
        return count == 2; // Все операции бинарные
    }

    /**
     * Рассчитывает результат выражение
     *
     * @return результат
     */
    private double calculateResult() {
        switch (type) {
            case PLUS:
                return parametres[0] + parametres[1];
            case MINUS:
                return parametres[1] - parametres[0];
            case MULTIPLICATION:
                return parametres[0] * parametres[1];
            case DIVISION:
                return parametres[1] / parametres[0];
            case POWER:
                return StrictMath.pow(parametres[1], parametres[0]);
            default:
                System.err.println("Ошибка при вычислении результата выражения");
                return 0;
        }
    }
}
