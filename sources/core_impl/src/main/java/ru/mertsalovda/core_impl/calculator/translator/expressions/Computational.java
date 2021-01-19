package ru.mertsalovda.core_impl.calculator.translator.expressions;

/**
 * Вычисляемое выражение
 */
public interface Computational extends Expression {
    /**
     * Устанавливает массив параметров parametres
     * для вычисляемого выражения
     *
     * @param parametres массив параметров
     */
    void setParametres(Double[] parametres);

    /**
     * Возвращает количество параметров, необходимое
     * для рассчета данного выражения
     *
     * @return челочисленное количество параметров
     */
    int getParametresCount();

    /**
     * Возвращает приоритет операции от 1 до N,
     * где N - максимальный приортиет
     *
     * @return целочисленное значение приоритета операции
     */
    int getPriority();
}
