package com.patrickhoette.core.data.config

import com.patrickhoette.core.domain.config.UserSettingRepository
import org.koin.core.annotation.Factory

@Factory
class PersistentUserSettingRepository(private val store: UserSettingStore) : UserSettingRepository by store
