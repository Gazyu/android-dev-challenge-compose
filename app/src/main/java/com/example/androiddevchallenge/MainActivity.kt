/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.androiddevchallenge

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import androidx.navigation.compose.navigate
import androidx.navigation.compose.rememberNavController
import com.example.androiddevchallenge.ui.theme.MyTheme

data class Animal(
    val id: Int,
    val name: String,
    val gender: String,
    val color: String,
    val sentence: String,
    val resName: String
)

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val animals = listOf(
            Animal(1, "neko", "Male", "White", "Kashikoi", "dog01"),
            Animal(1, "neko", "Male", "White", "Kashikoi", "dog02")
        )

        setContent {
            MyTheme {
                App(animals)
            }
        }
    }
}

@Composable
fun App(animals: List<Animal>) {

    Surface(Modifier.fillMaxSize()) {
        val navController = rememberNavController()
        NavHost(navController, startDestination = "list") {
            composable("list") {
                Scaffold(
                    topBar = {
                        TopAppBar(title = { Text("子犬の一覧") })
                    },
                    content = {
                        AnimalList(animals) {
                            navController.navigate("animals/" + it.id)
                        }
                    }
                )
            }
            composable(
                route = "animals/{animalId}",
                arguments = listOf(navArgument("animalId") { type = NavType.IntType })
            ) { backStackEntry ->
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = { Text("子犬の詳細") },
                            navigationIcon = {
                                IconButton(
                                    onClick = {
                                        navController.popBackStack()
                                    }
                                ) {
                                    Icon(Icons.Filled.ArrowBack, "back")
                                }
                            }
                        )
                    },
                    content = {
                        val animalId = backStackEntry.arguments?.getInt("animalId")!!
                        val animal = animals.first { it.id == animalId }
                        AnimalCard(animal)
                    }
                )
            }
        }
    }
}

@Composable
fun AnimalList(
    animals: List<Animal>,
    onSelected: (Animal) -> Unit
) {
    Surface(Modifier.fillMaxSize()) {
        LazyColumn() {
            items(animals) { animal ->
                AnimalItem(animal, onSelected)
            }
        }
    }
}

@Composable
fun AnimalItem(animal: Animal, onSelected: (Animal) -> Unit) {
    Row(
        modifier = Modifier
            .clickable {
                onSelected.invoke(animal)
            }
            .padding(16.dp)
            .height(64.dp)

    ) {
        val picId: Int = LocalContext.current.getResources()
            .getIdentifier(animal.resName, "drawable", LocalContext.current.packageName)
        Image(painter = painterResource(id = picId), contentDescription = null)
        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(start = 16.dp)
        ) {
            Text(text = "name:" + animal.name)
            Text(text = "gender:" + animal.gender)
            Text(text = "color:" + animal.color)
        }
    }
}

@Composable
fun AnimalCard(animal: Animal) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        val picId: Int = LocalContext.current.getResources()
            .getIdentifier(animal.resName, "drawable", LocalContext.current.packageName)
        Image(painter = painterResource(id = picId), contentDescription = null)
        Text(text = "name:" + animal.name)
        Text(text = "gender:" + animal.gender)
        Text(text = "color:" + animal.color)
        Text(text = "sentence:" + animal.sentence)
    }
}

@Preview("Light Theme", widthDp = 360, heightDp = 640)
@Composable
fun LightPreview() {
    val animals = listOf(
        Animal(1, "neko", "Male", "White", "Kashikoi", "dog01"),
        Animal(1, "neko", "Male", "White", "Kashikoi", "dog02")
    )

    MyTheme {
        Scaffold(
            topBar = {
                TopAppBar(title = { Text("TopAppBar") })
            },
            content = { App(animals) }
        )
    }
}

@Preview("Dark Theme", widthDp = 360, heightDp = 640)
@Composable
fun DarkPreview() {
    val animals = listOf(
        Animal(1, "neko", "Male", "White", "Kashikoi", "dog01"),
        Animal(1, "neko", "Male", "White", "Kashikoi", "dog02")
    )

    MyTheme(darkTheme = true) {
        Scaffold(
            topBar = {
                TopAppBar(title = { Text("TopAppBar") })
            },
            content = { App(animals) }
        )
    }
}
