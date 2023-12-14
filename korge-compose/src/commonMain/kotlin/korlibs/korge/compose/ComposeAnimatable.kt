package korlibs.korge.compose

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import korlibs.image.color.RGBA
import korlibs.image.color.interpolate
import korlibs.io.async.delay
import korlibs.math.interpolation.Easing
import korlibs.math.interpolation.interpolate
import korlibs.math.interpolation.toRatio
import korlibs.time.TimeSpan
import korlibs.time.milliseconds
import korlibs.time.seconds

fun Animatable(color: RGBA) = Animatable(color, interpolator = { l, r -> this.toRatio().interpolate(l, r) })
fun Animatable(value: Double) = Animatable(value, interpolator = { l, r -> this.toRatio().interpolate(l, r) })

class Animatable<T>(initialValue: T, val interpolator: Float.(start: T, end: T) -> T) {
    var value: T by mutableStateOf(initialValue)
        internal set
    suspend fun animateTo(end: T, time: TimeSpan = 0.5.seconds, easing: Easing = Easing.EASE_IN_OUT) {
        val start = this.value
        var elapsed = 0.milliseconds
        while (true) {
            val ratio = (elapsed / time).toDouble().coerceIn(0.0, 1.0)
            value = interpolator(easing(ratio.toFloat()), start, end)
            //println("ratio=$ratio, value=$value")
            kotlinx.coroutines.delay(10.milliseconds)
            elapsed += 10.milliseconds
            if (ratio >= 1.0) break
        }
    }

    //operator fun getValue(t: Any, property: KProperty<*>): T {
    //    return value
    //}
    //operator fun setValue(t: Any, property: KProperty<*>, value: T) {
    //    this.value = value
    //}
}
