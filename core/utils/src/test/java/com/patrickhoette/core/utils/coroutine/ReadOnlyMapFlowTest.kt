package com.patrickhoette.core.utils.coroutine

import com.patrickhoette.test.assertFalse
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class ReadOnlyMapFlowTest {

    @MockK
    private lateinit var mutableMapFlow: MutableMapFlow<String, String>

    @InjectMockKs
    private lateinit var readOnlyMapFlow: ReadOnlyMapFlow<String, String>

    @Test
    @Suppress("USELESS_IS_CHECK") // Yeh that is exactly what we are testing
    fun `Read only map should not be up-castable`() = runTest {
        // When -> Then
        (readOnlyMapFlow is MutableMapFlow<*, *>).assertFalse()
    }
}
