import androidx.compose.animation.animateColorAsState
import androidx.compose.desktop.AppManager
import androidx.compose.desktop.AppWindow
import androidx.compose.desktop.Window
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.Notifier
import java.awt.image.BufferedImage

var clicked = false
val notifier = Notifier()
fun main() = Window {
    BasicsCodelabTheme(darkTheme = null) {
        var text by remember { mutableStateOf("Hello, World!") }
        var confirm by remember { mutableStateOf(false) }
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            TopAppBar(
                title = {
                    Text(text = "Page title", maxLines = 1)
                },
            )
            Button(
                modifier = Modifier.padding(8.dp),
                onClick = {
                    text = if (clicked) {
                        "Hello"
                    } else {
                        "Goodbuy"
                    }
                    clicked = !clicked
                    AppWindow(size = IntSize(250, 250), icon = getMyAppIcon(), resizable = false).also { window ->
                        window.keyboard.setShortcut(Key.Escape) {
                            window.close()
                        }
                    }.show {
                        Text(
                            "I'm popup! Click me to close",
                            style = typography.h4,
                            modifier = Modifier.padding(8.dp).clickable {
                                AppManager.focusedWindow?.close()
                            })
                    }
                }) {
                Text("Click me")
            }
            Spacer(Modifier.height(16.dp))
            Surface(color = Color.Yellow) {
                Text(text = "Hello $text!")
            }
            Spacer(Modifier.height(24.dp))
            Text(
                "A day wandering through the sandhills " +
                        "in Shark Fin Cove, and a few of the " +
                        "sights I saw",
                style = typography.h4,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text("Davenport, California", style = typography.h4)
            Divider(color = Color.Black)
            Text(text, style = typography.h6)
            Spacer(Modifier.height(24.dp))
            MyScreenContent(listOf("Android", "IOS"))
            PhotographerCard {
                confirm = it
            }
            LayoutsCodelab()
        }
    }
}

@Composable
fun LayoutsCodelab() {
    Scaffold { innerPadding ->
        BodyContent(Modifier.padding(innerPadding))
    }
}

@Composable
fun BodyContent(modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text(text = "Hi there!")
        Text(text = "Thanks for going through the Layouts codelab")
    }
}

private val DarkColors = darkColors(
    primary = Color.Gray,
    primaryVariant = Color.LightGray,
    secondary = Color.White,
)

private val LightColors = lightColors(
    primary = Color.Cyan,
    primaryVariant = Color.Green,
    secondary = Color.Blue
)

@Composable
fun BasicsCodelabTheme(
    darkTheme: Boolean? = false,
    content: @Composable () -> Unit
) {
    val colors = darkTheme?.let {
        if (darkTheme) {
            DarkColors
        } else {
            LightColors
        }
    } ?: MaterialTheme.colors
    MaterialTheme(
        colors = colors
    ) {
        content()
    }
}

fun getMyAppIcon(): BufferedImage {
    val size = 256
    val image = BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB)
    val graphics = image.createGraphics()
    graphics.color = java.awt.Color(125, 125, 125)
    graphics.fillOval(0, 0, size, size)
    graphics.dispose()
    return image
}

@Composable
fun PhotographerCard(onDialogClose: (b: Boolean) -> Unit) {
    val dialogState = remember { mutableStateOf(false) }
    val (showDialog, setShowDialog) = remember { mutableStateOf(false) }
    DialogDemo(showDialog) { show ->
        setShowDialog(false)
        onDialogClose.invoke(show)
    }
    if (dialogState.value) {
        Dialog(
            onDismissRequest = { dialogState.value = false }
        ) {
            Column {
                Text(text = "Location")
                Button(
                    onClick = {
                        notifier.notify("Hello", "World")
                        dialogState.value = false
                    }
                ) {
                    Text(text = "Close app")
                }
            }
        }
    }
    Row(
        modifier = Modifier
            .padding(8.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(MaterialTheme.colors.surface)
            .clickable(onClick = {
                dialogState.value = true
            })
            .padding(16.dp)
    ) {
        Surface(
            modifier = Modifier.size(50.dp),
            shape = CircleShape,
            color = MaterialTheme.colors.onSurface.copy(alpha = 0.2f)
        ) {
            Greeting("Shape")
        }
        Column {
            Text("Alfred Sisley", fontWeight = FontWeight.Bold)
            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                Text("3 minutes ago", style = typography.body2)
            }
        }
    }
}

@Composable
fun DialogDemo(showDialog: Boolean, setShowDialog: (Boolean) -> Unit) {
    if (showDialog) {
        AlertDialog(
            onDismissRequest = {
            },
            title = {
                Text("Title")
            },
            confirmButton = {
                Button(
                    onClick = {
                        // Change the state to close the dialog
                        setShowDialog(true)
                    },
                ) {
                    Text("Confirm")
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        // Change the state to close the dialog
                        setShowDialog(false)
                    },
                ) {
                    Text("Dismiss")
                }
            },
            text = {
                Text("This is a text on the dialog")
            },
            properties = DialogProperties(resizable = false)
        )
    }
}

@Composable
fun Greeting(name: String) {
    var isSelected by remember { mutableStateOf(false) }
    val backgroundColor by animateColorAsState(if (isSelected) Color.Red else Color.Transparent)
    Column(
        modifier = Modifier
            .background(color = backgroundColor)
            .clickable(onClick = { isSelected = !isSelected }),
        content = {
            Text(
                text = "Hello $name!",
                modifier = Modifier.padding(8.dp)
            )
            Divider(color = Color.Black)
        })
}

@Composable
fun NameList(names: List<String>, modifier: Modifier = Modifier) {
    LazyColumn(modifier = modifier) {
        items(items = names) { name ->
            Greeting(name = name)
        }
    }
}

@Composable
fun MyScreenContent(names: List<String> = List(1000) { "Hello Android #$it" }) {
    val counterState = remember { mutableStateOf(0) }

    Column(modifier = Modifier.wrapContentHeight()) {
        NameList(names)
        Counter(
            count = counterState.value,
            updateCount = { newCount ->
                counterState.value = newCount
                if (newCount >= 10) {
                    counterState.value = 0
                }
            }
        )
    }
}

@Composable
fun Counter(count: Int, updateCount: (Int) -> Unit) {
    Button(
        onClick = { updateCount(count + 1) },
        colors = ButtonDefaults.buttonColors(
            backgroundColor = if (count > 5) Color.Green else Color.White
        )
    ) {
        Text("I've been clicked $count times")
    }
}