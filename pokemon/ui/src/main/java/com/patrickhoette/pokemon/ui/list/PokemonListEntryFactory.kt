package com.patrickhoette.pokemon.ui.list

import androidx.annotation.IntRange
import com.patrickhoette.pokedex.entity.generic.Type.*
import com.patrickhoette.pokemon.presentation.list.model.PokemonListEntryUIModel
import com.patrickhoette.pokemon.presentation.model.PokemonTypeUIModel.DualType
import com.patrickhoette.pokemon.presentation.model.PokemonTypeUIModel.MonoType

object PokemonListEntryFactory {

    private val PokemonList = listOf(
        PokemonListEntryUIModel.Entry(
            id = 1,
            name = "bulbasaur",
            imageUrl = createImageUrl(1),
            type = DualType(Grass, Poison),
        ),
        PokemonListEntryUIModel.Entry(
            id = 2,
            name = "ivysaur",
            imageUrl = createImageUrl(2),
            type = DualType(Grass, Poison),
        ),
        PokemonListEntryUIModel.Entry(
            id = 3,
            name = "venusaur",
            imageUrl = createImageUrl(3),
            type = DualType(Grass, Poison),
        ),
        PokemonListEntryUIModel.Entry(
            id = 4,
            name = "charmander",
            imageUrl = createImageUrl(4),
            type = MonoType(Fire),
        ),
        PokemonListEntryUIModel.Entry(
            id = 5,
            name = "charmeleon",
            imageUrl = createImageUrl(5),
            type = MonoType(Fire),
        ),
        PokemonListEntryUIModel.Entry(
            id = 6,
            name = "charizard",
            imageUrl = createImageUrl(6),
            type = DualType(Fire, Flying),
        ),
        PokemonListEntryUIModel.Entry(
            id = 7,
            name = "squirtle",
            imageUrl = createImageUrl(7),
            type = MonoType(Water),
        ),
        PokemonListEntryUIModel.Entry(
            id = 8,
            name = "wartortle",
            imageUrl = createImageUrl(8),
            type = MonoType(Water),
        ),
        PokemonListEntryUIModel.Entry(
            id = 9,
            name = "blastoise",
            imageUrl = createImageUrl(9),
            type = MonoType(Water),
        ),
        PokemonListEntryUIModel.Entry(
            id = 10,
            name = "caterpie",
            imageUrl = createImageUrl(10),
            type = MonoType(Bug),
        ),
        PokemonListEntryUIModel.Entry(
            id = 11,
            name = "metapod",
            imageUrl = createImageUrl(11),
            type = MonoType(Bug),
        ),
        PokemonListEntryUIModel.Entry(
            id = 12,
            name = "butterfree",
            imageUrl = createImageUrl(12),
            type = DualType(Bug, Flying),
        ),
        PokemonListEntryUIModel.Entry(
            id = 13,
            name = "weedle",
            imageUrl = createImageUrl(13),
            type = DualType(Bug, Poison),
        ),
        PokemonListEntryUIModel.Entry(
            id = 14,
            name = "kakuna",
            imageUrl = createImageUrl(14),
            type = DualType(Bug, Poison),
        ),
        PokemonListEntryUIModel.Entry(
            id = 15,
            name = "beedrill",
            imageUrl = createImageUrl(15),
            type = DualType(Bug, Poison),
        ),
        PokemonListEntryUIModel.Entry(
            id = 16,
            name = "pidgey",
            imageUrl = createImageUrl(16),
            type = MonoType(Flying),
        ),
        PokemonListEntryUIModel.Entry(
            id = 17,
            name = "pidgeotto",
            imageUrl = createImageUrl(17),
            type = MonoType(Flying),
        ),
        PokemonListEntryUIModel.Entry(
            id = 18,
            name = "pidgeot",
            imageUrl = createImageUrl(18),
            type = MonoType(Flying),
        ),
        PokemonListEntryUIModel.Entry(
            id = 19,
            name = "rattata",
            imageUrl = createImageUrl(19),
            type = MonoType(Normal),
        ),
        PokemonListEntryUIModel.Entry(
            id = 20,
            name = "raticate",
            imageUrl = createImageUrl(20),
            type = MonoType(Normal),
        ),
        PokemonListEntryUIModel.Entry(
            id = 21,
            name = "spearow",
            imageUrl = createImageUrl(21),
            type = MonoType(Flying),
        ),
        PokemonListEntryUIModel.Entry(
            id = 22,
            name = "fearow",
            imageUrl = createImageUrl(22),
            type = MonoType(Flying),
        ),
        PokemonListEntryUIModel.Entry(
            id = 23,
            name = "ekans",
            imageUrl = createImageUrl(23),
            type = MonoType(Poison),
        ),
        PokemonListEntryUIModel.Entry(
            id = 24,
            name = "arbok",
            imageUrl = createImageUrl(24),
            type = MonoType(Poison),
        ),
        PokemonListEntryUIModel.Entry(
            id = 25,
            name = "pikachu",
            imageUrl = createImageUrl(25),
            type = MonoType(Electric),
        ),
        PokemonListEntryUIModel.Entry(
            id = 26,
            name = "raichu",
            imageUrl = createImageUrl(26),
            type = MonoType(Electric),
        ),
        PokemonListEntryUIModel.Entry(
            id = 27,
            name = "sandshrew",
            imageUrl = createImageUrl(27),
            type = MonoType(Ground),
        ),
        PokemonListEntryUIModel.Entry(
            id = 28,
            name = "sandslash",
            imageUrl = createImageUrl(28),
            type = MonoType(Ground),
        ),
        PokemonListEntryUIModel.Entry(
            id = 29,
            name = "nidoran-f",
            imageUrl = createImageUrl(29),
            type = MonoType(Poison),
        ),
        PokemonListEntryUIModel.Entry(
            id = 30,
            name = "nidorina",
            imageUrl = createImageUrl(30),
            type = MonoType(Poison),
        ),
    )

    private fun createImageUrl(id: Int) =
        "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/$id.png"

    fun createList(@IntRange(from = 1, to = 30) amount: Int) = PokemonList.take(amount)
}
