package com.android.example.plantmamaapp_v3.ui

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.android.example.plantmamaapp_v3.R
import com.android.example.plantmamaapp_v3.data.Photo2
import com.android.example.plantmamaapp_v3.data.Plant
import com.android.example.plantmamaapp_v3.data.photo2s
import com.android.example.plantmamaapp_v3.ui.theme.PLantMamaTheme

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import coil.compose.AsyncImage
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import com.android.example.plantmamaapp_v3.ui.SegmentedButtonsDefaults.ITEM_ANIMATION_MILLIS
import com.android.example.plantmamaapp_v3.ui.SegmentedButtonsDefaults.minimumHeight
import com.android.example.plantmamaapp_v3.ui.SegmentedButtonsDefaults.outlineThickness
import com.android.example.plantmamaapp_v3.ui.navigation.NavigationDestination

import kotlinx.coroutines.flow.StateFlow

import java.util.Date
import java.util.Locale

object PlantProfileDestination : NavigationDestination {
    override val route = "plant_profile"
    const val itemIdArg = "itemId"
    val routeWithArgs = "$route/{$itemIdArg}"
}

@Composable
fun PlantProfleMain(
    viewModel: PlantMamaMainScreenViewModel = viewModel(factory = AppViewModelProvider.Factory),
    navController: NavController,
    reminderListiewModel: ReminderListViewModel = viewModel(factory = AppViewModelProvider.Factory),
    photoViewModel: PhotoViewModel = viewModel(factory = AppViewModelProvider.Factory)
)
{
    val backStackEntry by navController.currentBackStackEntryAsState()


    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val state = rememberScrollState()
    var showAddReminderDialog by rememberSaveable { mutableStateOf(false) }
    var showEditPlantDialog by rememberSaveable { mutableStateOf(false) }

    //REMINDER APP
    //var selectedPlant by rememberSaveable { mutableStateOf(plants[0]) }
    //var showReminderDialog by rememberSaveable { mutableStateOf(false) }

    val currentPlant = viewModel.currentPlant
    val context = LocalContext.current

    Scaffold(
        modifier = Modifier
            //.nestedScroll(scrollBehavior.nestedScrollConnection)
            .fillMaxSize()
            .padding(dimensionResource(R.dimen.padding_small)),
        topBar = {
            profileTopBar(plant = currentPlant,
                canNavigateBack = navController.previousBackStackEntry != null,
                navigateUp = {
                    //navController.navigateUp()
                    navController.navigate(PlantMamaHomeDesintation.route)
                })
        },

        content = { padding ->

            Column(
               // modifier = Modifier.verticalScroll(rememberScrollState())
            ) {
                Spacer(modifier = Modifier.size(dimensionResource(R.dimen.padding_medium)))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.4f)
                        .padding(8.dp)
                ) {

                    Box(
                        modifier = Modifier
                            .padding(padding)
                            .fillMaxWidth(0.9f)
                            .fillMaxHeight()
                    )
                    {

                     //   Row(modifier = Modifier) {
                        Card {
                            plantDetailsDisplay(plant = currentPlant)
                        }

                      //  }
                    }

                    Spacer(modifier = Modifier.size(dimensionResource(R.dimen.padding_small)))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(padding)
                            .fillMaxHeight()
                    )
                    {
                        Card ( colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                        )) {
                            Column {
                                // Text("Add/Edit", style = MaterialTheme.typography.headlineSmall)
                                IconButton(onClick = {
                                    navController.navigate(CameraStartDestination.route)
                                }) {
                                    Icon(painter = painterResource(id = R.drawable.baseline_camera_alt_24), contentDescription = "camera")
                                }
                                IconButton(onClick = {
                                    showAddReminderDialog = true
                                }) {
                                    Icon(painter = painterResource(id = R.drawable.baseline_timer_24), contentDescription = "Alarm")
                                }
                                IconButton(onClick = { showEditPlantDialog = true }) {
                                    Icon(painter = painterResource(id = R.drawable.baseline_edit_24), contentDescription = "edit")
                                }
                            }

                        }



                      //  Row() {
                        /*
                            IconDisplay(
                                cameraOnClick = {navController.navigate(PlantScreen.StartCamera.name)},
                                alarmOnClick = {showAddReminderDialog = true},
                                editOnClick = {showEditPlantDialog = true}
                            )

                         */
                       // }
                    }

                }
               // Spacer(modifier = Modifier.size(dimensionResource(R.dimen.padding_medium)))

                var selectedIndex by remember { mutableStateOf(0) }
                val options = listOf("Photos", "Reminders")
                SegmentedButtons {

                    SegmentedButtonItem(
                        selected = selectedIndex == 0,
                        onClick = { selectedIndex = 0 },
                        label = { Text(text = "Photos") },
                        icon = { Icon(painter = painterResource(id = R.drawable.baseline_camera_alt_24), contentDescription = "camera") }
                    )
                    SegmentedButtonItem(
                        selected = selectedIndex == 1,
                        onClick = { selectedIndex = 1 },
                        label = { Text(text = "Reminders") },
                        icon = { Icon(painter = painterResource(id = R.drawable.baseline_timer_24), contentDescription = "Alarm") }
                    )
                }
                Spacer(modifier = Modifier.size(dimensionResource(R.dimen.padding_medium)))
                if (selectedIndex == 0){
                    PhotoDisplay(onPhotoClick = {navController.navigate(InspectPhotoScreenDestination.route)})
                    //imageListLayout()
                }

                if (selectedIndex == 1){
                   // val reminderList: StateFlow<ReminderListUiState> =  reminderListiewModel.getRemindersForPlant(currentPlant.id)
                    ReminderListScreen(plantId = currentPlant.id)

                }

            }

            if (showAddReminderDialog){
                AddReminder(onDismissRequest = { showAddReminderDialog = false }, onConfirmation = {showAddReminderDialog = false},viewModel = viewModel )
            }

            if (showEditPlantDialog){
                AddPlant(onDismissRequest = { showEditPlantDialog = false }, onConfirmation = {showEditPlantDialog = false}, {navController.navigate(CameraStartDestination.route)}, viewModel2 = viewModel)
            }

        }
    )
}

@Composable
fun profileTopBar(plant: Plant,
                  canNavigateBack: Boolean,
                  navigateUp: () -> Unit,
                  modifier: Modifier = Modifier
){
    CenterAlignedTopAppBar(
        title = { Text(plant.name + "'s Profile") },
        modifier = modifier,
        colors = TopAppBarDefaults.topAppBarColors(MaterialTheme.colorScheme.primaryContainer),
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.nav_add_reminder)
                    )
                }
            }
        },
       actions = {
          // Image(
          //     painter = painterResource(plant.imageResourceId),
          //     modifier = Modifier
          //         .size(dimensionResource(R.dimen.main_image_size)),
          //     contentDescription = null
          // )
           if(!plant.profilePic.equals("")){
               AsyncImage(
                   model = ImageRequest.Builder(LocalContext.current)
                       .data(plant.profilePic)
                       .placeholder(R.drawable.plant_logo)
                       .build(),
                   contentDescription = "",
                   modifier = Modifier
                       .padding(2.dp)
                       .size(dimensionResource(R.dimen.main_image_size)),
                   //.clip(CircleShape),
                   contentScale = ContentScale.Fit,
               )
           }

           else{
               val painter = painterResource(R.drawable.plant_logo)

               val description = plant.name
               val title = plant.name
               Image(painter = painter, contentDescription = description, modifier = Modifier.size(dimensionResource(R.dimen.main_image_size)))
               //ImageCard(painter = painter, contentDescription = description, title = title, modifier = Modifier.size(dimensionResource(R.dimen.main_image_size)))

           }

       } ,
    )
}

@Composable
fun plantDetailsDisplay(plant: Plant){
   // Row {
        Column(modifier = Modifier.padding(6.dp)){

            Row(modifier = Modifier) {
                Text(text = "Name")
                Spacer(modifier = Modifier.weight(1f))
                Text(text = plant.name, fontWeight = FontWeight.Bold)

            }
            Row(modifier = Modifier) {
                Text(text = "Age")
                Spacer(modifier = Modifier.weight(1f))
                Text(text = plant.age.toString(), fontWeight = FontWeight.Bold)

            }
            Row(modifier = Modifier) {
                Text(text = "Type")
                Spacer(modifier = Modifier.weight(1f))
                Text(text = plant.type, fontWeight = FontWeight.Bold)

            }
            Row(modifier = Modifier) {
                Text(text = "Description")
                Spacer(modifier = Modifier.weight(1f))
                Text(text = plant.description, fontWeight = FontWeight.Bold)

            }


            /*
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                ){
                Text("Name: ", style = MaterialTheme.typography.headlineSmall,modifier = Modifier.defaultMinSize(minWidth =135.dp))
                Text(plant.name, style = MaterialTheme.typography.titleLarge)
            }

            Row (horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically){
                Text("Age: ", style = MaterialTheme.typography.headlineSmall,modifier = Modifier.defaultMinSize(minWidth =135.dp))
                Text(plant.age.toString(),style = MaterialTheme.typography.titleLarge)
            }
            Row (horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically) {
                Text("Type: ", style = MaterialTheme.typography.headlineSmall,modifier = Modifier.defaultMinSize(minWidth =135.dp))
                Text(plant.type,style = MaterialTheme.typography.titleLarge)
            }
            Row (horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically) {
                Text("Description: ", style = MaterialTheme.typography.headlineSmall,modifier = Modifier.defaultMinSize(minWidth =135.dp))
            }

            Text(plant.description,style = MaterialTheme.typography.titleLarge)

             */

        }
    //}
}

@Composable
fun IconDisplay(cameraOnClick: () -> Unit,
                alarmOnClick: () -> Unit,
                editOnClick: () -> Unit){
    Column {
        Text("Add/Edit", style = MaterialTheme.typography.headlineSmall)
        IconButton(onClick = {
            cameraOnClick
        }) {
            Icon(painter = painterResource(id = R.drawable.baseline_camera_alt_24), contentDescription = "camera")
        }
        IconButton(onClick = {
            alarmOnClick
        }) {
            Icon(painter = painterResource(id = R.drawable.baseline_timer_24), contentDescription = "Alarm")
        }
        IconButton(onClick = { editOnClick }) {
            Icon(painter = painterResource(id = R.drawable.baseline_edit_24), contentDescription = "edit")
        }
    }
}



@Composable
fun imageListLayout(){

    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 128.dp)
    ) {
        items(items = photo2s) {
            imageDisplay(photo2 = it)
        }
    }

}


@Composable
fun imageDisplay(photo2: Photo2){
    Image(
        painter = painterResource(photo2.imageResourceId),
        contentDescription = null
    )
}

@Composable
fun reminderListLayout(){

}


//Segmented Button Implementation from this website: https://medium.com/@fergus.a.hewson/segmented-buttons-compose-material-3-8d64e94989e1
/**
 * Segmented buttons implemented similar to M3 spec. Use for simple choices between two to five items.
 * Each button contains a label and an icon.
 *
 * @param modifier The modifier to be applied to these SegmentedButtons.
 * @param shape The shape of the SegmentedButtons.
 * @param colors The colors to style these SegmentedButtons.
 * @param outlineThickness The thickness of the outline and divider for these Segmented Buttons.
 * @param border The border stroke for the outline of these Segmented Buttons.
 * @param content The content of the SegmentedButtons, usually 3-5 [SegmentedButtonItem].
 */
@Composable
fun SegmentedButtons(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(percent = 50),
    colors: SegmentedButtonColors = SegmentedButtonsDefaults.colors(),
    outlineThickness: Dp = SegmentedButtonsDefaults.outlineThickness,
    border: BorderStroke = BorderStroke(outlineThickness, colors.outlineColor),
    content: @Composable () -> Unit
) {
    Surface(
        shape = shape,
        border = border,
        modifier = modifier.defaultMinSize(minHeight = minimumHeight)
    ) {
        SubcomposeLayout(Modifier.fillMaxWidth()) { constraints ->
            val bottonRowWidth = constraints.maxWidth
            val buttonMeasurables = subcompose(ButtonSlots.Buttons, content)
            val buttonCount = buttonMeasurables.size
            val dividerCount = buttonMeasurables.size - 1

            val outlineThicknessPx = outlineThickness.roundToPx()

            var buttonWidth = 0
            if (buttonCount > 0) {
                buttonWidth = (bottonRowWidth / buttonCount)
            }
            val buttonRowHeight =
                buttonMeasurables.fold(initial = minimumHeight.roundToPx()) { max, curr ->
                    maxOf(curr.maxIntrinsicHeight(buttonWidth), max)
                }

            val buttonPlaceables = buttonMeasurables.map {
                it.measure(
                    constraints.copy(
                        minWidth = buttonWidth,
                        maxWidth = buttonWidth,
                        minHeight = buttonRowHeight,
                        maxHeight = buttonRowHeight,
                    )
                )
            }
            val dividers = @Composable {
                repeat(dividerCount) {
                    colors.SegmentedDivider()
                }
            }
            val dividerPlaceables =
                subcompose(ButtonSlots.Divider, dividers).map {
                    it.measure(
                        constraints.copy(
                            minWidth = outlineThicknessPx,
                            maxWidth = outlineThicknessPx,
                            minHeight = buttonRowHeight - outlineThicknessPx * 2,
                            maxHeight = buttonRowHeight - outlineThicknessPx * 2,
                        )
                    )
                }

            layout(bottonRowWidth, buttonRowHeight) {
                buttonPlaceables.forEachIndexed { index, button ->
                    if (index < dividerPlaceables.size) {
                        dividerPlaceables[index].placeRelative(
                            index * buttonWidth + buttonWidth,
                            outlineThicknessPx,
                            1f
                        )
                    }
                    button.placeRelative(index * buttonWidth, 0, 0f)
                }
            }
        }
    }
}

/**
 * Material Design Segmented Button item. Use for simple choices between two to five items.
 *
 * @param selected Whether this item is selected.
 * @param onClick Called when this item is clicked.
 * @param modifier The modifier to apply to this item.
 * @param label Optional label for this item.
 * @param icon Optional icon for this item.
 * @param colors Colors to style this item.
 * @param textStyle Text style to be applied to the label of this item.
 */
@Composable
fun SegmentedButtonItem(
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    label: @Composable (() -> Unit)? = null,
    icon: @Composable (() -> Unit)? = null,
    colors: SegmentedButtonColors = SegmentedButtonsDefaults.colors(),
    textStyle: TextStyle = MaterialTheme.typography.labelLarge
) {
    val styledLabel: @Composable (() -> Unit)? =
        label?.let {
            @Composable {
                val textColor by colors.textColor(selected = selected)
                CompositionLocalProvider(LocalContentColor provides textColor) {
                    ProvideTextStyle(textStyle, content = label)
                }
            }
        }

    val styledIcon: @Composable (() -> Unit)? =
        icon?.let {
            @Composable {
                val iconColor by colors.iconColor(selected = selected)
                val clearSemantics = label != null && selected
                Box(modifier = if (clearSemantics) Modifier.clearAndSetSemantics {} else Modifier) {
                    CompositionLocalProvider(LocalContentColor provides iconColor, content = icon)
                }
            }
        }

    val animationProgress: Float by animateFloatAsState(
        targetValue = if (selected) colors.indicatorColor.alpha else 0f,
        animationSpec = tween(ITEM_ANIMATION_MILLIS), label = "SegmentedButton"
    )

    Box(
        modifier
            .fillMaxSize()
            .selectable(
                selected = selected,
                onClick = onClick,
                role = Role.Tab,
            )
            .background(
                color = colors.indicatorColor.copy(alpha = animationProgress),
            )
            .padding(12.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            styledIcon?.invoke()
            styledLabel?.invoke()
        }
    }
}

object SegmentedButtonsDefaults {

    @Composable
    fun colors(
        selectedTextColor: Color = MaterialTheme.colorScheme.primary,
        selectedIconColor: Color = MaterialTheme.colorScheme.primary,
        unselectedTextColor: Color = MaterialTheme.colorScheme.onSurface,
        unselectedIconColor: Color = MaterialTheme.colorScheme.onSurface,
        indicatorColor: Color = MaterialTheme.colorScheme.secondaryContainer,
        outlineColor: Color = MaterialTheme.colorScheme.outlineVariant
    ): SegmentedButtonColors = SegmentedButtonColors(
        selectedTextColor = selectedTextColor,
        selectedIconColor = selectedIconColor,
        indicatorColor = indicatorColor,
        unselectedTextColor = unselectedTextColor,
        unselectedIconColor = unselectedIconColor,
        outlineColor = outlineColor
    )

    internal val outlineThickness: Dp = 1.dp
    internal val minimumHeight: Dp = 48.dp
    internal const val ITEM_ANIMATION_MILLIS: Int = 100
}

@Stable
data class SegmentedButtonColors internal constructor(
    val selectedTextColor: Color,
    val selectedIconColor: Color,
    val unselectedTextColor: Color,
    val unselectedIconColor: Color,
    val indicatorColor: Color,
    val outlineColor: Color
) {
    @Composable
    internal fun textColor(selected: Boolean): State<Color> {
        val targetValue = when {
            selected -> selectedTextColor
            else -> unselectedTextColor
        }
        return animateColorAsState(
            targetValue = targetValue,
            animationSpec = tween(ITEM_ANIMATION_MILLIS),
            label = "SegmentedButtonsTextColor"
        )
    }

    @Composable
    fun iconColor(selected: Boolean): State<Color> {
        val targetValue = when {
            selected -> selectedIconColor
            else -> unselectedIconColor
        }
        return animateColorAsState(
            targetValue = targetValue,
            animationSpec = tween(ITEM_ANIMATION_MILLIS),
            label = "SegmentedButtonsIconColor"
        )
    }

    @Composable
    fun SegmentedDivider(
        modifier: Modifier = Modifier,
        thickness: Dp = outlineThickness,
        color: Color = outlineColor
    ) {
        Box(
            modifier
                .fillMaxHeight()
                .width(thickness)
                .background(color = color)
        )
    }
}

private enum class ButtonSlots {
    Buttons,
    Divider,
}
/*
@Preview
@Composable
fun PreviewPlantDetails(){
    PLantMamaTheme {
        plantDetailsDisplay(plants[0])
    }
}

 */

/*
@Preview
@Composable
fun ProfileScreenPreview(
    viewModel: PlantMamaMainScreenViewModel = viewModel(),
    navController: NavHostController = rememberNavController()
){
    PLantMamaTheme {
        PlantProfleMain(viewModel = viewModel, navController = navController)
    }
}


 */
