package com.stadiamaps.ferrostar.composeui.views.components.maneuver

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.stadiamaps.ferrostar.composeui.R
import uniffi.ferrostar.ManeuverModifier
import uniffi.ferrostar.ManeuverType
import uniffi.ferrostar.VisualInstructionContent

val VisualInstructionContent.maneuverIcon: String
  get() {
    val descriptor =
        listOfNotNull(
                maneuverType?.name?.replace(" ", "_"), maneuverModifier?.name?.replace(" ", "_"))
            .joinToString(separator = "_")
    return "direction_${descriptor}".lowercase()
  }

/**
 * Determines if exit numbers should be displayed for the given maneuver type. Exit numbers are
 * shown for roundabouts and rotaries.
 */
private fun shouldShowExitNumber(content: VisualInstructionContent): Boolean {
  return when (content.maneuverType) {
    ManeuverType.ROUNDABOUT,
    ManeuverType.ROTARY,
    ManeuverType.EXIT_ROUNDABOUT,
    ManeuverType.EXIT_ROTARY -> true
    else -> false
  }
}

/** An icon view using the public domain drawables from Mapbox. */
@SuppressLint("DiscouragedApi")
@Composable
fun ManeuverImage(content: VisualInstructionContent, tint: Color = LocalContentColor.current) {
  val context = LocalContext.current
  val resourceId =
      context.resources.getIdentifier(content.maneuverIcon, "drawable", context.packageName)

  if (resourceId != 0) {
    Box {
      Icon(
          painter = painterResource(id = resourceId),
          contentDescription = stringResource(id = R.string.maneuver_image),
          tint = tint,
          modifier = Modifier.size(64.dp))

      // Show exit number overlay for roundabouts/rotaries
      if (shouldShowExitNumber(content) && content.roundaboutExit != null) {
        Box(
            modifier =
                Modifier.align(Alignment.TopEnd)
                    .background(color = MaterialTheme.colorScheme.surface, shape = CircleShape)
                    .padding(horizontal = 6.dp, vertical = 2.dp)) {
              Text(
                  text = content.roundaboutExit.toString(),
                  fontSize = 10.sp,
                  fontWeight = FontWeight.Bold,
                  color = MaterialTheme.colorScheme.onSurface)
            }
      }
    }
  } else {
    // Ignore resolution failures for the moment.
  }
}

@Preview
@Composable
fun ManeuverImageLeftTurnPreview() {
  ManeuverImage(
      VisualInstructionContent(
          text = "",
          maneuverType = ManeuverType.TURN,
          maneuverModifier = ManeuverModifier.LEFT,
          roundaboutExitDegrees = null,
          laneInfo = null,
          exitNumbers = emptyList(),
          roundaboutExit = null))
}

@Preview
@Composable
fun ManeuverImageContinueUturnPreview() {
  ManeuverImage(
      VisualInstructionContent(
          text = "",
          maneuverType = ManeuverType.CONTINUE,
          maneuverModifier = ManeuverModifier.U_TURN,
          roundaboutExitDegrees = null,
          laneInfo = null,
          exitNumbers = emptyList(),
          roundaboutExit = null))
}

@Preview
@Composable
fun ManeuverImageRoundaboutPreview() {
  ManeuverImage(
      VisualInstructionContent(
          text = "",
          maneuverType = ManeuverType.ROUNDABOUT,
          maneuverModifier = ManeuverModifier.STRAIGHT,
          roundaboutExitDegrees = null,
          laneInfo = null,
          exitNumbers = emptyList(),
          roundaboutExit = 3u))
}
