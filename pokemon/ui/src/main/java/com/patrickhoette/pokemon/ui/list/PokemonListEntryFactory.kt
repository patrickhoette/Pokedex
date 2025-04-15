package com.patrickhoette.pokemon.ui.list

import androidx.annotation.IntRange
import com.patrickhoette.core.presentation.model.NamedEnumUIModel
import com.patrickhoette.core.presentation.model.StringUIModel
import com.patrickhoette.core.ui.R
import com.patrickhoette.pokedex.entity.generic.Type.*
import com.patrickhoette.pokemon.presentation.list.model.PokemonListEntryUIModel
import com.patrickhoette.pokemon.presentation.model.PokemonTypeUIModel.DualType
import com.patrickhoette.pokemon.presentation.model.PokemonTypeUIModel.MonoType

object PokemonListEntryFactory {

    private val PokemonList = listOf(
        PokemonListEntryUIModel.Entry(
            id = 1,
            name = StringUIModel.Raw("bulbasaur"),
            imageUrl = createImageUrl(1),
            type = DualType(
                primary = NamedEnumUIModel(
                    name = StringUIModel.Res(R.string.type_grass),
                    value = Grass,
                ),
                secondary = NamedEnumUIModel(
                    name = StringUIModel.Res(R.string.type_poison),
                    value = Poison,
                )
            ),
        ),
        PokemonListEntryUIModel.Entry(
            id = 2,
            name = StringUIModel.Raw("ivysaur"),
            imageUrl = createImageUrl(2),
            type = DualType(
                primary = NamedEnumUIModel(
                    name = StringUIModel.Res(R.string.type_grass),
                    value = Grass,
                ),
                secondary = NamedEnumUIModel(
                    name = StringUIModel.Res(R.string.type_poison),
                    value = Poison,
                )
            ),
        ),
        PokemonListEntryUIModel.Entry(
            id = 3,
            name = StringUIModel.Raw("venusaur"),
            imageUrl = createImageUrl(3),
            type = DualType(
                primary = NamedEnumUIModel(
                    name = StringUIModel.Res(R.string.type_grass),
                    value = Grass,
                ),
                secondary = NamedEnumUIModel(
                    name = StringUIModel.Res(R.string.type_poison),
                    value = Poison,
                )
            ),
        ),
        PokemonListEntryUIModel.Entry(
            id = 4,
            name = StringUIModel.Raw("charmander"),
            imageUrl = createImageUrl(4),
            type = MonoType(
                NamedEnumUIModel(
                    name = StringUIModel.Res(R.string.type_fire),
                    value = Fire,
                ),
            ),
        ),
        PokemonListEntryUIModel.Entry(
            id = 5,
            name = StringUIModel.Raw("charmeleon"),
            imageUrl = createImageUrl(5),
            type = MonoType(
                NamedEnumUIModel(
                    name = StringUIModel.Res(R.string.type_fire),
                    value = Fire,
                ),
            ),
        ),
        PokemonListEntryUIModel.Entry(
            id = 6,
            name = StringUIModel.Raw("charizard"),
            imageUrl = createImageUrl(6),
            type = DualType(
                primary = NamedEnumUIModel(
                    name = StringUIModel.Res(R.string.type_fire),
                    value = Fire,
                ),
                secondary = NamedEnumUIModel(
                    name = StringUIModel.Res(R.string.type_flying),
                    value = Flying,
                ),
            ),
        ),
        PokemonListEntryUIModel.Entry(
            id = 7,
            name = StringUIModel.Raw("squirtle"),
            imageUrl = createImageUrl(7),
            type = MonoType(
                NamedEnumUIModel(
                    name = StringUIModel.Res(R.string.type_water),
                    value = Water,
                ),
            ),
        ),
        PokemonListEntryUIModel.Entry(
            id = 8,
            name = StringUIModel.Raw("wartortle"),
            imageUrl = createImageUrl(8),
            type = MonoType(
                NamedEnumUIModel(
                    name = StringUIModel.Res(R.string.type_water),
                    value = Water,
                ),
            ),
        ),
        PokemonListEntryUIModel.Entry(
            id = 9,
            name = StringUIModel.Raw("blastoise"),
            imageUrl = createImageUrl(9),
            type = MonoType(
                NamedEnumUIModel(
                    name = StringUIModel.Res(R.string.type_water),
                    value = Water,
                ),
            ),
        ),
        PokemonListEntryUIModel.Entry(
            id = 10,
            name = StringUIModel.Raw("caterpie"),
            imageUrl = createImageUrl(10),
            type = MonoType(
                NamedEnumUIModel(
                    name = StringUIModel.Res(R.string.type_bug),
                    value = Bug,
                ),
            ),
        ),
        PokemonListEntryUIModel.Entry(
            id = 11,
            name = StringUIModel.Raw("metapod"),
            imageUrl = createImageUrl(11),
            type = MonoType(
                NamedEnumUIModel(
                    name = StringUIModel.Res(R.string.type_bug),
                    value = Bug,
                ),
            ),
        ),
        PokemonListEntryUIModel.Entry(
            id = 12,
            name = StringUIModel.Raw("butterfree"),
            imageUrl = createImageUrl(12),
            type = DualType(
                primary = NamedEnumUIModel(
                    name = StringUIModel.Res(R.string.type_bug),
                    value = Bug,
                ),
                secondary = NamedEnumUIModel(
                    name = StringUIModel.Res(R.string.type_flying),
                    value = Flying,
                ),
            ),
        ),
        PokemonListEntryUIModel.Entry(
            id = 13,
            name = StringUIModel.Raw("weedle"),
            imageUrl = createImageUrl(13),
            type = DualType(
                primary = NamedEnumUIModel(
                    name = StringUIModel.Res(R.string.type_bug),
                    value = Bug,
                ),
                secondary = NamedEnumUIModel(
                    name = StringUIModel.Res(R.string.type_poison),
                    value = Poison,
                ),
            ),
        ),
        PokemonListEntryUIModel.Entry(
            id = 14,
            name = StringUIModel.Raw("kakuna"),
            imageUrl = createImageUrl(14),
            type = DualType(
                primary = NamedEnumUIModel(
                    name = StringUIModel.Res(R.string.type_bug),
                    value = Bug,
                ),
                secondary = NamedEnumUIModel(
                    name = StringUIModel.Res(R.string.type_poison),
                    value = Poison,
                ),
            ),
        ),
        PokemonListEntryUIModel.Entry(
            id = 15,
            name = StringUIModel.Raw("beedrill"),
            imageUrl = createImageUrl(15),
            type = DualType(
                primary = NamedEnumUIModel(
                    name = StringUIModel.Res(R.string.type_bug),
                    value = Bug,
                ),
                secondary = NamedEnumUIModel(
                    name = StringUIModel.Res(R.string.type_poison),
                    value = Poison,
                ),
            ),
        ),
        PokemonListEntryUIModel.Entry(
            id = 16,
            name = StringUIModel.Raw("pidgey"),
            imageUrl = createImageUrl(16),
            type = MonoType(
                NamedEnumUIModel(
                    name = StringUIModel.Res(R.string.type_flying),
                    value = Flying,
                )
            ),
        ),
        PokemonListEntryUIModel.Entry(
            id = 17,
            name = StringUIModel.Raw("pidgeotto"),
            imageUrl = createImageUrl(17),
            type = MonoType(
                NamedEnumUIModel(
                    name = StringUIModel.Res(R.string.type_flying),
                    value = Flying,
                )
            ),
        ),
        PokemonListEntryUIModel.Entry(
            id = 18,
            name = StringUIModel.Raw("pidgeot"),
            imageUrl = createImageUrl(18),
            type = MonoType(
                NamedEnumUIModel(
                    name = StringUIModel.Res(R.string.type_flying),
                    value = Flying,
                )
            ),
        ),
        PokemonListEntryUIModel.Entry(
            id = 19,
            name = StringUIModel.Raw("rattata"),
            imageUrl = createImageUrl(19),
            type = MonoType(
                NamedEnumUIModel(
                    name = StringUIModel.Res(R.string.type_normal),
                    value = Normal,
                )
            ),
        ),
        PokemonListEntryUIModel.Entry(
            id = 20,
            name = StringUIModel.Raw("raticate"),
            imageUrl = createImageUrl(20),
            type = MonoType(
                NamedEnumUIModel(
                    name = StringUIModel.Res(R.string.type_normal),
                    value = Normal,
                )
            ),
        ),
        PokemonListEntryUIModel.Entry(
            id = 21,
            name = StringUIModel.Raw("spearow"),
            imageUrl = createImageUrl(21),
            type = MonoType(
                NamedEnumUIModel(
                    name = StringUIModel.Res(R.string.type_flying),
                    value = Flying,
                )
            ),
        ),
        PokemonListEntryUIModel.Entry(
            id = 22,
            name = StringUIModel.Raw("fearow"),
            imageUrl = createImageUrl(22),
            type = MonoType(
                NamedEnumUIModel(
                    name = StringUIModel.Res(R.string.type_flying),
                    value = Flying,
                )
            ),
        ),
        PokemonListEntryUIModel.Entry(
            id = 23,
            name = StringUIModel.Raw("ekans"),
            imageUrl = createImageUrl(23),
            type = MonoType(
                NamedEnumUIModel(
                    name = StringUIModel.Res(R.string.type_poison),
                    value = Poison,
                )
            ),
        ),
        PokemonListEntryUIModel.Entry(
            id = 24,
            name = StringUIModel.Raw("arbok"),
            imageUrl = createImageUrl(24),
            type = MonoType(
                NamedEnumUIModel(
                    name = StringUIModel.Res(R.string.type_poison),
                    value = Poison,
                )
            ),
        ),
        PokemonListEntryUIModel.Entry(
            id = 25,
            name = StringUIModel.Raw("pikachu"),
            imageUrl = createImageUrl(25),
            type = MonoType(
                NamedEnumUIModel(
                    name = StringUIModel.Res(R.string.type_electric),
                    value = Electric,
                )
            ),
        ),
        PokemonListEntryUIModel.Entry(
            id = 26,
            name = StringUIModel.Raw("raichu"),
            imageUrl = createImageUrl(26),
            type = MonoType(
                NamedEnumUIModel(
                    name = StringUIModel.Res(R.string.type_electric),
                    value = Electric,
                )
            ),
        ),
        PokemonListEntryUIModel.Entry(
            id = 27,
            name = StringUIModel.Raw("sandshrew"),
            imageUrl = createImageUrl(27),
            type = MonoType(
                NamedEnumUIModel(
                    name = StringUIModel.Res(R.string.type_ground),
                    value = Ground,
                )
            ),
        ),
        PokemonListEntryUIModel.Entry(
            id = 28,
            name = StringUIModel.Raw("sandslash"),
            imageUrl = createImageUrl(28),
            type = MonoType(
                NamedEnumUIModel(
                    name = StringUIModel.Res(R.string.type_ground),
                    value = Ground,
                )
            ),
        ),
        PokemonListEntryUIModel.Entry(
            id = 29,
            name = StringUIModel.Raw("nidoran-f"),
            imageUrl = createImageUrl(29),
            type = MonoType(
                NamedEnumUIModel(
                    name = StringUIModel.Res(R.string.type_poison),
                    value = Poison,
                )
            ),
        ),
        PokemonListEntryUIModel.Entry(
            id = 30,
            name = StringUIModel.Raw("nidorina"),
            imageUrl = createImageUrl(30),
            type = MonoType(
                NamedEnumUIModel(
                    name = StringUIModel.Res(R.string.type_poison),
                    value = Poison,
                )
            ),
        ),
    )

    private fun createImageUrl(id: Int) =
        "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/$id.png"

    fun createList(@IntRange(from = 1, to = 30) amount: Int) = PokemonList.take(amount)
}
