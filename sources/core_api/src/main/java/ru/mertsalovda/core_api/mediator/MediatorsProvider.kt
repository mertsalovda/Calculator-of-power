package ru.mertsalovda.core_api.mediator

interface MediatorsProvider {

    fun provideMainMediator(): MainMediator

    fun provideBasicMediator(): BasicMediator
}