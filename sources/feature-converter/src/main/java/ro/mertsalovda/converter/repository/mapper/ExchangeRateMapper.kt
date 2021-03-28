package ro.mertsalovda.converter.repository.mapper

import ru.mertsalovda.core_api.database.entity.ExchangeRate
import ru.mertsalovda.core_api.dto.ExchangeRateDto
import ru.mertsalovda.core_api.mapper.BaseMapper
import javax.inject.Inject

/**
 * Преобразователь типов курса валют.
 * Entity - [ExchangeRate]
 * Model - [ExchangeRateDto]
 */
open class ExchangeRateMapper @Inject constructor() : BaseMapper<ExchangeRate, ExchangeRateDto>() {

    override fun reverseMap(model: ExchangeRateDto?): ExchangeRate? =
        model?.let {
            ExchangeRate(
                base = model.base,
                rates = model.rates,
                date = model.date
                )
        }

    override fun map(entity: ExchangeRate?): ExchangeRateDto? =
        entity?.let {
            ExchangeRateDto(
                base = entity.base,
                rates = entity.rates,
                date = entity.date
            )
        }
}