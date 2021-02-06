package ru.mertsalovda.core_impl.calculator.translator.expressions;

/**
 * Класс, описывающий функцию математического выражения
 */
public class Function implements Computational {
    enum FunctionType {NONE, SIN, COS, TAN, COTAN, POW, SQRT}

    private FunctionType type = FunctionType.NONE;
    private Double[] parametres = null;

    public Function(String literal) {
        type = ExpressionsSet.getFunctionTypeByLiteral(literal);
    }

    @Override
    public void setParametres(Double[] parametres) {
        if (isCorrectParametresCount(parametres.length)) {
            this.parametres = parametres;
        }
    }

    @Override
    public int getParametresCount() {
        if (type == FunctionType.SIN ||
                type == FunctionType.COS ||
                type == FunctionType.TAN ||
                type == FunctionType.COTAN ||
                type == FunctionType.SQRT) {
            return 1;
        } else if (type == FunctionType.POW)
            return 2;

        System.err.println("Запрос параметров неподдерживаемой функции");
        return 0;
    }

    @Override
    public int getPriority() {
        return Integer.MAX_VALUE; // У функций максимальный приоритет
    }

    @Override
    public Type getType() {
        return Type.FUNCTION;
    }

    @Override
    public Double getValue() {
        if (parametres != null)
            return calculateResult();
        else {
            System.err.println("Параметры функции не были установлены до расчета");
            return 0.0;
        }
    }

    @Override
    public String toString() {
        return type.toString();
    }

    /**
     * Проверяет корректное ли количество параметров count
     * передано для данной операции
     *
     * @return true, если количество параметров коректно.
     * В противном случае возвращает false
     */
    private boolean isCorrectParametresCount(int count) {
        if (type == FunctionType.SIN ||
                type == FunctionType.COS ||
                type == FunctionType.TAN ||
                type == FunctionType.COTAN ||
                type == FunctionType.SQRT) {
            return count == 1; // 1 параметр
        } else if (type == FunctionType.POW) {
            return count == 2;
        }

        return false;
    }

    /**
     * Рассчитывает результат выражение
     *
     * @return результат
     */
    private double calculateResult() {
        switch (type) {
            case SIN:
                return StrictMath.sin(parametres[0]);
            case COS:
                return StrictMath.cos(parametres[0]);
            case TAN:
                return StrictMath.tan(parametres[0]);
            case COTAN:
                return 1 / StrictMath.tan(parametres[0]);
            case POW:
                return StrictMath.pow(parametres[1], parametres[0]);
            case SQRT:
                return StrictMath.sqrt(parametres[0]);
        }

        return 0.0;
    }

}
