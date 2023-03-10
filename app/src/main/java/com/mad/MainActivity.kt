package com.mad

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.mad.models.Movie
import com.mad.models.getMovies
import com.mad.ui.theme.MovieTheme
import kotlin.random.Random
import kotlin.system.exitProcess

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MovieTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    CreateTopAppBar()
                }
            }
        }
    }
}

@Composable
fun MovieRow(movie: Movie) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(all = 10.dp)
            .clip(shape = RoundedCornerShape(size = 10.dp))
    ) {
        Box {
            Row {
                val randomImage by rememberSaveable {
                    mutableStateOf(Random.nextInt(from = 0, until = movie.images.size - 1))
                }
                AsyncImage(
                    model = movie.images[randomImage],
                    contentDescription = movie.title
                )
            }
            Box {
                var heartClicked by rememberSaveable {
                    mutableStateOf(false)
                }
                IconButton(
                    modifier = Modifier.offset(x = 340.dp),
                    enabled = true,
                    onClick = ({
                        if (!heartClicked) {
                            addToFavouriteMovies(movie)
                        } else {
                            removeFromFavouriteMovies(movie)
                        }
                        heartClicked = !heartClicked
                    })
                ) {
                    Icon(
                        imageVector = if (!heartClicked) {
                            Icons.Default.FavoriteBorder
                        } else {
                            Icons.Default.Favorite
                        },
                        contentDescription = "Favorite",
                        tint = Color.Red
                    )
                }
            }
        }
        var movieExtended by rememberSaveable {
            mutableStateOf(false)
        }
        Row(
            modifier = Modifier
                .background(color = Color.LightGray)
                .fillMaxWidth(fraction = 1f)
                .padding(start = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = movie.title,
                fontSize = 30.sp
            )
            IconButton(
                enabled = true,
                onClick = ({
                    movieExtended = !movieExtended
                })
            ) {
                Icon(
                    imageVector = if (!movieExtended) {
                        Icons.Default.KeyboardArrowUp
                    } else {
                        Icons.Default.KeyboardArrowDown
                    },
                    contentDescription = if (!movieExtended) {
                        "KeyboardArrowUp"
                    } else {
                        "KeyboardArrowDown"
                    },
                    tint = Color.DarkGray
                )
            }
        }
        AnimatedVisibility(visible = movieExtended) {
            Row(
                modifier = Modifier
                    .background(color = Color.LightGray)
                    .padding(start = 10.dp)
            ) {
                Text(
                    text = "Actors: ${movie.actors}\nGenre: ${movie.genre}\n" +
                            "Year: ${movie.year}\nDirector: ${movie.director}\n" +
                            "Rating: ${movie.rating}",
                    fontSize = 22.sp
                )
            }
        }
    }
}

@Composable
fun CreateTopAppBar() {
    Scaffold(
        topBar = {
            var menuButtonClicked by rememberSaveable {
                mutableStateOf(false)
            }
            var showFavouriteMovies by rememberSaveable {
                mutableStateOf(false)
            }
            Box(modifier = Modifier.background(backgroundColor)) {
                TopAppBar(
                    title = {
                        Text(
                            text = "MovieApp",
                            color = Color.White
                        )
                    },
                    backgroundColor = Color.Black,
                    modifier = Modifier
                        .clip(
                            RoundedCornerShape(
                                bottomStart = 10.dp,
                                bottomEnd = 10.dp
                            )
                        ),
                    navigationIcon = {
                        IconButton(onClick = { menuButtonClicked = !menuButtonClicked }) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "MenuButton",
                                tint = Color.White
                            )
                            DropdownMenu(
                                expanded = menuButtonClicked,
                                onDismissRequest = { menuButtonClicked = false }
                            ) {
                                DropdownMenuItem(onClick = { exitProcess(0) }) {
                                    Text(text = "Quit")
                                    Icon(
                                        imageVector = Icons.Default.ExitToApp,
                                        "Quit"
                                    )
                                }
                            }
                        }
                    }, actions = {
                        IconButton(onClick = { showFavouriteMovies = !showFavouriteMovies }) {
                            Icon(
                                imageVector = Icons.Default.Favorite,
                                contentDescription = "FavouriteMovies",
                                tint = Color.White
                            )
                            DropdownMenu(
                                expanded = showFavouriteMovies,
                                onDismissRequest = { showFavouriteMovies = false }
                            ) {
                                if (favourites.size == 0) {
                                    DropdownMenuItem(
                                        enabled = false,
                                        onClick = { /* STUB */ }
                                    ) {
                                        Text(text = "No Favourites!")
                                    }
                                }
                                favourites.forEach(action = { movie ->
                                    DropdownMenuItem(onClick = { /* STUB */ }) {
                                        Text(text = movie.title)
                                    }
                                })
                            }
                        }
                    }
                )
            }
        }, content = { paddingValues ->
            Box(modifier = Modifier.background(backgroundColor)) {
                LazyColumn(
                    contentPadding = paddingValues,
                    content = {
                        items(getMovies()) { movie ->
                            MovieRow(movie)
                        }
                    }
                )
            }
        }
    )
}

var backgroundColor = Color(red = 109, green = 39, blue = 121, alpha = 87)

var favourites: ArrayList<Movie> = ArrayList()

fun addToFavouriteMovies(movie: Movie) {
    favourites.add(movie)
}

fun removeFromFavouriteMovies(movie: Movie) {
    favourites.remove(movie)
}