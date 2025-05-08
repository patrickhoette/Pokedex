package com.patrickhoette.core.presentation.mapper

import com.patrickhoette.core.presentation.model.NamedEnumUIModel
import com.patrickhoette.core.presentation.model.StringUIModel
import com.patrickhoette.core.presentation.text.NamedEnumTextProvider
import com.patrickhoette.pokedex.entity.generic.Type
import com.patrickhoette.pokedex.entity.move.MoveMethod
import com.patrickhoette.test.assertEquals
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource

@ExtendWith(MockKExtension::class)
class NamedEnumUIMapperTest {

    @MockK
    private lateinit var textProvider: NamedEnumTextProvider

    @InjectMockKs
    private lateinit var mapper: NamedEnumUIMapper

    @EnumSource(Type::class)
    @EnumSource(MoveMethod::class)
    @ParameterizedTest(name = "[{index}] Given enum is {0}, when mapping, then return properly mapped model")
    fun `Type mapping test`(
        type: Enum<*>,
    ) {
        // Given
        val name = StringUIModel.Raw("enum name text")
        every { textProvider.getName(type) } returns name

        // When
        val result = mapper.mapToUIModel(type)

        // Then
        result assertEquals NamedEnumUIModel(
            name = name,
            value = type,
        )
    }
}
